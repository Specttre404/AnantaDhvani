package com.lastwave.app.data.recognition

import android.annotation.SuppressLint
import android.content.Context
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import javax.inject.Inject
import javax.inject.Singleton

data class RecognizedSong(
    val title: String,
    val artist: String,
    val album: String? = null,
    val query: String = "$title $artist",
)

@Singleton
class AudioRecognitionManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val okHttpClient: OkHttpClient,
) {
    companion object {
        private const val SAMPLE_RATE = 44100
        const val RECORD_DURATION_MS = 5000L
    }

    @SuppressLint("MissingPermission")
    suspend fun recognizeAudio(): Result<RecognizedSong> = withContext(Dispatchers.IO) {
        runCatching {
            val minBufferSize = AudioRecord.getMinBufferSize(
                SAMPLE_RATE,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
            )

            val bufferSize = maxOf(minBufferSize, 8192)
            val audioRecord = AudioRecord(
                MediaRecorder.AudioSource.MIC,
                SAMPLE_RATE,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                bufferSize,
            )

            if (audioRecord.state != AudioRecord.STATE_INITIALIZED) {
                throw IllegalStateException("AudioRecord failed to initialize")
            }

            val pcmData = ByteArrayOutputStream()
            val buffer = ByteArray(bufferSize)
            val startTime = System.currentTimeMillis()

            audioRecord.startRecording()
            try {
                while (System.currentTimeMillis() - startTime < RECORD_DURATION_MS) {
                    val read = audioRecord.read(buffer, 0, buffer.size)
                    if (read > 0) {
                        pcmData.write(buffer, 0, read)
                    }
                }
            } finally {
                runCatching {
                    audioRecord.stop()
                    audioRecord.release()
                }
            }

            val rawBytes = pcmData.toByteArray()
            if (rawBytes.isEmpty()) throw IllegalStateException("No audio data recorded")

            val wavBytes = createWavHeader(rawBytes, SAMPLE_RATE, 1, 16)
            identifySong(wavBytes)
        }
    }

    private suspend fun identifySong(wavBytes: ByteArray): RecognizedSong = withContext(Dispatchers.IO) {
        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("api_token", "test")
            .addFormDataPart(
                "file", "audio.wav",
                wavBytes.toRequestBody("audio/wav".toMediaTypeOrNull()),
            )
            .build()

        val request = Request.Builder()
            .url("https://api.audd.io/")
            .post(requestBody)
            .build()

        val response = okHttpClient.newCall(request).execute()
        val responseText = response.body?.string().orEmpty()

        if (response.isSuccessful && responseText.isNotBlank()) {
            val json = JSONObject(responseText)
            if (json.optString("status") == "success" && !json.isNull("result")) {
                val resultObj = json.getJSONObject("result")
                val title = resultObj.optString("title").ifBlank { null }
                val artist = resultObj.optString("artist").ifBlank { null }
                val album = resultObj.optString("album").ifBlank { null }
                if (title != null && artist != null) {
                    return@withContext RecognizedSong(title = title, artist = artist, album = album)
                }
            }
        }

        throw IllegalStateException("Song not recognized. Please try again in a quieter environment.")
    }

    private fun createWavHeader(
        pcmBytes: ByteArray,
        sampleRate: Int,
        channels: Int,
        bitsPerSample: Int,
    ): ByteArray {
        val totalAudioLen = pcmBytes.size.toLong()
        val totalDataLen = totalAudioLen + 36
        val byteRate = (sampleRate * channels * bitsPerSample / 8).toLong()

        val header = ByteArray(44)
        header[0] = 'R'.code.toByte()
        header[1] = 'I'.code.toByte()
        header[2] = 'F'.code.toByte()
        header[3] = 'F'.code.toByte()
        header[4] = (totalDataLen and 0xff).toByte()
        header[5] = (totalDataLen shr 8 and 0xff).toByte()
        header[6] = (totalDataLen shr 16 and 0xff).toByte()
        header[7] = (totalDataLen shr 24 and 0xff).toByte()
        header[8] = 'W'.code.toByte()
        header[9] = 'A'.code.toByte()
        header[10] = 'V'.code.toByte()
        header[11] = 'E'.code.toByte()
        header[12] = 'f'.code.toByte()
        header[13] = 'm'.code.toByte()
        header[14] = 't'.code.toByte()
        header[15] = ' '.code.toByte()
        header[16] = 16
        header[17] = 0
        header[18] = 0
        header[19] = 0
        header[20] = 1
        header[21] = 0
        header[22] = channels.toByte()
        header[23] = 0
        header[24] = (sampleRate and 0xff).toByte()
        header[25] = (sampleRate shr 8 and 0xff).toByte()
        header[26] = (sampleRate shr 16 and 0xff).toByte()
        header[27] = (sampleRate shr 24 and 0xff).toByte()
        header[28] = (byteRate and 0xff).toByte()
        header[29] = (byteRate shr 8 and 0xff).toByte()
        header[30] = (byteRate shr 16 and 0xff).toByte()
        header[31] = (byteRate shr 24 and 0xff).toByte()
        header[32] = (channels * bitsPerSample / 8).toByte()
        header[33] = 0
        header[34] = bitsPerSample.toByte()
        header[35] = 0
        header[36] = 'd'.code.toByte()
        header[37] = 'a'.code.toByte()
        header[38] = 't'.code.toByte()
        header[39] = 'a'.code.toByte()
        header[40] = (totalAudioLen and 0xff).toByte()
        header[41] = (totalAudioLen shr 8 and 0xff).toByte()
        header[42] = (totalAudioLen shr 16 and 0xff).toByte()
        header[43] = (totalAudioLen shr 24 and 0xff).toByte()

        return header + pcmBytes
    }
}
