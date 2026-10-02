package com.lastwave.app.data.p2p

import com.lastwave.app.playback.PlayableTrack
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

data class SharedSessionState(
    val isHosting: Boolean = false,
    val isConnected: Boolean = false,
    val hostIp: String? = null,
    val currentTrack: PlayableTrack? = null,
    val positionMs: Long = 0L,
    val queue: List<PlayableTrack> = emptyList(),
)

@Singleton
class ListenAlongManager @Inject constructor() {
    private val _sessionState = MutableStateFlow(SharedSessionState())
    val sessionState: StateFlow<SharedSessionState> = _sessionState.asStateFlow()

    fun startHosting(currentTrack: PlayableTrack?, queue: List<PlayableTrack>, positionMs: Long) {
        _sessionState.value = SharedSessionState(
            isHosting = true,
            isConnected = true,
            hostIp = "192.168.1.100",
            currentTrack = currentTrack,
            positionMs = positionMs,
            queue = queue,
        )
    }

    fun stopHosting() {
        _sessionState.value = SharedSessionState()
    }

    fun joinSession(hostIp: String) {
        _sessionState.value = SharedSessionState(
            isHosting = false,
            isConnected = true,
            hostIp = hostIp,
        )
    }
}
