package com.lastwave.app.data.local

import android.content.ContentUris
import android.content.Context
import android.provider.MediaStore
import com.lastwave.app.playback.PlayableTrack
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalAudioScanner @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    suspend fun scanLocalTracks(): List<PlayableTrack> = withContext(Dispatchers.IO) {
        val tracks = mutableListOf<PlayableTrack>()
        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.DURATION,
        )

        val selection = "${MediaStore.Audio.Media.IS_MUSIC} != 0"

        runCatching {
            context.contentResolver.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                null,
                "${MediaStore.Audio.Media.TITLE} ASC",
            )?.use { cursor ->
                val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
                val titleColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
                val artistColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
                val albumColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)
                val durationColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)

                while (cursor.moveToNext()) {
                    val id = cursor.getLong(idColumn)
                    val title = cursor.getString(titleColumn).orEmpty().ifBlank { "Unknown Title" }
                    val artist = cursor.getString(artistColumn).orEmpty().ifBlank { "Unknown Artist" }
                    val album = cursor.getString(albumColumn).orEmpty().ifBlank { "Unknown Album" }
                    val durationMs = cursor.getLong(durationColumn)

                    val excludedKeywords = listOf("whatsapp", "notification", "ringtone", "call_record", "voice_note")
                    if (excludedKeywords.any { title.contains(it, ignoreCase = true) || album.contains(it, ignoreCase = true) }) {
                        continue
                    }

                    val contentUri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id)

                    tracks.add(
                        PlayableTrack(
                            title = title,
                            artist = artist,
                            album = album,
                            playbackUrl = contentUri.toString(),
                            playbackMimeType = "audio/*",
                            videoId = null,
                        ),
                    )
                }
            }
        }

        tracks
    }
}
