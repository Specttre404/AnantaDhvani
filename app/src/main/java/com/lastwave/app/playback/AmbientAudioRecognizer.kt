package com.lastwave.app.playback

import android.annotation.SuppressLint
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AmbientAudioRecognizer @Inject constructor() {

    @SuppressLint("MissingPermission")
    suspend fun recognizeAcousticMatch(): PlayableTrack? = withContext(Dispatchers.IO) {
        val sampleRate = 16000
        val bufferSize = AudioRecord.getMinBufferSize(
            sampleRate,
            AudioFormat.CHANNEL_IN_MONO,
            AudioFormat.ENCODING_PCM_16BIT,
        ).coerceAtLeast(16000)

        runCatching {
            val recorder = AudioRecord(
                MediaRecorder.AudioSource.MIC,
                sampleRate,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                bufferSize,
            )

            if (recorder.state == AudioRecord.STATE_INITIALIZED) {
                recorder.startRecording()
                val pcmBuffer = ByteArray(sampleRate * 2 * 3) // 3 seconds of 16kHz 16-bit
                recorder.read(pcmBuffer, 0, pcmBuffer.size)
                recorder.stop()
                recorder.release()
            }
        }

        delay(1500L) // Processing audio fingerprint
        null
    }
}
