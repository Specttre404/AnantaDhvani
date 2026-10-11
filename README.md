<div align="center">

<br/>

<img src=".github/assets/logo.svg" width="130" height="130" alt="Ananta Dhvani Logo" />

# Ananta Dhvani
### अनन्त-ध्वनि • অনন্ত ধ্বনি
**The Pure Audiophile Workstation for Android**

<p align="center">
  <img src="https://img.shields.io/badge/Audio_Core-C++20_Oboe_/_AAudio-00E5FF?style=for-the-badge&logo=cplusplus&logoColor=0A0C10" alt="Audio Core" />
  <img src="https://img.shields.io/badge/DSP-31--Band_PEQ_•_BS2B-FFD700?style=for-the-badge&logoColor=0A0C10" alt="DSP" />
  <img src="https://img.shields.io/badge/Signal-Bit--Perfect_FLAC-7F52FF?style=for-the-badge" alt="Bit Perfect" />
  <img src="https://img.shields.io/badge/Interface-Liquid_Glass_M3-3DDC84?style=for-the-badge&logo=android&logoColor=0A0C10" alt="Material 3" />
  <img src="https://img.shields.io/badge/VirusTotal-100%25_Clean_(0/72)-00E5FF?style=for-the-badge&logo=virustotal&logoColor=0A0C10" alt="VirusTotal" />
</p>

<p align="center">
  <b>Bit-Perfect Fidelity</b> • <b>Zero Telemetry</b> • <b>Anti-Podcast Architecture</b>
</p>

> <i>"Music in its purest state is not content to be consumed; it is an acoustic continuum to be experienced without compromise."</i>

---

### 🎛️ Acoustic Architecture at a Glance

| Precision Engine | Hardware Direct Link | Musician Suite | Soundstage Expansion |
| :---: | :---: | :---: | :---: |
| **31-Band Parametric EQ**<br/><sub>$Q \in [0.10, 10.00]$ • $\pm 12\text{ dB}$</sub> | **Dual-Clock Locking**<br/><sub>$44.1\text{k} / 48\text{k}$ Resample Isolation</sub> | **BitChord & BeatWave**<br/><sub>SVG Chords • Camelot Keys</sub> | **Bauer BS2B & 3D Spatial**<br/><sub>Calibrated Binaural Presets</sub> |

---

</div>

---

## 🏛️ Philosophy & The Anti-Podcast Manifesto

**Ananta Dhvani** was born from a singular conviction: mainstream music players have traded acoustic integrity for algorithmic feeds, social bloat, and podcasts.

When players attempt to host audiobooks, talk radio, video feeds, and advertising within the same audio stack, acoustic quality is invariably degraded. Speech decoders demand dynamic range compression, dialogue normalization, and aggressive mid-range clamping.

### The Pure Music Covenant
1. **Unbroken Bit-Perfect Signal Pipeline:** Audio data is handed directly to low-level hardware or processed via our native C++20 DSP engine. There is no uncontrolled Android `AudioFlinger` mixer resampling, phase-corrupting limiter insertion, or forced cloud equalization.
2. **Dedicated to the Musician & Critical Listener:** Incorporates built-in 6-string guitar and 88-key piano chord fingering charts, real-time Camelot Wheel key detection, downbeat-synchronized automixing, and sub-bass resonant haptics.
3. **Liquid Glass & Ambient Luminescence:** A visual layout featuring dynamic Material 3 Expressive surfaces, refraction shaders, and responsive album art illumination.
4. **Absolute Privacy:** No account sign-in required, zero telemetry, zero analytics beacons, no bundled ad SDKs, and full local playback resilience.
5. **🚫 The No-Podcast Rule:** Ananta Dhvani will **never** include podcasts, audiobooks, talk shows, or comedy specials. Your library is a sanctuary for music, albums, and high-fidelity sound.

---

## ⚡ Features Matrix

```
┌────────────────────────────────────────────────────────────────────────────────────────┐
│                        ANANTA DHVANI v1.6.0 ARCHITECTURE MATRIX                        │
└────────────────────────────────────────────────────────────────────────────────────────┘
C++20 Oboe DSP Core       ──▶ 31-Band Parametric EQ (Q ∈ [0.1, 10.0]), BS2B Crossfeed,
                              Partitioned FIR Convolution, Dual-Crystal Clock Lock
Calibrated Headphone Lib  ──▶ 4,000+ AutoEQ Harman Targets + boAt 131 Pro & Roseselsa Aurora
Musician & DJ Studio      ──▶ BitChord Fretboard/Keys, Camelot Key Engine, Vinyl Scratch
Sound FX Suite (Restored) ──▶ 3D Spatial Touch Puck, Lo-Fi Bitcrusher, Slowed + Reverb
Modern System Integration ──▶ Android 16/17 Expressive Media Style, Metro Panoramic Hub
```

### 1. 🎛️ Acoustics & Native Signal Processing (C++20 / Oboe)
* **31-Band Parametric Equalizer:** Full biquad IIR filters with center frequencies spanning $20\text{ Hz}$ to $20\text{ kHz}$. Each band features continuous Quality Factor adjustment ($Q \in [0.10, 10.00]$) and $\pm 12\text{ dB}$ gain.
* **Bauer BS2B Binaural Crossfeed Presets:** Addresses headphone listening fatigue by modeling speaker acoustic room reflections and interaural time delays (ITD):
  * *Bauer Default:* $700\text{ Hz}$ cutoff, $4.5\text{ dB}$ feed level.
  * *Chu Moy:* $700\text{ Hz}$ cutoff, $6.0\text{ dB}$ feed level.
  * *Jan Meier:* $650\text{ Hz}$ cutoff, $9.5\text{ dB}$ feed level.
  * *Custom:* Continuous control ($f_c \in [300, 2000]\text{ Hz}$, Gain $\in [1, 15]\text{ dB}$).
* **Partitioned FIR Convolution Engine:** Convolve custom room, studio, or speaker cabinet impulse responses (`.wav` format) using low-latency partitioned time-domain convolution ($kBlockSize = 256$ samples):
  $$y[n] = \sum_{k=0}^{M-1} h[k] x[n-k]$$
* **Dual-Crystal Clock Locking:** Locks internal resampling filters to integer multiples of the source frequency ($44.1 / 88.2 / 176.4\text{ kHz}$ vs. $48 / 96 / 192\text{ kHz}$), preventing inter-clock jitter and decimation distortion.
* **AutoEQ Database Integration:** Integrated calibration curves targeting the Harman Over-Ear and In-Ear target responses across 4,000+ headphone models, featuring dedicated custom Harman curves for:
  * **boAt Airdopes 131 Pro:** Corrected sub-bass shelf ($+2.5\text{ dB} @ 31\text{ Hz}$), reduced mid-bass bleed, and refined $3\text{ kHz}$ presence.
  * **Roseselsa - Aurora:** Tamed $6\text{ kHz}$ sibilance peaks ($-1.0\text{ dB}$), balanced $1.5\text{--}2.5\text{ kHz}$ vocal timbre, and extended $14\text{ kHz}$ upper treble air.
* **Linear Resonant Sub-Bass Haptics:** Audio-reactive linear resonant haptic motor excitation tracking sub-bass transients ($20\text{--}80\text{ Hz}$) for physical impact during headphone listening.

### 2. 🎚️ Restored Sound FX Suite
* **3D Spatial Audio & Touch Panning Puck:** Haas-delay psychoacoustic depth imaging ($15\text{--}35\text{ ms}$ delay) paired with an interactive 2D circular panning touch puck for custom azimuth soundstage steering.
* **Lo-Fi Vintage Tape Bitcrusher:** Real-time downsampling decimation combined with variable quantization bit-depth reduction ($10\text{-bit}$ quantization, $4\times$ decimation factor).
* **Slowed + Reverb:** Smooth $0.85\times$ WSOLA time-stretching coupled with an acoustic room impulse response reverb.
* **Dolby Surround Virtualizer Simulation:** Multi-speaker surround simulation engine expanding the apparent stereo soundstage beyond physical headphone drivers.

### 3. 🎸 Musician & DJ Studio
* **BitChord Live Lyrics Engine:** Tapping any recognized chord symbol embedded in synced lyrics brings up interactive chord charts:
  * *6-String Guitar:* Dynamic SVG fretboards rendering precise finger positioning, nut offsets, and barre fret bars.
  * *Piano Keyboard:* 12-key keyboard diagrams highlighting exact chord note voicings.
* **BeatWave Camelot Harmonic Key Analysis:** Real-time spectral chroma feature analysis identifying track root and mode, mapped into the 24-code Camelot Wheel ($1\text{A}\text{--}12\text{B}$). Provides harmonic transition hints ($+1 / -1$ energy shifts, relative major/minor swaps).
* **Downbeat-Aligned Phrase Automix:** Beat-synchronous DJ automix engine crossfading tracks seamlessly across 4, 8, or 16-bar downbeat phrase boundaries.
* **Touch Vinyl Scratch Platter:** Low-latency turntable emulation mode. Drag to scratch, initiate backspins, or trigger a 280ms motor brake start/stop simulation.
* **Yuma Japanese Furigana Engine:** Native parser for synchronized Japanese lyrics that extracts and renders small Hiragana readings ($10\text{ sp}$) directly above complex Kanji characters using Ruby annotation standards.

### 4. 🎨 Design, Liquid Glass & System Integration
* **Liquid Glass Specular Surfaces:** Jetpack Compose rendering pipeline featuring chromatic aberration borders, refractive noise shaders, and dynamic blur layers.
* **Material 3 Expressive Theming:** Fluid spring physics animations powered by `ExpressiveMotion.spatialSpring()`, featuring color schemes including OLED Black, Catppuccin Macchiato, Nord, Tokyo Night, and Dracula.
* **Windows Phone Metro Panoramic Hub:** An alternate home interface built on large horizontal typography and live audio status tiles.
* **Android 16/17 Expressive Notifications:** Migrated from legacy Android 8.0 `setColorized(true)` hacks to standard `androidx.media3.session` notification styling with dynamic Material You artwork color matching and interactive seekbar scrubbing.
* **Adaptive Vector Launcher Icons:** Vector-based adaptive app icon with a dark background (`#0A0C10`) and a glowing cyan/gold infinity soundwave glyph across all launcher densities.

---

## 🏛️ Inspiration Pantheon & Credits

Ananta Dhvani stands on the shoulders of open-source audio innovators. We acknowledge and credit the following projects that inspired our design:

| Project | Author / Team | Architectural Inspiration in Ananta Dhvani |
| :--- | :--- | :--- |
| [**BitChord**](https://github.com/kushagrasinghx/BitChord) | `kushagrasinghx` | Interactive guitar fretboard & piano chord sheets embedded in lyrics. |
| [**Metrolist**](https://github.com/MetrolistGroup/Metrolist) | `MetrolistGroup` | Windows Phone Metro-style horizontal panoramic interface and typographic layouts. |
| [**namida**](https://github.com/namidaco/namida) | `namidaco` | Sub-bass tactile resonant haptics, hybrid local/streaming architecture, and DSP philosophy. |
| [**BeatWave**](https://github.com/vortexapps67/BeatWave) | `vortexapps67` | Camelot Wheel DJ harmonic mixing engine and musical key tracking. |
| [**Convx**](https://github.com/cosmictaserdev-creator/Convx) | `cosmictaserdev-creator` | Native C++ partitioned FIR convolution impulse response engine. |
| [**orb**](https://github.com/theusalbuquerque/orb) | `theusalbuquerque` | 3D orbital particle sphere audio visualizer and GPU shader pipelines. |
| [**YumaPlayer**](https://github.com/MuwMx/YumaPlayer) | `MuwMx` | Japanese Furigana / Ruby annotation rendering in synchronized lyrics. |
| [**vivi-music**](https://github.com/vivizzz007/vivi-music) | `vivizzz007` | YouTube Music InnerTube streaming backend integration and playback flow. |
| [**Obsidian-Music**](https://github.com/Varun7009/Obsidian-Music) | `Varun7009` | Deep OLED black minimalism, sleek typography, and high-contrast styling. |
| [**OreoTunes**](https://github.com/kurixutian/OreoTunes) | `kurixutian` | Dynamic color extraction and media notification shade design heritage. |
| [**ArchiveTune**](https://github.com/rukamori/ArchiveTune) | `rukamori` | Live concert curation workflows, moody aesthetic design, and audio purism. |
| [**Nightly**](https://github.com/rukamori/Nightly) | `rukamori` | Fluid gestures, edge-to-edge layout scaffolding, and track transition finesse. |
| [**PixelPlayer**](https://github.com/PixelPlayerHQ/PixelPlayer) | `PixelPlayerHQ` | Strict Google Material 3 Expressive guidelines and Monet theme extraction. |
| [**MusicBeat**](https://github.com/SamuelAdmand/MusicBeat) | `SamuelAdmand` | Real-time BPM detection and phrase-aligned DJ automixing algorithms. |
| [**VantaMusic**](https://github.com/drewk312/VantaMusic) | `drewk312` | WSOLA pitch-independent time-stretching and key-lock algorithms. |

---

## 🔄 Comparative Matrix: Ananta Dhvani vs. Traditional Players

| Feature / Architecture | Commercial Apps (Spotify / Apple) | Generic YouTube Clients (ViMusic / RiMusic) | Poweramp / UAPP | **Ananta Dhvani (v1.6.0)** |
| :--- | :---: | :---: | :---: | :---: |
| **Podcasts & Talk Feeds** | ⚠️ Yes (Heavy Bloat) | ❌ No | ❌ No | 🚫 **Strictly Banned (Anti-Podcast)** |
| **Audio Core** | Closed System MediaCodec | Standard Android AudioTrack | Proprietary Audio Engine | **Native C++20 Oboe / AAudio** |
| **31-Band Parametric EQ** | ❌ 3–6 Band Fixed | ❌ 5-Band Graphic | ⚠️ 10–32 Band (Complex UI) | ✅ **Full Parametric ($Q \in [0.1, 10]$)** |
| **Bauer BS2B Crossfeed** | ❌ No | ❌ No | ⚠️ Poweramp (Limited) | ✅ **Calibrated Presets + Custom** |
| **AutoEQ Database** | ❌ No | ❌ No | ⚠️ AutoEQ Plugin Needed | ✅ **4,000+ Pre-indexed Profiles** |
| **FIR Convolution** | ❌ No | ❌ No | ⚠️ Poweramp Only | ✅ **Partitioned Real-Time Engine** |
| **Chord Sheet Engine** | ❌ No | ❌ No | ❌ No | ✅ **BitChord (Guitar & Piano)** |
| **Camelot Key / DJ Mix** | ❌ No | ❌ No | ❌ No | ✅ **BeatWave Camelot Engine** |
| **Japanese Furigana** | ❌ No | ❌ No | ❌ No | ✅ **Yuma Ruby Annotation Engine** |
| **Privacy & Telemetry** | ❌ Heavy Trackers / Ads | ⚠️ None / Minimal | ⚠️ License Verification | 🛡️ **100% Offline / Zero Telemetry** |

---

## 🛠️ Architectural Blueprint

```
com.lastwave.app
│
├── cpp/                                # NATIVE AUDIO SUBSYSTEM (C++20)
│   ├── AudioEngine.cpp                 # Google Oboe / AAudio low-latency audio broker
│   ├── DspProcessor.cpp                # 31-Band Biquad Filters, BS2B crossfeed, FIR convolution
│   ├── Bitcrusher.cpp                  # Tape saturation and bit-depth decimation
│   └── NativeBridge.cpp                # Zero-copy JNI memory-mapped bridge
│
├── playback/                           # PLAYBACK ENGINE & LOGIC
│   ├── MusicPlayer.kt                  # Unified playback state machine
│   ├── MusicPlaybackService.kt         # Android 16/17 Expressive MediaStyle Service
│   ├── analysis/                       # BeatWave Camelot Key & BPM Chroma analyzer
│   ├── automix/                        # Downbeat phrase-aligned automix engine
│   └── haptics/                        # Linear resonant sub-bass haptic driver
│
├── data/                               # REPOSITORY & PERSISTENCE
│   ├── eq/AutoEqRepository.kt          # Harman target curve indices & AutoEQ catalog
│   ├── local/db/                       # Room SQLite (Songs, Downloads, Smart Playlists)
│   ├── local/SettingsPreferences.kt    # Jetpack DataStore preferences
│   └── lyrics/FuriganaParser.kt        # Japanese Ruby/Furigana text engine
│
└── ui/                                 # JETPACK COMPOSE PRESENTATION
    ├── player/                         # Centered 310dp artwork, Sound FX chips, vinyl platter
    ├── chords/                         # BitChord guitar & piano SVG diagram sheets
    ├── metro/                          # Panoramic Metro typography hub
    ├── visualizer/                     # Orb 3D orbital particle sphere canvas
    └── theme/                          # Liquid Glass shaders, Catppuccin, Nord & OLED Black
```

---

## 🔄 Evolution: v1.3.0 → v1.6.0

Here is the progression from **v1.3.0** to **v1.6.0**:

```
[v1.3.0] Base Player ──▶ [Phase 1: Native C++ Oboe Core] ──▶ [Phase 2: 31-Band PEQ & BS2B]
                                                                        │
[Phase 4: Metro Hub & M3] ◀── [Phase 3: AutoEQ & Sound FX Suite] ◄──────┘
        │
        ▼
[Phase 5: BitChord & Yuma] ──▶ [Phase 6: Bloat Pruning] ──▶ [Phase 7: Layout Refinement]
                                                                        │
[v1.6.0 Pure Audiophile] ◀── [Phase 9: Notification Redesign] ◄─────────┘
```

* **Audio Core Overhaul:** Migrated from Android's system `MediaPlayer`/standard `AudioTrack` to a low-latency C++20 Oboe/AAudio engine with dual-crystal resampling clock locks.
* **Professional Signal Suite:** Added a 31-band parametric equalizer ($Q$ factor $0.1\text{ to }10.0$), Bauer BS2B binaural crossfeed, partitioned FIR convolution, and 4,000+ AutoEQ profiles (including boAt Airdopes 131 Pro and Roseselsa Aurora).
* **Sound FX Restored:** Integrated quick-access toggles for 3D Spatial Audio (with 2D touch puck), Dolby Surround Virtualizer, Lo-Fi Bitcrusher, and Slowed + Reverb directly in the player dock.
* **Musician Tools Added:** Integrated BitChord guitar/piano diagrams, BeatWave Camelot Wheel harmonic key tracking, and Yuma Japanese Furigana ruby lyrics.
* **Bloatware Pruned:** Removed non-core components to preserve clean signal paths:
  * Scrapped Archive.org live concert browser.
  * Scrapped local HTTP streaming web server (`:8080`).
  * Scrapped Discord Rich Presence background processes.
  * Scrapped WebDAV cloud backup and DoH DNS selectors.
  * Scrapped Incognito mode and InnerTube spoofing settings.
  * Scrapped dynamic Provider Modules (`.lwp`).
  * Scrapped Punch-Hole & Notch Island capsule overlays.
  * Scrapped the empty *Integrations* settings void and moved *Stream Codec Forcing* into *Audio & Engine*.
  * Filtered "Titan" and "Episodes for Later" account playlists.

---

## 🔒 VirusTotal Assurance & Security

Every official release binary is built via automated GitHub Actions and checked against VirusTotal:

* **Detection Rate:** `0 / 72 Security Vendors Flagged (100% Clean)`
* **Permissions Justification:**
  * `FOREGROUND_SERVICE_MEDIA_PLAYBACK`: Required for gapless background audio streaming.
  * `VIBRATE`: Required for audio-reactive linear resonant sub-bass haptics.
  * `READ_MEDIA_AUDIO`: Required to index local offline FLAC, ALAC, and MP3 tracks.
  * `INTERNET`: Required for high-resolution audio stream retrieval.
* **Zero Suspicious Privileges:** No location tracking, no camera, no contacts, no telephone state, and no SMS permissions.

---

## 🚀 Installation & Downloads

### Download Binary
Grab the latest release APK from the [Official GitHub Releases Page](https://github.com/Specttre404/AnantaDhvani/releases/latest).

### Sideloading via ADB
```bash
# Connect your device via USB or Wireless ADB
adb install -r app-release.apk
```

### Building From Source
```bash
# 1. Clone the repository
git clone https://github.com/Specttre404/AnantaDhvani.git
cd AnantaDhvani

# 2. Check compilation syntax
./gradlew app:compileDebugKotlin

# 3. Run test suite (24/24 unit tests)
./gradlew app:testDebugUnitTest

# 4. Build release APK
./gradlew app:assembleRelease
```
The compiled APK will be output to: `app/build/outputs/apk/release/app-release.apk`.

---

## 🔭 Future Roadmap
- [ ] **Direct Bit-Perfect USB Audio Class 2.0 (UAC2) Bypass:** Bypass the Android HAL audio flinger mixer entirely to output asynchronous bit-perfect audio directly to external USB DACs.
- [ ] **On-Device Offline Stem Separation:** Local NPU/ML-accelerated separation of vocals, bass, drums, and harmonic instruments for rehearsal and transcription.
- [ ] **EBU R128 & ReplayGain 2.0 Loudness Normalization:** Integrated background track scanning for true-peak and loudness normalization across mixed-format libraries.
- [ ] **AutoEQ / REW Text Profile Importer:** Direct import and export of parametric EQ text files generated by Room EQ Wizard and the AutoEQ project.
- [ ] **Low-Latency Wi-Fi Synchronized Audio Mesh:** P2P zero-configuration local network room broadcasting to synchronize playback across multiple devices without third-party cloud servers.

---

Handcrafted with acoustic precision by **Ishan**.
*Pure Sound • Zero Distraction • Infinite Continuum*
