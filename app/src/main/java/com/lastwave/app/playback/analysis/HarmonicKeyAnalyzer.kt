package com.lastwave.app.playback.analysis

import javax.inject.Inject
import javax.inject.Singleton

data class CamelotKeyInfo(
    val camelotCode: String,
    val musicalKey: String,
    val isMinor: Boolean,
    val number: Int,
)

object CamelotWheel {
    val keys = listOf(
        CamelotKeyInfo("1A", "G# Minor", true, 1),
        CamelotKeyInfo("1B", "B Major", false, 1),
        CamelotKeyInfo("2A", "D# Minor", true, 2),
        CamelotKeyInfo("2B", "F# Major", false, 2),
        CamelotKeyInfo("3A", "A# Minor", true, 3),
        CamelotKeyInfo("3B", "C# Major", false, 3),
        CamelotKeyInfo("4A", "F Minor", true, 4),
        CamelotKeyInfo("4B", "G# Major", false, 4),
        CamelotKeyInfo("5A", "C Minor", true, 5),
        CamelotKeyInfo("5B", "D# Major", false, 5),
        CamelotKeyInfo("6A", "G Minor", true, 6),
        CamelotKeyInfo("6B", "A# Major", false, 6),
        CamelotKeyInfo("7A", "D Minor", true, 7),
        CamelotKeyInfo("7B", "F Major", false, 7),
        CamelotKeyInfo("8A", "A Minor", true, 8),
        CamelotKeyInfo("8B", "C Major", false, 8),
        CamelotKeyInfo("9A", "E Minor", true, 9),
        CamelotKeyInfo("9B", "G Major", false, 9),
        CamelotKeyInfo("10A", "B Minor", true, 10),
        CamelotKeyInfo("10B", "D Major", false, 10),
        CamelotKeyInfo("11A", "F# Minor", true, 11),
        CamelotKeyInfo("11B", "A Major", false, 11),
        CamelotKeyInfo("12A", "C# Minor", true, 12),
        CamelotKeyInfo("12B", "E Major", false, 12)
    )

    fun getCompatibleKeys(camelotCode: String): List<String> {
        val info = keys.firstOrNull { it.camelotCode.equals(camelotCode, ignoreCase = true) } ?: return emptyList()
        val num = info.number
        val letter = if (info.isMinor) "A" else "B"
        val oppositeLetter = if (info.isMinor) "B" else "A"

        val plus1 = if (num == 12) 1 else num + 1
        val minus1 = if (num == 1) 12 else num - 1

        return listOf(
            "$num$letter",
            "$plus1$letter",
            "$minus1$letter",
            "$num$oppositeLetter"
        )
    }
}

data class HarmonicKeyResult(
    val camelotKey: String,
    val musicalKey: String,
    val bpm: Int,
    val compatibleCamelotKeys: List<String>,
)

@Singleton
class HarmonicKeyAnalyzer @Inject constructor() {

    init {
        defaultInstance = this
    }

    fun analyzeTrackKey(title: String, artist: String): HarmonicKeyResult {
        val hash = kotlin.math.abs("$title|$artist".hashCode())
        val keyIndex = hash % CamelotWheel.keys.size
        val info = CamelotWheel.keys[keyIndex]
        val bpm = 60 + (hash % 121)
        val compatible = CamelotWheel.getCompatibleKeys(info.camelotCode)

        return HarmonicKeyResult(
            camelotKey = info.camelotCode,
            musicalKey = info.musicalKey,
            bpm = bpm,
            compatibleCamelotKeys = compatible
        )
    }

    companion object {
        @Volatile
        private var defaultInstance: HarmonicKeyAnalyzer? = null

        fun getInstance(): HarmonicKeyAnalyzer {
            return defaultInstance ?: synchronized(this) {
                defaultInstance ?: HarmonicKeyAnalyzer().also { defaultInstance = it }
            }
        }
    }
}
