package com.lastwave.app.ui.settings

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.lastwave.app.data.local.SettingsPreferences
import com.lastwave.app.data.plugin.InstalledProviderModule
import com.lastwave.app.data.plugin.ModuleManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ModulesViewModel @Inject constructor(
    private val moduleManager: ModuleManager,
    private val settingsPreferences: SettingsPreferences,
) : ViewModel() {

    val modules: StateFlow<List<InstalledProviderModule>> = MutableStateFlow(emptyList<InstalledProviderModule>()).asStateFlow()
    val busy: StateFlow<Boolean> = MutableStateFlow(false).asStateFlow()
    val notice: StateFlow<String?> = MutableStateFlow<String?>(null).asStateFlow()
    val preferModules: StateFlow<Boolean> = MutableStateFlow(false).asStateFlow()

    fun setPreferModules(enabled: Boolean) {}
    fun install(uri: Uri) {}
    fun setEnabled(id: String, enabled: Boolean) {}
    fun remove(id: String) {}
    fun clearNotice() {}
}
