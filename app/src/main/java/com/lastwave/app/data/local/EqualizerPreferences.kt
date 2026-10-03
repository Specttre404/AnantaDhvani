package com.lastwave.app.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/** The 31 ISO 1/3-octave center frequencies (Hz) spanning 20 Hz to 20 kHz. */
val EQ_BAND_FREQS_HZ = floatArrayOf(
    20f, 25f, 31.5f, 40f, 50f, 63f, 80f, 100f, 125f, 160f,
    200f, 250f, 315f, 400f, 500f, 630f, 800f, 1000f, 1250f, 1600f,
    2000f, 2500f, 3150f, 4000f, 5000f, 6300f, 8000f, 10000f, 12500f, 16000f, 20000f
)
const val EQ_MAX_GAIN_DB = 12f
const val EQ_MAX_PREAMP_DB = 10f

/** Compact frequency label under each band slider ("20", "31.5", "1K", "20K"...). */
fun eqBandLabel(hz: Float): String = when {
    hz >= 1000f -> if (hz % 1000f == 0f) "${(hz / 1000f).toInt()}K" else "%.1fK".format(java.util.Locale.ROOT, hz / 1000f)
    hz == 31.5f -> "31.5"
    else -> "${hz.toInt()}"
}

data class EqPreset(
    val name: String,
    val gainsDb: List<Float>,
)

object EqualizerPresets {
    const val CUSTOM_NAME = "Custom"

    val FLAT = EqPreset("Flat", List(EQ_BAND_FREQS_HZ.size) { 0f })

    val STUDIO_MASTER = EqPreset("Studio Master", listOf(
        2.6f, 2.7f, 2.8f, 2.5f, 2.2f, 1.4f, 0.6f, -0.6f, -1.8f, -2.2f,
        -2.6f, -1.9f, -1.2f, -0.6f, 0.0f, 0.6f, 1.2f, 1.8f, 2.4f, 3.0f,
        3.6f, 3.8f, 4.0f, 4.1f, 4.2f, 4.35f, 4.5f, 4.65f, 4.8f, 4.9f, 5.0f
    ))

    val HARMAN_TARGET = EqPreset("Harman Target", listOf(
        5.5f, 5.8f, 6.0f, 5.8f, 5.2f, 4.0f, 2.8f, 1.8f, 0.8f, 0.2f,
        0.0f, 0.0f, 0.0f, 0.2f, 0.5f, 1.0f, 1.8f, 2.5f, 3.2f, 4.0f,
        3.5f, 2.0f, 1.0f, -0.5f, -1.5f, -2.0f, -2.5f, -3.0f, -3.5f, -4.0f, -4.5f
    ))

    val DIFFUSE_FIELD = EqPreset("Diffuse-Field", listOf(
        0.0f, 0.0f, 0.0f, 0.0f, 0.2f, 0.5f, 0.8f, 1.2f, 1.8f, 2.5f,
        3.2f, 4.0f, 4.8f, 5.5f, 6.2f, 6.8f, 7.2f, 7.5f, 6.5f, 5.0f,
        3.5f, 2.0f, 1.0f, 1.5f, 2.5f, 3.8f, 4.5f, 3.0f, 1.0f, -1.0f, -3.0f
    ))

    val ACOUSTIC_STRINGS = EqPreset("Acoustic Strings", listOf(
        1.5f, 1.5f, 1.2f, 1.0f, 0.8f, 0.5f, 0.2f, 0.0f, -0.2f, -0.5f,
        -0.2f, 0.2f, 0.8f, 1.5f, 2.2f, 2.8f, 3.2f, 3.5f, 3.2f, 2.8f,
        2.5f, 2.8f, 3.2f, 3.8f, 4.2f, 4.5f, 4.2f, 3.8f, 3.2f, 2.5f, 2.0f
    ))

    val CLASSICAL_HALL = EqPreset("Classical Hall", listOf(
        2.5f, 2.5f, 2.2f, 2.0f, 1.8f, 1.5f, 1.2f, 0.8f, 0.4f, 0.0f,
        -0.2f, -0.2f, 0.0f, 0.2f, 0.5f, 0.8f, 1.2f, 1.5f, 1.8f, 2.2f,
        2.5f, 2.8f, 3.0f, 3.2f, 3.5f, 3.5f, 3.2f, 2.8f, 2.2f, 1.5f, 1.0f
    ))

    val SMOOTH_JAZZ = EqPreset("Smooth Jazz", listOf(
        3.0f, 3.0f, 2.8f, 2.5f, 2.2f, 1.8f, 1.2f, 0.6f, 0.0f, -0.4f,
        -0.8f, -0.6f, -0.2f, 0.2f, 0.6f, 1.0f, 1.5f, 1.8f, 2.2f, 2.5f,
        2.5f, 2.2f, 1.8f, 1.5f, 1.2f, 1.0f, 0.8f, 0.5f, 0.2f, 0.0f, -0.5f
    ))

    val BASS_PUNCH = EqPreset("Bass Punch", listOf(
        4.0f, 4.5f, 5.2f, 6.0f, 6.5f, 5.8f, 4.2f, 2.5f, 1.2f, 0.2f,
        -0.5f, -0.8f, -0.5f, 0.0f, 0.2f, 0.5f, 0.8f, 1.0f, 1.2f, 1.5f,
        1.5f, 1.2f, 1.0f, 0.8f, 0.5f, 0.2f, 0.0f, -0.2f, -0.5f, -0.8f, -1.0f
    ))

    val SUB_BASS_RUMBLE = EqPreset("Sub-Bass Rumble", listOf(
        8.0f, 8.5f, 8.2f, 7.0f, 5.5f, 3.8f, 2.2f, 1.0f, 0.2f, -0.5f,
        -1.0f, -1.2f, -1.0f, -0.5f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f,
        0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f
    ))

    val BASS_CUT = EqPreset("Bass Cut", listOf(
        -8.0f, -8.0f, -7.5f, -6.8f, -5.8f, -4.5f, -3.2f, -2.0f, -1.0f, -0.2f,
        0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f,
        0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f
    ))

    val VOCAL_WARMTH = EqPreset("Vocal Warmth", listOf(
        -1.0f, -1.0f, -0.8f, -0.5f, -0.2f, 0.2f, 0.8f, 1.5f, 2.2f, 2.8f,
        3.2f, 3.5f, 3.8f, 4.0f, 3.8f, 3.5f, 3.0f, 2.5f, 2.0f, 1.5f,
        1.2f, 1.0f, 0.8f, 0.5f, 0.2f, 0.0f, -0.2f, -0.5f, -0.8f, -1.0f, -1.2f
    ))

    val HARD_ROCK = EqPreset("Hard Rock", listOf(
        4.5f, 4.8f, 4.5f, 3.8f, 3.0f, 2.0f, 1.0f, 0.0f, -0.8f, -1.5f,
        -1.8f, -1.5f, -0.8f, 0.0f, 0.8f, 1.5f, 2.2f, 2.8f, 3.2f, 3.8f,
        4.2f, 4.5f, 4.8f, 5.0f, 5.2f, 5.5f, 5.2f, 4.8f, 4.2f, 3.5f, 3.0f
    ))

    val HEAVY_METAL = EqPreset("Heavy Metal", listOf(
        5.2f, 5.5f, 5.2f, 4.2f, 3.0f, 1.8f, 0.5f, -0.8f, -1.8f, -2.5f,
        -2.8f, -2.5f, -1.8f, -0.8f, 0.2f, 1.2f, 2.2f, 3.0f, 3.8f, 4.5f,
        5.0f, 5.5f, 5.8f, 6.0f, 6.2f, 6.5f, 6.2f, 5.8f, 5.2f, 4.5f, 4.0f
    ))

    val EDM_CLUB = EqPreset("EDM / Club", listOf(
        6.0f, 6.2f, 6.5f, 5.8f, 4.8f, 3.5f, 2.2f, 1.0f, 0.0f, -0.8f,
        -1.2f, -1.0f, -0.5f, 0.2f, 0.8f, 1.5f, 2.2f, 2.8f, 3.5f, 4.2f,
        4.8f, 5.2f, 5.5f, 5.8f, 6.0f, 6.2f, 5.8f, 5.2f, 4.5f, 3.8f, 3.2f
    ))

    val HIP_HOP_808 = EqPreset("Hip-Hop 808", listOf(
        7.2f, 7.5f, 7.8f, 6.8f, 5.2f, 3.5f, 2.0f, 0.8f, -0.2f, -1.0f,
        -1.5f, -1.2f, -0.8f, -0.2f, 0.5f, 1.2f, 1.8f, 2.2f, 2.5f, 2.8f,
        3.0f, 3.2f, 3.5f, 3.8f, 4.0f, 4.2f, 3.8f, 3.2f, 2.5f, 1.8f, 1.2f
    ))

    val RNB_SOUL = EqPreset("R&B Soul", listOf(
        4.8f, 5.2f, 5.0f, 4.2f, 3.2f, 2.2f, 1.2f, 0.5f, -0.2f, -0.8f,
        -1.0f, -0.8f, -0.2f, 0.5f, 1.2f, 1.8f, 2.2f, 2.5f, 2.8f, 3.0f,
        3.2f, 3.5f, 3.8f, 4.0f, 3.8f, 3.5f, 3.0f, 2.5f, 2.0f, 1.5f, 1.0f
    ))

    val LOUNGE = EqPreset("Lounge", listOf(
        2.0f, 2.2f, 2.0f, 1.8f, 1.5f, 1.2f, 0.8f, 0.4f, 0.0f, -0.4f,
        -0.8f, -0.5f, 0.0f, 0.4f, 0.8f, 1.2f, 1.5f, 1.8f, 2.0f, 2.2f,
        2.2f, 2.0f, 1.8f, 1.5f, 1.2f, 1.0f, 0.8f, 0.5f, 0.2f, 0.0f, -0.5f
    ))

    val PODCAST_VOICE = EqPreset("Podcast Voice", listOf(
        -8.0f, -8.0f, -7.0f, -5.5f, -4.0f, -2.5f, -1.2f, 0.0f, 1.0f, 2.0f,
        3.0f, 3.8f, 4.2f, 4.5f, 4.2f, 3.8f, 3.2f, 2.5f, 1.8f, 1.2f,
        0.8f, 0.5f, 0.2f, 0.0f, -0.5f, -1.0f, -1.8f, -2.5f, -3.5f, -4.5f, -5.5f
    ))

    val ALL: List<EqPreset> = listOf(
        FLAT,
        STUDIO_MASTER,
        HARMAN_TARGET,
        DIFFUSE_FIELD,
        ACOUSTIC_STRINGS,
        CLASSICAL_HALL,
        SMOOTH_JAZZ,
        BASS_PUNCH,
        SUB_BASS_RUMBLE,
        BASS_CUT,
        VOCAL_WARMTH,
        HARD_ROCK,
        HEAVY_METAL,
        EDM_CLUB,
        HIP_HOP_808,
        RNB_SOUL,
        LOUNGE,
        PODCAST_VOICE,
    )

    fun byName(name: String): EqPreset? = ALL.firstOrNull { it.name.equals(name, ignoreCase = true) }
}

data class EqualizerSettings(
    val enabled: Boolean = false,
    val presetName: String = EqualizerPresets.FLAT.name,
    val preampDb: Float = 0f,
    val filterQ: Float = 1.414f,
    val gainsDb: List<Float> = EqualizerPresets.FLAT.gainsDb,
    val bandQValues: List<Float> = List(EQ_BAND_FREQS_HZ.size) { 1.414f },
)

@Singleton
class EqualizerPreferences @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) {
    private object Keys {
        val ENABLED = booleanPreferencesKey("lw_eq_enabled")
        val PRESET_NAME = stringPreferencesKey("lw_eq_preset")
        val PREAMP_DB = floatPreferencesKey("lw_eq_preamp")
        val FILTER_Q = floatPreferencesKey("lw_eq_filter_q")
        val GAINS_DB = stringPreferencesKey("lw_eq_gains")
        val BAND_QS = stringPreferencesKey("lw_eq_band_qs")
    }

    val settings: Flow<EqualizerSettings> = dataStore.data
        .recoverPreferences("EqualizerPreferences")
        .map { p ->
            val storedGains = p.readSafely(Keys.GAINS_DB)?.split(',')?.mapNotNull(String::toFloatOrNull)
                ?.takeIf { it.size == EQ_BAND_FREQS_HZ.size }
                ?.map { gain -> if (gain.isFinite()) gain.coerceIn(-EQ_MAX_GAIN_DB, EQ_MAX_GAIN_DB) else 0f }
            val resolvedName = p.readSafely(Keys.PRESET_NAME)
                ?.let { name ->
                    if (name == EqualizerPresets.CUSTOM_NAME || EqualizerPresets.byName(name) != null) name else null
                }
                ?: EqualizerPresets.FLAT.name
            val gains = storedGains
                ?: EqualizerPresets.byName(resolvedName)?.gainsDb
                ?: EqualizerPresets.FLAT.gainsDb
            val preamp = p.readSafely(Keys.PREAMP_DB)?.takeIf { it.isFinite() }?.coerceIn(-EQ_MAX_PREAMP_DB, EQ_MAX_PREAMP_DB) ?: 0f
            val q = p.readSafely(Keys.FILTER_Q)?.takeIf { it.isFinite() }?.coerceIn(0.1f, 10.0f) ?: 1.414f
            val storedQs = p.readSafely(Keys.BAND_QS)?.split(',')?.mapNotNull(String::toFloatOrNull)
                ?.takeIf { it.size == EQ_BAND_FREQS_HZ.size }
                ?.map { bandQ -> if (bandQ.isFinite()) bandQ.coerceIn(0.1f, 10.0f) else 1.414f }
                ?: List(EQ_BAND_FREQS_HZ.size) { q }
            EqualizerSettings(
                enabled = p.readSafely(Keys.ENABLED) ?: false,
                presetName = resolvedName,
                preampDb = preamp,
                filterQ = q,
                gainsDb = gains,
                bandQValues = storedQs,
            )
        }

    suspend fun setEnabled(enabled: Boolean) {
        dataStore.edit { it[Keys.ENABLED] = enabled }
    }

    suspend fun setPreampDb(db: Float) {
        val safeDb = if (db.isFinite()) db.coerceIn(-EQ_MAX_PREAMP_DB, EQ_MAX_PREAMP_DB) else 0f
        dataStore.edit { it[Keys.PREAMP_DB] = safeDb }
    }

    suspend fun applyCustomSettings(presetName: String, gainsDb: List<Float>, preampDb: Float = 0f) {
        dataStore.edit {
            it[Keys.PRESET_NAME] = presetName
            it[Keys.GAINS_DB] = gainsDb.joinToString(",")
            it[Keys.PREAMP_DB] = preampDb
        }
    }

    fun generateShareCode(settings: EqualizerSettings): String {
        val gainsStr = settings.gainsDb.joinToString(",") { "%.1f".format(it) }
        return android.util.Base64.encodeToString(gainsStr.toByteArray(), android.util.Base64.NO_WRAP)
    }

    fun parseShareCode(code: String): List<Float>? {
        return runCatching {
            val decoded = String(android.util.Base64.decode(code.trim(), android.util.Base64.NO_WRAP))
            decoded.split(",").mapNotNull { it.toFloatOrNull() }.takeIf { it.size == EQ_BAND_FREQS_HZ.size }
        }.getOrNull()
    }

    suspend fun setSingleBandQ(bandIndex: Int, q: Float) {
        if (bandIndex !in 0 until EQ_BAND_FREQS_HZ.size) return
        val currentQs = settings.first().bandQValues.toMutableList()
        currentQs[bandIndex] = if (q.isFinite()) q.coerceIn(0.1f, 10.0f) else 1.414f
        dataStore.edit { it[Keys.BAND_QS] = currentQs.joinToString(",") }
    }

    suspend fun setAllBandQValues(bandQs: List<Float>) {
        if (bandQs.size != EQ_BAND_FREQS_HZ.size) return
        val safeQs = bandQs.map { if (it.isFinite()) it.coerceIn(0.1f, 10.0f) else 1.414f }
        dataStore.edit { it[Keys.BAND_QS] = safeQs.joinToString(",") }
    }

    suspend fun setFilterQ(q: Float) {
        val safeQ = if (q.isFinite()) q.coerceIn(0.5f, 2.8f) else 1.414f
        dataStore.edit { it[Keys.FILTER_Q] = safeQ }
    }

    suspend fun applyPreset(preset: EqPreset) {
        dataStore.edit {
            it[Keys.PRESET_NAME] = preset.name
            it[Keys.GAINS_DB] = encodeGains(preset.gainsDb)
            it[Keys.ENABLED] = true
        }
    }

    suspend fun applyCustomSettings(settings: EqualizerSettings) {
        dataStore.edit {
            it[Keys.ENABLED] = settings.enabled
            it[Keys.PRESET_NAME] = settings.presetName
            it[Keys.PREAMP_DB] = settings.preampDb.coerceIn(-EQ_MAX_PREAMP_DB, EQ_MAX_PREAMP_DB)
            it[Keys.FILTER_Q] = settings.filterQ.coerceIn(0.5f, 2.8f)
            it[Keys.GAINS_DB] = encodeGains(settings.gainsDb)
        }
    }

    suspend fun setBandGain(bandIndex: Int, gainDb: Float) {
        if (bandIndex !in EQ_BAND_FREQS_HZ.indices) return
        dataStore.edit {
            val current = it.readSafely(Keys.GAINS_DB)?.split(',')?.mapNotNull { v -> v.toFloatOrNull() }
                ?.takeIf { v -> v.size == EQ_BAND_FREQS_HZ.size }
                ?: EqualizerPresets.FLAT.gainsDb
            val safeGain = if (gainDb.isFinite()) gainDb.coerceIn(-EQ_MAX_GAIN_DB, EQ_MAX_GAIN_DB) else 0f
            val next = current.toMutableList().also { list -> list[bandIndex] = safeGain }
            it[Keys.PRESET_NAME] = EqualizerPresets.CUSTOM_NAME
            it[Keys.GAINS_DB] = encodeGains(next)
        }
    }

    suspend fun reset() {
        applyPreset(EqualizerPresets.FLAT)
        setPreampDb(0f)
        setFilterQ(1.414f)
    }

    private fun encodeGains(gains: List<Float>): String =
        gains.joinToString(",") { gain ->
            "%.1f".format(java.util.Locale.ROOT, if (gain.isFinite()) gain.coerceIn(-EQ_MAX_GAIN_DB, EQ_MAX_GAIN_DB) else 0f)
        }

    fun exportCustomEqJson(settings: EqualizerSettings): String {
        val gainsStr = settings.gainsDb.joinToString(",") { "%.1f".format(java.util.Locale.ROOT, it) }
        return """
            {
              "version": "1.3.0",
              "presetName": "${settings.presetName}",
              "enabled": ${settings.enabled},
              "preampDb": %.1f,
              "filterQ": %.3f,
              "bandCount": 31,
              "gainsDb": [$gainsStr]
            }
        """.trimIndent().format(java.util.Locale.ROOT, settings.preampDb, settings.filterQ)
    }

    fun parseCustomEqJson(jsonString: String): EqualizerSettings? {
        return try {
            val enabled = jsonString.contains("\"enabled\": true") || jsonString.contains("\"enabled\":true")
            val preampMatch = Regex("\"preampDb\":\\s*(-?\\d+(\\.\\d+)?)").find(jsonString)
            val preamp = preampMatch?.groupValues?.get(1)?.toFloatOrNull()?.coerceIn(-10f, 10f) ?: 0f

            val filterQMatch = Regex("\"filterQ\":\\s*(\\d+(\\.\\d+)?)").find(jsonString)
            val filterQ = filterQMatch?.groupValues?.get(1)?.toFloatOrNull()?.coerceIn(0.5f, 2.8f) ?: 1.414f

            val gainsMatch = Regex("\"gainsDb\":\\s*\\[(.*?)]", RegexOption.DOT_MATCHES_ALL).find(jsonString)
            val gainsList = gainsMatch?.groupValues?.get(1)?.split(',')
                ?.mapNotNull { it.trim().toFloatOrNull() }
                ?.takeIf { it.size == EQ_BAND_FREQS_HZ.size }
                ?.map { it.coerceIn(-EQ_MAX_GAIN_DB, EQ_MAX_GAIN_DB) }
                ?: return null

            val presetNameMatch = Regex("\"presetName\":\\s*\"(.*?)\"").find(jsonString)
            val presetName = presetNameMatch?.groupValues?.get(1) ?: EqualizerPresets.CUSTOM_NAME

            EqualizerSettings(
                enabled = enabled,
                presetName = presetName,
                preampDb = preamp,
                filterQ = filterQ,
                gainsDb = gainsList,
            )
        } catch (_: Exception) {
            null
        }
    }
}
