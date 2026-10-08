package com.lastwave.app.data.metadata

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

data class MusicBrainzTagResult(
    val title: String,
    val artist: String,
    val album: String,
    val year: String?,
    val trackNumber: Int?,
    val genre: String?,
    val coverArtUrl: String?,
    val releaseMbid: String?,
)

class MusicBrainzAutoTagger {

    init {
        defaultInstance = this
    }

    suspend fun fetchMetadata(title: String, artist: String): MusicBrainzTagResult? = withContext(Dispatchers.IO) {
        try {
            val query = "recording:\"${title.trim()}\" AND artist:\"${artist.trim()}\""
            val encodedQuery = URLEncoder.encode(query, "UTF-8")
            val mbUrl = "https://musicbrainz.org/ws/2/recording?query=$encodedQuery&fmt=json"

            val conn = URL(mbUrl).openConnection() as HttpURLConnection
            conn.setRequestProperty("User-Agent", "AnantaDhvani/1.0 ( contact@lastwave.app )")
            conn.connectTimeout = 8000
            conn.readTimeout = 8000

            if (conn.responseCode != 200) {
                Log.w("MusicBrainzAutoTagger", "MusicBrainz API HTTP ${conn.responseCode}")
                return@withContext null
            }

            val jsonStr = conn.inputStream.bufferedReader().use { it.readText() }
            val root = JSONObject(jsonStr)
            val recordings = root.optJSONArray("recordings") ?: return@withContext null
            if (recordings.length() == 0) return@withContext null

            val rec = recordings.getJSONObject(0)
            val recTitle = rec.optString("title", title)

            var recArtist = artist
            val artistCredit = rec.optJSONArray("artist-credit")
            if (artistCredit != null && artistCredit.length() > 0) {
                recArtist = artistCredit.getJSONObject(0).optString("name", artist)
            }

            var albumTitle = "Unknown Album"
            var releaseYear: String? = null
            var releaseMbid: String? = null
            var trackPos: Int? = null

            val releases = rec.optJSONArray("releases")
            if (releases != null && releases.length() > 0) {
                val rel = releases.getJSONObject(0)
                albumTitle = rel.optString("title", "Unknown Album")
                releaseMbid = rel.optString("id").ifBlank { null }
                val date = rel.optString("date", "")
                if (date.isNotBlank()) {
                    releaseYear = date.take(4)
                }

                val media = rel.optJSONArray("media")
                if (media != null && media.length() > 0) {
                    val tracks = media.getJSONObject(0).optJSONArray("track")
                    if (tracks != null && tracks.length() > 0) {
                        trackPos = tracks.getJSONObject(0).optInt("number", 1)
                    }
                }
            }

            var genre: String? = null
            val tags = rec.optJSONArray("tags")
            if (tags != null && tags.length() > 0) {
                genre = tags.getJSONObject(0).optString("name", "").ifBlank { null }
            }

            var coverArtUrl: String? = null
            if (!releaseMbid.isNullOrBlank()) {
                coverArtUrl = fetchCoverArt(releaseMbid)
            }

            MusicBrainzTagResult(
                title = recTitle,
                artist = recArtist,
                album = albumTitle,
                year = releaseYear,
                trackNumber = trackPos,
                genre = genre,
                coverArtUrl = coverArtUrl,
                releaseMbid = releaseMbid,
            )
        } catch (e: Exception) {
            Log.e("MusicBrainzAutoTagger", "Error fetching MusicBrainz tags", e)
            null
        }
    }

    private fun fetchCoverArt(releaseMbid: String): String? {
        return try {
            val caaUrl = "https://coverartarchive.org/release/$releaseMbid"
            val conn = URL(caaUrl).openConnection() as HttpURLConnection
            conn.setRequestProperty("User-Agent", "AnantaDhvani/1.0 ( contact@lastwave.app )")
            conn.connectTimeout = 5000
            conn.readTimeout = 5000

            if (conn.responseCode != 200) return null
            val jsonStr = conn.inputStream.bufferedReader().use { it.readText() }
            val root = JSONObject(jsonStr)
            val images = root.optJSONArray("images") ?: return null
            if (images.length() == 0) return null

            val img = images.getJSONObject(0)
            val thumbs = img.optJSONObject("thumbnails")
            thumbs?.optString("1000", "")?.ifBlank { null }
                ?: thumbs?.optString("large", "")?.ifBlank { null }
                ?: img.optString("image", "").ifBlank { null }
        } catch (_: Exception) {
            null
        }
    }

    companion object {
        @Volatile
        private var defaultInstance: MusicBrainzAutoTagger? = null

        fun getInstance(): MusicBrainzAutoTagger {
            return defaultInstance ?: synchronized(this) {
                defaultInstance ?: MusicBrainzAutoTagger().also { defaultInstance = it }
            }
        }
    }
}
