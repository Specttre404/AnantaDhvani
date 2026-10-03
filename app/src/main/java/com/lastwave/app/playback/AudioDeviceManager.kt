package com.lastwave.app.playback

import android.content.Context
import android.media.AudioDeviceCallback
import android.media.AudioDeviceInfo
import android.media.AudioManager
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

data class AudioDeviceTelemetry(
    val deviceName: String = "Built-in Speaker",
    val deviceType: String = "Speaker",
    val codec: String = "PCM",
    val sampleRate: Int = 48000,
    val bitDepth: Int = 16,
)

@Singleton
class AudioDeviceManager @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    private val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as? AudioManager
    private val _telemetry = MutableStateFlow(AudioDeviceTelemetry())
    val telemetry: StateFlow<AudioDeviceTelemetry> = _telemetry.asStateFlow()

    init {
        updateTelemetry()
        audioManager?.registerAudioDeviceCallback(object : AudioDeviceCallback() {
            override fun onAudioDevicesAdded(addedDevices: Array<out AudioDeviceInfo>?) {
                updateTelemetry()
            }

            override fun onAudioDevicesRemoved(removedDevices: Array<out AudioDeviceInfo>?) {
                updateTelemetry()
            }
        }, null)
    }

    private fun updateTelemetry() {
        val am = audioManager ?: return
        val devices = am.getDevices(AudioManager.GET_DEVICES_OUTPUTS)
        val active = devices.firstOrNull { it.type != AudioDeviceInfo.TYPE_BUILTIN_SPEAKER } ?: devices.firstOrNull()

        if (active != null) {
            val typeName = when (active.type) {
                AudioDeviceInfo.TYPE_BLUETOOTH_A2DP -> "Bluetooth A2DP"
                AudioDeviceInfo.TYPE_USB_DEVICE, AudioDeviceInfo.TYPE_USB_HEADSET -> "USB DAC"
                AudioDeviceInfo.TYPE_WIRED_HEADPHONES, AudioDeviceInfo.TYPE_WIRED_HEADSET -> "Wired Headset"
                else -> "Built-in Speaker"
            }
            val sampleRate = active.sampleRates.maxOrNull() ?: 48000
            val bitDepth = if (active.type == AudioDeviceInfo.TYPE_USB_DEVICE) 24 else 16

            _telemetry.value = AudioDeviceTelemetry(
                deviceName = active.productName.toString().ifBlank { typeName },
                deviceType = typeName,
                codec = if (active.type == AudioDeviceInfo.TYPE_BLUETOOTH_A2DP) "LDAC / AAC" else "Direct PCM",
                sampleRate = sampleRate,
                bitDepth = bitDepth,
            )
        }
    }
}
