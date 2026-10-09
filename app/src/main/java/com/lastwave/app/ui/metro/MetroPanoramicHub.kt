package com.lastwave.app.ui.metro

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.PlaylistPlay
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lastwave.app.playback.MusicPlayer
import com.lastwave.app.ui.common.ArtworkImage

@Composable
fun MetroPanoramicHub(
    musicPlayer: MusicPlayer,
    onOpenSettings: () -> Unit,
    onOpenPlaylists: () -> Unit,
    onOpenDownloads: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val playerState by musicPlayer.state.collectAsStateWithLifecycle()
    val track = playerState.current
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF0F0F12))
            .padding(top = 32.dp, bottom = 24.dp),
    ) {
        Text(
            text = "ananta dhvani",
            fontSize = 42.sp,
            fontWeight = FontWeight.Light,
            color = Color.White.copy(alpha = 0.85f),
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp),
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .horizontalScroll(scrollState)
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Surface(
                onClick = { musicPlayer.togglePlayPause() },
                shape = RoundedCornerShape(20.dp),
                color = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier
                    .width(320.dp)
                    .fillMaxHeight(0.85f),
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text("NOW PLAYING", fontSize = 28.sp, fontWeight = FontWeight.Black, letterSpacing = (-1).sp)

                    if (track != null) {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            ArtworkImage(
                                name = track.title,
                                artist = track.artist,
                                embeddedUrl = track.artworkUrl,
                                fallbackIcon = Icons.Filled.MusicNote,
                                modifier = Modifier.size(120.dp),
                            )
                            Text(track.title, fontSize = 20.sp, fontWeight = FontWeight.Bold, maxLines = 1)
                            Text(track.artist, fontSize = 14.sp, color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f), maxLines = 1)
                        }
                    } else {
                        Text("Tap to start music playback", fontSize = 16.sp)
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        Icon(if (playerState.isPlaying) Icons.Filled.MusicNote else Icons.Filled.PlayArrow, contentDescription = null)
                        Text(if (playerState.isPlaying) "Playing" else "Paused", fontWeight = FontWeight.Bold)
                    }
                }
            }

            Surface(
                onClick = onOpenDownloads,
                shape = RoundedCornerShape(20.dp),
                color = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                modifier = Modifier
                    .width(280.dp)
                    .fillMaxHeight(0.85f),
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text("DOWNLOADS", fontSize = 28.sp, fontWeight = FontWeight.Black, letterSpacing = (-1).sp)
                    Icon(Icons.Filled.Download, contentDescription = null, modifier = Modifier.size(64.dp))
                    Text("Offline audio library & downloads manager", fontSize = 14.sp)
                }
            }

            Surface(
                onClick = onOpenPlaylists,
                shape = RoundedCornerShape(20.dp),
                color = MaterialTheme.colorScheme.tertiaryContainer,
                contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
                modifier = Modifier
                    .width(280.dp)
                    .fillMaxHeight(0.85f),
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text("PLAYLISTS", fontSize = 28.sp, fontWeight = FontWeight.Black, letterSpacing = (-1).sp)
                    Icon(Icons.AutoMirrored.Filled.PlaylistPlay, contentDescription = null, modifier = Modifier.size(64.dp))
                    Text("Smart mixes, AI playlists & favorites", fontSize = 14.sp)
                }
            }

            Surface(
                onClick = onOpenSettings,
                shape = RoundedCornerShape(20.dp),
                color = MaterialTheme.colorScheme.surfaceContainerHighest,
                contentColor = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .width(280.dp)
                    .fillMaxHeight(0.85f),
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text("SETTINGS", fontSize = 28.sp, fontWeight = FontWeight.Black, letterSpacing = (-1).sp)
                    Icon(Icons.Filled.Settings, contentDescription = null, modifier = Modifier.size(64.dp))
                    Text("DSP, EQ, 3D Spatial & System config", fontSize = 14.sp)
                }
            }
        }
    }
}
