<div align="center">

<br/>

<img src=".github/assets/logo.svg" width="130" height="130" alt="Ananta Dhvani Logo" />

# Ananta Dhvani
### अनन्त-ध्वनि • অনন্ত ধ্বনি
**The Pure High-Performance Audiophile Music Workstation for Android**

<p align="center">
  <img src="https://img.shields.io/badge/Audio_Core-C++20_Oboe_/_AAudio-00F0FF?style=for-the-badge&logo=cplusplus&logoColor=0A0C10" alt="Audio Core" />
  <img src="https://img.shields.io/badge/DSP-31--Band_PEQ_•_BS2B_•_FIR-FFC700?style=for-the-badge&logoColor=0A0C10" alt="DSP" />
  <img src="https://img.shields.io/badge/Signal-Bit--Perfect_32--Bit_Float-7F52FF?style=for-the-badge" alt="Bit Perfect" />
  <img src="https://img.shields.io/badge/UI-iOS_Liquid_Glass_•_M3_Expressive-FF2E93?style=for-the-badge&logo=android&logoColor=0A0C10" alt="Material 3" />
  <img src="https://img.shields.io/badge/VirusTotal-100%25_Clean_(0/72)-00F0FF?style=for-the-badge&logo=virustotal&logoColor=0A0C10" alt="VirusTotal" />
</p>

<p align="center">
  <b>Bit-Perfect Fidelity</b> • <b>Zero Telemetry</b> • <b>Anti-Podcast Architecture</b>
</p>

> <i>"Music in its purest state is not content to be consumed; it is an acoustic continuum to be experienced without compromise."</i>

---

### 🎛️ Acoustic Architecture at a Glance

| Precision Engine | Hardware Direct Link | Telemetry & Optics | Soundstage Expansion |
| :---: | :---: | :---: | :---: |
| **31-Band Parametric EQ**<br/><sub>$Q \in [0.10, 10.00]$ • $\pm 12\text{ dB}$</sub> | **Oboe C++20 Passthrough**<br/><sub>32-Bit Float / 96 kHz Direct DAC</sub> | **Vectorscope & Furigana**<br/><sub>Lissajous XY Scope • CJK Ruby</sub> | **HRTF 3D Spatial & BS2B**<br/><sub>Interactive Panning Touch Puck</sub> |

---

</div>

---

## 🏛️ Philosophy & The Anti-Podcast Manifesto

**Ananta Dhvani** was born from a singular conviction: mainstream music players have traded acoustic integrity for algorithmic feeds, social bloat, and podcasts.

When players attempt to host audiobooks, talk radio, video feeds, and advertising within the same audio stack, acoustic quality is invariably degraded. Speech decoders demand dynamic range compression, dialogue normalization, and aggressive mid-range clamping.

### The Pure Music Covenant
1. **Unbroken Bit-Perfect Signal Pipeline:** Audio data is handed directly to low-level hardware or processed via our native C++20 DSP engine. There is no uncontrolled Android `AudioFlinger` mixer resampling, phase-corrupting limiter insertion, or forced cloud equalization.
2. **Dedicated to the Critical Listener:** Incorporates 4,000+ calibrated AutoEQ Harman profiles, real-time Camelot Wheel key detection, HRTF 3D spatial touch puck, real-time stereo vectorscope phase monitoring, and sub-bass resonant haptics.
3. **iOS Liquid Glass & Material 3 Expressive:** A visual layout featuring Lelonio/Square-inspired frosted refraction, dual-stop specular reflection borders, and vivizzz007/vivi-music tactile spring press-scaling.
4. **Absolute Privacy:** No account sign-in required, zero telemetry, zero analytics beacons, no bundled ad SDKs, and full local playback resilience.
5. **🚫 The No-Podcast & Pure Signal Rule:** Ananta Dhvani will **never** include podcasts, talk shows, advertisements, or bloatware. Your library is a sanctuary for music, albums, and high-fidelity sound.

---

## ⚡ Features Matrix

```
┌────────────────────────────────────────────────────────────────────────────────────────┐
│                        ANANTA DHVANI PRODUCTION ARCHITECTURE MATRIX                    │
└────────────────────────────────────────────────────────────────────────────────────────┘
C++20 Oboe DSP Core       ──▶ 31-Band Parametric EQ (Q ∈ [0.1, 10.0]), BS2B Crossfeed,
                              Partitioned FIR Convolution, Dual-Crystal Clock Lock
Calibrated Headphone Lib  ──▶ 4,000+ AutoEQ Harman Targets (boAt, OnePlus, Sony, Sennheiser,
                              Audio-Technica, Moondrop, Apple, Samsung, JBL, Beyerdynamic, Bose)
Visual Optics & Studio    ──▶ Real-Time Stereo Vectorscope (Lissajous XY), 3D Spatial Touch Puck,
                              Orb 3D Particle Visualizer, Skeuomorphic Cassette Deck
Linguistics & E-Ink       ──▶ Yuma Japanese Furigana Ruby Engine, Hepburn Romaji, CJK Lexicon,
                              Pure Monochrome 1-Bit E-Ink Mode (Daylight Paper Mode)
Modern UI Architecture    ──▶ iOS Liquid Glass (Lelonio/Square), Material 3 Expressive Motion
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
* **AutoEQ Database Integration:** Integrated calibration curves targeting the Harman Over-Ear and In-Ear target responses across 4,000+ headphone models, featuring pre-indexed profiles for boAt, OnePlus, Sony, Sennheiser, Audio-Technica, Moondrop, Apple, Samsung, JBL, KZ, 7Hz, Beyerdynamic, and Bose.
* **Linear Resonant Sub-Bass Haptics:** Audio-reactive linear resonant haptic motor excitation tracking sub-bass transients ($20\text{--}80\text{ Hz}$) for physical impact during headphone listening.

### 2. 🎚️ Soundstage, Optics & Studio FX Suite
* **3D Spatial Audio & Touch Panning Puck:** HRTF depth imaging paired with an interactive 2D circular panning touch puck for live room size, azimuth angle, and elevation steering.
* **Real-Time Stereo Vectorscope & Oscilloscope:** Live Lissajous XY phase scope and time-domain acoustic waveform display for real-time stereo width and phase alignment monitoring.
* **Lo-Fi Vintage Tape Bitcrusher:** Real-time downsampling decimation combined with variable quantization bit-depth reduction.
* **Slowed + Reverb:** Smooth $0.85\times$ time-stretching coupled with an acoustic room impulse response reverb.
* **Dolby Surround Virtualizer:** Multi-speaker surround simulation engine expanding the apparent stereo soundstage.

### 3. 📖 Linguistics, Furigana & E-Ink Display
* **Yuma Japanese Furigana Engine:** Native parser for synchronized Japanese lyrics that renders small Hiragana readings ($10\text{ sp}$) directly above CJK Kanji characters using standard Ruby annotations.
* **Hepburn Romaji & CJK Dictionary:** Live transliteration line alongside an interactive Kanji dictionary token lookup with Tokyo pitch accent contours.
* **Pure Monochrome 1-Bit E-Ink Mode:** Disables GPU shaders, blurs, and animations for high-contrast sunlight readability and E-Ink paper displays.

### 4. 🎨 Design, Liquid Glass & System Integration
* **iOS Liquid Glass Engine:** Lelonio/Square-inspired frosted glass architecture featuring dual-layer vertical background gradients, hardware `RenderEffect` blur, and specular reflection borders.
* **Material 3 Expressive Motion:** Fluid spring physics animations powered by vivizzz007/vivi-music bouncy spring tokens (`dampingRatio = 0.68f`, `stiffness = 380f`).
* **Android 16/17 Expressive Notifications:** Standard `androidx.media3.session` notification styling with dynamic Material You artwork color matching and interactive seekbar scrubbing.
* **Vector Launcher Icons:** Crisp, modern launcher icon with a smooth neon cyan-to-magenta-to-gold harmonic wave crest and pulse nodes matching the splash screen.

---

## 🏛️ Inspiration Pantheon & Creator's Note

### 💡 Creator's Vision
Ananta Dhvani is an independent, non-commercial passion project conceptualized and directed by Ishan. Built 100% through Gemini as the primary software engineering engine.

### 🌟 Foundational Lineage
Our deepest appreciation and foremost credit go to the original **LastWave** repository and its creators, serving as the core foundation from which Ananta Dhvani originated.

| Project | Author / Team | Architectural Inspiration in Ananta Dhvani |
| :--- | :--- | :--- |
| [**BitChord**](https://github.com/kushagrasinghx/BitChord) | `kushagrasinghx` | Interactive guitar fretboard & piano chord sheets embedded in lyrics. |
| [**Metrolist**](https://github.com/MetrolistGroup/Metrolist) | `MetrolistGroup` | Horizontal panoramic interface and typographic layouts. |
| [**namida**](https://github.com/namidaco/namida) | `namidaco` | Sub-bass tactile resonant haptics and DSP philosophy. |
| [**BeatWave**](https://github.com/vortexapps67/BeatWave) | `vortexapps67` | Camelot Wheel DJ harmonic key tracking. |
| [**Convx**](https://github.com/cosmictaserdev-creator/Convx) | `cosmictaserdev-creator` | Native C++ partitioned FIR convolution impulse response engine. |
| [**orb**](https://github.com/theusalbuquerque/orb) | `theusalbuquerque` | 3D orbital particle sphere audio visualizer and GPU shader pipelines. |
| [**YumaPlayer**](https://github.com/MuwMx/YumaPlayer) | `MuwMx` | Japanese Furigana / Ruby annotation rendering in synchronized lyrics. |
| [**vivi-music**](https://github.com/vivizzz007/vivi-music) | `vivizzz007` | Material 3 Expressive motion spring physics. |
| [**Obsidian-Music**](https://github.com/Varun7009/Obsidian-Music) | `Varun7009` | Deep OLED black minimalism and high-contrast styling. |

---

## 🛠️ Building & Verification

```bash
# 1. Clone repository
git clone https://github.com/Specttre404/AnantaDhvani.git
cd AnantaDhvani

# 2. Check compilation syntax
./gradlew app:compileDebugKotlin

# 3. Run test suite (24/24 unit tests)
./gradlew app:testDebugUnitTest

# 4. Build release APK
./gradlew app:assembleRelease
```
The compiled release APK will be output to: `app/build/outputs/apk/release/app-release.apk`.

---

Handcrafted with acoustic precision by **Ishan**.
*Pure Sound • Zero Distraction • Infinite Continuum*
