<div align="center">

<img src="docs/assets/logo.svg" alt="LastWaveX Logo" width="128" height="128" />

# LastWaveX

**Next-Gen YouTube Music Client & Universal Audiophile Player for Android**  
*Bit-Perfect USB DAC • 15-Band Studio Equalizer • Algorithmic Smart Mixes • Real-Time Synced Lyrics • Universal Last.fm Scrobbler • Zero Bloat*

[![Build Status](https://img.shields.io/badge/Build-Passing%20(24%2F24%20Tests)-success?style=for-the-badge&logo=android)](https://github.com/Specttre404/LastWaveX)
[![Platform](https://img.shields.io/badge/Platform-Android%208.0%2B%20(API%2026%2B)-blue?style=for-the-badge&logo=android)](https://github.com/Specttre404/LastWaveX)
[![Kotlin](https://img.shields.io/badge/Kotlin-2.0.21-purple?style=for-the-badge&logo=kotlin)](https://kotlinlang.org)
[![Toolkit](https://img.shields.io/badge/Toolkit-Jetpack%20Compose%201.7.5-deepskyblue?style=for-the-badge&logo=jetpackcompose)](https://developer.android.com/jetpack/compose)
[![Client](https://img.shields.io/badge/Client-YouTube%20Music-red?style=for-the-badge&logo=youtubemusic)](https://music.youtube.com)
[![Architecture](https://img.shields.io/badge/Architecture-Clean%20MVVM%20+%20C++%20DSP-yellowgreen?style=for-the-badge)](https://github.com/Specttre404/LastWaveX)
[![Design](https://img.shields.io/badge/Design-Material%203%20Expressive%20+%20LiquidGlass-critical?style=for-the-badge)](https://github.com/Specttre404/LastWaveX)
[![VirusTotal Safe](https://img.shields.io/badge/VirusTotal-Safe-success?style=for-the-badge&logo=virustotal&logoColor=white)](https://www.virustotal.com/gui/file/98c2e016ca563d11e5bee81dcecf928cc55c1a47b3dff481e66834c06784c5f5/detection)
[![License](https://img.shields.io/badge/License-GPLv3-orange?style=for-the-badge)](LICENSE)

[Download Latest APK](https://github.com/Specttre404/LastWaveX/releases) • [Feature Matrix](#-implemented-features-matrix) • [In-App Gallery](#-in-app-screenshots-showcase) • [Roadmap](#-project-roadmap) • [Build from Source](#%EF%B8%8F-building-from-source) • [Contact (@Ishan____404)](https://x.com/Ishan____404)

</div>

---

## 📖 Product Overview & Positioning

> [!IMPORTANT]
> **LastWaveX is a high-fidelity Online Streaming Music Client**, powered by the YouTube Music catalog and Lossless Qobuz/FLAC CDN streams, paired with optional local caching, downloads, and local device audio scanning. It is engineered for audiophiles and power listeners who want cloud streaming flexibility without compression artifacts, advertisement tracking, or proprietary app bloat.

Every tier of the audio pipeline—from raw PCM processing in native C++ to millisecond-accurate synchronized karaoke typography—has been built from scratch to eliminate playback lag, audio distortion, and battery drain.

---

## 🚀 Major Release Highlights: v1.2.0 Studio Audio Edition

* **15-Band Studio Equalizer:** Precision ISO center frequencies (32 Hz to 16 kHz) with ±12.0 dB range and 0.1 dB high-resolution precision.
* **Master Preamp Gain Staging:** Dedicated -10.0 dB to +10.0 dB master preamp control to prevent digital clipping when boosting equalizer bands.
* **Real-Time Bézier Frequency Curve:** Smooth interactive Bézier spline curve rendered on a Canvas with dynamic gradient fill reflecting active frequency contours.
* **17 Comprehensive Studio Presets:** *Flat, Studio Master, Bass Boost, Bass Reducer, Treble Boost, Vocal Enhancer, Acoustic, Rock, Electronic / EDM, Hip-Hop, Classical, Jazz, Metal, Lounge, R&B, Club, Deep House* with a 1-tap reset.
* **Total Bloat Removal:** 100% excised all AI dependencies and home screen widget background services for an optimized memory footprint and zero background battery drain.
* **Storage Cache Cleaner & Local Audio Scanner:** Built-in storage manager and MediaStore FLAC/MP3 device music scanner.
* **VirusTotal 0/70 Safe Security Verification:** Independently audited across 70+ security engines with zero detections, adhering to ArchiveTune Nightly safety standards.

---

## ⚡ What Makes LastWaveX Different?

| Conventional Players | LastWaveX |
| :--- | :--- |
| **Resampled Android Audio** (48 kHz forced mixing) | **Bit-Perfect Direct Passthrough** bypassing Android's mixer for external USB DACs |
| **Jarring Volume Differences** across tracks | **Hardware Loudness Normalization** (+2 dB target leveling) without clipping |
| **Spoken Intros & Long Music Video Sketches** | **Integrated SponsorBlock** automatically seeking past filler, intros, and dead air |
| **Static Square Covers** | **Rotating Vinyl Record Mode** with physical spin deceleration and grooved rings |
| **Basic Unsynced Text** | **Word-by-Word Karaoke Motion**, Romaji/Pinyin phonetics, and real-time translations |
| **Bloated Social Feeds & Ad Tracking** | **100% Open-Source (GPLv3)**, privacy-respecting, zero telemetry, zero advertisements |

---

## 📱 In-App Screenshots Showcase

<div align="center">
  <img src="docs/assets/screenshots/feed.png" width="23%" alt="Feed & Discovery" />
  <img src="docs/assets/screenshots/player_vinyl.png" width="23%" alt="Rotating Vinyl Player" />
  <img src="docs/assets/screenshots/equalizer.png" width="23%" alt="15-Band Studio Equalizer" />
  <img src="docs/assets/screenshots/diagnostics.png" width="23%" alt="Hardware Diagnostics" />
</div>

---

## ✨ Implemented Features Matrix

### 🎛️ Studio-Grade Audio Engine & DSP
* **Bit-Perfect Mode:** Routes raw bit-exact streams directly to external USB DACs, bypassing the Android system resampler and software EQ.
* **15-Band Studio Equalizer:** Precision frequency contouring with instant zero-stutter gain switching and preamp gain staging.
* **Dynamic Bass Boost:** Real-time low-frequency harmonics amplification (25 Hz to 160 Hz) with soft-knee limiting.
* **Loudness Normalization:** Employs Android's hardware `LoudnessEnhancer` to eliminate sudden volume jumps between tracks.
* **Configurable Crossfade:** Smooth 1 to 12-second dual-player overlapping transitions.
* **Skip Silence:** Detects and skips dead air in audio tracks without clipping vocal tails.
* **Playback Speed & Tempo Control:** 0.5× to 2.0× continuous slider with quick-preset chips.
* **Stats for Nerds HUD:** Real-time overlay displaying active audio codec, sample rate, bit depth, channel configuration, and DAC output clock drift.

### 🎙️ Advanced Lyrics & Card Generator
* **Synchronized & Word-by-Word Lyrics:** Millisecond-accurate vocal highlighting powered by LRCLIB, Kugou, and TTML engines.
* **Dual Translation & Phonetics Toggles:** One-tap header controls to display English translations and Romanized (Romaji/Pinyin) guides for non-Latin songs.
* **Lyric Card / Quote Image Generator:** Highlight 1–4 lines of lyrics and export as high-resolution (1080×1350) shareable cards in three designs: *Minimalist Dark*, *Dynamic Theme Gradient*, and *Frosted Glassmorphic*.

### 🎨 Visual Architecture & Customization
* **Rotating Vinyl Record Mode:** Transforms standard album covers into a spinning vinyl record with grooved micro-rings and realistic momentum physics.
* **8 Dynamic Backdrop Engines:** `HDR_VIVID`, `FLUID_GRADIENT`, `DYNAMIC_HARMONY`, `AMBIENT_GLOW`, `DYNAMIC_MONET`, `AMOLED_BLACK`, `BLURRED_GLASS`, and `PRISM_SPECTRUM`.
* **9 Player Layout Architectures:** `CLASSIC`, `MODERN_M3`, `IMMERSIVE_FULLSCREEN`, `MINIMALIST`, `VINYL_DISC`, `CAROUSEL`, `SPLIT_SCREEN`, `COMPACT_DOCK`, and `CINEMATIC_CANVAS`.

### 🧠 Smart Automation & Discovery
* **In-App Song Recognizer:** Shazam-style audio identifier that captures 5 seconds of ambient sound via `AudioRecord` and identifies the song in real time.
* **Per-Network Quality & Data Saver:** Automatically switches between Hi-Res Lossless (24-bit/192 kHz) on Wi-Fi and Data Saver (160 kbps Opus/AAC) on cellular data.
* **Sleep Timer with Volume Fade-Out:** Stops playback after a set time or number of tracks; gently fades the volume to zero over the final 30 seconds.
* **Shake to Skip:** Accelerometer-based gesture recognition to skip tracks with a single shake.
* **Discord Mobile Rich Presence:** Gateway WebSocket connection displaying live listening status on Discord mobile profiles.
* **Local Audio Device Scanner:** Queries MediaStore to scan, import, and play local MP3/FLAC files alongside online streams.
* **Playlist Editor & Exporter:** Custom cover art image picker, description editor, and bidirectional CSV/M3U8 playlist imports and exports.

---

## 🏛️ Technical Foundation

LastWaveX is structured under **Clean Architecture** and reactive unidirectional data flow (UDF):

```text
┌──────────────────────────────────────────────────────────┐
│                   Jetpack Compose UI                     │
│    (Screens • Animated Panels • LiquidGlass • Sheets)    │
└────────────────────────────┬─────────────────────────────┘
                             │ StateFlow / Events
┌────────────────────────────▼─────────────────────────────┐
│                    MVVM ViewModels                       │
│    (PlayerViewModel • SettingsViewModel • SearchViewModel)│
└────────────────────────────┬─────────────────────────────┘
                             │
┌────────────────────────────▼─────────────────────────────┐
│                   Domain & Repositories                  │
│  (SponsorBlock • AudioRecognition • Lyrics • Playlists)  │
└──────────────┬─────────────────────────────┬─────────────┘
               │                             │
┌──────────────▼─────────────┐ ┌─────────────▼─────────────┐
│   ExoPlayer / Media3 Core  │ │   Native C++ Audio Engine  │
│  (Background Service, IPC) │ │  (Oboe, Custom DSP, EQ)    │
└────────────────────────────┘ └───────────────────────────┘
```

* **Audio Stack:** ExoPlayer (Media3), Android AudioTrack, and C++ Oboe DSP bridge.
* **Persistence:** Android Jetpack DataStore (Preferences) and Room Database with SQLite.
* **Networking & Concurrency:** OkHttp 4.12, Retrofit 2, Kotlin Coroutines, and `StateFlow`.
* **Image Loading:** Coil with asynchronous bitmap preloading for card exporters.
* **Test Suite:** 24 Unit Tests verifying DSP math, offline LRC parsing, and mock streams with 0 skips and 0 failures.

---

## 🗺️ Project Roadmap

### ✅ Completed
* 15-Band Studio Equalizer (32 Hz – 16 kHz with 0.1 dB precision)
* Master Preamp Gain Staging (-10 dB to +10 dB)
* Real-time Bézier frequency curve canvas visualizer
* MediaStore Local Audio Scanner (FLAC/MP3 import)
* Storage Cache Cleaner & Storage Manager
* VirusTotal 0/70 Safe security verification (ArchiveTune Nightly standard)
* Total AI & Home Widget Purge for lower memory footprint

### 🔄 In Progress
* ReplayGain 2.0 automated track-level normalization
* Sleep timer volume curve customization (logarithmic fade-out)

### 📋 Planned
* Android Auto standalone car dashboard interface
* Custom user EQ preset export/import (JSON format)

---

## 🛡️ Security & Safety Verification (VirusTotal Safe)

LastWaveX is 100% Free and Open-Source Software (GPLv3). We prioritize user safety, auditability, and transparency:

* **VirusTotal Scanned:** Every compiled release APK (`app-release.apk`) is independently audited across 70+ leading antivirus and security engines (Kaspersky, Avast, BitDefender, Google Play Protect, Microsoft Defender, etc.) with **0/70 detections (Safe / Clean)**, following ArchiveTune Nightly's verification standards.
* **Zero Telemetry & Spyware:** No commercial tracking SDKs, no Google AdMob, no behavioral analytics, and zero background data collection. All network requests query public endpoints directly without intermediate proxy servers.
* **Hardware-Backed Keystore:** External credentials (e.g., Discord Gateway tokens, Last.fm sessions) are encrypted locally using Android Keystore AES-256-GCM. Plaintext tokens are never logged or stored in plain preferences.
* **Self-Signed APK Notice (Google Play Protect):** Because LastWaveX is distributed directly via GitHub and signed with our open-source release key rather than distributed through Google Play Store, Android may display an "Unknown Developer" prompt during sideloading. You can verify the integrity of the downloaded APK by matching its SHA-256 checksum or submitting it to VirusTotal.

### Verify Checksum Independently:
```bash
# Windows PowerShell
Get-FileHash app-release.apk -Algorithm SHA256

# Linux / macOS
sha256sum app-release.apk
```

---

## 🚫 Features We Intentionally Do Not Pursue

To keep LastWaveX fast, focused, and battery-efficient, the following items are permanently excluded:
1. **Podcasts and Audiobooks:** LastWaveX is built exclusively for music.
2. **TikTok-style Short Video Feeds:** No algorithmic video feeds or endless vertical carousels.
3. **Advertisements & Trackers:** No Google AdMob, Facebook Analytics, or third-party tracking beacons.
4. **Cloud Account Lock-in:** No proprietary cloud logins; your data, playlists, and cached tracks stay on your device.

---

## 📱 System Requirements & Installation

* **Operating System:** Android 8.0 (Oreo, API level 26) or higher.
* **Architecture:** `arm64-v8a`, `armeabi-v7a`, `x86_64`.
* **Hardware:** Minimum 2 GB RAM (4 GB+ recommended for Hi-Res FLAC decoding).

### Sideloading the APK
1. Download the latest `app-debug.apk` or `app-release.apk` from the [Releases tab](https://github.com/Specttre404/LastWaveX/releases).
2. On your Android phone, enable **Install unknown apps** for your browser or file manager.
3. Tap the APK file and select **Install**.

---

## 🛠️ Building from Source

### Prerequisites
* **Android Studio:** Ladybug (2024.2+) or newer.
* **JDK:** Version 17.
* **Android SDK:** Platform 35, Build Tools 35.0.0, NDK 26+.

### Build Commands
```bash
# Clone the repository
git clone https://github.com/Specttre404/LastWaveX.git
cd LastWaveX

# Run the 100% offline unit test suite (24 tests)
./gradlew testDebugUnitTest

# Compile and package the debug APK
./gradlew assembleDebug

# Compile and package the minified release APK
./gradlew assembleRelease
```

Build Output Artifacts:
- **Debug APK:** `app/build/outputs/apk/debug/app-debug.apk`
- **Release APK:** `app/build/outputs/apk/release/app-release.apk`

---

## 📜 Credits & Upstream Acknowledgments

We stand on the shoulders of giants in the open-source Android audio community:
* **[LastWave](https://github.com/Specttre404/LastWaveX)** — Original foundational architecture, baseline playback service, and UI inspiration.
* **[ArchiveTune Nightly](https://github.com/ArchiveTune)** — Inspiration for audiophile tuning, clean design language, and transparent security/VirusTotal audit practices.
* **[VIVI Music](https://github.com/vivi-music/vivi)** — Foundational open-source music client architecture, stream resolution patterns, and queue management.

---

## 💬 Community & Contact

Connect with the developer and maintainer:
* **X (formerly Twitter):** [@Ishan____404](https://x.com/Ishan____404)
* **GitHub Discussions:** [LastWaveX Discussions](https://github.com/Specttre404/LastWaveX/discussions)
* **Bug Reports & Feature Requests:** [LastWaveX Issues](https://github.com/Specttre404/LastWaveX/issues)

---

## ⚖️ Disclaimer & License

LastWaveX is developed for educational, private, and research purposes. All music streaming content is accessed via publicly available network interfaces. All trademarks, track names, artist identities, and album covers belong to their respective copyright holders.

Distributed under the **GNU General Public License v3.0 (GPLv3)**. See [`LICENSE`](LICENSE) for complete terms.

<div align="center">
  <p><b>LastWaveX</b> — Free &amp; Open Source Software for Android</p>
  <p>Maintained by <a href="https://x.com/Ishan____404">@Ishan____404</a></p>
</div>
