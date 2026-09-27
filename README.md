<div align="center">

# LASTWAVEX

**Next-Generation Android Music Player, Bit-Perfect C++ Audio Engine & Universal Scrobbler**

<p align="center">
  <a href="https://kotlinlang.org">
    <img src="https://img.shields.io/badge/Kotlin-2.0.21-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white" alt="Kotlin 2.0.21" />
  </a>
  <a href="https://developer.android.com/jetpack/compose">
    <img src="https://img.shields.io/badge/Jetpack%20Compose-1.7.5-4285F4?style=for-the-badge&logo=jetpackcompose&logoColor=white" alt="Jetpack Compose 1.7.5" />
  </a>
  <a href="https://developer.android.com/media/media3">
    <img src="https://img.shields.io/badge/AndroidX%20Media3-1.4.1-3DDC84?style=for-the-badge&logo=android&logoColor=white" alt="Media3 1.4.1" />
  </a>
  <a href="#">
    <img src="https://img.shields.io/badge/C%2B%2B-Native%20Float32%20DSP-00599C?style=for-the-badge&logo=cplusplus&logoColor=white" alt="C++ Native Audio Engine" />
  </a>
  <a href="#">
    <img src="https://img.shields.io/badge/Unit%20Tests-28%2F28%20Passing-brightgreen?style=for-the-badge&logo=githubactions&logoColor=white" alt="Unit Tests 28/28 Passing" />
  </a>
  <a href="https://developer.android.com/about/versions/oreo">
    <img src="https://img.shields.io/badge/Android-8.0%2B%20(API%2026%2B)-3DDC84?style=for-the-badge&logo=android&logoColor=white" alt="Android 8.0+" />
  </a>
  <a href="LICENSE">
    <img src="https://img.shields.io/badge/License-GPLv3%20FOSS-blue?style=for-the-badge" alt="License GPLv3" />
  </a>
</p>

</div>

---

## Executive Product Overview

**LASTWAVEX** is an open-source, offline-first, bit-perfect music player and native audio engine designed for Android power listeners. Engineered from the ground up to unify high-fidelity audio reproduction, community intelligence, and kinetic visual design, LASTWAVEX pairs a native **C++ Float32 DSP engine** with **AndroidX Media3 ExoPlayer** and a **Material 3 Expressive + iOS LiquidGlass** Compose interface.

Whether streaming lossless studio master FLAC, listening offline, scrobbling to Last.fm, or outputting raw audio directly to external USB DACs without OS resampling, LASTWAVEX delivers a uncompromising, privacy-respecting listening experience free of commercial ad units, subscription paywalls, or corporate telemetry.

---

## What Makes LASTWAVEX Different

| Core Pillar | Technical Distinction & Value Proposition |
| :--- | :--- |
| **Native C++ Float32 DSP Engine** | Direct 32-bit floating-point software audio pipeline implementing a 15-band parametric equalizer, bass boost, and dynamic loudness normalization (+2 dB leveling). |
| **Bit-Perfect USB DAC Passthrough** | Completely bypasses Android OS resamplers (`AudioTrack`/SLES 44.1 kHz forced resampling) to route raw studio master FLAC streams directly to external hardware USB DACs. |
| **Community Intelligence Triad** | Integrated automatic non-music intro/outro skipping via **SponsorBlock**, Shazam-style in-app audio recognition via **AudD**, and synchronized lyrics via **LRCLIB**. |
| **Material 3 Expressive & LiquidGlass UI** | Unified design language blending Google's Material 3 Expressive tokens with Apple's iOS LiquidGlass translucent refraction, 9 player layouts, and 8 background backdrops. |
| **Zero-Clutter Privacy & FOSS** | 100% free and open-source software (GPLv3). No advertisements, no video shorts, no tracking analytics, and complete local data control. |

---

## Exhaustive Implemented Features Matrix

### 🎧 Audio Engine & Native DSP
- **15-Band Parametric Equalizer**: Precision graphic and parametric frequency adjustment across 15 bands with preset curves and custom saving.
- **Bass Boost & Tone Effects**: Hardware-accelerated and software C++ DSP bass enhancement for deep low-end response.
- **Loudness Normalization (+2 dB Leveling)**: Normalizes volume across tracks using `LoudnessEnhancer` to prevent sudden volume spikes between albums or sources.
- **Bit-Perfect Passthrough Mode**: Direct-to-hardware audio routing bypassing all software DSP, volume ducking, and system sample rate conversion for bit-exact reproduction.
- **Playback Speed & Tempo Slider**: Real-time pitch-preserved time-stretching from `0.5x` to `2.0x` with preset quick chips.
- **Skip Silence Engine**: Automatically detects and skips silent lead-ins and trailing gaps during audio playback.
- **Equal-Power Crossfade**: Configurable 1s–12s smooth dual-player crossfade blending the end of a track seamlessly into the next.

### 🤖 Smart Automation & Gestures
- **Sleep Timer with 30s Gentle Fade-Out**: Minute-based and track-based sleep timer that smoothly interpolates volume from `1.0` down to `0.0` during the final 30 seconds before pausing.
- **Shake-to-Skip Accelerometer Motion Gesture**: Hardware accelerometer `ShakeDetector` with a 13.0 m/s² threshold and 1000ms cooldown timer to skip tracks by shaking your device.
- **Data Saver & Per-Network Quality**: ConnectivityManager-aware quality resolution automatically switching between Cellular Data Saver (160k) and Wi-Fi Hi-Res Lossless (24-bit / 192 kHz FLAC).
- **SponsorBlock Auto-Seek**: Fetches and caches SponsorBlock skip segments to automatically jump non-music video intros, outros, chatter, and promotional commentary.

### 🎙️ Lyrics Engine & Quote Image Generator
- **Synced Karaoke Lyrics**: Real-time millisecond-synchronized lyrics powered by LRCLIB with fluid motion scrolling.
- **Word-by-Word Tracking**: Syllable-level karaoke highlighting with fluid spring scaling and focal tracking.
- **Phonetic Pronunciation Toggle**: One-tap toggle displaying Romanized/Pinyin phonetic guide above Japanese, Chinese, or non-Latin lyrics.
- **Line Translation Toggle**: One-tap toggle showing line-by-line translated lyrics underneath original text.
- **Lyric Card & Quote Generator**: Converts selected lyrics lines into 1080x1350 quote images across 3 card styles (*Minimalist Dark*, *Dynamic Gradient*, *Glassmorphic*) with 1-tap FileProvider sharing and gallery PNG export.

### 🎨 Visuals, Layouts & Theming
- **Rotating Vinyl Record Mode**: Circular vinyl record artwork view with realistic grooved rings and 33⅓ RPM spin physics that smoothly decelerates and holds angle when paused.
- **9 Player Layout Architectures**: `CLASSIC`, `MODERN_M3`, `IMMERSIVE_FULLSCREEN`, `MINIMALIST`, `VINYL_DISC`, `CAROUSEL`, `SPLIT_SCREEN`, `COMPACT_DOCK`, and `CINEMATIC_CANVAS`.
- **8 Background Canvas Backdrops**: `HDR_VIVID`, `FLUID_GRADIENT`, `DYNAMIC_HARMONY`, `AMBIENT_GLOW`, `DYNAMIC_MONET`, `AMOLED_BLACK`, `BLURRED_GLASS`, and `PRISM_SPECTRUM`.
- **Material 3 Home Screen Widget**: 3x2 rounded widget displaying artwork, track metadata, and real-time transport controls (`Previous`, `Play/Pause`, `Next`) connected directly to `MusicPlaybackService`.

### 🔍 Search, Song Recognizer & Library
- **In-App Song Recognizer (Shazam-Style)**: Records 5-second PCM audio samples via `AudioRecord`, generates WAV signatures, and identifies tracks via AudD API with an animated pulsing radar dialog.
- **YouTube Music & Last.fm Search**: Multi-tab search for tracks, artists, albums, playlists, and Last.fm user profiles with instant autocomplete.
- **Custom Playlist Metadata & Cover Picker**: Full playlist editor to rename title, edit description, and pick custom cover artwork from device storage (`GetContent`).
- **CSV & M3U Playlist Importer / Exporter**: Import and export custom playlists to UTF-8 CSV, TSV, M3U, and M3U8 formats.

---

## Technical Foundation

LASTWAVEX is architected following Google's modern Android development standards and clean architecture principles:

```
app/
 ├── data/
 │    ├── ai/          # AI Assistant & Recommendation Engine
 │    ├── artwork/     # Palette Color Extraction & Coil Cache
 │    ├── discord/     # Discord Local IPC / WebSocket Rich Presence
 │    ├── download/    # Offline File Downloader & AudioTagWriter
 │    ├── local/       # Room DB, DataStore Preferences & Settings
 │    ├── lossless/    # Lossless Music API & Stream Resolver
 │    ├── playlist/    # Saved Playlists, M3U/CSV Importer & Public Mirror
 │    ├── recognition/ # AudioRecord PCM 44.1kHz Song Recognizer
 │    └── search/      # YouTube Music & Last.fm Search Repositories
 ├── playback/
 │    ├── AudioEffectsEngine.kt  # Android Platform AudioFX & LoudnessEnhancer
 │    ├── MusicPlaybackService.kt# Foreground MediaSession & Notification
 │    ├── MusicPlayer.kt         # ExoPlayer Coordinator, Crossfade & Sleep Timer
 │    ├── NativeAudioEngine.kt   # JNI Bridge to C++ Float32 DSP Engine
 │    ├── ShakeDetector.kt       # SensorManager Accelerometer Motion Detector
 │    └── cast/                  # Google Cast Integration
 └── ui/
      ├── common/      # Shared Expressive & LiquidGlass Components
      ├── player/      # PlayerHost, LyricCardSheet, PlayerBackgroundBackdrop
      ├── settings/    # SettingsScreen, DiagnosticsScreen, AiAssistantSettings
      └── widget/      # Material 3 Home Screen Music Widget
```

- **Core Language**: 100% Idiomatic Kotlin 2.0.21
- **UI Framework**: Jetpack Compose 1.7.5 with Material 3 Expressive & LiquidGlass Tokens
- **Audio Pipeline**: AndroidX Media3 ExoPlayer 1.4.1, MediaSessionCompat, C++ Native NDK DSP
- **Persistence & DI**: Room 2.6.1, Jetpack DataStore Preferences, Dagger Hilt 2.51.1
- **Networking**: OkHttp 4.12.0, Retrofit 2.11.0, Kotlinx Serialization 1.6.3
- **Unit Testing**: JUnit 4 & Kotlin Coroutines Test (**28 passed, 0 skipped, 0 failed**)

---

## Vision & Philosophy

LASTWAVEX exists to prove that Android music software can be fast, beautiful, and completely under the user's control:

1. **Audio Quality Above All**: No lossy re-encoding or forced 44.1 kHz resampling. When bit-perfect is enabled, audio streams pass through uncorrupted.
2. **User Sovereignty**: Your playlists, scrobbles, downloaded lyrics, and settings belong to you on your device storage.
3. **Zero Telemetry**: No background tracking SDKs, no ad network SDKs, and no remote behavioral analytics.
4. **Open Source Permanence**: Free and open-source under GPLv3, ensuring community access and auditability forever.

---

## Features We Intentionally Do Not Pursue

To maintain peak audio performance, responsiveness, and battery efficiency, LASTWAVEX explicitly excludes:

- ❌ **Commercial Subscription Locks or Freemium Tiers**: Every feature is unlocked by default.
- ❌ **In-App Advertisements**: Zero ad banners, video popups, or commercial tracking scripts.
- ❌ **Podcasts & Video Short Feeds**: Dedicated exclusively to music listening.
- ❌ **Surveillance Telemetry**: Zero background telemetry or behavioral tracking.

---

## System Requirements & Installation

- **Minimum Version**: Android 8.0+ (API Level 26)
- **Target Version**: Android 15 (API Level 35)
- **Permissions**: `INTERNET`, `RECORD_AUDIO` (optional, for Song Recognizer), `POST_NOTIFICATIONS`, `FOREGROUND_SERVICE_MEDIA_PLAYBACK`.

### Sideloading Installation
1. Download `app-debug.apk` or `app-release.apk` from official **[Releases](https://github.com/specttre404/LastWaveX/releases)**.
2. Sideload the APK on your Android device.
3. Optional: Connect Last.fm in **Settings → Integrations** to enable scrobbling and global charts.

---

## Building from Source

```bash
# Clone the repository
git clone https://github.com/specttre404/LastWaveX.git
cd LastWaveX

# Run unit tests (28/28 tests passing)
./gradlew app:testDebugUnitTest

# Compile Kotlin sources
./gradlew app:compileDebugKotlin

# Build debug APK
./gradlew app:assembleDebug
```

Generated Build Artifacts:
- **Debug APK**: `app/build/outputs/apk/debug/app-debug.apk`

---

## Current Release Information

- **Release Version**: `v1.1`
- **Unit Test Status**: **28 Passed, 0 Skipped, 0 Failed**
- **Artifact Path**: `app/build/outputs/apk/debug/app-debug.apk`

---

## Credits & Upstream Acknowledgments

LASTWAVEX is built on open-source Android audio software and community APIs:

- **[LastWave](https://github.com/Clash-Projects/LastWave-native)** — Original open-source reference project.
- **[LRCLIB](https://lrclib.net)** — Synced lyrics database API.
- **[AudD](https://audd.io)** — Audio recognition API.
- **[SponsorBlock](https://sponsor.ajay.app)** — Community segment skip database.
- **[Media3 ExoPlayer](https://developer.android.com/media/media3)** — High-fidelity Android media framework.

---

## Disclaimer

> [!NOTE]
> **Educational & Non-Commercial Notice**
>
> LASTWAVEX is an open-source, non-commercial application developed for research, education, and personal use to demonstrate modern Android Media3 architecture and Jetpack Compose design patterns.
>
> LASTWAVEX is an independent community project and is **not affiliated with, endorsed, or sponsored by Google LLC, YouTube, YouTube Music, Last.fm, or AudD**. All media content is streamed directly from public web endpoints under fair research and personal use. All trademarks belong to their respective owners.

---

## License

This project is licensed under the **GNU General Public License v3.0 (GPLv3)** — see the [`LICENSE`](LICENSE) file for details.

<div align="center">
  <p><b>LASTWAVEX</b> — Free &amp; Open Source Software for Android</p>
</div>
