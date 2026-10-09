package com.lastwave.app.playback.automix

import android.content.Context
import com.lastwave.app.playback.MusicPlayer
import com.lastwave.app.playback.analysis.HarmonicKeyAnalyzer
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AutomixEngine @Inject constructor(
    @ApplicationContext private val context: Context,
    private val musicPlayer: MusicPlayer,
    private val harmonicKeyAnalyzer: HarmonicKeyAnalyzer,
) {
    @Volatile
    var automixActive = false

    fun calculateBeatIntervalMs(bpm: Int): Long {
        val safeBpm = bpm.coerceIn(60, 200)
        return 60_000L / safeBpm
    }

    fun calculatePhraseDurationMs(bpm: Int, bars: Int = 8): Long {
        val beatMs = calculateBeatIntervalMs(bpm)
        return beatMs * 4 * bars.coerceIn(4, 16)
    }

    fun evaluateAutomixTransition(
        positionMs: Long,
        durationMs: Long,
        currentBpm: Int,
        nextBpm: Int,
        bars: Int = 8,
    ): Boolean {
        if (!automixActive || durationMs <= 0L) return false
        val phraseMs = calculatePhraseDurationMs(currentBpm, bars)
        val remainingMs = durationMs - positionMs

        if (remainingMs <= phraseMs && remainingMs > 0L) {
            if (currentBpm > 0 && nextBpm > 0) {
                val speedRatio = (nextBpm.toFloat() / currentBpm.toFloat()).coerceIn(0.85f, 1.15f)
                musicPlayer.setPlaybackSpeed(speedRatio)
            }
            return true
        }
        return false
    }

    companion object {
        @Volatile
        private var defaultInstance: AutomixEngine? = null

        fun getInstance(context: Context, musicPlayer: MusicPlayer, harmonicKeyAnalyzer: HarmonicKeyAnalyzer): AutomixEngine {
            return defaultInstance ?: synchronized(this) {
                defaultInstance ?: AutomixEngine(context.applicationContext, musicPlayer, harmonicKeyAnalyzer).also { defaultInstance = it }
            }
        }
    }
}
