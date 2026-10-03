package com.lastwave.app.playback

import android.content.Context
import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder

data class WavAudioData(
    val sampleRate: Int,
    val channels: Int,
    val pcmFloats: FloatArray,
)

object WavPcmDecoder {
    suspend fun decodeWavUri(context: Context, uri: Uri): WavAudioData? = withContext(Dispatchers.IO) {
        runCatching {
            context.contentResolver.openInputStream(uri)?.use { stream ->
                decodeWavStream(stream)
            }
        }.getOrNull()
    }

    fun decodeWavStream(stream: InputStream): WavAudioData? {
        val bytes = stream.readBytes()
        if (bytes.size < 44) return null

        val buffer = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN)

        // Check RIFF and WAVE header
        val riff = String(bytes, 0, 4)
        val wave = String(bytes, 8, 4)
        if (riff != "RIFF" || wave != "WAVE") return null

        val channels = buffer.getShort(22).toInt()
        val sampleRate = buffer.getInt(24)
        val bitsPerSample = buffer.getShort(34).toInt()

        // Locate data chunk
        var dataOffset = 44
        for (i in 12 until bytes.size - 8) {
            if (bytes[i] == 'd'.code.toByte() && bytes[i + 1] == 'a'.code.toByte() &&
                bytes[i + 2] == 't'.code.toByte() && bytes[i + 3] == 'a'.code.toByte()
            ) {
                dataOffset = i + 8
                break
            }
        }

        val dataSize = bytes.size - dataOffset
        val sampleCount = dataSize / (bitsPerSample / 8)
        val floats = FloatArray(sampleCount)

        buffer.position(dataOffset)
        when (bitsPerSample) {
            16 -> {
                for (i in 0 until sampleCount) {
                    floats[i] = buffer.short.toFloat() / 32768f
                }
            }
            24 -> {
                for (i in 0 until sampleCount) {
                    val b1 = buffer.get().toInt() and 0xFF
                    val b2 = buffer.get().toInt() and 0xFF
                    val b3 = buffer.get().toInt()
                    val sample24 = (b3 shl 16) or (b2 shl 8) or b1
                    floats[i] = sample24.toFloat() / 8388608f
                }
            }
            32 -> {
                for (i in 0 until sampleCount) {
                    floats[i] = buffer.float
                }
            }
            else -> return null
        }

        return WavAudioData(sampleRate, channels, floats)
    }
}
