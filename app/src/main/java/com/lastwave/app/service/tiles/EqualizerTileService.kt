package com.lastwave.app.service.tiles

import android.os.Build
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import com.lastwave.app.data.local.EqualizerPreferences
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class EqualizerTileService : TileService() {

    @Inject
    lateinit var equalizerPreferences: EqualizerPreferences

    private var scope = CoroutineScope(Dispatchers.Main)
    private var job: Job? = null

    override fun onStartListening() {
        super.onStartListening()
        job?.cancel()
        job = scope.launch {
            equalizerPreferences.settings.collect { eq ->
                val tile = qsTile ?: return@collect
                tile.state = if (eq.enabled) Tile.STATE_ACTIVE else Tile.STATE_INACTIVE
                tile.label = "Equalizer"
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    tile.subtitle = if (eq.enabled) eq.presetName else "Bypassed"
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
        scope.launch {
            val current = equalizerPreferences.settings.first()
            equalizerPreferences.setEnabled(!current.enabled)
        }
    }
}
