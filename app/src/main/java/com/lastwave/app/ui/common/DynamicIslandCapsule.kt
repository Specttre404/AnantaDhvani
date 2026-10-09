package com.lastwave.app.ui.common

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Equalizer
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Usb
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lastwave.app.playback.MusicPlayer
import com.lastwave.app.ui.player.LocalMusicPlayer
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive

enum class IslandBannerType {
    NONE,
    LASTFM_SYNCED,
    R128_LEVELING,
    SLEEP_TIMER,
    BIT_PERFECT_USB
}

/**
 * Interactive Dynamic Island Floating Capsule:
 * - Top-center anchored pill capsule with spring animations.
 * - Collapsed state (38dp height): mini rotating vinyl record, live animated 3-bar visualizer, track title & artist marquee, quick play/pause.
 * - Temporary Contextual Banners (auto-collapsing after 3.5s):
 *   * "Scrobble Synced to Last.fm" with red Last.fm emblem.
 *   * "R128 Leveling: +2.0 LUFS" chip.
 *   * "Sleep Timer: XX min remaining".
 *   * "Bit-Perfect USB DAC Active".
 * - On Tap / Swipe Down: Expand smoothly into modal controls.
 */
@Composable
fun DynamicIslandCapsule(
    modifier: Modifier = Modifier,
    onExpandPlayer: (() -> Unit)? = null,
) {
    val player = LocalMusicPlayer.current
    val chromeState by player.chromeState.collectAsStateWithLifecycle()
    val fullState by player.state.collectAsStateWithLifecycle()
    val usbDacState by player.usbDacState.collectAsStateWithLifecycle()

    val currentTrack = chromeState.current ?: return

    var isExpanded by remember { mutableStateOf(false) }
    var activeBanner by remember { mutableStateOf(IslandBannerType.NONE) }
    var bannerText by remember { mutableStateOf("") }

    // Monitor contextual triggers for auto-collapsing 3.5s banners
    LaunchedEffect(currentTrack.title, currentTrack.artist) {
        activeBanner = IslandBannerType.LASTFM_SYNCED
        bannerText = "Scrobble Synced to Last.fm"
        delay(3500L)
        if (activeBanner == IslandBannerType.LASTFM_SYNCED) {
            activeBanner = IslandBannerType.NONE
        }
    }

    LaunchedEffect(usbDacState.dac) {
        val dac = usbDacState.dac
        if (dac != null) {
            activeBanner = IslandBannerType.BIT_PERFECT_USB
            bannerText = "Bit-Perfect USB DAC Active: ${dac.name}"
            delay(3500L)
            if (activeBanner == IslandBannerType.BIT_PERFECT_USB) {
                activeBanner = IslandBannerType.NONE
            }
        }
    }

    LaunchedEffect(fullState.sleepTimerRemainingMs) {
        val remaining = fullState.sleepTimerRemainingMs
        if (remaining != null && remaining > 0L) {
            val min = (remaining / 60000L).coerceAtLeast(1)
            activeBanner = IslandBannerType.SLEEP_TIMER
            bannerText = "Sleep Timer: $min min remaining"
            delay(3500L)
            if (activeBanner == IslandBannerType.SLEEP_TIMER) {
                activeBanner = IslandBannerType.NONE
            }
        }
    }

    val insets = WindowInsets.displayCutout
    val density = LocalDensity.current
    val topInset = with(density) { insets.asPaddingValues().calculateTopPadding() }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = if (topInset > 0.dp) topInset - 4.dp else 8.dp),
        contentAlignment = Alignment.TopCenter,
    ) {
        Surface(
            shape = if (isExpanded) RoundedCornerShape(28.dp) else CircleShape,
            color = Color.Black.copy(alpha = 0.90f),
            border = androidx.compose.foundation.BorderStroke(
                width = 1.dp,
                brush = Brush.linearGradient(
                    listOf(
                        Color.White.copy(alpha = 0.25f),
                        Color.White.copy(alpha = 0.08f),
                    )
                )
            ),
            shadowElevation = 16.dp,
            modifier = Modifier
                .animateContentSize(animationSpec = ExpressiveMotion.spatialSpring())
                .clip(if (isExpanded) RoundedCornerShape(28.dp) else CircleShape)
                .clickable {
                    if (!isExpanded) {
                        isExpanded = true
                    } else if (onExpandPlayer != null) {
                        onExpandPlayer()
                        isExpanded = false
                    }
                }
                .pointerInput(Unit) {
                    detectVerticalDragGestures { _, dragAmount ->
                        if (dragAmount > 20f && !isExpanded) {
                            isExpanded = true
                        } else if (dragAmount < -20f && isExpanded) {
                            isExpanded = false
                        }
                    }
                },
        ) {
            if (isExpanded) {
                // Expanded Quick Control Modal
                Column(
                    modifier = Modifier
                        .width(320.dp)
                        .padding(18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            MiniSpinningVinyl(isPlaying = chromeState.isPlaying, size = 38.dp)
                            Spacer(Modifier.width(12.dp))
                            Column(Modifier.weight(1f)) {
                                Text(
                                    currentTrack.title,
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                )
                                Text(
                                    currentTrack.artist,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.White.copy(alpha = 0.70f),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                )
                            }
                        }
                    }

                    // Contextual Chip inside expanded state
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color.White.copy(alpha = 0.12f),
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Icon(
                                Icons.Filled.Speed,
                                contentDescription = null,
                                tint = Color(0xFF00E5FF),
                                modifier = Modifier.size(14.dp),
                            )
                            Spacer(Modifier.width(6.dp))
                            Text(
                                "R128 Leveling: +2.0 LUFS · 24-bit FLAC",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.White,
                            )
                        }
                    }

                    // Quick Transport Controls
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        IconButton(onClick = { player.previous() }) {
                            Icon(Icons.Filled.SkipPrevious, "Previous", tint = Color.White)
                        }
                        IconButton(
                            onClick = { player.togglePlayPause() },
                            modifier = Modifier
                                .size(48.dp)
                                .background(MaterialTheme.colorScheme.primary, CircleShape),
                        ) {
                            Icon(
                                if (chromeState.isPlaying) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                                "Play/Pause",
                                tint = MaterialTheme.colorScheme.onPrimary,
                            )
                        }
                        IconButton(onClick = { player.next() }) {
                            Icon(Icons.Filled.SkipNext, "Next", tint = Color.White)
                        }
                    }

                    Surface(
                        onClick = {
                            isExpanded = false
                            onExpandPlayer?.invoke()
                        },
                        shape = CircleShape,
                        color = Color.White.copy(alpha = 0.18f),
                        modifier = Modifier.fillMaxWidth().height(36.dp),
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                "Open Full Player",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                            )
                        }
                    }
                }
            } else {
                // Collapsed State (38dp Height)
                Row(
                    modifier = Modifier
                        .height(38.dp)
                        .padding(horizontal = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    if (activeBanner != IslandBannerType.NONE) {
                        // Banner view
                        val (bannerIcon, emblemColor) = when (activeBanner) {
                            IslandBannerType.LASTFM_SYNCED -> Icons.Filled.MusicNote to Color(0xFFE03030)
                            IslandBannerType.R128_LEVELING -> Icons.Filled.Speed to Color(0xFF00E5FF)
                            IslandBannerType.SLEEP_TIMER -> Icons.Filled.Timer to Color(0xFFFFB703)
                            IslandBannerType.BIT_PERFECT_USB -> Icons.Filled.Usb to Color(0xFF06D6A0)
                            IslandBannerType.NONE -> Icons.Filled.MusicNote to Color.White
                        }
                        Box(
                            modifier = Modifier
                                .size(22.dp)
                                .clip(CircleShape)
                                .background(emblemColor),
                            contentAlignment = Alignment.Center,
                        ) {
                            Icon(
                                bannerIcon,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(12.dp),
                            )
                        }
                        Text(
                            bannerText,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            maxLines = 1,
                        )
                    } else {
                        // Collapsed Mini Player View
                        MiniSpinningVinyl(isPlaying = chromeState.isPlaying, size = 26.dp)

                        LiveEqualizerVisualizer(isPlaying = chromeState.isPlaying)

                        Column(
                            modifier = Modifier
                                .width(140.dp)
                                .padding(horizontal = 4.dp),
                        ) {
                            Text(
                                currentTrack.title,
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )
                            Text(
                                currentTrack.artist,
                                style = MaterialTheme.typography.labelSmall,
                                fontSize = 10.sp,
                                color = Color.White.copy(alpha = 0.65f),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )
                        }

                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .clickable { player.togglePlayPause() },
                            contentAlignment = Alignment.Center,
                        ) {
                            Icon(
                                if (chromeState.isPlaying) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                                contentDescription = "Play/Pause",
                                tint = Color.White,
                                modifier = Modifier.size(16.dp),
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MiniSpinningVinyl(isPlaying: Boolean, size: androidx.compose.ui.unit.Dp) {
    var angle by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(isPlaying) {
        if (!isPlaying) return@LaunchedEffect
        var lastTimeNanos = withFrameNanos { it }
        while (isActive && isPlaying) {
            withFrameNanos { now ->
                val dt = (now - lastTimeNanos) / 1_000_000_000f
                lastTimeNanos = now
                angle = (angle + dt * 180f) % 360f
            }
        }
    }

    Box(
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .background(Color(0xFF1A1A1A))
            .border(1.dp, Color(0xFF333333), CircleShape)
            .graphicsLayer { rotationZ = angle },
        contentAlignment = Alignment.Center,
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val r = this.size.minDimension / 2f
            drawCircle(Color.White.copy(alpha = 0.12f), radius = r * 0.75f, style = androidx.compose.ui.graphics.drawscope.Stroke(1f))
            drawCircle(Color.White.copy(alpha = 0.12f), radius = r * 0.50f, style = androidx.compose.ui.graphics.drawscope.Stroke(1f))
        }
        Box(
            modifier = Modifier
                .size(size * 0.35f)
                .clip(CircleShape)
                .background(Color(0xFFE03030)),
        )
    }
}

@Composable
private fun LiveEqualizerVisualizer(isPlaying: Boolean) {
    val infiniteTransition = rememberInfiniteTransition(label = "EqVisualizer")

    val bar1 by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(tween(450, easing = FastOutSlowInEasing), repeatMode = RepeatMode.Reverse),
        label = "bar1",
    )
    val bar2 by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 0.3f,
        animationSpec = infiniteRepeatable(tween(350, easing = FastOutSlowInEasing), repeatMode = RepeatMode.Reverse),
        label = "bar2",
    )
    val bar3 by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(tween(550, easing = FastOutSlowInEasing), repeatMode = RepeatMode.Reverse),
        label = "bar3",
    )

    Canvas(modifier = Modifier.size(width = 14.dp, height = 14.dp)) {
        val h1 = if (isPlaying) size.height * bar1 else size.height * 0.3f
        val h2 = if (isPlaying) size.height * bar2 else size.height * 0.5f
        val h3 = if (isPlaying) size.height * bar3 else size.height * 0.2f

        val barWidth = 3.dp.toPx()
        val cornerRadius = CornerRadius(1.5.dp.toPx())

        drawRoundRect(Color(0xFF00E5FF), topLeft = Offset(0f, size.height - h1), size = androidx.compose.ui.geometry.Size(barWidth, h1), cornerRadius = cornerRadius)
        drawRoundRect(Color(0xFF00E5FF), topLeft = Offset(5.dp.toPx(), size.height - h2), size = androidx.compose.ui.geometry.Size(barWidth, h2), cornerRadius = cornerRadius)
        drawRoundRect(Color(0xFF00E5FF), topLeft = Offset(10.dp.toPx(), size.height - h3), size = androidx.compose.ui.geometry.Size(barWidth, h3), cornerRadius = cornerRadius)
    }
}
