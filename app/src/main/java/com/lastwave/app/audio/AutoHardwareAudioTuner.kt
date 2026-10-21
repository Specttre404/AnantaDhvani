package com.lastwave.app.audio

import android.content.Context
import android.media.AudioDeviceInfo
import android.media.AudioManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AutoHardwareAudioTuner @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    data class HardwareAudioProfile(
        val optimalSampleRate: Int,
        val optimalBufferSize: Int,
        val isLowLatencySupported: Boolean,
        val isHeadphonesConnected: Boolean,
        val recommendedBufferDurationMs: Int,
        val forceOpusHighBitrate: Boolean
    )

    fun probeAndApply(): HardwareAudioProfile {
        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        val sampleRateStr = audioManager.getProperty(AudioManager.PROPERTY_OUTPUT_SAMPLE_RATE)
        val framesPerBufferStr = audioManager.getProperty(AudioManager.PROPERTY_OUTPUT_FRAMES_PER_BUFFER)

        val sampleRate = sampleRateStr?.toIntOrNull() ?: 48000
        val bufferFrames = framesPerBufferStr?.toIntOrNull() ?: 256
        val hasLowLatency = context.packageManager.hasSystemFeature("android.hardware.audio.low_latency")
        val hasProAudio = context.packageManager.hasSystemFeature("android.hardware.audio.pro")

        val devices = audioManager.getDevices(AudioManager.GET_DEVICES_OUTPUTS)
        val isHeadphones = devices.any {
            it.type == AudioDeviceInfo.TYPE_WIRED_HEADPHONES ||
            it.type == AudioDeviceInfo.TYPE_WIRED_HEADSET ||
            it.type == AudioDeviceInfo.TYPE_USB_HEADSET ||
            it.type == AudioDeviceInfo.TYPE_BLE_HEADSET
        }

        val bufferDuration = if (hasLowLatency || hasProAudio) 25000 else 40000

        return HardwareAudioProfile(
            optimalSampleRate = sampleRate,
            optimalBufferSize = bufferFrames,
            isLowLatencySupported = hasLowLatency,
            isHeadphonesConnected = isHeadphones,
            recommendedBufferDurationMs = bufferDuration,
            forceOpusHighBitrate = true
        )
    }
}
