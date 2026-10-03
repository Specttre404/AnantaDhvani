package com.lastwave.app.data.jiosaavn

import com.lastwave.app.playback.PlayableTrack
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class JioSaavnApi @Inject constructor(
    private val okHttpClient: OkHttpClient,
) {
    private val desKey = "38346591".toByteArray()

    suspend fun searchTracks(query: String): List<PlayableTrack> = withContext(Dispatchers.IO) {
        if (query.isBlank()) return@withContext emptyList()
        val url = "https://www.jiosaavn.com/api.php?__call=autocomplete.get&_format=json&_marker=0&query=${android.net.Uri.encode(query)}"
        val request = Request.Builder().url(url).build()

        runCatching {
            okHttpClient.newCall(request).execute().use { response ->
                if (!response.isSuccessful) return@withContext emptyList()
                val body = response.body?.string().orEmpty()
                val json = JSONObject(body)
                val songs = json.optJSONObject("songs")?.optJSONArray("data") ?: return@withContext emptyList()

                val tracks = mutableListOf<PlayableTrack>()
                for (i in 0 until songs.length()) {
                    val song = songs.getJSONObject(i)
                    val title = song.optString("title").replace("&quot;", "\"").replace("&amp;", "&")
                    val artist = song.optString("more_info") ?: song.optString("description")
                    val artworkUrl = song.optString("image").replace("50x50", "500x500")
                    val encryptedMediaUrl = song.optJSONObject("more_info")?.optString("encrypted_media_url")

                    val streamUrl = if (!encryptedMediaUrl.isNullOrBlank()) {
                        decryptMediaUrl(encryptedMediaUrl)
                    } else null

                    tracks.add(
                        PlayableTrack(
                            title = title,
                            artist = artist,
                            album = "JioSaavn Music",
                            artworkUrl = artworkUrl,
                            playbackUrl = streamUrl,
                            playbackMimeType = "audio/mp4",
                        )
                    )
                }
                tracks
            }
        }.getOrDefault(emptyList())
    }

    private fun decryptMediaUrl(encryptedUrl: String): String? {
        return runCatching {
            val cipher = Cipher.getInstance("DES/ECB/PKCS5Padding")
            val keySpec = SecretKeySpec(desKey, "DES")
            cipher.init(Cipher.DECRYPT_MODE, keySpec)
            val decodedBytes = android.util.Base64.decode(encryptedUrl, android.util.Base64.DEFAULT)
            val decryptedBytes = cipher.doFinal(decodedBytes)
            String(decryptedBytes, Charsets.UTF_8).replace("http://", "https://")
        }.getOrNull()
    }
}
