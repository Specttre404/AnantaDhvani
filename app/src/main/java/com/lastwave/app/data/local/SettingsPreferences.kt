package com.lastwave.app.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

enum class LyricsUiVersion(val id: String, val title: String) {
    CLASSIC("classic", "Classic"),
    MODERN("modern", "Modern");

    companion object {
        fun fromId(id: String?): LyricsUiVersion =
            entries.firstOrNull { it.id == id } ?: MODERN
    }
}

enum class PlayerCoverStyle(val id: String, val title: String) {
    SQUARE_COVER("square", "Standard Square Cover"),
    ROTATING_VINYL("vinyl", "Rotating Vinyl Record");

    companion object {
        fun fromId(id: String?): PlayerCoverStyle =
            entries.firstOrNull { it.id == id } ?: SQUARE_COVER
    }
}

enum class AppIconTheme(val id: String, val title: String) {
    DARK("dark", "Expressive Dark (Obsidian)"),
    LIGHT("light", "Expressive Light (Porcelain)");

    companion object {
        fun fromId(id: String?): AppIconTheme =
            entries.firstOrNull { it.id == id } ?: DARK
    }
}

enum class PreferredAudioCodec(val id: String, val label: String) {
    AUTO("auto", "Auto (Highest Bitrate)"),
    FORCE_OPUS("force_opus", "Force Opus / WebM (160–256 kbps)"),
    FORCE_AAC("force_aac", "Force AAC / M4A (128–256 kbps)"),
    FORCE_LOSSLESS("force_lossless", "Force Lossless / FLAC Direct");

    companion object {
        fun fromId(id: String?): PreferredAudioCodec =
            entries.firstOrNull { it.id == id } ?: AUTO
    }
}

enum class MiniPlayerSwipeStyle(val id: String, val title: String) {
    SKIP_TRACKS("skip_tracks", "Swipe Left/Right to Skip Tracks"),
    OPEN_QUEUE("open_queue", "Swipe Up to Open Queue"),
    DISMISS_PLAYER("dismiss_player", "Swipe Down to Dismiss Player");

    companion object {
        fun fromId(id: String?): MiniPlayerSwipeStyle =
            entries.firstOrNull { it.id == id } ?: SKIP_TRACKS
    }
}

enum class SeekbarStyle(val id: String, val title: String, val description: String) {
    WAVY_FLUID("wavy_fluid", "Wavy Fluid", "Multi-frequency sine wave undulating during playback and flattening on pause."),
    SEGMENTED_DASH("segmented_dash", "Segmented Dash", "High-tech discrete dashed time bar with glowing progress heads."),
    MINIMAL_PILL("minimal_pill", "Minimal Pill", "Ultra-slim line that expands into a tactile pill on touch."),
    STUDIO_CONSOLE("studio_console", "Studio Console", "Precision analog mixing desk fader with tick marks and exact time readout."),
    CAPSULE_PILL("capsule_pill", "Capsule Pill", "Tactile stadium capsule slider with smooth progress fill.");

    companion object {
        fun fromId(id: String?): SeekbarStyle =
            entries.firstOrNull { it.id == id } ?: WAVY_FLUID
    }
}

enum class PlayerStyle(val id: String, val title: String, val description: String) {
    MODERN_M3("modern_m3", "Modern M3 Expressive", "Adaptive layout with clean typography, dynamic spacing, and floating action dock"),
    CLASSIC("classic", "Classic", "Centered square artwork with balanced linear controllers and full transport rows"),
    IMMERSIVE_FULLSCREEN("immersive_fullscreen", "Immersive Fullscreen", "Edge-to-edge artwork presentation with subtle semi-transparent controls"),
    MINIMALIST("minimalist", "Minimalist", "Typography-first distraction-free mode with essential controls only"),
    COMPACT_DOCK("compact_dock", "Compact Dock", "One-handed reachability layout with controls closer to the bottom");

    companion object {
        fun fromId(id: String?): PlayerStyle =
            entries.firstOrNull { it.id == id } ?: MODERN_M3
    }
}

enum class PlayerBackgroundStyle(val id: String, val title: String, val description: String) {
    HDR_VIVID("hdr_vivid", "HDR High-Contrast", "Pushes luminance boundaries for ultra-vivid peaks and deep blacks."),
    FLUID_GRADIENT("fluid_gradient", "Mesh Gradient Flow", "Multi-stop organic color mesh that subtly shifts with audio transients."),
    DYNAMIC_HARMONY("dynamic_harmony", "Dynamic Material You", "Real-time palette extraction adapting to album artwork."),
    AMBIENT_GLOW("ambient_glow", "Ambient Aura Bloom", "Soft diffused color bleeding casting an atmospheric halo."),
    DYNAMIC_MONET("dynamic_monet", "Monet System", "Classic harmonious adaptation to system-wide colors."),
    AMOLED_BLACK("amoled_black", "Pure AMOLED", "Absolute zero-pixel emission for battery conservation."),
    BLURRED_GLASS("blurred_glass", "Liquid Glass Blur", "Frosted glass refraction stacked over backdrop elements."),
    PRISM_SPECTRUM("prism_spectrum", "Prism Spectrum", "Full-spectrum iridescent chromatic dispersion.");

    companion object {
        fun fromId(id: String?): PlayerBackgroundStyle =
            entries.firstOrNull { it.id == id } ?: BLURRED_GLASS
    }
}

enum class LyricsAnimation(val id: String, val title: String, val description: String) {
    APPLE_FLUID("apple_fluid", "Apple Fluid", "Smooth spring scaling with dynamic focal tracking"),
    KARAOKE_PULSE("karaoke_pulse", "Karaoke Pulse", "Rhythmic scale pop with energetic spring bounce"),
    KINETIC_SLIDE("kinetic_slide", "Kinetic Slide", "Active line glides smoothly from leading edge"),
    CINEMATIC_BLUR("cinematic_blur", "Cinematic Focus", "Soft background blur & vertical drift on past lines"),
    LOSSLESS_GLOW("lossless_glow", "Lossless Glow", "Vibrant gradient text with frosted glass reflection"),
    CARD_POP("card_pop", "Glass Elevation", "3D floating glass card lift with specular highlights"),
    APPLE_ZOOM("apple_zoom", "Dynamic Focus Zoom", "Expanded focal magnification with fluid spring push"),
    MINIMAL_WAVE("minimal_wave", "Minimal Clean", "Pure low-latency opacity transitions without distortion");

    companion object {
        fun fromId(id: String?): LyricsAnimation =
            entries.firstOrNull { it.id == id } ?: APPLE_FLUID
    }
}

data class MiscSettings(
    /** When on, the app's accent color follows the dominant color of the
     *  currently-scrobbling track's artwork (Home's "now playing" track),
     *  updating live as that track changes. Falls back to the user's
     *  regular selected accent whenever nothing is playing or artwork
     *  colors can't be extracted — see ThemeRepository.updateNowPlayingArtwork. */
    val dynamicNowPlayingEnabled: Boolean = false,
    /** "Use Application Font" — on: the bundled Google Sans Flex variable
     *  font (see ui/theme/Type.kt); off: the device's own system font.
     *  Defaults on so the app ships with its own identity out of the box. */
    val useCustomFont: Boolean = true,
    /** Last.fm usernames pinned to the top of Home's friend-switcher sheet
     *  (long-press a friend row to toggle). Order among pinned friends
     *  follows whatever order user.getfriends itself returns them in —
     *  just filtered to the front, not independently reorderable. */
    val pinnedFriends: Set<String> = emptySet(),
    /** When true, the player attempts to resolve and stream lossless / Hi-Res audio
     *  directly from lossless CDN when a high-confidence match exists. Falls back to YouTube Music. */
    val preferLosslessStreaming: Boolean = true,
    /** When true, the player also queries installed provider modules in
     *  parallel with the backend; a module hit plays instantly on miss. */
    val preferProviderModules: Boolean = true,
    /** Preferred quality preset for lossless streaming (27: 24/192, 7: 24/96, 6: 16/44.1, 5: 320k).
     *  If a track does not support the requested quality, the worker automatically selects the highest available. */
    val losslessQuality: Int = 27,
    /** Preferred quality preset for cellular data streaming (-1: Data Saver 160k, 5: High 320k, 6/27: Lossless). */
    val cellularStreamingQuality: Int = -1,
    /** Preferred quality preset for Wi-Fi streaming (27: 24/192 Lossless, 7: 24/96, 6: 16/44.1, 5: 320k). */
    val wifiStreamingQuality: Int = 27,
    /** When true (default), automatically switches quality presets based on active network connection. */
    val autoDataSaverEnabled: Boolean = true,
    /** Preferred quality preset for downloads (27: 24/192, 7: 24/96, 6: 16/44.1, 5: 320k, -1: YouTube Music). */
    val downloadQuality: Int = 27,
    /** Optional studio-clarity curve. On by default; Bit-Perfect disables it. */
    val isStudioMasterClarityEnabled: Boolean = true,
    /** When true, completely bypasses DSP, EQ, tone effects, and software volume ducking for bit-exact audio. */
    val isBitPerfectEnabled: Boolean = false,
    /** Lyrics UI layout version (Classic or Modern). */
    val lyricsUiVersion: LyricsUiVersion = LyricsUiVersion.MODERN,
    val wordByWordLyrics: Boolean = true,
    val showLyricsTranslation: Boolean = true,
    val showLyricsPhonetic: Boolean = true,
    /** Experimental lyrics animation style (Settings -> Experimental -> Lyrics Animation). */
    val lyricsAnimation: LyricsAnimation = LyricsAnimation.APPLE_FLUID,
    /** Blend the end of one queued track into the beginning of the next. */
    val crossfadeEnabled: Boolean = false,
    /** Crossfade length in seconds; kept within the native settings slider range. */
    val crossfadeSeconds: Int = 4,
    /** Automatically skip silent periods during audio playback. */
    val skipSilenceEnabled: Boolean = false,
    /** Balance volume levels across different tracks to prevent volume spikes. */
    val loudnessNormalizationEnabled: Boolean = true,
    /** Automatically skip non-music video intros, outros, and chatter using SponsorBlock. */
    val sponsorBlockEnabled: Boolean = true,
    /** Skip music video intro commentary and non-music segments. */
    val skipMusicVideoIntros: Boolean = true,
    /** Share current track, artist & progress status on Discord. */
    val discordRpcEnabled: Boolean = false,
    val discordUserToken: String = "",
    val discordConnectedUsername: String = "",
    /** Binaural Headphone Crossfeed Acoustic Room Tuning. */
    val crossfeedEnabled: Boolean = false,
    val crossfeedLevelDb: Float = 4.5f,
    val crossfeedCutoffHz: Float = 700f,
    /** Shake device to skip to next track during playback. */
    val shakeToSkipEnabled: Boolean = false,
    /** Player Now Playing artwork style (Square Cover or Rotating Vinyl). */
    val playerCoverStyle: PlayerCoverStyle = PlayerCoverStyle.SQUARE_COVER,
    /** Player UI layout style architecture (9 options). */
    val playerStyle: PlayerStyle = PlayerStyle.MODERN_M3,
    /** Player backdrop canvas rendering style (8 options). */
    val playerBackgroundStyle: PlayerBackgroundStyle = PlayerBackgroundStyle.BLURRED_GLASS,
    /** When true (default), uses the multi-layer dynamic wavy seekbar.
     *  When false, uses the classic standard progress slider in the player tab. */
    val wavySeekbarEnabled: Boolean = true,
    /** Selectable seekbar style (Wavy Fluid, Segmented Dash, Minimal Pill, Studio Console). */
    val seekbarStyle: SeekbarStyle = SeekbarStyle.WAVY_FLUID,
    /** When true (default), downloads fetch and save synced lyrics (.lrc companion files and embedded tags). */
    val downloadLyrics: Boolean = true,
    /** In-app language override tag: "system" (default), "en", "tr", "zh-Hans". */
    val appLanguageTag: String = AppLanguage.SYSTEM.tag,
    /** Subfolder name under public Music/ where downloads are saved.
     *  Default "LastWave" -> Music/LastWave. Scoped storage (API 29+)
     *  forbids arbitrary paths, so only the leaf folder name is configurable. */
    val downloadFolder: String = DEFAULT_DOWNLOAD_FOLDER,
    /** Subfolder layout under Music/<downloadFolder>/ (flat by default). */
    val downloadStructure: DownloadFolderStructure = DownloadFolderStructure.FLAT,
    /** When true, folder names use the album-artist tag when available (falls back to track artist). */
    val useAlbumArtistForFolders: Boolean = true,
    /** When true, folder names use only the primary artist (strips feat./collaborators). */
    val primaryArtistOnly: Boolean = true,
    /** Ids of Home tab sections the user hid ([HomeSection.id]).
     *  Empty = everything visible. Unknown ids are dropped on read. */
    val hiddenHomeSections: Set<String> = emptySet(),
    /** Optional floating dynamic island capsule/notch overlay settings. */
    val enableDynamicIslandNotch: Boolean = false,
    val notchTopMarginDp: Int = 4,
    val notchCapsuleWidthDp: Int = 220,
    val notchAutoDismissSec: Int = 4,
    /** Local disk cache quota in MB (default 2048 MB, -1 for unlimited). */
    val cacheQuotaMb: Long = 2048L,
    /** Active launcher app icon theme. */
    val appIconTheme: AppIconTheme = AppIconTheme.DARK,
    /** ExoPlayer LoadControl buffer preferences (in milliseconds). */
    val minBufferMs: Int = 15_000,
    val maxBufferMs: Int = 50_000,
    val bufferForPlaybackMs: Int = 2_500,
    val bufferForPlaybackAfterRebufferMs: Int = 5_000,
    /** Hardware Audio Offload / Direct SoC Tunneling mode. */
    val audioOffloadEnabled: Boolean = false,
    /** Mini player swipe action behavior. */
    val miniPlayerSwipeStyle: MiniPlayerSwipeStyle = MiniPlayerSwipeStyle.SKIP_TRACKS,
    /** Global lyrics sync calibration offset in milliseconds (-2000ms to +2000ms). */
    val lyricsOffsetMs: Long = 0L,
    /** Stream codec & container forcing preference. */
    val preferredAudioCodec: PreferredAudioCodec = PreferredAudioCodec.AUTO,
    /** Silence trimming parameters. */
    val silenceThresholdDb: Float = -42.0f,
    val minSilenceDurationMs: Long = 250L,
    /** Incognito / Private listening session. */
    val isIncognitoMode: Boolean = false,
) {
    val cellularQuality: Int get() = cellularStreamingQuality
    val wifiQuality: Int get() = wifiStreamingQuality
    val autoNetworkQualityEnabled: Boolean get() = autoDataSaverEnabled
}

/** Toggleable sections of the Home tab (see FeedScreen). Hero greeting and
 *  footer are structural; everything listed here can be hidden by the user.
 *  Titles are string resources so HomeSectionsScreen follows the app language. */
enum class HomeSection(val id: String, val titleRes: Int, val subtitleRes: Int) {
    HERO("hero", com.lastwave.app.R.string.home_section_hero_title, com.lastwave.app.R.string.home_section_hero_sub),
    QUICK_TILES("quick_tiles", com.lastwave.app.R.string.home_section_quick_tiles_title, com.lastwave.app.R.string.home_section_quick_tiles_sub),
    TASTE_STRIP("taste_strip", com.lastwave.app.R.string.home_section_taste_strip_title, com.lastwave.app.R.string.home_section_taste_strip_sub),
    QUICK_PICKS("quick_picks", com.lastwave.app.R.string.home_section_quick_picks_title, com.lastwave.app.R.string.home_section_quick_picks_sub),
    BECAUSE_YOU_LISTEN_TO("because_you_listen_to", com.lastwave.app.R.string.home_section_because_title, com.lastwave.app.R.string.home_section_because_sub),
    FRESH_FINDS("fresh_finds", com.lastwave.app.R.string.home_section_fresh_title, com.lastwave.app.R.string.home_section_fresh_sub),
    JUMP_BACK_IN("jump_back_in", com.lastwave.app.R.string.home_section_jump_title, com.lastwave.app.R.string.home_section_jump_sub),
    MIXES("mixed_for_you", com.lastwave.app.R.string.home_section_mixes_title, com.lastwave.app.R.string.home_section_mixes_sub),
    SPOTLIGHT("spotlight_hero", com.lastwave.app.R.string.home_section_spotlight_title, com.lastwave.app.R.string.home_section_spotlight_sub),
    TOP_ARTISTS("top_artists", com.lastwave.app.R.string.home_section_artists_title, com.lastwave.app.R.string.home_section_artists_sub),
    HEAVY_ROTATION("heavy_rotation", com.lastwave.app.R.string.home_section_heavy_title, com.lastwave.app.R.string.home_section_heavy_sub),
    ALBUMS("albums_in_rotation", com.lastwave.app.R.string.home_section_albums_title, com.lastwave.app.R.string.home_section_albums_sub),
    CHARTS("trending_charts", com.lastwave.app.R.string.home_section_charts_title, com.lastwave.app.R.string.home_section_charts_sub),
    NEW_RELEASES("new_releases", com.lastwave.app.R.string.home_section_releases_title, com.lastwave.app.R.string.home_section_releases_sub),
    FRIENDS("friends_activity", com.lastwave.app.R.string.home_section_friends_title, com.lastwave.app.R.string.home_section_friends_sub);

    companion object {
        fun fromId(id: String?): HomeSection? =
            entries.firstOrNull { it.id == id }
    }
}

/** Subfolder layout for downloaded tracks under Music/<downloadFolder>/.
 *  Titles are string resources; [example] stays raw (filesystem paths are universal). */
enum class DownloadFolderStructure(val id: String, val titleRes: Int, val example: String, val shortLabelRes: Int) {
    FLAT("flat", com.lastwave.app.R.string.dl_struct_flat_title, "Music/<dir>/song.flac", com.lastwave.app.R.string.dl_struct_flat_short),
    ARTIST_ALBUM("artist_album", com.lastwave.app.R.string.dl_struct_artist_album_title, "Artist/Album/", com.lastwave.app.R.string.dl_struct_artist_album_short),
    ARTIST_YEAR_ALBUM("artist_year_album", com.lastwave.app.R.string.dl_struct_artist_year_album_title, "Artist/[2025] Album/", com.lastwave.app.R.string.dl_struct_artist_year_album_short),
    ALBUM_ONLY("album_only", com.lastwave.app.R.string.dl_struct_album_only_title, "Album/", com.lastwave.app.R.string.dl_struct_album_only_short),
    YEAR_ALBUM("year_album", com.lastwave.app.R.string.dl_struct_year_album_title, "[2025] Album/", com.lastwave.app.R.string.dl_struct_year_album_short),
    ARTIST_ALBUM_SINGLES("artist_album_singles", com.lastwave.app.R.string.dl_struct_artist_album_singles_title, "Artist/Album/ and Artist/Singles/", com.lastwave.app.R.string.dl_struct_artist_album_singles_short),
    ARTIST_ALBUM_SINGLES_FLAT("artist_album_singles_flat", com.lastwave.app.R.string.dl_struct_artist_album_singles_flat_title, "Artist/Album/ and Artist/song.flac", com.lastwave.app.R.string.dl_struct_artist_album_singles_flat_short);

    companion object {
        fun fromId(id: String?): DownloadFolderStructure =
            entries.firstOrNull { it.id == id } ?: FLAT
    }
}

/** Default subfolder under Music/ for offline tracks (Music/LastWave). */
const val DEFAULT_DOWNLOAD_FOLDER = "LastWave"

/** Sanitize a user-entered download folder to a safe single path segment:
 *  no separators, no traversal, max 40 chars, fallback to default. */
fun sanitizeDownloadFolderName(raw: String?): String {
    var name = raw?.trim().orEmpty()
    // Prevent path traversal: keep only the last segment.
    name = name.split('/', '\\').lastOrNull()?.trim().orEmpty()
    name = name.replace(Regex("[\\\\/:*?\"<>|]"), "_").trim().trim('.', '_').trim()
    if (name.length > 40) name = name.take(40).trim()
    if (name.isBlank() || name == "." || name == "..") return DEFAULT_DOWNLOAD_FOLDER
    return name
}

/** Small dedicated prefs object for settings that don't fit ThemePreferences
 *  or SessionPreferences semantically — shares the app's single DataStore. */
@Singleton
class SettingsPreferences @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    @ApplicationContext private val appContext: Context,
) {
    private object Keys {
        val DYNAMIC_NOW_PLAYING = booleanPreferencesKey("lw_dynamic_now_playing")
        val USE_CUSTOM_FONT = booleanPreferencesKey("lw_use_custom_font")
        val PINNED_FRIENDS = stringSetPreferencesKey("lw_pinned_friends")
        val PREFER_LOSSLESS_STREAMING = booleanPreferencesKey("lw_prefer_lossless_streaming")
        val PREFER_PROVIDER_MODULES = booleanPreferencesKey("lw_prefer_provider_modules")
        val LOSSLESS_QUALITY = intPreferencesKey("lw_lossless_quality")
        val CELLULAR_STREAMING_QUALITY = intPreferencesKey("lw_cellular_streaming_quality")
        val WIFI_STREAMING_QUALITY = intPreferencesKey("lw_wifi_streaming_quality")
        val AUTO_DATA_SAVER_ENABLED = booleanPreferencesKey("lw_auto_data_saver_enabled")
        val DOWNLOAD_QUALITY = intPreferencesKey("lw_download_quality")
        val MUSIC_ENHANCER = booleanPreferencesKey("lw_music_enhancer")
        val BIT_PERFECT_ENABLED = booleanPreferencesKey("lw_bit_perfect_enabled")
        val LYRICS_UI_VERSION = stringPreferencesKey("lw_lyrics_ui_version")
        val WORD_BY_WORD_LYRICS = booleanPreferencesKey("lw_word_by_word_lyrics")
        val SHOW_LYRICS_TRANSLATION = booleanPreferencesKey("lw_show_lyrics_translation")
        val SHOW_LYRICS_PHONETIC = booleanPreferencesKey("lw_show_lyrics_phonetic")
        val LYRICS_ANIMATION = stringPreferencesKey("lw_lyrics_animation")
        val CROSSFADE_ENABLED = booleanPreferencesKey("lw_crossfade_enabled")
        val CROSSFADE_SECONDS = intPreferencesKey("lw_crossfade_seconds")
        val SKIP_SILENCE_ENABLED = booleanPreferencesKey("lw_skip_silence_enabled")
        val LOUDNESS_NORMALIZATION_ENABLED = booleanPreferencesKey("lw_loudness_normalization_enabled")
        val SPONSOR_BLOCK_ENABLED = booleanPreferencesKey("lw_sponsor_block_enabled")
        val SKIP_MUSIC_VIDEO_INTROS = booleanPreferencesKey("lw_skip_music_video_intros")
        val DISCORD_RPC_ENABLED = booleanPreferencesKey("lw_discord_rpc_enabled")
        val DISCORD_USER_TOKEN = stringPreferencesKey("lw_discord_user_token")
        val DISCORD_CONNECTED_USERNAME = stringPreferencesKey("lw_discord_connected_username")
        val CROSSFEED_ENABLED = booleanPreferencesKey("lw_crossfeed_enabled")
        val CROSSFEED_LEVEL_DB = floatPreferencesKey("lw_crossfeed_level_db")
        val CROSSFEED_CUTOFF_HZ = floatPreferencesKey("lw_crossfeed_cutoff_hz")
        val SHAKE_TO_SKIP_ENABLED = booleanPreferencesKey("lw_shake_to_skip_enabled")
        val PLAYER_COVER_STYLE = stringPreferencesKey("lw_player_cover_style")
        val PLAYER_STYLE = stringPreferencesKey("lw_player_style")
        val PLAYER_BACKGROUND_STYLE = stringPreferencesKey("lw_player_background_style")
        val BACKGROUND_STYLE_INDEX = intPreferencesKey("background_style_index")
        val WAVY_SEEKBAR_ENABLED = booleanPreferencesKey("lw_wavy_seekbar_enabled")
        val SEEKBAR_STYLE = stringPreferencesKey("lw_seekbar_style")
        val DOWNLOAD_LYRICS = booleanPreferencesKey("lw_download_lyrics")
        val APP_LANGUAGE = stringPreferencesKey("lw_app_language")
        val DOWNLOAD_FOLDER = stringPreferencesKey("lw_download_folder")
        val DOWNLOAD_STRUCTURE = stringPreferencesKey("lw_download_structure")
        val USE_ALBUM_ARTIST_FOLDERS = booleanPreferencesKey("lw_use_album_artist_folders")
        val PRIMARY_ARTIST_ONLY = booleanPreferencesKey("lw_primary_artist_only")
        val HIDDEN_HOME_SECTIONS = stringSetPreferencesKey("lw_hidden_home_sections")
        val ENABLE_DYNAMIC_ISLAND_NOTCH = booleanPreferencesKey("lw_enable_dynamic_island_notch")
        val NOTCH_TOP_MARGIN_DP = intPreferencesKey("lw_notch_top_margin_dp")
        val NOTCH_CAPSULE_WIDTH_DP = intPreferencesKey("lw_notch_capsule_width_dp")
        val NOTCH_AUTO_DISMISS_SEC = intPreferencesKey("lw_notch_auto_dismiss_sec")
        val APP_ICON_THEME = stringPreferencesKey("lw_app_icon_theme")
        val CACHE_QUOTA_MB = longPreferencesKey("lw_cache_quota_mb")
        val MIN_BUFFER_MS = intPreferencesKey("lw_min_buffer_ms")
        val MAX_BUFFER_MS = intPreferencesKey("lw_max_buffer_ms")
        val BUFFER_FOR_PLAYBACK_MS = intPreferencesKey("lw_buffer_for_playback_ms")
        val BUFFER_FOR_PLAYBACK_AFTER_REBUFFER_MS = intPreferencesKey("lw_buffer_for_playback_after_rebuffer_ms")
        val AUDIO_OFFLOAD_ENABLED = booleanPreferencesKey("lw_audio_offload_enabled")
        val LYRICS_OFFSET_MS = longPreferencesKey("lw_lyrics_offset_ms")
        val MINI_PLAYER_SWIPE_STYLE = stringPreferencesKey("lw_mini_player_swipe_style")
        val PREFERRED_AUDIO_CODEC = stringPreferencesKey("lw_preferred_audio_codec")
        val SILENCE_THRESHOLD_DB = floatPreferencesKey("lw_silence_threshold_db")
        val MIN_SILENCE_DURATION_MS = longPreferencesKey("lw_min_silence_duration_ms")
        val IS_INCOGNITO_MODE = booleanPreferencesKey("lw_is_incognito_mode")
    }

    val settings: Flow<MiscSettings> = dataStore.data
        .recoverPreferences("SettingsPreferences")
        .map { p ->
            MiscSettings(
                dynamicNowPlayingEnabled = p.readSafely(Keys.DYNAMIC_NOW_PLAYING) ?: false,
                useCustomFont = p.readSafely(Keys.USE_CUSTOM_FONT) ?: true,
                pinnedFriends = p.readSafely(Keys.PINNED_FRIENDS) ?: emptySet(),
                preferLosslessStreaming = p.readSafely(Keys.PREFER_LOSSLESS_STREAMING) ?: true,
                preferProviderModules = p.readSafely(Keys.PREFER_PROVIDER_MODULES) ?: true,
                losslessQuality = p.readSafely(Keys.LOSSLESS_QUALITY)?.takeIf { it in LOSSLESS_QUALITIES } ?: 27,
                cellularStreamingQuality = p.readSafely(Keys.CELLULAR_STREAMING_QUALITY)?.takeIf { it in LOSSLESS_QUALITIES } ?: -1,
                wifiStreamingQuality = p.readSafely(Keys.WIFI_STREAMING_QUALITY)?.takeIf { it in LOSSLESS_QUALITIES } ?: 27,
                autoDataSaverEnabled = p.readSafely(Keys.AUTO_DATA_SAVER_ENABLED) ?: true,
                downloadQuality = p.readSafely(Keys.DOWNLOAD_QUALITY)?.takeIf { it in DOWNLOAD_QUALITIES } ?: 27,
                isStudioMasterClarityEnabled = p.readSafely(Keys.MUSIC_ENHANCER) ?: true,
                isBitPerfectEnabled = p.readSafely(Keys.BIT_PERFECT_ENABLED) ?: false,
                lyricsUiVersion = LyricsUiVersion.fromId(p.readSafely(Keys.LYRICS_UI_VERSION)),
                wordByWordLyrics = p.readSafely(Keys.WORD_BY_WORD_LYRICS) ?: true,
                showLyricsTranslation = p.readSafely(Keys.SHOW_LYRICS_TRANSLATION) ?: true,
                showLyricsPhonetic = p.readSafely(Keys.SHOW_LYRICS_PHONETIC) ?: true,
                lyricsAnimation = LyricsAnimation.fromId(p.readSafely(Keys.LYRICS_ANIMATION)),
                crossfadeEnabled = p.readSafely(Keys.CROSSFADE_ENABLED) ?: false,
                crossfadeSeconds = (p.readSafely(Keys.CROSSFADE_SECONDS) ?: 4).coerceIn(1, 12),
                skipSilenceEnabled = p.readSafely(Keys.SKIP_SILENCE_ENABLED) ?: false,
                loudnessNormalizationEnabled = p.readSafely(Keys.LOUDNESS_NORMALIZATION_ENABLED) ?: true,
                sponsorBlockEnabled = p.readSafely(Keys.SPONSOR_BLOCK_ENABLED) ?: true,
                skipMusicVideoIntros = p.readSafely(Keys.SKIP_MUSIC_VIDEO_INTROS) ?: true,
                discordRpcEnabled = p.readSafely(Keys.DISCORD_RPC_ENABLED) ?: false,
                discordUserToken = p.readSafely(Keys.DISCORD_USER_TOKEN).orEmpty(),
                discordConnectedUsername = p.readSafely(Keys.DISCORD_CONNECTED_USERNAME).orEmpty(),
                crossfeedEnabled = p.readSafely(Keys.CROSSFEED_ENABLED) ?: false,
                crossfeedLevelDb = p.readSafely(Keys.CROSSFEED_LEVEL_DB)?.takeIf { it.isFinite() }?.coerceIn(3.0f, 9.5f) ?: 4.5f,
                crossfeedCutoffHz = p.readSafely(Keys.CROSSFEED_CUTOFF_HZ)?.takeIf { it.isFinite() } ?: 700.0f,
                shakeToSkipEnabled = p.readSafely(Keys.SHAKE_TO_SKIP_ENABLED) ?: false,
                playerCoverStyle = PlayerCoverStyle.fromId(p.readSafely(Keys.PLAYER_COVER_STYLE)),
                playerStyle = PlayerStyle.fromId(p.readSafely(Keys.PLAYER_STYLE)),
                playerBackgroundStyle = p.readSafely(Keys.BACKGROUND_STYLE_INDEX)?.let { PlayerBackgroundStyle.entries.getOrNull(it) }
                    ?: PlayerBackgroundStyle.fromId(p.readSafely(Keys.PLAYER_BACKGROUND_STYLE)),
                wavySeekbarEnabled = p.readSafely(Keys.WAVY_SEEKBAR_ENABLED) ?: true,
                seekbarStyle = SeekbarStyle.fromId(p.readSafely(Keys.SEEKBAR_STYLE)),
                downloadLyrics = p.readSafely(Keys.DOWNLOAD_LYRICS) ?: true,
                appLanguageTag = AppLanguage.fromTag(p.readSafely(Keys.APP_LANGUAGE)).tag,
                downloadFolder = sanitizeDownloadFolderName(p.readSafely(Keys.DOWNLOAD_FOLDER)),
                downloadStructure = DownloadFolderStructure.fromId(p.readSafely(Keys.DOWNLOAD_STRUCTURE)),
                useAlbumArtistForFolders = p.readSafely(Keys.USE_ALBUM_ARTIST_FOLDERS) ?: true,
                primaryArtistOnly = p.readSafely(Keys.PRIMARY_ARTIST_ONLY) ?: true,
                hiddenHomeSections = p.readSafely(Keys.HIDDEN_HOME_SECTIONS)
                    ?.filter { id -> HomeSection.entries.any { it.id == id } }?.toSet()
                    ?: emptySet(),
                enableDynamicIslandNotch = p.readSafely(Keys.ENABLE_DYNAMIC_ISLAND_NOTCH) ?: false,
                notchTopMarginDp = (p.readSafely(Keys.NOTCH_TOP_MARGIN_DP) ?: 4).coerceIn(0, 24),
                notchCapsuleWidthDp = (p.readSafely(Keys.NOTCH_CAPSULE_WIDTH_DP) ?: 220).coerceIn(180, 320),
                notchAutoDismissSec = (p.readSafely(Keys.NOTCH_AUTO_DISMISS_SEC) ?: 4).coerceIn(2, 6),
                appIconTheme = AppIconTheme.fromId(p.readSafely(Keys.APP_ICON_THEME)),
                cacheQuotaMb = p.readSafely(Keys.CACHE_QUOTA_MB) ?: 2048L,
                minBufferMs = (p.readSafely(Keys.MIN_BUFFER_MS) ?: 15_000).coerceIn(5_000, 60_000),
                maxBufferMs = (p.readSafely(Keys.MAX_BUFFER_MS) ?: 50_000).coerceIn(15_000, 120_000),
                bufferForPlaybackMs = (p.readSafely(Keys.BUFFER_FOR_PLAYBACK_MS) ?: 2_500).coerceIn(500, 10_000),
                bufferForPlaybackAfterRebufferMs = (p.readSafely(Keys.BUFFER_FOR_PLAYBACK_AFTER_REBUFFER_MS) ?: 5_000).coerceIn(1_000, 15_000),
                audioOffloadEnabled = p.readSafely(Keys.AUDIO_OFFLOAD_ENABLED) ?: false,
                lyricsOffsetMs = (p.readSafely(Keys.LYRICS_OFFSET_MS) ?: 0L).coerceIn(-5000L, 5000L),
                miniPlayerSwipeStyle = MiniPlayerSwipeStyle.fromId(p.readSafely(Keys.MINI_PLAYER_SWIPE_STYLE)),
                preferredAudioCodec = PreferredAudioCodec.fromId(p.readSafely(Keys.PREFERRED_AUDIO_CODEC)),
                silenceThresholdDb = (p.readSafely(Keys.SILENCE_THRESHOLD_DB) ?: -42.0f).coerceIn(-60.0f, -25.0f),
                minSilenceDurationMs = (p.readSafely(Keys.MIN_SILENCE_DURATION_MS) ?: 250L).coerceIn(50L, 1500L),
                isIncognitoMode = p.readSafely(Keys.IS_INCOGNITO_MODE) ?: false,
            )
        }

    suspend fun setDynamicNowPlaying(enabled: Boolean) {
        dataStore.edit { it[Keys.DYNAMIC_NOW_PLAYING] = enabled }
    }

    suspend fun setUseCustomFont(enabled: Boolean) {
        dataStore.edit { it[Keys.USE_CUSTOM_FONT] = enabled }
    }

    suspend fun setPreferLosslessStreaming(enabled: Boolean) {
        dataStore.edit {
            it[Keys.PREFER_LOSSLESS_STREAMING] = enabled
        }
    }

    suspend fun setPreferProviderModules(enabled: Boolean) {
        dataStore.edit {
            it[Keys.PREFER_PROVIDER_MODULES] = enabled
        }
    }

    suspend fun setLosslessQuality(quality: Int) {
        dataStore.edit {
            val q = quality.takeIf { it in LOSSLESS_QUALITIES } ?: 27
            it[Keys.LOSSLESS_QUALITY] = q
        }
    }

    suspend fun setCellularStreamingQuality(quality: Int) {
        dataStore.edit {
            val q = quality.takeIf { it in LOSSLESS_QUALITIES } ?: -1
            it[Keys.CELLULAR_STREAMING_QUALITY] = q
        }
    }

    suspend fun setWifiStreamingQuality(quality: Int) {
        dataStore.edit {
            val q = quality.takeIf { it in LOSSLESS_QUALITIES } ?: 27
            it[Keys.WIFI_STREAMING_QUALITY] = q
        }
    }

    suspend fun setAutoDataSaverEnabled(enabled: Boolean) {
        dataStore.edit {
            it[Keys.AUTO_DATA_SAVER_ENABLED] = enabled
        }
    }

    suspend fun setCellularQuality(quality: Int) = setCellularStreamingQuality(quality)
    suspend fun setWifiQuality(quality: Int) = setWifiStreamingQuality(quality)
    suspend fun setAutoNetworkQualityEnabled(enabled: Boolean) = setAutoDataSaverEnabled(enabled)

    suspend fun setDownloadQuality(quality: Int) {
        dataStore.edit {
            val q = quality.takeIf { it in DOWNLOAD_QUALITIES } ?: 27
            it[Keys.DOWNLOAD_QUALITY] = q
        }
    }

    suspend fun setStudioMasterClarity(enabled: Boolean) {
        dataStore.edit { it[Keys.MUSIC_ENHANCER] = enabled }
    }

    suspend fun setLyricsUiVersion(version: LyricsUiVersion) {
        dataStore.edit { it[Keys.LYRICS_UI_VERSION] = version.id }
    }

    suspend fun setBitPerfectEnabled(enabled: Boolean) {
        dataStore.edit { it[Keys.BIT_PERFECT_ENABLED] = enabled }
    }

    suspend fun setWordByWordLyrics(enabled: Boolean) {
        dataStore.edit { it[Keys.WORD_BY_WORD_LYRICS] = enabled }
    }

    suspend fun setShowLyricsTranslation(enabled: Boolean) {
        dataStore.edit { it[Keys.SHOW_LYRICS_TRANSLATION] = enabled }
    }

    suspend fun setShowLyricsPhonetic(enabled: Boolean) {
        dataStore.edit { it[Keys.SHOW_LYRICS_PHONETIC] = enabled }
    }

    suspend fun setLyricsAnimation(animation: LyricsAnimation) {
        dataStore.edit { it[Keys.LYRICS_ANIMATION] = animation.id }
    }

    suspend fun setCrossfadeEnabled(enabled: Boolean) {
        dataStore.edit { it[Keys.CROSSFADE_ENABLED] = enabled }
    }

    suspend fun setCrossfadeSeconds(seconds: Int) {
        dataStore.edit { it[Keys.CROSSFADE_SECONDS] = seconds.coerceIn(1, 12) }
    }

    suspend fun setSkipSilenceEnabled(enabled: Boolean) {
        dataStore.edit { it[Keys.SKIP_SILENCE_ENABLED] = enabled }
    }

    suspend fun setLoudnessNormalizationEnabled(enabled: Boolean) {
        dataStore.edit { it[Keys.LOUDNESS_NORMALIZATION_ENABLED] = enabled }
    }

    suspend fun setSponsorBlockEnabled(enabled: Boolean) {
        dataStore.edit { it[Keys.SPONSOR_BLOCK_ENABLED] = enabled }
    }

    suspend fun setSkipMusicVideoIntros(enabled: Boolean) {
        dataStore.edit { it[Keys.SKIP_MUSIC_VIDEO_INTROS] = enabled }
    }

    suspend fun setDiscordRpcEnabled(enabled: Boolean) {
        dataStore.edit { it[Keys.DISCORD_RPC_ENABLED] = enabled }
    }

    suspend fun setDiscordUserToken(token: String) {
        dataStore.edit { it[Keys.DISCORD_USER_TOKEN] = token.trim() }
    }

    suspend fun setDiscordConnectedUsername(username: String) {
        dataStore.edit { it[Keys.DISCORD_CONNECTED_USERNAME] = username.trim() }
    }

    suspend fun setCrossfeedEnabled(enabled: Boolean) {
        dataStore.edit { it[Keys.CROSSFEED_ENABLED] = enabled }
    }

    suspend fun setCrossfeedLevelDb(levelDb: Float) {
        dataStore.edit { it[Keys.CROSSFEED_LEVEL_DB] = levelDb.coerceIn(3.0f, 9.5f) }
    }

    suspend fun setCrossfeedCutoffHz(cutoffHz: Float) {
        dataStore.edit { it[Keys.CROSSFEED_CUTOFF_HZ] = cutoffHz.coerceIn(300.0f, 1200.0f) }
    }

    suspend fun setShakeToSkipEnabled(enabled: Boolean) {
        dataStore.edit { it[Keys.SHAKE_TO_SKIP_ENABLED] = enabled }
    }

    suspend fun setPlayerCoverStyle(style: PlayerCoverStyle) {
        dataStore.edit { it[Keys.PLAYER_COVER_STYLE] = style.id }
    }

    suspend fun setPlayerStyle(style: PlayerStyle) {
        dataStore.edit { it[Keys.PLAYER_STYLE] = style.id }
    }

    suspend fun setAppIconTheme(icon: AppIconTheme) {
        dataStore.edit { it[Keys.APP_ICON_THEME] = icon.id }
    }

    suspend fun setBufferTuning(minBufferMs: Int, maxBufferMs: Int, bufferForPlaybackMs: Int, bufferForPlaybackAfterRebufferMs: Int) {
        dataStore.edit {
            it[Keys.MIN_BUFFER_MS] = minBufferMs.coerceIn(5_000, 60_000)
            it[Keys.MAX_BUFFER_MS] = maxBufferMs.coerceIn(15_000, 120_000)
            it[Keys.BUFFER_FOR_PLAYBACK_MS] = bufferForPlaybackMs.coerceIn(500, 10_000)
            it[Keys.BUFFER_FOR_PLAYBACK_AFTER_REBUFFER_MS] = bufferForPlaybackAfterRebufferMs.coerceIn(1_000, 15_000)
        }
    }

    suspend fun setEnableDynamicIslandNotch(enabled: Boolean) {
        dataStore.edit { it[Keys.ENABLE_DYNAMIC_ISLAND_NOTCH] = enabled }
    }

    suspend fun setDynamicNotchSettings(enabled: Boolean, topMarginDp: Int, widthDp: Int, dismissSec: Int) {
        dataStore.edit {
            it[Keys.ENABLE_DYNAMIC_ISLAND_NOTCH] = enabled
            it[Keys.NOTCH_TOP_MARGIN_DP] = topMarginDp.coerceIn(0, 24)
            it[Keys.NOTCH_CAPSULE_WIDTH_DP] = widthDp.coerceIn(180, 320)
            it[Keys.NOTCH_AUTO_DISMISS_SEC] = dismissSec.coerceIn(2, 6)
        }
    }

    suspend fun setCacheQuotaMb(quotaMb: Long) {
        dataStore.edit { it[Keys.CACHE_QUOTA_MB] = quotaMb }
    }

    suspend fun setLyricsOffsetMs(offsetMs: Long) {
        dataStore.edit { it[Keys.LYRICS_OFFSET_MS] = offsetMs.coerceIn(-5000L, 5000L) }
    }

    suspend fun setMiniPlayerSwipeStyle(style: MiniPlayerSwipeStyle) {
        dataStore.edit { it[Keys.MINI_PLAYER_SWIPE_STYLE] = style.id }
    }

    suspend fun setPreferredAudioCodec(codec: PreferredAudioCodec) {
        dataStore.edit { it[Keys.PREFERRED_AUDIO_CODEC] = codec.id }
    }

    suspend fun setSilenceSettings(thresholdDb: Float, minDurationMs: Long) {
        dataStore.edit {
            it[Keys.SILENCE_THRESHOLD_DB] = thresholdDb.coerceIn(-60.0f, -25.0f)
            it[Keys.MIN_SILENCE_DURATION_MS] = minDurationMs.coerceIn(50L, 1500L)
        }
    }

    suspend fun setIncognitoMode(enabled: Boolean) {
        dataStore.edit { it[Keys.IS_INCOGNITO_MODE] = enabled }
    }

    suspend fun setAudioOffloadEnabled(enabled: Boolean) {
        dataStore.edit { it[Keys.AUDIO_OFFLOAD_ENABLED] = enabled }
    }

    suspend fun setPlayerBackgroundStyle(style: PlayerBackgroundStyle) {
        dataStore.edit {
            it[Keys.PLAYER_BACKGROUND_STYLE] = style.id
            it[Keys.BACKGROUND_STYLE_INDEX] = PlayerBackgroundStyle.entries.indexOf(style)
        }
    }

    suspend fun setBackgroundStyleIndex(index: Int) {
        dataStore.edit {
            val idx = index.coerceIn(PlayerBackgroundStyle.entries.indices)
            it[Keys.BACKGROUND_STYLE_INDEX] = idx
            PlayerBackgroundStyle.entries.getOrNull(idx)?.let { style ->
                it[Keys.PLAYER_BACKGROUND_STYLE] = style.id
            }
        }
    }

    suspend fun setWavySeekbarEnabled(enabled: Boolean) {
        dataStore.edit { it[Keys.WAVY_SEEKBAR_ENABLED] = enabled }
    }

    suspend fun setSeekbarStyle(style: SeekbarStyle) {
        dataStore.edit { it[Keys.SEEKBAR_STYLE] = style.id }
    }

    suspend fun setDownloadLyrics(enabled: Boolean) {
        dataStore.edit { it[Keys.DOWNLOAD_LYRICS] = enabled }
    }

    suspend fun setDownloadFolder(name: String) {
        dataStore.edit { it[Keys.DOWNLOAD_FOLDER] = sanitizeDownloadFolderName(name) }
    }

    suspend fun setDownloadStructure(structure: DownloadFolderStructure) {
        dataStore.edit { it[Keys.DOWNLOAD_STRUCTURE] = structure.id }
    }

    suspend fun setUseAlbumArtistForFolders(enabled: Boolean) {
        dataStore.edit { it[Keys.USE_ALBUM_ARTIST_FOLDERS] = enabled }
    }

    suspend fun setPrimaryArtistOnly(enabled: Boolean) {
        dataStore.edit { it[Keys.PRIMARY_ARTIST_ONLY] = enabled }
    }

    suspend fun setHomeSectionVisible(id: String, visible: Boolean) {
        if (HomeSection.entries.none { it.id == id }) return
        dataStore.edit { prefs ->
            val current = prefs.readSafely(Keys.HIDDEN_HOME_SECTIONS) ?: emptySet()
            prefs[Keys.HIDDEN_HOME_SECTIONS] = if (visible) current - id else current + id
        }
    }

    suspend fun showAllHomeSections() {
        dataStore.edit { it.remove(Keys.HIDDEN_HOME_SECTIONS) }
    }

    /**
     * Synchronous mirror for `attachBaseContext`, which runs before DataStore
     * can deliver a value. commit() on IO: immune to process-kill races and
     * never blocks the UI thread.
     */
    fun readLanguageTagSync(): String =
        runCatching {
            appContext.getSharedPreferences(AppLanguage.SYNC_FILE, Context.MODE_PRIVATE)
                .getString(AppLanguage.SYNC_KEY, AppLanguage.SYSTEM.tag)
        }.getOrNull().let { AppLanguage.fromTag(it).tag }

    suspend fun setAppLanguage(language: AppLanguage) {
        dataStore.edit { it[Keys.APP_LANGUAGE] = language.tag }
        withContext(Dispatchers.IO) {
            runCatching {
                appContext.getSharedPreferences(AppLanguage.SYNC_FILE, Context.MODE_PRIVATE)
                    .edit()
                    .putString(AppLanguage.SYNC_KEY, language.tag)
                    .commit()
            }
        }
    }

    suspend fun toggleFriendPinned(username: String) {
        dataStore.edit { prefs ->
            val current = prefs.readSafely(Keys.PINNED_FRIENDS) ?: emptySet()
            prefs[Keys.PINNED_FRIENDS] = if (username in current) current - username else current + username
        }
    }

    private companion object {
        val LOSSLESS_QUALITIES = setOf(-1, 5, 6, 7, 27)
        val DOWNLOAD_QUALITIES = setOf(-1, 5, 6, 7, 27)
    }
}
