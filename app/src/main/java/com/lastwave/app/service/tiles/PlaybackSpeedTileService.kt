package com.lastwave.app.service.tiles

import android.os.Build
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import com.lastwave.app.playback.MusicPlayer
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class PlaybackSpeedTileService : TileService() {

    @Inject
    lateinit var musicPlayer: MusicPlayer

    private var scope = CoroutineScope(Dispatchers.Main)
    private var job: Job? = null

    override fun onStartListening() {
        super.onStartListening()
        job?.cancel()
        job = scope.launch {
            musicPlayer.state.collect { state ->
                val tile = qsTile ?: return@collect
                tile.state = if (state.speed != 1.0f) Tile.STATE_ACTIVE else Tile.STATE_INACTIVE
                tile.label = "Speed"
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    tile.subtitle = "${"%.2f".format(state.speed)}x"
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
        musicPlayer.cycleSpeed()
    }
}
