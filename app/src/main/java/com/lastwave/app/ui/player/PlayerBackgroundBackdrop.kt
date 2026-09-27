package com.lastwave.app.ui.player

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.lastwave.app.data.local.PlayerBackgroundStyle
import com.lastwave.app.playback.PlayableTrack
import com.lastwave.app.ui.common.ArtworkImage
import com.lastwave.app.ui.theme.BackdropBlur
import kotlin.math.sin

@Composable
fun LastWaveXPlayerRoot(
    backgroundStyle: PlayerBackgroundStyle,
    dominantColor: Color = MaterialTheme.colorScheme.primary,
    accentColor: Color = MaterialTheme.colorScheme.tertiary,
    track: PlayableTrack? = null,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit,
) {
    Box(modifier = modifier.fillMaxSize()) {
        PlayerBackgroundBackdrop(
            backgroundStyle = backgroundStyle,
            track = track,
            dominantColor = dominantColor,
            accentColor = accentColor,
        )
        content()
    }
}

@Composable
fun PlayerBackgroundBackdrop(
    backgroundStyle: PlayerBackgroundStyle,
    track: PlayableTrack? = null,
    dominantColor: Color = MaterialTheme.colorScheme.primary,
    accentColor: Color = MaterialTheme.colorScheme.tertiary,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit = {},
) {
    val infiniteTransition = rememberInfiniteTransition(label = "BackgroundAnimation")

    val phase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(20000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "phase",
    )

    Box(modifier = modifier.fillMaxSize()) {
        when (backgroundStyle) {
            PlayerBackgroundStyle.HDR_VIVID -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.radialGradient(
                                colors = listOf(
                                    dominantColor.copy(alpha = 0.9f),
                                    accentColor.copy(alpha = 0.5f),
                                    Color.Black,
                                ),
                                center = Offset(500f, 300f),
                                radius = 1200f,
                            ),
                        ),
                )
            }

            PlayerBackgroundStyle.FLUID_GRADIENT -> {
                val offsetX = (sin(phase * 0.003f) * 300f)
                val offsetY = (sin(phase * 0.002f) * 300f)

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black),
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .graphicsLayer {
                                translationX = offsetX
                                translationY = offsetY
                                scaleX = 1.5f
                                scaleY = 1.5f
                            }
                            .blur(80.dp)
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(
                                        dominantColor.copy(alpha = 0.7f),
                                        accentColor.copy(alpha = 0.5f),
                                        Color.Transparent,
                                    ),
                                    start = Offset(0f, 0f),
                                    end = Offset(1000f, 1000f),
                                ),
                            ),
                    )
                }
            }

            PlayerBackgroundStyle.DYNAMIC_HARMONY -> {
                if (track != null) {
                    BackdropBlur(radius = 24.dp, modifier = Modifier.fillMaxSize()) {
                        ArtworkImage(
                            name = track.title,
                            artist = track.artist,
                            embeddedUrl = track.artworkUrl,
                            fallbackIcon = Icons.Filled.MusicNote,
                            modifier = Modifier
                                .fillMaxSize()
                                .graphicsLayer {
                                    scaleX = 1.3f
                                    scaleY = 1.3f
                                    alpha = 0.70f
                                },
                            decodeSizePx = 200,
                        )
                    }
                } else {
                    Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surfaceContainerHigh))
                }
            }

            PlayerBackgroundStyle.AMBIENT_GLOW -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFF0A0A0C)),
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .blur(100.dp)
                            .background(
                                Brush.radialGradient(
                                    colors = listOf(
                                        dominantColor.copy(alpha = 0.4f),
                                        accentColor.copy(alpha = 0.2f),
                                        Color.Transparent,
                                    ),
                                    center = Offset(500f, 400f),
                                    radius = 900f,
                                ),
                            ),
                    )
                }
            }

            PlayerBackgroundStyle.DYNAMIC_MONET -> {
                val primaryColor = MaterialTheme.colorScheme.primaryContainer
                val secondaryColor = MaterialTheme.colorScheme.surfaceContainerHighest
                val animatedPrimary by animateColorAsState(primaryColor, tween(1000, easing = FastOutSlowInEasing), label = "monetPrimary")
                val animatedSecondary by animateColorAsState(secondaryColor, tween(1000, easing = FastOutSlowInEasing), label = "monetSecondary")

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(animatedPrimary, animatedSecondary, MaterialTheme.colorScheme.surface),
                            ),
                        ),
                )
            }

            PlayerBackgroundStyle.AMOLED_BLACK -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black),
                )
            }

            PlayerBackgroundStyle.BLURRED_GLASS -> {
                if (track != null) {
                    BackdropBlur(radius = 36.dp, modifier = Modifier.fillMaxSize()) {
                        ArtworkImage(
                            name = track.title,
                            artist = track.artist,
                            embeddedUrl = track.artworkUrl,
                            fallbackIcon = Icons.Filled.MusicNote,
                            modifier = Modifier
                                .fillMaxSize()
                                .graphicsLayer {
                                    scaleX = 1.4f
                                    scaleY = 1.4f
                                    alpha = 0.65f
                                },
                            decodeSizePx = 200,
                        )
                    }
                } else {
                    Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surfaceContainerHigh))
                }
            }

            PlayerBackgroundStyle.PRISM_SPECTRUM -> {
                val primary = dominantColor.copy(alpha = 0.6f)
                val tertiary = accentColor.copy(alpha = 0.5f)
                val secondary = MaterialTheme.colorScheme.secondary.copy(alpha = 0.4f)

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.sweepGradient(
                                colors = listOf(primary, tertiary, secondary, primary),
                            ),
                        ),
                )
            }
        }

        content()
    }
}
