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

    suspend fun syncRemoteAutoEqIndex(): Int = withContext(Dispatchers.IO) {
        runCatching {
            val okHttpClient = okhttp3.OkHttpClient()
            val request = okhttp3.Request.Builder()
                .url("https://raw.githubusercontent.com/jaakkopasanen/AutoEq/master/results/INDEX.md")
                .build()
            okHttpClient.newCall(request).execute().use { response ->
                if (!response.isSuccessful) return@withContext 0
                val body = response.body?.string().orEmpty()
                val lines = body.lines()
                val remoteList = mutableListOf<AutoEqProfile>()
                for (line in lines) {
                    if (line.contains("|") && !line.startsWith("|---") && !line.startsWith("| Headphone")) {
                        val parts = line.split("|").map { it.trim() }
                        if (parts.size >= 2) {
                            val fullName = parts[1]
                            if (fullName.isNotBlank()) {
                                val brand = fullName.substringBefore(" ").ifBlank { "AutoEQ" }
                                remoteList.add(
                                    AutoEqProfile(
                                        name = fullName,
                                        brand = brand,
                                        gainsDb = fallbackProfiles.first().gainsDb,
                                    )
                                )
                            }
                        }
                    }
                }
                if (remoteList.isNotEmpty()) {
                    loadedProfiles = (loadProfilesFromAsset() + remoteList).distinctBy { it.name.lowercase() }
                    return@withContext remoteList.size
                }
                0
            }
        }.getOrDefault(0)
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
