package com.lastwave.app.data.scrobble

import com.lastwave.app.playback.PlayableTrack
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ListenBrainzManager @Inject constructor(
    private val okHttpClient: OkHttpClient,
) {
    suspend fun submitListen(token: String, track: PlayableTrack): Boolean = withContext(Dispatchers.IO) {
        if (token.isBlank()) return@withContext false
        val url = "https://api.listenbrainz.org/1/submit-listens"
        val payload = JSONObject().apply {
            put("listen_type", "single")
            put("payload", JSONArray().apply {
                put(JSONObject().apply {
                    put("track_metadata", JSONObject().apply {
                        put("artist_name", track.artist)
                        put("track_name", track.title)
                        put("release_name", track.album ?: "Ananta Dhvani Stream")
                    })
                })
            })
        }

        val request = Request.Builder()
            .url(url)
            .addHeader("Authorization", "Token $token")
            .post(payload.toString().toRequestBody("application/json".toMediaType()))
            .build()

        runCatching {
            okHttpClient.newCall(request).execute().use { it.isSuccessful }
        }.getOrDefault(false)
    }
}
