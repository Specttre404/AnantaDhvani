package com.lastwave.app.ui.settings

import androidx.compose.runtime.Composable

@Composable
fun AiAssistantSettingsScreen(
    viewModel: SettingsViewModel,
    onBackClick: () -> Unit,
) {
    AiSettingsScreen(onBack = onBackClick)
}
