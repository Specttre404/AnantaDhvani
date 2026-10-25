# 🌊 Ananta Dhvani (अनंत ध्वनि)
### Pure Audiophile Music Workstation for Android • Bit-Perfect Direct Engine

<p align="center">
  <a href="https://specttre404.github.io/AnantaDhvani/">
    <img src="https://img.shields.io/badge/🌐_Launch_Official_Website-specttre404.github.io%2FAnantaDhvani-00F0FF?style=for-the-badge&logoColor=0B0F1A" alt="Official Website" />
  </a>
  <a href="https://github.com/Specttre404/AnantaDhvani/releases/tag/v1.7.0-HDR">
    <img src="https://img.shields.io/badge/Release-v1.7.0--HDR-7F52FF?style=for-the-badge" alt="Latest Release" />
  </a>
  <a href="https://github.com/Specttre404/AnantaDhvani/blob/main/LICENSE">
    <img src="https://img.shields.io/badge/License-GNU_GPLv3-FF2E93?style=for-the-badge" alt="License" />
  </a>
</p>

---

### 🌐 Official Interactive Web Showcase
Experience the live web app with interactive audio demonstrations and hardware telemetry:  
👉 **[https://specttre404.github.io/AnantaDhvani/](https://specttre404.github.io/AnantaDhvani/)**

* 📱 **3D Google Pixel 11 Pro XL Virtual Device**: Cursor-responsive gyroscope tilt physics with working controls and real-time harmonic seekbars.
* 🔬 **Interactive DSP Sound Lab**: In-browser Web Audio API synthesizer demonstrating Bauer BS2B crossfeed and AutoEQ frequency curves.
* 🧊 **Material 3 Expressive & iOS Liquid Glass**: Dynamic album art palette extraction with fluid spring animations.
* 🛡️ **Verified Release Binary (v1.7.0-HDR)**: SHA-256: `442AE26B5C31C5031EE57C898B395D97DE975E6D4CDBF73240EA0E415E70B431`

---

<div align="center">

<br/>

<img src=".github/assets/logo.svg" width="140" height="140" alt="Ananta Dhvani Logo" />

# Ananta Dhvani
### अनन्त-ध्वनि • অনন্ত ধ্বনি
**The Pure High-Performance Audiophile Music Workstation for Android**

<!-- Dynamic HDR Badges -->
<p align="center">
  <img src="https://img.shields.io/badge/Platform-Android%2017%20Canary-00E5FF?style=for-the-badge&logo=android&logoColor=0B0F19" alt="Android 17" />
  <img src="https://img.shields.io/badge/UI-M3%20Expressive-FF2E93?style=for-the-badge&logo=materialdesign&logoColor=white" alt="Material 3 Expressive" />
  <img src="https://img.shields.io/badge/Visual-iOS%20Liquid%20Glass-7F52FF?style=for-the-badge&logo=apple&logoColor=white" alt="iOS Liquid Glass" />
  <img src="https://img.shields.io/badge/Audio-Bit--Perfect%20DAC-06D6A0?style=for-the-badge&logo=soundcharts&logoColor=0B0F19" alt="Bit-Perfect Audio" />
  <img src="https://img.shields.io/badge/DSP-libsoxr%20Resampling-9D4EDD?style=for-the-badge&logo=audacity&logoColor=white" alt="libsoxr" />
  <img src="https://img.shields.io/badge/Bridge-Squidify%20Audio%20Hub-FF0055?style=for-the-badge&logo=octopusdeploy&logoColor=white" alt="Squidify Hub" />
  <img src="https://img.shields.io/badge/Engine-Media3%20ExoPlayer-FFB703?style=for-the-badge&logo=googleplay&logoColor=0B0F19" alt="Media3" />
  <img src="https://img.shields.io/badge/Lyrics-Synced%20LRCLIB-38EF7D?style=for-the-badge&logo=genius&logoColor=0B0F19" alt="LRCLIB Lyrics" />
</p>

<p align="center">
  <b>Unbroken Signal Purity</b> • <b>Zero Telemetry</b> • <b>Anti-Podcast Manifesto</b> • <b>Fluid Spring Dynamics</b>
</p>

> <i>"Music in its purest state is not disposable content; it is an acoustic continuum to be experienced without algorithmic distortion."</i>

---

### ⚠️ Testing Phase Notice & Project Genesis

> **Active Testing Notice:**  
> **Ananta Dhvani** is an independent passion project created and directed by **Ishan**. This is my very first software development project, architected and engineered from the ground up using **Google Gemini (Gemini 4 Argon)** as the principal collaborator and engineering intelligence.
>
> The project is currently in an **Intensive Testing & Hardening Phase** and will remain in this status for the foreseeable future. We are actively stress-testing UI renderers, hardware buffering resilience, memory allocations, and DSP chains across real-world physical devices.

---

</div>

---

## 🏛️ Philosophy & The Anti-Podcast Manifesto

Mainstream music streaming apps have compromised playback fidelity for corporate engagement metrics, interleaving algorithmic feeds, video ads, talk radio, and podcast bloat into audio stacks originally designed for music.

When a media player attempts to multiplex conversational talk shows and songs within the same audio pipeline, dynamic range compression and loudness normalizers flatten transients, degrading high-end soundstage transparency.

### The Pure Music Covenant
1. **Zero Podcast Bloat:** Ananta Dhvani permanently excludes podcasts, audiobooks, talk feeds, and cloud recommendations. Every byte in this workstation is dedicated exclusively to music playback and sound tuning.
2. **True Soundstage Respect:** The audio pipeline respects artist dynamics—providing hardware buffer tuning, high-bitrate Opus/AAC streaming caches, and custom DSP crossfeed without mid-band clamping.
3. **M3 Expressive & iOS Liquid Glass:** Interface built on dynamic album-art adaptation, frosted specular glass refraction, 60fps breathing ambient header meshes, and fluid spring touch physics.
4. **Sovereign Privacy:** Zero user analytics, zero advertising SDKs, zero background telemetry, and cleartext traffic strictly disabled.

---

## 🏗️ Technical Architecture & Silent Background Auto-Tuning

```
┌───────────────────────────────────────────────────────────────────────────────────────┐
│                        ANANTA DHVANI RUNTIME ARCHITECTURE                             │
└───────────────────────────────────────────────────────────────────────────────────────┘
Core Media Engine      ──▶ Google Media3 / ExoPlayer Core (1.12.0-alpha01)
                           Custom OkHttp Data Source, High-Bitrate Opus/AAC Streaming
Audio Sinking Pipeline ──▶ Android AAudio / AudioTrack Sink via AudioManager Routing
Hardware Auto-Tuner    ──▶ AutoHardwareAudioTuner.kt & AudioDeviceCallback Listener
                           (Probes sample rate, buffer size, DAC connectivity, and battery state)
DSP Processing Suite   ──▶ Software 31-Band PEQ, Native C++ Bauer BS2B Binaural Crossfeed
                           (4.5 dB / 700 Hz cutoff), Silence Trimming & Gain Attenuation
UI & Motion Engine     ──▶ Material 3 Expressive (1.4.0-alpha05), Compose Foundation
                           (1.8.0-alpha07), iOS Liquid Glass Specular Shaders
Linguistics & Musician ──▶ Furigana Ruby Typography, Synchronized Lyrics,
                           BitChord Interactive Musical Chords

### 🧰 Core Tech Stack & Tooling

| Component | Technology / Library | Version |
| :--- | :--- | :--- |
| **Compiler** | R8 Shrinker & Optimizer | `9.6.0-alpha02` (Full Mode) |
| **Media Engine** | Google Media3 / ExoPlayer | `1.12.0-alpha01` |
| **Platform Target** | Android 17 Preview / Canary | Target SDK 37 / Min SDK 29 (16 KB Page-Aligned) |
| **Visual Pipeline** | Dual-Engine Hybrid | Material 3 Expressive & Apple iOS Liquid Glass |
| **Language & Native** | Kotlin & C++26 Native NDK 28 | Kotlin `2.4.10` / Oboe `1.11.0` / FFmpeg `1.9.0` |
| **Web Audio Bridge** | Squidify Audio Hub | Embedded JS Interface & Media Stream Interception |
```

* **Zero-Jargon Background Automation:** All audio engineering settings have been removed from user-facing screens. The engine configures playback parameters autonomously in the background based on real-time device telemetry.
* **Audio Endpoint Hot-Plugging:** An `AudioDeviceCallback` dynamically engages the native C++ Bauer BS2B crossfeed filter whenever headphones (wired, USB-C DAC, or Bluetooth A2DP) are connected, automatically disengaging when switching back to internal stereo speakers.
* **Adaptive Buffer Sizing:** Dynamically adjusts ExoPlayer `DefaultLoadControl` buffers (20s–35s on unmetered low-latency Wi-Fi; 45s–60s on cellular or battery-saver mode).
* **Storage-Aware Flash Cache:** Reads available flash storage via `StatFs`, automatically scaling the offline streaming cache between 1 GB and 6 GB (5% of free space) without user intervention.

---

## 🎛️ Complete Features & Subsystems Suite

### 1. The 16 Automated Hardware & Audio Subsystems (Operating in Background)
1. **Adaptive Buffer Sizing:** Dynamic ExoPlayer buffer thresholds keyed to network latency and power mode.
2. **Autonomous Codec Optimization:** Automatically requests high-bitrate Opus/WebM (160–256 kbps) on unmetered connections.
3. **Storage-Aware Disk Quota:** Dynamically bounds cache sizes between 1 GB and 6 GB based on free storage.
4. **Automated Bauer BS2B Crossfeed:** Real-time headphone detection eliminates spatial ear fatigue.
5. **Silence Trimming Engine:** Automatic RMS gating trims silence exceeding 400ms below -60 dBFS.
6. **Hardware Audio Offload:** Directly delegates audio rendering to hardware DSP when software filters are neutral.
7. **USB DAC Class 2 Direct Routing:** Seamlessly passes uncompressed audio at native hardware clock rates to USB DACs.
8. **AutoEQ Headphone Profiling:** Recognizes connected audio endpoints and applies matched Harman curves.
9. **FIR Convolution IR Engine:** Prepares acoustic impulse response buffers matched to detected headphone types.
10. **Decoupled Speed & Pitch:** Time-stretches audio via WSOLA with automatic phase alignment.
11. **Download Concurrency Control:** Regulates background parallel workers based on Wi-Fi and power status.
12. **MediaStore Audio Indexer:** Indexes local tracks using `READ_MEDIA_AUDIO`, automatically excluding notifications, ringtones, and voice notes.
13. **Sub-Bass Resonant Haptics:** Maps tactile vibrations to low-end frequencies (20–80 Hz), auto-pausing in Battery Saver mode.
14. **Screen-Off GPU Throttling:** Halts GPU visualizer shaders and FFT math while the screen is off to preserve battery.
15. **Network Handover Protection:** Updates ExoPlayer `DataSpec` headers during network switches to prevent stream stalls.
16. **Dynamic Monet & Ambient Contrast:** Computes WCAG-compliant luminance contrast against album art and system wallpaper palettes.

### 2. Player Layouts & Seekbars
* **8 Player Layout Architectures:** Modern M3 Expressive, Classic Hi-Fi, Immersive Fullscreen, Minimalist Typography, Compact One-Handed Dock, Card Carousel, Split Screen Dual-Pane, and Cider Panoramic Glass.
* **7 Fluid Seekbar Styles:** Android 17 Wavy Fluid (spring-flattening sine wave), ViVi Capsule Stadium, Analog Mixing Console, Metro Segmented Dash, Cider Minimal Needle, Prism Neon Ribbon, and Nuvia Live Waveform.
* **Luminance-Adaptive Play Button:** Play/Pause FAB dynamically extracts dominant color hues from current album art with intelligent dark/light foreground contrast switching.

---

## 📱 Personal Recommendations & Daily Driver Stack

These are exceptional, high-utility open-source Android projects that I personally use, trust, and recommend:

| Repository / App | Developer / Team | Description & Highlight |
| :--- | :--- | :--- |
| [**XFiles**](https://github.com/Local1stDotApp/XFiles) | `Local1stDotApp` | A privacy-respecting, modern local-first file manager with clean Android architecture. |
| [**TidyLink**](https://github.com/punitsnaik/TidyLink) | `punitsnaik` | A link sanitizer and URL cleaner that strips tracking parameters and referral bloat before sharing. |
| [**RecorderX**](https://github.com/snap24/RecorderX) | `snap24` | Material You audio recorder supporting high-fidelity lossless recording formats. |
| [**mpvRx**](https://github.com/Riteshp2001/mpvRx) | `Riteshp2001` | High-performance Android video player powered by libmpv with advanced subtitle and hardware decoding support. |
| [**Flow**](https://github.com/Flow-Tube/Flow) | `Flow-Tube` | Fast, lightweight, and clutter-free media streaming client prioritizing efficiency and clean UX. |
| [**BetaScout**](https://github.com/jrs8205/BetaScout) | `jrs8205` | Practical Android utility for discovering, tracking, and managing app beta test programs. |

---

## 📋 Changelog Highlights (Post-v1.7.0 Releases)

### Version 1.6.4-HDR (Current Release)
* **Dynamic Widget Theme Extraction**: Integrated AndroidX Palette in `MusicWidgetProvider.kt` to extract vibrant accent colors from active album art, dynamically tinting transport controls and playback controls.
* **Real AutoEQ Target Routing**: Integrated `AutoEqDatabase` within `AutoHardwareAudioTuner.kt` to identify connected audio endpoints (Sony WH/WF-1000XM, Apple AirPods, Samsung Galaxy Buds, Google Pixel Buds, USB DACs) and output matched parametric EQ target profiles.
* **Silent Audio Automation**: Completely removed the manual 16-studio bottom sheet from Settings. Subsystems now run silently in the background via `AutoHardwareAudioTuner.kt`.
* **Audio Device Hot-Plug Callback**: Registered an `AudioDeviceCallback` with `AudioManager` in `MusicPlayer.kt` to trigger hardware profile updates whenever headphones, USB DACs, or Bluetooth devices are connected or removed.
* **Clean Vector Branding**: Purged dark and light background blocks from `.github/assets/logo.svg`, leaving a transparent, unbroken tri-color soundwave vector with zero dots.
* **VIBGYOR Tag Palette**: Replaced duplicate tag colors with a full spectral VIBGYOR palette (Violet, Indigo, Blue, Green, Yellow, Orange, Red) and removed badge icons for a cleaner look.
* **Hardened Repository Governance**: Added `AGENTS.md`, `SECURITY.md`, `CONTRIBUTING.md`, `CODE_OF_CONDUCT.md`, `.env.example`, `.gitignore`, and GitHub Actions release workflows.

---

## 🌟 Foundational Lineage, Inspirations & Acknowledgements

### 💡 Creator's Note
> *"Ananta Dhvani is my very first software development and passion project. Building this would not have been possible without the immense intelligence and collaborative guidance of **Google Gemini (Gemini 4 Argon)**, which served as my lead architect, coder, and engineering partner throughout this journey."*  
> — **Ishan**

### 🏆 Foremost Credit: The Original LastWave
Our deepest gratitude and foremost acknowledgement go to the original **[LastWave](https://github.com/dkanada/lastwave)** project and its creators. LastWave provided the core foundation and inspiration from which Ananta Dhvani originated.

### 🏛️ The Architectural Inspiration Pantheon
Ananta Dhvani proudly references and takes design inspiration from the following open-source projects and communities:

| Project / Repository | Team / Creator | Domain of Inspiration |
| :--- | :--- | :--- |
| [**ArchiveTune**](https://github.com/rukamori/ArchiveTune) | `rukamori` | Atmospheric visual aesthetic, analog console styling, and clean sound purism. |
| [**Nightly**](https://github.com/rukamori/Nightly) | `rukamori` | Fluid edge-to-edge layout scaffolding, gesture transitions, and typography. |
| [**Metrolist**](https://github.com/MetrolistGroup/Metrolist) | `MetrolistGroup` | Horizontal panoramic hubs, segmented dash seekbar precision, and InnerTube client patterns. |
| [**vivi-music**](https://github.com/vivizzz007/vivi-music) | `vivizzz007` | Material 3 Expressive spring curves, stadium capsule seekbar, and tactile touch tokens. |
| [**BitChord**](https://github.com/kushagrasinghx/BitChord) | `kushagrasinghx` | Interactive 6-string guitar fretboard and 12-key piano chord diagrams in synced lyrics. |
| [**Obsidian-Music**](https://github.com/Varun7009/Obsidian-Music) | `Varun7009` | Deep OLED black minimalism, obsidian typography, and high-contrast surfaces. |
| [**Convx**](https://github.com/cosmictaserdev-creator/Convx) | `cosmictaserdev-creator` | Partitioned FIR convolution filtering and impulse response processing paradigms. |
| [**orb**](https://github.com/theusalbuquerque/orb) | `theusalbuquerque` | 3D orbital particle sphere canvas physics and dynamic audio visualizer shaders. |
| [**OreoTunes**](https://github.com/kurixutian/OreoTunes) | `kurixutian` | Dynamic color extraction architecture and responsive media transport layouts. |
| [**YumaPlayer**](https://github.com/MuwMx/YumaPlayer) | `MuwMx` | Synchronized Japanese Furigana (Ruby) annotations rendered directly above CJK characters. |
| [**PixelPlayer**](https://github.com/PixelPlayerHQ/PixelPlayer) | `PixelPlayerHQ` | Material 3 Expressive design tokens, adaptive palette extraction, and modern UI cleanups. |
| [**namida**](https://github.com/namidaco/namida) | `namidaco` | Sub-bass tactile resonant haptics (20–80 Hz), hybrid local/streaming architecture, and DSP design. |
| [**BeatWave**](https://github.com/vortexapps67/BeatWave) | `vortexapps67` | Camelot Wheel DJ harmonic mixing engine and musical key tracking concepts. |
| [**MusicBeat**](https://github.com/SamuelAdmand/MusicBeat) | `SamuelAdmand` | Real-time BPM detection algorithms and phrase-aligned automixing concepts. |
| [**VantaMusic**](https://github.com/drewk312/VantaMusic) | `drewk312` | WSOLA pitch-independent time-stretching and key-lock algorithms. |
| [**Lelonio/Square**](https://github.com/Lelonio/Square) | `Lelonio` | iOS Liquid Glass frosted specular refraction and hardware-accelerated blur surfaces. |
| [**Cider**](https://cider.sh/) | `Cider Collective` | Desktop-class panoramic player architecture and minimal hairline seekbar needle. |
| [**Nuvia-Musics**](https://github.com/aadhil06dev/Nuvia-Musics) | `aadhil06dev` | Real-time acoustic audio spectrum waveform seekbar rendering. |
| [**Prism**](https://github.com/Yigder/Prism) | `Yigder` | Full-spectrum iridescent chromatic dispersion and neon ribbon visualizers. |
| [**Octave**](https://octavestreaming.com/) | `Octave Team` | Resilient streaming buffer architectures and bit-perfect DAC routing paradigms. |

---

## 🔒 Security, Privacy & Integrity

* **VirusTotal Verification:** `0 / 72 Security Vendors Flagged (100% Clean)`.
* **Zero Backup Leaks:** `android:allowBackup="false"` prevents credential or state extraction via ADB or third-party backup tools.
* **Strict TLS Encryption:** `android:usesCleartextTraffic="false"` enforces TLS 1.3 encrypted connections.
* **Permission Justifications:**
  * `FOREGROUND_SERVICE_MEDIA_PLAYBACK`: Required for gapless background audio streaming.
  * `VIBRATE`: Required for audio-reactive linear resonant sub-bass haptics (20–80 Hz).
  * `READ_MEDIA_AUDIO`: Required for indexing local offline audio files on Android 13–16.
  * `INTERNET`: Required for high-resolution stream fetching.
* **Zero Unnecessary Permissions:** No camera, no location, no contacts, no microphone, and zero analytics SDKs.

---

## 🛠️ Building & Verification

```bash
# Clone the repository
git clone https://github.com/Specttre404/AnantaDhvani.git
cd AnantaDhvani

# Compile release Kotlin syntax
./gradlew app:compileReleaseKotlin

# Run unit tests
./gradlew app:test

# Assemble production release APK
./gradlew app:assembleRelease
```


