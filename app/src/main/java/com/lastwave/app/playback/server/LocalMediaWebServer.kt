package com.lastwave.app.playback.server

import android.content.Context
import android.net.wifi.WifiManager
import android.util.Log
import com.lastwave.app.playback.MusicPlayer
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStream
import java.net.InetAddress
import java.net.NetworkInterface
import java.net.ServerSocket
import java.net.Socket
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalMediaWebServer @Inject constructor(
    @ApplicationContext private val context: Context,
    private val musicPlayer: MusicPlayer,
) {
    @Volatile
    var isRunning = false
        private set

    private var serverSocket: ServerSocket? = null
    private var serverJob: Job? = null
    private val scope = CoroutineScope(Dispatchers.IO)

    init {
        instance = this
    }

    fun startServer(): Boolean {
        if (isRunning) return true
        return try {
            serverSocket = ServerSocket(8080)
            isRunning = true
            serverJob = scope.launch {
                while (isRunning) {
                    try {
                        val client = serverSocket?.accept() ?: break
                        launch { handleClient(client) }
                    } catch (e: Exception) {
                        if (!isRunning) break
                    }
                }
            }
            Log.i("LocalMediaWebServer", "Server started at ${getLocalServerUrl()}")
            true
        } catch (e: Exception) {
            Log.e("LocalMediaWebServer", "Failed to start server on port 8080", e)
            isRunning = false
            false
        }
    }

    fun stopServer() {
        isRunning = false
        runCatching { serverSocket?.close() }
        serverJob?.cancel()
        serverSocket = null
        serverJob = null
        Log.i("LocalMediaWebServer", "Server stopped")
    }

    fun getLocalIpAddress(): String {
        val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as? WifiManager
        val ipInt = wifiManager?.connectionInfo?.ipAddress ?: 0
        if (ipInt != 0) {
            return String.format(
                "%d.%d.%d.%d",
                ipInt and 0xff,
                ipInt shr 8 and 0xff,
                ipInt shr 16 and 0xff,
                ipInt shr 24 and 0xff
            )
        }
        try {
            val interfaces = NetworkInterface.getNetworkInterfaces()
            while (interfaces.hasMoreElements()) {
                val intf = interfaces.nextElement()
                val addrs = intf.inetAddresses
                while (addrs.hasMoreElements()) {
                    val addr = addrs.nextElement()
                    if (!addr.isLoopbackAddress && addr is InetAddress) {
                        val sAddr = addr.hostAddress
                        if (sAddr != null && !sAddr.contains(":")) {
                            return sAddr
                        }
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("LocalMediaWebServer", "Error getting IP", e)
        }
        return "127.0.0.1"
    }

    fun getLocalServerUrl(): String = "http://${getLocalIpAddress()}:8080"

    private suspend fun handleClient(socket: Socket) = withContext(Dispatchers.IO) {
        try {
            socket.use { client ->
                val reader = BufferedReader(InputStreamReader(client.getInputStream()))
                val out = client.getOutputStream()
                val requestLine = reader.readLine() ?: return@use

                val parts = requestLine.split(" ")
                if (parts.size < 2) return@use
                val method = parts[0]
                val pathAndQuery = parts[1]
                val path = pathAndQuery.substringBefore("?")

                when {
                    path == "/" || path == "/index.html" -> serveWebUi(out)
                    path == "/api/status" -> serveStatusJson(out)
                    path == "/api/playpause" -> {
                        musicPlayer.togglePlayPause()
                        serveJsonResponse(out, "{\"status\": \"ok\"}")
                    }
                    path == "/api/next" -> {
                        musicPlayer.next()
                        serveJsonResponse(out, "{\"status\": \"ok\"}")
                    }
                    path == "/api/prev" -> {
                        musicPlayer.previous()
                        serveJsonResponse(out, "{\"status\": \"ok\"}")
                    }
                    path.startsWith("/api/seek") -> {
                        val posStr = pathAndQuery.substringAfter("pos=", "")
                        val pos = posStr.toLongOrNull() ?: 0L
                        musicPlayer.seekTo(pos)
                        serveJsonResponse(out, "{\"status\": \"ok\"}")
                    }
                    path.startsWith("/stream") -> serveAudioStream(out, pathAndQuery)
                    else -> serveNotFound(out)
                }
            }
        } catch (e: Exception) {
            Log.d("LocalMediaWebServer", "Client handling exception: ${e.message}")
        }
    }

    private fun serveWebUi(out: OutputStream) {
        val html = """
            <!DOCTYPE html>
            <html lang="en">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Ananta Dhvani Web Remote</title>
                <style>
                    body { font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif; background: #121216; color: #f0f0f5; margin: 0; padding: 20px; display: flex; flex-direction: column; align-items: center; justify-content: center; min-height: 90vh; }
                    .card { background: #1e1e24; border-radius: 24px; padding: 28px; max-width: 400px; width: 100%; text-align: center; box-shadow: 0 12px 32px rgba(0,0,0,0.5); }
                    .cover { width: 240px; height: 240px; border-radius: 16px; object-fit: cover; margin-bottom: 20px; box-shadow: 0 8px 24px rgba(0,0,0,0.4); background: #2a2a32; }
                    .title { font-size: 20px; font-weight: 700; margin: 8px 0 4px 0; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
                    .artist { font-size: 14px; color: #a0a0b0; margin-bottom: 16px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
                    .controls { display: flex; align-items: center; justify-content: center; gap: 16px; margin: 20px 0; }
                    button { background: #2e2e38; border: none; color: #fff; border-radius: 50%; width: 52px; height: 52px; font-size: 18px; cursor: pointer; transition: transform 0.1s, background 0.2s; display: flex; align-items: center; justify-content: center; }
                    button:hover { background: #3e3e4a; }
                    button:active { transform: scale(0.92); }
                    .btn-play { width: 64px; height: 64px; background: #bb86fc; color: #000; font-weight: bold; }
                    .btn-play:hover { background: #d7b3ff; }
                    .slider-container { margin: 16px 0; }
                    input[type=range] { width: 100%; accent-color: #bb86fc; }
                    .stream-box { margin-top: 24px; padding-top: 16px; border-top: 1px solid #2e2e38; }
                    audio { width: 100%; margin-top: 8px; }
                </style>
            </head>
            <body>
                <div class="card">
                    <img id="cover" class="cover" src="/artwork" alt="Artwork" onerror="this.src='https://via.placeholder.com/240/2a2a32/ffffff?text=Ananta+Dhvani'" />
                    <div id="title" class="title">Loading...</div>
                    <div id="artist" class="artist">Ananta Dhvani Remote</div>
                    
                    <div class="slider-container">
                        <input type="range" id="seeker" min="0" max="100" value="0" onchange="seek(this.value)">
                    </div>

                    <div class="controls">
                        <button onclick="cmd('prev')">⏮</button>
                        <button class="btn-play" onclick="cmd('playpause')" id="playBtn">⏯</button>
                        <button onclick="cmd('next')">⏭</button>
                    </div>

                    <div class="stream-box">
                        <span style="font-size: 12px; color: #a0a0b0;">PC Live Audio Stream</span>
                        <audio controls src="/stream" preload="none"></audio>
                    </div>
                </div>

                <script>
                    let maxDuration = 100;
                    function update() {
                        fetch('/api/status').then(r => r.json()).then(data => {
                            document.getElementById('title').innerText = data.title || 'Nothing Playing';
                            document.getElementById('artist').innerText = (data.artist || '') + (data.album ? ' • ' + data.album : '');
                            document.getElementById('playBtn').innerText = data.isPlaying ? '⏸' : '▶';
                            if (data.artworkUrl) document.getElementById('cover').src = data.artworkUrl;
                            maxDuration = data.durationMs || 100;
                            document.getElementById('seeker').max = maxDuration;
                            document.getElementById('seeker').value = data.positionMs || 0;
                        }).catch(() => {});
                    }
                    function cmd(action) {
                        fetch('/api/' + action).then(() => setTimeout(update, 200));
                    }
                    function seek(val) {
                        fetch('/api/seek?pos=' + val).then(() => setTimeout(update, 200));
                    }
                    setInterval(update, 1000);
                    update();
                </script>
            </body>
            </html>
        """.trimIndent()

        val response = "HTTP/1.1 200 OK\r\nContent-Type: text/html; charset=utf-8\r\nContent-Length: ${html.toByteArray().size}\r\nConnection: close\r\n\r\n$html"
        out.write(response.toByteArray())
        out.flush()
    }

    private fun serveStatusJson(out: OutputStream) {
        val state = musicPlayer.state.value
        val track = state.current
        val json = JSONObject().apply {
            put("title", track?.title ?: "Unknown Title")
            put("artist", track?.artist ?: "Unknown Artist")
            put("album", track?.album ?: "")
            put("artworkUrl", track?.artworkUrl ?: "")
            put("isPlaying", state.isPlaying)
            put("positionMs", state.positionMs)
            put("durationMs", state.durationMs)
        }.toString()

        serveJsonResponse(out, json)
    }

    private fun serveJsonResponse(out: OutputStream, json: String) {
        val bytes = json.toByteArray()
        val response = "HTTP/1.1 200 OK\r\nContent-Type: application/json; charset=utf-8\r\nContent-Length: ${bytes.size}\r\nAccess-Control-Allow-Origin: *\r\nConnection: close\r\n\r\n"
        out.write(response.toByteArray())
        out.write(bytes)
        out.flush()
    }

    private fun serveAudioStream(out: OutputStream, query: String) {
        val header = "HTTP/1.1 200 OK\r\nContent-Type: audio/mpeg\r\nTransfer-Encoding: chunked\r\nAccess-Control-Allow-Origin: *\r\nConnection: keep-alive\r\n\r\n"
        out.write(header.toByteArray())
        out.flush()
    }

    private fun serveNotFound(out: OutputStream) {
        val body = "404 Not Found"
        val response = "HTTP/1.1 404 Not Found\r\nContent-Type: text/plain\r\nContent-Length: ${body.length}\r\nConnection: close\r\n\r\n$body"
        out.write(response.toByteArray())
        out.flush()
    }

    companion object {
        @Volatile
        var instance: LocalMediaWebServer? = null

        fun getInstance(context: Context, musicPlayer: MusicPlayer): LocalMediaWebServer {
            return instance ?: synchronized(this) {
                instance ?: LocalMediaWebServer(context.applicationContext, musicPlayer).also { instance = it }
            }
        }
    }
}
