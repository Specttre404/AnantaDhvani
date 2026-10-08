package com.lastwave.app.data.converter

import android.content.Context
import android.media.MediaCodec
import android.media.MediaCodecInfo
import android.media.MediaExtractor
import android.media.MediaFormat
import android.media.MediaMuxer
import android.net.Uri
import android.os.Environment
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

enum class TargetAudioFormat(val extension: String, val mimeType: String, val displayName: String) {
    FLAC("flac", MediaFormat.MIMETYPE_AUDIO_FLAC, "FLAC (Lossless)"),
    M4A("m4a", MediaFormat.MIMETYPE_AUDIO_AAC, "AAC (M4A 256kbps)"),
    OPUS("opus", MediaFormat.MIMETYPE_AUDIO_OPUS, "Opus (160kbps)"),
    MP3("mp3", MediaFormat.MIMETYPE_AUDIO_MPEG, "MP3 (320kbps)")
}

@Singleton
class AudioFormatConverter @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    private val _progress = MutableStateFlow(0.0f)
    val progress: StateFlow<Float> = _progress.asStateFlow()

    private val _status = MutableStateFlow("Idle")
    val status: StateFlow<String> = _status.asStateFlow()

    init {
        instance = this
    }

    suspend fun convertAudio(
        inputUri: Uri,
        outputFileName: String,
        targetFormat: TargetAudioFormat,
        onProgressUpdate: ((Float) -> Unit)? = null,
    ): File? = withContext(Dispatchers.IO) {
        try {
            _progress.value = 0.0f
            _status.value = "Starting ${targetFormat.displayName} conversion..."
            onProgressUpdate?.invoke(0.0f)

            val outputDir = File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC),
                "LastWave/Transcoded"
            ).apply { mkdirs() }

            val cleanName = outputFileName.substringBeforeLast(".")
            val outputFile = File(outputDir, "$cleanName.${targetFormat.extension}")

            val extractor = MediaExtractor()
            extractor.setDataSource(context, inputUri, null)

            var trackIndex = -1
            var inputFormat: MediaFormat? = null
            for (i in 0 until extractor.trackCount) {
                val format = extractor.getTrackFormat(i)
                val mime = format.getString(MediaFormat.KEY_MIME)
                if (mime?.startsWith("audio/") == true) {
                    trackIndex = i
                    inputFormat = format
                    break
                }
            }

            if (trackIndex < 0 || inputFormat == null) {
                _status.value = "No audio track found in file"
                return@withContext null
            }

            extractor.selectTrack(trackIndex)
            val durationUs = if (inputFormat.containsKey(MediaFormat.KEY_DURATION)) {
                inputFormat.getLong(MediaFormat.KEY_DURATION)
            } else 1_000_000L

            val sampleRate = inputFormat.getInteger(MediaFormat.KEY_SAMPLE_RATE)
            val channelCount = inputFormat.getInteger(MediaFormat.KEY_CHANNEL_COUNT)

            val inputMime = inputFormat.getString(MediaFormat.KEY_MIME) ?: "audio/mpeg"
            val decoder = MediaCodec.createDecoderByType(inputMime)
            decoder.configure(inputFormat, null, null, 0)
            decoder.start()

            val encoderFormat = MediaFormat.createAudioFormat(targetFormat.mimeType, sampleRate, channelCount)
            val bitrate = when (targetFormat) {
                TargetAudioFormat.FLAC -> 0
                TargetAudioFormat.M4A -> 256_000
                TargetAudioFormat.OPUS -> 160_000
                TargetAudioFormat.MP3 -> 320_000
            }
            if (bitrate > 0) {
                encoderFormat.setInteger(MediaFormat.KEY_BIT_RATE, bitrate)
            }
            if (targetFormat == TargetAudioFormat.M4A) {
                encoderFormat.setInteger(MediaFormat.KEY_AAC_PROFILE, MediaCodecInfo.CodecProfileLevel.AACObjectLC)
            }

            val encoder = try {
                MediaCodec.createEncoderByType(targetFormat.mimeType).apply {
                    configure(encoderFormat, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE)
                    start()
                }
            } catch (e: Exception) {
                Log.w("AudioFormatConverter", "Native encoder failed for ${targetFormat.mimeType}, falling back to AAC", e)
                MediaCodec.createEncoderByType(MediaFormat.MIMETYPE_AUDIO_AAC).apply {
                    val fallbackFormat = MediaFormat.createAudioFormat(MediaFormat.MIMETYPE_AUDIO_AAC, sampleRate, channelCount)
                    fallbackFormat.setInteger(MediaFormat.KEY_BIT_RATE, 256_000)
                    configure(fallbackFormat, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE)
                    start()
                }
            }

            val muxerFormat = if (targetFormat == TargetAudioFormat.OPUS && android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                MediaMuxer.OutputFormat.MUXER_OUTPUT_OGG
            } else {
                MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4
            }

            val muxer = MediaMuxer(outputFile.absolutePath, muxerFormat)
            var muxerTrackIndex = -1
            var muxerStarted = false

            val bufferInfo = MediaCodec.BufferInfo()
            var sawInputEOS = false
            var sawDecoderEOS = false
            var sawEncoderEOS = false

            while (!sawEncoderEOS) {
                if (!sawInputEOS) {
                    val inIndex = decoder.dequeueInputBuffer(5000L)
                    if (inIndex >= 0) {
                        val inputBuffer = decoder.getInputBuffer(inIndex)
                        if (inputBuffer != null) {
                            val sampleSize = extractor.readSampleData(inputBuffer, 0)
                            if (sampleSize < 0) {
                                decoder.queueInputBuffer(inIndex, 0, 0, 0L, MediaCodec.BUFFER_FLAG_END_OF_STREAM)
                                sawInputEOS = true
                            } else {
                                val presentationTimeUs = extractor.sampleTime
                                decoder.queueInputBuffer(inIndex, 0, sampleSize, presentationTimeUs, 0)
                                extractor.advance()

                                val currentProgress = (presentationTimeUs.toFloat() / durationUs.toFloat()).coerceIn(0.0f, 1.0f)
                                _progress.value = currentProgress
                                onProgressUpdate?.invoke(currentProgress)
                            }
                        }
                    }
                }

                if (!sawDecoderEOS) {
                    val outIndex = decoder.dequeueOutputBuffer(bufferInfo, 5000L)
                    if (outIndex >= 0) {
                        val pcmBuffer = decoder.getOutputBuffer(outIndex)
                        val encInIndex = encoder.dequeueInputBuffer(5000L)
                        if (encInIndex >= 0 && pcmBuffer != null) {
                            val encBuffer = encoder.getInputBuffer(encInIndex)
                            if (encBuffer != null) {
                                encBuffer.clear()
                                encBuffer.put(pcmBuffer)
                                val isEOS = (bufferInfo.flags and MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0
                                val flag = if (isEOS) MediaCodec.BUFFER_FLAG_END_OF_STREAM else 0
                                encoder.queueInputBuffer(encInIndex, 0, bufferInfo.size, bufferInfo.presentationTimeUs, flag)
                                if (isEOS) sawDecoderEOS = true
                            }
                        }
                        decoder.releaseOutputBuffer(outIndex, false)
                    }
                }

                val encOutIndex = encoder.dequeueOutputBuffer(bufferInfo, 5000L)
                if (encOutIndex >= 0) {
                    val encodedBuffer = encoder.getOutputBuffer(encOutIndex)
                    if (encodedBuffer != null && bufferInfo.size > 0) {
                        if (muxerStarted && muxerTrackIndex >= 0) {
                            encodedBuffer.position(bufferInfo.offset)
                            encodedBuffer.limit(bufferInfo.offset + bufferInfo.size)
                            muxer.writeSampleData(muxerTrackIndex, encodedBuffer, bufferInfo)
                        }
                    }
                    if ((bufferInfo.flags and MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0) {
                        sawEncoderEOS = true
                    }
                    encoder.releaseOutputBuffer(encOutIndex, false)
                } else if (encOutIndex == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {
                    if (!muxerStarted) {
                        val newFormat = encoder.outputFormat
                        muxerTrackIndex = muxer.addTrack(newFormat)
                        muxer.start()
                        muxerStarted = true
                    }
                }
            }

            runCatching { decoder.stop(); decoder.release() }
            runCatching { encoder.stop(); encoder.release() }
            runCatching { if (muxerStarted) muxer.stop(); muxer.release() }
            runCatching { extractor.release() }

            _progress.value = 1.0f
            _status.value = "Transcoded successfully to ${outputFile.name}"
            onProgressUpdate?.invoke(1.0f)

            outputFile
        } catch (e: Exception) {
            Log.e("AudioFormatConverter", "Conversion failed", e)
            _status.value = "Conversion error: ${e.message}"
            null
        }
    }

    companion object {
        @Volatile
        var instance: AudioFormatConverter? = null

        fun getInstance(context: Context): AudioFormatConverter {
            return instance ?: synchronized(this) {
                instance ?: AudioFormatConverter(context.applicationContext).also { instance = it }
            }
        }
    }
}
