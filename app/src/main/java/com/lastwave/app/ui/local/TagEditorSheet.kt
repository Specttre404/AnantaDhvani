package com.lastwave.app.ui.local

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.lastwave.app.playback.PlayableTrack

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TagEditorSheet(
    track: PlayableTrack,
    onSave: (title: String, artist: String, album: String) -> Unit,
    onDismiss: () -> Unit,
) {
    var title by remember { mutableStateOf(track.title) }
    var artist by remember { mutableStateOf(track.artist) }
    var album by remember { mutableStateOf(track.album.orEmpty()) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF0B0F1A),
        scrimColor = Color.Black.copy(alpha = 0.65f),
    ) {
        Surface(
            color = Color(0xFF0B0F1A),
            modifier = Modifier.fillMaxWidth(),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Text(
                    text = "Edit ID3 / Metadata Tags",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.ExtraBold,
                        brush = Brush.linearGradient(
                            colors = listOf(Color(0xFF00F0FF), Color(0xFFFF2E93), Color(0xFFFFC700)),
                        ),
                    ),
                )

                val textFieldColors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF00F0FF),
                    unfocusedBorderColor = Color(0xFF26304A),
                    focusedLabelColor = Color(0xFF00F0FF),
                    unfocusedLabelColor = Color(0xFF94A3B8),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                )

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Track Title") },
                    colors = textFieldColors,
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                )

                OutlinedTextField(
                    value = artist,
                    onValueChange = { artist = it },
                    label = { Text("Artist Name") },
                    colors = textFieldColors,
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                )

                OutlinedTextField(
                    value = album,
                    onValueChange = { album = it },
                    label = { Text("Album Title") },
                    colors = textFieldColors,
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                )

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                    OutlinedButton(onClick = onDismiss, modifier = Modifier.weight(1f)) {
                        Text("Cancel", color = Color(0xFF94A3B8))
                    }
                    Button(onClick = { onSave(title, artist, album) }, modifier = Modifier.weight(1f)) {
                        Text("Save", fontWeight = FontWeight.Bold, color = Color.Black)
                    }
                }
            }
        }
    }
}
