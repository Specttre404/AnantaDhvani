package com.lastwave.app.ui.player

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.lastwave.app.playback.PlaybackChromeState

@Composable
fun MiniPlayer(
    playbackState: PlaybackChromeState,
    onExpand: () -> Unit,
    onPlayPause: () -> Unit,
    onNext: () -> Unit,
    onPrevious: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    ExpressiveMiniPlayer(
        playbackState = playbackState,
        onExpand = onExpand,
        onPlayPause = onPlayPause,
        onNext = onNext,
        onPrevious = onPrevious,
        enableVinylRotation = true,
        modifier = modifier,
    )
}
