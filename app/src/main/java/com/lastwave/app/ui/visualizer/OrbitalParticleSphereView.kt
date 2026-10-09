package com.lastwave.app.ui.visualizer

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameMillis
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

private data class Particle3D(
    val phi: Float,
    val theta: Float,
    val baseRadius: Float,
    val speed: Float,
)

@Composable
fun OrbitalParticleSphereView(
    pcmData: FloatArray,
    modifier: Modifier = Modifier,
    primaryColor: Color = MaterialTheme.colorScheme.primary,
    accentColor: Color = MaterialTheme.colorScheme.tertiary,
) {
    var rotX by remember { mutableFloatStateOf(0.2f) }
    var rotY by remember { mutableFloatStateOf(0.3f) }
    var animPhase by remember { mutableFloatStateOf(0f) }

    val (bassEnergy, midEnergy, trebleEnergy) = remember(pcmData) {
        if (pcmData.isEmpty()) Triple(0f, 0f, 0f)
        else {
            val size = pcmData.size
            val bassEnd = (size * 0.15).toInt().coerceIn(1, size)
            val midEnd = (size * 0.60).toInt().coerceIn(bassEnd, size)

            var bSum = 0f
            for (i in 0 until bassEnd) bSum += kotlin.math.abs(pcmData[i])
            val b = (bSum / bassEnd * 3.0f).coerceIn(0f, 1f)

            var mSum = 0f
            for (i in bassEnd until midEnd) mSum += kotlin.math.abs(pcmData[i])
            val m = (mSum / (midEnd - bassEnd) * 3.5f).coerceIn(0f, 1f)

            var tSum = 0f
            for (i in midEnd until size) tSum += kotlin.math.abs(pcmData[i])
            val t = (tSum / (size - midEnd) * 4.0f).coerceIn(0f, 1f)

            Triple(b, m, t)
        }
    }

    LaunchedEffect(Unit) {
        while (true) {
            withFrameMillis {
                animPhase += 0.016f * (1f + trebleEnergy * 2f)
            }
        }
    }

    val particleCount = 350
    val particles = remember {
        val list = ArrayList<Particle3D>(particleCount)
        val goldenRatio = (1f + sqrt(5f)) / 2f
        for (i in 0 until particleCount) {
            val theta = 2f * PI.toFloat() * i / goldenRatio
            val phi = kotlin.math.acos(1f - 2f * (i + 0.5f) / particleCount)
            list.add(Particle3D(phi, theta, 180f, 0.5f + (i % 5) * 0.1f))
        }
        list
    }

    Canvas(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    rotY += dragAmount.x * 0.008f
                    rotX += dragAmount.y * 0.008f
                }
            }
    ) {
        val w = size.width
        val h = size.height
        val centerX = w / 2f
        val centerY = h / 2f

        val sphereRadius = minOf(w, h) * 0.28f * (1f + bassEnergy * 0.35f)
        val fov = 600f

        val cosX = cos(rotX.toDouble()).toFloat()
        val sinX = sin(rotX.toDouble()).toFloat()
        val cosY = cos(rotY.toDouble()).toFloat()
        val sinY = sin(rotY.toDouble()).toFloat()

        for (i in particles.indices) {
            val p = particles[i]

            val waveDisplacement = sin((p.phi * 5f + animPhase * 2f).toDouble()).toFloat() * midEnergy * 25f
            val r = sphereRadius + waveDisplacement

            val x0 = r * sin(p.phi.toDouble()).toFloat() * cos(p.theta.toDouble()).toFloat()
            val y0 = r * sin(p.phi.toDouble()).toFloat() * sin(p.theta.toDouble()).toFloat()
            val z0 = r * cos(p.phi.toDouble()).toFloat()

            val x1 = x0 * cosY - z0 * sinY
            val z1 = x0 * sinY + z0 * cosY

            val y1 = y0 * cosX - z1 * sinX
            val z1Final = y0 * sinX + z1 * cosX

            val distance = 400f
            val scale = fov / (fov + z1Final + distance)
            val px = centerX + x1 * scale
            val py = centerY + y1 * scale

            val sizePx = (3.5f * scale).coerceIn(1f, 8f)
            val depthAlpha = ((z1Final + sphereRadius) / (2f * sphereRadius)).coerceIn(0.15f, 1f)
            val glitter = (1f + trebleEnergy * 0.6f)
            val finalAlpha = (depthAlpha * glitter).coerceIn(0.1f, 1f)

            val pColor = if (i % 2 == 0) primaryColor else accentColor

            drawCircle(
                color = pColor.copy(alpha = finalAlpha),
                radius = sizePx,
                center = Offset(px, py),
            )
        }
    }
}
