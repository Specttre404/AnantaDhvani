<div align="center">

<img src="docs/assets/logo.svg" alt="Ananta Dhvani Logo" width="128" height="128" />

# Ananta Dhvani (अनन्त-ध्वनि / অনন্ত ধ্বনি)

**Next-Gen YouTube Music Client & Universal Audiophile Player for Android**  
*Bit-Perfect USB DAC • 31-Band Studio Equalizer • Parametric Q • Binaural Crossfeed • R128 Loudness • Synced Lyrics • Zero Bloat*

[![Build Status](https://img.shields.io/badge/Build-Passing%20(24%2F24%20Tests)-success?style=for-the-badge&logo=android)](https://github.com/Specttre404/LastWaveX)
[![Platform](https://img.shields.io/badge/Platform-Android%208.0%2B%20(API%2026%2B)-blue?style=for-the-badge&logo=android)](https://github.com/Specttre404/LastWaveX)
[![Kotlin](https://img.shields.io/badge/Kotlin-2.0.21-purple?style=for-the-badge&logo=kotlin)](https://kotlinlang.org)
[![Toolkit](https://img.shields.io/badge/Toolkit-Jetpack%20Compose%201.7.5-deepskyblue?style=for-the-badge&logo=jetpackcompose)](https://developer.android.com/jetpack/compose)
[![Client](https://img.shields.io/badge/Client-YouTube%20Music-red?style=for-the-badge&logo=youtubemusic)](https://music.youtube.com)
[![Architecture](https://img.shields.io/badge/Architecture-Clean%20MVVM%20%2B%20C%2B%2B%20DSP-yellowgreen?style=for-the-badge)](https://github.com/Specttre404/LastWaveX)
[![Design](https://img.shields.io/badge/Design-Material%203%20Expressive%20%2B%20LiquidGlass-critical?style=for-the-badge)](https://github.com/Specttre404/LastWaveX)
[![VirusTotal Safe](https://img.shields.io/badge/VirusTotal-Safe-success?style=for-the-badge&logo=virustotal&logoColor=white)](https://www.virustotal.com/gui/file/98c2e016ca563d11e5bee81dcecf928cc55c1a47b3dff481e66834c06784c5f5/detection)
[![License](https://img.shields.io/badge/License-GPLv3-orange?style=for-the-badge)](LICENSE)

[Download Latest APK](https://github.com/Specttre404/LastWaveX/releases) • [Key Highlights](#-key-upgrades--highlights-since-v100) • [Feature Matrix](#-feature-comparison) • [Architecture](#-technical-foundation) • [Security & Privacy](#-security-privacy--integrity) • [Developer (@Ishan____404)](https://x.com/Ishan____404)

</div>

---

## 📖 Product Overview & Positioning

> [!IMPORTANT]
> **Ananta Dhvani (अनन्त-ध्वनि / অনন্ত ধ্বনি)** — meaning *Infinite Sound* — is a high-fidelity Online Streaming Music Client and Universal Audiophile Player, powered by the YouTube Music catalog and Lossless FLAC/Qobuz CDN streams, paired with local audio file playback. Engineered by Ishan ([@Ishan____404](https://x.com/Ishan____404)) for audiophiles and power listeners who demand uncompromising sound quality without advertisement tracking, telemetry, or proprietary app bloat.

Every tier of the audio pipeline—from raw 32-bit Float PCM processing in C++ to millisecond-accurate synchronized karaoke typography—is built to eliminate playback jitter, audio distortion, and background battery drain.

---

## 🌟 Key Upgrades & Highlights (Since v1.0.0)

### 1. 🎛️ 31-Band Studio Graphic EQ Suite + Parametric Q-Factor
- **Full 31 ISO Center Frequencies**: Spanning 20 Hz to 20 kHz with -12.0 dB to +12.0 dB precision sliders.
- **Parametric Q Bandwidth Control**: Adjustable filter bell resonance (0.5 to 2.8, default 1.414 / √2) for surgical notch filtering or broad tonal sculpting.
- **Master Preamp Gain Staging**: Slider (-10.0 dB to +10.0 dB) to eliminate digital clipping when boosting bands.
- **18 Studio Presets**: Flat, Studio Master, Harman Target, Diffuse-Field, Acoustic Strings, Classical Hall, Smooth Jazz, Bass Punch, Sub-Bass Rumble, Bass Cut, Vocal Warmth, Hard Rock, Heavy Metal, EDM / Club, Hip-Hop 808, R&B Soul, Lounge, and Podcast Voice.
- **JSON Profile Import, Export & Copy Code**: Full SAF profile serialization and 1-tap clipboard code sharing.

### 2. 🎧 Binaural Headphone Crossfeed & Dual-DSP Engine
- **BS2B Acoustic Room Tuning**: Crossfeed algorithm (`levelDb` 3.0 to 9.5 dB, 700 Hz cutoff) simulates natural speaker acoustic decay on headphones, eliminating fatigue during extended listening sessions.
- **Dual-DSP Pipeline**: Parallel `oboeDsp_` and `mediaDsp_` instances process both native Oboe output and platform MediaCodec streams with zero dropped frames.

### 3. 🎨 Decoupled Canvas Shaders & Refractive Resonance Prism Icon
- **8 Pure Backdrop Canvas Engines**: `AMOLED_BLACK` (pure #000000 with sub-bass radial glow), `HDR_VIVID` (high-contrast gradient with pulse), `FLUID_GRADIENT`, `AMBIENT_GLOW`, `DYNAMIC_HARMONY`, `DYNAMIC_MONET`, `PRISM_SPECTRUM`, and `BLURRED_GLASS`.
- **Decoupled Rendering**: Canvas shaders render cleanly without muddy or dark artwork overlays.
- **Refractive Resonance Prism Icon**: Brand-new adaptive icon and standalone vector logo with specular glass ring and gradient audio wave.

### 4. 📊 5 Selectable Seekbar Styles
- **Wavy Fluid**: Multi-frequency sine wave undulating during playback and flattening on pause.
- **Segmented Dash**: High-tech 32-segment dashed time bar with glowing progress heads.
- **Minimal Pill**: Ultra-slim line expanding into a tactile pill on touch.
- **Studio Console**: Precision analog mixing desk fader with tick marks and numeric readout.
- **Capsule Pill**: Stadium-rounded tactile pill slider.

### 5. 📱 Clean Player Architecture & Tactile Gestures
- **Uncluttered Transport Deck**: Removed enclosing pill shapes and Surface boxes around play/pause and metadata—controls sit directly over the dynamic background canvas.
- **5 Layout Architectures**: `MODERN_M3`, `CLASSIC` (centered 320dp artwork), `IMMERSIVE_FULLSCREEN` (expanded 420dp art), `MINIMALIST` (typography-first 220dp art), and `COMPACT_DOCK` (bottom-anchored controls).
- **Physics & Gestures**: Left/Right 30% double-tap seek (±5s/10s), Center 40% double-tap heart burst with spring scaling, vertical drag dismiss into mini player, and tactile haptic feedback.

### 6. 🎙️ Ambient Audio Recognition & Voice Search
- **In-App Sound Search**: 5-second 16-bit PCM AudioRecord capture with radar pulse animation, identifying tracks via AudD / SoundSearch and displaying high-res match cards with a 1-tap **"Play Now in LastWaveX"** button.
- **Voice Search & Multi-Source Tabs**: SpeechRecognizer integration with multi-category tabs: Tracks, Artists, Albums, Playlists, Users, and **Local Files** (`SearchTab.LOCAL`).

### 7. 📶 Stream Quality & Storage Cache Controls
- **Per-Network Streaming Tiers**: Independent quality settings for Wi-Fi (Hi-Res Lossless 24/192, 24/96, CD 16/44.1, 320k) and Cellular (Data Saver 160k, 320k, Match Wi-Fi) with Auto Data-Saver toggle.
- **1-Tap Cache Manager**: Calculates temporary audio/image cache size and wipes temporary chunk buffers without touching downloaded offline tracks.

### 8. 📜 Lyrics Engine & Quote Exporter
- **Multi-Engine Fallback**: LRCLIB, Kugou, and TTML engines with dual "Translate" and "Phonetic" toggles.
- **8 Kinetic Animation Presets**: Apple Fluid, Karaoke Pulse, Kinetic Slide, Cinematic Focus, Lossless Glow, Glass Elevation, Dynamic Focus Zoom, and Minimal Clean.
- **1080×1350 Quote Exporter**: Select 1–4 lyric lines and export shareable PNG quote cards in Minimalist Dark, Dynamic Gradient, or Frosted Glassmorphic styles.

### 9. 🌐 Integrations (Last.fm & Discord RPC)
- **Last.fm Overhaul**: Local offline scrobble queuing in Room/memory with auto-flush on connection restore, real-time love/unlove sync on track heart tap, and configurable scrobble percent threshold (25% to 90%).
- **Discord Rich Presence**: Sends live track title, artist, album, elapsed/total playback timestamps, high-res artwork URL, and `"Listen on YouTube Music"` action buttons via Discord Gateway WebSocket.

### 10. 📊 Audio Diagnostics HUD & Offline Audio
- **Full Hardware Signal Path**: Input Source → Decoder → 31-Band C++ DSP Engine → R128 Loudness Enhancer → AudioTrack / USB DAC.
- **Live Telemetry & Copy Report**: Real-time sample rate, bit depth, bitrate, jitter (0.0 ms), buffer health, R128 target (+2.0 LUFS), volume headroom, and 1-tap "Copy Full Diagnostics" report button.
- **Logarithmic Sleep Timer**: Exponential decay volume fade-out over the final 30 seconds of playback.

---

## ⚡ Feature Comparison

| Feature | Conventional Players | Ananta Dhvani |
| :--- | :--- | :--- |
| **USB DAC Output** | Resampled forced 48 kHz mixing | **Bit-Perfect Direct Passthrough** bypassing Android system mixer |
| **Equalizer** | Basic 5-band system EQ | **31-Band Studio Graphic EQ** + Parametric Q + Preamp + 18 Presets |
| **Acoustic Tuning** | Flat stereo output | **Binaural Headphone Crossfeed** (BS2B algorithm) |
| **Volume Leveling** | Inconsistent track volumes | **R128 Loudness Normalization** (+2 dB target, -0.5 dBFS soft limiter) |
| **Commercial Intros / Skits** | Played in full | **Integrated SponsorBlock** automatically skipping filler & dead air |
| **Lyrics Experience** | Basic unsynced text | **Word-by-Word Karaoke Motion**, Phonetics, Translations, PNG Exporter |
| **Privacy & Telemetry** | Ad tracking & analytics SDKs | **100% Free & Open-Source (GPLv3)**, Zero Telemetry, Zero Ads |

---

## 🛡️ Security, Privacy & Integrity

We prioritize user safety, code auditability, and absolute transparency:

* **VirusTotal Clean Verification**: Every compiled release APK (`app-release.apk`) is independently audited across 70+ leading antivirus engines with **0/70 detections (Safe / Clean)**.
* **Zero Telemetry & Spyware**: No AdMob, no Google Analytics, no Firebase tracking, and zero background data harvesting. Network calls query public endpoints directly without intermediate proxy servers.
* **ProGuard Log Sanitization**: All `Log.v`, `Log.d`, and `Log.i` debug calls are stripped in release builds (`-assumenosideeffects class android.util.Log`).
* **Cleartext Traffic Elimination**: Enforces `cleartextTrafficPermitted="false"` across `AndroidManifest.xml` and `network_security_config.xml`.
* **Hardware-Backed Keystore**: Credentials (e.g. Discord tokens, Last.fm sessions) are encrypted locally using Android Keystore AES-256-GCM.

---

## 🏛️ Technical Foundation

Ananta Dhvani is structured under **Clean Architecture** and reactive unidirectional data flow (UDF):

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
* **Image Loading:** Coil with asynchronous bitmap preloading.
* **Test Suite:** 24 Unit Tests verifying DSP math, offline LRC parsing, and mock streams with 0 skips and 0 failures.

---

## 🛠️ Building from Source

### Prerequisites
* **Android Studio**: Ladybug / 2024.2.1+ or IntelliJ IDEA with Android plugin.
* **JDK**: OpenJDK 17 or higher.
* **Android SDK**: API Level 35 (Android 15) with NDK 28.2.

### Build Commands
```bash
# Clone repository
git clone https://github.com/Specttre404/LastWaveX.git
cd LastWaveX

# Run Kotlin compilation check
./gradlew app:compileDebugKotlin

# Execute unit tests
./gradlew app:testDebugUnitTest

# Build Release APK
./gradlew app:assembleRelease
```

The compiled release APK will be generated at `app/build/outputs/apk/release/app-release.apk`.

---

## 📜 Credits & Upstream Acknowledgments

We stand on the shoulders of giants in the open-source Android audio community:
* **[LastWave](https://github.com/Specttre404/LastWaveX)** — Foundational architecture, baseline playback service, and original UI inspiration.
* **[ArchiveTune Nightly](https://github.com/ArchiveTune)** — Inspiration for audiophile tuning, clean design language, and transparent security/VirusTotal audit practices.
* **[VIVI Music](https://github.com/vivi-music/vivi)** — Foundational open-source music client architecture, stream resolution patterns, and queue management.
* **[Media3 ExoPlayer](https://developer.android.com/media/media3)** — Android audio playback foundation.
* **[LRCLIB](https://lrclib.net)** — Public community synchronized lyrics database.
* **[SponsorBlock](https://sponsor.ajay.app)** — Community-driven database for skipping non-music segments.
* **[AudD](https://audd.io)** — Music recognition infrastructure.
* **[Last.fm](https://last.fm)** — Metadata search and scrobbling protocol.

---

## 💬 Community & Contact

Connect with the developer and maintainer:
* **Developer:** **Ishan**
* **X (formerly Twitter):** [@Ishan____404](https://x.com/Ishan____404)
* **GitHub Discussions:** [Ananta Dhvani Discussions](https://github.com/Specttre404/LastWaveX/discussions)
* **Bug Reports & Feature Requests:** [Ananta Dhvani Issues](https://github.com/Specttre404/LastWaveX/issues)

---

## ⚖️ Disclaimer & License

Ananta Dhvani is developed for educational, private, and research purposes. All music streaming content is accessed via publicly available network interfaces. All trademarks, track names, artist identities, and album covers belong to their respective copyright holders.

Distributed under the **GNU General Public License v3.0 (GPLv3)**. See [`LICENSE`](LICENSE) for complete terms.

<div align="center">
  <p><b>Ananta Dhvani (अनन्त-ध्वनि / অনন্ত ধ্বনি)</b> — Free &amp; Open Source Software for Android</p>
  <p>Crafted with passion by <a href="https://x.com/Ishan____404">Ishan (@Ishan____404)</a></p>
</div>
