package com.lastwave.app.service.tiles

import android.os.Build
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import com.lastwave.app.data.generate.GeneratedTrack
import com.lastwave.app.data.playlist.LikedSongsManager
import com.lastwave.app.playback.MusicPlayer
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class FavoriteTrackTileService : TileService() {

    @Inject
    lateinit var musicPlayer: MusicPlayer

    @Inject
    lateinit var likedSongsManager: LikedSongsManager

    private var scope = CoroutineScope(Dispatchers.Main)
    private var job: Job? = null

    override fun onStartListening() {
        super.onStartListening()
        job?.cancel()
        likedSongsManager.start()
        job = scope.launch {
            musicPlayer.state.collect { state ->
                val track = state.current
                val keys = likedSongsManager.likedTrackKeys.value
                val isFavorite = track != null && GeneratedTrack(track.title, track.artist, null).key in keys
                val tile = qsTile ?: return@collect
                tile.state = if (isFavorite) Tile.STATE_ACTIVE else Tile.STATE_INACTIVE
                tile.label = "Favorite"
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    tile.subtitle = track?.title ?: "No track playing"
                }
                tile.updateTile()
            }
        }
    }

    override fun onStopListening() {
        super.onStopListening()
        job?.cancel()
    }

    override fun onClick() {
        super.onClick()
        val track = musicPlayer.state.value.current ?: return
        scope.launch {
            likedSongsManager.toggle(GeneratedTrack(track.title, track.artist, artworkUrl = track.artworkUrl))
            val tile = qsTile ?: return@launch
            val keys = likedSongsManager.likedTrackKeys.value
            val isFavorite = GeneratedTrack(track.title, track.artist, null).key in keys
            tile.state = if (isFavorite) Tile.STATE_ACTIVE else Tile.STATE_INACTIVE
            tile.updateTile()
        }
    }
}
