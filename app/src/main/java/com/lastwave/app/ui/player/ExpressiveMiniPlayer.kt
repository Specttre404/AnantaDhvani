package com.lastwave.app.ui.player

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.SkipNext
import androidx.compose.material.icons.rounded.SkipPrevious
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lastwave.app.playback.PlaybackChromeState
import com.lastwave.app.ui.common.ArtworkImage
import com.lastwave.app.ui.theme.LocalLiquidGlass
import com.lastwave.app.ui.theme.liquidGlassChrome
import com.lastwave.app.ui.theme.liquidGlassContainerColor
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

private val MiniPlayerPillShape = RoundedCornerShape(28.dp)

@Composable
fun ExpressiveMiniPlayer(
    playbackState: PlaybackChromeState,
    onExpand: () -> Unit,
    onPlayPause: () -> Unit,
    onNext: () -> Unit,
    onPrevious: () -> Unit,
    modifier: Modifier = Modifier,
    enableVinylRotation: Boolean = true,
) {
    val track = playbackState.current ?: return
    val liquidGlass = LocalLiquidGlass.current
    val haptic = LocalHapticFeedback.current
    val scope = rememberCoroutineScope()
    val isDark = isSystemInDarkTheme()

    // Dynamic HDR Accent Palette & Luminance Detection
    val accentTone = MaterialTheme.colorScheme.primary
    val isAccentLight = accentTone.luminance() > 0.5f
    val onAccentColor = if (isAccentLight) Color(0xFF0B0F1A) else Color.White

    // Gesture Drag Physics
    var offsetX by remember { mutableFloatStateOf(0f) }
    val animatedOffsetX = remember { Animatable(0f) }

    // Vinyl Rotation Physics
    val infiniteTransition = rememberInfiniteTransition(label = "miniplayer_physics")
    val continuousAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(12000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "vinyl_rotation",
    )
    val rotationAngle = if (enableVinylRotation && playbackState.isPlaying) continuousAngle else 0f

    // Ambient Halo Animation
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.94f,
        targetValue = 1.06f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "ambient_pulse",
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 6.dp)
            .offset { IntOffset(offsetX.roundToInt(), 0) }
            .draggable(
                orientation = Orientation.Horizontal,
                state = rememberDraggableState { delta ->
                    offsetX += delta * 0.75f
                },
                onDragStopped = { velocity ->
                    scope.launch {
                        if (offsetX > 240f || velocity > 1000f) {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            onNext()
                        } else if (offsetX < -240f || velocity < -1000f) {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            onPrevious()
                        }
                        animatedOffsetX.snapTo(offsetX)
                        animatedOffsetX.animateTo(
                            targetValue = 0f,
                            animationSpec = spring(stiffness = 500f, dampingRatio = 0.65f),
                        )
                        offsetX = 0f
                    }
                },
            ),
    ) {
        // Ambient Bloom Under-Halo (HDR Glow)
        Canvas(
            modifier = Modifier
                .matchParentSize()
                .graphicsLayer {
                    scaleX = pulseScale
                    scaleY = pulseScale
                    alpha = if (isDark) 0.35f else 0.22f
                },
        ) {
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(accentTone.copy(alpha = 0.55f), Color.Transparent),
                    center = Offset(size.width * 0.25f, size.height * 0.5f),
                    radius = size.width * 0.45f,
                ),
            )
        }

        // Frosted Specular Liquid Glass Surface
        Surface(
            shape = MiniPlayerPillShape,
            color = liquidGlassContainerColor(
                if (isDark) Color(0xFF111726).copy(alpha = 0.88f) else Color(0xFFF8FAFC).copy(alpha = 0.90f),
            ),
            tonalElevation = 6.dp,
            shadowElevation = 8.dp,
            border = BorderStroke(
                width = 1.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        accentTone.copy(alpha = 0.65f),
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
                        Color.Transparent,
                    ),
                ),
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(68.dp)
                .liquidGlassChrome(MiniPlayerPillShape, liquidGlass)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onExpand,
                ),
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 10.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    // Artwork / Vinyl Record Disc container
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .shadow(6.dp, CircleShape)
                            .clip(if (enableVinylRotation) CircleShape else RoundedCornerShape(14.dp)),
                    ) {
                        ArtworkImage(
                            name = track.title,
                            artist = track.artist,
                            embeddedUrl = track.artworkUrl,
                            fallbackIcon = Icons.Filled.MusicNote,
                            modifier = Modifier
                                .fillMaxSize()
                                .rotate(rotationAngle),
                        )
                    }

                    Spacer(Modifier.width(12.dp))

                    // Title & Artist
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.Center,
                    ) {
                        Text(
                            text = track.title,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                letterSpacing = (-0.3).sp,
                            ),
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                        Spacer(Modifier.height(2.dp))
                        Text(
                            text = track.artist,
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight.Medium,
                            ),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }

                    // Transport Actions
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        IconButton(
                            onClick = {
                                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                onPrevious()
                            },
                            modifier = Modifier.size(36.dp),
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.SkipPrevious,
                                contentDescription = "Previous",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(22.dp),
                            )
                        }

                        // Luminance-Adaptive Play/Pause FAB
                        Surface(
                            onClick = {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                onPlayPause()
                            },
                            shape = CircleShape,
                            color = accentTone,
                            shadowElevation = 4.dp,
                            modifier = Modifier.size(42.dp),
                        ) {
                            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                                Icon(
                                    imageVector = if (playbackState.isPlaying) Icons.Rounded.Pause else Icons.Rounded.PlayArrow,
                                    contentDescription = if (playbackState.isPlaying) "Pause" else "Play",
                                    tint = onAccentColor,
                                    modifier = Modifier.size(24.dp),
                                )
                            }
                        }

                        IconButton(
                            onClick = {
                                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                onNext()
                            },
                            modifier = Modifier.size(36.dp),
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.SkipNext,
                                contentDescription = "Next",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(22.dp),
                            )
                        }
                    }
                }
            }
        }
    }
}
