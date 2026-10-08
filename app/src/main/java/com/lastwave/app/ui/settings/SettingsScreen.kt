package com.lastwave.app.ui.settings

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.lastwave.app.data.local.MiscSettings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Album
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Backup
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Check
import com.lastwave.app.util.BatteryOptimizationHelper
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.CloudSync
import androidx.compose.material.icons.filled.FolderSpecial
import androidx.compose.material.icons.filled.Wifi
import com.lastwave.app.playback.server.LocalMediaWebServer
import androidx.compose.material.icons.filled.Colorize
import androidx.compose.material.icons.filled.Contrast
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.BubbleChart
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Lyrics
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Waves
import com.lastwave.app.data.local.LyricsAnimation
import com.lastwave.app.data.local.LyricsUiVersion
import com.lastwave.app.data.local.AppIconTheme
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.HighQuality
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Headset
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.QueueMusic
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.FormatListBulleted
import androidx.compose.material.icons.filled.Extension
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.material3.Surface
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.lastwave.app.data.local.AppLanguage
import com.lastwave.app.data.local.nativeDisplayName
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.Canvas
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.input.pointer.pointerInput
import com.lastwave.app.ui.theme.LocalLiquidGlass
import com.lastwave.app.ui.theme.LocalLiquidGlassOverlayBackdrop
import com.lastwave.app.ui.theme.LiquidGlassPreset
import com.lastwave.app.ui.theme.liquidGlassContainerColor
import com.lastwave.app.ui.theme.LiquidGlassSurface
import com.lastwave.app.ui.theme.liquidGlassChrome
import com.lastwave.app.ui.player.LocalMiniPlayerScrollClearance
import com.lastwave.app.R
import com.lastwave.app.data.local.AccentMode
import com.lastwave.app.data.local.ThemeMode
import com.lastwave.app.data.local.EQ_BAND_FREQS_HZ
import com.lastwave.app.data.local.EQ_MAX_GAIN_DB
import com.lastwave.app.data.local.EqualizerPresets
import com.lastwave.app.data.local.EqualizerSettings
import com.lastwave.app.data.local.eqBandLabel
import com.lastwave.app.ui.common.ConnectedButtonGroup
import com.lastwave.app.ui.common.ConnectedButtonItem
import com.lastwave.app.ui.common.ExpressiveHeader
import com.lastwave.app.ui.common.safeDrawingBottomPadding
import com.lastwave.app.ui.common.safeHorizontalContentPadding
import com.lastwave.app.ui.common.adaptiveContentWidth
import com.lastwave.app.ui.theme.ExpressivePillShape
import androidx.compose.material.icons.filled.BrightnessAuto
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.DarkMode
import kotlin.math.roundToInt

private data class AccentPreset(val name: String, val hex: String)
private val ACCENT_PRESETS = listOf(
    AccentPreset("Electric Cyan", "#00E5FF"),
    AccentPreset("Neon Amethyst", "#9D4EDD"),
    AccentPreset("Amber Tube", "#FFB703"),
    AccentPreset("Obsidian Gold", "#D4AF37"),
    AccentPreset("Emerald Hifi", "#06D6A0"),
    AccentPreset("Cyber Magenta", "#FF007F"),
    AccentPreset("Crimson Peak", "#E63946"),
)

// -- Expressive shape scale used only within this screen --
private val CardOuterShape = RoundedCornerShape(28.dp)
private val IconBadgeShape = RoundedCornerShape(14.dp)

/** Where a row sits within a visually-connected group of settings rows —
 *  drives per-row corner radii so a multi-row group reads as one premium
 *  surface split into rows, not a stack of separate cards (see groupShape
 *  below and the GROUP_GAP spacing used between rows in a group's Column). */
private enum class GroupPosition { SINGLE, TOP, MIDDLE, BOTTOM }

private val GROUP_OUTER_RADIUS = 28.dp
private val GROUP_INNER_RADIUS = 6.dp
private val GROUP_GAP = 3.dp

private fun groupShape(position: GroupPosition): RoundedCornerShape = when (position) {
    GroupPosition.SINGLE -> RoundedCornerShape(GROUP_OUTER_RADIUS)
    GroupPosition.TOP -> RoundedCornerShape(
        topStart = GROUP_OUTER_RADIUS, topEnd = GROUP_OUTER_RADIUS,
        bottomStart = GROUP_INNER_RADIUS, bottomEnd = GROUP_INNER_RADIUS,
    )
    GroupPosition.MIDDLE -> RoundedCornerShape(GROUP_INNER_RADIUS)
    GroupPosition.BOTTOM -> RoundedCornerShape(
        topStart = GROUP_INNER_RADIUS, topEnd = GROUP_INNER_RADIUS,
        bottomStart = GROUP_OUTER_RADIUS, bottomEnd = GROUP_OUTER_RADIUS,
    )
}

/** Wraps a fixed list of settings rows and assigns each one its
 *  GroupPosition automatically — SINGLE for a lone row, TOP/BOTTOM for the
 *  ends of a longer group, MIDDLE for everything between. Rows are stacked
 *  with a tiny GROUP_GAP rather than normal item spacing, so the group
 *  reads as one connected surface with rows peeking through a hairline gap
 *  rather than a list of separate cards. */
@Composable
private fun SettingsGroup(rowCount: Int, content: @Composable (index: Int, position: GroupPosition) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(GROUP_GAP)) {
        for (i in 0 until rowCount) {
            val position = when {
                rowCount == 1 -> GroupPosition.SINGLE
                i == 0 -> GroupPosition.TOP
                i == rowCount - 1 -> GroupPosition.BOTTOM
                else -> GroupPosition.MIDDLE
            }
            content(i, position)
        }
    }
}

/**
 * Faithful port of settings.js (par 8): Last.fm account management, appearance
 * (AMOLED / Dynamic Color / Monochrome / accent presets / custom color
 * wheel), iTunes/ListenBrainz artwork toggles, data management (clear
 * recommendation exclusions, clear all data), backup & restore, and app info.
 *
 * Visuals only: restyled into a Material 3 Expressive presentation
 * (larger touch targets, per-row cards, tonal icon badges, spring-based
 * press feedback). Every setting, callback, and piece of state below is
 * unchanged from the original implementation.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBack: () -> Unit = {},
    onLoggedOut: () -> Unit = {},
    onOpenChooseApps: () -> Unit = {},
    onOpenDownloads: () -> Unit = {},
    onOpenModules: () -> Unit = {},
    onOpenHomeSections: () -> Unit = {},
    onOpenExcludedSongs: () -> Unit = {},
    onOpenYouTubeImport: () -> Unit = {},
    onOpenYouTubeLogin: () -> Unit = {},
    onOpenExternalImport: () -> Unit = {},
    playerViewModel: com.lastwave.app.ui.player.PlayerViewModel = hiltViewModel(),
    viewModel: SettingsViewModel = hiltViewModel(),
) {
    val session by viewModel.session.collectAsStateWithLifecycle()
    val theme by viewModel.theme.collectAsStateWithLifecycle()
    val misc by viewModel.misc.collectAsStateWithLifecycle()
    val scrobbler by viewModel.scrobbler.collectAsStateWithLifecycle()
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val downloadCount by viewModel.downloadCount.collectAsStateWithLifecycle()
    val downloadTotalBytes by viewModel.downloadTotalBytes.collectAsStateWithLifecycle()
    val ytConnection by viewModel.ytConnection.collectAsStateWithLifecycle()
    val ytSyncEnabled by viewModel.ytSyncEnabled.collectAsStateWithLifecycle()
    val ytHistorySyncEnabled by viewModel.ytHistorySyncEnabled.collectAsStateWithLifecycle()
    val ytSyncState by viewModel.ytSyncState.collectAsStateWithLifecycle()
    val ytLastSyncAt by viewModel.ytLastSyncAt.collectAsStateWithLifecycle()
    val syncedPlaylistIds by viewModel.syncedPlaylistIds.collectAsStateWithLifecycle()
    val allPlaylists by viewModel.allPlaylists.collectAsStateWithLifecycle()
    val ytAccountPlaylists by viewModel.ytAccountPlaylists.collectAsStateWithLifecycle()
    val hiddenYtLibraryPlaylistIds by viewModel.hiddenYtLibraryPlaylistIds.collectAsStateWithLifecycle()
    val eq by viewModel.equalizer.collectAsStateWithLifecycle()
    val isLastFmConnected by viewModel.isLastFmConnected.collectAsStateWithLifecycle()
    val updateInfo by viewModel.updateInfo.collectAsStateWithLifecycle()
    val hasApiKey by viewModel.hasApiKey.collectAsStateWithLifecycle()
    val cacheSizeText by viewModel.cacheSizeText.collectAsStateWithLifecycle()
    val lastFmAuthUrl by viewModel.lastFmAuthUrl.collectAsStateWithLifecycle()
    val lastFmConnecting by viewModel.lastFmConnecting.collectAsStateWithLifecycle()
    val context = LocalContext.current

    // Last.fm web auth (Settings → Integrations): open the auth URL in Custom
    // Tabs the moment SettingsViewModel produces one.
    androidx.compose.runtime.LaunchedEffect(lastFmAuthUrl) {
        lastFmAuthUrl?.let { url ->
            runCatching {
                androidx.browser.customtabs.CustomTabsIntent.Builder().build()
                    .launchUrl(context, android.net.Uri.parse(url))
            }.onFailure {
                viewModel.showToast("No browser available to connect Last.fm")
                viewModel.cancelLastFmConnect()
            }
        }
    }
    var showQualityDialog by remember { mutableStateOf(false) }
    var showDownloadQualityDialog by remember { mutableStateOf(false) }
    var showEqSheet by remember { mutableStateOf(false) }
    var showLyricsAnimationSheet by remember { mutableStateOf(false) }
    var showPlayerStyleSheet by remember { mutableStateOf(false) }
    var showPlayerBgStyleSheet by remember { mutableStateOf(false) }
    var showSyncPlaylistsSheet by remember { mutableStateOf(false) }
    var showYtLibraryVisibilitySheet by remember { mutableStateOf(false) }
    var showYtDisconnectConfirm by remember { mutableStateOf(false) }
    var showDiscordTokenDialog by remember { mutableStateOf(false) }
    var showLanguageDialog by remember { mutableStateOf(false) }
    val currentLanguage = remember(misc.appLanguageTag) { AppLanguage.fromTag(misc.appLanguageTag) }

    // Sends the user to Android's own Notification Listener access screen
    // — the one permission this feature needs that the app can never grant
    // itself, only deep-link to. There's no reliable "is it already
    // granted for THIS app" API pre-33 short of parsing a settings string,
    // so this always opens the picker rather than guessing; picking
    // LastWave again there if it's already on is harmless.
    fun openNotificationAccessSettings() {
        val opened = startActivitySafely(
            context,
            Intent(android.provider.Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS),
        ) || startActivitySafely(context, Intent(android.provider.Settings.ACTION_SETTINGS))
        if (!opened) viewModel.showToast("Android Settings is unavailable on this ROM")
    }

    // "*/*" rather than "application/json": many document providers (Drive,
    // Downloads, some file managers) report a .json file as
    // application/octet-stream or text/plain, and GetContent's mime filter
    // hides anything that doesn't match — the user's own backup file would
    // silently not show up. Real validation happens right after the file is
    // read (stagePendingRestore), so being permissive here is safe.
    val restoreLauncher = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        if (uri != null) {
            viewModel.handleRestorePicked(uri)
        }
    }

    val csvPickerLauncher = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        if (uri != null) {
            viewModel.handleCsvPicked(uri)
        }
    }

    val irPickerLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: android.net.Uri? ->
        uri?.let { viewModel.loadImpulseResponseUri(context, it) }
    }

    var showAutoEqSheet by remember { mutableStateOf(false) }
    var autoEqSearchQuery by remember { mutableStateOf("") }
    var showWebDavDialog by remember { mutableStateOf(false) }
    var webDavMode by remember { mutableStateOf("backup") }
    var webDavUrl by remember { mutableStateOf("") }
    var webDavUser by remember { mutableStateOf("") }
    var webDavPass by remember { mutableStateOf("") }
    var webDavKey by remember { mutableStateOf("") }

    var showYouTubeImportSheet by remember { mutableStateOf(false) }

    // Lets the user pick exactly where the backup file is saved (SAF), so
    // it's guaranteed to be somewhere restoreLauncher's picker can browse
    // back to later — unlike a silent write into app-private storage.
    val backupLauncher = rememberLauncherForActivityResult(ActivityResultContracts.CreateDocument("application/json")) { uri ->
        if (uri != null) viewModel.exportBackup(uri, appVersionName(context))
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter,
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .adaptiveContentWidth(maxWidth = 760.dp),
        ) {
        ExpressiveHeader(title = stringResource(R.string.settings), onBack = onBack)

        val categoryTitles = listOf("Everyday", "Audio & Engine", "Integrations", "Personalization", "Diagnostics")
        val pagerState = rememberPagerState(pageCount = { categoryTitles.size })
        val scope = rememberCoroutineScope()

        ScrollableTabRow(
            selectedTabIndex = pagerState.currentPage,
            edgePadding = 16.dp,
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.primary,
            divider = {},
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        ) {
            categoryTitles.forEachIndexed { index, title ->
                val selected = pagerState.currentPage == index
                Tab(
                    selected = selected,
                    onClick = {
                        scope.launch { pagerState.animateScrollToPage(index) }
                    },
                    text = {
                        Text(
                            title,
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                        )
                    },
                )
            }
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize(),
        ) { page ->
            when (page) {
                4 -> {
                    DiagnosticsScreen(
                        player = playerViewModel.player,
                        modifier = Modifier.fillMaxSize(),
                    )
                }
                else -> {
                    LazyColumn(
                        contentPadding = PaddingValues(
                            start = 16.dp,
                            end = 16.dp,
                            top = 16.dp,
                            bottom = 32.dp + LocalMiniPlayerScrollClearance.current + safeDrawingBottomPadding()
                        ),
                        verticalArrangement = Arrangement.spacedBy(28.dp),
                        modifier = Modifier.fillMaxSize().safeHorizontalContentPadding(),
                    ) {


            if (page == 0) {
            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    SectionLabel("Integrations / Scrobbling")
                    // Last.fm is optional here — never a gate. Connected:
                    // global scrobbles + stats sync. Disconnected: Stats and
                    // recommendations run local-first from Room.
                    LastFmIntegrationCard(
                        isConnected = isLastFmConnected,
                        username = session.username,
                        connecting = lastFmConnecting,
                        awaitingApproval = lastFmAuthUrl != null,
                        hasApiKey = hasApiKey,
                        onConnect = { viewModel.beginLastFmConnect() },
                        onCancel = viewModel::cancelLastFmConnect,
                        onDisconnect = viewModel::disconnectLastFm,
                        onSaveKeys = viewModel::saveApiCredentials,
                        onRemoveKey = viewModel::clearApiKey,
                        onOpenCreateKeyPage = {
                            runCatching {
                                androidx.browser.customtabs.CustomTabsIntent.Builder().build()
                                    .launchUrl(context, android.net.Uri.parse(LAST_FM_CREATE_KEY_URL))
                            }.onFailure {
                                viewModel.showToast("No browser available to open Last.fm")
                            }
                        },
                    )
                }
            }

            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    SectionLabel(stringResource(R.string.settings_section_language))
                    SettingsGroup(rowCount = 1) { _, position ->
                        SettingsActionCard(
                            icon = Icons.Filled.Language,
                            iconContainer = MaterialTheme.colorScheme.primaryContainer,
                            iconTint = MaterialTheme.colorScheme.onPrimaryContainer,
                            title = stringResource(R.string.settings_language_title),
                            subtitle = currentLanguage.nativeDisplayName(),
                            onClick = { showLanguageDialog = true },
                            position = position,
                        )
                    }
                }
            }

            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    SectionLabel(stringResource(R.string.settings_section_youtube))
                    val ytConnected = ytConnection.isConnected
                    val syncSubtitle = when (val sync = ytSyncState) {
                        is com.lastwave.app.data.ytmusic.YtSyncState.Running ->
                            "Syncing ${sync.current}/${sync.total} \u2022 ${sync.label}"
                        is com.lastwave.app.data.ytmusic.YtSyncState.Completed ->
                            "Playlists mirror to your account \u2022 synced ${relativeTime(sync.atMillis)}"
                        is com.lastwave.app.data.ytmusic.YtSyncState.Failed ->
                            "Last pass failed \u2014 will retry automatically"
                        else ->
                            if (!ytConnected) "Connect an account first"
                            else if (ytSyncEnabled) "Selected playlists mirror to your account, 24/7" + lastSyncSuffix(ytLastSyncAt)
                            else "Keep your YT Music library in sync with LastWave"
                    }
                    val ytRowCount = if (ytConnected) 6 else 2
                    SettingsGroup(rowCount = ytRowCount) { index, position ->
                        when (index) {
                            0 -> if (ytConnected) {
                                YouTubeAccountRow(
                                    accountName = ytConnection.accountName,
                                    channelHandle = ytConnection.channelHandle,
                                    onDisconnect = { showYtDisconnectConfirm = true },
                                    position = position,
                                )
                            } else {
                                SettingsActionCard(
                                    icon = Icons.Filled.CloudSync,
                                    iconContainer = MaterialTheme.colorScheme.primaryContainer,
                                    iconTint = MaterialTheme.colorScheme.onPrimaryContainer,
                                    title = stringResource(R.string.settings_connect_yt),
                                    subtitle = stringResource(R.string.settings_connect_yt_sub),
                                    onClick = onOpenYouTubeLogin,
                                    position = position,
                                )
                            }
                            1 -> SettingsToggleCard(
                                icon = Icons.Filled.CloudSync,
                                iconContainer = MaterialTheme.colorScheme.secondaryContainer,
                                iconTint = MaterialTheme.colorScheme.onSecondaryContainer,
                                title = stringResource(R.string.settings_yt_sync),
                                subtitle = syncSubtitle,
                                checked = ytConnected && ytSyncEnabled,
                                onCheckedChange = viewModel::setYtSyncEnabled,
                                position = position,
                            )
                            2 -> if (ytConnected) {
                                val selectedCount = syncedPlaylistIds?.size ?: allPlaylists.size
                                val syncCountText = if (syncedPlaylistIds == null || selectedCount == allPlaylists.size) {
                                    "All (${allPlaylists.size}) playlists syncing"
                                } else {
                                    "$selectedCount of ${allPlaylists.size} playlists selected"
                                }
                                SettingsActionCard(
                                    icon = Icons.Filled.FormatListBulleted,
                                    iconContainer = MaterialTheme.colorScheme.tertiaryContainer,
                                    iconTint = MaterialTheme.colorScheme.onTertiaryContainer,
                                    title = stringResource(R.string.settings_select_playlists),
                                    subtitle = syncCountText,
                                    onClick = { showSyncPlaylistsSheet = true },
                                    position = position,
                                )
                            } else {
                                SettingsActionCard(
                                    icon = Icons.Filled.QueueMusic,
                                    iconContainer = MaterialTheme.colorScheme.tertiaryContainer,
                                    iconTint = MaterialTheme.colorScheme.onTertiaryContainer,
                                    title = "Import from YouTube Music",
                                    subtitle = "Import playlists directly from YouTube Music",
                                    onClick = onOpenYouTubeImport,
                                    position = position,
                                )
                            }
                            3 -> if (ytConnected) {
                                val shownCount = ytAccountPlaylists.count { it.id !in hiddenYtLibraryPlaylistIds }
                                val visibilitySubtitle = if (shownCount == ytAccountPlaylists.size) {
                                    "All (${ytAccountPlaylists.size}) account playlists shown"
                                } else {
                                    "$shownCount of ${ytAccountPlaylists.size} account playlists shown"
                                }
                                SettingsActionCard(
                                    icon = Icons.Filled.Visibility,
                                    iconContainer = MaterialTheme.colorScheme.secondaryContainer,
                                    iconTint = MaterialTheme.colorScheme.onSecondaryContainer,
                                    title = stringResource(R.string.settings_yt_shown),
                                    subtitle = visibilitySubtitle,
                                    onClick = { showYtLibraryVisibilitySheet = true },
                                    position = position,
                                )
                            }
                            4 -> SettingsActionCard(
                                icon = Icons.Filled.QueueMusic,
                                iconContainer = MaterialTheme.colorScheme.primaryContainer,
                                iconTint = MaterialTheme.colorScheme.onPrimaryContainer,
                                title = stringResource(R.string.settings_make_local),
                                subtitle = stringResource(R.string.settings_make_local_sub),
                                onClick = onOpenYouTubeImport,
                                position = position,
                            )
                            5 -> if (ytConnected) {
                                SettingsToggleCard(
                                    icon = Icons.Filled.History,
                                    iconContainer = MaterialTheme.colorScheme.tertiaryContainer,
                                    iconTint = MaterialTheme.colorScheme.onTertiaryContainer,
                                    title = stringResource(R.string.settings_yt_history),
                                    subtitle = if (ytHistorySyncEnabled) {
                                        "On • songs you listen to in LastWave, including lossless & downloads, appear in your YouTube Music history"
                                    } else {
                                        "Off • listening in LastWave stays out of your YouTube Music history"
                                    },
                                    checked = ytHistorySyncEnabled,
                                    onCheckedChange = viewModel::setYtHistorySyncEnabled,
                                    position = position,
                                )
                            }
                        }
                    }
                }
            }

            item {
                val mb = (downloadTotalBytes ?: 0L).toDouble() / (1024 * 1024)
                val formattedStorage = if (mb >= 1000) "%.1f GB".format(mb / 1024) else "%.1f MB".format(mb)
                val downloadsSubtitle = if (downloadCount > 0) {
                    if (misc.downloadStructure == com.lastwave.app.data.local.DownloadFolderStructure.FLAT) {
                        stringResource(
                            R.string.settings_downloads_sub,
                            downloadCount,
                            formattedStorage,
                            misc.downloadFolder,
                        )
                    } else {
                        stringResource(
                            R.string.settings_downloads_sub_structured,
                            downloadCount,
                            formattedStorage,
                            misc.downloadFolder,
                            stringResource(misc.downloadStructure.shortLabelRes),
                        )
                    }
                } else {
                    stringResource(R.string.settings_downloads_empty)
                }

                Card(
                    onClick = onOpenDownloads,
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 18.dp, vertical = 16.dp).fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primary),
                            contentAlignment = Alignment.Center,
                        ) {
                            Icon(
                                Icons.Filled.Download,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.size(22.dp),
                            )
                        }
                        Spacer(Modifier.width(16.dp))
                        Column(Modifier.weight(1f)) {
                            Text(
                                stringResource(R.string.settings_downloads_title),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                            )
                            Spacer(Modifier.height(2.dp))
                            Text(
                                downloadsSubtitle,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f),
                            )
                        }
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowForwardIos,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.6f),
                            modifier = Modifier.size(16.dp),
                        )
                    }
                }
            }

            item {
                Card(
                    onClick = onOpenModules,
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 18.dp, vertical = 16.dp).fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.secondary),
                            contentAlignment = Alignment.Center,
                        ) {
                            Icon(
                                Icons.Filled.Extension,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSecondary,
                                modifier = Modifier.size(22.dp),
                            )
                        }
                        Spacer(Modifier.width(16.dp))
                        Column(Modifier.weight(1f)) {
                            Text(
                                stringResource(R.string.settings_modules_title),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSecondaryContainer,
                            )
                            Spacer(Modifier.height(2.dp))
                            Text(
                                stringResource(R.string.settings_modules_sub),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.8f),
                            )
                        }
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowForwardIos,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.6f),
                            modifier = Modifier.size(16.dp),
                        )
                    }
                }
            }

            }

            if (page == 2) {
            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    SectionLabel(stringResource(R.string.settings_section_appearance))
                    SettingsGroup(rowCount = 9) { index, position ->
                        when (index) {
                            0 -> ThemeModeSelectorCard(
                                currentThemeMode = theme?.themeMode ?: ThemeMode.SYSTEM,
                                onSelectThemeMode = viewModel::setThemeMode,
                                position = position,
                            )
                            1 -> SettingsToggleCard(
                                icon = Icons.Filled.Contrast,
                                iconContainer = MaterialTheme.colorScheme.tertiaryContainer,
                                iconTint = MaterialTheme.colorScheme.onTertiaryContainer,
                                title = stringResource(R.string.settings_amoled),
                                subtitle = stringResource(R.string.settings_amoled_sub),
                                checked = theme?.amoled ?: false,
                                enabled = theme?.themeMode != ThemeMode.LIGHT,
                                onCheckedChange = viewModel::setAmoled,
                                position = position,
                            )
                            2 -> SettingsToggleCard(
                                icon = Icons.Filled.Palette,
                                iconContainer = MaterialTheme.colorScheme.primaryContainer,
                                iconTint = MaterialTheme.colorScheme.onPrimaryContainer,
                                title = stringResource(R.string.settings_dynamic_color),
                                subtitle = stringResource(R.string.settings_dynamic_color_sub),
                                checked = theme?.mode == AccentMode.DYNAMIC,
                                onCheckedChange = { enabled ->
                                    viewModel.setAccentMode(if (enabled) AccentMode.DYNAMIC else AccentMode.MANUAL)
                                },
                                position = position,
                            )
                            3 -> SettingsToggleCard(
                                icon = Icons.Filled.Album,
                                iconContainer = MaterialTheme.colorScheme.tertiaryContainer,
                                iconTint = MaterialTheme.colorScheme.onTertiaryContainer,
                                title = stringResource(R.string.settings_dynamic_now_playing),
                                subtitle = stringResource(R.string.settings_dynamic_now_playing_sub),
                                checked = misc.dynamicNowPlayingEnabled,
                                onCheckedChange = viewModel::setDynamicNowPlaying,
                                position = position,
                            )
                            4 -> SettingsActionCard(
                                icon = Icons.Filled.Dashboard,
                                iconContainer = MaterialTheme.colorScheme.primaryContainer,
                                iconTint = MaterialTheme.colorScheme.onPrimaryContainer,
                                title = "Player Layout Architecture",
                                subtitle = "${misc.playerStyle.title} • ${misc.playerStyle.description}",
                                onClick = { showPlayerStyleSheet = true },
                                position = position,
                            )
                            5 -> SettingsActionCard(
                                icon = Icons.Filled.Image,
                                iconContainer = MaterialTheme.colorScheme.secondaryContainer,
                                iconTint = MaterialTheme.colorScheme.onSecondaryContainer,
                                title = "Player Background Canvas",
                                subtitle = "${misc.playerBackgroundStyle.title} • ${misc.playerBackgroundStyle.description}",
                                onClick = { showPlayerBgStyleSheet = true },
                                position = position,
                            )
                            6 -> SettingsToggleCard(
                                icon = Icons.Filled.Album,
                                iconContainer = MaterialTheme.colorScheme.secondaryContainer,
                                iconTint = MaterialTheme.colorScheme.onSecondaryContainer,
                                title = stringResource(R.string.settings_cover_style),
                                subtitle = if (misc.playerCoverStyle == com.lastwave.app.data.local.PlayerCoverStyle.ROTATING_VINYL) {
                                    "Circular vinyl disc with grooved record rings & live rotation"
                                } else {
                                    stringResource(R.string.settings_cover_style_sub)
                                },
                                checked = misc.playerCoverStyle == com.lastwave.app.data.local.PlayerCoverStyle.ROTATING_VINYL,
                                onCheckedChange = { isVinyl ->
                                    viewModel.setPlayerCoverStyle(
                                        if (isVinyl) com.lastwave.app.data.local.PlayerCoverStyle.ROTATING_VINYL
                                        else com.lastwave.app.data.local.PlayerCoverStyle.SQUARE_COVER
                                    )
                                },
                                position = position,
                            )
                            7 -> SettingsToggleCard(
                                icon = Icons.Filled.TextFields,
                                iconContainer = MaterialTheme.colorScheme.secondaryContainer,
                                iconTint = MaterialTheme.colorScheme.onSecondaryContainer,
                                title = stringResource(R.string.settings_app_font),
                                subtitle = stringResource(R.string.settings_app_font_sub),
                                checked = misc.useCustomFont,
                                onCheckedChange = viewModel::setUseCustomFont,
                                position = position,
                            )
                            8 -> SettingsActionCard(
                                icon = Icons.Filled.Dashboard,
                                iconContainer = MaterialTheme.colorScheme.primaryContainer,
                                iconTint = MaterialTheme.colorScheme.onPrimaryContainer,
                                title = stringResource(R.string.settings_home_sections),
                                subtitle = run {
                                    val total = com.lastwave.app.data.local.HomeSection.entries.size
                                    val visible = total - misc.hiddenHomeSections.size
                                    stringResource(R.string.home_sections_visible, visible, total)
                                },
                                onClick = onOpenHomeSections,
                                position = position,
                            )
                        }
                    }
                }
            }

            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    SectionLabel(stringResource(R.string.settings_section_accent))
                    Card(
                        shape = CardOuterShape,
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh),
                    ) {
                        Column(Modifier.padding(20.dp)) {
                            AccentPresetGrid(
                                currentMode = theme?.mode ?: AccentMode.MANUAL,
                                selectedHex = theme?.accentColorHex,
                                onPickDynamic = { viewModel.setAccentMode(AccentMode.DYNAMIC) },
                                onPickPreset = { hex -> viewModel.setManualAccent(Color(android.graphics.Color.parseColor(hex))) },
                                onPickMono = { viewModel.setAccentMode(AccentMode.MONOCHROME) },
                                onPickCustom = viewModel::openColorWheel,
                            )
                        }
                    }
                    AppIconSelectorCard(
                        currentTheme = misc.appIconTheme,
                        onSelectIcon = viewModel::setAppIconTheme,
                    )
                    StreamBufferTuningCard(
                        minBufferMs = misc.minBufferMs,
                        maxBufferMs = misc.maxBufferMs,
                        playbackBufferMs = misc.bufferForPlaybackMs,
                        onBufferChange = viewModel::setBufferTuning,
                    )
                    StreamCodecCard(
                        currentCodec = misc.preferredAudioCodec,
                        onSelectCodec = viewModel::setPreferredAudioCodec,
                    )
                    NotchSettingsCard(
                        enabled = misc.enableDynamicIslandNotch,
                        topMarginDp = misc.notchTopMarginDp,
                        widthDp = misc.notchCapsuleWidthDp,
                        dismissSec = misc.notchAutoDismissSec,
                        onNotchChange = viewModel::setDynamicNotchSettings,
                    )
                    CacheQuotaCard(
                        currentQuotaMb = misc.cacheQuotaMb,
                        onSelectQuotaMb = viewModel::setCacheQuotaMb,
                    )
                    CrossfeedPresetsCard(
                        enabled = misc.crossfeedEnabled,
                        levelDb = misc.crossfeedLevelDb,
                        cutoffHz = misc.crossfeedCutoffHz,
                        onToggle = viewModel::setCrossfeedEnabled,
                        onChange = { lvl, cut -> viewModel.setCrossfeedLevelDb(lvl); viewModel.setCrossfeedCutoffHz(cut) },
                    )
                    SilenceTrimmingCard(
                        thresholdDb = misc.silenceThresholdDb,
                        minDurationMs = misc.minSilenceDurationMs,
                        onChange = viewModel::setSilenceSettings,
                    )
                    MiniPlayerSwipeCard(
                        currentStyle = misc.miniPlayerSwipeStyle,
                        onSelectStyle = viewModel::setMiniPlayerSwipeStyle,
                    )
                    LyricsTypographyCard(
                        fontSize = misc.lyricsFontSize,
                        lineSpacing = misc.lyricsLineSpacing,
                        blurRadius = misc.lyricsInactiveBlurRadius,
                        onChange = viewModel::setLyricsTypographySettings,
                    )
                    NavigationBarTabsCard(
                        visibleTabs = misc.visibleNavTabs,
                        onToggleTab = viewModel::toggleNavTab,
                    )
                    DownloadConcurrencyCard(
                        concurrency = misc.downloadConcurrency,
                        wifiOnly = misc.downloadWifiOnly,
                        onConcurrencyChange = viewModel::setDownloadConcurrency,
                        onWifiOnlyChange = viewModel::setDownloadWifiOnly,
                    )
                    LocalWebRemoteCard(
                        musicPlayer = playerViewModel.player,
                    )
                    LibraryFoldersAndExclusionCard(
                        settings = misc,
                        onAddScanFolder = viewModel::addCustomScanFolder,
                        onRemoveScanFolder = viewModel::removeCustomScanFolder,
                        onAddExcludedFolder = viewModel::addCustomExcludedFolder,
                        onRemoveExcludedFolder = viewModel::removeCustomExcludedFolder,
                    )
                    AudioOffloadCard(
                        enabled = misc.audioOffloadEnabled,
                        onToggle = viewModel::setAudioOffloadEnabled,
                    )
                    ConvolutionIrCard(
                        wetLevel = misc.convolutionWetLevel,
                        onSelectFile = { irPickerLauncher.launch("audio/*") },
                        onWetChange = viewModel::setConvolutionWetLevel,
                    )
                    AutoEqCard(
                        onOpenSearch = { showAutoEqSheet = true },
                    )
                    AudiophileThemePaletteCard(
                        onSelectPreset = viewModel::setAudiophilePalette,
                    )
                    IncognitoModeCard(
                        enabled = misc.isIncognitoMode,
                        onToggle = viewModel::setIncognitoMode,
                    )
                    InnerTubeSpoofingCard(
                        currentClient = misc.innerTubeClientName,
                        onSelectClient = viewModel::setInnerTubeClientName,
                    )
                    DohAndProxyCard(
                        dohProvider = misc.dohProviderName,
                        onSelectDoh = viewModel::setDohProviderName,
                    )
                    WebDavBackupCard(
                        onBackup = { webDavMode = "backup"; showWebDavDialog = true },
                        onRestore = { webDavMode = "restore"; showWebDavDialog = true },
                    )
                }
            }

            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    SectionLabel(stringResource(R.string.settings_section_experimental))
                    SettingsGroup(rowCount = 5) { index, position ->
                        when (index) {
                            0 -> SettingsToggleCard(
                                icon = Icons.Filled.BubbleChart,
                                iconContainer = MaterialTheme.colorScheme.primaryContainer,
                                iconTint = MaterialTheme.colorScheme.onPrimaryContainer,
                                title = stringResource(R.string.settings_liquid_glass),
                                subtitle = stringResource(R.string.settings_liquid_glass_sub),
                                checked = theme?.liquidGlass ?: false,
                                onCheckedChange = viewModel::setLiquidGlass,
                                position = position,
                            )
                            1 -> SettingsActionCard(
                                icon = Icons.Filled.Lyrics,
                                iconContainer = MaterialTheme.colorScheme.tertiaryContainer,
                                iconTint = MaterialTheme.colorScheme.onTertiaryContainer,
                                title = stringResource(R.string.settings_lyrics_animation),
                                subtitle = if (misc.lyricsUiVersion == LyricsUiVersion.MODERN) {
                                    "New UI (Modern)"
                                } else {
                                    "${misc.lyricsAnimation.title} \u2022 ${misc.lyricsAnimation.description}"
                                },
                                onClick = { showLyricsAnimationSheet = true },
                                position = position,
                            )
                            2 -> SettingsActionCard(
                                icon = Icons.Filled.GraphicEq,
                                iconContainer = MaterialTheme.colorScheme.secondaryContainer,
                                iconTint = MaterialTheme.colorScheme.onSecondaryContainer,
                                title = stringResource(R.string.settings_equalizer),
                                subtitle = if (misc.isBitPerfectEnabled && eq.enabled) {
                                    "On \u2022 ${eq.presetName} (Bypassed by Bit-Perfect Mode)"
                                } else if (eq.enabled) {
                                    "On \u2022 ${eq.presetName} \u2022 31-band"
                                } else {
                                    "Shape your sound across 31 frequencies"
                                },
                                onClick = { showEqSheet = true },
                                position = position,
                            )
                            3 -> SettingsToggleCard(
                                icon = Icons.Filled.Waves,
                                iconContainer = MaterialTheme.colorScheme.secondaryContainer,
                                iconTint = MaterialTheme.colorScheme.onSecondaryContainer,
                                title = stringResource(R.string.settings_wavy_seekbar),
                                subtitle = if (misc.wavySeekbarEnabled) {
                                    "Style: ${misc.seekbarStyle.title} — ${misc.seekbarStyle.description}"
                                } else {
                                    "Classic standard progress slider"
                                },
                                checked = misc.wavySeekbarEnabled,
                                onCheckedChange = viewModel::setWavySeekbarEnabled,
                                position = position,
                            )
                            4 -> if (misc.wavySeekbarEnabled) {
                                Surface(
                                    shape = RoundedCornerShape(16.dp),
                                    color = MaterialTheme.colorScheme.surfaceContainerHigh,
                                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                                ) {
                                    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                        Text("Seekbar Style", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                        com.lastwave.app.data.local.SeekbarStyle.entries.forEach { style ->
                                            val selected = style == misc.seekbarStyle
                                            Surface(
                                                onClick = { viewModel.setSeekbarStyle(style) },
                                                shape = CircleShape,
                                                color = if (selected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceContainerLow,
                                                modifier = Modifier.fillMaxWidth(),
                                            ) {
                                                Row(Modifier.padding(horizontal = 14.dp, vertical = 10.dp), verticalAlignment = Alignment.CenterVertically) {
                                                    Text(
                                                        style.title,
                                                        style = MaterialTheme.typography.bodyMedium,
                                                        fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                                                        color = if (selected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface,
                                                        modifier = Modifier.weight(1f),
                                                    )
                                                    if (selected) {
                                                        Icon(Icons.Filled.Check, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            } else {
                                SettingsToggleCard(
                                    icon = Icons.Filled.AutoAwesome,
                                    iconContainer = MaterialTheme.colorScheme.primaryContainer,
                                    iconTint = MaterialTheme.colorScheme.onPrimaryContainer,
                                    title = stringResource(R.string.settings_studio_clarity),
                                    subtitle = if (misc.isStudioMasterClarityEnabled) {
                                        "Restores high-frequency detail and enhances audio spatial clarity"
                                    } else {
                                        "Original unshaped output"
                                    },
                                    checked = misc.isStudioMasterClarityEnabled,
                                    onCheckedChange = viewModel::setStudioMasterClarity,
                                    position = position,
                                )
                            }
                            4 -> SettingsToggleCard(
                                icon = Icons.Filled.AutoAwesome,
                                iconContainer = MaterialTheme.colorScheme.primaryContainer,
                                iconTint = MaterialTheme.colorScheme.onPrimaryContainer,
                                title = stringResource(R.string.settings_studio_clarity),
                                subtitle = if (misc.isStudioMasterClarityEnabled) {
                                    "Restores high-frequency detail and enhances audio spatial clarity"
                                } else {
                                    "Original unshaped output"
                                },
                                checked = misc.isStudioMasterClarityEnabled,
                                onCheckedChange = viewModel::setStudioMasterClarity,
                                position = position,
                            )
                                                    }
                    }
                }
            }
            }

            if (page == 1) {
            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    SectionLabel(stringResource(R.string.settings_section_audio))
                    val qualitySubtitle = when {
                        misc.autoDataSaverEnabled -> {
                            val wifiLabel = when (misc.wifiStreamingQuality) {
                                27 -> "Wi-Fi: Lossless 24/192"
                                5 -> "Wi-Fi: 320k"
                                else -> "Wi-Fi: Data Saver"
                            }
                            val cellLabel = when (misc.cellularStreamingQuality) {
                                27 -> "Cellular: Lossless 24/192"
                                5 -> "Cellular: 320k"
                                else -> "Cellular: Data Saver 160k"
                            }
                            "$wifiLabel • $cellLabel"
                        }
                        else -> when (misc.losslessQuality) {
                            27 -> "Max (Up to 24-bit / 192 kHz)"
                            7 -> "Hi-Res (24-bit / 96 kHz)"
                            6 -> "CD Lossless (16-bit / 44.1 kHz FLAC)"
                            5 -> "Standard (320 kbps MP3)"
                            -1 -> "YouTube Music (AAC / Opus)"
                            else -> "Max (Up to 24-bit / 192 kHz)"
                        }
                    }
                    val downloadQualitySubtitle = when (misc.downloadQuality) {
                        27 -> "Max (24-bit / 192 kHz FLAC)"
                        7 -> "Hi-Res (24-bit / 96 kHz FLAC)"
                        6 -> "CD Lossless (16-bit / 44.1 kHz FLAC)"
                        5 -> "Standard (320 kbps MP3)"
                        -1 -> "YouTube Music (AAC / Opus)"
                        else -> "Max (24-bit / 192 kHz FLAC)"
                    }

                    val totalAudioRows = if (misc.crossfadeEnabled) 11 else 10
                    SettingsGroup(rowCount = totalAudioRows) { index, position ->
                        when (index) {
                            0 -> SettingsActionCard(
                                icon = Icons.Filled.HighQuality,
                                iconContainer = MaterialTheme.colorScheme.primaryContainer,
                                iconTint = MaterialTheme.colorScheme.onPrimaryContainer,
                                title = stringResource(R.string.settings_streaming_quality),
                                subtitle = qualitySubtitle,
                                onClick = { showQualityDialog = true },
                                position = position,
                            )
                            1 -> SettingsActionCard(
                                icon = Icons.Filled.CloudDownload,
                                iconContainer = MaterialTheme.colorScheme.secondaryContainer,
                                iconTint = MaterialTheme.colorScheme.onSecondaryContainer,
                                title = stringResource(R.string.settings_download_quality),
                                subtitle = "$downloadQualitySubtitle \u2022 Lossless & YouTube",
                                onClick = { showDownloadQualityDialog = true },
                                position = position,
                            )
                            2 -> SettingsToggleCard(
                                icon = Icons.Filled.Tune,
                                iconContainer = MaterialTheme.colorScheme.primaryContainer,
                                iconTint = MaterialTheme.colorScheme.onPrimaryContainer,
                                title = stringResource(R.string.settings_bit_perfect),
                                subtitle = if (misc.isBitPerfectEnabled) {
                                    "Routes raw audio directly to external USB DACs without Android resampler processing"
                                } else {
                                    stringResource(R.string.settings_bit_perfect_off_detail)
                                },
                                checked = misc.isBitPerfectEnabled,
                                onCheckedChange = viewModel::setBitPerfectEnabled,
                                position = position,
                            )
                            3 -> SettingsToggleCard(
                                icon = Icons.Filled.GraphicEq,
                                iconContainer = MaterialTheme.colorScheme.tertiaryContainer,
                                iconTint = MaterialTheme.colorScheme.onTertiaryContainer,
                                title = stringResource(R.string.settings_crossfade),
                                subtitle = if (misc.crossfadeEnabled && misc.isBitPerfectEnabled) {
                                    "Paused while Bit-Perfect is enabled"
                                } else if (misc.crossfadeEnabled) {
                                    "Smooth transition between tracks \u2022 ${misc.crossfadeSeconds} sec"
                                } else {
                                    "Blend the end of a track into the next one"
                                },
                                checked = misc.crossfadeEnabled,
                                onCheckedChange = viewModel::setCrossfadeEnabled,
                                position = position,
                            )
                            4 -> if (misc.crossfadeEnabled) {
                                CrossfadeDurationRow(
                                    seconds = misc.crossfadeSeconds,
                                    onSecondsChange = viewModel::setCrossfadeSeconds,
                                    position = position,
                                )
                            } else {
                                SettingsToggleCard(
                                    icon = Icons.Filled.VolumeUp,
                                    iconContainer = MaterialTheme.colorScheme.primaryContainer,
                                    iconTint = MaterialTheme.colorScheme.onPrimaryContainer,
                                    title = stringResource(R.string.settings_loudness_normalization),
                                    subtitle = if (misc.loudnessNormalizationEnabled) {
                                        "Leveling volume across tracks to prevent sudden loudness spikes"
                                    } else {
                                        stringResource(R.string.settings_loudness_normalization_sub)
                                    },
                                    checked = misc.loudnessNormalizationEnabled,
                                    onCheckedChange = viewModel::setLoudnessNormalizationEnabled,
                                    position = position,
                                )
                            }
                            5 -> if (misc.crossfadeEnabled) {
                                SettingsToggleCard(
                                    icon = Icons.Filled.VolumeUp,
                                    iconContainer = MaterialTheme.colorScheme.primaryContainer,
                                    iconTint = MaterialTheme.colorScheme.onPrimaryContainer,
                                    title = stringResource(R.string.settings_loudness_normalization),
                                    subtitle = if (misc.loudnessNormalizationEnabled) {
                                        "Leveling volume across tracks to prevent sudden loudness spikes"
                                    } else {
                                        stringResource(R.string.settings_loudness_normalization_sub)
                                    },
                                    checked = misc.loudnessNormalizationEnabled,
                                    onCheckedChange = viewModel::setLoudnessNormalizationEnabled,
                                    position = position,
                                )
                            } else {
                                SettingsToggleCard(
                                    icon = Icons.Filled.FastForward,
                                    iconContainer = MaterialTheme.colorScheme.tertiaryContainer,
                                    iconTint = MaterialTheme.colorScheme.onTertiaryContainer,
                                    title = stringResource(R.string.settings_skip_silence),
                                    subtitle = if (misc.skipSilenceEnabled) {
                                        "Automatically skipping silent sections during playback"
                                    } else {
                                        stringResource(R.string.settings_skip_silence_sub)
                                    },
                                    checked = misc.skipSilenceEnabled,
                                    onCheckedChange = viewModel::setSkipSilenceEnabled,
                                    position = position,
                                )
                            }
                            6 -> if (misc.crossfadeEnabled) {
                                SettingsToggleCard(
                                    icon = Icons.Filled.FastForward,
                                    iconContainer = MaterialTheme.colorScheme.tertiaryContainer,
                                    iconTint = MaterialTheme.colorScheme.onTertiaryContainer,
                                    title = stringResource(R.string.settings_skip_silence),
                                    subtitle = if (misc.skipSilenceEnabled) {
                                        "Automatically skipping silent sections during playback"
                                    } else {
                                        stringResource(R.string.settings_skip_silence_sub)
                                    },
                                    checked = misc.skipSilenceEnabled,
                                    onCheckedChange = viewModel::setSkipSilenceEnabled,
                                    position = position,
                                )
                            } else {
                                SettingsToggleCard(
                                    icon = Icons.Filled.AutoAwesome,
                                    iconContainer = MaterialTheme.colorScheme.primaryContainer,
                                    iconTint = MaterialTheme.colorScheme.onPrimaryContainer,
                                    title = stringResource(R.string.settings_sponsorblock),
                                    subtitle = if (misc.sponsorBlockEnabled) {
                                        "Auto-skipping non-music intros, outros, and commentary"
                                    } else {
                                        stringResource(R.string.settings_sponsorblock_sub)
                                    },
                                    checked = misc.sponsorBlockEnabled,
                                    onCheckedChange = viewModel::setSponsorBlockEnabled,
                                    position = position,
                                )
                            }
                            7 -> if (misc.crossfadeEnabled) {
                                SettingsToggleCard(
                                    icon = Icons.Filled.AutoAwesome,
                                    iconContainer = MaterialTheme.colorScheme.primaryContainer,
                                    iconTint = MaterialTheme.colorScheme.onPrimaryContainer,
                                    title = stringResource(R.string.settings_sponsorblock),
                                    subtitle = if (misc.sponsorBlockEnabled) {
                                        "Auto-skipping non-music intros, outros, and commentary"
                                    } else {
                                        stringResource(R.string.settings_sponsorblock_sub)
                                    },
                                    checked = misc.sponsorBlockEnabled,
                                    onCheckedChange = viewModel::setSponsorBlockEnabled,
                                    position = position,
                                )
                            } else {
                                SettingsToggleCard(
                                    icon = Icons.Filled.Lyrics,
                                    iconContainer = MaterialTheme.colorScheme.secondaryContainer,
                                    iconTint = MaterialTheme.colorScheme.onSecondaryContainer,
                                    title = stringResource(R.string.settings_download_lyrics),
                                    subtitle = if (misc.downloadLyrics) {
                                        "Save .lrc companion files & embed lyrics in downloads"
                                    } else {
                                        "Do not fetch or save lyrics when downloading"
                                    },
                                    checked = misc.downloadLyrics,
                                    onCheckedChange = viewModel::setDownloadLyrics,
                                    position = position,
                                )
                            }
                            8 -> if (misc.crossfadeEnabled) {
                                SettingsToggleCard(
                                    icon = Icons.Filled.Lyrics,
                                    iconContainer = MaterialTheme.colorScheme.secondaryContainer,
                                    iconTint = MaterialTheme.colorScheme.onSecondaryContainer,
                                    title = stringResource(R.string.settings_download_lyrics),
                                    subtitle = if (misc.downloadLyrics) {
                                        "Save .lrc companion files & embed lyrics in downloads"
                                    } else {
                                        "Do not fetch or save lyrics when downloading"
                                    },
                                    checked = misc.downloadLyrics,
                                    onCheckedChange = viewModel::setDownloadLyrics,
                                    position = position,
                                )
                            } else {
                                val isIgnored = BatteryOptimizationHelper.isIgnoringBatteryOptimizations(context)
                                SettingsToggleCard(
                                    icon = Icons.Filled.Vibration,
                                    iconContainer = MaterialTheme.colorScheme.tertiaryContainer,
                                    iconTint = MaterialTheme.colorScheme.onTertiaryContainer,
                                    title = "Shake to Skip / Motion Gesture",
                                    subtitle = if (misc.shakeToSkipEnabled) {
                                        "Active \u2022 Shake device to skip to next track during playback"
                                    } else {
                                        "Shake your phone to skip to the next track"
                                    },
                                    checked = misc.shakeToSkipEnabled,
                                    onCheckedChange = viewModel::setShakeToSkipEnabled,
                                    position = position,
                                )
                            }
                            9 -> if (misc.crossfadeEnabled) {
                                SettingsToggleCard(
                                    icon = Icons.Filled.Vibration,
                                    iconContainer = MaterialTheme.colorScheme.tertiaryContainer,
                                    iconTint = MaterialTheme.colorScheme.onTertiaryContainer,
                                    title = "Shake to Skip / Motion Gesture",
                                    subtitle = if (misc.shakeToSkipEnabled) {
                                        "Active \u2022 Shake device to skip to next track during playback"
                                    } else {
                                        "Shake your phone to skip to the next track"
                                    },
                                    checked = misc.shakeToSkipEnabled,
                                    onCheckedChange = viewModel::setShakeToSkipEnabled,
                                    position = position,
                                )
                            } else {
                                val isIgnored = BatteryOptimizationHelper.isIgnoringBatteryOptimizations(context)
                                SettingsActionCard(
                                    icon = Icons.Filled.Bolt,
                                    iconContainer = if (isIgnored) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.errorContainer,
                                    iconTint = if (isIgnored) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.onErrorContainer,
                                    title = stringResource(R.string.settings_battery_title),
                                    subtitle = if (isIgnored) {
                                        "Unrestricted — background playback and scrobbling will run without interruption"
                                    } else {
                                        "Optimized — Android may restrict playback when screen is locked"
                                    },
                                    onClick = { BatteryOptimizationHelper.requestIgnoreBatteryOptimizations(context) },
                                    position = position,
                                )
                            }
                            10 -> {
                                val isIgnored = BatteryOptimizationHelper.isIgnoringBatteryOptimizations(context)
                                SettingsActionCard(
                                    icon = Icons.Filled.Bolt,
                                    iconContainer = if (isIgnored) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.errorContainer,
                                    iconTint = if (isIgnored) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.onErrorContainer,
                                    title = stringResource(R.string.settings_battery_title),
                                    subtitle = if (isIgnored) {
                                        "Unrestricted — background playback and scrobbling will run without interruption"
                                    } else {
                                        "Optimized — Android may restrict playback when screen is locked"
                                    },
                                    onClick = { BatteryOptimizationHelper.requestIgnoreBatteryOptimizations(context) },
                                    position = position,
                                )
                            }
                        }
                    }
                }
            }


            }

            if (page == 3) {
            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    SectionLabel(stringResource(R.string.settings_section_imports))
                    SettingsGroup(rowCount = 2) { index, position ->
                        when (index) {
                            0 -> SettingsActionCard(
                                icon = Icons.Filled.QueueMusic,
                                iconContainer = MaterialTheme.colorScheme.primaryContainer,
                                iconTint = MaterialTheme.colorScheme.onPrimaryContainer,
                                title = "Import from Spotify / Apple Music",
                                subtitle = "Import public playlist links from Spotify or Apple Music",
                                onClick = onOpenExternalImport,
                                position = position,
                            )
                            else -> SettingsActionCard(
                                icon = Icons.Filled.FileDownload,
                                iconContainer = MaterialTheme.colorScheme.secondaryContainer,
                                iconTint = MaterialTheme.colorScheme.onSecondaryContainer,
                                title = stringResource(R.string.settings_import_file),
                                subtitle = stringResource(R.string.settings_import_file_sub),
                                onClick = {
                                    runCatching {
                                        csvPickerLauncher.launch(arrayOf("text/*", "text/csv", "application/csv", "audio/x-mpegurl", "application/x-mpegurl", "application/vnd.apple.mpegurl", "*/*"))
                                    }.onFailure { viewModel.showToast("No file picker is available") }
                                },
                                position = position,
                            )
                        }
                    }
                }
            }

            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    SectionLabel(stringResource(R.string.settings_section_scrobbler))
                    SettingsGroup(rowCount = 4) { index, position ->
                        when (index) {
                            0 -> SettingsToggleCard(
                                icon = Icons.Filled.GraphicEq,
                                iconContainer = MaterialTheme.colorScheme.primaryContainer,
                                iconTint = MaterialTheme.colorScheme.onPrimaryContainer,
                                title = stringResource(R.string.settings_scrobble),
                                subtitle = if (scrobbler.enabled) "Watching ${scrobbler.selectedPackages.size} app(s)" else "Detect and submit plays from other apps",
                                checked = scrobbler.enabled,
                                onCheckedChange = { enabled ->
                                    if (enabled) openNotificationAccessSettings()
                                    viewModel.setScrobblerEnabled(enabled)
                                },
                                position = position,
                            )
                            1 -> SettingsActionCard(
                                icon = Icons.Filled.Apps,
                                iconContainer = MaterialTheme.colorScheme.secondaryContainer,
                                iconTint = MaterialTheme.colorScheme.onSecondaryContainer,
                                title = stringResource(R.string.settings_choose_apps),
                                subtitle = if (scrobbler.selectedPackages.isEmpty()) "None selected yet" else "${scrobbler.selectedPackages.size} app(s) selected",
                                onClick = onOpenChooseApps,
                                position = position,
                            )
                            2 -> SettingsToggleCard(
                                icon = Icons.Filled.NotificationsActive,
                                iconContainer = MaterialTheme.colorScheme.tertiaryContainer,
                                iconTint = MaterialTheme.colorScheme.onTertiaryContainer,
                                title = stringResource(R.string.settings_now_playing),
                                subtitle = stringResource(R.string.settings_now_playing_sub),
                                checked = scrobbler.submitNowPlaying,
                                onCheckedChange = viewModel::setSubmitNowPlaying,
                                position = position,
                            )
                            3 -> ScrobbleThresholdRow(
                                percent = scrobbler.scrobblePercent,
                                onPercentChange = viewModel::setScrobblePercent,
                                position = position,
                            )
                        }
                    }
                }
            }

            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    SectionLabel(stringResource(R.string.settings_section_integrations))
                    SettingsGroup(rowCount = 2) { index, position ->
                        when (index) {
                            0 -> SettingsToggleCard(
                                icon = Icons.Filled.CloudSync,
                                iconContainer = MaterialTheme.colorScheme.primaryContainer,
                                iconTint = MaterialTheme.colorScheme.onPrimaryContainer,
                                title = stringResource(R.string.settings_discord_rpc),
                                subtitle = if (misc.discordRpcEnabled) {
                                    if (misc.discordConnectedUsername.isNotBlank()) "Connected as @${misc.discordConnectedUsername}"
                                    else "Sharing current track, artist & live progress in Discord"
                                } else {
                                    stringResource(R.string.settings_discord_rpc_sub)
                                },
                                checked = misc.discordRpcEnabled,
                                onCheckedChange = viewModel::setDiscordRpcEnabled,
                                position = position,
                            )
                            1 -> SettingsActionCard(
                                icon = Icons.Filled.CloudSync,
                                iconContainer = MaterialTheme.colorScheme.secondaryContainer,
                                iconTint = MaterialTheme.colorScheme.onSecondaryContainer,
                                title = "Discord Account Gateway Token",
                                subtitle = if (misc.discordConnectedUsername.isNotBlank()) {
                                    "Connected as @${misc.discordConnectedUsername} • Tap to update token"
                                } else if (misc.discordUserToken.isNotBlank()) {
                                    "Token configured • Tap to update"
                                } else {
                                    "Paste user token to enable Mobile Discord Rich Presence"
                                },
                                onClick = { showDiscordTokenDialog = true },
                                position = position,
                            )
                        }
                    }
                }
            }

            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    SectionLabel(stringResource(R.string.settings_section_data))
                    SettingsGroup(rowCount = 4) { index, position ->
                        when (index) {
                            0 -> SettingsActionCard(
                                icon = Icons.Filled.RestartAlt,
                                iconContainer = MaterialTheme.colorScheme.secondaryContainer,
                                iconTint = MaterialTheme.colorScheme.onSecondaryContainer,
                                title = stringResource(R.string.settings_excluded_songs),
                                subtitle = "${state.recommendationExclusionCount} songs excluded",
                                onClick = onOpenExcludedSongs,
                                position = position,
                            )
                            1 -> SettingsActionCard(
                                icon = Icons.Filled.History,
                                iconContainer = MaterialTheme.colorScheme.tertiaryContainer,
                                iconTint = MaterialTheme.colorScheme.onTertiaryContainer,
                                title = stringResource(R.string.settings_reset_stats),
                                subtitle = stringResource(R.string.settings_reset_stats_sub),
                                onClick = viewModel::requestResetStats,
                                position = position,
                            )
                            2 -> SettingsActionCard(
                                icon = Icons.Filled.Delete,
                                iconContainer = MaterialTheme.colorScheme.secondaryContainer,
                                iconTint = MaterialTheme.colorScheme.onSecondaryContainer,
                                title = "Clear Temporary Cache",
                                subtitle = "Cached audio chunks & images: $cacheSizeText • Tap to clear",
                                onClick = viewModel::clearCache,
                                position = position,
                            )
                            3 -> SettingsActionCard(
                                icon = Icons.Filled.Delete,
                                iconContainer = MaterialTheme.colorScheme.errorContainer,
                                iconTint = MaterialTheme.colorScheme.onErrorContainer,
                                title = stringResource(R.string.settings_clear_all),
                                subtitle = stringResource(R.string.settings_clear_all_sub),
                                danger = true,
                                onClick = viewModel::requestClearAllData,
                                position = position,
                            )
                        }
                    }
                }
            }

            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    SectionLabel(stringResource(R.string.settings_section_backup))
                    SettingsGroup(rowCount = 2) { index, position ->
                        when (index) {
                            0 -> SettingsActionCard(
                                icon = Icons.Filled.Backup,
                                iconContainer = MaterialTheme.colorScheme.primaryContainer,
                                iconTint = MaterialTheme.colorScheme.onPrimaryContainer,
                                title = stringResource(R.string.settings_backup),
                                subtitle = stringResource(R.string.settings_backup_sub),
                                onClick = {
                                    runCatching { backupLauncher.launch("lastwave-backup-${System.currentTimeMillis()}.json") }
                                        .onFailure { viewModel.showToast("No file picker is available") }
                                },
                                position = position,
                            )
                            1 -> SettingsActionCard(
                                icon = Icons.Filled.CloudDownload,
                                iconContainer = MaterialTheme.colorScheme.primaryContainer,
                                iconTint = MaterialTheme.colorScheme.onPrimaryContainer,
                                title = stringResource(R.string.settings_restore),
                                subtitle = stringResource(R.string.settings_restore_sub),
                                onClick = {
                                    runCatching { restoreLauncher.launch(arrayOf("*/*")) }
                                        .onFailure { viewModel.showToast("No file picker is available") }
                                },
                                position = position,
                            )
                        }
                    }
                }
            }

            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    SectionLabel(stringResource(R.string.settings_section_about))
                    SettingsGroup(rowCount = 4) { index, position ->
                        when (index) {
                            0 -> SettingsActionCard(
                                icon = Icons.Filled.Refresh,
                                iconContainer = MaterialTheme.colorScheme.primaryContainer,
                                iconTint = MaterialTheme.colorScheme.onPrimaryContainer,
                                title = "Check for Updates",
                                subtitle = when {
                                    updateInfo.isChecking -> "Checking GitHub for updates..."
                                    !updateInfo.message.isNullOrBlank() -> updateInfo.message.orEmpty()
                                    else -> "Check GitHub releases for updates"
                                },
                                onClick = {
                                    if (updateInfo.isUpdateAvailable) {
                                        viewModel.openUpdate(context)
                                    } else {
                                        viewModel.checkForUpdates()
                                    }
                                },
                                position = position,
                            )
                            1 -> SettingsActionCard(
                                icon = Icons.Filled.Code,
                                iconContainer = MaterialTheme.colorScheme.primaryContainer,
                                iconTint = MaterialTheme.colorScheme.onPrimaryContainer,
                                title = stringResource(R.string.settings_source_code),
                                subtitle = "github.com/Specttre404/AnantaDhvani",
                                onClick = {
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/Specttre404/AnantaDhvani"))
                                    if (!startActivitySafely(context, intent)) {
                                        viewModel.showToast("No browser is available")
                                    }
                                },
                                position = position,
                            )
                            2 -> SettingsActionCard(
                                icon = Icons.AutoMirrored.Filled.Send,
                                iconContainer = MaterialTheme.colorScheme.tertiaryContainer,
                                iconTint = MaterialTheme.colorScheme.onTertiaryContainer,
                                title = "Developer & Community (@Ishan____404)",
                                subtitle = "Follow updates and connect on X (Twitter)",
                                onClick = {
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://x.com/Ishan____404"))
                                    if (!startActivitySafely(context, intent)) {
                                        viewModel.showToast("No browser is available")
                                    }
                                },
                                position = position,
                            )
                            3 -> SettingsActionCard(
                                icon = Icons.Filled.Code,
                                iconContainer = MaterialTheme.colorScheme.secondaryContainer,
                                iconTint = MaterialTheme.colorScheme.onSecondaryContainer,
                                title = stringResource(R.string.settings_diagnostics),
                                subtitle = stringResource(R.string.settings_diagnostics_sub),
                                onClick = { viewModel.exportDiagnostics() },
                                position = position,
                            )
                        }
                    }
                    Spacer(Modifier.height(4.dp))

                    AboutCard(versionName = appVersionName(context))
                }
            }
        }
        }
    }
}
}
}

    // -- Custom color wheel dialog (par 8.4) --
    if (state.showColorWheel) {
        ColorWheelSheet(onDismiss = viewModel::dismissColorWheel, onApply = viewModel::applyCustomColor)
    }

    // -- Reset listening stats confirm --
    if (state.showResetStatsConfirm) {
        AlertDialog(
            onDismissRequest = viewModel::dismissResetStatsConfirm,
            title = { Text(stringResource(R.string.dialog_reset_stats_title)) },
            text = { Text(stringResource(R.string.dialog_reset_stats_text)) },
            confirmButton = { TextButton(onClick = viewModel::confirmResetStats) { Text(stringResource(R.string.dialog_reset_stats_confirm)) } },
            dismissButton = { TextButton(onClick = viewModel::dismissResetStatsConfirm) { Text(stringResource(R.string.common_cancel)) } },
        )
    }

    // -- Clear-all-data confirm --
    if (state.showClearAllConfirm) {
        AlertDialog(
            onDismissRequest = viewModel::dismissClearAllConfirm,
            title = { Text(stringResource(R.string.dialog_clear_all_title)) },
            text = { Text(stringResource(R.string.dialog_clear_all_text)) },
            confirmButton = { TextButton(onClick = { viewModel.confirmClearAllData(onLoggedOut) }) { Text(stringResource(R.string.dialog_clear_all_confirm)) } },
            dismissButton = { TextButton(onClick = viewModel::dismissClearAllConfirm) { Text(stringResource(R.string.common_cancel)) } },
        )
    }

    // -- YouTube Music disconnect confirm --
    if (showYtDisconnectConfirm) {
        AlertDialog(
            onDismissRequest = { showYtDisconnectConfirm = false },
            title = { Text(stringResource(R.string.dialog_disconnect_yt_title)) },
            text = { Text(stringResource(R.string.dialog_disconnect_yt_text)) },
            confirmButton = {
                TextButton(onClick = {
                    showYtDisconnectConfirm = false
                    viewModel.disconnectYouTube()
                }) { Text(stringResource(R.string.common_disconnect)) }
            },
            dismissButton = {
                TextButton(onClick = { showYtDisconnectConfirm = false }) { Text(stringResource(R.string.common_cancel)) }
            },
        )
    }

    // -- Restore confirm --
    if (state.showRestoreConfirm) {        val isPlaylistMirror = state.pendingRestoreKind == PendingRestoreKind.PLAYLIST_MIRROR
        AlertDialog(
            onDismissRequest = viewModel::dismissRestoreConfirm,
            title = { Text(if (isPlaylistMirror) "Sync playlist JSON?" else "Restore backup?") },
            text = {
                Text(
                    if (isPlaylistMirror) {
                        "This will merge ${state.pendingRestorePlaylistCount ?: 0} playlist(s) from the local JSON file and reconnect automatic syncing."
                    } else {
                        "This will replace your current data with ${state.pendingRestorePlaylistCount ?: 0} playlist(s) and all settings from the backup file."
                    },
                )
            },
            confirmButton = { TextButton(onClick = { viewModel.confirmRestore(onBack) }) { Text(if (isPlaylistMirror) stringResource(R.string.common_sync) else stringResource(R.string.common_restore)) } },
            dismissButton = { TextButton(onClick = viewModel::dismissRestoreConfirm) { Text(stringResource(R.string.common_cancel)) } },
        )
    }

    // -- App language picker (Settings -> Language) --
    if (showLanguageDialog) {
        AlertDialog(
            onDismissRequest = { showLanguageDialog = false },
            title = { Text(stringResource(R.string.settings_language_dialog_title)) },
            text = {
                Column(
                    modifier = Modifier.verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    AppLanguage.SELECTABLE.forEach { language ->
                        val selected = language == currentLanguage
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(14.dp))
                                .clickable {
                                    viewModel.setAppLanguage(language)
                                    showLanguageDialog = false
                                }
                                .padding(horizontal = 8.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            RadioButton(
                                selected = selected,
                                onClick = {
                                    viewModel.setAppLanguage(language)
                                    showLanguageDialog = false
                                },
                            )
                            Spacer(Modifier.width(10.dp))
                            Column(Modifier.weight(1f)) {
                                Text(
                                    language.nativeDisplayName(),
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                                )
                                if (language == AppLanguage.SYSTEM) {
                                    Text(
                                        stringResource(R.string.settings_language_system_sub),
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    )
                                }
                            }
                            if (selected) {
                                Icon(
                                    Icons.Filled.Check,
                                    contentDescription = stringResource(R.string.common_selected),
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(18.dp),
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showLanguageDialog = false }) {
                    Text(stringResource(R.string.common_done))
                }
            },
        )
    }

    // -- Enable Scrobbling password dialog --
    if (state.showSessionKeyDialog) {
        var password by remember { mutableStateOf("") }
        AlertDialog(
            onDismissRequest = viewModel::dismissSessionKeyDialog,
            title = { Text("Enable scrobbling") },
            text = {
                Column {
                    Text(
                        "Last.fm only allows scrobbling through a signed session, and the only way to get one without a browser is with your password. It's sent once, directly to Last.fm over HTTPS, and never stored.",
                        style = MaterialTheme.typography.bodyMedium,
                    )
                    Spacer(Modifier.height(16.dp))
                    androidx.compose.material3.OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Last.fm password") },
                        singleLine = true,
                        visualTransformation = androidx.compose.ui.text.input.PasswordVisualTransformation(),
                        isError = state.sessionKeyError != null,
                        supportingText = state.sessionKeyError?.let { { Text(it, color = MaterialTheme.colorScheme.error) } },
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = { viewModel.submitPassword(password) },
                    enabled = !state.sessionKeyLoading,
                ) {
                    if (state.sessionKeyLoading) {
                        com.lastwave.app.ui.common.ExpressiveInlineLoadingIndicator(size = 18.dp)
                    } else {
                        Text("Enable")
                    }
                }
            },
            dismissButton = { TextButton(onClick = viewModel::dismissSessionKeyDialog) { Text("Cancel") } },
        )
    }

    val exportEqLauncher = androidx.activity.compose.rememberLauncherForActivityResult(
        contract = androidx.activity.result.contract.ActivityResultContracts.CreateDocument("application/json"),
    ) { uri ->
        if (uri != null) {
            runCatching {
                context.contentResolver.openOutputStream(uri)?.use { out ->
                    out.write(viewModel.exportCustomEqJson().toByteArray())
                }
                viewModel.showToast("EQ profile exported successfully")
            }.onFailure {
                viewModel.showToast("Failed to export EQ profile")
            }
        }
    }

    val importEqLauncher = androidx.activity.compose.rememberLauncherForActivityResult(
        contract = androidx.activity.result.contract.ActivityResultContracts.OpenDocument(),
    ) { uri ->
        if (uri != null) {
            runCatching {
                val json = context.contentResolver.openInputStream(uri)?.use { inp ->
                    inp.bufferedReader().readText()
                }
                if (!json.isNullOrBlank()) {
                    viewModel.importCustomEqJson(json)
                }
            }.onFailure {
                viewModel.showToast("Failed to import EQ profile")
            }
        }
    }

    // -- 31-band studio equalizer --
    if (showEqSheet) {
        EqualizerSheet(
            eq = eq,
            onDismiss = { showEqSheet = false },
            onSetEnabled = viewModel::setEqualizerEnabled,
            onPickPreset = viewModel::applyEqPreset,
            onBandPreview = viewModel::previewEqBandGain,
            onBandChange = viewModel::setEqBandGain,
            onPreampPreview = viewModel::previewPreampDb,
            onPreampChange = viewModel::setPreampDb,
            onFilterQChange = viewModel::setFilterQ,
            onSingleBandQChange = viewModel::setSingleBandQ,
            onCopyEqCode = {
                val json = viewModel.exportCustomEqJson()
                val clipboard = context.getSystemService(android.content.Context.CLIPBOARD_SERVICE) as? android.content.ClipboardManager
                clipboard?.setPrimaryClip(android.content.ClipData.newPlainText("EQ Profile JSON", json))
                viewModel.showToast("EQ Code copied to clipboard")
            },
            onExportProfile = { exportEqLauncher.launch("lastwavex_eq_${eq.presetName.lowercase().replace(' ', '_')}.json") },
            onImportProfile = { importEqLauncher.launch(arrayOf("application/json", "*/*")) },
        )
    }

    if (showAutoEqSheet) {
        ModalBottomSheet(
            onDismissRequest = { showAutoEqSheet = false },
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text(
                    "AutoEQ Headphone Targets",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                OutlinedTextField(
                    value = autoEqSearchQuery,
                    onValueChange = { autoEqSearchQuery = it },
                    placeholder = { Text("Search 4,000+ headphones (e.g. HD 600, Sony...)") },
                    leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp)
                )
                val profiles = remember(autoEqSearchQuery) {
                    viewModel.searchAutoEqProfiles(autoEqSearchQuery)
                }
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 420.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(profiles) { profile ->
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = MaterialTheme.colorScheme.surfaceContainerLow,
                            onClick = {
                                viewModel.applyAutoEqProfile(profile)
                                showAutoEqSheet = false
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Filled.Headset, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                                Spacer(Modifier.width(12.dp))
                                Column(Modifier.weight(1f)) {
                                    Text(profile.name, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold)
                                    Text(profile.brand, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                                Text("Harman Target", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
                            }
                        }
                    }
                }
                Spacer(Modifier.height(16.dp))
            }
        }
    }

    if (showWebDavDialog) {
        AlertDialog(
            onDismissRequest = { showWebDavDialog = false },
            title = { Text(if (webDavMode == "backup") "WebDAV Cloud Backup" else "WebDAV Restore") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Enter your Nextcloud, ownCloud, or WebDAV server details:", style = MaterialTheme.typography.bodySmall)
                    OutlinedTextField(value = webDavUrl, onValueChange = { webDavUrl = it }, label = { Text("Server URL (https://...)") }, singleLine = true)
                    OutlinedTextField(value = webDavUser, onValueChange = { webDavUser = it }, label = { Text("Username") }, singleLine = true)
                    OutlinedTextField(value = webDavPass, onValueChange = { webDavPass = it }, label = { Text("Password / App Token") }, singleLine = true)
                    OutlinedTextField(value = webDavKey, onValueChange = { webDavKey = it }, label = { Text("AES-256 Encryption Key") }, singleLine = true)
                }
            },
            confirmButton = {
                Button(onClick = {
                    if (webDavMode == "backup") {
                        viewModel.backupToWebDav(webDavUrl, webDavUser, webDavPass, webDavKey)
                    } else {
                        viewModel.restoreFromWebDav(webDavUrl, webDavUser, webDavPass, webDavKey)
                    }
                    showWebDavDialog = false
                }) {
                    Text(if (webDavMode == "backup") "Start Backup" else "Start Restore")
                }
            },
            dismissButton = {
                TextButton(onClick = { showWebDavDialog = false }) { Text("Cancel") }
            }
        )
    }

    // -- Experimental Lyrics Animation Sheet --
    if (showLyricsAnimationSheet) {
        LyricsAnimationSheet(
            version = misc.lyricsUiVersion,
            wordByWord = misc.wordByWordLyrics,
            onWordByWordChange = viewModel::setWordByWordLyrics,
            showTranslation = misc.showLyricsTranslation,
            onShowTranslationChange = viewModel::setShowLyricsTranslation,
            showPhonetic = misc.showLyricsPhonetic,
            onShowPhoneticChange = viewModel::setShowLyricsPhonetic,
            onSelectVersion = viewModel::setLyricsUiVersion,
            current = misc.lyricsAnimation,
            onSelect = {
                viewModel.setLyricsAnimation(it)
                showLyricsAnimationSheet = false
            },
            onDismiss = { showLyricsAnimationSheet = false },
        )
    }

    // -- Player Layout Style Sheet --
    if (showPlayerStyleSheet) {
        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        val haptic = androidx.compose.ui.platform.LocalHapticFeedback.current
        ModalBottomSheet(
            onDismissRequest = { showPlayerStyleSheet = false },
            sheetState = sheetState,
            shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .adaptiveContentWidth(maxWidth = 640.dp)
                    .align(Alignment.CenterHorizontally)
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 24.dp + safeDrawingBottomPadding()),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Text(
                    "Player Layout Architecture",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    "Select M3 Expressive layout structure for Now Playing",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )

                com.lastwave.app.data.local.PlayerStyle.entries.forEach { style ->
                    val isSelected = misc.playerStyle == style
                    Surface(
                        onClick = {
                            haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress)
                            viewModel.setPlayerStyle(style)
                            showPlayerStyleSheet = false
                        },
                        shape = RoundedCornerShape(16.dp),
                        color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceContainerHigh,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(style.title, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
                                Text(style.description, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            if (isSelected) {
                                Icon(Icons.Filled.Check, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                            }
                        }
                    }
                }
            }
        }
    }

    // -- Player Background Style Sheet --
    if (showPlayerBgStyleSheet) {
        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        val haptic = androidx.compose.ui.platform.LocalHapticFeedback.current
        ModalBottomSheet(
            onDismissRequest = { showPlayerBgStyleSheet = false },
            sheetState = sheetState,
            shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .adaptiveContentWidth(maxWidth = 640.dp)
                    .align(Alignment.CenterHorizontally)
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 24.dp + safeDrawingBottomPadding()),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Text(
                    "Player Background Canvas",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    "Select iOS Liquid Glass or M3 canvas backdrop renderer",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )

                com.lastwave.app.data.local.PlayerBackgroundStyle.entries.forEach { bgStyle ->
                    val isSelected = misc.playerBackgroundStyle == bgStyle
                    Surface(
                        onClick = {
                            haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress)
                            viewModel.setPlayerBackgroundStyle(bgStyle)
                            showPlayerBgStyleSheet = false
                        },
                        shape = RoundedCornerShape(16.dp),
                        color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceContainerHigh,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(bgStyle.title, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
                                Text(bgStyle.description, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            if (isSelected) {
                                Icon(Icons.Filled.Check, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                            }
                        }
                    }
                }
            }
        }
    }

    // -- Selective Playlist Sync sheet --
    if (showSyncPlaylistsSheet) {
        SyncPlaylistsSheet(
            playlists = allPlaylists,
            syncedIds = syncedPlaylistIds,
            onToggleSync = viewModel::togglePlaylistSync,
            onSelectAll = viewModel::selectAllPlaylistsForSync,
            onDismiss = { showSyncPlaylistsSheet = false },
        )
    }

    if (showDiscordTokenDialog) {
        var tokenInput by remember { mutableStateOf(misc.discordUserToken) }
        AlertDialog(
            onDismissRequest = { showDiscordTokenDialog = false },
            title = { Text("Discord Account Token") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        "Paste your Discord user authorization token to display live listening activity on your Discord mobile profile.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    OutlinedTextField(
                        value = tokenInput,
                        onValueChange = { tokenInput = it },
                        label = { Text("User Token") },
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.setDiscordUserToken(tokenInput)
                        showDiscordTokenDialog = false
                    },
                ) {
                    Text("Save & Connect")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDiscordTokenDialog = false }) {
                    Text("Cancel")
                }
            },
        )
    }

    if (showYtLibraryVisibilitySheet) {
        YouTubeLibraryVisibilitySheet(
            playlists = ytAccountPlaylists,
            hiddenIds = hiddenYtLibraryPlaylistIds,
            onSetVisible = viewModel::setYtLibraryPlaylistVisible,
            onSetAllVisible = viewModel::setAllYtLibraryPlaylistsVisible,
            onDismiss = { showYtLibraryVisibilitySheet = false },
        )
    }

    if (showQualityDialog) {
        val wifiTiers = listOf(
            Triple(27, "Max Lossless", "24-bit / 192 kHz Studio FLAC" to "24-BIT"),
            Triple(7, "Hi-Res FLAC", "24-bit / 96 kHz Studio FLAC" to "24-BIT"),
            Triple(6, "CD Lossless", "16-bit / 44.1 kHz FLAC" to "16-BIT"),
            Triple(5, "High Quality", "320 kbps MP3" to "320k"),
            Triple(-1, "Data Saver", "YouTube Music 160k" to "160k"),
        )
        val cellTiers = listOf(
            Triple(-1, "Data Saver / Standard", "YouTube Music 160k" to "160k"),
            Triple(5, "High Quality", "320 kbps MP3" to "320k"),
            Triple(6, "CD Lossless", "16-bit / 44.1 kHz FLAC" to "16-BIT"),
            Triple(27, "Max Lossless", "24-bit / 192 kHz FLAC" to "24-BIT"),
        )
        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        val haptic = androidx.compose.ui.platform.LocalHapticFeedback.current

        ModalBottomSheet(
            onDismissRequest = { showQualityDialog = false },
            sheetState = sheetState,
            shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
            dragHandle = {
                Surface(
                    shape = RoundedCornerShape(50),
                    color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f),
                    modifier = Modifier
                        .padding(top = 12.dp, bottom = 8.dp)
                        .size(width = 36.dp, height = 4.dp),
                ) {}
            },
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .adaptiveContentWidth(maxWidth = 640.dp)
                    .align(Alignment.CenterHorizontally)
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 24.dp + safeDrawingBottomPadding()),
                verticalArrangement = Arrangement.spacedBy(14.dp),
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 4.dp),
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(MaterialTheme.colorScheme.primaryContainer),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            Icons.Filled.HighQuality,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.size(24.dp),
                        )
                    }
                    Spacer(Modifier.width(14.dp))
                    Column {
                        Text(
                            "Streaming Quality & Data Saver",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                        )
                        Text(
                            "Configure Wi-Fi and Mobile Data qualities separately",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }

                SettingsToggleCard(
                    icon = Icons.Filled.AutoAwesome,
                    iconContainer = MaterialTheme.colorScheme.tertiaryContainer,
                    iconTint = MaterialTheme.colorScheme.onTertiaryContainer,
                    title = stringResource(R.string.settings_auto_data_saver),
                    subtitle = stringResource(R.string.settings_auto_data_saver_sub),
                    checked = misc.autoDataSaverEnabled,
                    onCheckedChange = viewModel::setAutoDataSaverEnabled,
                )

                Text(
                    stringResource(R.string.settings_wifi_quality),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                )

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    wifiTiers.forEach { (qualityId, title, meta) ->
                        val (subtitle, badge) = meta
                        val isSelected = if (misc.autoDataSaverEnabled) misc.wifiStreamingQuality == qualityId else misc.losslessQuality == qualityId
                        Surface(
                            onClick = {
                                haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress)
                                viewModel.setWifiStreamingQuality(qualityId)
                                viewModel.setLosslessQuality(qualityId)
                            },
                            shape = RoundedCornerShape(16.dp),
                            color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceContainerHigh,
                            border = if (isSelected) androidx.compose.foundation.BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary) else null,
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        title,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                                        color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface,
                                    )
                                    Text(
                                        subtitle,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f) else MaterialTheme.colorScheme.onSurfaceVariant,
                                    )
                                }
                                if (isSelected) {
                                    Icon(Icons.Filled.Check, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                                }
                            }
                        }
                    }
                }

                if (misc.autoDataSaverEnabled) {
                    Text(
                        stringResource(R.string.settings_cellular_quality),
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                    )

                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        cellTiers.forEach { (qualityId, title, meta) ->
                            val (subtitle, badge) = meta
                            val isSelected = misc.cellularStreamingQuality == qualityId
                            Surface(
                                onClick = {
                                    haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress)
                                    viewModel.setCellularStreamingQuality(qualityId)
                                },
                                shape = RoundedCornerShape(16.dp),
                                color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceContainerHigh,
                                border = if (isSelected) androidx.compose.foundation.BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary) else null,
                                modifier = Modifier.fillMaxWidth(),
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            title,
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                                            color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface,
                                        )
                                        Text(
                                            subtitle,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f) else MaterialTheme.colorScheme.onSurfaceVariant,
                                        )
                                    }
                                    if (isSelected) {
                                        Icon(Icons.Filled.Check, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showDownloadQualityDialog) {
        val downloadTiers = listOf(
            Triple(27, "Max Quality", "Up to 24-bit / 192 kHz • Studio Master FLAC" to "24-BIT / 192k"),
            Triple(7, "Hi-Res Audio", "24-bit / 96 kHz • Studio FLAC" to "24-BIT / 96k"),
            Triple(6, "CD Lossless", "16-bit / 44.1 kHz • Bit-Exact CD FLAC" to "16-BIT / 44.1k"),
            Triple(5, "Standard Quality", "320 kbps • High-Bitrate MP3" to "320 kbps"),
            Triple(-1, "YouTube Music", "128-256 kbps • YouTube Music AAC / Opus stream" to "YOUTUBE"),
        )
        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        val haptic = androidx.compose.ui.platform.LocalHapticFeedback.current

        ModalBottomSheet(
            onDismissRequest = { showDownloadQualityDialog = false },
            sheetState = sheetState,
            shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
            dragHandle = {
                Surface(
                    shape = RoundedCornerShape(50),
                    color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f),
                    modifier = Modifier
                        .padding(top = 12.dp, bottom = 8.dp)
                        .size(width = 36.dp, height = 4.dp),
                ) {}
            },
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .adaptiveContentWidth(maxWidth = 640.dp)
                    .align(Alignment.CenterHorizontally)
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 24.dp + safeDrawingBottomPadding()),
                verticalArrangement = Arrangement.spacedBy(14.dp),
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 4.dp),
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(MaterialTheme.colorScheme.secondaryContainer),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            Icons.Filled.CloudDownload,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSecondaryContainer,
                            modifier = Modifier.size(24.dp),
                        )
                    }
                    Spacer(Modifier.width(14.dp))
                    Column {
                        Text(
                            "Download Quality",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                        )
                        Text(
                            "Select offline audio resolution & bit depth",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }

                Text(
                    "Lossless downloads provide bit-exact studio quality (FLAC/MP3). If your chosen quality is unavailable, LastWave automatically downloads the higher quality tier above it (or falls back to YouTube Music if unavailable in lossless).",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline,
                )

                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    downloadTiers.forEach { (qualityId, title, meta) ->
                        val (subtitle, badge) = meta
                        val isSelected = misc.downloadQuality == qualityId
                        Surface(
                            onClick = {
                                haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress)
                                viewModel.setDownloadQuality(qualityId)
                                showDownloadQualityDialog = false
                            },
                            shape = RoundedCornerShape(20.dp),
                            color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceContainerHigh,
                            border = if (isSelected) androidx.compose.foundation.BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary) else null,
                            shadowElevation = if (isSelected) 3.dp else 0.dp,
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 18.dp, vertical = 16.dp),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            title,
                                            style = MaterialTheme.typography.bodyLarge,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                                            color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface,
                                        )
                                        Spacer(Modifier.width(8.dp))
                                        Surface(
                                            shape = RoundedCornerShape(6.dp),
                                            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceContainerHighest,
                                        ) {
                                            Text(
                                                badge,
                                                style = MaterialTheme.typography.labelSmall,
                                                fontWeight = FontWeight.Bold,
                                                color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                            )
                                        }
                                    }
                                    Spacer(Modifier.height(4.dp))
                                    Text(
                                        subtitle,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f) else MaterialTheme.colorScheme.onSurfaceVariant,
                                    )
                                }

                                if (isSelected) {
                                    Box(
                                        modifier = Modifier
                                            .size(28.dp)
                                            .clip(CircleShape)
                                            .background(MaterialTheme.colorScheme.primary),
                                        contentAlignment = Alignment.Center,
                                    ) {
                                        Icon(
                                            Icons.Filled.Check,
                                            contentDescription = "Selected",
                                            tint = MaterialTheme.colorScheme.onPrimary,
                                            modifier = Modifier.size(18.dp),
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showYouTubeImportSheet) {
        YouTubeImportSheet(
            onDismiss = { showYouTubeImportSheet = false },
            innerTube = viewModel.innerTube,
            importManager = viewModel.playlistImportManager,
            onImportSuccess = { saved ->
                viewModel.showToast("Imported \"${saved.title}\" (${saved.tracks.size} tracks)")
            },
        )
    }

    state.toastMessage?.let { msg ->
        LaunchedEffect(msg) {
            kotlinx.coroutines.delay(3000)
            viewModel.dismissToast()
        }
        Box(
            Modifier
                .fillMaxSize()
                .safeHorizontalContentPadding()
                .padding(
                    bottom = 24.dp +
                        LocalMiniPlayerScrollClearance.current +
                        safeDrawingBottomPadding(),
                ),
            contentAlignment = Alignment.BottomCenter,
        ) {
            Surface(shape = ExpressivePillShape, color = MaterialTheme.colorScheme.inverseSurface, tonalElevation = 6.dp) {
                Text(msg, color = MaterialTheme.colorScheme.inverseOnSurface, modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp))
            }
        }
    }
}
}

private fun appVersionName(context: android.content.Context): String = try {
    context.packageManager.getPackageInfo(context.packageName, 0).versionName ?: "1.2.1"
} catch (error: Exception) {
    "1.2.1"
} catch (error: LinkageError) {
    "1.2.1"
}

/** Small tap-scale used across the row-style cards on this screen for a
 *  softer, springier press response than the plain ripple alone gives. */
@Composable
private fun rememberPressScale(interactionSource: MutableInteractionSource): Float {
    val pressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.97f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessMedium),
        label = "pressScale",
    )
    return scale
}

@Composable
private fun SectionLabel(text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(start = 4.dp, bottom = 4.dp, top = 6.dp),
    ) {
        Box(
            modifier = Modifier
                .size(width = 4.dp, height = 16.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(MaterialTheme.colorScheme.primary)
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text = text.uppercase(),
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            letterSpacing = androidx.compose.ui.unit.TextUnit(1.1f, androidx.compose.ui.unit.TextUnitType.Sp),
        )
    }
}



@Composable
private fun IconBadge(icon: ImageVector, container: Color, tint: Color, modifier: Modifier = Modifier) {
    Box(
        modifier
            .size(44.dp)
            .clip(IconBadgeShape)
            .background(container),
        contentAlignment = Alignment.Center,
    ) {
        Icon(icon, contentDescription = null, tint = tint, modifier = Modifier.size(22.dp))
    }
}

@Composable
private fun ThemeModeSelectorCard(
    currentThemeMode: ThemeMode,
    onSelectThemeMode: (ThemeMode) -> Unit,
    position: GroupPosition = GroupPosition.SINGLE,
) {
    val shape = groupShape(position)
    val liquidGlass = LocalLiquidGlass.current

    Card(
        shape = shape,
        colors = CardDefaults.cardColors(containerColor = liquidGlassContainerColor(MaterialTheme.colorScheme.surfaceContainerHigh)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        modifier = Modifier
            .fillMaxWidth()
            .liquidGlassChrome(shape, liquidGlass),
    ) {
        Column(
            Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconBadge(
                    Icons.Filled.BrightnessAuto,
                    MaterialTheme.colorScheme.primaryContainer,
                    MaterialTheme.colorScheme.onPrimaryContainer,
                )
                Spacer(Modifier.width(14.dp))
                Column(Modifier.weight(1f)) {
                    Text(
                        stringResource(R.string.settings_theme_mode),
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium,
                    )
                    Text(
                        stringResource(R.string.settings_theme_mode_sub),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
            Spacer(Modifier.height(14.dp))
            val items = listOf(
                ConnectedButtonItem(stringResource(R.string.theme_mode_system), Icons.Filled.BrightnessAuto),
                ConnectedButtonItem(stringResource(R.string.theme_mode_light), Icons.Filled.LightMode),
                ConnectedButtonItem(stringResource(R.string.theme_mode_dark), Icons.Filled.DarkMode),
            )
            val selectedIdx = when (currentThemeMode) {
                ThemeMode.SYSTEM -> 0
                ThemeMode.LIGHT -> 1
                ThemeMode.DARK -> 2
            }
            ConnectedButtonGroup(
                items = items,
                selectedIndex = selectedIdx,
                onSelect = { idx ->
                    val mode = when (idx) {
                        0 -> ThemeMode.SYSTEM
                        1 -> ThemeMode.LIGHT
                        else -> ThemeMode.DARK
                    }
                    onSelectThemeMode(mode)
                },
            )
        }
    }
}

@Composable
private fun SettingsToggleCard(
    icon: ImageVector,
    iconContainer: Color,
    iconTint: Color,
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    position: GroupPosition = GroupPosition.SINGLE,
    enabled: Boolean = true,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val scale = rememberPressScale(interactionSource)
    val shape = groupShape(position)
    val liquidGlass = LocalLiquidGlass.current

    Card(
        onClick = { if (enabled) onCheckedChange(!checked) },
        shape = shape,
        enabled = enabled,
        colors = CardDefaults.cardColors(
            containerColor = liquidGlassContainerColor(MaterialTheme.colorScheme.surfaceContainerHigh),
            disabledContainerColor = liquidGlassContainerColor(MaterialTheme.colorScheme.surfaceContainerHigh.copy(alpha = 0.5f)),
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        interactionSource = interactionSource,
        modifier = Modifier
            .fillMaxWidth()
            .scale(if (enabled) scale else 1f)
            .liquidGlassChrome(shape, liquidGlass),
    ) {
        Row(
            Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconBadge(
                icon,
                if (enabled) iconContainer else iconContainer.copy(alpha = 0.5f),
                if (enabled) iconTint else iconTint.copy(alpha = 0.5f),
            )
            Spacer(Modifier.width(14.dp))
            Column(Modifier.weight(1f)) {
                Text(
                    title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    color = if (enabled) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.45f),
                )
                Text(
                    subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = if (enabled) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.45f),
                    maxLines = 2,
                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis,
                )
            }
            Spacer(Modifier.width(8.dp))
            Switch(
                checked = checked,
                enabled = enabled,
                onCheckedChange = if (enabled) onCheckedChange else null,
                thumbContent = if (checked) {
                    {
                        Icon(
                            Icons.Filled.Check,
                            contentDescription = null,
                            modifier = Modifier.size(SwitchDefaults.IconSize),
                        )
                    }
                } else null,
            )
        }
    }
}

/** The percent-of-track threshold before a scrobble is submitted — same
 *  idea as Pano Scrobbler's "Percent" slider, minus its separate parallel
 *  "Minutes" slider: Last.fm's own scrobble rule already caps the wait at
 *  4 minutes regardless of percent, so that second slider would only ever
 *  matter for tracks over 8 minutes long, a genuine edge case not worth
 *  the extra UI here. */
@Composable
private fun ScrobbleThresholdRow(percent: Int, onPercentChange: (Int) -> Unit, position: GroupPosition = GroupPosition.SINGLE) {
    var sliderValue by remember(percent) { mutableStateOf(percent.coerceIn(25, 90).toFloat()) }
    val shape = groupShape(position)
    val liquidGlass = LocalLiquidGlass.current
    Card(
        shape = shape,
        colors = CardDefaults.cardColors(containerColor = liquidGlassContainerColor(MaterialTheme.colorScheme.surfaceContainerHigh)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        modifier = Modifier
            .fillMaxWidth()
            .liquidGlassChrome(shape, liquidGlass),
    ) {
        Column(Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconBadge(Icons.Filled.Timer, MaterialTheme.colorScheme.secondaryContainer, MaterialTheme.colorScheme.onSecondaryContainer)
                Spacer(Modifier.width(14.dp))
                Column(Modifier.weight(1f)) {
                    Text("Scrobble after", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium)
                    Text(
                        "${sliderValue.toInt()}% played (capped at 4 min)",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
            Slider(
                value = sliderValue,
                onValueChange = { sliderValue = it },
                onValueChangeFinished = { onPercentChange(sliderValue.toInt()) },
                valueRange = 25f..90f,
                steps = 12,
                modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
            )
        }
    }
}

@Composable
private fun CrossfadeDurationRow(
    seconds: Int,
    onSecondsChange: (Int) -> Unit,
    position: GroupPosition = GroupPosition.SINGLE,
) {
    val haptic = androidx.compose.ui.platform.LocalHapticFeedback.current
    var sliderValue by remember { mutableStateOf(seconds.coerceIn(1, 12).toFloat()) }
    var isDragging by remember { mutableStateOf(false) }
    LaunchedEffect(seconds) {
        if (!isDragging) sliderValue = seconds.coerceIn(1, 12).toFloat()
    }
    val shape = groupShape(position)
    val liquidGlass = LocalLiquidGlass.current

    val blendStyle = when (sliderValue.roundToInt()) {
        in 1..2 -> "Quick DJ overlap"
        in 3..5 -> "Standard smooth blend"
        in 6..8 -> "Long musical transition"
        else -> "Extended cinematic crossfade"
    }

    Card(
        shape = shape,
        colors = CardDefaults.cardColors(containerColor = liquidGlassContainerColor(MaterialTheme.colorScheme.surfaceContainerHigh)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        modifier = Modifier
            .fillMaxWidth()
            .liquidGlassChrome(shape, liquidGlass),
    ) {
        Column(Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconBadge(
                    Icons.Filled.Tune,
                    MaterialTheme.colorScheme.secondaryContainer,
                    MaterialTheme.colorScheme.onSecondaryContainer,
                )
                Spacer(Modifier.width(14.dp))
                Column(Modifier.weight(1f)) {
                    Text("Crossfade duration", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium)
                    Text(
                        "${sliderValue.roundToInt()}s \u2022 $blendStyle",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.primaryContainer,
                ) {
                    Text(
                        "${sliderValue.roundToInt()}s",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                    )
                }
            }
            Slider(
                value = sliderValue,
                onValueChange = { value ->
                    isDragging = true
                    val rounded = value.roundToInt().coerceIn(1, 12)
                    if (rounded.toFloat() != sliderValue) {
                        haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.TextHandleMove)
                        sliderValue = rounded.toFloat()
                        onSecondsChange(rounded)
                    }
                },
                onValueChangeFinished = {
                    isDragging = false
                },
                valueRange = 1f..12f,
                steps = 10,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 6.dp),
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text("1s (Tight)", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline)
                Text("6s", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline)
                Text("12s (Long)", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline)
            }
        }
    }
}

@Composable
private fun SettingsActionCard(
    icon: ImageVector,
    iconContainer: Color,
    iconTint: Color,
    title: String,
    subtitle: String,
    danger: Boolean = false,
    onClick: () -> Unit,
    position: GroupPosition = GroupPosition.SINGLE,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val scale = rememberPressScale(interactionSource)
    val titleColor = if (danger) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
    val shape = groupShape(position)
    val liquidGlass = LocalLiquidGlass.current

    Card(
        onClick = onClick,
        shape = shape,
        colors = CardDefaults.cardColors(containerColor = liquidGlassContainerColor(MaterialTheme.colorScheme.surfaceContainerHigh)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        interactionSource = interactionSource,
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale)
            .liquidGlassChrome(shape, liquidGlass),
    ) {
        Row(
            Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconBadge(icon, iconContainer, iconTint)
            Spacer(Modifier.width(14.dp))
            Column(Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium, color = titleColor)
                Text(subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Icon(
                Icons.Filled.ChevronRight,
                contentDescription = null,
                tint = if (danger) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun YouTubeAccountRow(
    accountName: String,
    channelHandle: String?,
    onDisconnect: () -> Unit,
    position: GroupPosition,
) {
    val shape = groupShape(position)
    val liquidGlass = LocalLiquidGlass.current
    Card(
        shape = shape,
        colors = CardDefaults.cardColors(containerColor = liquidGlassContainerColor(MaterialTheme.colorScheme.surfaceContainerHigh)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize()
            .liquidGlassChrome(shape, liquidGlass),
    ) {
        Row(
            Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconBadge(
                Icons.Filled.CloudSync,
                MaterialTheme.colorScheme.primaryContainer,
                MaterialTheme.colorScheme.onPrimaryContainer,
            )
            Spacer(Modifier.width(14.dp))
            Column(Modifier.weight(1f)) {
                Text(
                    "YouTube Music Account",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Text(
                    accountName.ifBlank { "Connected" },
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis,
                )
                if (!channelHandle.isNullOrBlank()) {
                    Text(
                        channelHandle,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
            Spacer(Modifier.width(8.dp))
            OutlinedButton(
                onClick = onDisconnect,
                shape = ExpressivePillShape,
            ) {
                Text("Switch")
            }
            Spacer(Modifier.width(4.dp))
            FilledTonalIconButton(
                onClick = onDisconnect,
                colors = IconButtonDefaults.filledTonalIconButtonColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    contentColor = MaterialTheme.colorScheme.onErrorContainer,
                ),
            ) {
                Icon(Icons.Filled.Logout, contentDescription = "Disconnect")
            }
        }
    }
}

private fun lastSyncSuffix(lastSyncAtMillis: Long): String {
    if (lastSyncAtMillis <= 0L) return ""
    return " \u2022 synced ${relativeTime(lastSyncAtMillis)}"
}

private fun relativeTime(timestampMillis: Long): String {
    if (timestampMillis <= 0L) return "never"
    val delta = System.currentTimeMillis() - timestampMillis
    val minutes = delta / 60_000L
    return when {
        minutes < 1 -> "just now"
        minutes < 60 -> "${minutes}m ago"
        minutes < 60 * 24 -> "${minutes / 60}h ago"
        else -> "${minutes / (60 * 24)}d ago"
    }
}

private const val LAST_FM_CREATE_KEY_URL = "https://www.last.fm/api/account/create"

@Composable
private fun LastFmIntegrationCard(
    isConnected: Boolean,
    username: String,
    connecting: Boolean,
    awaitingApproval: Boolean,
    hasApiKey: Boolean,
    onConnect: () -> Unit,
    onCancel: () -> Unit,
    onDisconnect: () -> Unit,
    onSaveKeys: (String, String) -> Unit,
    onRemoveKey: () -> Unit,
    onOpenCreateKeyPage: () -> Unit,
) {
    var showDisconnectConfirm by remember { mutableStateOf(false) }
    // No shared key exists, so the form starts open until a key is saved.
    var showKeyForm by remember(hasApiKey) { mutableStateOf(!hasApiKey) }
    var keyInput by remember { mutableStateOf("") }
    var secretInput by remember { mutableStateOf("") }

    Card(
        shape = CardOuterShape,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh),
        modifier = Modifier.fillMaxWidth().animateContentSize(),
    ) {
        Column(Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        if (isConnected && username.isNotBlank()) username.take(1).uppercase() else "L",
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium,
                    )
                }
                Spacer(Modifier.width(14.dp))
                Column(Modifier.weight(1f)) {
                    Text(
                        "Last.fm",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Text(
                        if (isConnected && username.isNotBlank()) username else "Not connected",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                    )
                    Text(
                        if (isConnected) "Scrobbles sync globally • Stats from Last.fm"
                        else "Optional • Stats use local listening when disconnected",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
            when {
                isConnected -> {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                        OutlinedButton(
                            onClick = { showDisconnectConfirm = true },
                            shape = ExpressivePillShape,
                            modifier = Modifier.weight(1f),
                        ) { Text("Disconnect") }
                    }
                }
                awaitingApproval || connecting -> {
                    com.lastwave.app.ui.common.ExpressiveLoadingIndicator(
                        message = if (connecting) "Connecting to Last.fm…" else "Waiting for approval in the browser…",
                    )
                    Spacer(Modifier.height(4.dp))
                    TextButton(onClick = onCancel) { Text("Cancel") }
                }
                else -> {
                    Button(
                        onClick = onConnect,
                        enabled = hasApiKey,
                        shape = ExpressivePillShape,
                        modifier = Modifier.fillMaxWidth(),
                    ) { Text("Connect Last.fm") }
                    Text(
                        if (hasApiKey) "Approve in your browser. You can disconnect anytime — Stats keep working locally."
                        else "Add your API key below first, then connect.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }

            // ── Bring-your-own-key (required, no shared key): Last.fm
            //    rate-limits per API key, so each person adds their own key
            //    here before connecting.
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Column(Modifier.weight(1f)) {
                        Text(
                            "API key",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Medium,
                        )
                        Text(
                            if (hasApiKey) "Your key is saved"
                            else "Required — get one free, then paste it here",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                    TextButton(onClick = { showKeyForm = !showKeyForm }) {
                        Text(if (showKeyForm) "Hide" else if (hasApiKey) "Change" else "Add key")
                    }
                }
                if (showKeyForm) {
                    androidx.compose.material3.OutlinedTextField(
                        value = keyInput,
                        onValueChange = { keyInput = it.trim() },
                        label = { Text("API key (32 chars)") },
                        singleLine = true,
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth(),
                    )
                    androidx.compose.material3.OutlinedTextField(
                        value = secretInput,
                        onValueChange = { secretInput = it.trim() },
                        label = { Text("Shared secret") },
                        singleLine = true,
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth(),
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                        Button(
                            onClick = {
                                onSaveKeys(keyInput, secretInput)
                                keyInput = ""
                                secretInput = ""
                                showKeyForm = false
                            },
                            enabled = keyInput.length >= 16 && secretInput.length >= 16,
                            shape = ExpressivePillShape,
                            modifier = Modifier.weight(1f),
                        ) { Text("Save key") }
                        if (hasApiKey) {
                            OutlinedButton(
                                onClick = onRemoveKey,
                                shape = ExpressivePillShape,
                            ) { Text("Remove") }
                        }
                    }
                    TextButton(onClick = onOpenCreateKeyPage) {
                        Text("Get a free key at last.fm/api →")
                    }
                } else {
                    TextButton(onClick = onOpenCreateKeyPage) {
                        Text("How to get a free key →")
                    }
                }
            }
        }
    }

    if (showDisconnectConfirm) {
        AlertDialog(
            onDismissRequest = { showDisconnectConfirm = false },
            title = { Text("Disconnect Last.fm?") },
            text = { Text("Global scrobbles pause. Your Stats switch to local listening history — nothing is deleted.") },
            confirmButton = {
                TextButton(onClick = {
                    showDisconnectConfirm = false
                    onDisconnect()
                }) { Text("Disconnect") }
            },
            dismissButton = { TextButton(onClick = { showDisconnectConfirm = false }) { Text("Cancel") } },
        )
    }
}

@Composable
private fun StreamBufferTuningCard(
    minBufferMs: Int,
    maxBufferMs: Int,
    playbackBufferMs: Int,
    onBufferChange: (min: Int, max: Int, start: Int, rebuffer: Int) -> Unit,
) {
    Card(
        shape = CardOuterShape,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.Waves, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(10.dp))
                Column {
                    Text("Stream Buffer Tuning", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text("ExoPlayer buffer duration limits", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            Text("Min Buffer: ${minBufferMs / 1000}s", style = MaterialTheme.typography.labelSmall)
            Slider(
                value = minBufferMs.toFloat(),
                onValueChange = { onBufferChange(it.toInt(), maxBufferMs, playbackBufferMs, 5000) },
                valueRange = 5000f..60000f,
            )
            Text("Max Buffer: ${maxBufferMs / 1000}s", style = MaterialTheme.typography.labelSmall)
            Slider(
                value = maxBufferMs.toFloat(),
                onValueChange = { onBufferChange(minBufferMs, it.toInt(), playbackBufferMs, 5000) },
                valueRange = 15000f..120000f,
            )
        }
    }
}

@Composable
private fun StreamCodecCard(
    currentCodec: com.lastwave.app.data.local.PreferredAudioCodec,
    onSelectCodec: (com.lastwave.app.data.local.PreferredAudioCodec) -> Unit,
) {
    Card(
        shape = CardOuterShape,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.HighQuality, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(10.dp))
                Column {
                    Text("Stream Codec Forcing", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text("Select preferred audio container & codec", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                com.lastwave.app.data.local.PreferredAudioCodec.entries.forEach { codec ->
                    val isSelected = codec == currentCodec
                    FilterChip(
                        selected = isSelected,
                        onClick = { onSelectCodec(codec) },
                        label = { Text(codec.label, style = MaterialTheme.typography.labelSmall) },
                    )
                }
            }
        }
    }
}

@Composable
private fun NotchSettingsCard(
    enabled: Boolean,
    topMarginDp: Int,
    widthDp: Int,
    dismissSec: Int,
    onNotchChange: (enabled: Boolean, topMargin: Int, width: Int, dismiss: Int) -> Unit,
) {
    Card(
        shape = CardOuterShape,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.Apps, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(10.dp))
                Column(Modifier.weight(1f)) {
                    Text("Punch-Hole & Notch Island", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text("Floating dynamic island capsule", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Switch(
                    checked = enabled,
                    onCheckedChange = { onNotchChange(it, topMarginDp, widthDp, dismissSec) },
                )
            }
            if (enabled) {
                Text("Top Offset: ${topMarginDp}dp", style = MaterialTheme.typography.labelSmall)
                Slider(
                    value = topMarginDp.toFloat(),
                    onValueChange = { onNotchChange(enabled, it.toInt(), widthDp, dismissSec) },
                    valueRange = 0f..24f,
                )
                Text("Capsule Width: ${widthDp}dp", style = MaterialTheme.typography.labelSmall)
                Slider(
                    value = widthDp.toFloat(),
                    onValueChange = { onNotchChange(enabled, topMarginDp, it.toInt(), dismissSec) },
                    valueRange = 180f..320f,
                )
            }
        }
    }
}

@Composable
private fun CacheQuotaCard(
    currentQuotaMb: Long,
    onSelectQuotaMb: (Long) -> Unit,
) {
    Card(
        shape = CardOuterShape,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.Download, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(10.dp))
                Column {
                    Text("Disk Cache Quota", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text("Maximum local buffer limit", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            val quotas = listOf(512L to "512 MB", 1024L to "1 GB", 2048L to "2 GB", 5120L to "5 GB", -1L to "Unlimited")
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                quotas.forEach { (mb, label) ->
                    val isSelected = mb == currentQuotaMb
                    FilterChip(
                        selected = isSelected,
                        onClick = { onSelectQuotaMb(mb) },
                        label = { Text(label, style = MaterialTheme.typography.labelSmall) },
                    )
                }
            }
        }
    }
}

@Composable
private fun CrossfeedPresetsCard(
    enabled: Boolean,
    levelDb: Float,
    cutoffHz: Float,
    onToggle: (Boolean) -> Unit,
    onChange: (levelDb: Float, cutoffHz: Float) -> Unit,
) {
    Card(
        shape = CardOuterShape,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.Headset, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(10.dp))
                Column(Modifier.weight(1f)) {
                    Text("Binaural Headphone Crossfeed (BS2B)", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text("Reduces fatigue by simulating acoustic room decay", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Switch(checked = enabled, onCheckedChange = onToggle)
            }
            if (enabled) {
                val presets = listOf(
                    "Bauer" to (4.5f to 700f),
                    "Chu Moy" to (6.0f to 700f),
                    "Jan Meier" to (9.5f to 650f),
                    "Studio" to (3.5f to 800f),
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                    presets.forEach { (name, pair) ->
                        val isSelected = kotlin.math.abs(levelDb - pair.first) < 0.1f && kotlin.math.abs(cutoffHz - pair.second) < 10f
                        FilterChip(
                            selected = isSelected,
                            onClick = { onChange(pair.first, pair.second) },
                            label = { Text(name, style = MaterialTheme.typography.labelSmall) },
                        )
                    }
                }
                Text("Crossfeed Level: ${"%.1f".format(levelDb)} dB", style = MaterialTheme.typography.labelSmall)
                Slider(
                    value = levelDb,
                    onValueChange = { onChange(it, cutoffHz) },
                    valueRange = 3.0f..9.5f,
                )
            }
        }
    }
}

@Composable
private fun SilenceTrimmingCard(
    thresholdDb: Float,
    minDurationMs: Long,
    onChange: (thresholdDb: Float, minDurationMs: Long) -> Unit,
) {
    Card(
        shape = CardOuterShape,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.VolumeOff, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(10.dp))
                Column {
                    Text("Silence Trimming Parameters", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text("Skip leading/trailing dead air in audio streams", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            Text("Threshold: ${"%.1f".format(thresholdDb)} dB", style = MaterialTheme.typography.labelSmall)
            Slider(
                value = thresholdDb,
                onValueChange = { onChange(it, minDurationMs) },
                valueRange = -60.0f..-25.0f,
            )
            Text("Min Silence Duration: ${minDurationMs} ms", style = MaterialTheme.typography.labelSmall)
            Slider(
                value = minDurationMs.toFloat(),
                onValueChange = { onChange(thresholdDb, it.toLong()) },
                valueRange = 50f..1500f,
            )
        }
    }
}

@Composable
private fun MiniPlayerSwipeCard(
    currentStyle: com.lastwave.app.data.local.MiniPlayerSwipeStyle,
    onSelectStyle: (com.lastwave.app.data.local.MiniPlayerSwipeStyle) -> Unit,
) {
    Card(
        shape = CardOuterShape,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.TouchApp, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(10.dp))
                Column {
                    Text("Mini Player Gesture Behavior", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text("Configure swipe gesture on dock bar", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                com.lastwave.app.data.local.MiniPlayerSwipeStyle.entries.forEach { style ->
                    val isSelected = style == currentStyle
                    FilterChip(
                        selected = isSelected,
                        onClick = { onSelectStyle(style) },
                        label = { Text(style.title, style = MaterialTheme.typography.labelSmall) },
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }
        }
    }
}

@Composable
private fun LyricsTypographyCard(
    fontSize: Int,
    lineSpacing: Int,
    blurRadius: Int,
    onChange: (fontSize: Int, lineSpacing: Int, blurRadius: Int) -> Unit,
) {
    Card(
        shape = CardOuterShape,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.Lyrics, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(10.dp))
                Column {
                    Text("Lyrics Typography & Blur", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text("Customize font size, line spacing & inactive line blur", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            Text("Font Size: ${fontSize}sp", style = MaterialTheme.typography.labelSmall)
            Slider(
                value = fontSize.toFloat(),
                onValueChange = { onChange(it.toInt(), lineSpacing, blurRadius) },
                valueRange = 18f..36f,
            )
            Text("Line Spacing: ${lineSpacing}dp", style = MaterialTheme.typography.labelSmall)
            Slider(
                value = lineSpacing.toFloat(),
                onValueChange = { onChange(fontSize, it.toInt(), blurRadius) },
                valueRange = 8f..24f,
            )
            Text("Inactive Blur Radius: ${blurRadius}dp", style = MaterialTheme.typography.labelSmall)
            Slider(
                value = blurRadius.toFloat(),
                onValueChange = { onChange(fontSize, lineSpacing, it.toInt()) },
                valueRange = 0f..8f,
            )
        }
    }
}

@Composable
private fun NavigationBarTabsCard(
    visibleTabs: Set<String>,
    onToggleTab: (String) -> Unit,
) {
    Card(
        shape = CardOuterShape,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.Dashboard, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(10.dp))
                Column {
                    Text("Navigation Bar Tabs", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text("Toggle visibility of bottom navigation bar items", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            val allTabs = listOf("feed" to "Feed", "discover" to "Discover", "playlists" to "Playlists", "stats" to "Stats")
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                allTabs.forEach { (id, label) ->
                    val isVisible = id in visibleTabs
                    FilterChip(
                        selected = isVisible,
                        onClick = { onToggleTab(id) },
                        label = { Text(label, style = MaterialTheme.typography.labelSmall) },
                    )
                }
            }
        }
    }
}

@Composable
private fun LocalWebRemoteCard(
    musicPlayer: com.lastwave.app.playback.MusicPlayer,
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val webServer = remember { LocalMediaWebServer.getInstance(context, musicPlayer) }
    var isServerRunning by remember { mutableStateOf(webServer.isRunning) }
    val serverUrl = remember(isServerRunning) { webServer.getLocalServerUrl() }

    Card(
        shape = CardOuterShape,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.Wifi, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(10.dp))
                Column(Modifier.weight(1f)) {
                    Text("Local Web Remote & Streaming Server", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text("Control & stream live audio on PC browser via local Wi-Fi", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Switch(
                    checked = isServerRunning,
                    onCheckedChange = { checked ->
                        if (checked) {
                            isServerRunning = webServer.startServer()
                        } else {
                            webServer.stopServer()
                            isServerRunning = false
                        }
                    },
                )
            }

            if (isServerRunning) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Text(
                            text = serverUrl,
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                        )
                        OutlinedButton(
                            onClick = {
                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as? android.content.ClipboardManager
                                val clip = android.content.ClipData.newPlainText("Ananta Web Remote", serverUrl)
                                clipboard?.setPrimaryClip(clip)
                                android.widget.Toast.makeText(context, "Copied $serverUrl", android.widget.Toast.LENGTH_SHORT).show()
                            },
                        ) {
                            Text("Copy Link")
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun LibraryFoldersAndExclusionCard(
    settings: MiscSettings,
    onAddScanFolder: (String) -> Unit,
    onRemoveScanFolder: (String) -> Unit,
    onAddExcludedFolder: (String) -> Unit,
    onRemoveExcludedFolder: (String) -> Unit,
) {
    val context = androidx.compose.ui.platform.LocalContext.current

    val whitelistLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocumentTree()
    ) { uri ->
        uri?.let {
            val path = it.path ?: it.toString()
            onAddScanFolder(path)
            android.widget.Toast.makeText(context, "Added to Included Folders", android.widget.Toast.LENGTH_SHORT).show()
        }
    }

    val blacklistLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocumentTree()
    ) { uri ->
        uri?.let {
            val path = it.path ?: it.toString()
            onAddExcludedFolder(path)
            android.widget.Toast.makeText(context, "Added to Excluded Folders", android.widget.Toast.LENGTH_SHORT).show()
        }
    }

    Card(
        shape = CardOuterShape,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.FolderSpecial, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(10.dp))
                Column(Modifier.weight(1f)) {
                    Text("Library Folders & Exclusion Manager", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text("Whitelist custom music folders & blacklist unwanted directories", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }

            Text("Included Folders (Whitelist)", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
            if (settings.customScanFolders.isEmpty()) {
                Text("All device music folders included (Default)", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            } else {
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    settings.customScanFolders.forEach { folder ->
                        Surface(
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.surfaceContainerHighest,
                            contentColor = MaterialTheme.colorScheme.onSurface,
                            onClick = { onRemoveScanFolder(folder) }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(folder.substringAfterLast("/").ifBlank { folder }.take(24), style = MaterialTheme.typography.labelSmall)
                                Icon(Icons.Filled.Close, contentDescription = "Remove", modifier = Modifier.size(14.dp))
                            }
                        }
                    }
                }
            }
            OutlinedButton(
                onClick = { whitelistLauncher.launch(null) },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Icon(Icons.Filled.Add, contentDescription = null, Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text("Add Included Folder via SAF")
            }

            Spacer(Modifier.height(4.dp))

            Text("Excluded Folders (Blacklist)", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
            if (settings.customExcludedFolders.isEmpty()) {
                Text("No custom folders excluded", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            } else {
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    settings.customExcludedFolders.forEach { folder ->
                        Surface(
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.surfaceContainerHighest,
                            contentColor = MaterialTheme.colorScheme.onSurface,
                            onClick = { onRemoveExcludedFolder(folder) }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(folder.substringAfterLast("/").ifBlank { folder }.take(24), style = MaterialTheme.typography.labelSmall)
                                Icon(Icons.Filled.Close, contentDescription = "Remove", modifier = Modifier.size(14.dp))
                            }
                        }
                    }
                }
            }
            OutlinedButton(
                onClick = { blacklistLauncher.launch(null) },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Icon(Icons.Filled.Add, contentDescription = null, Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text("Add Excluded Folder via SAF")
            }
        }
    }
}

@Composable
private fun DownloadConcurrencyCard(
    concurrency: Int,
    wifiOnly: Boolean,
    onConcurrencyChange: (Int) -> Unit,
    onWifiOnlyChange: (Boolean) -> Unit,
) {
    Card(
        shape = CardOuterShape,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.CloudDownload, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(10.dp))
                Column(Modifier.weight(1f)) {
                    Text("Download Concurrency & Network", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text("Parallel download threads & Wi-Fi constraints", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            Text("Parallel Download Threads", style = MaterialTheme.typography.labelSmall)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                listOf(1, 2, 3, 5).forEach { count ->
                    val isSelected = count == concurrency
                    FilterChip(
                        selected = isSelected,
                        onClick = { onConcurrencyChange(count) },
                        label = { Text("$count Threads", style = MaterialTheme.typography.labelSmall) },
                    )
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text("Download Over Wi-Fi Only", style = MaterialTheme.typography.bodyMedium)
                Switch(checked = wifiOnly, onCheckedChange = onWifiOnlyChange)
            }
        }
    }
}

@Composable
private fun AudioOffloadCard(
    enabled: Boolean,
    onToggle: (Boolean) -> Unit,
) {
    Card(
        shape = CardOuterShape,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(Icons.Filled.Bolt, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
            Spacer(Modifier.width(10.dp))
            Column(Modifier.weight(1f)) {
                Text("Hardware Audio Offload", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text("Direct SoC DSP tunneling for ultra-low screen-off power", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Switch(checked = enabled, onCheckedChange = onToggle)
        }
    }
}

@Composable
private fun ConvolutionIrCard(
    wetLevel: Float,
    onSelectFile: () -> Unit,
    onWetChange: (Float) -> Unit,
) {
    Card(
        shape = CardOuterShape,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.GraphicEq, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(10.dp))
                Column(Modifier.weight(1f)) {
                    Text("Impulse Response (IR) Convolution", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text("Load custom room/cabinet .wav profiles", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            OutlinedButton(onClick = onSelectFile, modifier = Modifier.fillMaxWidth()) {
                Text("Import .wav IR Profile")
            }
            Text("Wet/Dry Mix: ${(wetLevel * 100).toInt()}%", style = MaterialTheme.typography.labelSmall)
            Slider(
                value = wetLevel,
                onValueChange = onWetChange,
                valueRange = 0f..1f,
            )
        }
    }
}

@Composable
private fun IncognitoModeCard(
    enabled: Boolean,
    onToggle: (Boolean) -> Unit,
) {
    Card(
        shape = CardOuterShape,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(Icons.Filled.Visibility, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
            Spacer(Modifier.width(10.dp))
            Column(Modifier.weight(1f)) {
                Text("Incognito Listening Session", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text("Pause history logging, taste signals & scrobblers", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Switch(checked = enabled, onCheckedChange = onToggle)
        }
    }
}

@Composable
private fun InnerTubeSpoofingCard(
    currentClient: String,
    onSelectClient: (String) -> Unit,
) {
    Card(
        shape = CardOuterShape,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.Language, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(10.dp))
                Column {
                    Text("InnerTube Client Spoofing", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text("Bypass geographic blocks & throttling", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            val clients = listOf("Android VR", "Web Remix", "iOS", "Testsuite")
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                clients.forEach { client ->
                    val isSelected = client.equals(currentClient, ignoreCase = true)
                    FilterChip(
                        selected = isSelected,
                        onClick = { onSelectClient(client) },
                        label = { Text(client, style = MaterialTheme.typography.labelSmall) },
                    )
                }
            }
        }
    }
}

@Composable
private fun DohAndProxyCard(
    dohProvider: String,
    onSelectDoh: (String) -> Unit,
) {
    Card(
        shape = CardOuterShape,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.Language, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(10.dp))
                Column {
                    Text("DNS-over-HTTPS (DoH)", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text("Encrypted DNS resolvers for privacy", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            val providers = listOf("System", "Cloudflare", "Quad9", "AdGuard")
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                providers.forEach { provider ->
                    val isSelected = provider.equals(dohProvider, ignoreCase = true)
                    FilterChip(
                        selected = isSelected,
                        onClick = { onSelectDoh(provider) },
                        label = { Text(provider, style = MaterialTheme.typography.labelSmall) },
                    )
                }
            }
        }
    }
}

@Composable
private fun AudiophileThemePaletteCard(
    onSelectPreset: (String) -> Unit,
) {
    Card(
        shape = CardOuterShape,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.Palette, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(10.dp))
                Column {
                    Text("Audiophile Theme Palettes", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text("Curated design systems and color palettes", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            val palettes = listOf("Catppuccin", "Nord", "Dracula", "Tokyo Night", "Gruvbox", "OLED Black")
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                palettes.forEach { palette ->
                    FilterChip(
                        selected = false,
                        onClick = { onSelectPreset(palette) },
                        label = { Text(palette, style = MaterialTheme.typography.labelSmall) },
                    )
                }
            }
        }
    }
}

@Composable
private fun WebDavBackupCard(
    onBackup: () -> Unit,
    onRestore: () -> Unit,
) {
    Card(
        shape = CardOuterShape,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.CloudSync, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(10.dp))
                Column {
                    Text("Encrypted WebDAV Sync", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text("AES-256 cloud database backup & restore", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                Button(onClick = onBackup, modifier = Modifier.weight(1f)) {
                    Text("Backup to Cloud")
                }
                OutlinedButton(onClick = onRestore, modifier = Modifier.weight(1f)) {
                    Text("Restore")
                }
            }
        }
    }
}

@Composable
private fun AutoEqCard(
    onOpenSearch: () -> Unit,
) {
    Card(
        shape = CardOuterShape,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.Tune, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(10.dp))
                Column(Modifier.weight(1f)) {
                    Text("AutoEQ Headphone Targets (4,000+)", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text("Apply calibrated Harman target curves", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            OutlinedButton(onClick = onOpenSearch, modifier = Modifier.fillMaxWidth()) {
                Text("Browse Headphone Profiles")
            }
        }
    }
}

@Composable
private fun AppIconSelectorCard(
    currentTheme: AppIconTheme,
    onSelectIcon: (AppIconTheme) -> Unit,
) {
    Card(
        shape = CardOuterShape,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.Palette, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(10.dp))
                Column {
                    Text("App Launcher Icon", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text("Choose Material 3 Expressive launcher icon theme", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                AppIconTheme.entries.forEach { iconTheme ->
                    val isSelected = iconTheme == currentTheme
                    Surface(
                        onClick = { onSelectIcon(iconTheme) },
                        shape = RoundedCornerShape(18.dp),
                        color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceContainerLow,
                        border = if (isSelected) BorderStroke(2.dp, MaterialTheme.colorScheme.primary) else null,
                        modifier = Modifier.weight(1f),
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)
                                    .background(if (iconTheme == AppIconTheme.DARK) Color(0xFF0B0D13) else Color(0xFFF2F4F8)),
                                contentAlignment = Alignment.Center,
                            ) {
                                Icon(
                                    painter = painterResource(if (iconTheme == AppIconTheme.DARK) R.drawable.ic_launcher_dark_foreground else R.drawable.ic_launcher_light_foreground),
                                    contentDescription = iconTheme.title,
                                    tint = Color.Unspecified,
                                    modifier = Modifier.size(38.dp),
                                )
                            }
                            Text(
                                iconTheme.title,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface,
                                textAlign = TextAlign.Center,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AccentPresetGrid(
    currentMode: AccentMode,
    selectedHex: String?,
    onPickDynamic: () -> Unit,
    onPickPreset: (String) -> Unit,
    onPickMono: () -> Unit,
    onPickCustom: () -> Unit,
) {
    fun isPresetSelected(hex: String) =
        currentMode == AccentMode.MANUAL && selectedHex?.equals(hex, ignoreCase = true) == true
    val customSelected = currentMode == AccentMode.MANUAL &&
        selectedHex != null &&
        ACCENT_PRESETS.none { it.hex.equals(selectedHex, ignoreCase = true) }
    val monoSelected = currentMode == AccentMode.MONOCHROME
    val dynamicSelected = currentMode == AccentMode.DYNAMIC

    // 2 rows of 4 columns
    Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
        ColorTile(
            label = "Monet",
            selected = dynamicSelected,
            modifier = Modifier.weight(1f),
            onClick = onPickDynamic,
        ) {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(
                        androidx.compose.ui.graphics.Brush.linearGradient(
                            listOf(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.tertiary),
                        ),
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Icon(Icons.Filled.AutoAwesome, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
            }
        }
        ACCENT_PRESETS.take(3).forEach { preset ->
            ColorTile(
                label = preset.name,
                selected = isPresetSelected(preset.hex),
                modifier = Modifier.weight(1f),
                onClick = { onPickPreset(preset.hex) },
            ) {
                PaletteTilePreview(preset.hex)
            }
        }
    }
    Spacer(Modifier.height(12.dp))
    Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
        ACCENT_PRESETS.drop(3).take(2).forEach { preset ->
            ColorTile(
                label = preset.name,
                selected = isPresetSelected(preset.hex),
                modifier = Modifier.weight(1f),
                onClick = { onPickPreset(preset.hex) },
            ) {
                PaletteTilePreview(preset.hex)
            }
        }
        ColorTile(
            label = "Mono",
            selected = monoSelected,
            modifier = Modifier.weight(1f),
            onClick = onPickMono,
        ) {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surfaceContainerHighest),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    Icons.Filled.Contrast,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(20.dp),
                )
            }
        }
        ColorTile(
            label = "Custom",
            selected = customSelected,
            modifier = Modifier.weight(1f),
            onClick = onPickCustom,
        ) {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(
                        androidx.compose.ui.graphics.Brush.sweepGradient(
                            listOf(Color(0xFF00E5FF), Color(0xFF9D4EDD), Color(0xFFFFB703), Color(0xFF06D6A0), Color(0xFFFF007F), Color(0xFF00E5FF)),
                        ),
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    Icons.Filled.Palette,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(20.dp),
                )
            }
        }
    }
}

/**
 * Four coordinated shades derived from one preset hex via HSV — Dark,
 * Medium, Light, and a punchier Accent tone — used to render a real
 * multi-tone palette preview inside each Accent tile instead of one flat
 * swatch. Order returned: [dark, medium, light, accent].
 *
 * Tuned for a muted, Material You / Monet feel rather than raw HSV
 * vibrance: saturation is capped well below 100% even for the "accent"
 * shade (Monet's HCT-derived tonal palettes rarely reach full chroma —
 * that's what read as neon here), and the value range is narrower so
 * "dark" and "light" stay closer to the preset's own character instead of
 * swinging to near-black/near-white extremes.
 */
private fun accentShades(hex: String): List<Color> {
    val argb = android.graphics.Color.parseColor(hex)
    val hsv = FloatArray(3)
    android.graphics.Color.colorToHSV(argb, hsv)
    val (h, s, v) = hsv
    val mutedBase = s * 0.72f // the single biggest lever for "less neon"

    fun shade(saturation: Float, value: Float): Color {
        val arr = floatArrayOf(h, saturation.coerceIn(0f, 0.82f), value.coerceIn(0.2f, 0.92f))
        return Color(android.graphics.Color.HSVToColor(arr))
    }

    return listOf(
        shade(mutedBase.coerceAtLeast(0.42f), v * 0.62f), // dark
        shade(mutedBase, v * 0.80f), // medium — closest to the preset's own tone
        shade((mutedBase * 0.55f), (v + (1f - v) * 0.45f).coerceAtLeast(0.78f)), // light
        shade((mutedBase * 1.15f), (v * 0.95f)), // accent — a touch richer, never maxed out
    )
}

/** Renders a preset's four shades as a 2x2 block grid filling the tile,
 *  so selecting a preset previews its whole coordinated palette rather
 *  than one flat color. */
@Composable
private fun PaletteTilePreview(hex: String) {
    val shades = remember(hex) { accentShades(hex) }
    Column(Modifier.fillMaxSize()) {
        Row(Modifier.weight(1f).fillMaxWidth()) {
            Box(Modifier.weight(1f).fillMaxHeight().background(shades[0]))
            Box(Modifier.weight(1f).fillMaxHeight().background(shades[1]))
        }
        Row(Modifier.weight(1f).fillMaxWidth()) {
            Box(Modifier.weight(1f).fillMaxHeight().background(shades[2]))
            Box(Modifier.weight(1f).fillMaxHeight().background(shades[3]))
        }
    }
}


/**
 * One expressive accent tile: a real elevated square swatch (Modifier.shadow
 * — a true drop shadow, not Card's tonal-elevation color blend, which would
 * otherwise wash out the exact color a swatch is supposed to preview),
 * a spring scale/elevation lift on selection, a genuine ripple on tap, and
 * an animated check badge. [content] draws the tile's fill — a flat color
 * for presets, an icon-on-surface treatment for Mono/Custom.
 */
@Composable
private fun ColorTile(
    label: String,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    content: @Composable (Boolean) -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.94f else if (selected) 1.04f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessMedium),
        label = "tileScale",
    )
    val elevation by animateDpAsState(
        targetValue = if (selected) 8.dp else 2.dp,
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
        label = "tileElevation",
    )
    val borderColor by androidx.compose.animation.animateColorAsState(
        targetValue = if (selected) MaterialTheme.colorScheme.primary else Color.Transparent,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow),
        label = "tileBorder",
    )
    val tileShape = RoundedCornerShape(20.dp)

    Column(
        modifier = modifier.scale(scale),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                // Real shadow, not tonal elevation — keeps every tile's
                // color a true, undistorted preview of the accent it
                // represents (see GenerateScreen's ModeCard fix for why
                // Card's own elevation param is the wrong tool for this).
                .shadow(elevation = elevation, shape = tileShape, clip = false)
                .clip(tileShape)
                .clickable(
                    interactionSource = interactionSource,
                    indication = LocalIndication.current,
                    onClick = onClick,
                )
                .border(2.5.dp, borderColor, tileShape),
            contentAlignment = Alignment.Center,
        ) {
            content(selected)
            androidx.compose.animation.AnimatedVisibility(
                visible = selected,
                enter = androidx.compose.animation.fadeIn() + androidx.compose.animation.scaleIn(),
                exit = androidx.compose.animation.fadeOut() + androidx.compose.animation.scaleOut(),
                modifier = Modifier.align(Alignment.TopEnd).padding(6.dp),
            ) {
                Box(
                    Modifier
                        .size(20.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        Icons.Filled.Check,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(13.dp),
                    )
                }
            }
        }
        Spacer(Modifier.height(6.dp))
        Text(
            label,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
            color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun AboutCard(versionName: String) {
    val context = LocalContext.current
    Card(
        shape = CardOuterShape,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(
            Modifier.fillMaxWidth().padding(vertical = 28.dp, horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .background(colorResource(R.color.ic_launcher_background)),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_launcher_logo),
                    contentDescription = "Ananta Dhvani",
                    tint = Color.Unspecified,
                    modifier = Modifier.size(72.dp),
                )
            }
            Spacer(Modifier.height(14.dp))
            Text("Ananta Dhvani (अनन्त-ध्वनि / অনন্ত ধ্বনি)", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
            Spacer(Modifier.height(6.dp))
            Text(
                "Crafted with passion by Ishan",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.primary,
            )
            Spacer(Modifier.height(8.dp))
            Surface(
                shape = ExpressivePillShape,
                color = MaterialTheme.colorScheme.primaryContainer,
            ) {
                Text(
                    "Version $versionName",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                )
            }
            Spacer(Modifier.height(14.dp))
            OutlinedButton(
                onClick = {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://x.com/Ishan____404"))
                    startActivitySafely(context, intent)
                },
                shape = ExpressivePillShape,
            ) {
                Text("@Ishan____404 on X (Twitter)", style = MaterialTheme.typography.labelMedium)
            }
            Spacer(Modifier.height(8.dp))
            Text(
                "High-Fidelity Audiophile Player for Android",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )
        }
    }
}

/** OEM Settings/browser components are optional and occasionally broken on
 * custom ROMs. Never let an external activity failure escape a click event. */
private fun startActivitySafely(context: android.content.Context, intent: Intent): Boolean {
    val safeIntent = Intent(intent).apply {
        if (context !is android.app.Activity) addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    return try {
        context.startActivity(safeIntent)
        true
    } catch (_: Exception) {
        false
    } catch (_: LinkageError) {
        false
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ColorWheelSheet(onDismiss: () -> Unit, onApply: (Color) -> Unit) {
    val sheetState = rememberModalBottomSheetState()
    var hue by remember { mutableStateOf(4f) }
    var saturation by remember { mutableStateOf(0.75f) }
    var lightness by remember { mutableStateOf(0.5f) }
    val previewColor = Color.hsl(hue, saturation, lightness)

    ModalBottomSheet(onDismissRequest = onDismiss, sheetState = sheetState) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .adaptiveContentWidth(maxWidth = 560.dp)
                .align(Alignment.CenterHorizontally)
                .padding(20.dp),
        ) {
            Text("Custom Color", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(16.dp))
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(previewColor),
            )
            Spacer(Modifier.height(20.dp))
            Text("Hue", style = MaterialTheme.typography.labelLarge)
            Slider(value = hue, onValueChange = { hue = it }, valueRange = 0f..360f)
            Text("Saturation", style = MaterialTheme.typography.labelLarge)
            Slider(value = saturation, onValueChange = { saturation = it }, valueRange = 0f..1f)
            Text("Lightness", style = MaterialTheme.typography.labelLarge)
            Slider(value = lightness, onValueChange = { lightness = it }, valueRange = 0.15f..0.85f)
            Spacer(Modifier.height(12.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedButton(onClick = onDismiss, shape = ExpressivePillShape, modifier = Modifier.weight(1f).height(48.dp)) { Text("Cancel") }
                Button(onClick = { onApply(previewColor) }, shape = ExpressivePillShape, modifier = Modifier.weight(1f).height(48.dp)) { Text("Apply") }
            }
            Spacer(Modifier.height(8.dp))
        }
    }
}

// -- Experimental 31-band equalizer (Settings → Experimental → Equalizer) --

private const val EQ_MAX_DB = EQ_MAX_GAIN_DB

private fun eqBandCategory(hz: Float): String = when {
    hz <= 40f -> "SUB"
    hz <= 100f -> "BASS"
    hz <= 250f -> "LOW-MID"
    hz <= 1000f -> "MID"
    hz <= 2500f -> "HIGH-MID"
    hz <= 6300f -> "PRES"
    else -> "AIR"
}

@Composable
private fun EqualizerCurveGraph(
    gains: FloatArray,
    enabled: Boolean,
    modifier: Modifier = Modifier,
) {
    val primaryColor = MaterialTheme.colorScheme.primary
    val onSurfaceVariant = MaterialTheme.colorScheme.onSurfaceVariant
    val outlineVariant = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.35f)
    val gridColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(130.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(MaterialTheme.colorScheme.surfaceContainerLowest)
            .padding(horizontal = 14.dp, vertical = 10.dp),
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height
            val paddingX = 14.dp.toPx()
            val availableW = w - paddingX * 2f
            val baselineY = h / 2f
            val maxDbPx = (h - 22.dp.toPx()) / 2f

            val topY = baselineY - maxDbPx
            val bottomY = baselineY + maxDbPx

            // Baseline (0 dB)
            drawLine(
                color = if (enabled) outlineVariant else outlineVariant.copy(alpha = 0.15f),
                start = Offset(paddingX, baselineY),
                end = Offset(w - paddingX, baselineY),
                strokeWidth = 1.5.dp.toPx(),
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 8f), 0f),
            )
            // +8 dB line
            drawLine(
                color = gridColor,
                start = Offset(paddingX, topY),
                end = Offset(w - paddingX, topY),
                strokeWidth = 1.dp.toPx(),
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(4f, 6f), 0f),
            )
            // -8 dB line
            drawLine(
                color = gridColor,
                start = Offset(paddingX, bottomY),
                end = Offset(w - paddingX, bottomY),
                strokeWidth = 1.dp.toPx(),
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(4f, 6f), 0f),
            )

            if (gains.isEmpty()) return@Canvas

            val count = gains.size
            val stepX = availableW / (count - 1).coerceAtLeast(1)
            val points = List(count) { i ->
                val x = paddingX + i * stepX
                val gain = if (enabled) gains[i].coerceIn(-EQ_MAX_DB, EQ_MAX_DB) else 0f
                val y = baselineY - (gain / EQ_MAX_DB) * maxDbPx
                Offset(x, y)
            }

            val path = Path()
            val fillPath = Path()

            path.moveTo(points.first().x, points.first().y)
            fillPath.moveTo(points.first().x, baselineY)
            fillPath.lineTo(points.first().x, points.first().y)

            for (i in 0 until points.size - 1) {
                val p0 = points[i]
                val p1 = points[i + 1]
                val controlX1 = p0.x + (p1.x - p0.x) / 2f
                val controlY1 = p0.y
                val controlX2 = p0.x + (p1.x - p0.x) / 2f
                val controlY2 = p1.y
                path.cubicTo(controlX1, controlY1, controlX2, controlY2, p1.x, p1.y)
                fillPath.cubicTo(controlX1, controlY1, controlX2, controlY2, p1.x, p1.y)
            }

            fillPath.lineTo(points.last().x, baselineY)
            fillPath.close()

            if (enabled) {
                drawPath(
                    path = fillPath,
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            primaryColor.copy(alpha = 0.35f),
                            primaryColor.copy(alpha = 0.05f),
                            Color.Transparent,
                        ),
                        startY = topY,
                        endY = bottomY,
                    ),
                )
            }

            drawPath(
                path = path,
                color = if (enabled) primaryColor else onSurfaceVariant.copy(alpha = 0.4f),
                style = Stroke(
                    width = 3.dp.toPx(),
                    cap = StrokeCap.Round,
                    join = StrokeJoin.Round,
                ),
            )

            for (p in points) {
                val hasBoostOrCut = Math.abs(p.y - baselineY) > 2f && enabled
                drawCircle(
                    color = if (hasBoostOrCut) primaryColor else if (enabled) primaryColor.copy(alpha = 0.7f) else onSurfaceVariant.copy(alpha = 0.3f),
                    radius = if (hasBoostOrCut) 4.5.dp.toPx() else 3.dp.toPx(),
                    center = p,
                )
                if (hasBoostOrCut) {
                    drawCircle(
                        color = Color.White,
                        radius = 2.dp.toPx(),
                        center = p,
                    )
                }
            }
        }

        Column(
            modifier = Modifier.fillMaxHeight().align(Alignment.CenterEnd).padding(end = 2.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.End,
        ) {
            Text("+12dB", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            Text("0dB", fontSize = 9.sp, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f))
            Text("-12dB", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f))
        }
    }
}

/**
 * 100% natural, native Material 3 Equalizer:
 * - Edge-to-edge layout with status bar and navigation bar insets protection
 * - Live dynamic Bézier Spline frequency response visualizer
 * - Hardware acoustic fader board with real-time numeric dB readouts and 0 dB center detent haptics
 * - Standard ISO center frequencies
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
private fun EqualizerSheet(
    eq: EqualizerSettings,
    onDismiss: () -> Unit,
    onSetEnabled: (Boolean) -> Unit,
    onPickPreset: (String) -> Unit,
    onBandPreview: (Int, Float) -> Unit,
    onBandChange: (Int, Float) -> Unit,
    onPreampPreview: (Float) -> Unit,
    onPreampChange: (Float) -> Unit,
    onFilterQChange: (Float) -> Unit = {},
    onSingleBandQChange: (Int, Float) -> Unit = { _, _ -> },
    onCopyEqCode: () -> Unit = {},
    onExportProfile: () -> Unit = {},
    onImportProfile: () -> Unit = {},
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val haptic = androidx.compose.ui.platform.LocalHapticFeedback.current

    var gains by remember(eq.gainsDb) {
        mutableStateOf(
            FloatArray(EQ_BAND_FREQS_HZ.size) { index ->
                eq.gainsDb.getOrNull(index)
                    ?.takeIf { it.isFinite() }
                    ?.coerceIn(-EQ_MAX_DB, EQ_MAX_DB)
                    ?: 0f
            },
        )
    }

    var preampDb by androidx.compose.runtime.remember(eq.preampDb) {
        androidx.compose.runtime.mutableFloatStateOf(eq.preampDb.coerceIn(-10f, 10f))
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
        dragHandle = {
            Surface(
                shape = RoundedCornerShape(50),
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f),
                modifier = Modifier
                    .padding(top = 12.dp, bottom = 8.dp)
                    .size(width = 36.dp, height = 4.dp),
            ) {}
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .adaptiveContentWidth(maxWidth = 640.dp)
                .align(Alignment.CenterHorizontally)
                .statusBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .padding(bottom = 32.dp + safeDrawingBottomPadding()),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(MaterialTheme.colorScheme.primaryContainer),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            Icons.Filled.GraphicEq,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.size(24.dp),
                        )
                    }
                    Spacer(Modifier.width(14.dp))
                    Column {
                        Text(
                            "Equalizer",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                        )
                        Text(
                            "31-band studio graphic tuning",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                        )
                    }
                }

                TextButton(
                    onClick = {
                        haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress)
                        onPickPreset(EqualizerPresets.FLAT.name)
                    },
                    enabled = eq.enabled,
                ) {
                    Icon(Icons.Filled.RestartAlt, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("Reset", style = MaterialTheme.typography.labelMedium)
                }
            }

            // Master On/Off Switch Card
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = MaterialTheme.colorScheme.surfaceContainerHigh,
                tonalElevation = 2.dp,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp).fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Column(Modifier.weight(1f)) {
                        Text("Enable Equalizer", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium)
                        Text(
                            if (eq.enabled) "Shaping your music in real-time" else "Off \u2014 original audio passes through",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 2,
                        )
                    }
                    Switch(
                        checked = eq.enabled,
                        onCheckedChange = { enabled ->
                            haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress)
                            onSetEnabled(enabled)
                        },
                    )
                }
            }

            // Preamp Gain Slider (-10 dB to +10 dB)
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = MaterialTheme.colorScheme.surfaceContainerHigh,
                tonalElevation = 2.dp,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp).fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Column(Modifier.weight(1f)) {
                            Text("Preamp Gain", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium)
                            Text("Master gain staging to prevent clipping", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.primaryContainer,
                        ) {
                            Text(
                                text = if (preampDb > 0f) "+${"%.1f".format(preampDb)} dB" else "${"%.1f".format(preampDb)} dB",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                            )
                        }
                    }
                    Slider(
                        value = preampDb,
                        onValueChange = { value ->
                            preampDb = value
                            onPreampPreview(value)
                        },
                        valueRange = -10f..10f,
                        enabled = eq.enabled,
                        modifier = Modifier.fillMaxWidth(),
                        onValueChangeFinished = { onPreampChange(preampDb) },
                    )
                }
            }

            // Filter Q Slider (0.1 to 10.0)
            var filterQ by androidx.compose.runtime.remember(eq.filterQ) {
                androidx.compose.runtime.mutableFloatStateOf(eq.filterQ.coerceIn(0.1f, 10.0f))
            }
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = MaterialTheme.colorScheme.surfaceContainerHigh,
                tonalElevation = 2.dp,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp).fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Column(Modifier.weight(1f)) {
                            Text("Filter Q (Bandwidth)", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium)
                            Text("Filter bell resonance (0.1 broad to 10.0 surgical)", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.primaryContainer,
                        ) {
                            Text(
                                text = "%.2f".format(filterQ),
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                            )
                        }
                    }
                    Slider(
                        value = filterQ,
                        onValueChange = { value -> filterQ = value },
                        valueRange = 0.1f..10.0f,
                        enabled = eq.enabled,
                        modifier = Modifier.fillMaxWidth(),
                        onValueChangeFinished = { onFilterQChange(filterQ) },
                    )
                }
            }

            // Real-Time Frequency Response Visualizer
            EqualizerCurveGraph(
                gains = gains,
                enabled = eq.enabled,
            )

            // Export & Import Profiles Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                OutlinedButton(
                    onClick = onExportProfile,
                    modifier = Modifier.weight(1f),
                    shape = ExpressivePillShape,
                ) {
                    Icon(Icons.Filled.FileDownload, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("Export", style = MaterialTheme.typography.labelMedium)
                }
                OutlinedButton(
                    onClick = onImportProfile,
                    modifier = Modifier.weight(1f),
                    shape = ExpressivePillShape,
                ) {
                    Icon(Icons.Filled.Download, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("Import", style = MaterialTheme.typography.labelMedium)
                }
                OutlinedButton(
                    onClick = onCopyEqCode,
                    modifier = Modifier.weight(1f),
                    shape = ExpressivePillShape,
                ) {
                    Icon(Icons.Filled.ContentCopy, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("Copy Code", style = MaterialTheme.typography.labelMedium)
                }
            }

            // Presets Horizontal Flow
            SectionLabel("Presets")
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.fillMaxWidth(),
            ) {
                EqualizerPresets.ALL.forEach { preset ->
                    FilterChip(
                        selected = eq.presetName.equals(preset.name, ignoreCase = true),
                        onClick = {
                            haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress)
                            onPickPreset(preset.name)
                        },
                        label = { Text(preset.name, style = MaterialTheme.typography.labelMedium) },
                    )
                }
                if (eq.presetName == EqualizerPresets.CUSTOM_NAME) {
                    FilterChip(selected = true, onClick = {}, label = { Text("Custom", style = MaterialTheme.typography.labelMedium) })
                }
            }

            // Native Equalizer Fader Board
            Surface(
                shape = RoundedCornerShape(24.dp),
                color = MaterialTheme.colorScheme.surfaceContainerHigh,
                tonalElevation = 2.dp,
                modifier = Modifier.fillMaxWidth(),
            ) {
                val curveAlpha by animateFloatAsState(
                    targetValue = if (eq.enabled) 1f else 0.38f,
                    animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
                    label = "eqCurveAlpha",
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                        .alpha(curveAlpha),
                ) {
                    // Top Scale Label
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 2.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            "+8 dB (Boost)",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.primary,
                        )
                        Text(
                            "-8 dB (Cut)",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }

                    var showPerBandQ by remember { mutableStateOf(false) }
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text("Individual Band Q Factors", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
                        FilterChip(
                            selected = showPerBandQ,
                            onClick = { showPerBandQ = !showPerBandQ },
                            label = { Text(if (showPerBandQ) "Hide Band Q" else "Expand Band Q", style = MaterialTheme.typography.labelSmall) },
                        )
                    }

                    Spacer(Modifier.height(10.dp))

                    // Scrollable Horizontal Row of Native Equalizer Faders
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState())
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        EQ_BAND_FREQS_HZ.forEachIndexed { index, hz ->
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                EqNativeSlider(
                                    gainDb = gains[index],
                                    hz = hz,
                                    enabled = eq.enabled,
                                    onGainChange = { value ->
                                        gains = gains.copyOf().also { it[index] = value }
                                        onBandPreview(index, value)
                                    },
                                    onChangeFinished = { onBandChange(index, gains[index]) },
                                )
                                if (showPerBandQ) {
                                    val currentBandQ = eq.bandQValues.getOrElse(index) { 1.414f }
                                    var bandQState by androidx.compose.runtime.remember(currentBandQ) { androidx.compose.runtime.mutableFloatStateOf(currentBandQ) }
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        modifier = Modifier.width(60.dp).padding(top = 8.dp),
                                    ) {
                                        Text("Q: ${"%.2f".format(bandQState)}", style = MaterialTheme.typography.labelSmall, fontSize = 10.sp)
                                        Slider(
                                            value = bandQState,
                                            onValueChange = {
                                                bandQState = it
                                                onSingleBandQChange(index, it)
                                            },
                                            valueRange = 0.1f..10.0f,
                                            modifier = Modifier.fillMaxWidth(),
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Text(
                text = when {
                    !eq.enabled -> "Turn on to apply equalization live"
                    eq.presetName == EqualizerPresets.CUSTOM_NAME -> "Custom profile \u2022 touch and drag any bar up/down"
                    else -> "${eq.presetName} preset active \u2022 touch any bar to customize"
                },
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth().padding(top = 2.dp),
            )
        }
    }
}

/**
 * Goated native hardware equalizer fader bar:
 * - Vertical capsule track with central 0 dB baseline notch and active level gradient beam
 * - Tactile hardware capsule thumb knob with double grip ridges
 * - Real-time continuous numeric dB badge on top with container coloring
 * - Standard frequency label + band category tag underneath
 */
@Composable
private fun EqNativeSlider(
    gainDb: Float,
    hz: Float,
    enabled: Boolean,
    modifier: Modifier = Modifier,
    onGainChange: (Float) -> Unit,
    onChangeFinished: () -> Unit,
) {
    val haptic = androidx.compose.ui.platform.LocalHapticFeedback.current
    val normalized = ((gainDb + EQ_MAX_DB) / (EQ_MAX_DB * 2f)).coerceIn(0f, 1f)
    var isDragging by remember { mutableStateOf(false) }
    val currentGain by rememberUpdatedState(gainDb)
    val currentOnGainChange by rememberUpdatedState(onGainChange)
    val currentOnChangeFinished by rememberUpdatedState(onChangeFinished)

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // Numeric Gain on top in a small pill container
        Surface(
            shape = RoundedCornerShape(8.dp),
            color = when {
                !enabled -> MaterialTheme.colorScheme.surfaceContainer
                gainDb > 0f -> MaterialTheme.colorScheme.primaryContainer
                gainDb < 0f -> MaterialTheme.colorScheme.tertiaryContainer
                else -> MaterialTheme.colorScheme.surfaceContainerHighest
            },
        ) {
            Text(
                text = if (gainDb > 0f) "+${"%.1f".format(gainDb)}" else "${"%.1f".format(gainDb)}",
                fontSize = 11.sp,
                fontWeight = if (gainDb != 0f) FontWeight.Bold else FontWeight.Medium,
                color = when {
                    !enabled -> MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                    gainDb > 0f -> MaterialTheme.colorScheme.onPrimaryContainer
                    gainDb < 0f -> MaterialTheme.colorScheme.onTertiaryContainer
                    else -> MaterialTheme.colorScheme.onSurfaceVariant
                },
                maxLines = 1,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
            )
        }

        Spacer(Modifier.height(6.dp))

        // Vertical Track Box
        val trackHeight = 152.dp
        val thumbHeight = 24.dp
        val thumbWidth = 38.dp

        Box(
            modifier = Modifier
                .width(44.dp)
                .height(trackHeight)
                .clip(RoundedCornerShape(22.dp))
                .background(MaterialTheme.colorScheme.surfaceContainerHighest)
                .pointerInput(enabled) {
                    if (!enabled) return@pointerInput
                    detectVerticalDragGestures(
                        onDragStart = { isDragging = true },
                        onDragEnd = {
                            isDragging = false
                            currentOnChangeFinished()
                        },
                        onDragCancel = {
                            isDragging = false
                            currentOnChangeFinished()
                        },
                    ) { change, dragAmount ->
                        change.consume()
                        val deltaFraction = -dragAmount / size.height.toFloat()
                        val currentFraction = ((currentGain + EQ_MAX_DB) / (EQ_MAX_DB * 2f))
                        val newFraction = (currentFraction + deltaFraction).coerceIn(0f, 1f)
                        val newGain = (newFraction * EQ_MAX_DB * 2f - EQ_MAX_DB).let {
                            if (it in -0.3f..0.3f) 0f else (Math.round(it * 2f) / 2f)
                        }
                        if (newGain != currentGain) {
                            if (newGain == 0f && currentGain != 0f) {
                                haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.TextHandleMove)
                            }
                            currentOnGainChange(newGain)
                        }
                    }
                },
            contentAlignment = Alignment.Center,
        ) {
            // Center Baseline Line (0 dB)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp)
                    .background(MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
            )

            // Active Level Fill (from baseline to thumb)
            val baselineFraction = 0.5f
            val topFraction = if (normalized >= baselineFraction) 1f - normalized else 1f - baselineFraction
            val heightFraction = Math.abs(normalized - baselineFraction)

            if (heightFraction > 0.01f && enabled) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.55f)
                        .fillMaxHeight(heightFraction)
                        .align(Alignment.TopCenter)
                        .graphicsLayer {
                            translationY = size.height * topFraction
                        }
                        .clip(RoundedCornerShape(6.dp))
                        .background(
                            if (gainDb > 0f) {
                                Brush.verticalGradient(
                                    listOf(
                                        MaterialTheme.colorScheme.primary,
                                        MaterialTheme.colorScheme.primaryContainer,
                                    ),
                                )
                            } else {
                                Brush.verticalGradient(
                                    listOf(
                                        MaterialTheme.colorScheme.tertiaryContainer,
                                        MaterialTheme.colorScheme.tertiary,
                                    ),
                                )
                            },
                        ),
                )
            }

            // Tactile Hardware Capsule Thumb Knob
            Box(
                modifier = Modifier
                    .size(width = thumbWidth, height = thumbHeight)
                    .align(Alignment.TopCenter)
                    .graphicsLayer {
                        val maxTravel = (trackHeight - thumbHeight).toPx()
                        translationY = maxTravel * (1f - normalized)
                    }
                    .shadow(if (isDragging) 8.dp else 3.dp, RoundedCornerShape(12.dp))
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        if (enabled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceContainerHigh,
                    ),
                contentAlignment = Alignment.Center,
            ) {
                // Double grip ridges
                Column(
                    verticalArrangement = Arrangement.spacedBy(3.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Box(
                        modifier = Modifier
                            .size(width = 14.dp, height = 2.dp)
                            .clip(RoundedCornerShape(1.dp))
                            .background(
                                if (enabled) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                            ),
                    )
                    Box(
                        modifier = Modifier
                            .size(width = 14.dp, height = 2.dp)
                            .clip(RoundedCornerShape(1.dp))
                            .background(
                                if (enabled) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                            ),
                    )
                }
            }
        }

        Spacer(Modifier.height(8.dp))

        // Frequency Label
        Text(
            text = eqBandLabel(hz),
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = if (gainDb != 0f && enabled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
        )
        Text(
            text = eqBandCategory(hz),
            fontSize = 9.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.75f),
            maxLines = 1,
        )
    }
}

/** Controls which connected-account playlists appear in LastWave. This is
 * intentionally independent from importing and two-way sync. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun YouTubeLibraryVisibilitySheet(
    playlists: List<com.lastwave.app.data.music.YouTubePlaylistSummary>,
    hiddenIds: Set<String>,
    onSetVisible: (String, Boolean) -> Unit,
    onSetAllVisible: (Boolean) -> Unit,
    onDismiss: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val haptic = androidx.compose.ui.platform.LocalHapticFeedback.current
    val allShown = playlists.isNotEmpty() && playlists.all { it.id !in hiddenIds }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .adaptiveContentWidth(maxWidth = 640.dp)
                .align(Alignment.CenterHorizontally)
                .statusBarsPadding()
                .padding(horizontal = 20.dp)
                .padding(bottom = 32.dp + safeDrawingBottomPadding()),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Column(Modifier.weight(1f)) {
                    Text("YouTube Playlists Shown", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Text(
                        "Choose which account playlists appear in LastWave",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                TextButton(
                    onClick = {
                        haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress)
                        onSetAllVisible(!allShown)
                    },
                ) {
                    Text(if (allShown) "Hide All" else "Show All", fontWeight = FontWeight.Bold)
                }
            }

            if (playlists.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 40.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text("No YouTube playlists found", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth().heightIn(max = 480.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    items(playlists.size, key = { playlists[it].id }) { index ->
                        val playlist = playlists[index]
                        val isShown = playlist.id !in hiddenIds
                        Surface(
                            onClick = {
                                haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.TextHandleMove)
                                onSetVisible(playlist.id, !isShown)
                            },
                            shape = RoundedCornerShape(16.dp),
                            color = if (isShown) MaterialTheme.colorScheme.surfaceContainerHigh else MaterialTheme.colorScheme.surfaceContainer,
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(if (isShown) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceContainerHighest),
                                    contentAlignment = Alignment.Center,
                                ) {
                                    Icon(
                                        Icons.Filled.QueueMusic,
                                        contentDescription = null,
                                        tint = if (isShown) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant,
                                    )
                                }
                                Spacer(Modifier.width(14.dp))
                                Column(Modifier.weight(1f)) {
                                    Text(playlist.title, fontWeight = FontWeight.SemiBold, maxLines = 1)
                                    Text(
                                        playlist.trackCountText ?: playlist.author ?: "YouTube Music playlist",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        maxLines = 1,
                                    )
                                }
                                Checkbox(
                                    checked = isShown,
                                    onCheckedChange = { checked -> onSetVisible(playlist.id, checked) },
                                )
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))
            Button(onClick = onDismiss, shape = CircleShape, modifier = Modifier.fillMaxWidth().height(48.dp)) {
                Text("Done", fontWeight = FontWeight.Bold)
            }
        }
    }
}

/** Bottom sheet allowing user to select which specific playlists to mirror to YouTube Music. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SyncPlaylistsSheet(
    playlists: List<com.lastwave.app.data.playlist.SavedPlaylist>,
    syncedIds: Set<Long>?,
    onToggleSync: (Long, Boolean) -> Unit,
    onSelectAll: (Boolean) -> Unit,
    onDismiss: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val haptic = androidx.compose.ui.platform.LocalHapticFeedback.current
    val allSelected = playlists.isNotEmpty() && (syncedIds == null || (playlists.all { it.id in syncedIds } && syncedIds.isNotEmpty()))

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
        dragHandle = {
            Surface(
                shape = RoundedCornerShape(50),
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f),
                modifier = Modifier
                    .padding(top = 12.dp, bottom = 8.dp)
                    .size(width = 36.dp, height = 4.dp),
            ) {}
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .adaptiveContentWidth(maxWidth = 640.dp)
                .align(Alignment.CenterHorizontally)
                .statusBarsPadding()
                .padding(horizontal = 20.dp)
                .padding(bottom = 32.dp + safeDrawingBottomPadding()),
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 4.dp, bottom = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Column(Modifier.weight(1f)) {
                    Text(
                        "Sync Playlists",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                    )
                    Text(
                        "Choose which playlists mirror to YouTube Music",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }

                TextButton(
                    onClick = {
                        haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress)
                        onSelectAll(!allSelected)
                    },
                ) {
                    Text(if (allSelected) "Deselect All" else "Select All", fontWeight = FontWeight.Bold)
                }
            }

            if (playlists.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 40.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        "No playlists in your library yet",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth().weight(1f, fill = false),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    items(playlists.size, key = { playlists[it].id }) { idx ->
                        val playlist = playlists[idx]
                        val isChecked = syncedIds == null || playlist.id in syncedIds
                        Surface(
                            onClick = {
                                haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.TextHandleMove)
                                onToggleSync(playlist.id, !isChecked)
                            },
                            shape = RoundedCornerShape(16.dp),
                            color = if (isChecked) MaterialTheme.colorScheme.surfaceContainerHigh else MaterialTheme.colorScheme.surfaceContainer,
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp).fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(
                                            if (isChecked) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceContainerHighest
                                        ),
                                    contentAlignment = Alignment.Center,
                                ) {
                                    Icon(
                                        Icons.Filled.QueueMusic,
                                        contentDescription = null,
                                        tint = if (isChecked) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.size(22.dp),
                                    )
                                }

                                Spacer(Modifier.width(14.dp))

                                Column(Modifier.weight(1f)) {
                                    Text(
                                        playlist.title,
                                        style = MaterialTheme.typography.bodyLarge,
                                        fontWeight = FontWeight.SemiBold,
                                        maxLines = 1,
                                    )
                                    Text(
                                        "${playlist.tracks.size} tracks",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    )
                                }

                                Checkbox(
                                    checked = isChecked,
                                    onCheckedChange = { checked ->
                                        haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.TextHandleMove)
                                        onToggleSync(playlist.id, checked)
                                    },
                                )
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = onDismiss,
                shape = CircleShape,
                modifier = Modifier.fillMaxWidth().height(48.dp),
            ) {
                Text("Done", fontWeight = FontWeight.Bold)
            }
        }
    }
}

/**
 * Bottom sheet to pick from 8 experimental lyrics animation physics profiles.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LyricsAnimationSheet(
    wordByWord: Boolean,
    onWordByWordChange: (Boolean) -> Unit,
    showTranslation: Boolean,
    onShowTranslationChange: (Boolean) -> Unit,
    showPhonetic: Boolean,
    onShowPhoneticChange: (Boolean) -> Unit,
    version: LyricsUiVersion,
    onSelectVersion: (LyricsUiVersion) -> Unit,
    current: LyricsAnimation,
    onSelect: (LyricsAnimation) -> Unit,
    onDismiss: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val haptic = androidx.compose.ui.platform.LocalHapticFeedback.current
    val liquidGlass = LocalLiquidGlass.current

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        modifier = Modifier.liquidGlassChrome(
            RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
            liquidGlass,
            LiquidGlassPreset.ModalSheet,
            LocalLiquidGlassOverlayBackdrop.current,
        ),
        containerColor = liquidGlassContainerColor(
            MaterialTheme.colorScheme.surfaceContainer,
            backdrop = LocalLiquidGlassOverlayBackdrop.current,
        ),
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .adaptiveContentWidth(maxWidth = 640.dp)
                .align(Alignment.CenterHorizontally)
                .padding(horizontal = 20.dp)
                .padding(bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = MaterialTheme.colorScheme.tertiaryContainer,
                    modifier = Modifier.size(42.dp),
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Filled.Lyrics,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onTertiaryContainer,
                            modifier = Modifier.size(22.dp),
                        )
                    }
                }
                Column {
                    Text(
                        text = "Lyrics Animation",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                    Text(
                        text = "Real-time motion physics & optical tracking",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }

            SettingsToggleCard(
                icon = Icons.Filled.Lyrics,
                iconContainer = MaterialTheme.colorScheme.tertiaryContainer,
                iconTint = MaterialTheme.colorScheme.onTertiaryContainer,
                title = "Word-by-word lyrics",
                subtitle = "Turn off to use LRCLIB line-by-line lyrics",
                checked = wordByWord,
                onCheckedChange = onWordByWordChange,
            )

            SettingsToggleCard(
                icon = Icons.Filled.Language,
                iconContainer = MaterialTheme.colorScheme.primaryContainer,
                iconTint = MaterialTheme.colorScheme.onPrimaryContainer,
                title = stringResource(R.string.settings_show_translation),
                subtitle = stringResource(R.string.settings_show_translation_sub),
                checked = showTranslation,
                onCheckedChange = onShowTranslationChange,
            )

            SettingsToggleCard(
                icon = Icons.Filled.TextFields,
                iconContainer = MaterialTheme.colorScheme.secondaryContainer,
                iconTint = MaterialTheme.colorScheme.onSecondaryContainer,
                title = stringResource(R.string.settings_show_phonetic),
                subtitle = stringResource(R.string.settings_show_phonetic_sub),
                checked = showPhonetic,
                onCheckedChange = onShowPhoneticChange,
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                val versions = listOf(
                    LyricsUiVersion.CLASSIC to "Classic",
                    LyricsUiVersion.MODERN to "New UI",
                )
                versions.forEach { (ver, label) ->
                    val isVerSelected = ver == version
                    val chipShape = RoundedCornerShape(14.dp)
                    LiquidGlassSurface(
                        glassModifier = Modifier.liquidGlassChrome(chipShape, liquidGlass),
                        onClick = {
                            haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.TextHandleMove)
                            onSelectVersion(ver)
                        },
                        shape = chipShape,
                        color = liquidGlassContainerColor(if (isVerSelected) {
                            if (liquidGlass) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.92f)
                            else MaterialTheme.colorScheme.primaryContainer
                        } else {
                            if (liquidGlass) MaterialTheme.colorScheme.surfaceContainerHighest.copy(alpha = 0.60f)
                            else MaterialTheme.colorScheme.surfaceContainerHigh
                        }),
                        modifier = Modifier
                            .weight(1f)
                            .clip(chipShape),
                    ) {
                        Box(
                            modifier = Modifier.padding(vertical = 10.dp),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text = label,
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = if (isVerSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isVerSelected) {
                                    MaterialTheme.colorScheme.onPrimaryContainer
                                } else {
                                    MaterialTheme.colorScheme.onSurfaceVariant
                                },
                            )
                        }
                    }
                }
            }

            if (version == LyricsUiVersion.CLASSIC) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 440.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    items(LyricsAnimation.entries.toTypedArray(), key = { it.id }) { anim ->
                        val isSelected = anim == current
                        val cardShape = RoundedCornerShape(18.dp)

                        Surface(
                            shape = cardShape,
                            color = liquidGlassContainerColor(if (isSelected) {
                                if (liquidGlass) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.92f)
                                else MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.70f)
                            } else {
                                if (liquidGlass) MaterialTheme.colorScheme.surfaceContainerHighest.copy(alpha = 0.90f)
                                else MaterialTheme.colorScheme.surfaceContainerHighest.copy(alpha = 0.50f)
                            }),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(cardShape)
                                .liquidGlassChrome(cardShape, liquidGlass)
                                .clickable {
                                    haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.TextHandleMove)
                                    onSelect(anim)
                                },
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 14.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(14.dp),
                            ) {
                                Surface(
                                    shape = CircleShape,
                                    color = if (isSelected) MaterialTheme.colorScheme.primary
                                    else MaterialTheme.colorScheme.surfaceContainerLowest,
                                    modifier = Modifier.size(24.dp),
                                ) {
                                    if (isSelected) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Icon(
                                                imageVector = Icons.Filled.Check,
                                                contentDescription = "Selected",
                                                tint = MaterialTheme.colorScheme.onPrimary,
                                                modifier = Modifier.size(16.dp),
                                            )
                                        }
                                    }
                                }

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = anim.title,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                                        color = if (isSelected && liquidGlass) MaterialTheme.colorScheme.onPrimaryContainer
                                        else if (isSelected) MaterialTheme.colorScheme.primary
                                        else MaterialTheme.colorScheme.onSurface,
                                    )
                                    Text(
                                        text = anim.description,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = if (isSelected && liquidGlass) {
                                            MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.78f)
                                        } else {
                                            MaterialTheme.colorScheme.onSurfaceVariant
                                        },
                                    )
                                }
                            }
                        }
                    }
                }
            } else {
                Surface(
                    shape = RoundedCornerShape(18.dp),
                    color = liquidGlassContainerColor(if (liquidGlass) MaterialTheme.colorScheme.surfaceContainerHighest.copy(alpha = 0.70f)
                    else MaterialTheme.colorScheme.surfaceContainerHighest.copy(alpha = 0.40f)),
                    modifier = Modifier.fillMaxWidth().liquidGlassChrome(RoundedCornerShape(18.dp), liquidGlass),
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(
                            text = "New Lyrics UI",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                        Text(
                            text = "Modern lyrics rendering active",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center,
                        )
                    }
                }
            }
        }
    }
}
