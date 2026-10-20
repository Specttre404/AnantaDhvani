<div align="center">

<br/>

<img src=".github/assets/logo.svg" width="140" height="140" alt="Ananta Dhvani Logo" />

# Ananta Dhvani
### अनन्त-ध्वनि • অনন্ত ধ্বনি
**The Pure High-Performance Audiophile Music Workstation for Android**

<p align="center">
  <a href="https://github.com/Specttre404/AnantaDhvani/releases/latest">
    <img src="https://img.shields.io/badge/Latest_Release-v1.6.1--HDR_Preview-FF2E93?style=for-the-badge&logo=github&logoColor=FFFFFF" alt="Latest Release" />
  </a>
  <img src="https://img.shields.io/badge/Lifecycle-Testing_Phase_(Active_Evaluation)-FFC700?style=for-the-badge&logo=gitbook&logoColor=0B0F1A" alt="Testing Phase" />
  <a href="https://github.com/Specttre404/AnantaDhvani/blob/main/LICENSE">
    <img src="https://img.shields.io/badge/License-GNU_GPL_v3.0-00F0FF?style=for-the-badge&logo=gnu&logoColor=0B0F1A" alt="License" />
  </a>
  <img src="https://img.shields.io/badge/Platform-Android_12+_--_16_(API_31--36)-3DDC84?style=for-the-badge&logo=android&logoColor=0B0F1A" alt="Platform" />
  <img src="https://img.shields.io/badge/VirusTotal-100%25_Clean_(0/72)-00F0FF?style=for-the-badge&logo=virustotal&logoColor=0B0F1A" alt="VirusTotal" />
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Architecture-Google_Media3_•_ExoPlayer-00F0FF?style=for-the-badge&logo=android&logoColor=0B0F1A" alt="Architecture" />
  <img src="https://img.shields.io/badge/DSP_Pipeline-Biquad_PEQ_•_BS2B_Crossfeed-FFC700?style=for-the-badge&logoColor=0B0F1A" alt="DSP Pipeline" />
  <img src="https://img.shields.io/badge/UI_Style-M3_Expressive_•_Liquid_Glass-FF2E93?style=for-the-badge&logo=jetpackcompose&logoColor=FFFFFF" alt="UI Style" />
  <img src="https://img.shields.io/badge/Client-YouTube_Music_InnerTube-FF0000?style=for-the-badge&logo=youtubemusic&logoColor=FFFFFF" alt="YouTube Music Client" />
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Forked_From-LastWave-6366F1?style=for-the-badge&logo=git&logoColor=FFFFFF" alt="Forked From LastWave" />
  <img src="https://img.shields.io/badge/Built_With-Google_Gemini_4_Argon-4285F4?style=for-the-badge&logo=google&logoColor=FFFFFF" alt="Built With Gemini" />
  <img src="https://img.shields.io/badge/Studios-16_Advanced_Hardware_Cards-00F0FF?style=for-the-badge" alt="Studios" />
  <img src="https://img.shields.io/badge/Codebase-Kotlin_•_Java_DSP_•_Compose-10B981?style=for-the-badge" alt="Codebase" />
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

## 🏗️ Technical Architecture & Pipeline Reality

To maintain complete transparency, the technical architecture of Ananta Dhvani is structured as follows:

* **Playback Engine:** Powered by Google's modern **Media3 / ExoPlayer** architecture with custom buffer configuration (`StreamBufferTuningCard`) ranging from 15s to 50s for drop-out resilience.
* **DSP Processing:** Software-level audio processing incorporating virtualized biquad equalization, Bauer BS2B crossfeed filters, and dynamic gain attenuation.
* **Storage & Indexing:** Compliant with modern Android storage models using scoped MediaStore queries and the mandatory `READ_MEDIA_AUDIO` permission for Android 13–16.

---

## 🎛️ Complete Features & Subsystems Suite

### 1. Advanced Audio Engineering & Hardware Tuning (16 Studios)
Accessed via **Settings → Personalization → Advanced Audio Engineering & Hardware Tuning**:
1. **Stream Buffer Tuning:** Granular ExoPlayer buffer limits (Min Buffer, Max Buffer, Playback Buffer).
2. **Stream Codec Forcing:** Direct selection between Highest Bitrate Auto and forced Opus / WebM (160–256 kbps).
3. **Disk Cache Quota:** Configurable offline streaming cache allocation (500 MB – 8 GB).
4. **Bauer BS2B Crossfeed:** Natural headphone acoustic simulation (Bauer Default, Chu Moy, Jan Meier, Custom).
5. **Silence Trimming:** Automatic silence detection threshold (dB) and minimum duration gating.
6. **Mini Player Gestures:** Configurable swipe gestures (Fluid Fade, Velocity Dismiss, Stadium Pill).
7. **Lyrics Typography Studio:** Real-time font size, line spacing, and inactive blur radius controls.
8. **Navigation Bar Layout:** Custom visibility toggles for bottom navigation tabs.
9. **Download Concurrency:** Parallel track download limits with Wi-Fi only safety toggling.
10. **Folder Exclusions:** Custom scanner folder inclusion and exclusion manager.
11. **Hardware Audio Offload:** Direct toggle for SoC hardware-accelerated audio rendering.
12. **Convolution IR Studio:** Impulse response (.wav) file selector with wet/dry gain mixing.
13. **Decoupled Pitch & Speed:** Independent tempo and pitch shifting controls.
14. **USB Direct Routing:** Class 2 USB DAC direct output monitoring and routing indicator.
15. **AutoEQ Calibration:** Calibrated parametric curves across 4,000+ headphone models.
16. **Metro E-Ink & Network Proxy:** High-contrast monochrome rendering and proxy resilience controls.

### 2. Player Layouts & Seekbars
* **8 Player Layout Architectures:** Modern M3 Expressive, Classic Hi-Fi, Immersive Fullscreen, Minimalist Typography, Compact One-Handed Dock, Card Carousel, Split Screen Dual-Pane, and Cider Panoramic Glass.
* **7 Fluid Seekbar Styles:** Android 17 Wavy Fluid (spring-flattening sine wave), ViVi Capsule Stadium, Analog Mixing Console, Metro Segmented Dash, Cider Minimal Needle, Prism Neon Ribbon, and Nuvia Live Waveform.
* **Luminance-Adaptive Play Button:** Play/Pause FAB dynamically extracts dominant color hues from current album art with intelligent dark/light foreground contrast switching.

---

## 📋 Changelog (Post-v1.6.0 & Current Releases)

### Version 1.6.1-HDR (Current Testing Release)
* **Dynamic Palette Play Button:** Fixed static cyan play button bug; Play/Pause FAB now extracts and renders dominant track artwork colors dynamically across all layouts.
* **Restored All 16 Audio Studios:** Completely repopulated the `showAdvancedSubsystemsSheet` modal bottom sheet to full 92% screen height with complete scrolling across all 16 engineering studios.
* **Safe Launcher Switching:** Resolved application termination crashes during icon theme changes by implementing pre-flight state checks and `PackageManager.DONT_KILL_APP` sequencing.
* **Notch Circle Purge:** Completely removed the orphaned grey camera punch-hole circle overlay from the top-center app scaffold.
* **Obsidian Splash Screen Rebranding:** Rebranded the startup splash screen to **Ananta Dhvani** in ExtraBold Serif on an obsidian void (`#0B0F1A`) background.
* **Ambient Header Aura Boost:** Boosted the dual-orb fluid particle breathing mesh alpha in `ExpressiveHeader.kt` for vivid visibility behind title typography.
* **Stream Transcoding Safeguard:** Added pre-flight stream validation checks in `AudioFormatConverter.kt` to prevent runtime crashes on remote or restricted media.
* **Storage Compliance:** Added `READ_MEDIA_AUDIO` permission to `AndroidManifest.xml` for local file indexing on Android 13 through 16.
* **Canary M3 Expressive Overrides:** Decoupled Compose BOM platform pinning in `app/build.gradle.kts` to allow Canary M3 Expressive and Media3 1.6-alpha dependencies to compile cleanly.

---

## 🗺️ Future Roadmap & Engineering Pipeline

The following milestones are planned as the testing phase progresses:

- [ ] **Phase 1: Native C++ Engine Integration:** Build an experimental C++20 Oboe / AAudio direct audio sink option alongside the Media3 core for bit-perfect hardware USB DAC passthrough.
- [ ] **Phase 2: Partitioned FIR Convolution DSP:** Integrate a native time-domain partitioned FIR convolution engine for `.wav` impulse responses directly into the audio pipeline.
- [ ] **Phase 3: Camelot Wheel & DJ Automix:** Implement harmonic key detection and phrase-aligned BPM crossfading inspired by BeatWave and MusicBeat.
- [ ] **Phase 4: Full Modularization:** Refactor monolithic UI files (`SettingsScreen.kt` and `PlayerHost.kt`) into independent feature modules for faster incremental compilation.
- [ ] **Phase 5: Native Predictive Back:** Implement system-level Predictive Back gestures for sheets and screens following Android 15/16 standards.

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
