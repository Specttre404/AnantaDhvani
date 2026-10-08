package com.lastwave.app.playback.power

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.PowerManager
import android.util.Log
import com.lastwave.app.data.local.SettingsPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DisplaySleepPowerGuard @Inject constructor(
    @ApplicationContext private val context: Context,
    private val settingsPreferences: SettingsPreferences,
) {
    private val _isScreenOn = MutableStateFlow(true)
    val isScreenOn: StateFlow<Boolean> = _isScreenOn.asStateFlow()

    private val scope = CoroutineScope(Dispatchers.Default)
    private var isRegistered = false

    init {
        instance = this
    }

    private val screenReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when (intent?.action) {
                Intent.ACTION_SCREEN_OFF -> {
                    _isScreenOn.value = false
                    scope.launch {
                        val settings = runCatching { settingsPreferences.settings.first() }.getOrNull()
                        if (settings?.screenOffBatterySaver == true) {
                            Log.i("DisplaySleepPowerGuard", "Screen Off: Guard engaged, pausing visualizer frame clocks")
                        }
                    }
                }
                Intent.ACTION_SCREEN_ON -> {
                    _isScreenOn.value = true
                    Log.i("DisplaySleepPowerGuard", "Screen On: Guard disengaged, resuming full visualizer frame clocks")
                }
            }
        }
    }

    fun register() {
        if (isRegistered) return
        val filter = IntentFilter().apply {
            addAction(Intent.ACTION_SCREEN_OFF)
            addAction(Intent.ACTION_SCREEN_ON)
        }
        context.registerReceiver(screenReceiver, filter)
        isRegistered = true

        val powerManager = context.getSystemService(Context.POWER_SERVICE) as? PowerManager
        _isScreenOn.value = powerManager?.isInteractive ?: true
    }

    fun unregister() {
        if (!isRegistered) return
        runCatching { context.unregisterReceiver(screenReceiver) }
        isRegistered = false
    }

    companion object {
        @Volatile
        var instance: DisplaySleepPowerGuard? = null
    }
}
