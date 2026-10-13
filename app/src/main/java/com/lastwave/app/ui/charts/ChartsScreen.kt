package com.lastwave.app.ui.charts

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier

@Composable
fun ChartsScreen(
    onBack: () -> Unit = {},
    onOpenArtist: (String, String?) -> Unit = { _, _ -> },
) {
    LaunchedEffect(Unit) {
        onBack()
    }
    Box(modifier = Modifier.fillMaxSize())
}
