package com.lastwave.app.ui.theme

import android.app.Activity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.lastwave.app.data.repository.ThemeUiState
import com.kyant.backdrop.backdrops.rememberCanvasBackdrop

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.staticCompositionLocalOf
import com.lastwave.app.data.local.ThemeMode

/** Shared composition local indicating whether the active theme is dark mode. */
val LocalIsDarkTheme = staticCompositionLocalOf { true }

/**
 * Wraps the whole app. Supports System Default, Light, and Dark modes.
 * Dynamically updates system bar icons, background surface, and Liquid Glass layers.
 */
@Composable
fun LastWaveTheme(
    themeState: ThemeUiState,
    content: @Composable () -> Unit,
) {
    val isSystemDark = isSystemInDarkTheme()
    val isDark = when (themeState.themeMode) {
        ThemeMode.SYSTEM -> isSystemDark
        ThemeMode.DARK -> true
        ThemeMode.LIGHT -> false
    }
    val activeColorScheme = if (isDark) themeState.darkColorScheme else themeState.lightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window ?: return@SideEffect
            runCatching {
                WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !isDark
                WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = !isDark
            }
        }
    }

    MaterialTheme(
        colorScheme = activeColorScheme,
        typography = if (themeState.useCustomFont) LastWaveTypography else SystemTypography,
        shapes = LastWaveShapes,
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background,
        ) {
            CompositionLocalProvider(
                LocalLiquidGlass provides themeState.liquidGlass,
                LocalIsDarkTheme provides isDark,
            ) {
                if (isLiquidGlassBackdropSupported()) {
                    val backgroundColor = MaterialTheme.colorScheme.background
                    val backgroundBackdrop = rememberCanvasBackdrop { drawRect(backgroundColor) }
                    CompositionLocalProvider(
                        LocalLiquidGlassBackdrop provides backgroundBackdrop,
                        LocalLiquidGlassOverlayBackdrop provides backgroundBackdrop,
                    ) {
                        content()
                    }
                } else {
                    content()
                }
            }
        }
    }
}

