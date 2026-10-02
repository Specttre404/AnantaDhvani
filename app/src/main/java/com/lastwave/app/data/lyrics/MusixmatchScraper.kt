package com.lastwave.app.data.lyrics

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MusixmatchScraper @Inject constructor(
    private val okHttpClient: OkHttpClient,
) {
    private var userToken: String? = null

    suspend fun getGuestToken(): String? = withContext(Dispatchers.IO) {
        if (!userToken.isNullOrBlank()) return@withContext userToken
        val url = "https://apic-desktop.musixmatch.com/ws/1.1/token.get?app_id=web-desktop-app-v1.0"
        val request = Request.Builder().url(url).build()

        runCatching {
            okHttpClient.newCall(request).execute().use { response ->
                if (!response.isSuccessful) return@withContext null
                val body = response.body?.string().orEmpty()
                val json = JSONObject(body)
                val token = json.optJSONObject("message")?.optJSONObject("body")?.optString("user_token")
                if (!token.isNullOrBlank()) userToken = token
                token
            }
        }.getOrNull()
    }

    suspend fun fetchLyrics(trackTitle: String, artistName: String): String? = withContext(Dispatchers.IO) {
        val token = getGuestToken() ?: return@withContext null
        val url = "https://apic-desktop.musixmatch.com/ws/1.1/macro.subtitles.get?format=json&q_track=${android.net.Uri.encode(trackTitle)}&q_artist=${android.net.Uri.encode(artistName)}&user_token=$token&app_id=web-desktop-app-v1.0"
        val request = Request.Builder().url(url).build()

        runCatching {
            okHttpClient.newCall(request).execute().use { response ->
                if (!response.isSuccessful) return@withContext null
                val body = response.body?.string().orEmpty()
                val json = JSONObject(body)
                val macro = json.optJSONObject("message")?.optJSONObject("body")?.optJSONObject("macro_calls")
                val subtitle = macro?.optJSONObject("track.subtitles.get")?.optJSONObject("message")?.optJSONObject("body")?.optJSONObject("subtitle")
                subtitle?.optString("subtitle_body")
            }
        }.getOrNull()
    }
}
