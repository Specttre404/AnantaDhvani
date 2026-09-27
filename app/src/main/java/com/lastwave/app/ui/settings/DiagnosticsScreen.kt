package com.lastwave.app.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.HighQuality
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lastwave.app.playback.MusicPlayer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiagnosticsScreen(
    player: MusicPlayer,
    onBackClick: () -> Unit,
) {
    val playerState by player.state.collectAsStateWithLifecycle()
    val signalPath by player.signalPath.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "System & Audio Diagnostics",
                        fontWeight = FontWeight.Bold,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                ),
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            // Live Stream Header Card
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                ),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Row(
                    modifier = Modifier.padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(MaterialTheme.colorScheme.primary),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            Icons.Filled.HighQuality,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(26.dp),
                        )
                    }
                    Spacer(Modifier.width(16.dp))
                    Column {
                        Text(
                            text = playerState.current?.title ?: "No Track Playing",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                        )
                        Text(
                            text = playerState.current?.artist ?: "Audio Engine Idle",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f),
                        )
                    }
                }
            }

            // Audio Spec Grid
            DiagnosticSection(
                title = "Audio Engine & Codec Specs",
                icon = Icons.Filled.GraphicEq,
            ) {
                DiagnosticItem("Audio Codec", playerState.audioCodec ?: "Opus / AAC")
                DiagnosticItem("Bitrate", playerState.bitrateKbps?.let { "$it kbps" } ?: "Variable")
                DiagnosticItem("Sampling Rate", playerState.samplingRateKHz?.let { "$it kHz" } ?: "44.1 kHz")
                DiagnosticItem("Bit Depth", playerState.bitDepth?.let { "$it-bit" } ?: "16-bit")
                DiagnosticItem("Encoding Type", if (playerState.isLossless) "Lossless (FLAC)" else "Compressed Lossy")
            }

            // DAC Signal Path Specs
            DiagnosticSection(
                title = "DAC Signal Path",
                icon = Icons.Filled.Memory,
            ) {
                DiagnosticItem("Output Device", signalPath.dacName ?: "Built-in Speaker / System Mixer")
                DiagnosticItem(
                    "Bit-Perfect Mode",
                    if (signalPath.bitPerfect) "Active (Direct Passthrough)" else "Disabled (Software Mixed)",
                )
                DiagnosticItem("Platform Sample Rate", if (signalPath.platformRateHz > 0) "${signalPath.platformRateHz / 1000.0} kHz" else "44.1 kHz")
            }

            // System Performance Specs
            DiagnosticSection(
                title = "Engine Performance & Buffer",
                icon = Icons.Filled.Speed,
            ) {
                val runtime = Runtime.getRuntime()
                val usedMemMb = (runtime.totalMemory() - runtime.freeMemory()) / (1024 * 1024)
                val maxMemMb = runtime.maxMemory() / (1024 * 1024)

                DiagnosticItem("Buffer State", if (playerState.isBuffering) "Buffering..." else "Healthy")
                DiagnosticItem("JVM Memory Usage", "$usedMemMb MB / $maxMemMb MB")
                DiagnosticItem("Active Threads", Thread.activeCount().toString())
            }
        }
    }
}

@Composable
private fun DiagnosticSection(
    title: String,
    icon: ImageVector,
    content: @Composable () -> Unit,
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
        ),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp),
                )
                Spacer(Modifier.width(10.dp))
                Text(
                    title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                )
            }
            content()
        }
    }
}

@Composable
private fun DiagnosticItem(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}
