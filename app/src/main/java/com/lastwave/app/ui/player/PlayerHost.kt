@file:OptIn(androidx.compose.foundation.layout.ExperimentalLayoutApi::class)

package com.lastwave.app.ui.player

import android.content.Context
import android.content.res.Configuration
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.ui.platform.LocalConfiguration
import com.lastwave.app.playback.haptics.BassHapticDriver
import com.lastwave.app.ui.player.spatial.SpatialPanningPuckView
import com.lastwave.app.ui.visualizer.OrbitalParticleSphereView
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.SystemClock
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.lastwave.app.ui.common.PredictiveBackScreen
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.luminance
import androidx.compose.runtime.withFrameNanos
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.ui.graphics.Shape
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import com.lastwave.app.ui.common.adaptiveContentWidth
import com.lastwave.app.ui.common.isTabletOrWideScreen
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ClearAll
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.DragHandle
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.FastRewind
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.HighQuality
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.PlaylistAdd
import androidx.compose.material.icons.filled.QueueMusic
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.RepeatOne
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.withContext
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.zIndex
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import androidx.palette.graphics.Palette
import coil.imageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import androidx.media3.common.Player
import com.lastwave.app.data.generate.GeneratedTrack
import com.lastwave.app.data.lyrics.LyricsRepository
import com.lastwave.app.data.lyrics.LyricsResult
import com.lastwave.app.data.playlist.PlaylistRepository
import com.lastwave.app.data.playlist.SavedPlaylist
import com.lastwave.app.data.playlist.LIKED_SONGS_MODE
import com.lastwave.app.data.local.LyricsAnimation
import com.lastwave.app.data.local.LyricsUiVersion
import com.lastwave.app.playback.MusicPlayer
import com.lastwave.app.playback.MusicPlayerState
import com.lastwave.app.playback.PlaybackChromeState
import com.lastwave.app.playback.PlaybackProgressState
import com.lastwave.app.playback.PlayableTrack
import kotlinx.coroutines.isActive
import com.lastwave.app.ui.common.ArtworkImage
import com.lastwave.app.ui.common.ExpressiveInlineLoadingIndicator
import com.lastwave.app.ui.common.ExpressiveMotion
import com.lastwave.app.ui.common.PlaylistCover
import com.lastwave.app.ui.common.TrackContextMenuSheet
import com.lastwave.app.ui.common.TrackMenuCapabilities
import com.lastwave.app.ui.common.TrackMenuTarget
import com.lastwave.app.ui.common.expressivePressScale
import com.lastwave.app.ui.theme.liquidGlassSurface
import com.lastwave.app.ui.theme.LocalLiquidGlass
import com.lastwave.app.ui.theme.LiquidGlassSurface
import com.lastwave.app.ui.theme.liquidGlassChrome
import com.lastwave.app.ui.theme.liquidGlassContainerColor
import com.lastwave.app.ui.theme.liquidGlassSource
import com.lastwave.app.ui.theme.isLiquidGlassBackdropSupported
import com.lastwave.app.ui.theme.LocalLiquidGlassBackdrop
import com.lastwave.app.ui.theme.LocalLiquidGlassOverlayBackdrop
import com.lastwave.app.ui.theme.LiquidGlassPreset
import com.lastwave.app.ui.theme.BackdropBlur
import com.kyant.backdrop.Backdrop
import com.kyant.backdrop.backdrops.rememberLayerBackdrop
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.math.abs

enum class FullPlayerTab {
    NOW_PLAYING,
    LYRICS,
    QUEUE
}

val LocalMusicPlayer = staticCompositionLocalOf<MusicPlayer> {
    error("MusicPlayer is only available inside PlayerHost")
}

val LocalAddToPlaylist = staticCompositionLocalOf<(PlayableTrack) -> Unit> {
    error("Add-to-playlist is only available inside PlayerHost")
}

/**
 * Extra list-end clearance needed while the collapsed player overlays a
 * screen. The Material 3 bar is about 86dp tall; 88dp clears it without
 * leaving a large empty band at the end of short lists.
 */
val LocalMiniPlayerScrollClearance = staticCompositionLocalOf { 0.dp }

@HiltViewModel
class PlayerViewModel @Inject constructor(
    val player: MusicPlayer,
    private val playlistRepository: PlaylistRepository,
    private val lyricsRepository: LyricsRepository,
    private val settingsPreferences: com.lastwave.app.data.local.SettingsPreferences,
    val navigator: com.lastwave.app.ui.navigation.ArtistAlbumNavigator,
    val genreExplorer: com.lastwave.app.ui.genres.GenreExplorer,
    val mixLauncher: com.lastwave.app.ui.generate.MixLauncher,
    private val ytMusicLibraryManager: com.lastwave.app.data.ytmusic.YtMusicLibraryManager,
    private val likedSongsManager: com.lastwave.app.data.playlist.LikedSongsManager,
    private val scrobbleRepository: com.lastwave.app.data.repository.ScrobbleRepository,
) : ViewModel() {
    val navEvents = navigator.events
    val state = player.state
    val chromeState = player.chromeState
    val progressState = player.progressState
    val fullPlayerState = player.state
        .map { it.copy(positionMs = 0L, bufferedPositionMs = 0L) }
        .distinctUntilChanged()
        .stateIn(viewModelScope, SharingStarted.Eagerly, player.state.value.copy(positionMs = 0L, bufferedPositionMs = 0L))
    val settings: StateFlow<com.lastwave.app.data.local.MiscSettings> = settingsPreferences.settings
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), com.lastwave.app.data.local.MiscSettings())
    private val _customPlaylists = MutableStateFlow<List<SavedPlaylist>>(emptyList())
    val customPlaylists = _customPlaylists.asStateFlow()
    private var customPlaylistsLoaded = false
    val likedTrackKeys = likedSongsManager.likedTrackKeys

    fun openArtist(name: String, browseId: String? = null) {
        navigator.openArtist(name, browseId)
    }

    fun openAlbum(title: String, artist: String = "", browseId: String? = null) {
        navigator.openAlbum(title, artist, browseId)
    }

    fun toggleLyricsTranslation() {
        viewModelScope.launch {
            settingsPreferences.setShowLyricsTranslation(!settings.value.showLyricsTranslation)
        }
    }

    fun toggleLyricsPhonetic() {
        viewModelScope.launch {
            settingsPreferences.setShowLyricsPhonetic(!settings.value.showLyricsPhonetic)
        }
    }

    private val _lyricsState = MutableStateFlow<LyricsUiState>(LyricsUiState.Idle)
    val lyricsState = _lyricsState.asStateFlow()

    private var currentTrackLyricsKey: String? = null
    private var lyricsJob: Job? = null

    init {
        viewModelScope.launch {
            playlistRepository.changes.collect {
                if (customPlaylistsLoaded) refreshCustomPlaylists()
            }
        }
        viewModelScope.launch {
            ytMusicLibraryManager.playlists.collect {
                if (customPlaylistsLoaded) refreshCustomPlaylists()
            }
        }
        viewModelScope.launch {
            combine(
                player.chromeState.map { it.current }.distinctUntilChanged(),
                settingsPreferences.settings.map { it.wordByWordLyrics }.distinctUntilChanged(),
            ) { track, wordByWord -> track to wordByWord }.collect { (track, wordByWord) ->
                val key = track?.let { "${it.artist}|${it.title}|$wordByWord" }
                if (key != currentTrackLyricsKey) {
                    currentTrackLyricsKey = key
                    if (track != null) {
                        loadLyrics(track, forceRefresh = false)
                    } else {
                        lyricsJob?.cancel()
                        _lyricsState.value = LyricsUiState.Idle
                    }
                }
            }
        }
    }

    private suspend fun refreshCustomPlaylists() {
        try {
            _customPlaylists.value = (playlistRepository.getAll()
                .filter { it.mode == "custom" || it.mode == LIKED_SONGS_MODE }
                .sortedByDescending { it.mode == LIKED_SONGS_MODE } + ytMusicLibraryManager.playlists.value)
                .distinctBy { it.id }
        } catch (error: CancellationException) {
            throw error
        } catch (error: Exception) {
            customPlaylistsLoaded = false
            android.util.Log.e("PlayerViewModel", "Couldn't refresh playlists", error)
        }
    }

    fun prepareCustomPlaylists() {
        if (customPlaylistsLoaded) return
        customPlaylistsLoaded = true
        viewModelScope.launch { refreshCustomPlaylists() }
    }

    fun loadLyrics(track: PlayableTrack, forceRefresh: Boolean = false) {
        lyricsJob?.cancel()
        lyricsJob = viewModelScope.launch {
            _lyricsState.value = LyricsUiState.Loading
            val durationSeconds = if (player.state.value.durationMs > 0) {
                (player.state.value.durationMs / 1000).toInt()
            } else null

            val result = try {
                lyricsRepository.getLyrics(
                    track.title, track.artist, track.album, durationSeconds, forceRefresh,
                    wordByWord = settingsPreferences.settings.first().wordByWordLyrics,
                    onPartialResult = { partial ->
                        withContext(Dispatchers.Main.immediate) {
                            coroutineContext.ensureActive()
                            publishLyrics(partial)
                        }
                    },
                )
            } catch (error: CancellationException) {
                throw error
            } catch (error: Exception) {
                LyricsResult.Error(error.message ?: "Couldn't load lyrics")
            }
            coroutineContext.ensureActive()
            if (result is LyricsResult.Success || _lyricsState.value !is LyricsUiState.Success) {
                publishLyrics(result)
            }
        }
    }

    private fun publishLyrics(result: LyricsResult) {
        when (result) {
            is LyricsResult.Success -> {
                _lyricsState.value = LyricsUiState.Success(
                    lines = result.lines,
                    isSynced = result.isSynced,
                    isWordSynced = result.isWordSynced,
                    plainLyrics = result.plainLyrics,
                    isInstrumental = result.isInstrumental,
                    source = result.source,
                )
            }
            is LyricsResult.Empty -> {
                _lyricsState.value = LyricsUiState.Empty
            }
            is LyricsResult.Error -> {
                _lyricsState.value = LyricsUiState.Error(result.message)
            }
        }
    }

    fun retryLyrics() {
        val track = player.state.value.current ?: return
        loadLyrics(track, forceRefresh = true)
    }

    fun addToPlaylists(
        playlistIds: Set<Long>,
        duplicatePlaylistIds: Set<Long>,
        track: PlayableTrack,
    ) {
        if (playlistIds.isEmpty()) return
        viewModelScope.launch {
            runCatching {
                val generatedTrack = track.toGeneratedTrack()
                var remoteChanged = false
                playlistIds.forEach { playlistId ->
                    val allowDuplicate = playlistId in duplicatePlaylistIds
                    if (playlistId < 0L) {
                        val added = ytMusicLibraryManager.addTrack(playlistId, generatedTrack, allowDuplicate)
                        remoteChanged = remoteChanged || added
                    } else {
                        playlistRepository.addTrack(
                            id = playlistId,
                            track = generatedTrack,
                            allowDuplicate = allowDuplicate,
                        )
                    }
                }
                if (remoteChanged) {
                    ytMusicLibraryManager.refresh()
                    refreshCustomPlaylists()
                }
            }
        }
    }

    suspend fun findDuplicatePlaylistIds(playlistIds: Set<Long>, track: PlayableTrack): Set<Long> {
        val generatedTrack = track.toGeneratedTrack()
        val localDuplicates = _customPlaylists.value
            .filter { playlist ->
                playlist.id in playlistIds &&
                    playlist.remotePlaylistId == null &&
                    playlist.tracks.any {
                        it.key == generatedTrack.key ||
                            (it.name.equals(generatedTrack.name, ignoreCase = true) && it.artist.equals(generatedTrack.artist, ignoreCase = true))
                    }
            }
            .mapTo(mutableSetOf(), SavedPlaylist::id)
        val remoteIds = playlistIds.filterTo(mutableSetOf()) { it < 0L }
        return localDuplicates + ytMusicLibraryManager.findDuplicatePlaylistIds(remoteIds, generatedTrack)
    }

    suspend fun findCachedDuplicatePlaylistIds(playlistIds: Set<Long>, track: PlayableTrack): Set<Long> {
        val generatedTrack = track.toGeneratedTrack()
        val remoteIds = playlistIds.filterTo(mutableSetOf()) { it < 0L }
        return ytMusicLibraryManager.findCachedDuplicatePlaylistIds(remoteIds, generatedTrack)
    }

    fun createPlaylistAndAdd(title: String, track: PlayableTrack) {
        if (title.isBlank()) return
        viewModelScope.launch {
            runCatching {
                val playlist = playlistRepository.createCustom(title)
                playlistRepository.addTrack(playlist.id, track.toGeneratedTrack())
            }
        }
    }

    fun toggleLiked(track: PlayableTrack) {
        viewModelScope.launch {
            val loved = likedSongsManager.toggle(track.toGeneratedTrack())
            mirrorLastFmLove(track, loved)
        }
    }

    fun like(track: PlayableTrack) {
        viewModelScope.launch {
            likedSongsManager.like(track.toGeneratedTrack())
            mirrorLastFmLove(track, loved = true)
        }
    }

    private suspend fun mirrorLastFmLove(track: PlayableTrack, loved: Boolean) {
        when (val result = scrobbleRepository.setTrackLoved(track.artist, track.title, loved)) {
            com.lastwave.app.data.repository.ScrobbleRepository.Result.Success,
            com.lastwave.app.data.repository.ScrobbleRepository.Result.NoSessionKey -> Unit
            is com.lastwave.app.data.repository.ScrobbleRepository.Result.Failed ->
                android.util.Log.w("PlayerViewModel", "Last.fm love sync failed: ${result.message}")
        }
    }
}

/** App-wide collapsed + maximized player layered over every navigation route. */
@Composable
fun PlayerHost(
    viewModel: PlayerViewModel = hiltViewModel(),
    hasBottomNavigation: Boolean = false,
    content: @Composable () -> Unit,
) {
    val state by viewModel.chromeState.collectAsStateWithLifecycle()
    var expanded by rememberSaveable { mutableStateOf(false) }
    var currentTab by rememberSaveable { mutableStateOf(FullPlayerTab.NOW_PLAYING) }
    var playlistTrack by remember { mutableStateOf<PlayableTrack?>(null) }
    val requestAddToPlaylist = remember(viewModel) {
        { track: PlayableTrack ->
            viewModel.prepareCustomPlaylists()
            playlistTrack = track
        }
    }
    val trackKey = state.current?.let { it.videoId ?: "${it.artist}|${it.title}" }
    LaunchedEffect(trackKey) {
        if (trackKey == null) {
            expanded = false
            currentTab = FullPlayerTab.NOW_PLAYING
        }
    }
    LaunchedEffect(expanded) {
        if (!expanded) {
            currentTab = FullPlayerTab.NOW_PLAYING
        }
    }
    LaunchedEffect(Unit) {
        viewModel.navEvents.collect {
            expanded = false
            currentTab = FullPlayerTab.NOW_PLAYING
        }
    }
    LaunchedEffect(Unit) {
        viewModel.mixLauncher.requests.collect {
            expanded = false
            currentTab = FullPlayerTab.NOW_PLAYING
        }
    }
    LaunchedEffect(Unit) {
        viewModel.genreExplorer.pendingGenre.collect { genre ->
            if (genre != null) {
                expanded = false
                currentTab = FullPlayerTab.NOW_PLAYING
            }
        }
    }
    val miniPlayerVisible = state.current != null && !expanded

    CompositionLocalProvider(
        LocalMusicPlayer provides viewModel.player,
        LocalAddToPlaylist provides requestAddToPlaylist,
        LocalMiniPlayerScrollClearance provides if (state.current != null) 88.dp else 0.dp,
    ) {
        Box(Modifier.fillMaxSize()) {
            content()
            if (miniPlayerVisible) {
                MiniPlayer(
                    state = state,
                    progressState = viewModel.progressState,
                    onExpand = { expanded = true },
                    onToggle = viewModel.player::togglePlayPause,
                    onPrevious = viewModel.player::previous,
                    onNext = viewModel.player::next,
                    onClose = viewModel.player::stopAndClear,
                    bottomPadding = if (hasBottomNavigation) 92.dp else 12.dp,
                    edgeToEdge = !hasBottomNavigation,
                    backdrop = null,
                    modifier = Modifier.align(Alignment.BottomCenter),
                )
            }
            AnimatedVisibility(
                visible = expanded && state.current != null,
                enter = slideInVertically(
                    animationSpec = ExpressiveMotion.smoothSpring(),
                    initialOffsetY = { it },
                ) + fadeIn(tween(180)),
                exit = slideOutVertically(
                    animationSpec = ExpressiveMotion.smoothSpring(),
                    targetOffsetY = { it },
                ) + fadeOut(tween(150)),
            ) {
                PredictiveBackScreen(
                    enabled = expanded && state.current != null,
                    onBack = {
                        if (currentTab != FullPlayerTab.NOW_PLAYING) {
                            currentTab = FullPlayerTab.NOW_PLAYING
                        } else {
                            expanded = false
                        }
                    },
                ) {
                    ExpandedPlayer(
                        viewModel = viewModel,
                        currentTab = currentTab,
                        onTabChange = { currentTab = it },
                        onRetryLyrics = viewModel::retryLyrics,
                        onCollapse = { expanded = false },
                        onOpenArtist = { artist ->
                            expanded = false
                            viewModel.openArtist(artist)
                        },
                    )
                }
            }
            playlistTrack?.let { track ->
                AddToPlaylistDialogHost(
                    viewModel = viewModel,
                    track = track,
                    onDismiss = { playlistTrack = null },
                    onAdd = { playlistIds, duplicatePlaylistIds ->
                        viewModel.addToPlaylists(playlistIds, duplicatePlaylistIds, track)
                        playlistTrack = null
                    },
                    onFindDuplicates = { playlistIds ->
                        viewModel.findDuplicatePlaylistIds(playlistIds, track)
                    },
                    onFindCachedDuplicates = { playlistIds ->
                        viewModel.findCachedDuplicatePlaylistIds(playlistIds, track)
                    },
                    onCreate = { title ->
                        viewModel.createPlaylistAndAdd(title, track)
                        playlistTrack = null
                    },
                )
            }
        }
    }
}

@Composable
private fun ExpandedPlayer(
    viewModel: PlayerViewModel,
    currentTab: FullPlayerTab,
    onTabChange: (FullPlayerTab) -> Unit,
    onRetryLyrics: () -> Unit,
    onCollapse: () -> Unit,
    onOpenArtist: (String) -> Unit,
) {
    val state by viewModel.fullPlayerState.collectAsStateWithLifecycle()
    val lyricsState by viewModel.lyricsState.collectAsStateWithLifecycle()
    val settings by viewModel.settings.collectAsStateWithLifecycle()
    val likedTrackKeys by viewModel.likedTrackKeys.collectAsStateWithLifecycle()
    val currentTrack = state.current
    val isLiked = currentTrack != null &&
        "${currentTrack.title}|${currentTrack.artist}".lowercase() in likedTrackKeys
    FullPlayer(
        state = state,
        progressState = viewModel.progressState,
        player = viewModel.player,
        lyricsState = lyricsState,
        lyricsUiVersion = settings.lyricsUiVersion,
        lyricsAnimation = settings.lyricsAnimation,
        wavySeekbarEnabled = settings.wavySeekbarEnabled,
        seekbarStyle = settings.seekbarStyle,
        playerCoverStyle = settings.playerCoverStyle,
        playerStyle = settings.playerStyle,
        playerBackgroundStyle = settings.playerBackgroundStyle,
        showLyricsTranslation = settings.showLyricsTranslation,
        showLyricsPhonetic = settings.showLyricsPhonetic,
        onToggleTranslation = viewModel::toggleLyricsTranslation,
        onTogglePhonetic = viewModel::toggleLyricsPhonetic,
        currentTab = currentTab,
        onTabChange = onTabChange,
        onRetryLyrics = onRetryLyrics,
        onCollapse = onCollapse,
        onOpenArtist = onOpenArtist,
        isLiked = isLiked,
        onToggleLiked = { currentTrack?.let(viewModel::toggleLiked) },
        onDoubleTapLike = { currentTrack?.let(viewModel::like) },
    )
}

@Composable
private fun AddToPlaylistDialogHost(
    viewModel: PlayerViewModel,
    track: PlayableTrack,
    onDismiss: () -> Unit,
    onAdd: (Set<Long>, Set<Long>) -> Unit,
    onFindDuplicates: suspend (Set<Long>) -> Set<Long>,
    onFindCachedDuplicates: suspend (Set<Long>) -> Set<Long>,
    onCreate: (String) -> Unit,
) {
    val playlists by viewModel.customPlaylists.collectAsStateWithLifecycle()
    AddToPlaylistDialog(
        track = track,
        playlists = playlists,
        onDismiss = onDismiss,
        onAdd = onAdd,
        onFindDuplicates = onFindDuplicates,
        onFindCachedDuplicates = onFindCachedDuplicates,
        onCreate = onCreate,
    )
}

@Composable
internal fun AnimatedPlayPauseIcon(isPlaying: Boolean, modifier: Modifier = Modifier, tint: Color = androidx.compose.material3.LocalContentColor.current) {
    AnimatedContent(
        targetState = isPlaying,
        transitionSpec = {
            (fadeIn(tween(ExpressiveMotion.Quick)) +
                scaleIn(ExpressiveMotion.spatialSpring(), initialScale = 0.7f)) togetherWith
                (fadeOut(tween(ExpressiveMotion.Quick)) +
                    scaleOut(tween(ExpressiveMotion.Quick), targetScale = 0.7f))
        },
        label = "playPauseIcon",
        modifier = modifier,
    ) { playing ->
        Icon(
            if (playing) Icons.Filled.Pause else Icons.Filled.PlayArrow,
            contentDescription = "Play or pause",
            tint = tint,
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Composable
private fun MiniPlayer(
    state: PlaybackChromeState,
    progressState: StateFlow<PlaybackProgressState>,
    onExpand: () -> Unit,
    onToggle: () -> Unit,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
    onClose: () -> Unit,
    bottomPadding: androidx.compose.ui.unit.Dp,
    edgeToEdge: Boolean,
    backdrop: Backdrop? = LocalLiquidGlassBackdrop.current,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val track = state.current ?: return
    // Liquid Glass dressing for the floating mini player (no-op when the
    // experimental setting is off — see ui/theme/LiquidGlass.kt).
    val liquidGlass = LocalLiquidGlass.current
    var dragX by remember(track.videoId, track.title) { mutableFloatStateOf(0f) }
    var dragY by remember(track.videoId, track.title) { mutableFloatStateOf(0f) }
    val shownX by animateFloatAsState(dragX, ExpressiveMotion.spatialSpring(), label = "miniPlayerX")
    val shownY by animateFloatAsState(dragY, ExpressiveMotion.spatialSpring(), label = "miniPlayerY")
    val threshold = with(LocalDensity.current) { 72.dp.toPx() }
    val isTablet = isTabletOrWideScreen()
    val shape = if (edgeToEdge && !isTablet) {
        RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
    } else {
        RoundedCornerShape(32.dp)
    }
    val positionedModifier = if (edgeToEdge && !isTablet) {
        modifier.fillMaxWidth()
    } else {
        modifier
            .windowInsetsPadding(
                WindowInsets.safeDrawing.only(WindowInsetsSides.Bottom + WindowInsetsSides.Horizontal),
            )
            .padding(horizontal = 14.dp)
            .padding(bottom = if (isTablet && edgeToEdge) 14.dp else bottomPadding)
            .widthIn(max = 680.dp)
            .fillMaxWidth()
    }
    Box(
        modifier = positionedModifier
            .graphicsLayer {
                translationX = shownX
                translationY = shownY.coerceAtLeast(0f)
                alpha = (1f - (abs(shownX) + shownY.coerceAtLeast(0f)) / (threshold * 4f)).coerceIn(0.55f, 1f)
            }
            .pointerInput(track.videoId, track.title) {
                detectDragGestures(
                    onDragCancel = { dragX = 0f; dragY = 0f },
                    onDragEnd = {
                        when {
                            dragY > threshold -> onClose()
                            dragY < -threshold -> onExpand()
                            dragX < -threshold -> onNext()
                            dragX > threshold -> onPrevious()
                        }
                        dragX = 0f
                        dragY = 0f
                    },
                ) { change, amount ->
                    change.consume()
                    if (abs(dragX + amount.x) > abs(dragY + amount.y)) dragX += amount.x
                    else dragY += amount.y
                }
            }
            .clickable(onClick = onExpand),
        contentAlignment = Alignment.Center,
    ) {
        Surface(
            shape = shape,
            color = MaterialTheme.colorScheme.surfaceContainerHigh.copy(alpha = if (liquidGlass) 0.80f else 1f),
            tonalElevation = if (edgeToEdge || liquidGlass) 0.dp else 6.dp,
            shadowElevation = if (edgeToEdge || liquidGlass) 0.dp else 12.dp,
            modifier = Modifier.fillMaxWidth().liquidGlassChrome(shape, liquidGlass, LiquidGlassPreset.MiniPlayer, backdrop),
        ) {
            Column(
                modifier = if (edgeToEdge) {
                    Modifier.windowInsetsPadding(
                        WindowInsets.safeDrawing.only(WindowInsetsSides.Bottom + WindowInsetsSides.Horizontal),
                    )
                } else {
                    Modifier
                },
            ) {
                Row(
                    Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(Modifier.size(56.dp)) {
                        Surface(
                            shape = RoundedCornerShape(18.dp),
                            color = MaterialTheme.colorScheme.surfaceContainerHighest,
                            tonalElevation = 2.dp,
                            modifier = Modifier.fillMaxSize(),
                        ) {
                            PlayerArtwork(track, Modifier.fillMaxSize(), 18.dp)
                        }
                    }
                    Column(Modifier.weight(1f).padding(horizontal = 14.dp)) {
                        Text(
                            track.title,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                        Text(
                            track.artist,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                    Surface(
                        onClick = onToggle,
                        shape = CircleShape,
                        color = if (liquidGlass) MaterialTheme.colorScheme.primary.copy(alpha = 0.22f)
                            else MaterialTheme.colorScheme.primary,
                        contentColor = if (liquidGlass) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(48.dp),
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            if (state.isBuffering) {
                                ExpressiveInlineLoadingIndicator(
                                    size = 24.dp,
                                    color = androidx.compose.material3.LocalContentColor.current,
                                    strokeWidth = 2.5.dp,
                                )
                            } else {
                                AnimatedPlayPauseIcon(state.isPlaying, Modifier.size(27.dp))
                            }
                        }
                    }
                    IconButton(
                        onClick = onNext,
                        enabled = state.queueSize > 1,
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.secondaryContainer),
                    ) {
                        Icon(
                            Icons.Filled.SkipNext,
                            "Next",
                            tint = if (state.queueSize > 1) MaterialTheme.colorScheme.onSecondaryContainer
                            else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
                        )
                    }
                }
                MiniPlayerProgress(progressState)
            }
        }
    }
}

@Composable
private fun MiniPlayerProgress(progressState: StateFlow<PlaybackProgressState>) {
    val state by progressState.collectAsStateWithLifecycle()
    val progress = if (state.durationMs > 0) state.positionMs.toFloat() / state.durationMs else 0f
    Box(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(3.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surfaceVariant),
    ) {
        // Scale an already measured layer instead of changing its width and
        // forcing the mini-player through measure/layout on every ticker tick.
        Box(
            Modifier
                .fillMaxSize()
                .graphicsLayer {
                    scaleX = progress.coerceIn(0f, 1f)
                    transformOrigin = TransformOrigin(0f, 0.5f)
                }
                .background(MaterialTheme.colorScheme.primary),
        )
    }
}

@Composable
fun PlayingWaveBars(
    modifier: Modifier = Modifier,
    waveColor: Color = MaterialTheme.colorScheme.onPrimaryContainer,
    containerColor: Color = MaterialTheme.colorScheme.primaryContainer,
    shape: Shape = CircleShape,
) {
    val transition = rememberInfiniteTransition(label = "miniArtworkWave")
    val first = transition.animateFloat(
        initialValue = 0.25f,
        targetValue = 0.95f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 480, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "miniWaveFirst",
    )
    val second = transition.animateFloat(
        initialValue = 0.95f,
        targetValue = 0.30f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 640, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "miniWaveSecond",
    )
    val third = transition.animateFloat(
        initialValue = 0.35f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 530, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "miniWaveThird",
    )

    val content: @Composable () -> Unit = {
        // Fixed small badge content so it can never expand to fill the cover.
        // Outer Box is 26x22 (16+5+5, 12+5+5) — always tiny, bottom-end aligned by caller.
        androidx.compose.foundation.layout.Box(
            modifier = Modifier
                .padding(horizontal = 5.dp, vertical = 5.dp)
                .size(16.dp, 12.dp),
            contentAlignment = Alignment.Center,
        ) {
            androidx.compose.foundation.Canvas(
                Modifier.fillMaxSize(),
            ) {
            if (size.width <= 0f || size.height <= 0f) return@Canvas
            val barCount = 3
            val barWidth = (size.width / 5.2f).coerceAtLeast(1.5f)
            val barGap = barWidth * 0.9f
            val totalContentWidth = barCount * barWidth + (barCount - 1) * barGap
            val startX = ((size.width - totalContentWidth) / 2f).coerceAtLeast(0f)

            repeat(barCount) { index ->
                val fraction = when (index) {
                    0 -> first.value
                    1 -> second.value
                    else -> third.value
                }
                val barHeight = (size.height * fraction).coerceIn(minOf(barWidth, size.height), size.height)
                drawRoundRect(
                    color = waveColor,
                    topLeft = androidx.compose.ui.geometry.Offset(
                        x = startX + index * (barWidth + barGap),
                        y = size.height - barHeight,
                    ),
                    size = androidx.compose.ui.geometry.Size(barWidth, barHeight),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(barWidth / 2f, barWidth / 2f),
                )
            }
            }
        }
    }

    if (containerColor == Color.Transparent) {
        Box(
            modifier = modifier.defaultMinSize(minWidth = 24.dp, minHeight = 18.dp),
            contentAlignment = Alignment.Center,
        ) {
            content()
        }
    } else {
        Surface(
            shape = shape,
            color = containerColor,
            modifier = modifier.defaultMinSize(minWidth = 26.dp, minHeight = 22.dp),
        ) {
            content()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddToPlaylistDialog(
    track: PlayableTrack,
    playlists: List<SavedPlaylist>,
    onDismiss: () -> Unit,
    onAdd: (Set<Long>, Set<Long>) -> Unit,
    onFindDuplicates: suspend (Set<Long>) -> Set<Long>,
    onFindCachedDuplicates: suspend (Set<Long>) -> Set<Long>,
    onCreate: (String) -> Unit,
) {
    val sanitizedPlaylists = remember(playlists) { playlists.distinctBy { it.id } }
    var newPlaylistName by remember(track) { mutableStateOf("") }
    var selectedPlaylistIds by remember(track) { mutableStateOf(emptySet<Long>()) }
    var duplicateConfirmation by remember(track) { mutableStateOf<Set<Long>?>(null) }
    var duplicatePlaylistIds by remember(track) { mutableStateOf(emptySet<Long>()) }
    var knownDuplicatePlaylistIds by remember(track) { mutableStateOf(emptySet<Long>()) }
    var isCheckingDuplicates by remember(track) { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    val trackKey = remember(track.title, track.artist) { track.toGeneratedTrack().key }
    val selectedPlaylists = sanitizedPlaylists.filter { it.id in selectedPlaylistIds }
    val playlistListMaxHeight = (LocalConfiguration.current.screenHeightDp.dp - 360.dp)
        .coerceIn(180.dp, 410.dp)

    LaunchedEffect(sanitizedPlaylists.map(SavedPlaylist::id)) {
        selectedPlaylistIds = selectedPlaylistIds.intersect(sanitizedPlaylists.mapTo(mutableSetOf(), SavedPlaylist::id))
    }

    LaunchedEffect(trackKey, sanitizedPlaylists.map(SavedPlaylist::id)) {
        runCatching {
            val remoteIds = sanitizedPlaylists
                .filterTo(mutableListOf()) { it.remotePlaylistId != null }
                .mapTo(mutableSetOf(), SavedPlaylist::id)
            knownDuplicatePlaylistIds = knownDuplicatePlaylistIds + onFindCachedDuplicates(remoteIds)
        }
    }

    fun requestAdd(playlistIds: Set<Long>) {
        if (playlistIds.isEmpty() || isCheckingDuplicates) return
        val immediateDuplicates = playlistIds.filter { id ->
            id in knownDuplicatePlaylistIds ||
                sanitizedPlaylists.firstOrNull { it.id == id }?.tracks?.any {
                    it.key == trackKey ||
                        (it.name.equals(track.title, ignoreCase = true) && it.artist.equals(track.artist, ignoreCase = true))
                } == true
        }.toSet()
        if (immediateDuplicates.isNotEmpty()) {
            duplicatePlaylistIds = emptySet()
            duplicateConfirmation = immediateDuplicates
            return
        }
        scope.launch {
            runCatching {
                isCheckingDuplicates = true
                val duplicates = onFindDuplicates(playlistIds)
                isCheckingDuplicates = false
                knownDuplicatePlaylistIds = (knownDuplicatePlaylistIds - playlistIds) + duplicates
                if (duplicates.isEmpty()) {
                    onAdd(playlistIds, emptySet())
                } else {
                    duplicatePlaylistIds = emptySet()
                    duplicateConfirmation = duplicates
                }
            }.onFailure {
                isCheckingDuplicates = false
                onAdd(playlistIds, emptySet())
            }
        }
    }

    duplicateConfirmation?.let { duplicates ->
        val duplicatePlaylists = sanitizedPlaylists.filter { it.id in duplicates }
        if (duplicatePlaylists.size == 1) {
            val playlist = duplicatePlaylists.first()
            AlertDialog(
                onDismissRequest = { duplicateConfirmation = null },
                title = { Text("Song already in playlist") },
                text = {
                    Text(
                        "${track.title} by ${track.artist} is already present in ${playlist.title}. " +
                            "You can leave the playlist unchanged or add another copy.",
                        style = MaterialTheme.typography.bodyMedium,
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            onAdd(selectedPlaylistIds, setOf(playlist.id))
                            duplicateConfirmation = null
                        },
                    ) {
                        Text("Add anyway")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { duplicateConfirmation = null }) {
                        Text("Leave unchanged")
                    }
                },
            )
            return
        }

        val missingCount = selectedPlaylistIds.size - duplicates.size
        AlertDialog(
            onDismissRequest = { duplicateConfirmation = null },
            title = {
                Text("${duplicates.size} playlists already have this song")
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        "Existing copies stay unchanged. Select only the playlists where you want another copy.",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth().heightIn(max = 260.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                    ) {
                        itemsIndexed(
                            duplicatePlaylists,
                            key = { index, playlist -> "${playlist.id}_${playlist.remotePlaylistId ?: ""}_$index" },
                        ) { _, playlist ->
                            val addAgain = playlist.id in duplicatePlaylistIds
                            val canAddAnother = playlist.mode != LIKED_SONGS_MODE
                            Surface(
                                onClick = {
                                    duplicatePlaylistIds = if (addAgain) {
                                        duplicatePlaylistIds - playlist.id
                                    } else {
                                        duplicatePlaylistIds + playlist.id
                                    }
                                },
                                enabled = canAddAnother,
                                shape = RoundedCornerShape(14.dp),
                                color = if (addAgain) {
                                    MaterialTheme.colorScheme.secondaryContainer
                                } else {
                                    MaterialTheme.colorScheme.surfaceContainerHigh
                                },
                                modifier = Modifier.fillMaxWidth(),
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    Column(Modifier.weight(1f)) {
                                        Text(
                                            playlist.title,
                                            style = MaterialTheme.typography.bodyLarge,
                                            fontWeight = FontWeight.SemiBold,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis,
                                        )
                                        Text(
                                            if (canAddAnother) "Add another copy" else "Already liked",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        )
                                    }
                                    Checkbox(checked = addAgain, onCheckedChange = null, enabled = canAddAnother)
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onAdd(selectedPlaylistIds, duplicatePlaylistIds)
                        duplicateConfirmation = null
                    },
                ) {
                    Text(
                        when {
                            duplicatePlaylistIds.isNotEmpty() -> "Add anyway"
                            missingCount > 0 -> "Add missing"
                            else -> "Leave unchanged"
                        },
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { duplicateConfirmation = null }) {
                    Text("Leave unchanged")
                }
            },
        )
        return
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        modifier = Modifier.liquidGlassChrome(
            androidx.compose.material3.BottomSheetDefaults.ExpandedShape,
            LocalLiquidGlass.current,
            LiquidGlassPreset.ModalSheet,
            LocalLiquidGlassOverlayBackdrop.current,
        ),
        containerColor = liquidGlassContainerColor(
            MaterialTheme.colorScheme.surfaceContainer,
            backdrop = LocalLiquidGlassOverlayBackdrop.current,
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .adaptiveContentWidth(maxWidth = 640.dp)
                .align(Alignment.CenterHorizontally)
                .padding(start = 20.dp, end = 20.dp, bottom = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    modifier = Modifier.size(52.dp),
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(Icons.Filled.PlaylistAdd, contentDescription = null, modifier = Modifier.size(27.dp))
                    }
                }
                Column(Modifier.weight(1f).padding(start = 14.dp)) {
                    Text(
                        "Add to playlist",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                    )
                    Text(
                        if (selectedPlaylistIds.isEmpty()) "Choose one or more playlists" else "${selectedPlaylistIds.size} selected",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary,
                    )
                }
            }

            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surfaceContainerHigh,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Column(Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
                    Text(
                        track.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        track.artist,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }

            if (sanitizedPlaylists.isEmpty()) {
                Text(
                    "No playlists yet. Create your first playlist below.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth().heightIn(max = playlistListMaxHeight),
                    verticalArrangement = Arrangement.spacedBy(7.dp),
                ) {
                    itemsIndexed(
                        sanitizedPlaylists,
                        key = { index, playlist -> "${playlist.id}_${playlist.remotePlaylistId ?: ""}_$index" },
                    ) { _, playlist ->
                        val selected = playlist.id in selectedPlaylistIds
                        val alreadyAdded = playlist.id in knownDuplicatePlaylistIds ||
                            playlist.tracks.any {
                                it.key == trackKey ||
                                    (it.name.equals(track.title, ignoreCase = true) && it.artist.equals(track.artist, ignoreCase = true))
                            }
                        val trackCount = playlist.remoteTrackCount
                            ?: playlist.tracks.size.takeIf { playlist.remotePlaylistId == null }
                        val countLabel = when (trackCount) {
                            null -> "Track count unavailable"
                            1 -> "1 track"
                            else -> "$trackCount tracks"
                        }
                        val rowColor by animateColorAsState(
                            targetValue = if (selected) {
                                MaterialTheme.colorScheme.secondaryContainer
                            } else {
                                MaterialTheme.colorScheme.surfaceContainerHigh
                            },
                            animationSpec = tween(ExpressiveMotion.Quick),
                            label = "playlistSelectionColor",
                        )
                        Surface(
                            onClick = {
                                selectedPlaylistIds = if (selected) {
                                    selectedPlaylistIds - playlist.id
                                } else {
                                    selectedPlaylistIds + playlist.id
                                }
                            },
                            shape = RoundedCornerShape(18.dp),
                            color = rowColor,
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                PlaylistCover(
                                    playlist = playlist,
                                    modifier = Modifier.size(52.dp),
                                    cornerRadius = 14.dp,
                                )
                                Column(Modifier.weight(1f).padding(horizontal = 13.dp)) {
                                    Text(
                                        playlist.title,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.SemiBold,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                    )
                                    Text(
                                        when {
                                            isCheckingDuplicates && selected -> "$countLabel · Checking contents…"
                                            alreadyAdded -> "$countLabel · Already added"
                                            else -> countLabel
                                        },
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = if (alreadyAdded) {
                                            MaterialTheme.colorScheme.primary
                                        } else {
                                            MaterialTheme.colorScheme.onSurfaceVariant
                                        },
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                    )
                                }
                                Checkbox(checked = selected, onCheckedChange = null)
                            }
                        }
                    }
                }
            }

            OutlinedTextField(
                value = newPlaylistName,
                onValueChange = { newPlaylistName = it },
                label = { Text("New playlist name") },
                singleLine = true,
                trailingIcon = {
                    IconButton(
                        enabled = newPlaylistName.isNotBlank(),
                        onClick = {
                            val cleanName = newPlaylistName.trim()
                            val existingPlaylist = sanitizedPlaylists.firstOrNull {
                                it.mode == "custom" && it.title.equals(cleanName, ignoreCase = true)
                            }
                            if (existingPlaylist == null) {
                                onCreate(cleanName)
                            } else {
                                selectedPlaylistIds = setOf(existingPlaylist.id)
                                requestAdd(setOf(existingPlaylist.id))
                            }
                        },
                    ) {
                        Icon(Icons.Filled.Add, contentDescription = "Create playlist and add track")
                    }
                },
                modifier = Modifier.fillMaxWidth(),
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                TextButton(onClick = onDismiss) { Text("Cancel") }
                Spacer(Modifier.width(8.dp))
                Button(
                    onClick = { requestAdd(selectedPlaylistIds) },
                    enabled = selectedPlaylistIds.isNotEmpty() && !isCheckingDuplicates,
                ) {
                    if (isCheckingDuplicates) {
                        ExpressiveInlineLoadingIndicator(
                            size = 18.dp,
                            strokeWidth = 2.dp,
                            color = MaterialTheme.colorScheme.onPrimary,
                        )
                    } else {
                        Icon(Icons.Filled.PlaylistAdd, contentDescription = null, modifier = Modifier.size(19.dp))
                    }
                    Spacer(Modifier.width(8.dp))
                    Text(
                        when {
                            selectedPlaylistIds.isEmpty() -> "Add"
                            isCheckingDuplicates -> "Checking"
                            else -> "Add to ${selectedPlaylists.size}"
                        },
                    )
                }
            }
        }
    }
}

private enum class SeekDirection { REWIND, FORWARD }

@Composable
private fun FullPlayer(
    state: MusicPlayerState,
    progressState: StateFlow<PlaybackProgressState>,
    player: MusicPlayer,
    lyricsState: LyricsUiState,
    lyricsUiVersion: LyricsUiVersion = LyricsUiVersion.MODERN,
    lyricsAnimation: LyricsAnimation = LyricsAnimation.APPLE_FLUID,
    wavySeekbarEnabled: Boolean = true,
    seekbarStyle: com.lastwave.app.data.local.SeekbarStyle = com.lastwave.app.data.local.SeekbarStyle.WAVY_FLUID,
    playerCoverStyle: com.lastwave.app.data.local.PlayerCoverStyle = com.lastwave.app.data.local.PlayerCoverStyle.SQUARE_COVER,
    playerStyle: com.lastwave.app.data.local.PlayerStyle = com.lastwave.app.data.local.PlayerStyle.MODERN_M3,
    playerBackgroundStyle: com.lastwave.app.data.local.PlayerBackgroundStyle = com.lastwave.app.data.local.PlayerBackgroundStyle.BLURRED_GLASS,
    showLyricsTranslation: Boolean = true,
    showLyricsPhonetic: Boolean = true,
    onToggleTranslation: (() -> Unit)? = null,
    onTogglePhonetic: (() -> Unit)? = null,
    currentTab: FullPlayerTab,
    onTabChange: (FullPlayerTab) -> Unit,
    onRetryLyrics: () -> Unit,
    onCollapse: () -> Unit,
    onOpenArtist: (String) -> Unit = {},
    isLiked: Boolean = false,
    onToggleLiked: () -> Unit = {},
    onDoubleTapLike: () -> Unit = {},
) {
    val track = state.current ?: return
    var lyricsFullscreen by remember(currentTab) { mutableStateOf(false) }
    BackHandler(enabled = lyricsFullscreen) { lyricsFullscreen = false }
    val view = LocalView.current
    DisposableEffect(view, lyricsFullscreen) {
        val fullscreenActive = lyricsFullscreen
        val activity = generateSequence(view.context) { (it as? android.content.ContextWrapper)?.baseContext }
            .filterIsInstance<android.app.Activity>().firstOrNull()
        val controller = activity?.window?.let { WindowCompat.getInsetsController(it, view) }
        val previousBehavior = controller?.systemBarsBehavior
        if (fullscreenActive) {
            controller?.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            controller?.hide(WindowInsetsCompat.Type.systemBars())
        }
        onDispose {
            if (fullscreenActive) {
                controller?.show(WindowInsetsCompat.Type.systemBars())
                previousBehavior?.let { controller?.systemBarsBehavior = it }
            }
        }
    }
    var showTrackMenu by remember(track.videoId, track.title) { mutableStateOf(false) }
    var artworkDragX by remember(track.videoId, track.title) { mutableFloatStateOf(0f) }
    var dismissDragY by remember(track.videoId, track.title) { mutableFloatStateOf(0f) }
    var isDismissDragging by remember { mutableStateOf(false) }
    var seekOverlayDirection by remember(track.videoId, track.title) { mutableStateOf<SeekDirection?>(null) }
    var seekOverlaySeconds by remember(track.videoId, track.title) { mutableIntStateOf(0) }
    var lastTapTimestamp by remember(track.videoId, track.title) { mutableLongStateOf(0L) }
    var lastTapSide by remember(track.videoId, track.title) { mutableStateOf<SeekDirection?>(null) }
    var lastLikeTapTimestamp by remember(track.videoId, track.title) { mutableLongStateOf(0L) }
    var seekResetJob by remember(track.videoId, track.title) { mutableStateOf<Job?>(null) }
    val coroutineScope = rememberCoroutineScope()
    val shownArtworkX by animateFloatAsState(
        artworkDragX,
        ExpressiveMotion.spatialSpring(),
        label = "fullPlayerArtworkX",
    )
    val shownDismissY by animateFloatAsState(
        targetValue = dismissDragY,
        animationSpec = if (isDismissDragging) snap() else ExpressiveMotion.spatialSpring(),
        label = "fullPlayerDismissY",
    )
    val swipeThreshold = with(LocalDensity.current) { 88.dp.toPx() }

    // Dominant cover-art color for the ambient background glow. Extracted
    // once per track through the shared Coil loader (normally a cache hit)
    // with Palette; any failure leaves the standard surface gradient.
    val context = LocalContext.current
    var ambientPrimary by remember(track.videoId, track.artworkUrl, track.title, track.artist) { mutableStateOf<Color?>(null) }
    var ambientSecondary by remember(track.videoId, track.artworkUrl, track.title, track.artist) { mutableStateOf<Color?>(null) }
    var ambientTertiary by remember(track.videoId, track.artworkUrl, track.title, track.artist) { mutableStateOf<Color?>(null) }
    LaunchedEffect(track.videoId, track.artworkUrl, track.title, track.artist) {
        val url = track.artworkUrl?.takeIf { it.isNotBlank() } ?: return@LaunchedEffect
        withContext(Dispatchers.IO) {
            runCatching {
                val request = ImageRequest.Builder(context)
                    .data(url)
                    .allowHardware(false)
                    .size(128)
                    .build()
                val bitmap = ((context.imageLoader.execute(request) as? SuccessResult)?.drawable as? BitmapDrawable)?.bitmap
                    ?: return@runCatching
                val palette = Palette.from(bitmap).clearFilters().generate()
                val p = palette.vibrantSwatch ?: palette.dominantSwatch ?: palette.mutedSwatch
                val s = palette.lightVibrantSwatch ?: palette.darkVibrantSwatch ?: palette.mutedSwatch ?: palette.dominantSwatch
                val t = palette.darkVibrantSwatch ?: palette.darkMutedSwatch ?: palette.dominantSwatch
                if (p != null) ambientPrimary = Color(p.rgb)
                if (s != null) ambientSecondary = Color(s.rgb)
                if (t != null) ambientTertiary = Color(t.rgb)
            }
        }
    }
    val ambientColor by animateColorAsState(
        targetValue = ambientPrimary ?: MaterialTheme.colorScheme.primary,
        animationSpec = tween(700),
        label = "playerAmbientColor",
    )
    val ambientCompanion by animateColorAsState(
        targetValue = ambientSecondary ?: androidx.compose.ui.graphics.lerp(MaterialTheme.colorScheme.tertiary, ambientColor, 0.42f),
        animationSpec = tween(700),
        label = "playerAmbientCompanion",
    )
    val ambientDeep by animateColorAsState(
        targetValue = ambientTertiary ?: androidx.compose.ui.graphics.lerp(MaterialTheme.colorScheme.secondary, ambientColor, 0.25f),
        animationSpec = tween(700),
        label = "playerAmbientDeep",
    )

    fun Modifier.playerVerticalSwipe(enabled: Boolean): Modifier = if (!enabled) this else pointerInput(track.videoId, track.title, currentTab) {
        detectVerticalDragGestures(
            onDragStart = { isDismissDragging = true },
            onDragCancel = {
                isDismissDragging = false
                dismissDragY = 0f
            },
            onDragEnd = {
                isDismissDragging = false
                when {
                    currentTab != FullPlayerTab.NOW_PLAYING && dismissDragY > swipeThreshold -> onTabChange(FullPlayerTab.NOW_PLAYING)
                    currentTab == FullPlayerTab.NOW_PLAYING && dismissDragY < -swipeThreshold -> onTabChange(FullPlayerTab.QUEUE)
                    currentTab == FullPlayerTab.NOW_PLAYING && dismissDragY > swipeThreshold -> onCollapse()
                }
                dismissDragY = 0f
            },
        ) { change, amount ->
            val updatedDrag = if (currentTab != FullPlayerTab.NOW_PLAYING) {
                (dismissDragY + amount).coerceAtLeast(0f)
            } else {
                dismissDragY + amount
            }
            if (updatedDrag != dismissDragY) change.consume()
            dismissDragY = updatedDrag
        }
    }

    val playerBackdrop = if (isLiquidGlassBackdropSupported()) rememberLayerBackdrop() else null
    var activeCanvasUrl by remember { mutableStateOf<String?>(null) }
    LaunchedEffect(track.videoId) {
        activeCanvasUrl = null
        val current = track
        activeCanvasUrl = runCatching {
            com.lastwave.app.data.canvas.CanvasRepository(okhttp3.OkHttpClient()).fetchCanvasLoop(current.title, current.artist)?.canvasUrl
        }.getOrNull()
    }

    val canvasPlayer = remember {
        androidx.media3.exoplayer.ExoPlayer.Builder(context).build().apply {
            volume = 0f
            repeatMode = androidx.media3.common.Player.REPEAT_MODE_ALL
        }
    }
    DisposableEffect(canvasPlayer) {
        onDispose { canvasPlayer.release() }
    }
    LaunchedEffect(activeCanvasUrl) {
        if (!activeCanvasUrl.isNullOrBlank()) {
            canvasPlayer.setMediaItem(androidx.media3.common.MediaItem.fromUri(activeCanvasUrl!!))
            canvasPlayer.prepare()
            canvasPlayer.play()
        } else {
            canvasPlayer.stop()
            canvasPlayer.clearMediaItems()
        }
    }

    CompositionLocalProvider(
        LocalLiquidGlassBackdrop provides playerBackdrop,
        LocalLiquidGlassOverlayBackdrop provides playerBackdrop,
        androidx.compose.material3.LocalContentColor provides Color.White,
    ) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        modifier = Modifier
            .fillMaxSize()
            .graphicsLayer {
                translationY = shownDismissY.coerceAtLeast(0f)
                val playerHeight = size.height.coerceAtLeast(1f)
                alpha = (1f - shownDismissY.coerceAtLeast(0f) / (playerHeight * 1.5f)).coerceIn(0.72f, 1f)
            }
            .playerVerticalSwipe(enabled = currentTab == FullPlayerTab.NOW_PLAYING),
    ) {
        LastWaveXPlayerRoot(
            backgroundStyle = playerBackgroundStyle,
            dominantColor = ambientColor,
            accentColor = ambientCompanion,
            track = track,
            isPlaying = state.isPlaying,
            modifier = Modifier.fillMaxSize(),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            0.0f to Color.Transparent,
                            0.40f to Color.Transparent,
                            0.70f to Color(0x99050811),
                            1.0f to Color(0xEE050811),
                        )
                    )
            )
            Column(
                Modifier
                    .fillMaxSize()
                    .windowInsetsPadding(WindowInsets.safeDrawing),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                // ── Header: slimmer, calmer, premium ─────────────────────
                if (!lyricsFullscreen) Box(
                    Modifier
                        .fillMaxWidth()
                        .adaptiveContentWidth(maxWidth = 640.dp)
                        .height(52.dp)
                        .padding(horizontal = 20.dp)
                        .playerVerticalSwipe(enabled = currentTab != FullPlayerTab.NOW_PLAYING),
                ) {
                    IconButton(
                        onClick = {
                            if (currentTab != FullPlayerTab.NOW_PLAYING) {
                                onTabChange(FullPlayerTab.NOW_PLAYING)
                            } else {
                                onCollapse()
                            }
                        },
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .size(44.dp)
                            .clip(CircleShape)
                            .liquidGlassChrome(CircleShape, LocalLiquidGlass.current, LiquidGlassPreset.FloatingControls)
                            .background(
                                liquidGlassContainerColor(MaterialTheme.colorScheme.surfaceContainerHighest.copy(alpha = 0.40f)),
                            ),
                    ) {
                        Icon(
                            if (currentTab != FullPlayerTab.NOW_PLAYING) Icons.Filled.ArrowBack else Icons.Filled.ExpandMore,
                            if (currentTab != FullPlayerTab.NOW_PLAYING) "Back to player" else "Minimize player",
                            modifier = Modifier.size(26.dp),
                            tint = Color.White,
                        )
                    }
                    Column(
                        Modifier.align(Alignment.Center).padding(horizontal = 96.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(
                            when (currentTab) {
                                FullPlayerTab.NOW_PLAYING -> "NOW PLAYING"
                                FullPlayerTab.LYRICS -> "LYRICS"
                                FullPlayerTab.QUEUE -> "PLAYING QUEUE"
                            },
                            style = MaterialTheme.typography.labelMedium,
                            color = Color(0xFF00F0FF),
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.9.sp,
                        )
                        Text(
                            if (currentTab == FullPlayerTab.LYRICS) {
                                "${track.title} • ${track.artist}"
                            } else {
                                state.sourceLabel.takeIf { it.isNotBlank() } ?: "LASTWAVEX"
                            },
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFFCBD5E1),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                    Row(
                        modifier = Modifier.align(Alignment.CenterEnd),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        IconButton(
                            onClick = { showTrackMenu = true },
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .liquidGlassChrome(CircleShape, LocalLiquidGlass.current, LiquidGlassPreset.FloatingControls)
                                .background(
                                    liquidGlassContainerColor(MaterialTheme.colorScheme.surfaceContainerHighest.copy(alpha = 0.40f)),
                                ),
                        ) {
                            Icon(
                                Icons.Filled.MoreVert,
                                "Song options",
                                modifier = Modifier.size(22.dp),
                                tint = Color.White,
                            )
                        }
                    }
                }

                if (!lyricsFullscreen) Spacer(Modifier.height(6.dp))

                val configuration = LocalConfiguration.current
                val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
                val isSplitScreen = isLandscape || (playerStyle == com.lastwave.app.data.local.PlayerStyle.SPLIT_SCREEN && isLandscape)

                if (isSplitScreen && !lyricsFullscreen) {
                    Row(
                        modifier = Modifier.fillMaxSize().padding(horizontal = 12.dp, vertical = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        Column(
                            modifier = Modifier.weight(0.45f).fillMaxHeight(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.SpaceBetween,
                        ) {
                            Box(
                                modifier = Modifier.fillMaxWidth().weight(1f),
                                contentAlignment = Alignment.Center,
                            ) {
                                ArtworkImage(
                                    name = track.title,
                                    artist = track.artist,
                                    embeddedUrl = track.artworkUrl,
                                    fallbackIcon = Icons.Filled.MusicNote,
                                    modifier = Modifier.size(200.dp).clip(RoundedCornerShape(24.dp)),
                                )
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = track.title,
                                    style = MaterialTheme.typography.headlineSmall,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = track.artist,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color(0xFFCBD5E1),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                            SeekBar(
                                progressState = progressState,
                                isPlaying = state.isPlaying,
                                trackKey = track.videoId ?: "${track.artist}|${track.title}",
                                wavyEnabled = wavySeekbarEnabled,
                                seekbarStyle = seekbarStyle,
                                onSeek = player::seekTo,
                            )
                            MainControls(state = state, player = player)
                            PlayerUtilityControls(state = state, player = player)
                        }
                        Column(
                            modifier = Modifier.weight(0.55f).fillMaxHeight(),
                        ) {
                            ModernLyricsPanel(
                                state = state,
                                player = player,
                                lyricsState = lyricsState,
                                progressState = progressState,
                                wavySeekbarEnabled = wavySeekbarEnabled,
                                showTranslation = showLyricsTranslation,
                                showPhonetic = showLyricsPhonetic,
                                onToggleTranslation = onToggleTranslation,
                                onTogglePhonetic = onTogglePhonetic,
                                onRetry = onRetryLyrics,
                                modifier = Modifier.fillMaxSize(),
                            )
                        }
                    }
                } else {
                AnimatedContent(
                    targetState = currentTab,
                    modifier = Modifier.weight(1f).adaptiveContentWidth(maxWidth = 680.dp),
                    transitionSpec = {
                        if (targetState != FullPlayerTab.NOW_PLAYING) {
                            (slideInVertically(animationSpec = ExpressiveMotion.smoothSpring()) { it / 6 } +
                                fadeIn(tween(ExpressiveMotion.Standard))) togetherWith
                                (slideOutVertically(animationSpec = tween(ExpressiveMotion.Quick)) { -it / 6 } +
                                    fadeOut(tween(ExpressiveMotion.Quick)))
                        } else {
                            (slideInVertically(animationSpec = ExpressiveMotion.smoothSpring()) { -it / 6 } +
                                fadeIn(tween(ExpressiveMotion.Standard))) togetherWith
                                (slideOutVertically(animationSpec = tween(ExpressiveMotion.Quick)) { it / 6 } +
                                    fadeOut(tween(ExpressiveMotion.Quick)))
                        }
                    },
                    label = "playerTabContent",
                ) { tab ->
                    when (tab) {
                        FullPlayerTab.LYRICS -> {
                            if (lyricsUiVersion == LyricsUiVersion.MODERN) {
                                ModernLyricsPanel(
                                    state = state,
                                    player = player,
                                    lyricsState = lyricsState,
                                    progressState = progressState,
                                    wavySeekbarEnabled = wavySeekbarEnabled,
                                    showTranslation = showLyricsTranslation,
                                    showPhonetic = showLyricsPhonetic,
                                    onToggleTranslation = onToggleTranslation,
                                    onTogglePhonetic = onTogglePhonetic,
                                    onRetry = onRetryLyrics,
                                    onToggleFullscreen = { lyricsFullscreen = !lyricsFullscreen },
                                    isFullscreen = lyricsFullscreen,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .adaptiveContentWidth(maxWidth = 720.dp),
                                )
                            } else {
                                LyricsPanel(
                                    state = state,
                                    progressState = progressState,
                                    player = player,
                                    lyricsState = lyricsState,
                                    lyricsAnimation = lyricsAnimation,
                                    wavySeekbarEnabled = wavySeekbarEnabled,
                                    onRetry = onRetryLyrics,
                                    onToggleFullscreen = { lyricsFullscreen = !lyricsFullscreen },
                                    isFullscreen = lyricsFullscreen,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .adaptiveContentWidth(maxWidth = 720.dp),
                                )
                            }
                        }

                        FullPlayerTab.QUEUE -> {
                            QueuePanel(
                                state,
                                player,
                                Modifier
                                    .fillMaxSize()
                                    .adaptiveContentWidth(maxWidth = 720.dp)
                                    .padding(horizontal = 20.dp),
                            )
                        }

                        FullPlayerTab.NOW_PLAYING -> {
                                // ── LASTWAVEX Prism Player Stage ──────
                                Column(
                                    Modifier.fillMaxSize().padding(horizontal = 20.dp).padding(bottom = 18.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                ) {
                                    // 1. Large Artwork Stage
                                    BoxWithConstraints(
                                        modifier = Modifier.fillMaxWidth().weight(1f),
                                        contentAlignment = BiasAlignment(0f, -0.45f),
                                    ) {
                                        val maxArtSize = when (playerStyle) {
                                            com.lastwave.app.data.local.PlayerStyle.CLASSIC -> 320.dp
                                            com.lastwave.app.data.local.PlayerStyle.MINIMALIST -> 220.dp
                                            com.lastwave.app.data.local.PlayerStyle.COMPACT_DOCK -> 260.dp
                                            com.lastwave.app.data.local.PlayerStyle.IMMERSIVE_FULLSCREEN -> 420.dp
                                            com.lastwave.app.data.local.PlayerStyle.CAROUSEL -> 340.dp
                                            com.lastwave.app.data.local.PlayerStyle.SPLIT_SCREEN -> 280.dp
                                            com.lastwave.app.data.local.PlayerStyle.CIDER_PANORAMIC -> 360.dp
                                            else -> 380.dp
                                        }
                                        val artworkSize = (minOf(maxWidth, maxHeight) - 12.dp)
                                            .coerceAtLeast(0.dp)
                                            .coerceAtMost(maxArtSize)

                                        val glowAlpha by animateFloatAsState(
                                            targetValue = if (state.isPlaying) 0.70f else 0.35f,
                                            animationSpec = tween(600),
                                            label = "artworkGlowAlpha",
                                        )
                                        Box(
                                            modifier = Modifier
                                                .size(artworkSize + 32.dp)
                                                .graphicsLayer {
                                                    translationX = shownArtworkX * 0.7f
                                                    alpha = glowAlpha
                                                }
                                                .background(
                                                    Brush.radialGradient(
                                                        0.0f to ambientColor.copy(alpha = 0.55f),
                                                        0.50f to ambientCompanion.copy(alpha = 0.25f),
                                                        1.0f to Color.Transparent,
                                                    ),
                                                    shape = CircleShape,
                                                ),
                                        )

                                        val artworkPlayingScale by animateFloatAsState(
                                            targetValue = if (state.isPlaying) 1.0f else 0.88f,
                                            animationSpec = spring(
                                                dampingRatio = Spring.DampingRatioMediumBouncy,
                                                stiffness = Spring.StiffnessMediumLow,
                                            ),
                                            label = "artworkPlayingScale",
                                        )

                                        val isVinylMode = playerCoverStyle == com.lastwave.app.data.local.PlayerCoverStyle.ROTATING_VINYL
                                        val effectiveCoverStyle = if (isVinylMode) com.lastwave.app.data.local.PlayerCoverStyle.ROTATING_VINYL else playerCoverStyle

                                        val tonearmAngle by animateFloatAsState(
                                            targetValue = if (state.isPlaying && isVinylMode) 28f else 0f,
                                            animationSpec = spring(stiffness = 300f, dampingRatio = 0.7f),
                                            label = "tonearmAngle",
                                        )

                                        if (playerStyle == com.lastwave.app.data.local.PlayerStyle.CAROUSEL) {
                                            val queue = state.queue.ifEmpty { listOf(track) }
                                            val initialPage = state.currentIndex.coerceIn(0, queue.lastIndex)
                                            val pagerState = rememberPagerState(initialPage = initialPage) { queue.size }

                                            LaunchedEffect(pagerState.currentPage) {
                                                if (pagerState.currentPage > state.currentIndex) player.next()
                                                else if (pagerState.currentPage < state.currentIndex) player.previous()
                                            }

                                            HorizontalPager(
                                                state = pagerState,
                                                modifier = Modifier.fillMaxWidth().height(artworkSize + 20.dp),
                                                contentPadding = PaddingValues(horizontal = 36.dp),
                                                pageSpacing = 16.dp,
                                            ) { page ->
                                                val itemTrack = queue.getOrNull(page) ?: track
                                                val pageOffset = (pagerState.currentPage - page) + pagerState.currentPageOffsetFraction
                                                val depthScale = (1.0f - 0.12f * kotlin.math.abs(pageOffset)).coerceIn(0.85f, 1.0f)
                                                val depthAlpha = (1.0f - 0.35f * kotlin.math.abs(pageOffset)).coerceIn(0.40f, 1.0f)

                                                Surface(
                                                    shape = RoundedCornerShape(28.dp),
                                                    color = MaterialTheme.colorScheme.surfaceContainerHighest.copy(alpha = 0.88f),
                                                    tonalElevation = 6.dp,
                                                    shadowElevation = if (state.isPlaying) 20.dp else 8.dp,
                                                    modifier = Modifier
                                                        .size(artworkSize)
                                                        .graphicsLayer {
                                                            scaleX = depthScale
                                                            scaleY = depthScale
                                                            alpha = depthAlpha
                                                        },
                                                ) {
                                                    ArtworkImage(
                                                        name = itemTrack.title,
                                                        artist = itemTrack.artist,
                                                        embeddedUrl = itemTrack.artworkUrl,
                                                        fallbackIcon = Icons.Filled.MusicNote,
                                                        modifier = Modifier.fillMaxSize(),
                                                    )
                                                }
                                            }
                                        } else {
                                        Surface(
                                            shape = if (isVinylMode) RoundedCornerShape(0.dp) else RoundedCornerShape(32.dp),
                                            color = if (isVinylMode) Color.Transparent else MaterialTheme.colorScheme.surfaceContainerHighest.copy(alpha = 0.88f),
                                            tonalElevation = if (isVinylMode) 0.dp else 6.dp,
                                            shadowElevation = if (isVinylMode) 0.dp else (if (state.isPlaying) 28.dp else 12.dp),
                                            modifier = Modifier
                                                .size(artworkSize)
                                                .graphicsLayer {
                                                    scaleX = artworkPlayingScale
                                                    scaleY = artworkPlayingScale
                                                    translationX = shownArtworkX
                                                    rotationZ = shownArtworkX / 80f
                                                }
                                                .pointerInput(track.videoId, track.title) {
                                                    awaitEachGesture {
                                                        val down = awaitFirstDown(requireUnconsumed = false)
                                                        var isDrag = false
                                                        val touchSlop = viewConfiguration.touchSlop
                                                        val initialX = down.position.x
                                                        val initialY = down.position.y

                                                        while (true) {
                                                            val event = awaitPointerEvent()
                                                            val change = event.changes.firstOrNull { it.id == down.id } ?: break

                                                            if (!change.pressed) {
                                                                if (isDrag) {
                                                                    when {
                                                                        artworkDragX < -swipeThreshold -> player.next()
                                                                        artworkDragX > swipeThreshold -> player.previous()
                                                                    }
                                                                    artworkDragX = 0f
                                                                } else {
                                                                    val now = SystemClock.elapsedRealtime()
                                                                    val side = when {
                                                                        initialX < size.width * 0.34f -> SeekDirection.REWIND
                                                                        initialX > size.width * 0.66f -> SeekDirection.FORWARD
                                                                        else -> null
                                                                    }

                                                                    if (side == null) {
                                                                        lastTapSide = null
                                                                        lastTapTimestamp = 0L
                                                                        if (lastLikeTapTimestamp != 0L && now - lastLikeTapTimestamp < 450L) {
                                                                            lastLikeTapTimestamp = 0L
                                                                            onDoubleTapLike()
                                                                        } else {
                                                                            lastLikeTapTimestamp = now
                                                                        }
                                                                    } else {
                                                                        lastLikeTapTimestamp = 0L
                                                                        if (lastTapSide != side) {
                                                                            seekResetJob?.cancel()
                                                                            seekOverlayDirection = null
                                                                            lastTapSide = side
                                                                            lastTapTimestamp = now
                                                                        } else if (now - lastTapTimestamp < 450L) {
                                                                            val newSeconds = if (seekOverlayDirection == side) seekOverlaySeconds + 5 else 5
                                                                            seekOverlaySeconds = newSeconds
                                                                            seekOverlayDirection = side
                                                                            lastTapTimestamp = now
                                                                            val deltaMs = if (side == SeekDirection.FORWARD) 5_000L else -5_000L
                                                                            val newPos = (player.state.value.positionMs + deltaMs).coerceIn(0L, player.state.value.durationMs.coerceAtLeast(0L))
                                                                            player.seekTo(newPos)

                                                                            seekResetJob?.cancel()
                                                                            seekResetJob = coroutineScope.launch {
                                                                                delay(700L)
                                                                                seekOverlayDirection = null
                                                                                lastTapSide = null
                                                                            }
                                                                        } else {
                                                                            lastTapTimestamp = now
                                                                            lastTapSide = side
                                                                            seekOverlayDirection = null
                                                                        }
                                                                    }
                                                                }
                                                                break
                                                            }

                                                            if (change.isConsumed) {
                                                                artworkDragX = 0f
                                                                lastLikeTapTimestamp = 0L
                                                                lastTapTimestamp = 0L
                                                                lastTapSide = null
                                                                break
                                                            }

                                                            val dx = change.position.x - initialX
                                                            val dy = change.position.y - initialY
                                                            if (!isDrag) {
                                                                if (kotlin.math.abs(dx) > touchSlop && kotlin.math.abs(dx) > kotlin.math.abs(dy)) {
                                                                    isDrag = true
                                                                    lastLikeTapTimestamp = 0L
                                                                    lastTapTimestamp = 0L
                                                                    lastTapSide = null
                                                                    change.consume()
                                                                }
                                                            } else {
                                                                change.consume()
                                                                artworkDragX = dx
                                                            }
                                                        }
                                                    }
                                                },
                                        ) {
                                            Box(Modifier.fillMaxSize()) {
                                                PlayerArtwork(
                                                    track = track,
                                                    modifier = Modifier.fillMaxSize(),
                                                    corner = if (isVinylMode) 0.dp else 32.dp,
                                                    coverStyle = effectiveCoverStyle,
                                                    isPlaying = state.isPlaying,
                                                )

                                                if (isVinylMode) {
                                                    com.lastwave.app.ui.player.vinyl.TurntablePlatterView(
                                                        musicPlayer = player,
                                                        modifier = Modifier.fillMaxSize(),
                                                    )
                                                }

                                                androidx.compose.animation.AnimatedVisibility(
                                                    visible = seekOverlayDirection == SeekDirection.REWIND,
                                                    enter = fadeIn(tween(100)) + scaleIn(ExpressiveMotion.spatialSpring(), initialScale = 0.88f),
                                                    exit = fadeOut(tween(200)),
                                                    modifier = Modifier
                                                        .align(Alignment.CenterStart)
                                                        .fillMaxHeight()
                                                        .fillMaxWidth(0.5f),
                                                ) {
                                                    Box(
                                                        modifier = Modifier
                                                            .fillMaxSize()
                                                            .clip(RoundedCornerShape(topStart = 32.dp, bottomStart = 32.dp, topEnd = 120.dp, bottomEnd = 120.dp))
                                                            .background(Color.Black.copy(alpha = 0.58f)),
                                                        contentAlignment = Alignment.Center,
                                                    ) {
                                                        Column(
                                                            horizontalAlignment = Alignment.CenterHorizontally,
                                                            verticalArrangement = Arrangement.Center,
                                                        ) {
                                                            Surface(
                                                                shape = CircleShape,
                                                                color = Color.White.copy(alpha = 0.22f),
                                                                modifier = Modifier.size(52.dp),
                                                            ) {
                                                                Box(contentAlignment = Alignment.Center) {
                                                                    Icon(
                                                                        Icons.Filled.FastRewind,
                                                                        contentDescription = "Seek rewind",
                                                                        tint = Color.White,
                                                                        modifier = Modifier.size(28.dp),
                                                                    )
                                                                }
                                                            }
                                                            Spacer(Modifier.height(6.dp))
                                                            Text(
                                                                "-${seekOverlaySeconds}s",
                                                                style = MaterialTheme.typography.titleMedium,
                                                                fontWeight = FontWeight.ExtraBold,
                                                                color = Color.White,
                                                            )
                                                        }
                                                    }
                                                }

                                                androidx.compose.animation.AnimatedVisibility(
                                                    visible = seekOverlayDirection == SeekDirection.FORWARD,
                                                    enter = fadeIn(tween(100)) + scaleIn(ExpressiveMotion.spatialSpring(), initialScale = 0.88f),
                                                    exit = fadeOut(tween(200)),
                                                    modifier = Modifier
                                                        .align(Alignment.CenterEnd)
                                                        .fillMaxHeight()
                                                        .fillMaxWidth(0.5f),
                                                ) {
                                                    Box(
                                                        modifier = Modifier
                                                            .fillMaxSize()
                                                            .clip(RoundedCornerShape(topEnd = 32.dp, bottomEnd = 32.dp, topStart = 120.dp, bottomStart = 120.dp))
                                                            .background(Color.Black.copy(alpha = 0.58f)),
                                                        contentAlignment = Alignment.Center,
                                                    ) {
                                                        Column(
                                                            horizontalAlignment = Alignment.CenterHorizontally,
                                                            verticalArrangement = Arrangement.Center,
                                                        ) {
                                                            Surface(
                                                                shape = CircleShape,
                                                                color = Color.White.copy(alpha = 0.22f),
                                                                modifier = Modifier.size(52.dp),
                                                            ) {
                                                                Box(contentAlignment = Alignment.Center) {
                                                                    Icon(
                                                                        Icons.Filled.FastForward,
                                                                        contentDescription = "Seek forward",
                                                                        tint = Color.White,
                                                                        modifier = Modifier.size(28.dp),
                                                                    )
                                                                }
                                                            }
                                                            Spacer(Modifier.height(6.dp))
                                                            Text(
                                                                "+${seekOverlaySeconds}s",
                                                                style = MaterialTheme.typography.titleMedium,
                                                                fontWeight = FontWeight.ExtraBold,
                                                                color = Color.White,
                                                            )
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                        }
                                    }

                                    Spacer(Modifier.height(8.dp))

                                    // 2. Track Information & Actions Row (No Card)
                                    Row(
                                        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 8.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = track.title,
                                                style = MaterialTheme.typography.headlineSmall,
                                                color = Color.White,
                                                fontWeight = FontWeight.Bold,
                                                maxLines = 1,
                                                modifier = Modifier.basicMarquee(iterations = Int.MAX_VALUE)
                                            )
                                            Spacer(Modifier.height(4.dp))
                                            Text(
                                                text = track.artist,
                                                style = MaterialTheme.typography.bodyMedium,
                                                color = Color(0xFFCBD5E1),
                                                maxLines = 1
                                            )
                                            val qualityBadgeText = remember(track.videoId, track.playbackMimeType, track.playbackUrl) {
                                                val isLocalTrack = track.videoId == null || track.playbackUrl?.startsWith("content:") == true || track.playbackUrl?.startsWith("file:") == true
                                                val isFlac = track.playbackMimeType?.contains("flac", ignoreCase = true) == true || track.playbackUrl?.endsWith(".flac", ignoreCase = true) == true
                                                if (isLocalTrack || isFlac) {
                                                    "LOSSLESS • HIGH FIDELITY"
                                                } else if (track.playbackMimeType?.contains("opus", ignoreCase = true) == true || track.playbackMimeType?.contains("webm", ignoreCase = true) == true) {
                                                    "OPUS • 160 KBPS"
                                                } else {
                                                    "AAC • 256 KBPS"
                                                }
                                            }
                                            Spacer(Modifier.height(4.dp))
                                            Surface(
                                                shape = CircleShape,
                                                color = Color(0xFF00F0FF).copy(alpha = 0.20f),
                                                border = BorderStroke(1.dp, Color(0xFF00F0FF).copy(alpha = 0.45f)),
                                            ) {
                                                Text(
                                                    text = qualityBadgeText,
                                                    style = MaterialTheme.typography.labelSmall,
                                                    fontWeight = FontWeight.Bold,
                                                    color = Color(0xFF00F0FF),
                                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                                )
                                            }
                                        }
                                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                                            IconButton(onClick = onToggleLiked) {
                                                Icon(if (isLiked) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder, contentDescription = "Favorite", tint = if (isLiked) Color(0xFFFF2E93) else Color.White.copy(alpha = 0.85f))
                                            }
                                            IconButton(onClick = { onTabChange(FullPlayerTab.LYRICS) }) {
                                                Icon(Icons.Filled.FormatQuote, contentDescription = "Lyrics", tint = Color.White.copy(alpha = 0.85f))
                                            }
                                        }
                                    }

                                    Spacer(Modifier.height(12.dp))

                                    // 3. Seek Progress Area
                                    SeekBar(
                                        progressState = progressState,
                                        isPlaying = state.isPlaying,
                                        trackKey = track.videoId ?: "${track.artist}|${track.title}",
                                        wavyEnabled = wavySeekbarEnabled,
                                        seekbarStyle = seekbarStyle,
                                        onSeek = player::seekTo,
                                        isTranslucent = false,
                                    )

                                    Spacer(Modifier.height(12.dp))

                                    // 4. Primary Transport Controls
                                    Box(
                                        modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp),
                                        contentAlignment = Alignment.Center,
                                    ) {
                                        MainControls(state, player, isTranslucent = false)
                                    }

                                    Spacer(Modifier.height(16.dp))

                                    // 5. Secondary Satellite Actions Dock
                                    if (playerStyle != com.lastwave.app.data.local.PlayerStyle.MINIMALIST) {
                                        PlayerUtilityControls(state, player, isTranslucent = false)
                                    }
                                }
                        }
                        }
                    }
                }
                }
                state.error?.takeUnless { lyricsFullscreen }?.let { message ->
                    Surface(
                        onClick = player::retry,
                        shape = RoundedCornerShape(20.dp),
                        color = MaterialTheme.colorScheme.errorContainer,
                        contentColor = MaterialTheme.colorScheme.onErrorContainer,
                        modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                    ) {
                        Row(
                            Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Icon(Icons.Filled.ErrorOutline, null, modifier = Modifier.size(21.dp))
                            Column(Modifier.weight(1f).padding(horizontal = 10.dp)) {
                                Text(
                                    message,
                                    style = MaterialTheme.typography.bodySmall,
                                    fontWeight = FontWeight.SemiBold,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                )
                                Text(
                                    "Tap to retry",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onErrorContainer.copy(alpha = 0.8f),
                                )
                            }
                            IconButton(
                                onClick = player::clearError,
                                modifier = Modifier.size(28.dp),
                            ) {
                                Icon(Icons.Filled.Close, "Dismiss error", modifier = Modifier.size(18.dp))
                            }
                        }
                    }
                }
            }
        }
    }
    if (showTrackMenu) {
        TrackContextMenuSheet(
            target = TrackMenuTarget.Track(track.title, track.artist, ""),
            capabilities = TrackMenuCapabilities(
                showCopyActions = true,
                showDeleteScrobble = false,
            ),
            playableTrack = track,
            onDismiss = { showTrackMenu = false },
            onPlayInLastWave = { player.play(track, sourceLabel = state.sourceLabel) },
        )
    }
}

@Composable
internal fun PlayerProgressSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    onValueChangeFinished: () -> Unit,
    valueRange: ClosedFloatingPointRange<Float>,
    enabled: Boolean,
    modifier: Modifier = Modifier,
) {
    val primary = Color(0xFF00F0FF)
    val secondary = Color(0xFFFF2E93)
    val inactive = Color.White.copy(alpha = 0.22f)
    val range = (valueRange.endInclusive - valueRange.start).coerceAtLeast(0.0001f)
    val fraction = ((value - valueRange.start) / range).coerceIn(0f, 1f)

    // Playback progress is time, not directional content: pin to LTR so the
    // Material Slider's touch mapping always matches the LTR custom drawing,
    // even in RTL locales (Arabic, …) where Slider would otherwise mirror.
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
    Slider(
        value = value,
        onValueChange = onValueChange,
        onValueChangeFinished = onValueChangeFinished,
        valueRange = valueRange,
        enabled = enabled,
        modifier = modifier.drawBehind {
            val inset = 10.dp.toPx()
            val startX = inset
            val endX = (size.width - inset).coerceAtLeast(startX)
            val activeEndX = startX + ((endX - startX) * fraction)
            val centerY = size.height / 2f
            drawLine(
                color = inactive,
                start = androidx.compose.ui.geometry.Offset(startX, centerY),
                end = androidx.compose.ui.geometry.Offset(endX, centerY),
                strokeWidth = 3.dp.toPx(),
                cap = StrokeCap.Round,
            )
            if (activeEndX > startX) {
                drawLine(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            primary.copy(alpha = if (enabled) 1f else 0.42f),
                            secondary.copy(alpha = if (enabled) 0.92f else 0.36f),
                        ),
                        startX = startX,
                        endX = activeEndX,
                    ),
                    start = androidx.compose.ui.geometry.Offset(startX, centerY),
                    end = androidx.compose.ui.geometry.Offset(activeEndX, centerY),
                    strokeWidth = 4.dp.toPx(),
                    cap = StrokeCap.Round,
                )
            }
        },
        colors = SliderDefaults.colors(
            thumbColor = primary,
            activeTrackColor = Color.Transparent,
            inactiveTrackColor = Color.Transparent,
            activeTickColor = Color.Transparent,
            inactiveTickColor = Color.Transparent,
            disabledThumbColor = primary.copy(alpha = 0.34f),
            disabledActiveTrackColor = Color.Transparent,
            disabledInactiveTrackColor = Color.Transparent,
            disabledActiveTickColor = Color.Transparent,
            disabledInactiveTickColor = Color.Transparent,
        ),
    )
    }
}

@Composable
private fun SeekBar(
    progressState: StateFlow<PlaybackProgressState>,
    isPlaying: Boolean,
    trackKey: String?,
    wavyEnabled: Boolean = true,
    seekbarStyle: com.lastwave.app.data.local.SeekbarStyle = com.lastwave.app.data.local.SeekbarStyle.WAVY_FLUID,
    onSeek: (Long) -> Unit,
    isTranslucent: Boolean = false,
) {
    val progress by progressState.collectAsStateWithLifecycle()

    if (wavyEnabled || seekbarStyle == com.lastwave.app.data.local.SeekbarStyle.STUDIO_CONSOLE) {
        WavySeekBar(
            positionMs = progress.positionMs,
            durationMs = progress.durationMs,
            isPlaying = isPlaying,
            onSeek = onSeek,
            isTranslucent = isTranslucent,
            trackKey = trackKey,
            seekbarStyle = seekbarStyle,
        )
        return
    }

    var dragging by remember(trackKey) { mutableStateOf(false) }
    var dragFraction by remember(trackKey) { mutableFloatStateOf(0f) }

    val boundedDurationMs = progress.durationMs.coerceAtLeast(0L)
    val currentFraction = if (boundedDurationMs > 0L) {
        (progress.positionMs.toDouble() / boundedDurationMs.toDouble()).toFloat().coerceIn(0f, 1f)
    } else {
        0f
    }
    val fraction = if (dragging) dragFraction else currentFraction
    val shownMs = if (dragging) {
        (dragFraction * boundedDurationMs).toLong().coerceIn(0L, boundedDurationMs)
    } else {
        progress.positionMs.coerceIn(0L, boundedDurationMs)
    }

    val primaryColor = Color(0xFF00F0FF)
    val activeBrush = Brush.horizontalGradient(listOf(Color(0xFF00F0FF), Color(0xFFFF2E93)))
    val inactiveColor = Color.White.copy(alpha = 0.22f)
    val textColor = Color(0xFFCBD5E1)

    // Same RTL pin as above: custom Canvas draws LTR, invisible Slider must match it.
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(44.dp),
            contentAlignment = Alignment.Center,
        ) {
            Canvas(modifier = Modifier.fillMaxWidth().height(44.dp)) {
                val width = size.width
                val height = size.height
                val centerY = height / 2f
                val trackHeightPx = 14.dp.toPx()
                val cornerRadius = CornerRadius(trackHeightPx / 2f, trackHeightPx / 2f)
                val thumbWidthPx = 5.dp.toPx()
                val thumbHeightPx = 38.dp.toPx()
                val thumbClearancePx = 9.dp.toPx()
                val rawThumbCenterX = fraction * width
                val thumbCenterX = if (width > thumbWidthPx) {
                    rawThumbCenterX.coerceIn(thumbWidthPx / 2f, width - thumbWidthPx / 2f)
                } else {
                    width / 2f
                }
                val activeEndX = (thumbCenterX - thumbClearancePx).coerceIn(0f, width)
                val inactiveStartX = (thumbCenterX + thumbClearancePx).coerceIn(0f, width)

                // Thick active capsule ending before the vertical thumb.
                if (activeEndX > 0f) {
                    drawRoundRect(
                        brush = activeBrush,
                        topLeft = Offset(0f, centerY - trackHeightPx / 2f),
                        size = Size(activeEndX, trackHeightPx),
                        cornerRadius = cornerRadius,
                    )
                }

                // Thick inactive capsule starting after the vertical thumb.
                if (inactiveStartX < width) {
                    drawRoundRect(
                        color = inactiveColor,
                        topLeft = Offset(inactiveStartX, centerY - trackHeightPx / 2f),
                        size = Size(width - inactiveStartX, trackHeightPx),
                        cornerRadius = cornerRadius,
                    )
                }

                // Small endpoint marker from the reference design.
                val endpointX = width - trackHeightPx / 2f
                if (inactiveStartX < endpointX) {
                    drawCircle(
                        color = primaryColor.copy(alpha = 0.86f),
                        radius = 2.dp.toPx(),
                        center = Offset(endpointX, centerY),
                    )
                }

                // Tall vertical pill thumb with clear space on both sides.
                val thumbX = thumbCenterX - thumbWidthPx / 2f
                val thumbCornerRadius = CornerRadius(thumbWidthPx / 2f, thumbWidthPx / 2f)

                drawRoundRect(
                    color = primaryColor,
                    topLeft = Offset(thumbX, centerY - thumbHeightPx / 2f),
                    size = Size(thumbWidthPx, thumbHeightPx),
                    cornerRadius = thumbCornerRadius,
                )
            }

            // Invisible Material interaction layer: custom visuals, reliable seeking semantics.
            Slider(
                value = fraction,
                onValueChange = {
                    dragging = true
                    dragFraction = it
                },
                onValueChangeFinished = {
                    if (boundedDurationMs > 0L) {
                        onSeek((dragFraction * boundedDurationMs).toLong().coerceIn(0L, boundedDurationMs))
                    }
                    dragging = false
                },
                valueRange = 0f..1f,
                enabled = boundedDurationMs > 0L,
                modifier = Modifier.fillMaxSize(),
                colors = SliderDefaults.colors(
                    thumbColor = Color.Transparent,
                    activeTrackColor = Color.Transparent,
                    inactiveTrackColor = Color.Transparent,
                    activeTickColor = Color.Transparent,
                    inactiveTickColor = Color.Transparent,
                    disabledThumbColor = Color.Transparent,
                    disabledActiveTrackColor = Color.Transparent,
                    disabledInactiveTrackColor = Color.Transparent,
                    disabledActiveTickColor = Color.Transparent,
                    disabledInactiveTickColor = Color.Transparent,
                ),
            )
        }

        Spacer(Modifier.height(2.dp))

        // Time labels
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = formatTime(shownMs),
                style = if (isTranslucent) MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Medium) else MaterialTheme.typography.labelMedium,
                color = textColor,
            )
            Text(
                text = "−${formatTime((boundedDurationMs - shownMs).coerceAtLeast(0))}",
                style = if (isTranslucent) MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Medium) else MaterialTheme.typography.labelMedium,
                color = textColor,
            )
        }
    }
    }
}

@Composable
private fun MainControls(
    state: MusicPlayerState,
    player: MusicPlayer,
    isTranslucent: Boolean = false,
    ambientPrimary: Color? = null,
) {
    val fabBackground = if (isTranslucent) Color.White else (ambientPrimary ?: MaterialTheme.colorScheme.primary)
    val fabForeground = if (fabBackground.luminance() > 0.5f) Color(0xFF050811) else Color.White

    val prevInteraction = remember { androidx.compose.foundation.interaction.MutableInteractionSource() }
    val isPrevPressed by prevInteraction.collectIsPressedAsState()
    val prevScale by animateFloatAsState(if (isPrevPressed) 0.85f else 1.0f, ExpressiveMotion.spatialSpring(), label = "prevScale")

    val playInteraction = remember { androidx.compose.foundation.interaction.MutableInteractionSource() }
    val isPlayPressed by playInteraction.collectIsPressedAsState()
    val playScale by animateFloatAsState(
        targetValue = if (isPlayPressed) 0.88f else 1.0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMediumLow,
        ),
        label = "playScale",
    )

    val nextInteraction = remember { androidx.compose.foundation.interaction.MutableInteractionSource() }
    val isNextPressed by nextInteraction.collectIsPressedAsState()
    val nextScale by animateFloatAsState(if (isNextPressed) 0.85f else 1.0f, ExpressiveMotion.spatialSpring(), label = "nextScale")

    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(if (isTranslucent) 18.dp else 16.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        LiquidGlassSurface(
            glassModifier = Modifier.liquidGlassChrome(CircleShape, LocalLiquidGlass.current, LiquidGlassPreset.PlayerControls),
            onClick = player::previous,
            interactionSource = prevInteraction,
            shape = CircleShape,
            color = liquidGlassContainerColor(if (isTranslucent) Color.White.copy(alpha = 0.14f) else MaterialTheme.colorScheme.surfaceContainerHighest.copy(alpha = 0.40f)),
            contentColor = Color.White,
            tonalElevation = 0.dp,
            shadowElevation = 0.dp,
            modifier = Modifier
                .size(if (isTranslucent) 54.dp else 58.dp)
                .graphicsLayer {
                    scaleX = prevScale
                    scaleY = prevScale
                },
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(Icons.Filled.SkipPrevious, "Previous", Modifier.size(if (isTranslucent) 28.dp else 31.dp), tint = Color.White)
            }
        }
        LiquidGlassSurface(
            glassModifier = Modifier.liquidGlassChrome(CircleShape, LocalLiquidGlass.current, LiquidGlassPreset.FloatingControls),
            onClick = player::togglePlayPause,
            interactionSource = playInteraction,
            shape = CircleShape,
            color = fabBackground,
            contentColor = fabForeground,
            tonalElevation = 0.dp,
            shadowElevation = 0.dp,
            modifier = Modifier
                .size(if (isTranslucent) 72.dp else 76.dp)
                .graphicsLayer {
                    scaleX = playScale
                    scaleY = playScale
                },
        ) {
            Box(contentAlignment = Alignment.Center) {
                if (state.isBuffering) {
                    ExpressiveInlineLoadingIndicator(
                        size = if (isTranslucent) 28.dp else 30.dp,
                        color = fabForeground,
                        strokeWidth = 3.dp,
                    )
                } else {
                    AnimatedPlayPauseIcon(state.isPlaying, Modifier.size(if (isTranslucent) 36.dp else 39.dp), tint = fabForeground)
                }
            }
        }
        LiquidGlassSurface(
            glassModifier = Modifier.liquidGlassChrome(CircleShape, LocalLiquidGlass.current, LiquidGlassPreset.PlayerControls),
            onClick = player::next,
            interactionSource = nextInteraction,
            shape = CircleShape,
            color = liquidGlassContainerColor(if (isTranslucent) Color.White.copy(alpha = 0.14f) else MaterialTheme.colorScheme.surfaceContainerHighest.copy(alpha = 0.40f)),
            contentColor = Color.White,
            tonalElevation = 0.dp,
            shadowElevation = 0.dp,
            modifier = Modifier
                .size(if (isTranslucent) 54.dp else 58.dp)
                .graphicsLayer {
                    scaleX = nextScale
                    scaleY = nextScale
                },
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(Icons.Filled.SkipNext, "Next", Modifier.size(if (isTranslucent) 28.dp else 31.dp))
            }
        }
    }
}

@Composable
private fun PlayerModeButton(
    active: Boolean,
    description: String,
    icon: ImageVector,
    onClick: () -> Unit,
    background: Color,
    foreground: Color,
    iconSize: androidx.compose.ui.unit.Dp,
    modifier: Modifier = Modifier,
) {
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    val scale by animateFloatAsState(
        if (pressed) 0.92f else 1f, ExpressiveMotion.spatialSpring(), label = "modePress",
    )
    val container by animateColorAsState(
        if (active) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.86f)
        else liquidGlassContainerColor(background), label = "modeContainer",
    )
    val content by animateColorAsState(
        if (active) MaterialTheme.colorScheme.onPrimaryContainer else foreground, label = "modeContent",
    )
    LiquidGlassSurface(
        glassModifier = Modifier.liquidGlassChrome(CircleShape, LocalLiquidGlass.current, LiquidGlassPreset.PlayerControls),
        onClick = onClick,
        interactionSource = interaction,
        shape = CircleShape,
        color = container,
        contentColor = content,
        tonalElevation = 0.dp,
        shadowElevation = 0.dp,
        modifier = modifier
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .semantics {
                selected = active
                stateDescription = description
            },
    ) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Icon(icon, description, Modifier.size(iconSize))
            if (active) {
                Box(
                    Modifier.align(Alignment.BottomCenter).padding(bottom = 5.dp)
                        .size(4.dp).background(content, CircleShape),
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PlayerUtilityControls(state: MusicPlayerState, player: MusicPlayer, isTranslucent: Boolean = false) {
    var showSignalPath by remember { mutableStateOf(false) }
    val signalPath by player.signalPath.collectAsStateWithLifecycle()
    val usbDac by player.usbDacState.collectAsStateWithLifecycle()
    val edgeButtonBackground = if (isTranslucent) {
        Color.White.copy(alpha = 0.12f)
    } else {
        MaterialTheme.colorScheme.surfaceContainerHighest.copy(alpha = 0.40f)
    }
    val edgeButtonContent = if (isTranslucent) {
        Color.White.copy(alpha = 0.85f)
    } else {
        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.90f)
    }
    val qualityButtonBackground = if (isTranslucent) {
        Color.White.copy(alpha = 0.12f)
    } else {
        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.65f)
    }
    val qualityButtonContent = if (isTranslucent) {
        Color.White
    } else {
        MaterialTheme.colorScheme.onPrimaryContainer
    }

    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(if (isTranslucent) 10.dp else 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        PlayerModeButton(
            active = state.shuffleEnabled,
            description = if (state.shuffleEnabled) "Shuffle on" else "Shuffle off",
            icon = Icons.Filled.Shuffle,
            onClick = player::toggleShuffle,
            background = edgeButtonBackground,
            foreground = edgeButtonContent,
            iconSize = if (isTranslucent) 19.dp else 20.dp,
            modifier = Modifier.weight(1f).height(if (isTranslucent) 44.dp else 48.dp),
        )
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = liquidGlassContainerColor(qualityButtonBackground),
            contentColor = qualityButtonContent,
            tonalElevation = 0.dp,
            shadowElevation = 0.dp,
            onClick = { showSignalPath = true },
            modifier = Modifier.weight(1.3f).height(if (isTranslucent) 44.dp else 48.dp)
                .liquidGlassChrome(RoundedCornerShape(24.dp), LocalLiquidGlass.current, LiquidGlassPreset.PlayerControls),
        ) {
            Row(
                Modifier.fillMaxSize().padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    Icons.Filled.HighQuality,
                    null,
                    tint = if (isTranslucent) Color.White.copy(alpha = 0.92f) else MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.size(17.dp),
                )
                Text(
                    qualityLabel(state) + if (signalPath.bitPerfect) " • " + stringResource(com.lastwave.app.R.string.signal_bit_perfect) else "",
                    style = if (isTranslucent) MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold, letterSpacing = 0.3.sp) else MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                    color = if (isTranslucent) Color.White.copy(alpha = 0.90f) else Color.Unspecified,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(start = 5.dp),
                )
            }
        }
        PlayerModeButton(
            active = state.repeatMode != Player.REPEAT_MODE_OFF,
            description = when (state.repeatMode) {
                Player.REPEAT_MODE_ONE -> "Repeat one"
                Player.REPEAT_MODE_ALL -> "Repeat all"
                else -> "Repeat off"
            },
            icon = if (state.repeatMode == Player.REPEAT_MODE_ONE) Icons.Filled.RepeatOne else Icons.Filled.Repeat,
            onClick = player::cycleRepeatMode,
            background = edgeButtonBackground,
            foreground = edgeButtonContent,
            iconSize = if (isTranslucent) 19.dp else 20.dp,
            modifier = Modifier.weight(1f).height(if (isTranslucent) 44.dp else 48.dp),
        )
    }
    Row(
        Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()).padding(top = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        val chipColors = FilterChipDefaults.filterChipColors(
            containerColor = Color(0xD90B0F1A),
            labelColor = Color.White,
            selectedContainerColor = Color(0xFF00F0FF).copy(alpha = 0.25f),
            selectedLabelColor = Color(0xFF00F0FF),
        )
        val chipBorder = FilterChipDefaults.filterChipBorder(
            enabled = true,
            selected = false,
            borderColor = Color(0xFF26304A),
            selectedBorderColor = Color(0xFF00F0FF),
        )

        val context = LocalContext.current
        var bassHapticsEnabled by remember { mutableStateOf(false) }
        FilterChip(
            selected = bassHapticsEnabled,
            onClick = {
                bassHapticsEnabled = !bassHapticsEnabled
                val driver = BassHapticDriver.getInstance(context)
                driver.enabled = bassHapticsEnabled
            },
            colors = chipColors,
            border = chipBorder,
            label = { Text("Bass Haptics", style = MaterialTheme.typography.labelSmall) },
        )

        val keyResult = remember(state.current?.title, state.current?.artist) {
            val title = state.current?.title ?: "Unknown"
            val artist = state.current?.artist ?: "Unknown"
            com.lastwave.app.playback.analysis.HarmonicKeyAnalyzer.getInstance().analyzeTrackKey(title, artist)
        }
        var showCamelotSheet by remember { mutableStateOf(false) }

        FilterChip(
            selected = showCamelotSheet,
            onClick = { showCamelotSheet = true },
            colors = chipColors,
            border = chipBorder,
            label = { Text("${keyResult.camelotKey} • ${keyResult.bpm} BPM", style = MaterialTheme.typography.labelSmall) },
        )

        val keyLockOn by player.keyLockEnabled.collectAsStateWithLifecycle()
        var pitchSemitones by remember { mutableFloatStateOf(0f) }
        var showPitchDialog by remember { mutableStateOf(false) }

        FilterChip(
            selected = pitchSemitones != 0f || keyLockOn,
            onClick = { showPitchDialog = true },
            colors = chipColors,
            border = chipBorder,
            label = {
                Text(
                    if (pitchSemitones == 0f) "Pitch: 0st" else "Pitch: ${if (pitchSemitones > 0) "+" else ""}${"%.1f".format(pitchSemitones)}st",
                    style = MaterialTheme.typography.labelSmall,
                )
            },
        )

        var spatialActive by remember { mutableStateOf(false) }
        var showSpatialPuckDialog by remember { mutableStateOf(false) }
        FilterChip(
            selected = spatialActive,
            onClick = {
                spatialActive = !spatialActive
                player.setSpatialAudio(spatialActive, 0.6f, 0.5f, 18.0f, 1.3f)
                if (spatialActive) showSpatialPuckDialog = true
            },
            colors = chipColors,
            border = chipBorder,
            label = { Text("3D Spatial", style = MaterialTheme.typography.labelSmall) },
        )

        if (showSpatialPuckDialog) {
            SpatialAudioPuckSheet(
                player = player,
                onDismiss = { showSpatialPuckDialog = false },
            )
        }

        var dolbyActive by remember { mutableStateOf(false) }
        FilterChip(
            selected = dolbyActive,
            onClick = {
                dolbyActive = !dolbyActive
                player.setSurroundVirtualizer(dolbyActive, 800)
            },
            colors = chipColors,
            border = chipBorder,
            label = { Text("Surround Virtualizer", style = MaterialTheme.typography.labelSmall) },
        )

        var lofiActive by remember { mutableStateOf(false) }
        FilterChip(
            selected = lofiActive,
            onClick = {
                lofiActive = !lofiActive
                player.setBitcrusher(lofiActive, 10, 4)
            },
            colors = chipColors,
            border = chipBorder,
            label = { Text("Lo-Fi", style = MaterialTheme.typography.labelSmall) },
        )

        var slowedActive by remember { mutableStateOf(false) }
        FilterChip(
            selected = slowedActive,
            onClick = {
                slowedActive = !slowedActive
                player.setSlowedReverb(slowedActive)
            },
            colors = chipColors,
            border = chipBorder,
            label = { Text("Slowed + Reverb", style = MaterialTheme.typography.labelSmall) },
        )



        var showOrbPhysicsSheet by remember { mutableStateOf(false) }
        FilterChip(
            selected = showOrbPhysicsSheet,
            onClick = { showOrbPhysicsSheet = true },
            colors = chipColors,
            border = chipBorder,
            label = { Text("Orb Physics", style = MaterialTheme.typography.labelSmall) },
        )

        var showCassetteDeck by remember { mutableStateOf(false) }
        FilterChip(
            selected = showCassetteDeck,
            onClick = { showCassetteDeck = true },
            colors = chipColors,
            border = chipBorder,
            label = { Text("Cassette Deck", style = MaterialTheme.typography.labelSmall) },
        )

        if (showOrbPhysicsSheet) {
            OrbPhysicsStudioSheet(
                isPlaying = state.isPlaying,
                onDismiss = { showOrbPhysicsSheet = false },
            )
        }

        if (showCassetteDeck) {
            SkeuomorphicCassetteDialog(
                trackTitle = state.current?.title ?: "Unknown Track",
                trackArtist = state.current?.artist ?: "Unknown Artist",
                isPlaying = state.isPlaying,
                playbackSpeed = state.speed,
                progressFraction = if (state.durationMs > 0) (state.positionMs.toFloat() / state.durationMs.toFloat()).coerceIn(0f, 1f) else 0.35f,
                onDismiss = { showCassetteDeck = false },
            )
        }

        var showLinguisticsSheet by remember { mutableStateOf(false) }
        FilterChip(
            selected = showLinguisticsSheet,
            onClick = { showLinguisticsSheet = true },
            colors = chipColors,
            border = chipBorder,
            label = { Text("Linguistics & Romaji", style = MaterialTheme.typography.labelSmall) },
        )

        if (showLinguisticsSheet) {
            LinguisticLyricsStudioSheet(
                trackTitle = state.current?.title ?: "夢の音響 (Yume no Onkyō)",
                onDismiss = { showLinguisticsSheet = false },
            )
        }

        if (showCamelotSheet) {
            ModalBottomSheet(
                onDismissRequest = { showCamelotSheet = false },
                containerColor = Color(0xFF0B0F1A),
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                ) {
                    Text(
                        text = "Camelot Wheel DJ Mix Key",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.ExtraBold,
                            brush = Brush.linearGradient(
                                colors = listOf(Color(0xFF00F0FF), Color(0xFFFF2E93), Color(0xFFFFC700)),
                            ),
                        ),
                    )
                    Text("Current Track: ${keyResult.camelotKey} (${keyResult.musicalKey}) @ ${keyResult.bpm} BPM", style = MaterialTheme.typography.bodyMedium, color = Color(0xFF00F0FF))
                    Text("Harmonically Compatible Transition Keys:", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = Color.White)

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                        keyResult.compatibleCamelotKeys.forEach { key ->
                            Surface(
                                shape = CircleShape,
                                color = Color(0xFF00F0FF).copy(alpha = 0.2f),
                                contentColor = Color(0xFF00F0FF),
                                border = BorderStroke(1.dp, Color(0xFF00F0FF)),
                            ) {
                                Text(key, style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold), modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp))
                            }
                        }
                    }

                    Text("• Same Code (${keyResult.camelotKey}): Perfect energy match\n• +1 / -1: Smooth energy shift (+1 boost, -1 drop)\n• Swap A/B: Relative Major/Minor harmonic shift", style = MaterialTheme.typography.bodySmall, color = Color(0xFF94A3B8))
                }
            }
        }
        if (showPitchDialog) {
            AlertDialog(
                onDismissRequest = { showPitchDialog = false },
                containerColor = Color(0xFF0B0F1A),
                title = {
                    Text(
                        text = "Key Lock & Pitch Tuning",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.ExtraBold,
                            brush = Brush.linearGradient(
                                colors = listOf(Color(0xFF00F0FF), Color(0xFFFF2E93), Color(0xFFFFC700)),
                            ),
                        ),
                    )
                },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Column(Modifier.weight(1f)) {
                                Text("WSOLA Key Lock", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Color.White)
                                Text("Preserve musical key when changing speed", style = MaterialTheme.typography.bodySmall, color = Color(0xFF94A3B8))
                            }
                            Switch(
                                checked = keyLockOn,
                                onCheckedChange = { player.toggleKeyLock() },
                            )
                        }
                        Box(Modifier.fillMaxWidth().height(1.dp).background(Color(0xFF26304A)))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            Text("Semitone Shift", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold, color = Color.White)
                            Text("${if (pitchSemitones > 0) "+" else ""}${"%.1f".format(pitchSemitones)} st", style = MaterialTheme.typography.bodyMedium, color = Color(0xFF00F0FF), fontWeight = FontWeight.Bold)
                        }
                        Slider(
                            value = pitchSemitones,
                            onValueChange = {
                                pitchSemitones = it
                                player.setPlaybackPitch(it)
                            },
                            valueRange = -12f..12f,
                            steps = 47,
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                            OutlinedButton(onClick = { pitchSemitones = 0f; player.setPlaybackPitch(0f) }, modifier = Modifier.weight(1f)) {
                                Text("Reset")
                            }
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = { showPitchDialog = false }) { Text("Done") }
                },
            )
        }
        val telemetry by player.deviceTelemetry.collectAsStateWithLifecycle()
        var showOutputDeviceDialog by remember { mutableStateOf(false) }
        FilterChip(
            selected = false,
            onClick = { showOutputDeviceDialog = true },
            label = { Text("Output: ${telemetry.deviceType} (${telemetry.sampleRate / 1000}kHz)", style = MaterialTheme.typography.labelSmall) },
        )
        if (showOutputDeviceDialog) {
            AlertDialog(
                onDismissRequest = { showOutputDeviceDialog = false },
                title = { Text("Audio Output Telemetry") },
                text = {
                    val ctx = LocalContext.current
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Connected Device: ${telemetry.deviceName}")
                        Text("Output Endpoint: ${telemetry.deviceType}")
                        Text("Active Codec: ${telemetry.codec}")
                        Text("Sample Rate: ${telemetry.sampleRate} Hz")
                        Text("Hardware Bit Depth: ${telemetry.bitDepth}-bit")
                        OutlinedButton(
                            onClick = {
                                android.widget.Toast.makeText(ctx, "Bound current EQ to ${telemetry.deviceName}", android.widget.Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Text("Bind Current EQ to This Device")
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = { showOutputDeviceDialog = false }) { Text("Done") }
                },
            )
        }
        var showOscilloscope by remember { mutableStateOf(false) }
        FilterChip(
            selected = showOscilloscope,
            onClick = { showOscilloscope = !showOscilloscope },
            colors = chipColors,
            border = chipBorder,
            label = { Text("Oscilloscope", style = MaterialTheme.typography.labelSmall) },
        )

        if (showOscilloscope) {
            OscilloscopeStudioSheet(
                isPlaying = state.isPlaying,
                onDismiss = { showOscilloscope = false },
            )
        }
    }
    if (showSignalPath) {
        val dac = usbDac.dac
        SignalPathDialog(
            report = signalPath,
            needsUsbPermission = dac != null && dac.hasUsbPeripheral && !dac.usbPermissionGranted,
            onRequestUsbAccess = player::requestUsbPermission,
            onDismiss = { showSignalPath = false },
        )
    }
}

@OptIn(androidx.compose.foundation.ExperimentalFoundationApi::class)
@Composable
private fun QueuePanel(state: MusicPlayerState, player: MusicPlayer, modifier: Modifier = Modifier) {
    val listState = rememberLazyListState()
    val haptics = LocalHapticFeedback.current
    val scope = rememberCoroutineScope()
    var draggingIndex by remember { mutableIntStateOf(-1) }
    var dragOffsetY by remember { mutableFloatStateOf(0f) }

    // Stable keys so animateItem() can animate moves instead of treating
    // every shifted row as a new item. Duplicates get occurrence suffixes.
    val queueKeys = remember(state.queue) {
        val counts = mutableMapOf<String, Int>()
        state.queue.map { item ->
            val base = item.videoId ?: item.playbackUrl
                ?: "${item.artist}|${item.title}|${item.album}"
            val n = counts.getOrDefault(base, 0)
            counts[base] = n + 1
            "$base#$n"
        }
    }
    // Abort a stale drag if the queue itself changes underneath us
    // (track ended, clear-upcoming, fresh playQueue, …).
    LaunchedEffect(state.queue.size) {
        if (draggingIndex >= state.queue.size) {
            draggingIndex = -1
            dragOffsetY = 0f
        }
    }

    Column(modifier) {
        Row(
            Modifier.fillMaxWidth().padding(vertical = 12.dp, horizontal = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(Modifier.weight(1f)) {
                Text("Up next", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Text(
                    if (state.isEndlessQueue) {
                        "Unlimited songs · ${state.currentIndex.coerceAtLeast(0) + 1} playing"
                    } else {
                        "${state.queue.size} songs · ${state.currentIndex.coerceAtLeast(0) + 1} playing"
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                if (state.queue.size > 1) {
                    Text(
                        stringResource(com.lastwave.app.R.string.queue_rearrange_hint),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                    )
                }
            }
            IconButton(
                onClick = player::clearUpcoming,
                enabled = state.currentIndex >= 0 && state.currentIndex + 1 < state.queue.size,
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.surfaceContainerHigh),
            ) {
                Icon(Icons.Filled.ClearAll, "Clear upcoming songs")
            }
        }
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            itemsIndexed(state.queue, key = { index, _ -> queueKeys.getOrNull(index) ?: index.toString() }) { index, item ->
                val isCurrent = index == state.currentIndex
                val isDragging = index == draggingIndex
                val stableKey = queueKeys.getOrNull(index) ?: index.toString()
                LiquidGlassSurface(
                    glassModifier = Modifier.liquidGlassChrome(RoundedCornerShape(20.dp), LocalLiquidGlass.current),
                    onClick = { player.seekToQueueItem(index) },
                    shape = RoundedCornerShape(20.dp),
                    color = liquidGlassContainerColor(
                        if (isCurrent) MaterialTheme.colorScheme.primaryContainer
                        else MaterialTheme.colorScheme.surfaceContainerLow.copy(alpha = 0.86f),
                    ),
                    contentColor = if (isCurrent) MaterialTheme.colorScheme.onPrimaryContainer
                    else MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .animateItem()
                        .zIndex(if (isDragging) 1f else 0f)
                        .graphicsLayer {
                            translationY = if (isDragging) dragOffsetY else 0f
                            shadowElevation = if (isDragging) 18f else 0f
                            val s = if (isDragging) 1.025f else 1f
                            scaleX = s
                            scaleY = s
                            alpha = if (isDragging) 0.96f else 1f
                        },
                ) {
                    Row(
                        Modifier.fillMaxWidth().padding(9.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        PlayerArtwork(item, Modifier.size(50.dp), 13.dp)
                        Column(Modifier.weight(1f).padding(horizontal = 12.dp)) {
                            Text(
                                item.title,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.SemiBold,
                            )
                            Text(
                                item.artist,
                                style = MaterialTheme.typography.bodySmall,
                                color = if (isCurrent) MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.76f)
                                else MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )
                        }
                        if (isCurrent) {
                            Icon(
                                Icons.Filled.GraphicEq,
                                "Currently playing",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(22.dp),
                            )
                        }
                        Icon(
                            Icons.Filled.DragHandle,
                            stringResource(com.lastwave.app.R.string.queue_drag_hint),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = if (isDragging) 1f else 0.6f),
                            modifier = Modifier
                                .padding(start = 4.dp)
                                .size(40.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(
                                    if (isDragging) MaterialTheme.colorScheme.primary.copy(alpha = 0.18f)
                                    else MaterialTheme.colorScheme.surfaceContainerHigh.copy(alpha = 0.6f),
                                )
                                .padding(8.dp)
                                .pointerInput(stableKey) {
                                    detectDragGesturesAfterLongPress(
                                        onDragStart = {
                                            draggingIndex = index
                                            dragOffsetY = 0f
                                            haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                                        },
                                        onDragEnd = {
                                            draggingIndex = -1
                                            dragOffsetY = 0f
                                        },
                                        onDragCancel = {
                                            draggingIndex = -1
                                            dragOffsetY = 0f
                                        },
                                        onDrag = { change, dragAmount ->
                                            change.consume()
                                            val source = draggingIndex
                                            if (source < 0) return@detectDragGesturesAfterLongPress
                                            dragOffsetY += dragAmount.y
                                            val layoutInfo = listState.layoutInfo
                                            val draggedInfo = layoutInfo.visibleItemsInfo
                                                .firstOrNull { it.index == source }
                                                ?: return@detectDragGesturesAfterLongPress
                                            val draggedCenter = draggedInfo.offset + draggedInfo.size / 2 + dragOffsetY.toInt()
                                            val target = layoutInfo.visibleItemsInfo.firstOrNull { info ->
                                                info.index != source &&
                                                    info.index in state.queue.indices &&
                                                    draggedCenter in info.offset..(info.offset + info.size)
                                            }?.index
                                            if (target != null && target != source) {
                                                player.moveQueueItem(source, target)
                                                // Keep the row glued under the finger across
                                                // the layout shift caused by the move.
                                                val targetInfo = layoutInfo.visibleItemsInfo
                                                    .firstOrNull { it.index == target }
                                                if (targetInfo != null) {
                                                    dragOffsetY += (draggedInfo.offset - targetInfo.offset).toFloat()
                                                }
                                                draggingIndex = target
                                                haptics.performHapticFeedback(HapticFeedbackType.SegmentFrequentTick)
                                            }
                                            // Edge auto-scroll while dragging.
                                            val viewportStart = layoutInfo.viewportStartOffset
                                            val viewportEnd = layoutInfo.viewportEndOffset
                                            val edgeZone = 180
                                            when {
                                                draggedCenter < viewportStart + edgeZone ->
                                                    scope.launch { listState.scrollBy(-28f) }
                                                draggedCenter > viewportEnd - edgeZone ->
                                                    scope.launch { listState.scrollBy(28f) }
                                            }
                                        },
                                    )
                                },
                        )
                        IconButton(
                            onClick = { player.removeQueueItem(index) },
                            modifier = Modifier
                                .padding(start = 4.dp)
                                .size(40.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(
                                    if (isCurrent) MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.08f)
                                    else MaterialTheme.colorScheme.surfaceContainerHigh,
                                ),
                        ) {
                            Icon(Icons.Filled.DeleteOutline, "Remove from queue", modifier = Modifier.size(20.dp))
                        }
                    }
                }
            }
            item { Spacer(Modifier.height(12.dp)) }
        }
    }
}

@Composable
private fun PlayerArtwork(
    track: PlayableTrack,
    modifier: Modifier,
    corner: androidx.compose.ui.unit.Dp,
    decodeSizePx: Int? = null,
    coverStyle: com.lastwave.app.data.local.PlayerCoverStyle = com.lastwave.app.data.local.PlayerCoverStyle.SQUARE_COVER,
    isPlaying: Boolean = false,
) {
    if (coverStyle == com.lastwave.app.data.local.PlayerCoverStyle.ROTATING_VINYL) {
        RotatingVinylArtwork(
            track = track,
            isPlaying = isPlaying,
            modifier = modifier,
        )
    } else {
        Box(modifier.clip(RoundedCornerShape(corner)).background(MaterialTheme.colorScheme.surfaceContainerHighest), contentAlignment = Alignment.Center) {
            ArtworkImage(
                name = track.title,
                artist = track.artist,
                embeddedUrl = track.artworkUrl,
                fallbackIcon = Icons.Filled.MusicNote,
                modifier = Modifier.fillMaxSize(),
                decodeSizePx = decodeSizePx,
            )
        }
    }
}

@Composable
private fun RotatingVinylArtwork(
    track: PlayableTrack,
    isPlaying: Boolean,
    modifier: Modifier = Modifier,
) {
    var rotationAngle by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(isPlaying) {
        if (!isPlaying) return@LaunchedEffect
        var lastTimeNanos = withFrameNanos { it }
        while (isActive && isPlaying) {
            withFrameNanos { currentTimeNanos ->
                val deltaSeconds = (currentTimeNanos - lastTimeNanos) / 1_000_000_000f
                lastTimeNanos = currentTimeNanos
                rotationAngle = (rotationAngle + deltaSeconds * 200f) % 360f
            }
        }
    }

    Box(
        modifier = modifier
            .aspectRatio(1f)
            .background(Color.Transparent)
            .graphicsLayer { rotationZ = rotationAngle },
        contentAlignment = Alignment.Center,
    ) {
        Surface(
            shape = CircleShape,
            color = Color(0xFF121212),
            border = BorderStroke(1.dp, Color(0xFF2E2E2E)),
            shadowElevation = 0.dp,
            modifier = Modifier.fillMaxSize(),
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val radius = size.minDimension / 2f
                val center = Offset(size.width / 2f, size.height / 2f)
                for (r in listOf(0.92f, 0.84f, 0.76f, 0.68f, 0.60f, 0.52f)) {
                    drawCircle(
                        color = Color.White.copy(alpha = 0.08f),
                        radius = radius * r,
                        center = center,
                        style = Stroke(width = 1.5.dp.toPx()),
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize(0.48f)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceContainerHighest),
            contentAlignment = Alignment.Center,
        ) {
            ArtworkImage(
                name = track.title,
                artist = track.artist,
                embeddedUrl = track.artworkUrl,
                fallbackIcon = Icons.Filled.MusicNote,
                modifier = Modifier.fillMaxSize(),
            )
        }

        Box(
            modifier = Modifier
                .fillMaxSize(0.08f)
                .clip(CircleShape)
                .background(Color.Black)
                .border(1.dp, Color(0xFF3D3D3D), CircleShape),
        )
    }
}

internal fun formatTime(ms: Long): String {
    val total = (ms.coerceAtLeast(0) / 1000)
    return "%d:%02d".format(total / 60, total % 60)
}

private fun qualityLabel(state: MusicPlayerState): String = when {
    // Lossless with known bit depth / sampling rate → show precise format
    state.isLossless && state.bitDepth != null && state.samplingRateKHz != null -> {
        val rate = if (state.samplingRateKHz % 1.0 == 0.0) state.samplingRateKHz.toInt().toString()
        else state.samplingRateKHz.toString()
        "FLAC ${state.bitDepth}/$rate"
    }
    state.isLossless && state.audioCodec == "MP3 320k" -> "MP3 320k"
    state.isLossless -> state.audioCodec ?: "LOSSLESS"
    // YouTube with known codec + bitrate → e.g. "OPUS 160k" or "AAC 256k"
    state.audioCodec != null && state.bitrateKbps != null -> "${state.audioCodec} ${state.bitrateKbps}k"
    state.audioCodec != null -> state.audioCodec
    state.bitrateKbps != null -> "${state.bitrateKbps} kbps"
    else -> "AUDIO"
}

private fun PlayableTrack.toGeneratedTrack() = com.lastwave.app.data.generate.GeneratedTrack(
    name = title,
    artist = artist,
    artworkUrl = artworkUrl,
    album = album,
    url = videoId?.let { "https://music.youtube.com/watch?v=$it" }.orEmpty(),
)



@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SpatialAudioPuckSheet(
    player: MusicPlayer,
    onDismiss: () -> Unit,
) {
    var roomSize by remember { mutableFloatStateOf(0.6f) }
    var azimuthAngle by remember { mutableFloatStateOf(0.5f) }
    var elevationAngle by remember { mutableFloatStateOf(18.0f) }
    var headTrackingGain by remember { mutableFloatStateOf(1.3f) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF0B0F1A),
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Canvas(modifier = Modifier.matchParentSize()) {
                drawCircle(
                    color = Color(0xFF00F0FF).copy(alpha = 0.10f),
                    radius = size.minDimension * 0.65f,
                    center = androidx.compose.ui.geometry.Offset(size.width * 0.8f, size.height * 0.2f),
                )
                drawCircle(
                    color = Color(0xFFFF2E93).copy(alpha = 0.08f),
                    radius = size.minDimension * 0.55f,
                    center = androidx.compose.ui.geometry.Offset(size.width * 0.2f, size.height * 0.8f),
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Text(
                    text = "3D Soundstage & Spatial Radar Puck",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.ExtraBold,
                        brush = Brush.linearGradient(
                            colors = listOf(Color(0xFF00F0FF), Color(0xFFFF2E93), Color(0xFFFFC700)),
                        ),
                    ),
                )
                Text(
                    text = "Interactive HRTF 3D spatial acoustic positioning & room reverberation radar",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF94A3B8),
                )

                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = Color(0xFF070A12),
                    border = BorderStroke(1.dp, Color(0xFF00F0FF).copy(alpha = 0.4f)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp),
                ) {
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                        Canvas(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp)
                                .pointerInput(Unit) {
                                    detectDragGestures { change, _ ->
                                        change.consume()
                                        val centerPtX = size.width * 0.5f
                                        val centerPtY = size.height * 0.5f
                                        val radarRadius = (size.width.coerceAtMost(size.height) * 0.5f) * 0.42f
                                        val dx = change.position.x - centerPtX
                                        val dy = change.position.y - centerPtY
                                        val dist = kotlin.math.sqrt(dx * dx + dy * dy)
                                        val newRoom = (dist / radarRadius.coerceAtLeast(1f)).coerceIn(0.1f, 1.5f)
                                        val angleRad = kotlin.math.atan2(dx.toDouble(), -dy.toDouble()).toFloat()
                                        val newAzimuth = ((angleRad / (Math.PI.toFloat() * 1.5f)) + 0.5f).coerceIn(0.0f, 1.0f)
                                        roomSize = newRoom
                                        azimuthAngle = newAzimuth
                                        player.setSpatialAudio(true, roomSize, azimuthAngle, elevationAngle, headTrackingGain)
                                    }
                                },
                        ) {
                            val centerPt = androidx.compose.ui.geometry.Offset(size.width * 0.5f, size.height * 0.5f)
                            val radarRadius = size.minDimension * 0.42f

                            drawCircle(color = Color(0xFF00F0FF).copy(alpha = 0.12f), radius = radarRadius, center = centerPt)
                            drawCircle(color = Color(0xFF00F0FF).copy(alpha = 0.25f), radius = radarRadius * 0.66f, center = centerPt)
                            drawCircle(color = Color(0xFF00F0FF).copy(alpha = 0.40f), radius = radarRadius * 0.33f, center = centerPt)

                            drawLine(color = Color(0xFF00F0FF).copy(alpha = 0.3f), start = androidx.compose.ui.geometry.Offset(centerPt.x - radarRadius, centerPt.y), end = androidx.compose.ui.geometry.Offset(centerPt.x + radarRadius, centerPt.y), strokeWidth = 1.5f)
                            drawLine(color = Color(0xFF00F0FF).copy(alpha = 0.3f), start = androidx.compose.ui.geometry.Offset(centerPt.x, centerPt.y - radarRadius), end = androidx.compose.ui.geometry.Offset(centerPt.x, centerPt.y + radarRadius), strokeWidth = 1.5f)

                            val angleRad = (azimuthAngle - 0.5f) * Math.PI.toFloat() * 1.5f
                            val distance = radarRadius * roomSize.coerceIn(0.2f, 1.0f)
                            val puckX = centerPt.x + kotlin.math.sin(angleRad.toDouble()).toFloat() * distance
                            val puckY = centerPt.y - kotlin.math.cos(angleRad.toDouble()).toFloat() * distance
                            val puckOffset = androidx.compose.ui.geometry.Offset(puckX, puckY)

                            drawLine(color = Color(0xFFFF2E93), start = centerPt, end = puckOffset, strokeWidth = 3f)
                            drawCircle(color = Color.White, radius = 8f, center = centerPt)
                            drawCircle(color = Color(0xFF00F0FF), radius = 14f, center = puckOffset)
                            drawCircle(color = Color(0xFFFF2E93), radius = 6f, center = puckOffset)
                        }
                    }
                }

                Text("Acoustic Room Volume Size: ${"%.2f".format(roomSize)}", style = MaterialTheme.typography.labelSmall, color = Color(0xFF00F0FF))
                Slider(
                    value = roomSize,
                    onValueChange = {
                        roomSize = it
                        player.setSpatialAudio(true, roomSize, azimuthAngle, elevationAngle, headTrackingGain)
                    },
                    valueRange = 0.1f..1.5f,
                )

                Text("Azimuth Orientation Angle: ${((azimuthAngle - 0.5f) * 180f).toInt()}°", style = MaterialTheme.typography.labelSmall, color = Color(0xFFFF2E93))
                Slider(
                    value = azimuthAngle,
                    onValueChange = {
                        azimuthAngle = it
                        player.setSpatialAudio(true, roomSize, azimuthAngle, elevationAngle, headTrackingGain)
                    },
                    valueRange = 0.0f..1.0f,
                )

                Text("Acoustic Elevation Angle: ${elevationAngle.toInt()}°", style = MaterialTheme.typography.labelSmall, color = Color(0xFFFFC700))
                Slider(
                    value = elevationAngle,
                    onValueChange = {
                        elevationAngle = it
                        player.setSpatialAudio(true, roomSize, azimuthAngle, elevationAngle, headTrackingGain)
                    },
                    valueRange = -30f..60f,
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun OscilloscopeStudioSheet(
    isPlaying: Boolean,
    onDismiss: () -> Unit,
) {
    val infiniteTransition = rememberInfiniteTransition(label = "scope_anim")
    val phaseCycle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "phase_angle",
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF0B0F1A),
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Canvas(modifier = Modifier.matchParentSize()) {
                drawCircle(
                    color = Color(0xFF00F0FF).copy(alpha = 0.10f),
                    radius = size.minDimension * 0.65f,
                    center = androidx.compose.ui.geometry.Offset(size.width * 0.15f, size.height * 0.2f),
                )
                drawCircle(
                    color = Color(0xFFFF2E93).copy(alpha = 0.08f),
                    radius = size.minDimension * 0.55f,
                    center = androidx.compose.ui.geometry.Offset(size.width * 0.85f, size.height * 0.8f),
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Text(
                    text = "Real-Time Stereo Vectorscope & Oscilloscope",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.ExtraBold,
                        brush = Brush.linearGradient(
                            colors = listOf(Color(0xFF00F0FF), Color(0xFFFF2E93), Color(0xFFFFC700)),
                        ),
                    ),
                )
                Text(
                    text = "Live Lissajous XY stereo phase scope & time-domain acoustic waveform display",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF94A3B8),
                )

                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = Color(0xFF070A12),
                    border = BorderStroke(1.dp, Color(0xFF00F0FF).copy(alpha = 0.4f)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                ) {
                    Canvas(modifier = Modifier.fillMaxSize().padding(12.dp)) {
                        val cx = size.width * 0.5f
                        val cy = size.height * 0.5f
                        val scopeRadius = size.minDimension * 0.4f

                        drawCircle(color = Color(0xFF00F0FF).copy(alpha = 0.15f), radius = scopeRadius, center = androidx.compose.ui.geometry.Offset(cx, cy))
                        drawCircle(color = Color(0xFF00F0FF).copy(alpha = 0.30f), radius = scopeRadius * 0.5f, center = androidx.compose.ui.geometry.Offset(cx, cy))
                        drawLine(color = Color(0xFF00F0FF).copy(alpha = 0.2f), start = androidx.compose.ui.geometry.Offset(cx - scopeRadius, cy), end = androidx.compose.ui.geometry.Offset(cx + scopeRadius, cy), strokeWidth = 1f)
                        drawLine(color = Color(0xFF00F0FF).copy(alpha = 0.2f), start = androidx.compose.ui.geometry.Offset(cx, cy - scopeRadius), end = androidx.compose.ui.geometry.Offset(cx, cy + scopeRadius), strokeWidth = 1f)

                        if (isPlaying) {
                            val points = 120
                            val radBase = Math.toRadians(phaseCycle.toDouble())
                            for (i in 0 until points) {
                                val t = (i.toFloat() / points) * Math.PI.toFloat() * 4f
                                val x = cx + scopeRadius * 0.8f * kotlin.math.sin((t + radBase).toDouble()).toFloat() * kotlin.math.cos((t * 0.5f).toDouble()).toFloat()
                                val y = cy + scopeRadius * 0.8f * kotlin.math.sin((t * 1.5f + radBase * 0.5f).toDouble()).toFloat()
                                drawCircle(
                                    color = if (i % 2 == 0) Color(0xFF00F0FF) else Color(0xFFFF2E93),
                                    radius = 2.5f,
                                    center = androidx.compose.ui.geometry.Offset(x, y),
                                )
                            }
                        } else {
                            drawLine(color = Color(0xFF00F0FF).copy(alpha = 0.5f), start = androidx.compose.ui.geometry.Offset(cx - scopeRadius * 0.8f, cy), end = androidx.compose.ui.geometry.Offset(cx + scopeRadius * 0.8f, cy), strokeWidth = 2f)
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text("Phase Scope Mode: 2D Lissajous XY", style = MaterialTheme.typography.labelSmall, color = Color(0xFF00F0FF), fontWeight = FontWeight.Bold)
                    Text("Sampling: 96 kHz / 32-Bit", style = MaterialTheme.typography.labelSmall, color = Color(0xFFFFC700))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun OrbPhysicsStudioSheet(
    isPlaying: Boolean,
    onDismiss: () -> Unit,
) {
    var particleCount by remember { mutableFloatStateOf(1200f) }
    var spinSpeed by remember { mutableFloatStateOf(1.0f) }
    var gravityPull by remember { mutableFloatStateOf(0.8f) }
    var particleJitter by remember { mutableFloatStateOf(0.35f) }
    var decayRate by remember { mutableFloatStateOf(0.6f) }
    var bloomIntensity by remember { mutableFloatStateOf(0.7f) }
    var motionBlur by remember { mutableFloatStateOf(0.4f) }
    var paletteIndex by remember { mutableIntStateOf(0) }

    val palettes = listOf(
        "Cyan / Gold" to listOf(Color(0xFF00E5FF), Color(0xFF7F52FF), Color(0xFFFFD700)),
        "Crimson Bass" to listOf(Color(0xFFFF1744), Color(0xFFFF9100), Color(0xFFFFEA00)),
        "Matrix Emerald" to listOf(Color(0xFF00E676), Color(0xFF00B0FF), Color(0xFF1DE9B6)),
        "Solar Nebula" to listOf(Color(0xFFD500F9), Color(0xFF651FFF), Color(0xFF00E5FF)),
    )
    val activeColors = palettes[paletteIndex].second

    val infiniteTransition = rememberInfiniteTransition(label = "orb_spin")
    val phase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = (2f * Math.PI).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = (4000 / spinSpeed.coerceAtLeast(0.2f)).toInt(), easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "orb_phase",
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF0B0F1A),
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Canvas(modifier = Modifier.matchParentSize()) {
                drawCircle(
                    color = Color(0xFF00F0FF).copy(alpha = 0.10f),
                    radius = size.minDimension * 0.65f,
                    center = androidx.compose.ui.geometry.Offset(size.width * 0.85f, size.height * 0.2f),
                )
                drawCircle(
                    color = Color(0xFFFF2E93).copy(alpha = 0.08f),
                    radius = size.minDimension * 0.55f,
                    center = androidx.compose.ui.geometry.Offset(size.width * 0.15f, size.height * 0.8f),
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Text(
                    text = "Orb Visualizer & Shader Physics",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.ExtraBold,
                        brush = Brush.linearGradient(
                            colors = listOf(Color(0xFF00F0FF), Color(0xFFFF2E93), Color(0xFFFFC700)),
                        ),
                    ),
                )
                Text("Real-time 3D particle density, orbital attractors & frequency color zones", style = MaterialTheme.typography.bodySmall, color = Color(0xFF94A3B8))

                Canvas(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFF07090E)),
                ) {
                    val center = androidx.compose.ui.geometry.Offset(size.width / 2f, size.height / 2f)
                    val maxRadius = size.minDimension * 0.42f * (1.2f - gravityPull * 0.3f)
                    val renderedPoints = (particleCount.toInt() / 8).coerceIn(40, 625)

                    for (i in 0 until renderedPoints) {
                        val norm = i.toFloat() / renderedPoints.toFloat()
                        val goldenAngle = i * 2.3999632f + (if (isPlaying) phase else 0f)
                        val r = kotlin.math.sqrt(norm) * maxRadius
                        val jitterOffset = kotlin.math.sin(phase * 3f + i) * particleJitter * 12f
                        val x = center.x + kotlin.math.cos(goldenAngle) * (r + jitterOffset)
                        val y = center.y + kotlin.math.sin(goldenAngle) * (r + jitterOffset)

                        val zoneColor = when {
                            norm < 0.33f -> activeColors[0]
                            norm < 0.66f -> activeColors[1]
                            else -> activeColors[2]
                        }

                        if (bloomIntensity > 0.1f) {
                            drawCircle(
                                color = zoneColor.copy(alpha = (0.18f * bloomIntensity).coerceIn(0f, 0.5f)),
                                radius = 6f + bloomIntensity * 8f,
                                center = androidx.compose.ui.geometry.Offset(x, y),
                            )
                        }
                        drawCircle(
                            color = zoneColor.copy(alpha = (0.85f - motionBlur * 0.3f).coerceIn(0.3f, 1f)),
                            radius = 2.2f,
                            center = androidx.compose.ui.geometry.Offset(x, y),
                        )
                    }
                }

                Text("Frequency Zone Palette (Bass • Mid • Treble)", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = Color.White)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                    palettes.forEachIndexed { idx, pair ->
                        FilterChip(
                            selected = paletteIndex == idx,
                            onClick = { paletteIndex = idx },
                            label = { Text(pair.first, style = MaterialTheme.typography.labelSmall) },
                            modifier = Modifier.weight(1f),
                        )
                    }
                }

                Text("Particle Density: ${particleCount.toInt()} pts", style = MaterialTheme.typography.labelSmall, color = Color.White)
                Slider(value = particleCount, onValueChange = { particleCount = it }, valueRange = 200f..5000f)

                Text("Orbital Spin Speed: ${"%.2f".format(spinSpeed)}x", style = MaterialTheme.typography.labelSmall, color = Color.White)
                Slider(value = spinSpeed, onValueChange = { spinSpeed = it }, valueRange = 0.1f..3.0f)

                Text("Gravity Pull & Attractor: ${"%.2f".format(gravityPull)} G", style = MaterialTheme.typography.labelSmall, color = Color.White)
                Slider(value = gravityPull, onValueChange = { gravityPull = it }, valueRange = 0.0f..2.0f)

                Text("Particle Jitter & Decay: ${(particleJitter * 100).toInt()}% • ${(decayRate * 100).toInt()}%", style = MaterialTheme.typography.labelSmall, color = Color.White)
                Slider(
                    value = particleJitter,
                    onValueChange = {
                        particleJitter = it
                        decayRate = (1f - it * 0.5f).coerceIn(0.1f, 1f)
                    },
                    valueRange = 0.0f..1.0f,
                )

                Text("Bloom Glow Radiance: ${(bloomIntensity * 100).toInt()}%", style = MaterialTheme.typography.labelSmall, color = Color.White)
                Slider(value = bloomIntensity, onValueChange = { bloomIntensity = it }, valueRange = 0.0f..1.0f)

                Text("Camera Motion Blur: ${(motionBlur * 100).toInt()}%", style = MaterialTheme.typography.labelSmall, color = Color.White)
                Slider(value = motionBlur, onValueChange = { motionBlur = it }, valueRange = 0.0f..1.0f)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SkeuomorphicCassetteDialog(
    trackTitle: String,
    trackArtist: String,
    isPlaying: Boolean,
    playbackSpeed: Float,
    progressFraction: Float,
    onDismiss: () -> Unit,
) {
    var analogWarmthDial by remember { mutableFloatStateOf(0.65f) }
    var azimuthDial by remember { mutableFloatStateOf(0.5f) }

    val infiniteTransition = rememberInfiniteTransition(label = "cassette_reels")
    val reelAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = (2400 / playbackSpeed.coerceAtLeast(0.5f)).toInt(), easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "reel_rotation",
    )
    val effectiveAngle = if (isPlaying) reelAngle else 0f

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF0B0F1A),
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Canvas(modifier = Modifier.matchParentSize()) {
                drawCircle(
                    color = Color(0xFF00F0FF).copy(alpha = 0.10f),
                    radius = size.minDimension * 0.65f,
                    center = androidx.compose.ui.geometry.Offset(size.width * 0.2f, size.height * 0.2f),
                )
                drawCircle(
                    color = Color(0xFFFF2E93).copy(alpha = 0.08f),
                    radius = size.minDimension * 0.55f,
                    center = androidx.compose.ui.geometry.Offset(size.width * 0.85f, size.height * 0.8f),
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Text(
                    text = "OreoTunes Vintage Cassette Deck",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.ExtraBold,
                        brush = Brush.linearGradient(
                            colors = listOf(Color(0xFF00F0FF), Color(0xFFFF2E93), Color(0xFFFFC700)),
                        ),
                    ),
                )
                Text("Type IV Metal Bias • Synchronous Dual-Spool Transport", style = MaterialTheme.typography.labelSmall, color = Color(0xFF9E9E9E))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(210.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(Color(0xFF22252A))
                    .border(2.dp, Color(0xFF3E434C), RoundedCornerShape(18.dp))
                    .padding(16.dp),
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Surface(
                        color = Color(0xFFF5F0E6),
                        shape = RoundedCornerShape(6.dp),
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Column(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Text(trackTitle, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold, color = Color(0xFF1A1A1A), maxLines = 1)
                            Text("$trackArtist • ${"%.2f".format(playbackSpeed)}x IPS", style = MaterialTheme.typography.labelSmall, color = Color(0xFF555555), maxLines = 1)
                        }
                    }

                    Canvas(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(105.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFF111315)),
                    ) {
                        val leftCenter = androidx.compose.ui.geometry.Offset(size.width * 0.28f, size.height * 0.5f)
                        val rightCenter = androidx.compose.ui.geometry.Offset(size.width * 0.72f, size.height * 0.5f)
                        val maxTapeRadius = size.height * 0.44f
                        val minCoreRadius = size.height * 0.18f

                        val leftTapeRadius = minCoreRadius + (maxTapeRadius - minCoreRadius) * kotlin.math.sqrt(1f - progressFraction)
                        val rightTapeRadius = minCoreRadius + (maxTapeRadius - minCoreRadius) * kotlin.math.sqrt(progressFraction)

                        drawCircle(color = Color(0xFF2B1D16), radius = leftTapeRadius, center = leftCenter)
                        drawCircle(color = Color(0xFF2B1D16), radius = rightTapeRadius, center = rightCenter)

                        drawCircle(color = Color(0xFFDADCE0), radius = minCoreRadius, center = leftCenter)
                        drawCircle(color = Color(0xFFDADCE0), radius = minCoreRadius, center = rightCenter)

                        listOf(leftCenter, rightCenter).forEach { hub ->
                            for (tooth in 0 until 6) {
                                val rad = Math.toRadians((effectiveAngle + tooth * 60f).toDouble())
                                val startX = hub.x + kotlin.math.cos(rad).toFloat() * (minCoreRadius * 0.35f)
                                val startY = hub.y + kotlin.math.sin(rad).toFloat() * (minCoreRadius * 0.35f)
                                val endX = hub.x + kotlin.math.cos(rad).toFloat() * (minCoreRadius * 0.9f)
                                val endY = hub.y + kotlin.math.sin(rad).toFloat() * (minCoreRadius * 0.9f)
                                drawLine(
                                    color = Color(0xFF111315),
                                    start = androidx.compose.ui.geometry.Offset(startX, startY),
                                    end = androidx.compose.ui.geometry.Offset(endX, endY),
                                    strokeWidth = 3.5f,
                                )
                            }
                        }
                    }

                    Text("ANANTA DHVANI • HI-BIAS CrO2 90", style = MaterialTheme.typography.labelSmall, color = Color(0xFF00E5FF))
                }
            }

            Text("Analog Bias Warmth: ${(analogWarmthDial * 100).toInt()}%", style = MaterialTheme.typography.labelSmall, color = Color.White)
            Slider(value = analogWarmthDial, onValueChange = { analogWarmthDial = it }, valueRange = 0f..1f)

            Text("Tape Head Azimuth Calibration: ${"%.2f".format(azimuthDial)}", style = MaterialTheme.typography.labelSmall, color = Color.White)
            Slider(value = azimuthDial, onValueChange = { azimuthDial = it }, valueRange = 0f..1f)
        }
    }
}
}

private data class KanjiLexiconEntry(
    val surface: String,
    val readingKana: String,
    val romaji: String,
    val jlptLevel: String,
    val pitchPattern: String,
    val moraHeights: List<Boolean>,
    val definition: String,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LinguisticLyricsStudioSheet(
    trackTitle: String,
    onDismiss: () -> Unit,
) {
    var furiganaEnabled by remember { mutableStateOf(true) }
    var romajiEnabled by remember { mutableStateOf(true) }
    var pitchAccentEnabled by remember { mutableStateOf(true) }
    var dualTranslationEnabled by remember { mutableStateOf(true) }
    var selectedLatinFontIdx by remember { mutableIntStateOf(0) }
    var selectedCjkFontIdx by remember { mutableIntStateOf(0) }
    var selectedWordIndex by remember { mutableIntStateOf(0) }

    val latinFonts = listOf(
        "Geometric Sans" to androidx.compose.ui.text.font.FontFamily.SansSerif,
        "Editorial Serif" to androidx.compose.ui.text.font.FontFamily.Serif,
        "Studio Mono" to androidx.compose.ui.text.font.FontFamily.Monospace,
    )
    val cjkFonts = listOf(
        "Gothic CJK" to androidx.compose.ui.text.font.FontFamily.SansSerif,
        "Mincho Serif" to androidx.compose.ui.text.font.FontFamily.Serif,
        "Fixed Kana" to androidx.compose.ui.text.font.FontFamily.Monospace,
    )

    val lexicon = remember {
        listOf(
            KanjiLexiconEntry(
                surface = "永遠",
                readingKana = "えいえん",
                romaji = "eien",
                jlptLevel = "JLPT N3",
                pitchPattern = "[0] Heiban (平板)",
                moraHeights = listOf(false, true, true, true),
                definition = "Eternity, perpetuity, immortality, permanence.",
            ),
            KanjiLexiconEntry(
                surface = "響き",
                readingKana = "ひびき",
                romaji = "hibiki",
                jlptLevel = "JLPT N2",
                pitchPattern = "[3] Odaka (尾高)",
                moraHeights = listOf(false, true, true),
                definition = "Echo, reverberation, acoustic resonance, sound.",
            ),
            KanjiLexiconEntry(
                surface = "夜空",
                readingKana = "よぞら",
                romaji = "yozora",
                jlptLevel = "JLPT N3",
                pitchPattern = "[1] Atamadaka (頭高)",
                moraHeights = listOf(true, false, false),
                definition = "Night sky, starlit firmament.",
            ),
            KanjiLexiconEntry(
                surface = "旋律",
                readingKana = "せんりつ",
                romaji = "senritsu",
                jlptLevel = "JLPT N1",
                pitchPattern = "[0] Heiban (平板)",
                moraHeights = listOf(false, true, true, true),
                definition = "Melody, tune, harmonic progression.",
            ),
            KanjiLexiconEntry(
                surface = "心",
                readingKana = "こころ",
                romaji = "kokoro",
                jlptLevel = "JLPT N4",
                pitchPattern = "[2] Nakadaka (中高)",
                moraHeights = listOf(false, true, false),
                definition = "Heart, mind, spirit, inner acoustic emotion.",
            ),
        )
    }

    val activeWord = lexicon[selectedWordIndex]

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF0B0F1A),
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Canvas(modifier = Modifier.matchParentSize()) {
                drawCircle(
                    color = Color(0xFF00F0FF).copy(alpha = 0.10f),
                    radius = size.minDimension * 0.6f,
                    center = androidx.compose.ui.geometry.Offset(size.width * 0.85f, size.height * 0.18f),
                )
                drawCircle(
                    color = Color(0xFFFF2E93).copy(alpha = 0.08f),
                    radius = size.minDimension * 0.55f,
                    center = androidx.compose.ui.geometry.Offset(size.width * 0.15f, size.height * 0.78f),
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
            ) {
                Text(
                    text = "Linguistics, Furigana & Romaji Studio",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontFamily = androidx.compose.ui.text.font.FontFamily.Serif,
                        fontWeight = FontWeight.ExtraBold,
                        brush = Brush.linearGradient(
                            colors = listOf(Color(0xFF00F0FF), Color(0xFFFF2E93), Color(0xFFFFC700)),
                        ),
                    ),
                )
                Text(
                    text = "Furigana Ruby readings, Hepburn Romaji, Tokyo pitch accent contours & Kanji dictionary",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF94A3B8),
                )

                Surface(
                    shape = RoundedCornerShape(18.dp),
                    color = Color(0xFF070A12),
                    border = BorderStroke(
                        width = 1.dp,
                        brush = Brush.linearGradient(
                            colors = listOf(Color(0xFF00F0FF).copy(alpha = 0.5f), Color(0xFFFF2E93).copy(alpha = 0.4f)),
                        ),
                    ),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        Text(
                            text = "ACTIVE LYRIC STREAM • ${trackTitle.uppercase()}",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color(0xFF00F0FF),
                            fontWeight = FontWeight.Bold,
                        )

                        if (pitchAccentEnabled) {
                            Canvas(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(34.dp)
                                    .padding(horizontal = 16.dp),
                            ) {
                                val heights = activeWord.moraHeights
                                val stepX = size.width / (heights.size.coerceAtLeast(1))
                                val highY = size.height * 0.22f
                                val lowY = size.height * 0.78f

                                for (i in heights.indices) {
                                    val x = stepX * i + stepX * 0.5f
                                    val y = if (heights[i]) highY else lowY
                                    if (i < heights.size - 1) {
                                        val nextX = stepX * (i + 1) + stepX * 0.5f
                                        val nextY = if (heights[i + 1]) highY else lowY
                                        drawLine(
                                            color = Color(0xFFFFC700),
                                            start = androidx.compose.ui.geometry.Offset(x, y),
                                            end = androidx.compose.ui.geometry.Offset(nextX, nextY),
                                            strokeWidth = 3f,
                                        )
                                    }
                                    drawCircle(
                                        color = if (heights[i]) Color(0xFF00F0FF) else Color(0xFFFF2E93),
                                        radius = 5f,
                                        center = androidx.compose.ui.geometry.Offset(x, y),
                                    )
                                }
                            }
                        }

                        // 27A: Furigana Ruby Line Above Kanji
                        if (furiganaEnabled) {
                            Text(
                                text = "えいえん の ひびき が よぞら に ひろがる",
                                style = MaterialTheme.typography.labelSmall,
                                fontFamily = cjkFonts[selectedCjkFontIdx].second,
                                color = Color(0xFFFFC700),
                                fontWeight = FontWeight.SemiBold,
                            )
                        }

                        // Primary CJK Line
                        Text(
                            text = "永遠の響きが夜空に広がる",
                            style = MaterialTheme.typography.titleMedium,
                            fontFamily = cjkFonts[selectedCjkFontIdx].second,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White,
                        )

                        // 26A: Romaji Subtitle Line
                        if (romajiEnabled) {
                            Text(
                                text = "eien no hibiki ga yozora ni hirogaru",
                                style = MaterialTheme.typography.bodyMedium,
                                fontFamily = latinFonts[selectedLatinFontIdx].second,
                                color = Color(0xFF00F0FF),
                            )
                        }

                        // 30A: Simultaneous Dual-Language Translation
                        if (dualTranslationEnabled) {
                            Text(
                                text = "An eternal resonance spreads across the night sky",
                                style = MaterialTheme.typography.bodySmall,
                                fontFamily = latinFonts[selectedLatinFontIdx].second,
                                color = Color(0xFFE2E8F0),
                            )
                        }
                    }
                }

                // 28A: Interactive Kanji Dictionary Lookup
                Text("Interactive Kanji Dictionary Lookup (Tap token)", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = Color.White)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                    lexicon.forEachIndexed { idx, entry ->
                        FilterChip(
                            selected = selectedWordIndex == idx,
                            onClick = { selectedWordIndex = idx },
                            label = {
                                Text(
                                    text = entry.surface,
                                    fontFamily = cjkFonts[selectedCjkFontIdx].second,
                                    fontWeight = FontWeight.Bold,
                                )
                            },
                            modifier = Modifier.weight(1f),
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = Color(0xFF13192B),
                    border = BorderStroke(1.dp, Color(0xFF26304A)),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                text = "${activeWord.surface} 【${activeWord.readingKana} •${activeWord.romaji}】",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                fontFamily = cjkFonts[selectedCjkFontIdx].second,
                            )
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = Color(0xFF00F0FF).copy(alpha = 0.2f),
                            ) {
                                Text(
                                    text = activeWord.jlptLevel,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color(0xFF00F0FF),
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                )
                            }
                        }
                        Text(
                            text = "Tokyo Pitch Accent: ${activeWord.pitchPattern}",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color(0xFFFFC700),
                        )
                        Text(
                            text = activeWord.definition,
                            style = MaterialTheme.typography.bodySmall,
                            fontFamily = latinFonts[selectedLatinFontIdx].second,
                            color = Color(0xFFCBD5E1),
                        )
                    }
                }

                // 31A: Dual-Script Font Picker
                Text("Latin Script Typeface", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = Color.White)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                    latinFonts.forEachIndexed { idx, pair ->
                        FilterChip(
                            selected = selectedLatinFontIdx == idx,
                            onClick = { selectedLatinFontIdx = idx },
                            label = { Text(pair.first, style = MaterialTheme.typography.labelSmall) },
                            modifier = Modifier.weight(1f),
                        )
                    }
                }

                Text("East Asian (CJK) Script Typeface", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = Color.White)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                    cjkFonts.forEachIndexed { idx, pair ->
                        FilterChip(
                            selected = selectedCjkFontIdx == idx,
                            onClick = { selectedCjkFontIdx = idx },
                            label = { Text(pair.first, style = MaterialTheme.typography.labelSmall) },
                            modifier = Modifier.weight(1f),
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text("Furigana Ruby Readings Above Kanji", style = MaterialTheme.typography.bodyMedium, color = Color.White)
                    Switch(checked = furiganaEnabled, onCheckedChange = { furiganaEnabled = it })
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text("Hepburn Romaji Subtitle Line", style = MaterialTheme.typography.bodyMedium, color = Color.White)
                    Switch(checked = romajiEnabled, onCheckedChange = { romajiEnabled = it })
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text("Tokyo Pitch Accent Contours", style = MaterialTheme.typography.bodyMedium, color = Color.White)
                    Switch(checked = pitchAccentEnabled, onCheckedChange = { pitchAccentEnabled = it })
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text("Simultaneous Dual-Language Translation", style = MaterialTheme.typography.bodyMedium, color = Color.White)
                    Switch(checked = dualTranslationEnabled, onCheckedChange = { dualTranslationEnabled = it })
                }
            }
        }
    }
}
