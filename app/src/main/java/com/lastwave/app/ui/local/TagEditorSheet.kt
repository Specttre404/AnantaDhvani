package com.lastwave.app.ui.local

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.lastwave.app.playback.PlayableTrack

@Composable
fun TagEditorSheet(
    track: PlayableTrack,
    onSave: (title: String, artist: String, album: String) -> Unit,
    onDismiss: () -> Unit,
) {
    var title by remember { mutableStateOf(track.title) }
    var artist by remember { mutableStateOf(track.artist) }
    var album by remember { mutableStateOf(track.album.orEmpty()) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text("Edit ID3 / Metadata Tags", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Track Title") },
            modifier = Modifier.fillMaxWidth(),
        )

        OutlinedTextField(
            value = artist,
            onValueChange = { artist = it },
            label = { Text("Artist Name") },
            modifier = Modifier.fillMaxWidth(),
        )

        OutlinedTextField(
            value = album,
            onValueChange = { album = it },
            label = { Text("Album Title") },
            modifier = Modifier.fillMaxWidth(),
        )

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
            OutlinedButton(onClick = onDismiss, modifier = Modifier.weight(1f)) {
                Text("Cancel")
            }
            Button(onClick = { onSave(title, artist, album) }, modifier = Modifier.weight(1f)) {
                Text("Save")
            }
        }
    }
}
