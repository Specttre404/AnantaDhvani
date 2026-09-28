package com.lastwave.app.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
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
    val BASS_BOOST = EqPreset("Bass Boost", listOf(
        6.0f, 6.0f, 6.0f, 5.5f, 5.0f, 4.2f, 3.5f, 2.5f, 1.5f, 0.8f,
        0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f,
        0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f
    ))
    val BASS_REDUCER = EqPreset("Bass Reducer", listOf(
        -6.0f, -6.0f, -6.0f, -5.5f, -5.0f, -4.0f, -3.0f, -2.0f, -1.0f, -0.5f,
        0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f,
        0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f
    ))
    val TREBLE_BOOST = EqPreset("Treble Boost", listOf(
        0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f,
        0f, 0f, 0f, 0f, 0f, 0.2f, 0.5f, 1.0f, 1.5f, 2.2f,
        3.0f, 3.7f, 4.5f, 5.2f, 6.0f, 6.5f, 7.0f, 7.5f, 8.0f, 8.5f, 9.0f
    ))
    val VOCAL_ENHANCER = EqPreset("Vocal Enhancer", listOf(
        -1.5f, -1.2f, -1.0f, -0.7f, -0.5f, -0.2f, 0f, 0.2f, 0.5f, 1.0f,
        1.5f, 2.2f, 3.0f, 3.7f, 4.5f, 4.2f, 4.0f, 3.5f, 3.0f, 2.5f,
        2.0f, 1.5f, 1.0f, 0.7f, 0.5f, 0.5f, 0.5f, 0.2f, 0f, 0f, 0f
    ))
    val ACOUSTIC = EqPreset("Acoustic", listOf(
        3.0f, 2.8f, 2.5f, 2.2f, 2.0f, 1.7f, 1.5f, 1.0f, 0.5f, 0.7f,
        1.0f, 1.2f, 1.5f, 1.7f, 2.0f, 2.0f, 2.0f, 1.7f, 1.5f, 1.2f,
        1.0f, 1.0f, 1.0f, 1.2f, 1.5f, 1.5f, 1.5f, 1.5f, 1.5f, 1.5f, 1.5f
    ))
    val ROCK = EqPreset("Rock", listOf(
        4.0f, 3.8f, 3.5f, 3.2f, 3.0f, 2.2f, 1.5f, 0.7f, 0f, -0.5f,
        -1.0f, -1.0f, -1.0f, -0.7f, -0.5f, 0f, 0.5f, 1.0f, 1.5f, 2.0f,
        2.5f, 2.7f, 3.0f, 3.2f, 3.5f, 3.7f, 4.0f, 4.0f, 4.0f, 4.0f, 4.0f
    ))
    val ELECTRONIC_EDM = EqPreset("Electronic / EDM", listOf(
        5.0f, 4.7f, 4.5f, 4.0f, 3.5f, 2.7f, 2.0f, 1.2f, 0.5f, 0f,
        -0.5f, -0.8f, -1.0f, -0.5f, 0f, 0.5f, 1.0f, 1.7f, 2.5f, 2.8f,
        3.0f, 3.0f, 3.0f, 2.7f, 2.5f, 3.0f, 3.5f, 4.0f, 4.5f, 4.8f, 5.0f
    ))
    val HIP_HOP = EqPreset("Hip-Hop", listOf(
        5.5f, 5.2f, 5.0f, 4.5f, 4.0f, 3.2f, 2.5f, 1.7f, 1.0f, 0.2f,
        -0.5f, -1.0f, -1.5f, -1.0f, -0.5f, 0.2f, 1.0f, 1.5f, 2.0f, 2.0f,
        2.0f, 2.2f, 2.5f, 2.7f, 3.0f, 3.2f, 3.5f, 3.7f, 4.0f, 4.0f, 4.0f
    ))
    val CLASSICAL = EqPreset("Classical", listOf(
        3.0f, 2.8f, 2.5f, 2.2f, 2.0f, 1.7f, 1.5f, 1.0f, 0.5f, 0.2f,
        0f, 0f, 0f, 0f, 0f, 0.2f, 0.5f, 0.7f, 1.0f, 1.2f,
        1.5f, 1.7f, 2.0f, 2.2f, 2.5f, 2.7f, 3.0f, 3.0f, 3.0f, 3.0f, 3.0f
    ))
    val JAZZ = EqPreset("Jazz", listOf(
        2.5f, 2.2f, 2.0f, 1.7f, 1.5f, 1.2f, 1.0f, 0.5f, 0f, -0.2f,
        -0.5f, -0.3f, 0f, 0.2f, 0.5f, 0.7f, 1.0f, 1.2f, 1.5f, 1.7f,
        2.0f, 2.0f, 2.0f, 2.2f, 2.5f, 2.5f, 2.5f, 2.5f, 2.5f, 2.5f, 2.5f
    ))
    val METAL = EqPreset("Metal", listOf(
        4.5f, 4.2f, 4.0f, 3.7f, 3.5f, 2.7f, 2.0f, 1.2f, 0.5f, -0.5f,
        -1.5f, -1.8f, -2.0f, -1.5f, -1.0f, -0.2f, 0.5f, 1.2f, 2.0f, 2.5f,
        3.0f, 3.5f, 4.0f, 4.2f, 4.5f, 4.7f, 5.0f, 5.0f, 5.0f, 5.0f, 5.0f
    ))
    val LOUNGE = EqPreset("Lounge", listOf(
        1.5f, 1.5f, 1.5f, 1.2f, 1.0f, 0.7f, 0.5f, 0.2f, 0f, -0.2f,
        -0.5f, -0.2f, 0f, 0.2f, 0.5f, 0.7f, 1.0f, 1.2f, 1.5f, 1.7f,
        2.0f, 2.0f, 2.0f, 1.7f, 1.5f, 1.2f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f
    ))
    val RNB = EqPreset("R&B", listOf(
        5.0f, 4.7f, 4.5f, 4.0f, 3.5f, 2.7f, 2.0f, 1.5f, 1.0f, 0.2f,
        -0.5f, -0.8f, -1.0f, -0.8f, -0.5f, 0f, 0.5f, 1.0f, 1.5f, 1.7f,
        2.0f, 2.2f, 2.5f, 2.7f, 3.0f, 3.0f, 3.0f, 3.0f, 3.0f, 3.0f, 3.0f
    ))
    val CLUB = EqPreset("Club", listOf(
        5.5f, 5.2f, 5.0f, 4.5f, 4.0f, 3.2f, 2.5f, 1.7f, 1.0f, 0.5f,
        0f, -0.2f, -0.5f, -0.2f, 0f, 0.7f, 1.5f, 2.2f, 3.0f, 3.2f,
        3.5f, 3.7f, 4.0f, 4.2f, 4.5f, 4.7f, 5.0f, 5.0f, 5.0f, 5.0f, 5.0f
    ))
    val DEEP_HOUSE = EqPreset("Deep House", listOf(
        6.0f, 5.7f, 5.5f, 5.0f, 4.5f, 3.7f, 3.0f, 2.0f, 1.0f, 0.2f,
        -0.5f, -0.8f, -1.0f, -0.5f, 0f, 0.5f, 1.0f, 1.5f, 2.0f, 2.2f,
        2.5f, 2.7f, 3.0f, 3.2f, 3.5f, 3.7f, 4.0f, 4.2f, 4.5f, 4.8f, 5.0f
    ))

    val ALL: List<EqPreset> = listOf(
        FLAT,
        STUDIO_MASTER,
        BASS_BOOST,
        BASS_REDUCER,
        TREBLE_BOOST,
        VOCAL_ENHANCER,
        ACOUSTIC,
        ROCK,
        ELECTRONIC_EDM,
        HIP_HOP,
        CLASSICAL,
        JAZZ,
        METAL,
        LOUNGE,
        RNB,
        CLUB,
        DEEP_HOUSE,
    )

    fun byName(name: String): EqPreset? = ALL.firstOrNull { it.name.equals(name, ignoreCase = true) }
}

data class EqualizerSettings(
    val enabled: Boolean = false,
    val presetName: String = EqualizerPresets.FLAT.name,
    val preampDb: Float = 0f,
    val gainsDb: List<Float> = EqualizerPresets.FLAT.gainsDb,
)

@Singleton
class EqualizerPreferences @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) {
    private object Keys {
        val ENABLED = booleanPreferencesKey("lw_eq_enabled")
        val PRESET_NAME = stringPreferencesKey("lw_eq_preset")
        val PREAMP_DB = floatPreferencesKey("lw_eq_preamp")
        val GAINS_DB = stringPreferencesKey("lw_eq_gains")
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
            EqualizerSettings(
                enabled = p.readSafely(Keys.ENABLED) ?: false,
                presetName = resolvedName,
                preampDb = preamp,
                gainsDb = gains,
            )
        }

    suspend fun setEnabled(enabled: Boolean) {
        dataStore.edit { it[Keys.ENABLED] = enabled }
    }

    suspend fun setPreampDb(db: Float) {
        val safeDb = if (db.isFinite()) db.coerceIn(-EQ_MAX_PREAMP_DB, EQ_MAX_PREAMP_DB) else 0f
        dataStore.edit { it[Keys.PREAMP_DB] = safeDb }
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
    }

    private fun encodeGains(gains: List<Float>): String =
        gains.joinToString(",") { gain ->
            "%.1f".format(java.util.Locale.ROOT, if (gain.isFinite()) gain.coerceIn(-EQ_MAX_GAIN_DB, EQ_MAX_GAIN_DB) else 0f)
        }

    fun exportCustomEqJson(settings: EqualizerSettings): String {
        val gainsStr = settings.gainsDb.joinToString(",") { "%.1f".format(java.util.Locale.ROOT, it) }
        return """
            {
              "version": "1.2.1",
              "presetName": "${settings.presetName}",
              "enabled": ${settings.enabled},
              "preampDb": %.1f,
              "bandCount": 31,
              "gainsDb": [$gainsStr]
            }
        """.trimIndent().format(java.util.Locale.ROOT, settings.preampDb)
    }

    fun parseCustomEqJson(jsonString: String): EqualizerSettings? {
        return try {
            val enabled = jsonString.contains("\"enabled\": true") || jsonString.contains("\"enabled\":true")
            val preampMatch = Regex("\"preampDb\":\\s*(-?\\d+(\\.\\d+)?)").find(jsonString)
            val preamp = preampMatch?.groupValues?.get(1)?.toFloatOrNull()?.coerceIn(-10f, 10f) ?: 0f

            val gainsMatch = Regex("\"gainsDb\":\\s*\\[(.*?)\\]", RegexOption.DOT_MATCHES_ALL).find(jsonString)
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
                gainsDb = gainsList
            )
        } catch (_: Exception) {
            null
        }
    }
}
