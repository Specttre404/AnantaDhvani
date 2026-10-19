package com.lastwave.app.ui.player

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.lastwave.app.data.local.SeekbarStyle
import kotlin.math.PI
import kotlin.math.sin

@Composable
fun WavySeekBar(
    positionMs: Long,
    durationMs: Long,
    isPlaying: Boolean,
    onSeek: (Long) -> Unit,
    modifier: Modifier = Modifier,
    isTranslucent: Boolean = false,
    trackKey: String? = null,
    showTimeLabels: Boolean = true,
    seekbarStyle: SeekbarStyle = SeekbarStyle.WAVY_FLUID,
) {
    val interactionSource = remember(trackKey) { MutableInteractionSource() }
    val dragging by interactionSource.collectIsDraggedAsState()
    var dragPositionMs by remember(trackKey) { mutableFloatStateOf(0f) }

    val boundedDurationMs = durationMs.coerceAtLeast(0L)
    val boundedPositionMs = if (boundedDurationMs > 0L) {
        positionMs.coerceIn(0L, boundedDurationMs)
    } else {
        0L
    }
    val shownMs = if (dragging) {
        dragPositionMs.toLong().coerceIn(0L, boundedDurationMs)
    } else {
        boundedPositionMs
    }
    val shownFraction = if (boundedDurationMs > 0L) {
        (shownMs.toDouble() / boundedDurationMs.toDouble()).toFloat().coerceIn(0f, 1f)
    } else {
        0f
    }

    val primaryColor = if (isTranslucent) Color(0xFF00F0FF) else Color(0xFF00F0FF)
    val secondaryColor = Color(0xFFFF2E93)
    val inactiveColor = Color.White.copy(alpha = 0.22f)
    val textColor = Color(0xFFCBD5E1)

    val infiniteTransition = rememberInfiniteTransition(label = "WaveAnimation")
    val phase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = (2 * PI).toFloat(),
        animationSpec = infiniteRepeatable(tween(2000, easing = LinearEasing), repeatMode = RepeatMode.Restart),
        label = "Phase",
    )

    val density = LocalDensity.current
    val activeWaveAmpPx = if (isPlaying && !dragging) with(density) { 7.dp.toPx() } else 0f
    val waveAmpPx by animateFloatAsState(
        targetValue = activeWaveAmpPx,
        animationSpec = spring(stiffness = 300f, dampingRatio = 0.7f),
        label = "waveAmplitude",
    )
    val wavePeriodPx = with(density) { 28.dp.toPx() }

    Column(modifier = modifier.fillMaxWidth()) {
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp),
            ) {
                Canvas(modifier = Modifier.matchParentSize()) {
                    val width = size.width
                    val height = size.height
                    val centerY = height / 2f
                    val thumbX = (shownFraction * width).coerceIn(0f, width)

                    when (seekbarStyle) {
                        SeekbarStyle.WAVY_FLUID -> {
                            val trackWidthPx = 4.dp.toPx()

                            // Inactive line
                            drawLine(
                                color = inactiveColor,
                                start = Offset(thumbX, centerY),
                                end = Offset(width, centerY),
                                strokeWidth = trackWidthPx,
                                cap = StrokeCap.Round,
                            )

                            // Active squiggly path
                            if (thumbX > 0f) {
                                val wavePath = Path()
                                wavePath.moveTo(0f, centerY)
                                var x = 0f
                                val step = 2.dp.toPx()
                                while (x <= thumbX) {
                                    val waveY = centerY + sin((x / wavePeriodPx) * 2 * PI + phase).toFloat() * waveAmpPx
                                    wavePath.lineTo(x, waveY)
                                    x += step
                                }
                                wavePath.lineTo(thumbX, centerY)

                                val activeBrush = Brush.horizontalGradient(
                                    colors = listOf(primaryColor, secondaryColor),
                                    startX = 0f,
                                    endX = thumbX,
                                )
                                drawPath(
                                    path = wavePath,
                                    brush = activeBrush,
                                    style = Stroke(width = trackWidthPx, cap = StrokeCap.Round),
                                )
                            }

                            // Glowing thumb
                            drawCircle(
                                color = primaryColor,
                                radius = 8.dp.toPx(),
                                center = Offset(thumbX, centerY),
                            )
                        }

                        SeekbarStyle.SEGMENTED_DASH -> {
                            val segmentCount = 32
                            val gap = 2.dp.toPx()
                            val totalGap = gap * (segmentCount - 1)
                            val segmentWidth = (width - totalGap) / segmentCount
                            val segHeight = 8.dp.toPx()

                            for (i in 0 until segmentCount) {
                                val segLeft = i * (segmentWidth + gap)
                                val isFilled = (segLeft + segmentWidth / 2f) <= thumbX
                                val color = if (isFilled) primaryColor else inactiveColor

                                drawRoundRect(
                                    color = color,
                                    topLeft = Offset(segLeft, centerY - segHeight / 2f),
                                    size = Size(segmentWidth, segHeight),
                                    cornerRadius = CornerRadius(2.dp.toPx(), 2.dp.toPx()),
                                )
                            }
                            drawCircle(
                                color = primaryColor,
                                radius = 8.dp.toPx(),
                                center = Offset(thumbX, centerY),
                            )
                        }

                        SeekbarStyle.MINIMAL_PILL -> {
                            val pillHeight = if (dragging) 8.dp.toPx() else 3.dp.toPx()
                            drawLine(
                                color = inactiveColor,
                                start = Offset(0f, centerY),
                                end = Offset(width, centerY),
                                strokeWidth = pillHeight,
                                cap = StrokeCap.Round,
                            )
                            if (thumbX > 0f) {
                                drawLine(
                                    color = primaryColor,
                                    start = Offset(0f, centerY),
                                    end = Offset(thumbX, centerY),
                                    strokeWidth = pillHeight,
                                    cap = StrokeCap.Round,
                                )
                            }
                            drawCircle(
                                color = primaryColor,
                                radius = if (dragging) 9.dp.toPx() else 5.dp.toPx(),
                                center = Offset(thumbX, centerY),
                            )
                        }

                        SeekbarStyle.STUDIO_CONSOLE -> {
                            val barCount = 56
                            val gap = 2.dp.toPx()
                            val totalGap = gap * (barCount - 1)
                            val barWidth = (width - totalGap) / barCount
                            val maxBarHeight = 32.dp.toPx()

                            for (i in 0 until barCount) {
                                val seed = (trackKey?.hashCode() ?: 42) + i * 31
                                val sinVal = sin(i * 0.2) * 0.4 + 0.5
                                val hashVal = (kotlin.math.abs(seed) % 100) / 100.0
                                val amp = ((sinVal * 0.6 + hashVal * 0.4).toFloat() * 0.85f + 0.15f).coerceIn(0.15f, 1.0f)

                                val barX = i * (barWidth + gap)
                                val isPlayed = (barX + barWidth / 2f) <= thumbX
                                val h = amp * maxBarHeight
                                val barColor = if (isPlayed) primaryColor else inactiveColor

                                drawRoundRect(
                                    color = barColor,
                                    topLeft = Offset(barX, centerY - h / 2f),
                                    size = Size(barWidth, h),
                                    cornerRadius = CornerRadius(1.5.dp.toPx(), 1.5.dp.toPx()),
                                )
                            }

                            // Interactive playhead needle
                            drawLine(
                                color = secondaryColor,
                                start = Offset(thumbX, centerY - maxBarHeight / 2f - 2.dp.toPx()),
                                end = Offset(thumbX, centerY + maxBarHeight / 2f + 2.dp.toPx()),
                                strokeWidth = 2.5.dp.toPx(),
                                cap = StrokeCap.Round,
                            )
                        }

                        SeekbarStyle.CAPSULE_PILL -> {
                            val pillH = 12.dp.toPx()
                            val cRadius = CornerRadius(6.dp.toPx(), 6.dp.toPx())
                            drawRoundRect(
                                color = inactiveColor,
                                topLeft = Offset(0f, centerY - pillH / 2f),
                                size = Size(width, pillH),
                                cornerRadius = cRadius,
                            )
                            if (thumbX > 0f) {
                                val activeBrush = Brush.horizontalGradient(
                                    colors = listOf(primaryColor, secondaryColor),
                                    startX = 0f,
                                    endX = thumbX,
                                )
                                drawRoundRect(
                                    brush = activeBrush,
                                    topLeft = Offset(0f, centerY - pillH / 2f),
                                    size = Size(thumbX, pillH),
                                    cornerRadius = cRadius,
                                )
                            }
                            drawCircle(
                                color = primaryColor,
                                radius = 9.dp.toPx(),
                                center = Offset(thumbX, centerY),
                            )
                        }

                        SeekbarStyle.PRISM_SPECTRUM -> {
                            val ribbonH = 8.dp.toPx()
                            val cRadius = CornerRadius(4.dp.toPx(), 4.dp.toPx())
                            drawRoundRect(
                                color = inactiveColor,
                                topLeft = Offset(0f, centerY - ribbonH / 2f),
                                size = Size(width, ribbonH),
                                cornerRadius = cRadius,
                            )
                            if (thumbX > 0f) {
                                val prismBrush = Brush.horizontalGradient(
                                    colors = listOf(
                                        Color(0xFF00F0FF),
                                        Color(0xFFFF2E93),
                                        Color(0xFFFFC700),
                                    ),
                                    startX = 0f,
                                    endX = thumbX,
                                )
                                drawRoundRect(
                                    brush = prismBrush,
                                    topLeft = Offset(0f, centerY - ribbonH / 2f),
                                    size = Size(thumbX, ribbonH),
                                    cornerRadius = cRadius,
                                )
                            }
                            drawCircle(
                                color = Color(0xFF00F0FF),
                                radius = 9.dp.toPx(),
                                center = Offset(thumbX, centerY),
                            )
                        }
                    }
                }

                Slider(
                    value = shownFraction,
                    onValueChange = { frac ->
                        dragPositionMs = frac * boundedDurationMs
                    },
                    onValueChangeFinished = {
                        onSeek(dragPositionMs.toLong().coerceIn(0L, boundedDurationMs))
                    },
                    interactionSource = interactionSource,
                    colors = SliderDefaults.colors(
                        thumbColor = Color.Transparent,
                        activeTrackColor = Color.Transparent,
                        inactiveTrackColor = Color.Transparent,
                    ),
                    modifier = Modifier
                        .matchParentSize()
                        .alpha(0f),
                )
            }

            if (showTimeLabels) {
                Spacer(Modifier.height(2.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        formatMs(shownMs),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = textColor,
                    )
                    Text(
                        formatMs(boundedDurationMs),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = textColor,
                    )
                }
            }
        }
    }
}

private fun formatMs(ms: Long): String {
    val totalSeconds = (ms / 1000).coerceAtLeast(0)
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return "%d:%02d".format(minutes, seconds)
}
