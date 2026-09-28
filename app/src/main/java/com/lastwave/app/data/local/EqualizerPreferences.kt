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

/** The 15 studio-grade center frequencies (Hz) spanning 32 Hz to 16 kHz with high-resolution sub-bands. */
val EQ_BAND_FREQS_HZ = intArrayOf(32, 48, 64, 96, 125, 180, 250, 350, 500, 750, 1000, 2000, 4000, 8000, 16000)
const val EQ_MAX_GAIN_DB = 12f
const val EQ_MAX_PREAMP_DB = 10f

/** Compact frequency label under each band slider ("32", "48", "1K", "16K"...). */
fun eqBandLabel(hz: Int): String = if (hz >= 1000) "${hz / 1000}K" else "$hz"

data class EqPreset(
    val name: String,
    val gainsDb: List<Float>,
)

object EqualizerPresets {
    const val CUSTOM_NAME = "Custom"

    val FLAT = EqPreset("Flat", List(EQ_BAND_FREQS_HZ.size) { 0f })
    val STUDIO_MASTER = EqPreset(
        "Studio Master",
        listOf(2.6f, 2.8f, 2.2f, 0.6f, -1.8f, -2.6f, -1.2f, 0.0f, 1.2f, 2.4f, 3.6f, 4.0f, 4.2f, 4.5f, 4.8f),
    )
    val BASS_BOOST = EqPreset("Bass Boost", listOf(6.0f, 6.0f, 5.0f, 3.5f, 1.5f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f))
    val BASS_REDUCER = EqPreset("Bass Reducer", listOf(-6.0f, -6.0f, -5.0f, -3.0f, -1.0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f))
    val TREBLE_BOOST = EqPreset("Treble Boost", listOf(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0.5f, 1.5f, 3.0f, 4.5f, 6.0f, 7.0f, 8.0f, 9.0f))
    val VOCAL_ENHANCER = EqPreset("Vocal Enhancer", listOf(-1.5f, -1.0f, -0.5f, 0f, 0.5f, 1.5f, 3.0f, 4.5f, 4.0f, 3.0f, 2.0f, 1.0f, 0.5f, 0.5f, 0f))
    val ACOUSTIC = EqPreset("Acoustic", listOf(3.0f, 2.5f, 2.0f, 1.5f, 0.5f, 1.0f, 1.5f, 2.0f, 2.0f, 1.5f, 1.0f, 1.0f, 1.0f, 1.5f, 1.5f))
    val ROCK = EqPreset("Rock", listOf(4.0f, 3.5f, 3.0f, 1.5f, 0f, -1.0f, -1.0f, -0.5f, 0.5f, 1.5f, 2.5f, 3.0f, 3.5f, 4.0f, 4.0f))
    val ELECTRONIC_EDM = EqPreset("Electronic / EDM", listOf(5.0f, 4.5f, 3.5f, 2.0f, 0.5f, -1.0f, -0.5f, 1.0f, 2.5f, 3.0f, 3.0f, 2.5f, 3.5f, 4.5f, 5.0f))
    val HIP_HOP = EqPreset("Hip-Hop", listOf(5.5f, 5.0f, 4.0f, 2.5f, 1.0f, -0.5f, -1.5f, -0.5f, 1.0f, 2.0f, 2.0f, 2.5f, 3.0f, 3.5f, 4.0f))
    val CLASSICAL = EqPreset("Classical", listOf(3.0f, 2.5f, 2.0f, 1.5f, 0.5f, 0f, 0f, 0f, 0.5f, 1.0f, 1.5f, 2.0f, 2.5f, 3.0f, 3.0f))
    val JAZZ = EqPreset("Jazz", listOf(2.5f, 2.0f, 1.5f, 1.0f, 0f, -0.5f, 0f, 0.5f, 1.0f, 1.5f, 2.0f, 2.0f, 2.5f, 2.5f, 2.5f))
    val METAL = EqPreset("Metal", listOf(4.5f, 4.0f, 3.5f, 2.0f, 0.5f, -1.5f, -2.0f, -1.0f, 0.5f, 2.0f, 3.0f, 4.0f, 4.5f, 5.0f, 5.0f))
    val LOUNGE = EqPreset("Lounge", listOf(1.5f, 1.5f, 1.0f, 0.5f, 0f, -0.5f, 0f, 0.5f, 1.0f, 1.5f, 2.0f, 2.0f, 1.5f, 1.0f, 1.0f))
    val RNB = EqPreset("R&B", listOf(5.0f, 4.5f, 3.5f, 2.0f, 1.0f, -0.5f, -1.0f, -0.5f, 0.5f, 1.5f, 2.0f, 2.5f, 3.0f, 3.0f, 3.0f))
    val CLUB = EqPreset("Club", listOf(5.5f, 5.0f, 4.0f, 2.5f, 1.0f, 0f, -0.5f, 0f, 1.5f, 3.0f, 3.5f, 4.0f, 4.5f, 5.0f, 5.0f))
    val DEEP_HOUSE = EqPreset("Deep House", listOf(6.0f, 5.5f, 4.5f, 3.0f, 1.0f, -0.5f, -1.0f, 0f, 1.0f, 2.0f, 2.5f, 3.0f, 3.5f, 4.0f, 4.5f))

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
}
