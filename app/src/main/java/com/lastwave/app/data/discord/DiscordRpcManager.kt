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
import org.json.JSONObject
import java.io.InputStream
import java.net.InetAddress
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
) {
    private var rpcJob: Job? = null
    private var socket: Socket? = null
    private var isConnected = false
    private val clientId = "1345000000000000000"

    fun start(playerStateFlow: StateFlow<MusicPlayerState>) {
        rpcJob?.cancel()
        rpcJob = applicationScope.launch(Dispatchers.IO) {
            settingsPreferences.settings.collectLatest { settings ->
                if (settings.discordRpcEnabled) {
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
                            updatePresence(activity)
                        } else {
                            clearPresence()
                        }
                    }
                } else {
                    clearPresence()
                    disconnect()
                }
            }
        }
    }

    private suspend fun updatePresence(activity: DiscordActivity) = withContext(Dispatchers.IO) {
        runCatching {
            ensureConnected()
            if (!isConnected) return@withContext

            val now = System.currentTimeMillis()
            val startTimestamp = if (activity.isPlaying) now - activity.positionMs else null
            val endTimestamp = if (activity.isPlaying && activity.durationMs > 0) {
                now + (activity.durationMs - activity.positionMs)
            } else null

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
                    put("large_text", activity.album ?: "LastWave Music")
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

            sendFrame(1, payloadJson.toString())
        }.onFailure { e ->
            Log.d("DiscordRpc", "RPC update skipped/failed: ${e.message}")
            disconnect()
        }
    }

    private suspend fun clearPresence() = withContext(Dispatchers.IO) {
        if (!isConnected) return@withContext
        runCatching {
            val payloadJson = JSONObject().apply {
                put("cmd", "SET_ACTIVITY")
                put("args", JSONObject().apply {
                    put("pid", android.os.Process.myPid())
                    put("activity", JSONObject.NULL)
                })
                put("nonce", UUID.randomUUID().toString())
            }
            sendFrame(1, payloadJson.toString())
        }.onFailure { disconnect() }
    }

    private fun ensureConnected() {
        if (isConnected && socket?.isConnected == true && !socket!!.isClosed) return
        disconnect()

        for (port in 6463..6472) {
            try {
                val s = Socket(InetAddress.getByName("127.0.0.1"), port)
                s.soTimeout = 3000
                socket = s

                val handshakeJson = JSONObject().apply {
                    put("v", 1)
                    put("client_id", clientId)
                }
                sendFrameOnSocket(s, 0, handshakeJson.toString())
                readFrameFromSocket(s)
                isConnected = true
                Log.d("DiscordRpc", "Connected to Discord RPC on port $port")
                break
            } catch (_: Exception) {
                disconnect()
            }
        }
    }

    private fun sendFrame(opcode: Int, jsonText: String) {
        val s = socket ?: return
        sendFrameOnSocket(s, opcode, jsonText)
    }

    private fun sendFrameOnSocket(s: Socket, opcode: Int, jsonText: String) {
        val bytes = jsonText.toByteArray(Charsets.UTF_8)
        val buffer = ByteBuffer.allocate(8 + bytes.size).order(ByteOrder.LITTLE_ENDIAN)
        buffer.putInt(opcode)
        buffer.putInt(bytes.size)
        buffer.put(bytes)
        s.getOutputStream().write(buffer.array())
        s.getOutputStream().flush()
    }

    private fun readFrameFromSocket(s: Socket): String {
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

    private fun disconnect() {
        runCatching { socket?.close() }
        socket = null
        isConnected = false
    }
}
