package com.lastwave.app.ui.player

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
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
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.lastwave.app.data.local.SeekbarStyle
import kotlin.math.PI
import kotlin.math.sin

private fun Color.shiftTonal(lightnessDelta: Float, saturationScale: Float = 1.0f): Color {
    val hsv = FloatArray(3)
    android.graphics.Color.RGBToHSV(
        (red * 255).toInt().coerceIn(0, 255),
        (green * 255).toInt().coerceIn(0, 255),
        (blue * 255).toInt().coerceIn(0, 255),
        hsv,
    )
    hsv[1] = (hsv[1] * saturationScale).coerceIn(0.15f, 1.0f)
    hsv[2] = (hsv[2] + lightnessDelta).coerceIn(0.15f, 1.0f)
    val rgb = android.graphics.Color.HSVToColor(hsv)
    return Color(rgb)
}

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

    val primaryColor = if (isTranslucent) Color.White else MaterialTheme.colorScheme.primary
    val secondaryColor = if (isTranslucent) Color.White else MaterialTheme.colorScheme.secondary
    val tertiaryColor = if (isTranslucent) Color.White else MaterialTheme.colorScheme.tertiary

    val inactiveColor = if (isTranslucent) {
        Color.White.copy(alpha = 0.22f)
    } else {
        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.14f)
    }
    val textColor = if (isTranslucent) {
        Color.White.copy(alpha = 0.85f)
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant
    }

    val layer1Light = remember(tertiaryColor, isTranslucent) { tertiaryColor.shiftTonal(lightnessDelta = +0.18f, saturationScale = 0.85f) }
    val layer1Dark = remember(tertiaryColor, isTranslucent) { tertiaryColor.shiftTonal(lightnessDelta = -0.15f, saturationScale = 1.30f) }
    val layer2Dark = remember(secondaryColor, isTranslucent) { secondaryColor.shiftTonal(lightnessDelta = -0.16f, saturationScale = 1.30f) }
    val layer2Light = remember(secondaryColor, isTranslucent) { secondaryColor.shiftTonal(lightnessDelta = +0.18f, saturationScale = 0.85f) }
    val layer3Light = remember(primaryColor, isTranslucent) { primaryColor.shiftTonal(lightnessDelta = +0.20f, saturationScale = 0.90f) }
    val layer3Dark = remember(primaryColor, isTranslucent) { primaryColor.shiftTonal(lightnessDelta = -0.14f, saturationScale = 1.35f) }
    val thumbColor = remember(primaryColor) { primaryColor.shiftTonal(lightnessDelta = -0.10f, saturationScale = 1.25f) }

    val infiniteTransition = rememberInfiniteTransition(label = "WaveAnimation")

    val phase1 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = (2 * PI).toFloat(),
        animationSpec = infiniteRepeatable(tween(2400, easing = LinearEasing), repeatMode = RepeatMode.Restart),
        label = "Phase1",
    )
    val phase2 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = (2 * PI).toFloat(),
        animationSpec = infiniteRepeatable(tween(1800, easing = LinearEasing), repeatMode = RepeatMode.Restart),
        label = "Phase2",
    )
    val phase3 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = (2 * PI).toFloat(),
        animationSpec = infiniteRepeatable(tween(1300, easing = LinearEasing), repeatMode = RepeatMode.Restart),
        label = "Phase3",
    )

    val wavesActive = isPlaying && !dragging
    val currPhase1 = if (wavesActive) phase1 + 2.2f else 2.2f
    val currPhase2 = if (wavesActive) phase2 + 1.2f else 1.2f
    val currPhase3 = if (wavesActive) phase3 else 0f

    val density = LocalDensity.current
    val baseAmp1Px = with(density) { 13.0.dp.toPx() }
    val baseAmp2Px = with(density) { 10.0.dp.toPx() }
    val baseAmp3Px = with(density) { 7.5.dp.toPx() }
    val draggingAmpPx = with(density) { 1.2.dp.toPx() }

    val targetAmp1 = if (dragging) draggingAmpPx else if (isPlaying) baseAmp1Px else baseAmp1Px * 0.25f
    val targetAmp2 = if (dragging) draggingAmpPx else if (isPlaying) baseAmp2Px else baseAmp2Px * 0.25f
    val targetAmp3 = if (dragging) draggingAmpPx else if (isPlaying) baseAmp3Px else baseAmp3Px * 0.25f

    val amp1 by animateFloatAsState(targetAmp1, tween(240), label = "Amp1")
    val amp2 by animateFloatAsState(targetAmp2, tween(240), label = "Amp2")
    val amp3 by animateFloatAsState(targetAmp3, tween(240), label = "Amp3")

    val waveLength1Px = with(density) { 160.dp.toPx() }
    val waveLength2Px = with(density) { 125.dp.toPx() }
    val waveLength3Px = with(density) { 95.dp.toPx() }

    val baseTrackThicknessPx = with(density) { 4.5.dp.toPx() }
    val transitionLengthPx = with(density) { 44.dp.toPx() }
    val waveSampleStepPx = with(density) { 1.5.dp.toPx() }

    val pathFilled1 = remember { Path() }
    val pathContour1 = remember { Path() }
    val pathFilled2 = remember { Path() }
    val pathContour2 = remember { Path() }
    val pathFilled3 = remember { Path() }
    val pathContour3 = remember { Path() }
    val clipPathBounds = remember { Path() }

    Column(modifier = modifier.fillMaxWidth()) {
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
            ) {
                Canvas(modifier = Modifier.matchParentSize()) {
                    val width = size.width
                    val height = size.height
                    val centerY = height / 2f + 4.dp.toPx()
                    val thumbX = (shownFraction * width).coerceIn(0f, width)

                    when (seekbarStyle) {
                        SeekbarStyle.WAVY_FLUID -> {
                            val halfThickness = baseTrackThicknessPx / 2f
                            drawLine(
                                color = inactiveColor,
                                start = Offset(0f, centerY),
                                end = Offset(width, centerY),
                                strokeWidth = baseTrackThicknessPx,
                                cap = StrokeCap.Round,
                            )

                            if (thumbX > 0f) {
                                clipPathBounds.reset()
                                clipPathBounds.addRoundRect(
                                    RoundRect(
                                        rect = androidx.compose.ui.geometry.Rect(
                                            left = -halfThickness,
                                            top = 0f,
                                            right = thumbX,
                                            bottom = height,
                                        ),
                                        cornerRadius = CornerRadius(baseTrackThicknessPx, baseTrackThicknessPx),
                                    )
                                )

                                clipPath(clipPathBounds) {
                                    val bottomY = centerY + halfThickness
                                    val topBaselineY = centerY - halfThickness

                                    // Layer 1
                                    pathFilled1.reset()
                                    pathContour1.reset()
                                    pathFilled1.moveTo(0f, bottomY)
                                    pathContour1.moveTo(0f, topBaselineY)

                                    var x = 0f
                                    while (x <= thumbX) {
                                        val damping = ((thumbX - x) / transitionLengthPx).coerceIn(0f, 1f)
                                        val waveY = topBaselineY - sin((x / waveLength1Px) * 2 * PI + currPhase1).toFloat() * amp1 * damping
                                        pathFilled1.lineTo(x, waveY)
                                        pathContour1.lineTo(x, waveY)
                                        x += waveSampleStepPx
                                    }
                                    pathFilled1.lineTo(thumbX, bottomY)
                                    pathFilled1.close()

                                    val grad1 = Brush.horizontalGradient(
                                        colors = listOf(layer1Light, layer1Dark),
                                        startX = 0f,
                                        endX = thumbX.coerceAtLeast(1f),
                                    )
                                    drawPath(pathFilled1, brush = grad1)
                                    drawPath(pathContour1, color = layer1Light, style = Stroke(1.2.dp.toPx(), cap = StrokeCap.Round))

                                    // Layer 2
                                    pathFilled2.reset()
                                    pathContour2.reset()
                                    pathFilled2.moveTo(0f, bottomY)
                                    pathContour2.moveTo(0f, topBaselineY)

                                    x = 0f
                                    while (x <= thumbX) {
                                        val damping = ((thumbX - x) / transitionLengthPx).coerceIn(0f, 1f)
                                        val waveY = topBaselineY - sin((x / waveLength2Px) * 2 * PI + currPhase2).toFloat() * amp2 * damping
                                        pathFilled2.lineTo(x, waveY)
                                        pathContour2.lineTo(x, waveY)
                                        x += waveSampleStepPx
                                    }
                                    pathFilled2.lineTo(thumbX, bottomY)
                                    pathFilled2.close()

                                    val grad2 = Brush.horizontalGradient(
                                        colors = listOf(layer2Dark, layer2Light),
                                        startX = 0f,
                                        endX = thumbX.coerceAtLeast(1f),
                                    )
                                    drawPath(pathFilled2, brush = grad2)
                                    drawPath(pathContour2, color = layer2Light, style = Stroke(1.4.dp.toPx(), cap = StrokeCap.Round))

                                    // Layer 3
                                    pathFilled3.reset()
                                    pathContour3.reset()
                                    pathFilled3.moveTo(0f, bottomY)
                                    pathContour3.moveTo(0f, topBaselineY)

                                    x = 0f
                                    while (x <= thumbX) {
                                        val damping = ((thumbX - x) / transitionLengthPx).coerceIn(0f, 1f)
                                        val waveY = topBaselineY - sin((x / waveLength3Px) * 2 * PI + currPhase3).toFloat() * amp3 * damping
                                        pathFilled3.lineTo(x, waveY)
                                        pathContour3.lineTo(x, waveY)
                                        x += waveSampleStepPx
                                    }
                                    pathFilled3.lineTo(thumbX, bottomY)
                                    pathFilled3.close()

                                    val grad3 = Brush.horizontalGradient(
                                        colors = listOf(layer3Light, layer3Dark),
                                        startX = 0f,
                                        endX = thumbX.coerceAtLeast(1f),
                                    )
                                    drawPath(pathFilled3, brush = grad3)
                                    drawPath(pathContour3, color = layer3Light, style = Stroke(1.6.dp.toPx(), cap = StrokeCap.Round))
                                }
                            }

                            drawCircle(
                                color = thumbColor,
                                radius = 7.5.dp.toPx(),
                                center = Offset(thumbX, centerY),
                            )
                        }

                        SeekbarStyle.SEGMENTED_DASH -> {
                            val segmentCount = 32
                            val gap = 4.dp.toPx()
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
                                    cornerRadius = CornerRadius(3.dp.toPx(), 3.dp.toPx()),
                                )
                            }
                            drawCircle(
                                color = primaryColor,
                                radius = 9.dp.toPx(),
                                center = Offset(thumbX, centerY),
                            )
                        }

                        SeekbarStyle.MINIMAL_PILL -> {
                            val pillHeight = if (dragging) 10.dp.toPx() else 3.5.dp.toPx()
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
                                radius = if (dragging) 10.dp.toPx() else 6.dp.toPx(),
                                center = Offset(thumbX, centerY),
                            )
                        }

                        SeekbarStyle.STUDIO_CONSOLE -> {
                            val trackH = 6.dp.toPx()
                            // Top/Bottom calibration ticks
                            val tickCount = 20
                            for (i in 0..tickCount) {
                                val tx = (width / tickCount) * i
                                drawLine(
                                    color = textColor.copy(alpha = 0.35f),
                                    start = Offset(tx, centerY - 14.dp.toPx()),
                                    end = Offset(tx, centerY - 8.dp.toPx()),
                                    strokeWidth = 1.2.dp.toPx(),
                                )
                                drawLine(
                                    color = textColor.copy(alpha = 0.35f),
                                    start = Offset(tx, centerY + 8.dp.toPx()),
                                    end = Offset(tx, centerY + 14.dp.toPx()),
                                    strokeWidth = 1.2.dp.toPx(),
                                )
                            }

                            // Track
                            drawRoundRect(
                                color = inactiveColor,
                                topLeft = Offset(0f, centerY - trackH / 2f),
                                size = Size(width, trackH),
                                cornerRadius = CornerRadius(2.dp.toPx(), 2.dp.toPx()),
                            )
                            if (thumbX > 0f) {
                                drawRoundRect(
                                    color = primaryColor.copy(alpha = 0.85f),
                                    topLeft = Offset(0f, centerY - trackH / 2f),
                                    size = Size(thumbX, trackH),
                                    cornerRadius = CornerRadius(2.dp.toPx(), 2.dp.toPx()),
                                )
                            }

                            // Console Fader Knob
                            val faderW = 16.dp.toPx()
                            val faderH = 24.dp.toPx()
                            drawRoundRect(
                                color = Color(0xFF2A2A2E),
                                topLeft = Offset(thumbX - faderW / 2f, centerY - faderH / 2f),
                                size = Size(faderW, faderH),
                                cornerRadius = CornerRadius(4.dp.toPx(), 4.dp.toPx()),
                            )
                            drawRoundRect(
                                color = primaryColor,
                                topLeft = Offset(thumbX - 1.5.dp.toPx(), centerY - faderH / 2f + 3.dp.toPx()),
                                size = Size(3.dp.toPx(), faderH - 6.dp.toPx()),
                                cornerRadius = CornerRadius(1.dp.toPx(), 1.dp.toPx()),
                            )
                        }

                        SeekbarStyle.CAPSULE_PILL -> {
                            val pillH = 10.dp.toPx()
                            val cRadius = CornerRadius(5.dp.toPx(), 5.dp.toPx())
                            drawRoundRect(
                                color = inactiveColor,
                                topLeft = Offset(0f, centerY - pillH / 2f),
                                size = Size(width, pillH),
                                cornerRadius = cRadius,
                            )
                            if (thumbX > 0f) {
                                drawRoundRect(
                                    color = primaryColor,
                                    topLeft = Offset(0f, centerY - pillH / 2f),
                                    size = Size(thumbX, pillH),
                                    cornerRadius = cRadius,
                                )
                            }
                            drawCircle(
                                color = primaryColor,
                                radius = 8.dp.toPx(),
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
                Spacer(Modifier.height(4.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 2.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        formatMs(shownMs),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = textColor,
                    )
                    Text(
                        formatMs(boundedDurationMs),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.SemiBold,
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
