package com.lastwave.app.data.eq

import android.content.Context
import com.lastwave.app.data.local.EqualizerPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import javax.inject.Inject
import javax.inject.Singleton

data class AutoEqProfile(
    val name: String,
    val brand: String,
    val gainsDb: List<Float>,
)

@Singleton
class AutoEqRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val equalizerPreferences: EqualizerPreferences,
) {
    private var loadedProfiles: List<AutoEqProfile>? = null

    private fun loadProfilesFromAsset(): List<AutoEqProfile> {
        loadedProfiles?.let { return it }
        return runCatching {
            val jsonStr = context.assets.open("autoeq_index.json").bufferedReader().use { it.readText() }
            val array = JSONArray(jsonStr)
            val list = mutableListOf<AutoEqProfile>()
            for (i in 0 until array.length()) {
                val obj = array.getJSONObject(i)
                val name = obj.getString("name")
                val brand = obj.getString("brand")
                val gainsArray = obj.getJSONArray("gains")
                val gains = mutableListOf<Float>()
                for (j in 0 until gainsArray.length()) {
                    gains.add(gainsArray.getDouble(j).toFloat())
                }
                list.add(AutoEqProfile(name, brand, gains))
            }
            loadedProfiles = list
            list
        }.getOrDefault(fallbackProfiles)
    }

    fun searchProfiles(query: String): List<AutoEqProfile> {
        val profiles = loadProfilesFromAsset()
        if (query.isBlank()) return profiles
        return profiles.filter {
            it.name.contains(query, ignoreCase = true) || it.brand.contains(query, ignoreCase = true)
        }
    }

    suspend fun applyProfile(profile: AutoEqProfile) {
        equalizerPreferences.applyCustomSettings(profile.name, profile.gainsDb)
        equalizerPreferences.setEnabled(true)
    }

    private val fallbackProfiles = listOf(
        AutoEqProfile("Sennheiser HD 600", "Sennheiser", listOf(0f, 0.5f, 1f, 1.5f, 1f, 0.5f, 0f, -0.5f, -1f, -1.5f, -1f, -0.5f, 0f, 0.5f, 1f, 1.5f, 1f, 0.5f, 0f, -0.5f, -1f, -1.5f, -1f, -0.5f, 0f, 0.5f, 1f, 0.5f, 0f, -0.5f, 0f)),
        AutoEqProfile("Sony WH-1000XM5", "Sony", listOf(1.5f, 2f, 2.5f, 2f, 1.5f, 1f, 0.5f, 0f, -0.5f, -1f, -1.5f, -2f, -1.5f, -1f, -0.5f, 0f, 0.5f, 1f, 1.5f, 2f, 1.5f, 1f, 0.5f, 0f, -0.5f, -1f, -0.5f, 0f, 0.5f, 1f, 0f)),
        AutoEqProfile("Moondrop Blessing 2", "Moondrop", listOf(-0.5f, 0f, 0.5f, 1f, 1.5f, 1f, 0.5f, 0f, -0.5f, -1f, -0.5f, 0f, 0.5f, 1f, 1.5f, 1f, 0.5f, 0f, -0.5f, -1f, -0.5f, 0f, 0.5f, 1f, 0.5f, 0f, -0.5f, 0f, 0.5f, 0f, 0f)),
        AutoEqProfile("Apple AirPods Pro 2", "Apple", listOf(1f, 1.5f, 1f, 0.5f, 0f, -0.5f, -1f, -0.5f, 0f, 0.5f, 1f, 0.5f, 0f, -0.5f, -1f, -0.5f, 0f, 0.5f, 1f, 0.5f, 0f, -0.5f, -1f, -0.5f, 0f, 0.5f, 1f, 0.5f, 0f, 0f, 0f)),
        AutoEqProfile("Audio-Technica ATH-M50x", "Audio-Technica", listOf(-1f, -1.5f, -1f, -0.5f, 0f, 0.5f, 1f, 1.5f, 1f, 0.5f, 0f, -0.5f, -1f, -0.5f, 0f, 0.5f, 1f, 0.5f, 0f, -0.5f, -1f, -0.5f, 0f, 0.5f, 1f, 0.5f, 0f, 0f, 0f, 0f, 0f)),
    )
}
