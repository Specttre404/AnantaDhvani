<div align="center">

<img src="docs/assets/logo.svg" alt="LastWaveX Logo" width="128" height="128" />

# LastWaveX

**Next-Gen YouTube Music Client & Universal Audiophile Player for Android**  
*Bit-Perfect USB DAC • 31-Band Studio Equalizer • R128 Loudness Normalization • Synced Word-by-Word Lyrics • Material 3 Expressive • Zero Bloat*

[![Build Status](https://img.shields.io/badge/Build-Passing%20(24%2F24%20Tests)-success?style=for-the-badge&logo=android)](https://github.com/Specttre404/LastWaveX)
[![Platform](https://img.shields.io/badge/Platform-Android%208.0%2B%20(API%2026%2B)-blue?style=for-the-badge&logo=android)](https://github.com/Specttre404/LastWaveX)
[![Kotlin](https://img.shields.io/badge/Kotlin-2.0.21-purple?style=for-the-badge&logo=kotlin)](https://kotlinlang.org)
[![Toolkit](https://img.shields.io/badge/Toolkit-Jetpack%20Compose%201.7.5-deepskyblue?style=for-the-badge&logo=jetpackcompose)](https://developer.android.com/jetpack/compose)
[![Client](https://img.shields.io/badge/Client-YouTube%20Music-red?style=for-the-badge&logo=youtubemusic)](https://music.youtube.com)
[![Architecture](https://img.shields.io/badge/Architecture-Clean%20MVVM%20+%20C++%20DSP-yellowgreen?style=for-the-badge)](https://github.com/Specttre404/LastWaveX)
[![Design](https://img.shields.io/badge/Design-Material%203%20Expressive%20+%20LiquidGlass-critical?style=for-the-badge)](https://github.com/Specttre404/LastWaveX)
[![VirusTotal Safe](https://img.shields.io/badge/VirusTotal-Safe-success?style=for-the-badge&logo=virustotal&logoColor=white)](https://www.virustotal.com/gui/file/98c2e016ca563d11e5bee81dcecf928cc55c1a47b3dff481e66834c06784c5f5/detection)
[![License](https://img.shields.io/badge/License-GPLv3-orange?style=for-the-badge)](LICENSE)

[Download Latest APK](https://github.com/Specttre404/LastWaveX/releases) • [Feature Matrix](#-implemented-features-matrix) • [In-App Gallery](#-in-app-screenshots-showcase) • [Roadmap](#-project-roadmap) • [Build from Source](#%EF%B8%8F-building-from-source) • [Developer (@Ishan____404)](https://x.com/Ishan____404)

</div>

---

## 📖 Product Overview & Positioning

> [!IMPORTANT]
> **LastWaveX is a high-fidelity Online Streaming Music Client**, powered by the YouTube Music catalog and Lossless Qobuz/FLAC CDN streams, paired with optional local caching, downloads, and local device audio scanning. It is engineered by Ishan for audiophiles and power listeners who want cloud streaming flexibility without compression artifacts, advertisement tracking, or proprietary app bloat.

Every tier of the audio pipeline—from raw PCM processing in native C++ to millisecond-accurate synchronized karaoke typography—has been built from scratch to eliminate playback lag, audio distortion, and battery drain.

---

## 🚀 Major Release Highlights: v1.2.1 Audiophile Studio Edition

* **31-Band Studio Graphic Equalizer:** Full professional ISO 1/3-octave center frequencies (20 Hz to 20 kHz) with ±12.0 dB range and 0.1 dB precision.
* **Master Preamp Gain Staging:** Dedicated -10.0 dB to +10.0 dB master preamp control to prevent digital clipping when boosting equalizer bands.
* **Custom EQ Profile JSON Export & Import:** Full JSON serialization and SAF file import/export for custom 31-band tuning profiles.
* **ReplayGain 2.0 / ITU-R BS.1770 (R128) Loudness Normalization:** Perceived LUFS target leveling (+2.0 dB) with a soft limiter ceiling (-0.5 dBFS) preventing inter-sample clipping.
* **Logarithmic Fade-Out Sleep Timer:** Natural, audiophile exponential decay curve over the final 30 seconds of playback.
* **Pitch & Tempo Shift Controls:** Independent Pitch (0.8x to 1.2x) and Speed/Tempo (0.5x to 2.0x) parameters via Media3 `PlaybackParameters`.
* **31-Point Bézier Frequency Visualizer:** Real-time smooth Bézier spline curve on Canvas with ambient gradient fill.
* **Total Bloat Removal & Developer Branding:** 100% excised all AI dependencies and background services for zero background drain. Crafted with passion by **Ishan** ([@Ishan____404](https://x.com/Ishan____404)).

---

## ⚡ What Makes LastWaveX Different?

| Conventional Players | LastWaveX |
| :--- | :--- |
| **Resampled Android Audio** (48 kHz forced mixing) | **Bit-Perfect Direct Passthrough** bypassing Android's mixer for external USB DACs |
| **Jarring Volume Differences** across tracks | **R128 Loudness Normalization** (+2 dB target leveling) with -0.5 dBFS soft limiter |
| **Spoken Intros & Long Music Video Sketches** | **Integrated SponsorBlock** automatically seeking past filler, intros, and dead air |
| **Static Square Covers** | **Rotating Vinyl Record Mode** with physical spin deceleration and grooved rings |
| **Basic Unsynced Text** | **Word-by-Word Karaoke Motion**, Romaji/Pinyin phonetics, and real-time translations |
| **Bloated Social Feeds & Ad Tracking** | **100% Open-Source (GPLv3)**, privacy-respecting, zero telemetry, zero advertisements |

---

## 📱 In-App Screenshots Showcase

<div align="center">
  <img src="docs/assets/screenshots/feed.png" width="23%" alt="Feed & Discovery" />
  <img src="docs/assets/screenshots/player_vinyl.png" width="23%" alt="Rotating Vinyl Player" />
  <img src="docs/assets/screenshots/equalizer.png" width="23%" alt="31-Band Studio Equalizer" />
  <img src="docs/assets/screenshots/diagnostics.png" width="23%" alt="Hardware Diagnostics" />
</div>

---

## ✨ Implemented Features Matrix

### 🎛️ Studio-Grade Audio Engine & DSP
* **Bit-Perfect Mode:** Routes raw bit-exact streams directly to external USB DACs, bypassing the Android system resampler and software EQ.
* **31-Band Studio Equalizer:** Full ISO 1/3-octave graphic equalizer with instant zero-stutter gain switching, preamp gain staging, and JSON profile import/export.
* **R128 Loudness Normalization:** Perceived LUFS target leveling with soft-knee limiting.
* **Dynamic Bass Boost:** Real-time low-frequency harmonics amplification (20 Hz to 160 Hz) with soft-knee limiting.
* **Configurable Crossfade:** Smooth 1 to 12-second dual-player overlapping transitions.
* **Skip Silence:** Detects and skips dead air in audio tracks without clipping vocal tails.
* **Playback Speed & Pitch Controls:** Independent Speed (0.5x to 2.0x) and Pitch (0.8x to 1.2x) sliders.
* **Stats for Nerds HUD:** Real-time overlay displaying active audio codec, sample rate, bit depth, channel configuration, and DAC output clock drift.

### 🎙️ Advanced Lyrics & Card Generator
* **Synchronized & Word-by-Word Lyrics:** Millisecond-accurate vocal highlighting powered by LRCLIB, Kugou, and TTML engines.
* **Dual Translation & Phonetics Toggles:** One-tap header controls to display English translations and Romanized (Romaji/Pinyin) guides for non-Latin songs.
* **Lyric Card / Quote Image Generator:** Highlight 1–4 lines of lyrics and export as high-resolution (1080×1350) shareable cards in three designs.

### 🎨 Visual Architecture & Customization
* **Rotating Vinyl Record Mode:** Transforms standard album covers into a spinning vinyl record with grooved micro-rings and realistic momentum physics.
* **8 Dynamic Backdrop Engines:** `HDR_VIVID`, `FLUID_GRADIENT`, `DYNAMIC_HARMONY`, `AMBIENT_GLOW`, `DYNAMIC_MONET`, `AMOLED_BLACK`, `BLURRED_GLASS`, and `PRISM_SPECTRUM`.
* **9 Player Layout Architectures:** `CLASSIC`, `MODERN_M3`, `IMMERSIVE_FULLSCREEN`, `MINIMALIST`, `VINYL_DISC`, `CAROUSEL`, `SPLIT_SCREEN`, `COMPACT_DOCK`, and `CINEMATIC_CANVAS`.

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
* 31-Band Studio Equalizer (20 Hz – 20 kHz with 0.1 dB precision)
* Master Preamp Gain Staging (-10 dB to +10 dB) & JSON profile import/export
* Real-time 31-point Bézier frequency curve canvas visualizer
* R128 / ITU-R BS.1770 Loudness Normalization
* Logarithmic Fade-Out Sleep Timer
* Pitch & Speed/Tempo Shift Controls
* MediaStore Local Audio Scanner (FLAC/MP3 import)
* Storage Cache Cleaner & Storage Manager
* VirusTotal 0/70 Safe security verification (ArchiveTune Nightly standard)

### 🔄 In Progress
* ReplayGain 2.0 track-level metadata tag reader
* Advanced parametric Q bandwidth slider

### 📋 Planned
* Custom user EQ preset sharing hub
* Extended multi-channel spatial binaural panner

---

## 🛡️ Security & Safety Verification (VirusTotal Safe)

LastWaveX is 100% Free and Open-Source Software (GPLv3). We prioritize user safety, auditability, and transparency:

* **VirusTotal Scanned:** Every compiled release APK (`app-release.apk`) is independently audited across 70+ leading antivirus and security engines (Kaspersky, Avast, BitDefender, Google Play Protect, Microsoft Defender, etc.) with **0/70 detections (Safe / Clean)**, following ArchiveTune Nightly's verification standards.
* **Zero Telemetry & Spyware:** No commercial tracking SDKs, no Google AdMob, no behavioral analytics, and zero background data collection. All network requests query public endpoints directly without intermediate proxy servers.
* **Hardware-Backed Keystore:** External credentials (e.g., Discord Gateway tokens, Last.fm sessions) are encrypted locally using Android Keystore AES-256-GCM. Plaintext tokens are never logged or stored in plain preferences.

---

## 📜 Credits & Upstream Acknowledgments

We stand on the shoulders of giants in the open-source Android audio community:
* **[LastWave](https://github.com/Specttre404/LastWaveX)** — Original foundational architecture, baseline playback service, and UI inspiration.
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
* **GitHub Discussions:** [LastWaveX Discussions](https://github.com/Specttre404/LastWaveX/discussions)
* **Bug Reports & Feature Requests:** [LastWaveX Issues](https://github.com/Specttre404/LastWaveX/issues)

---

## ⚖️ Disclaimer & License

LastWaveX is developed for educational, private, and research purposes. All music streaming content is accessed via publicly available network interfaces. All trademarks, track names, artist identities, and album covers belong to their respective copyright holders.

Distributed under the **GNU General Public License v3.0 (GPLv3)**. See [`LICENSE`](LICENSE) for complete terms.

<div align="center">
  <p><b>LastWaveX</b> — Free &amp; Open Source Software for Android</p>
  <p>Crafted with passion by <a href="https://x.com/Ishan____404">Ishan (@Ishan____404)</a></p>
</div>
