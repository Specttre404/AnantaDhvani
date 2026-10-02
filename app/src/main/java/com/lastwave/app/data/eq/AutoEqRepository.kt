package com.lastwave.app.data.eq

import com.lastwave.app.data.local.EqualizerPreferences
import com.lastwave.app.data.local.EqualizerSettings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

data class AutoEqProfile(
    val name: String,
    val brand: String,
    val gainsDb: List<Float>,
)

@Singleton
class AutoEqRepository @Inject constructor(
    private val equalizerPreferences: EqualizerPreferences,
) {
    private val popularProfiles = listOf(
        AutoEqProfile("Sennheiser HD 600", "Sennheiser", listOf(0f, 0.5f, 1f, 1.5f, 1f, 0.5f, 0f, -0.5f, -1f, -1.5f, -1f, -0.5f, 0f, 0.5f, 1f, 1.5f, 1f, 0.5f, 0f, -0.5f, -1f, -1.5f, -1f, -0.5f, 0f, 0.5f, 1f, 0.5f, 0f, -0.5f, 0f)),
        AutoEqProfile("Sony WH-1000XM5", "Sony", listOf(1.5f, 2f, 2.5f, 2f, 1.5f, 1f, 0.5f, 0f, -0.5f, -1f, -1.5f, -2f, -1.5f, -1f, -0.5f, 0f, 0.5f, 1f, 1.5f, 2f, 1.5f, 1f, 0.5f, 0f, -0.5f, -1f, -0.5f, 0f, 0.5f, 1f, 0f)),
        AutoEqProfile("Moondrop Blessing 2", "Moondrop", listOf(-0.5f, 0f, 0.5f, 1f, 1.5f, 1f, 0.5f, 0f, -0.5f, -1f, -0.5f, 0f, 0.5f, 1f, 1.5f, 1f, 0.5f, 0f, -0.5f, -1f, -0.5f, 0f, 0.5f, 1f, 0.5f, 0f, -0.5f, 0f, 0.5f, 0f, 0f)),
        AutoEqProfile("Apple AirPods Pro 2", "Apple", listOf(1f, 1.5f, 1f, 0.5f, 0f, -0.5f, -1f, -0.5f, 0f, 0.5f, 1f, 0.5f, 0f, -0.5f, -1f, -0.5f, 0f, 0.5f, 1f, 0.5f, 0f, -0.5f, -1f, -0.5f, 0f, 0.5f, 1f, 0.5f, 0f, 0f, 0f)),
        AutoEqProfile("Audio-Technica ATH-M50x", "Audio-Technica", listOf(-1f, -1.5f, -1f, -0.5f, 0f, 0.5f, 1f, 1.5f, 1f, 0.5f, 0f, -0.5f, -1f, -0.5f, 0f, 0.5f, 1f, 0.5f, 0f, -0.5f, -1f, -0.5f, 0f, 0.5f, 1f, 0.5f, 0f, 0f, 0f, 0f, 0f)),
    )

    fun searchProfiles(query: String): List<AutoEqProfile> {
        if (query.isBlank()) return popularProfiles
        return popularProfiles.filter {
            it.name.contains(query, ignoreCase = true) || it.brand.contains(query, ignoreCase = true)
        }
    }

    suspend fun applyProfile(profile: AutoEqProfile) {
        equalizerPreferences.applyCustomSettings(profile.name, profile.gainsDb)
        equalizerPreferences.setEnabled(true)
    }
}
