package com.lastwave.app.data.canvas

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import javax.inject.Inject
import javax.inject.Singleton

data class CanvasVideoLoop(
    val trackId: String,
    val canvasUrl: String,
)

@Singleton
class CanvasRepository @Inject constructor(
    private val okHttpClient: OkHttpClient,
) {
    suspend fun fetchCanvasLoop(trackTitle: String, artistName: String): CanvasVideoLoop? = withContext(Dispatchers.IO) {
        val queryUrl = "https://canvas.spotify.com/api/v1/canvas?artist=${android.net.Uri.encode(artistName)}&track=${android.net.Uri.encode(trackTitle)}"
        val request = Request.Builder().url(queryUrl).build()

        runCatching {
            okHttpClient.newCall(request).execute().use { response ->
                if (!response.isSuccessful) return@withContext null
                val body = response.body?.string().orEmpty()
                val json = JSONObject(body)
                val canvasUrl = json.optString("canvas_url")
                if (!canvasUrl.isNullOrBlank()) {
                    CanvasVideoLoop(trackTitle, canvasUrl)
                } else null
            }
        }.getOrNull()
    }
}
