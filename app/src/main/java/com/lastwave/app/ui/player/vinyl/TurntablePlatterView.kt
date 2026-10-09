package com.lastwave.app.ui.player.vinyl

import android.os.SystemClock
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.lastwave.app.playback.MusicPlayer
import kotlinx.coroutines.launch
import kotlin.math.atan2

@Composable
fun TurntablePlatterView(
    musicPlayer: MusicPlayer,
    modifier: Modifier = Modifier,
) {
    val scope = rememberCoroutineScope()
    val primaryColor = MaterialTheme.colorScheme.primary

    var lastAngle = remember { 0f }
    var lastTimeMs = remember { 0L }

    Box(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { offset ->
                        val cx = size.width / 2f
                        val cy = size.height / 2f
                        lastAngle = Math.toDegrees(atan2((offset.y - cy).toDouble(), (offset.x - cx).toDouble())).toFloat()
                        lastTimeMs = SystemClock.elapsedRealtime()
                    },
                    onDragEnd = {
                        scope.launch {
                            val anim = Animatable(musicPlayer.state.value.speed)
                            anim.animateTo(1.0f, animationSpec = tween(280)) {
                                musicPlayer.setPlaybackSpeed(value)
                            }
                        }
                    },
                    onDragCancel = {
                        scope.launch {
                            val anim = Animatable(musicPlayer.state.value.speed)
                            anim.animateTo(1.0f, animationSpec = tween(280)) {
                                musicPlayer.setPlaybackSpeed(value)
                            }
                        }
                    },
                    onDrag = { change, _ ->
                        change.consume()
                        val cx = size.width / 2f
                        val cy = size.height / 2f
                        val pos = change.position
                        val currentAngle = Math.toDegrees(atan2((pos.y - cy).toDouble(), (pos.x - cx).toDouble())).toFloat()
                        val nowMs = SystemClock.elapsedRealtime()

                        var dAngle = currentAngle - lastAngle
                        if (dAngle > 180f) dAngle -= 360f
                        if (dAngle < -180f) dAngle += 360f

                        val dtMs = (nowMs - lastTimeMs).coerceAtLeast(1L)
                        val angularVelocity = (dAngle / dtMs) * 10f
                        val scratchSpeed = angularVelocity.coerceIn(-3.0f, 3.0f)

                        musicPlayer.setPlaybackSpeed(if (kotlin.math.abs(scratchSpeed) < 0.1f) 0.05f else scratchSpeed)

                        lastAngle = currentAngle
                        lastTimeMs = nowMs
                    }
                )
            }
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height
            val cx = w / 2f
            val cy = h / 2f
            val radius = minOf(w, h) / 2f

            val grooveCount = 6
            for (i in 1..grooveCount) {
                val r = radius * (0.35f + 0.10f * i)
                drawCircle(
                    color = Color.White.copy(alpha = 0.12f),
                    radius = r,
                    center = Offset(cx, cy),
                    style = Stroke(width = 1.dp.toPx())
                )
            }

            drawCircle(
                color = primaryColor,
                radius = 12.dp.toPx(),
                center = Offset(cx, cy)
            )
            drawCircle(
                color = Color.Black,
                radius = 4.dp.toPx(),
                center = Offset(cx, cy)
            )
        }
    }
}
