package com.lastwave.app.audio

import android.content.Context
import android.media.AudioDeviceInfo
import android.media.AudioManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Environment
import android.os.PowerManager
import android.os.StatFs
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

data class AutoEqProfile(
    val modelName: String,
    val bandGains: FloatArray // 10-band adjustments in dB (-12.0f to +12.0f)
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as AutoEqProfile
        if (modelName != other.modelName) return false
        if (!bandGains.contentEquals(other.bandGains)) return false
        return true
    }

    override fun hashCode(): Int {
        var result = modelName.hashCode()
        result = 31 * result + bandGains.contentHashCode()
        return result
    }
}

object AutoEqDatabase {
    fun findProfile(deviceName: String): FloatArray? {
        val lower = deviceName.lowercase()
        return when {
            lower.contains("wh-1000xm") || lower.contains("wf-1000xm") ->
                floatArrayOf(-1.5f, -2.0f, -0.5f, 0.0f, 1.2f, 2.0f, 1.5f, -0.5f, 1.0f, 0.5f)
            lower.contains("airpods") ->
                floatArrayOf(2.5f, 1.8f, 0.5f, -0.5f, -1.0f, 0.5f, 1.8f, 2.2f, 1.0f, -0.5f)
            lower.contains("galaxy buds") ->
                floatArrayOf(0.5f, 0.0f, -0.5f, 0.5f, 1.0f, 0.0f, -1.2f, 0.8f, 1.5f, 0.0f)
            lower.contains("pixel buds") ->
                floatArrayOf(1.0f, 0.5f, -0.5f, 0.0f, 0.8f, 1.2f, -0.5f, 1.0f, 1.2f, 0.0f)
            lower.contains("dac") || lower.contains("apple") || lower.contains("dongle") ->
                floatArrayOf(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f)
            else -> null
        }
    }
}

@Singleton
class AutoHardwareAudioTuner @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    data class HardwareAudioProfile(
        val optimalSampleRate: Int,
        val optimalBufferSize: Int,
        val isLowLatencySupported: Boolean,
        val isHeadphonesConnected: Boolean,
        val isUsbDacConnected: Boolean,
        val connectedDeviceName: String,
        val minBufferMs: Int,
        val maxBufferMs: Int,
        val playbackBufferMs: Int,
        val dynamicCacheQuotaMb: Int,
        val downloadConcurrency: Int,
        val isOffloadViable: Boolean,
        val crossfeedRecommended: Boolean,
        val hapticsEnabled: Boolean,
        val powerSaveActive: Boolean,
        val autoEqGains: FloatArray? = null,
    )

    private val _currentProfile = MutableStateFlow<HardwareAudioProfile?>(null)
    val currentProfile: StateFlow<HardwareAudioProfile?> = _currentProfile.asStateFlow()

    fun probeAndApply(): HardwareAudioProfile {
        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        // 1. Hardware Audio Clock & Buffer Telemetry
        val sampleRateStr = audioManager.getProperty(AudioManager.PROPERTY_OUTPUT_SAMPLE_RATE)
        val framesPerBufferStr = audioManager.getProperty(AudioManager.PROPERTY_OUTPUT_FRAMES_PER_BUFFER)
        val sampleRate = sampleRateStr?.toIntOrNull() ?: 48000
        val bufferFrames = framesPerBufferStr?.toIntOrNull() ?: 256
        val hasLowLatency = context.packageManager.hasSystemFeature("android.hardware.audio.low_latency")
        val hasProAudio = context.packageManager.hasSystemFeature("android.hardware.audio.pro")

        // 2. Audio Endpoint Routing
        val devices = audioManager.getDevices(AudioManager.GET_DEVICES_OUTPUTS)
        val usbDevice = devices.firstOrNull { it.type == AudioDeviceInfo.TYPE_USB_DEVICE || it.type == AudioDeviceInfo.TYPE_USB_HEADSET }
        val isUsbDac = usbDevice != null
        val isHeadphones = isUsbDac || devices.any {
            it.type == AudioDeviceInfo.TYPE_WIRED_HEADPHONES ||
            it.type == AudioDeviceInfo.TYPE_WIRED_HEADSET ||
            it.type == AudioDeviceInfo.TYPE_BLUETOOTH_A2DP ||
            it.type == AudioDeviceInfo.TYPE_BLE_HEADSET
        }
        val deviceName = usbDevice?.productName?.toString() ?: devices.firstOrNull { it.type == AudioDeviceInfo.TYPE_BLUETOOTH_A2DP }?.productName?.toString() ?: "Internal Audio"

        // AutoEQ Endpoint Target Profile Routing
        val autoEqGains = AutoEqDatabase.findProfile(deviceName)

        // 3. Network & Connection Speed Adaptation
        val activeNetwork = connectivityManager.activeNetwork
        val caps = connectivityManager.getNetworkCapabilities(activeNetwork)
        val isUnmetered = caps?.hasCapability(NetworkCapabilities.NET_CAPABILITY_NOT_METERED) == true
        val isHighSpeed = caps?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) == true || caps?.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) == true

        // 4. Power & Battery Status
        val isPowerSave = powerManager.isPowerSaveMode

        // 5. Dynamic Buffer Calculations
        val minBuf = when {
            isPowerSave -> 45000
            isHighSpeed && hasLowLatency -> 20000
            isUnmetered -> 25000
            else -> 35000
        }
        val maxBuf = minBuf + 15000

        // 6. Dynamic Storage Cache Allocation (5% of available free storage, bound 1GB - 6GB)
        val dynamicCache = calculateOptimalCacheQuota()

        // 7. Parallel Worker Scaling
        val downloadWorkers = if (isUnmetered && isHighSpeed && !isPowerSave) 4 else 1

        val profile = HardwareAudioProfile(
            optimalSampleRate = sampleRate,
            optimalBufferSize = bufferFrames,
            isLowLatencySupported = hasLowLatency,
            isHeadphonesConnected = isHeadphones,
            isUsbDacConnected = isUsbDac,
            connectedDeviceName = deviceName,
            minBufferMs = minBuf,
            maxBufferMs = maxBuf,
            playbackBufferMs = 2500,
            dynamicCacheQuotaMb = dynamicCache,
            downloadConcurrency = downloadWorkers,
            isOffloadViable = !isPowerSave && (hasLowLatency || hasProAudio),
            crossfeedRecommended = isHeadphones,
            hapticsEnabled = isHeadphones && !isPowerSave,
            powerSaveActive = isPowerSave,
            autoEqGains = autoEqGains,
        )

        _currentProfile.value = profile
        return profile
    }

    private fun calculateOptimalCacheQuota(): Int {
        return try {
            val stat = StatFs(Environment.getDataDirectory().path)
            val availableBytes = stat.availableBlocksLong * stat.blockSizeLong
            val fivePercentMb = (availableBytes * 0.05 / (1024 * 1024)).toInt()
            fivePercentMb.coerceIn(1024, 6144)
        } catch (_: Exception) {
            2048
        }
    }
}
