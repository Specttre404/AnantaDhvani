package com.lastwave.app.data.discord

import android.util.Log
import com.lastwave.app.data.local.SettingsPreferences
import com.lastwave.app.playback.MusicPlayerState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import org.json.JSONArray
import org.json.JSONObject
import java.io.InputStream
import java.net.InetSocketAddress
import java.net.Socket
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

data class DiscordActivity(
    val title: String,
    val artist: String,
    val album: String?,
    val artworkUrl: String?,
    val isPlaying: Boolean,
    val positionMs: Long,
    val durationMs: Long,
)

@Singleton
class DiscordRpcManager @Inject constructor(
    private val settingsPreferences: SettingsPreferences,
    private val applicationScope: CoroutineScope,
    private val okHttpClient: OkHttpClient,
) {
    private var rpcJob: Job? = null
    private var webSocket: WebSocket? = null
    private var localSocket: Socket? = null
    private var isGatewayConnected = false
    private var isLocalConnected = false
    private var lastConnectionAttemptTime = 0L
    private val clientId = "1345000000000000000"
    private var lastActivity: DiscordActivity? = null

    fun start(playerStateFlow: StateFlow<MusicPlayerState>) {
        rpcJob?.cancel()
        rpcJob = applicationScope.launch(Dispatchers.IO) {
            settingsPreferences.settings.collectLatest { settings ->
                if (settings.discordRpcEnabled) {
                    if (settings.discordUserToken.isNotBlank() && !isGatewayConnected) {
                        connectGateway(settings.discordUserToken)
                    }
                    playerStateFlow.collectLatest { playerState ->
                        if (playerState.current != null) {
                            val activity = DiscordActivity(
                                title = playerState.current.title,
                                artist = playerState.current.artist,
                                album = playerState.current.album,
                                artworkUrl = playerState.current.artworkUrl,
                                isPlaying = playerState.isPlaying,
                                positionMs = playerState.positionMs,
                                durationMs = playerState.durationMs,
                            )
                            lastActivity = activity
                            updatePresence(activity)
                        } else {
                            lastActivity = null
                            clearPresence()
                        }
                    }
                } else {
                    lastActivity = null
                    clearPresence()
                    disconnect()
                }
            }
        }
    }

    private fun connectGateway(token: String) {
        disconnectGateway()
        val request = Request.Builder()
            .url("wss://gateway.discord.gg/?v=10&encoding=json")
            .build()

        webSocket = okHttpClient.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                Log.d("DiscordRpc", "Gateway WebSocket opened")
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                runCatching {
                    val json = JSONObject(text)
                    val op = json.optInt("op")
                    val t = json.optString("t")

                    if (op == 10) {
                        val identify = JSONObject().apply {
                            put("op", 2)
                            put("d", JSONObject().apply {
                                put("token", token)
                                put("properties", JSONObject().apply {
                                    put("os", "android")
                                    put("browser", "LastWaveX")
                                    put("device", "LastWaveX Mobile")
                                })
                            })
                        }
                        webSocket.send(identify.toString())
                    } else if (t == "READY") {
                        isGatewayConnected = true
                        val d = json.optJSONObject("d")
                        val user = d?.optJSONObject("user")
                        val username = user?.optString("username")
                        if (!username.isNullOrBlank()) {
                            applicationScope.launch {
                                settingsPreferences.setDiscordConnectedUsername(username)
                            }
                        }
                        lastActivity?.let { sendGatewayPresence(webSocket, it) }
                    }
                }.onFailure { Log.d("DiscordRpc", "Gateway message error: ${it.message}") }
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                Log.d("DiscordRpc", "Gateway WebSocket failure: ${t.message}")
                isGatewayConnected = false
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                isGatewayConnected = false
            }
        })
    }

    private suspend fun updatePresence(activity: DiscordActivity) = withContext(Dispatchers.IO) {
        val ws = webSocket
        if (isGatewayConnected && ws != null) {
            sendGatewayPresence(ws, activity)
            return@withContext
        }

        runCatching {
            ensureLocalConnected()
            if (!isLocalConnected) return@withContext

            val now = System.currentTimeMillis()
            val startTimestamp = if (activity.isPlaying) now - activity.positionMs else null
            val endTimestamp = if (activity.isPlaying && activity.durationMs > 0) now + (activity.durationMs - activity.positionMs) else null

            val activityJson = JSONObject().apply {
                put("details", activity.title)
                put("state", activity.artist)
                put("type", 2)

                val timestampsJson = JSONObject()
                if (startTimestamp != null) timestampsJson.put("start", startTimestamp)
                if (endTimestamp != null) timestampsJson.put("end", endTimestamp)
                if (timestampsJson.length() > 0) put("timestamps", timestampsJson)

                val assetsJson = JSONObject().apply {
                    if (!activity.artworkUrl.isNullOrBlank()) put("large_image", activity.artworkUrl)
                    put("large_text", activity.album ?: "LastWaveX Music")
                    put("small_image", if (activity.isPlaying) "play" else "pause")
                    put("small_text", if (activity.isPlaying) "Playing" else "Paused")
                }
                put("assets", assetsJson)
            }

            val payloadJson = JSONObject().apply {
                put("cmd", "SET_ACTIVITY")
                put("args", JSONObject().apply {
                    put("pid", android.os.Process.myPid())
                    put("activity", activityJson)
                })
                put("nonce", UUID.randomUUID().toString())
            }

            sendLocalFrame(1, payloadJson.toString())
        }.onFailure {
            disconnectLocal()
        }
    }

    private fun sendGatewayPresence(ws: WebSocket, activity: DiscordActivity) {
        runCatching {
            val now = System.currentTimeMillis()
            val startTimestamp = if (activity.isPlaying) now - activity.positionMs else null
            val endTimestamp = if (activity.isPlaying && activity.durationMs > 0) now + (activity.durationMs - activity.positionMs) else null

            val activityJson = JSONObject().apply {
                put("name", "LastWaveX")
                put("details", activity.title)
                put("state", activity.artist)
                put("type", 2)

                val timestampsJson = JSONObject()
                if (startTimestamp != null) timestampsJson.put("start", startTimestamp)
                if (endTimestamp != null) timestampsJson.put("end", endTimestamp)
                if (timestampsJson.length() > 0) put("timestamps", timestampsJson)

                val assetsJson = JSONObject().apply {
                    if (!activity.artworkUrl.isNullOrBlank()) put("large_image", activity.artworkUrl)
                    put("large_text", activity.album ?: "LastWaveX Music")
                }
                put("assets", assetsJson)
            }

            val presenceJson = JSONObject().apply {
                put("op", 3)
                put("d", JSONObject().apply {
                    put("since", now)
                    put("activities", JSONArray().put(activityJson))
                    put("status", "online")
                    put("afk", false)
                })
            }

            ws.send(presenceJson.toString())
        }
    }

    private suspend fun clearPresence() = withContext(Dispatchers.IO) {
        val ws = webSocket
        if (isGatewayConnected && ws != null) {
            val presenceJson = JSONObject().apply {
                put("op", 3)
                put("d", JSONObject().apply {
                    put("since", System.currentTimeMillis())
                    put("activities", JSONArray())
                    put("status", "online")
                    put("afk", false)
                })
            }
            ws.send(presenceJson.toString())
            return@withContext
        }

        if (!isLocalConnected) return@withContext
        runCatching {
            val payloadJson = JSONObject().apply {
                put("cmd", "SET_ACTIVITY")
                put("args", JSONObject().apply {
                    put("pid", android.os.Process.myPid())
                    put("activity", JSONObject.NULL)
                })
                put("nonce", UUID.randomUUID().toString())
            }
            sendLocalFrame(1, payloadJson.toString())
        }.onFailure { disconnectLocal() }
    }

    private fun ensureLocalConnected() {
        if (isLocalConnected && localSocket?.isConnected == true && !localSocket!!.isClosed) return
        disconnectLocal()

        val now = System.currentTimeMillis()
        if (now - lastConnectionAttemptTime < 60_000L) return
        lastConnectionAttemptTime = now

        for (port in 6463..6472) {
            try {
                val s = Socket()
                s.connect(InetSocketAddress("127.0.0.1", port), 250)
                s.soTimeout = 1000
                localSocket = s

                val handshakeJson = JSONObject().apply {
                    put("v", 1)
                    put("client_id", clientId)
                }
                sendLocalFrameOnSocket(s, 0, handshakeJson.toString())
                readLocalFrameFromSocket(s)
                isLocalConnected = true
                Log.d("DiscordRpc", "Connected to local Discord RPC on port $port")
                break
            } catch (_: Exception) {
                disconnectLocal()
            }
        }
    }

    private fun sendLocalFrame(opcode: Int, jsonText: String) {
        val s = localSocket ?: return
        sendLocalFrameOnSocket(s, opcode, jsonText)
    }

    private fun sendLocalFrameOnSocket(s: Socket, opcode: Int, jsonText: String) {
        val bytes = jsonText.toByteArray(Charsets.UTF_8)
        val buffer = ByteBuffer.allocate(8 + bytes.size).order(ByteOrder.LITTLE_ENDIAN)
        buffer.putInt(opcode)
        buffer.putInt(bytes.size)
        buffer.put(bytes)
        s.getOutputStream().write(buffer.array())
        s.getOutputStream().flush()
    }

    private fun readLocalFrameFromSocket(s: Socket): String {
        val input = s.getInputStream()
        val header = ByteArray(8)
        readFully(input, header)
        val buffer = ByteBuffer.wrap(header).order(ByteOrder.LITTLE_ENDIAN)
        val opcode = buffer.int
        val length = buffer.int
        val payload = ByteArray(length)
        readFully(input, payload)
        return String(payload, Charsets.UTF_8)
    }

    private fun readFully(input: InputStream, target: ByteArray) {
        var count = 0
        while (count < target.size) {
            val read = input.read(target, count, target.size - count)
            if (read < 0) throw java.io.EOFException("Unexpected end of RPC stream")
            count += read
        }
    }

    private fun disconnectGateway() {
        runCatching { webSocket?.close(1000, "Disconnecting") }
        webSocket = null
        isGatewayConnected = false
    }

    private fun disconnectLocal() {
        runCatching { localSocket?.close() }
        localSocket = null
        isLocalConnected = false
    }

    private fun disconnect() {
        disconnectGateway()
        disconnectLocal()
    }
}
