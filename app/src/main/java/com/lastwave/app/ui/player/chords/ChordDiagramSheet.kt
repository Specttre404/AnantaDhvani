package com.lastwave.app.ui.player.chords

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class ChordFingering(
    val chordName: String,
    val guitarFrets: IntArray,
    val pianoKeyOffsets: List<Int>,
)

object ChordDatabase {
    private val chords = listOf(
        ChordFingering("C", intArrayOf(-1, 3, 2, 0, 1, 0), listOf(0, 4, 7)),
        ChordFingering("Cm", intArrayOf(-1, 3, 5, 5, 4, 3), listOf(0, 3, 7)),
        ChordFingering("C7", intArrayOf(-1, 3, 2, 3, 1, 0), listOf(0, 4, 7, 10)),
        ChordFingering("Cmaj7", intArrayOf(-1, 3, 2, 0, 0, 0), listOf(0, 4, 7, 11)),
        ChordFingering("D", intArrayOf(-1, -1, 0, 2, 3, 2), listOf(2, 6, 9)),
        ChordFingering("Dm", intArrayOf(-1, -1, 0, 2, 3, 1), listOf(2, 5, 9)),
        ChordFingering("D7", intArrayOf(-1, -1, 0, 2, 1, 2), listOf(2, 6, 9, 0)),
        ChordFingering("E", intArrayOf(0, 2, 2, 1, 0, 0), listOf(4, 8, 11)),
        ChordFingering("Em", intArrayOf(0, 2, 2, 0, 0, 0), listOf(4, 7, 11)),
        ChordFingering("E7", intArrayOf(0, 2, 0, 1, 0, 0), listOf(4, 8, 11, 2)),
        ChordFingering("F", intArrayOf(1, 3, 3, 2, 1, 1), listOf(5, 9, 0)),
        ChordFingering("Fm", intArrayOf(1, 3, 3, 1, 1, 1), listOf(5, 8, 0)),
        ChordFingering("G", intArrayOf(3, 2, 0, 0, 0, 3), listOf(7, 11, 2)),
        ChordFingering("Gm", intArrayOf(3, 5, 5, 3, 3, 3), listOf(7, 10, 2)),
        ChordFingering("G7", intArrayOf(3, 2, 0, 0, 0, 1), listOf(7, 11, 2, 5)),
        ChordFingering("A", intArrayOf(-1, 0, 2, 2, 2, 0), listOf(9, 1, 4)),
        ChordFingering("Am", intArrayOf(-1, 0, 2, 2, 1, 0), listOf(9, 0, 4)),
        ChordFingering("A7", intArrayOf(-1, 0, 2, 0, 2, 0), listOf(9, 1, 4, 7)),
        ChordFingering("B", intArrayOf(-1, 2, 4, 4, 4, 2), listOf(11, 3, 6)),
        ChordFingering("Bm", intArrayOf(-1, 2, 4, 4, 3, 2), listOf(11, 2, 6)),
    )

    fun getFingering(name: String): ChordFingering {
        val clean = name.trim()
        return chords.firstOrNull { it.chordName.equals(clean, ignoreCase = true) }
            ?: ChordFingering(clean, intArrayOf(-1, 0, 2, 2, 1, 0), listOf(0, 4, 7))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChordDiagramSheet(
    chordName: String,
    onDismiss: () -> Unit,
) {
    val fingering = remember(chordName) { ChordDatabase.getFingering(chordName) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            ) {
                Text(
                    text = fingering.chordName,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Black,
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 6.dp),
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Guitar Fretboard", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    Spacer(Modifier.height(8.dp))
                    GuitarFretboardCanvas(frets = fingering.guitarFrets)
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Piano Keyboard", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    Spacer(Modifier.height(8.dp))
                    PianoKeyboardCanvas(keyOffsets = fingering.pianoKeyOffsets)
                }
            }
        }
    }
}

@Composable
private fun GuitarFretboardCanvas(frets: IntArray) {
    val primary = MaterialTheme.colorScheme.primary
    val onSurface = MaterialTheme.colorScheme.onSurface

    Canvas(modifier = Modifier.size(width = 130.dp, height = 160.dp)) {
        val w = size.width
        val h = size.height
        val stringCount = 6
        val fretCount = 5

        val startX = 20.dp.toPx()
        val endX = w - 20.dp.toPx()
        val startY = 30.dp.toPx()
        val endY = h - 20.dp.toPx()

        val stringGap = (endX - startX) / (stringCount - 1)
        val fretGap = (endY - startY) / fretCount

        drawLine(color = onSurface, start = Offset(startX, startY), end = Offset(endX, startY), strokeWidth = 5.dp.toPx())

        for (f in 1..fretCount) {
            val y = startY + f * fretGap
            drawLine(color = onSurface.copy(alpha = 0.5f), start = Offset(startX, y), end = Offset(endX, y), strokeWidth = 1.5.dp.toPx())
        }

        for (s in 0 until stringCount) {
            val x = startX + s * stringGap
            drawLine(color = onSurface.copy(alpha = 0.7f), start = Offset(x, startY), end = Offset(x, endY), strokeWidth = 1.5.dp.toPx())

            val fretVal = frets.getOrElse(s) { 0 }
            val indY = startY - 12.dp.toPx()

            when (fretVal) {
                -1 -> {
                    drawLine(color = Color.Red, start = Offset(x - 4.dp.toPx(), indY - 4.dp.toPx()), end = Offset(x + 4.dp.toPx(), indY + 4.dp.toPx()), strokeWidth = 2.dp.toPx())
                    drawLine(color = Color.Red, start = Offset(x + 4.dp.toPx(), indY - 4.dp.toPx()), end = Offset(x - 4.dp.toPx(), indY + 4.dp.toPx()), strokeWidth = 2.dp.toPx())
                }
                0 -> {
                    drawCircle(color = primary, radius = 4.dp.toPx(), center = Offset(x, indY), style = Stroke(width = 1.5.dp.toPx()))
                }
                else -> {
                    val dotY = startY + (fretVal - 0.5f) * fretGap
                    drawCircle(color = primary, radius = 7.dp.toPx(), center = Offset(x, dotY))
                }
            }
        }
    }
}

@Composable
private fun PianoKeyboardCanvas(keyOffsets: List<Int>) {
    val primary = MaterialTheme.colorScheme.primary
    val onSurface = MaterialTheme.colorScheme.onSurface

    Canvas(modifier = Modifier.size(width = 140.dp, height = 150.dp)) {
        val w = size.width
        val h = size.height

        val whiteKeyWidth = w / 7f
        val whiteKeyHeight = h - 20.dp.toPx()

        val whiteKeySemitones = intArrayOf(0, 2, 4, 5, 7, 9, 11)
        val blackKeySemitones = intArrayOf(1, 3, -1, 6, 8, 10, -1)

        for (k in 0 until 7) {
            val x = k * whiteKeyWidth
            val semitone = whiteKeySemitones[k]
            val isPressed = semitone in keyOffsets || (semitone + 12) in keyOffsets

            val keyColor = if (isPressed) primary else Color.White
            drawRect(color = keyColor, topLeft = Offset(x, 10.dp.toPx()), size = Size(whiteKeyWidth - 2.dp.toPx(), whiteKeyHeight))
            drawRect(color = onSurface.copy(alpha = 0.4f), topLeft = Offset(x, 10.dp.toPx()), size = Size(whiteKeyWidth - 2.dp.toPx(), whiteKeyHeight), style = Stroke(1.dp.toPx()))
        }

        val blackKeyWidth = whiteKeyWidth * 0.6f
        val blackKeyHeight = whiteKeyHeight * 0.6f

        for (k in 0 until 6) {
            val semitone = blackKeySemitones[k]
            if (semitone != -1) {
                val x = (k + 1) * whiteKeyWidth - blackKeyWidth / 2f
                val isPressed = semitone in keyOffsets || (semitone + 12) in keyOffsets
                val keyColor = if (isPressed) primary else Color.Black

                drawRect(color = keyColor, topLeft = Offset(x, 10.dp.toPx()), size = Size(blackKeyWidth, blackKeyHeight))
            }
        }
    }
}
