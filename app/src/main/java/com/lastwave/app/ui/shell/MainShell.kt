package com.lastwave.app.ui.shell

import androidx.compose.foundation.BorderStroke
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.QueueMusic
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.rounded.Explore
import androidx.compose.material.icons.rounded.GraphicEq
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kyant.backdrop.backdrops.LayerBackdrop
import com.kyant.backdrop.backdrops.rememberLayerBackdrop
import com.lastwave.app.ui.common.ExpressiveMotion
import com.lastwave.app.ui.common.PredictiveBackScreen
import com.lastwave.app.ui.discover.DiscoverScreen
import com.lastwave.app.ui.feed.FeedScreen
import com.lastwave.app.ui.player.LocalMiniPlayerScrollClearance
import com.lastwave.app.ui.player.LocalMusicPlayer
import com.lastwave.app.ui.playlist.PlaylistScreen
import com.lastwave.app.ui.theme.LiquidGlassPreset
import com.lastwave.app.ui.theme.LocalLiquidGlass
import com.lastwave.app.ui.theme.isLiquidGlassBackdropSupported
import com.lastwave.app.ui.theme.liquidGlassChrome
import com.lastwave.app.ui.theme.liquidGlassSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/** Thin bridge exposing AppUpdateManager's live update state to MainShell */
@HiltViewModel
class MainShellViewModel @Inject constructor(
    val appUpdateManager: com.lastwave.app.data.update.AppUpdateManager,
) : ViewModel() {
    val updateInfo = appUpdateManager.updateInfo

    fun dismissUpdate(version: String) {
        appUpdateManager.dismissUpdate(version)
    }

    fun openUpdate(context: android.content.Context) {
        appUpdateManager.openUpdate(context)
    }
}

private enum class MainTab(val labelRes: Int) {
    FEED(com.lastwave.app.R.string.nav_feed),
    DISCOVER(com.lastwave.app.R.string.nav_discover),
    PLAYLISTS(com.lastwave.app.R.string.nav_playlists),
}

/** Shared with any screen hosted inside [MainShell] so their scrolling
 *  lists know how much bottom content padding to reserve. */
object FloatingNavDefaults {
    val ContentBottomPadding = 112.dp

    @Composable
    fun contentBottomPadding(): Dp =
        ContentBottomPadding +
            LocalMiniPlayerScrollClearance.current +
            WindowInsets.safeDrawing.asPaddingValues().calculateBottomPadding()
}

private val DockShape: Shape = RoundedCornerShape(32.dp)
private val PillShape: Shape = CircleShape

private fun <T> navSpring() = ExpressiveMotion.spatialSpring<T>()

@Composable
fun MainShell(
    onOpenSettings: () -> Unit,
    onOpenSearch: () -> Unit,
    onOpenDiscover: () -> Unit,
    onOpenFriends: () -> Unit,
    onOpenFriendProfile: (username: String, displayName: String?, avatarUrl: String?) -> Unit = { _, _, _ -> },
    onOpenFeedPlaylist: (String) -> Unit,
    onOpenPlaylist: (Long) -> Unit = {},
    onOpenGenerator: () -> Unit = {},
    onOpenNewReleases: () -> Unit = {},
    onOpenFullPlayer: () -> Unit = com.lastwave.app.ui.player.LocalExpandPlayer.current,
    mainShellViewModel: MainShellViewModel = hiltViewModel(),
) {
    val tabs = MainTab.entries
    val pagerState = rememberPagerState(pageCount = { tabs.size })
    val scope = rememberCoroutineScope()
    val navigationBackdrop = if (isLiquidGlassBackdropSupported()) rememberLayerBackdrop() else null

    val musicPlayer = LocalMusicPlayer.current
    val chromeState by musicPlayer.chromeState.collectAsStateWithLifecycle()
    val currentTrack = chromeState.current

    Box(Modifier.fillMaxSize()) {
        val feedIndex = tabs.indexOf(MainTab.FEED)
        HorizontalPager(
            state = pagerState,
            beyondViewportPageCount = 0,
            modifier = Modifier.fillMaxSize().liquidGlassSource(navigationBackdrop),
        ) { page ->
            val isCurrent = page == pagerState.currentPage
            PredictiveBackScreen(
                enabled = isCurrent && tabs[page] != MainTab.FEED,
                onBack = { scope.launch { pagerState.animateScrollToPage(feedIndex) } },
            ) {
                when (tabs[page]) {
                    MainTab.FEED -> FeedScreen(
                        onOpenSettings = onOpenSettings,
                        onOpenSearch = onOpenSearch,
                        onOpenDiscover = onOpenDiscover,
                        onOpenPlaylist = onOpenPlaylist,
                        onOpenFeedPlaylist = onOpenFeedPlaylist,
                        onOpenGenerator = onOpenGenerator,
                        onOpenFriends = onOpenFriends,
                        onOpenFriendProfile = onOpenFriendProfile,
                        onOpenNewReleases = onOpenNewReleases,
                    )
                    MainTab.DISCOVER -> DiscoverScreen(
                        onBack = { scope.launch { pagerState.animateScrollToPage(feedIndex) } },
                    )
                    MainTab.PLAYLISTS -> PlaylistScreen(
                        onOpenPlaylist = onOpenPlaylist,
                    )
                }
            }
        }

        // Persistent MiniPlayer
        AnimatedVisibility(
            visible = currentTrack != null,
            enter = slideInVertically { it } + fadeIn(),
            exit = slideOutVertically { it } + fadeOut(),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 84.dp)
                .windowInsetsPadding(
                    WindowInsets.safeDrawing.only(WindowInsetsSides.Bottom + WindowInsetsSides.Horizontal),
                ),
        ) {
            com.lastwave.app.ui.player.ExpressiveMiniPlayer(
                playbackState = chromeState,
                onExpand = onOpenFullPlayer,
                onPlayPause = { musicPlayer.togglePlayPause() },
                onNext = { musicPlayer.next() },
                onPrevious = { musicPlayer.previous() },
                modifier = Modifier
                    .fillMaxWidth(0.94f)
                    .padding(horizontal = 8.dp),
            )
        }

        FloatingNavBar(
            backdrop = navigationBackdrop,
            tabs = tabs,
            selectedIndex = pagerState.currentPage,
            isPlaying = chromeState.isPlaying,
            onSelect = { index -> scope.launch { pagerState.animateScrollToPage(index) } },
            modifier = Modifier.align(Alignment.BottomCenter),
        )
    }
}

@Composable
private fun FloatingNavBar(
    backdrop: LayerBackdrop?,
    tabs: List<MainTab>,
    selectedIndex: Int,
    isPlaying: Boolean,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val liquidGlass = LocalLiquidGlass.current
    val effectiveDockShape = if (liquidGlass) DockShape else CircleShape
    val navBarHeight = if (liquidGlass) 60.dp else 56.dp
    val navHorizontalPadding = if (liquidGlass) 8.dp else 10.dp

    Box(
        modifier = modifier
            .windowInsetsPadding(
                WindowInsets.safeDrawing.only(WindowInsetsSides.Bottom + WindowInsetsSides.Horizontal),
            )
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .animateContentSize(animationSpec = navSpring()),
        contentAlignment = Alignment.Center,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            Surface(
                shape = effectiveDockShape,
                color = if (liquidGlass) MaterialTheme.colorScheme.surfaceContainerHigh.copy(alpha = 0.80f)
                        else MaterialTheme.colorScheme.surfaceContainerHighest,
                border = if (liquidGlass) null else BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.35f)),
                tonalElevation = if (liquidGlass) 0.dp else 6.dp,
                shadowElevation = if (liquidGlass) 0.dp else 8.dp,
                modifier = Modifier
                    .height(navBarHeight)
                    .clip(effectiveDockShape)
                    .liquidGlassChrome(effectiveDockShape, liquidGlass, LiquidGlassPreset.BottomNavigation, backdrop),
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = navHorizontalPadding, vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    tabs.forEachIndexed { index, tab ->
                        val onClick = remember(index) { { onSelect(index) } }
                        FloatingNavItem(
                            label = androidx.compose.ui.res.stringResource(tab.labelRes),
                            icon = tab.icon(),
                            selected = selectedIndex == index,
                            isPlaying = selectedIndex == index && isPlaying,
                            onClick = onClick,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FloatingNavItem(
    label: String,
    icon: ImageVector,
    selected: Boolean,
    isPlaying: Boolean,
    onClick: () -> Unit,
) {
    val liquidGlass = LocalLiquidGlass.current
    val backgroundColor by animateColorAsState(
        targetValue = if (selected) MaterialTheme.colorScheme.primaryContainer.copy(
            alpha = if (liquidGlass) 0.28f else 1f,
        ) else Color.Transparent,
        animationSpec = navSpring(),
        label = "navItemBackground",
    )
    val contentColor by animateColorAsState(
        targetValue = if (selected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant,
        animationSpec = navSpring(),
        label = "navItemContent",
    )

    val activeIcon = if (isPlaying) Icons.Rounded.GraphicEq else icon

    if (!liquidGlass) {
        Surface(
            onClick = onClick,
            shape = PillShape,
            color = backgroundColor,
            modifier = Modifier
                .width(76.dp)
                .height(48.dp),
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize(),
            ) {
                Icon(
                    imageVector = activeIcon,
                    contentDescription = label,
                    tint = contentColor,
                    modifier = Modifier.size(20.dp),
                )
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                    color = contentColor,
                    maxLines = 1,
                    softWrap = false,
                )
            }
        }
    } else {
        Surface(
            onClick = onClick,
            shape = PillShape,
            color = backgroundColor,
            modifier = Modifier
                .height(48.dp)
                .animateContentSize(animationSpec = navSpring()),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .padding(horizontal = if (selected) 18.dp else 12.dp)
                    .height(48.dp),
            ) {
                Icon(
                    imageVector = activeIcon,
                    contentDescription = label,
                    tint = contentColor,
                    modifier = Modifier.size(22.dp),
                )
                AnimatedVisibility(
                    visible = selected,
                    enter = fadeIn(animationSpec = navSpring()) + expandHorizontally(
                        animationSpec = navSpring(),
                        expandFrom = Alignment.Start,
                    ),
                    exit = fadeOut(animationSpec = tween(90)) + shrinkHorizontally(
                        animationSpec = navSpring(),
                        shrinkTowards = Alignment.Start,
                    ),
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(start = 6.dp),
                    ) {
                        Text(
                            text = label,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = contentColor,
                            maxLines = 1,
                            softWrap = false,
                        )
                    }
                }
            }
        }
    }
}

private fun MainTab.icon(): ImageVector = when (this) {
    MainTab.FEED -> Icons.Filled.Home
    MainTab.DISCOVER -> Icons.Rounded.Explore
    MainTab.PLAYLISTS -> Icons.AutoMirrored.Filled.QueueMusic
}
