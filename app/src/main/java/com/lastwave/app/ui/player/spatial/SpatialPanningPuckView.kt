package com.lastwave.app.ui.player.spatial

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun SpatialPanningPuckView(
    currentHaasDelayMs: Float = 18.0f,
    currentWidthRatio: Float = 1.3f,
    onSpatialChanged: (haasDelayMs: Float, widthRatio: Float) -> Unit,
    modifier: Modifier = Modifier,
) {
    var widthRatio by remember(currentWidthRatio) { mutableFloatStateOf(currentWidthRatio) }
    var haasDelayMs by remember(currentHaasDelayMs) { mutableFloatStateOf(currentHaasDelayMs) }

    val primaryColor = MaterialTheme.colorScheme.primary
    val surfaceVariant = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.35f)

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colorScheme.surfaceContainerHighest.copy(alpha = 0.5f),
            modifier = Modifier
                .size(240.dp)
                .padding(8.dp),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(Unit) {
                        detectTapGestures { offset ->
                            val nx = (offset.x / size.width).coerceIn(0f, 1f)
                            val ny = (offset.y / size.height).coerceIn(0f, 1f)
                            widthRatio = 0.5f + nx * 2.0f
                            haasDelayMs = 35.0f - ny * 30.0f
                            onSpatialChanged(haasDelayMs, widthRatio)
                        }
                    }
                    .pointerInput(Unit) {
                        detectDragGestures { change, dragAmount ->
                            change.consume()
                            val curNx = (widthRatio - 0.5f) / 2.0f
                            val curNy = (35.0f - haasDelayMs) / 30.0f

                            val newNx = (curNx + dragAmount.x / size.width).coerceIn(0f, 1f)
                            val newNy = (curNy + dragAmount.y / size.height).coerceIn(0f, 1f)

                            widthRatio = 0.5f + newNx * 2.0f
                            haasDelayMs = 35.0f - newNy * 30.0f
                            onSpatialChanged(haasDelayMs, widthRatio)
                        }
                    }
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val w = size.width
                    val h = size.height
                    val cx = w / 2f
                    val cy = h / 2f

                    drawCircle(color = surfaceVariant, radius = minOf(w, h) * 0.40f, style = Stroke(width = 1.5.dp.toPx(), pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f))))
                    drawCircle(color = surfaceVariant, radius = minOf(w, h) * 0.25f, style = Stroke(width = 1.5.dp.toPx()))

                    drawLine(color = surfaceVariant, start = Offset(cx, 16.dp.toPx()), end = Offset(cx, h - 16.dp.toPx()), strokeWidth = 1.5.dp.toPx())
                    drawLine(color = surfaceVariant, start = Offset(16.dp.toPx(), cy), end = Offset(w - 16.dp.toPx(), cy), strokeWidth = 1.5.dp.toPx())

                    val puckNx = (widthRatio - 0.5f) / 2.0f
                    val puckNy = (35.0f - haasDelayMs) / 30.0f
                    val puckX = puckNx * w
                    val puckY = puckNy * h

                    drawCircle(color = primaryColor.copy(alpha = 0.35f), radius = 22.dp.toPx(), center = Offset(puckX, puckY))
                    drawCircle(color = primaryColor, radius = 14.dp.toPx(), center = Offset(puckX, puckY))
                    drawCircle(color = Color.White, radius = 5.dp.toPx(), center = Offset(puckX, puckY))
                }
            }
        }

        Spacer(Modifier.height(8.dp))

        Text(
            text = "Width / Azimuth: ${"%.2f".format(widthRatio)}x  •  Acoustic Depth: ${"%.1f".format(haasDelayMs)} ms",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
        )
    }
}
