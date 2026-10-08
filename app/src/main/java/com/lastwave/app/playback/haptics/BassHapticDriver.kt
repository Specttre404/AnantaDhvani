package com.lastwave.app.playback.haptics

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BassHapticDriver @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    private val vibrator: Vibrator? by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
            vibratorManager?.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
        }
    }

    @Volatile
    var enabled: Boolean = false

    init {
        instance = this
    }

    @Suppress("MissingPermission")
    fun processLowFrequencyTransient(energyLevel: Float, enabled: Boolean) {
        val isEnabled = enabled || this.enabled
        if (isEnabled && energyLevel > 0.65f) {
            vibrator?.let { v ->
                if (v.hasVibrator()) {
                    val amplitude = (energyLevel * 255).toInt().coerceIn(1, 255)
                    val effect = VibrationEffect.createOneShot(25L, amplitude)
                    v.vibrate(effect)
                }
            }
        }
    }

    companion object {
        @Volatile
        var instance: BassHapticDriver? = null

        fun getInstance(context: Context): BassHapticDriver {
            return instance ?: synchronized(this) {
                instance ?: BassHapticDriver(context.applicationContext).also { instance = it }
            }
        }
    }
}
