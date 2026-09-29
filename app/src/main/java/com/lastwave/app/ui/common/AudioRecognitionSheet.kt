package com.lastwave.app.ui.common

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.lastwave.app.data.recognition.AudioRecognitionManager
import com.lastwave.app.data.recognition.RecognizedSong
import com.lastwave.app.playback.PlayableTrack
import com.lastwave.app.ui.player.LocalMusicPlayer
import kotlinx.coroutines.launch

/**
 * Modal bottom sheet for In-App Ambient Audio Track Identification:
 * - Radar pulse wave animation while recording 5 seconds.
 * - Captures 16-bit PCM, 44.1 kHz via AudioRecord.
 * - Displays matched track card with high-res artwork and a 1-tap "Play Now in LastWaveX" button.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AudioRecognitionSheet(
    recognitionManager: AudioRecognitionManager,
    onDismiss: () -> Unit,
    onPlayRecognizedTrack: (RecognizedSong) -> Unit,
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var isRecording by remember { mutableStateOf(false) }
    var recognizedSong by remember { mutableStateOf<RecognizedSong?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    fun startListening() {
        if (isRecording) return
        isRecording = true
        errorMessage = null
        recognizedSong = null

        scope.launch {
            val result = recognitionManager.recognizeAudio()
            isRecording = false
            result.onSuccess { song ->
                recognizedSong = song
            }.onFailure { error ->
                errorMessage = error.message ?: "Could not recognize audio. Try again in a quiet environment."
            }
        }
    }

    val micPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
    ) { isGranted ->
        if (isGranted) {
            startListening()
        } else {
            errorMessage = "Microphone permission required for sound recognition."
        }
    }

    LaunchedEffect(Unit) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
            startListening()
        } else {
            micPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    "Sound Recognition",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                )
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Filled.Close, contentDescription = "Close")
                }
            }

            if (isRecording) {
                // Radar Pulse Animation
                RadarPulseAnimation()

                Spacer(Modifier.height(8.dp))
                Text(
                    "Listening for ambient music...",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary,
                )
                Text(
                    "Keep your device near the audio source (5 seconds)",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                )
            } else if (recognizedSong != null) {
                val song = recognizedSong!!

                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Row(
                        modifier = Modifier.padding(20.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(64.dp),
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    Icons.Filled.MusicNote,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onPrimary,
                                    modifier = Modifier.size(32.dp),
                                )
                            }
                        }

                        Spacer(Modifier.width(16.dp))

                        Column(Modifier.weight(1f)) {
                            Text(
                                song.title,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                            )
                            Text(
                                song.artist,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f),
                            )
                            if (!song.album.isNullOrBlank()) {
                                Text(
                                    song.album,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.6f),
                                )
                            }
                        }
                    }
                }

                Button(
                    onClick = {
                        onPlayRecognizedTrack(song)
                        onDismiss()
                    },
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.PlayArrow, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text("Play Now in LastWaveX", fontWeight = FontWeight.Bold)
                    }
                }
            } else {
                Text(
                    errorMessage ?: "Failed to detect song",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center,
                )

                Button(
                    onClick = { startListening() },
                    shape = CircleShape,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text("Try Again")
                }
            }
            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
private fun RadarPulseAnimation() {
    val infiniteTransition = rememberInfiniteTransition(label = "RadarPulse")

    val pulse1 by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(tween(1800, easing = FastOutSlowInEasing), repeatMode = RepeatMode.Restart),
        label = "pulse1",
    )
    val pulse2 by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(tween(1800, delayMillis = 600, easing = FastOutSlowInEasing), repeatMode = RepeatMode.Restart),
        label = "pulse2",
    )

    val primaryColor = MaterialTheme.colorScheme.primary

    Box(
        modifier = Modifier.size(140.dp),
        contentAlignment = Alignment.Center,
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val maxRadius = size.minDimension / 2f
            drawCircle(
                color = primaryColor.copy(alpha = (1f - pulse1).coerceIn(0f, 1f)),
                radius = maxRadius * pulse1,
                style = Stroke(3.dp.toPx()),
            )
            drawCircle(
                color = primaryColor.copy(alpha = (1f - pulse2).coerceIn(0f, 1f)),
                radius = maxRadius * pulse2,
                style = Stroke(2.dp.toPx()),
            )
        }

        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(primaryColor),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                Icons.Filled.GraphicEq,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.size(32.dp),
            )
        }
    }
}
