package com.lastwave.app.data.archiveorg

import com.lastwave.app.playback.PlayableTrack
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import javax.inject.Inject
import javax.inject.Singleton

data class ArchiveShow(
    val identifier: String,
    val title: String,
    val artist: String,
    val date: String,
)

@Singleton
class ArchiveOrgRepository @Inject constructor(
    private val okHttpClient: OkHttpClient,
) {
    suspend fun searchLiveConcerts(artistQuery: String): List<ArchiveShow> = withContext(Dispatchers.IO) {
        val q = if (artistQuery.isBlank()) "Grateful Dead" else artistQuery
        val url = "https://archive.org/advancedsearch.php?q=collection:(etree)+AND+creator:($q)&fl[]=identifier,title,creator,date&rows=20&output=json"
        val request = Request.Builder().url(url).build()

        runCatching {
            okHttpClient.newCall(request).execute().use { response ->
                if (!response.isSuccessful) return@withContext emptyList()
                val body = response.body?.string().orEmpty()
                val json = JSONObject(body)
                val docs = json.optJSONObject("response")?.optJSONArray("docs") ?: return@withContext emptyList()
                val shows = mutableListOf<ArchiveShow>()
                for (i in 0 until docs.length()) {
                    val doc = docs.getJSONObject(i)
                    shows.add(
                        ArchiveShow(
                            identifier = doc.optString("identifier"),
                            title = doc.optString("title", "Live Concert"),
                            artist = doc.optString("creator", "Live Artist"),
                            date = doc.optString("date", "Unknown Date"),
                        )
                    )
                }
                shows
            }
        }.getOrDefault(emptyList())
    }

    suspend fun getShowTracks(identifier: String): List<PlayableTrack> = withContext(Dispatchers.IO) {
        val url = "https://archive.org/metadata/$identifier"
        val request = Request.Builder().url(url).build()

        runCatching {
            okHttpClient.newCall(request).execute().use { response ->
                if (!response.isSuccessful) return@withContext emptyList()
                val body = response.body?.string().orEmpty()
                val json = JSONObject(body)
                val files = json.optJSONArray("files") ?: return@withContext emptyList()
                val meta = json.optJSONObject("metadata")
                val artist = meta?.optString("creator") ?: "Live Artist"
                val album = meta?.optString("title") ?: "Live Show"

                val tracks = mutableListOf<PlayableTrack>()
                for (i in 0 until files.length()) {
                    val file = files.getJSONObject(i)
                    val format = file.optString("format", "")
                    if (format.contains("VBR MP3", ignoreCase = true) || format.contains("FLAC", ignoreCase = true)) {
                        val fileName = file.optString("name")
                        val title = file.optString("title").ifBlank { fileName }
                        val streamUrl = "https://archive.org/download/$identifier/$fileName"
                        tracks.add(
                            PlayableTrack(
                                title = title,
                                artist = artist,
                                album = album,
                                playbackUrl = streamUrl,
                                playbackMimeType = if (format.contains("FLAC")) "audio/flac" else "audio/mpeg",
                            )
                        )
                    }
                }
                tracks
            }
        }.getOrDefault(emptyList())
    }
}
