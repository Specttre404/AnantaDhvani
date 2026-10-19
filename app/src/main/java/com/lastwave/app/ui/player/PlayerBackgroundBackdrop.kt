package com.lastwave.app.ui.player

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
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
import kotlin.math.sin

@Composable
fun LastWaveXPlayerRoot(
    backgroundStyle: PlayerBackgroundStyle,
    dominantColor: Color = MaterialTheme.colorScheme.primary,
    accentColor: Color = MaterialTheme.colorScheme.tertiary,
    track: PlayableTrack? = null,
    isPlaying: Boolean = false,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit,
) {
    Box(modifier = modifier.fillMaxSize()) {
        PlayerBackgroundBackdrop(
            backgroundStyle = backgroundStyle,
            track = track,
            isPlaying = isPlaying,
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
    isPlaying: Boolean = false,
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

    // Sub-bass reactive pulse scale (1.00f to 1.04f, 60ms spring)
    val pulseScale by animateFloatAsState(
        targetValue = if (isPlaying) 1.035f else 1.000f,
        animationSpec = spring(stiffness = 800f, dampingRatio = 0.5f),
        label = "pulseScale",
    )

    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        val w = constraints.maxWidth.toFloat()
        val h = constraints.maxHeight.toFloat()
        val maxDim = maxOf(w, h, 1f)

        when (backgroundStyle) {
            PlayerBackgroundStyle.AMOLED_BLACK -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black),
                ) {
                    if (isPlaying && track != null) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.radialGradient(
                                        colors = listOf(
                                            dominantColor.copy(alpha = 0.22f),
                                            accentColor.copy(alpha = 0.10f),
                                            Color.Transparent,
                                        ),
                                        center = Offset(w * 0.5f, h * 0.40f),
                                        radius = maxDim * 0.55f,
                                    ),
                                ),
                        )
                    }
                }
            }

            PlayerBackgroundStyle.BLURRED_GLASS -> {
                Box(modifier = Modifier.fillMaxSize().background(Color.Black)) {
                    if (track != null) {
                        ArtworkImage(
                            name = track.title,
                            artist = track.artist,
                            embeddedUrl = track.artworkUrl,
                            fallbackIcon = Icons.Filled.MusicNote,
                            modifier = Modifier
                                .fillMaxSize()
                                .blur(60.dp)
                                .graphicsLayer {
                                    scaleX = 1.35f
                                    scaleY = 1.35f
                                },
                            decodeSizePx = null,
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.Black.copy(alpha = 0.50f)),
                        )
                    } else {
                        Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surfaceContainerHigh))
                    }
                }
            }

            PlayerBackgroundStyle.HDR_VIVID -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer {
                            scaleX = pulseScale
                            scaleY = pulseScale
                        }
                        .background(
                            Brush.radialGradient(
                                colors = listOf(
                                    dominantColor.copy(alpha = 0.95f),
                                    accentColor.copy(alpha = 0.50f),
                                    Color(0xFF07080B),
                                ),
                                center = Offset(w * 0.5f, h * 0.35f),
                                radius = maxDim * 0.85f,
                            ),
                        ),
                )
            }

            PlayerBackgroundStyle.FLUID_GRADIENT -> {
                val offsetX1 = sin(phase * 0.003f) * (w * 0.25f)
                val offsetY1 = sin(phase * 0.002f) * (h * 0.20f)
                val offsetX2 = sin(phase * 0.004f + 1.5f) * (w * 0.20f)
                val offsetY2 = sin(phase * 0.003f + 1.0f) * (h * 0.25f)

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFF060709)),
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .graphicsLayer {
                                translationX = offsetX1
                                translationY = offsetY1
                                scaleX = 1.4f
                                scaleY = 1.4f
                            }
                            .blur(90.dp)
                            .background(
                                Brush.radialGradient(
                                    colors = listOf(
                                        dominantColor.copy(alpha = 0.75f),
                                        Color.Transparent,
                                    ),
                                    center = Offset(w * 0.30f, h * 0.30f),
                                    radius = maxDim * 0.60f,
                                ),
                            ),
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .graphicsLayer {
                                translationX = offsetX2
                                translationY = offsetY2
                                scaleX = 1.4f
                                scaleY = 1.4f
                            }
                            .blur(90.dp)
                            .background(
                                Brush.radialGradient(
                                    colors = listOf(
                                        accentColor.copy(alpha = 0.60f),
                                        Color.Transparent,
                                    ),
                                    center = Offset(w * 0.70f, h * 0.65f),
                                    radius = maxDim * 0.55f,
                                ),
                            ),
                    )
                }
            }

            PlayerBackgroundStyle.AMBIENT_GLOW -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer {
                            scaleX = pulseScale
                            scaleY = pulseScale
                        }
                        .background(Color(0xFF0A0A0C)),
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .blur(100.dp)
                            .background(
                                Brush.radialGradient(
                                    colors = listOf(
                                        dominantColor.copy(alpha = 0.50f),
                                        accentColor.copy(alpha = 0.30f),
                                        Color.Transparent,
                                    ),
                                    center = Offset(w * 0.5f, h * 0.35f),
                                    radius = maxDim * 0.75f,
                                ),
                            ),
                    )
                }
            }

            PlayerBackgroundStyle.DYNAMIC_HARMONY -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    dominantColor.copy(alpha = 0.85f),
                                    accentColor.copy(alpha = 0.40f),
                                    MaterialTheme.colorScheme.background,
                                ),
                            ),
                        ),
                )
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

            PlayerBackgroundStyle.PRISM_SPECTRUM -> {
                val primary = dominantColor.copy(alpha = 0.65f)
                val tertiary = accentColor.copy(alpha = 0.55f)
                val secondary = MaterialTheme.colorScheme.secondary.copy(alpha = 0.45f)

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.sweepGradient(
                                colors = listOf(primary, tertiary, secondary, primary),
                                center = Offset(w * 0.5f, h * 0.4f),
                            ),
                        ),
                )
            }
        }

        content()
    }
}
