package com.lastwave.app.ui.player

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

enum class OscilloscopeStyle {
    LUMINESCENT_PHASOR,
    CHROMATIC_LISSAJOUS,
    MULTI_BEZIER_SPLINE,
    DISCRETE_STEM_PLOT,
}

@Composable
fun AudioOscilloscopeView(
    pcmData: FloatArray,
    style: OscilloscopeStyle = OscilloscopeStyle.MULTI_BEZIER_SPLINE,
    modifier: Modifier = Modifier,
) {
    Canvas(modifier = modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        val centerY = h / 2f

        if (pcmData.isEmpty()) return@Canvas

        when (style) {
            OscilloscopeStyle.MULTI_BEZIER_SPLINE,
            OscilloscopeStyle.LUMINESCENT_PHASOR -> {
                val path = Path()
                val step = w / (pcmData.size - 1).coerceAtLeast(1)
                path.moveTo(0f, centerY)
                for (i in pcmData.indices) {
                    val x = i * step
                    val y = centerY + (pcmData[i] * h * 0.4f)
                    if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
                }
                drawPath(
                    path = path,
                    color = if (style == OscilloscopeStyle.LUMINESCENT_PHASOR) Color(0xFF00FF66) else Color(0xFF00E5FF),
                    style = Stroke(width = 2.dp.toPx()),
                )
            }
            OscilloscopeStyle.CHROMATIC_LISSAJOUS -> {
                val radius = minOf(w, h) * 0.35f
                val path = Path()
                val mid = pcmData.size / 2
                for (i in 0 until mid) {
                    val x = w / 2f + (pcmData[i] * radius)
                    val y = h / 2f + (pcmData.getOrElse(i + mid) { 0f } * radius)
                    if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
                }
                drawPath(
                    path = path,
                    color = Color(0xFFBB86FC),
                    style = Stroke(width = 1.8.dp.toPx()),
                )
            }
            OscilloscopeStyle.DISCRETE_STEM_PLOT -> {
                val step = w / pcmData.size.coerceAtLeast(1)
                for (i in pcmData.indices) {
                    val x = i * step + step / 2f
                    val y = centerY + (pcmData[i] * h * 0.4f)
                    drawLine(
                        color = Color(0xFFFF4081),
                        start = Offset(x, centerY),
                        end = Offset(x, y),
                        strokeWidth = 2.dp.toPx(),
                    )
                }
            }
        }
    }
}
