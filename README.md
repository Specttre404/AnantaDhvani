# Ananta Dhvani (अनन्त-ध्वनि / অনন্ত ধ্বনি)
## Pure Audiophile Music Workstation for Android

[![Android](https://img.shields.io/badge/Android-35+-00E5FF.svg?style=for-the-badge&logo=android&logoColor=black)](https://developer.android.com/)
[![Kotlin](https://img.shields.io/badge/Kotlin-2.1.10-7F52FF.svg?style=for-the-badge&logo=kotlin&logoColor=white)](https://kotlinlang.org/)
[![C++20](https://img.shields.io/badge/C++-20_Oboe_DSP-00599C.svg?style=for-the-badge&logo=cplusplus&logoColor=white)](https://github.com/google/oboe)
[![License](https://img.shields.io/badge/License-MIT-FFD700.svg?style=for-the-badge)](LICENSE)

Ananta Dhvani (*Infinite Sound*) is a pure, zero-compromise high-fidelity music workstation engineered specifically for audiophiles, musicians, and critical listeners on Android.

---

## 💎 Philosophy & Manifesto

1. **Bit-Perfect Signal Fidelity**: Direct SoC USB DAC hardware passthrough bypassing Android's `AudioFlinger` resampler.
2. **Musician & DJ First Utility**: Real-time 31-band parametric equalization, Camelot Wheel harmonic key analysis, interactive chord diagrams, and phrase-aligned automixing.
3. **Liquid Glass Aesthetic**: Expressive Material 3 UI with dynamic glassmorphism and ambient album artwork color bleeding.
4. **Zero Telemetry & Absolute Privacy**: No trackers, no ad SDKs, no bloatware, and zero background analytics.

---

## 🚫 Explicit No-Podcast Policy

Ananta Dhvani is strictly a **music reproduction engine**.
- **No Podcasts**: Talk feeds, speech compression algorithms, and voice-activation DSP conflict with music dynamics.
- **No Audiobooks**: Zero playback rate voice warping or chapter bookmark clutter.
- **Zero Feed Clutter**: No promoted talk shows, video interviews, or news feeds.

---

## ⚡ Key Features Matrix

### 🎛️ Audiophile DSP & Engine
- **31-Band Parametric Equalizer**: Precision IIR biquad filters with adjustable Q-factor (0.1 to 10.0) and ±12 dB gain per band.
- **Bauer BS2B Headphone Crossfeed**: Calibrated acoustic room simulation profiles (*Bauer Default*, *Chu Moy*, *Jan Meier*) to eliminate headphone listening fatigue.
- **4,000+ AutoEQ Profiles**: Integrated Harman Target headphone curves, featuring custom profiles for **boAt Airdopes 131 Pro** and **Roseselsa - Aurora**.
- **FIR Impulse Response Convolution**: Real-time room and speaker cabinet acoustic simulation via `.wav` IR profiles.
- **Studio Sound FX Suite**:
  - **3D Spatial Audio**: Haas delay and stereo azimuth widening.
  - **Dolby Virtualizer**: Multi-channel acoustic expansion.
  - **Lo-Fi Tape Machine**: Vintage bitcrusher and downsampling.
  - **Slowed + Reverb**: 0.85x speed modulation with room reverberation.

### 🎧 BeatWave & DJ Automation
- **Harmonic Key & BPM Engine**: Real-time Chroma feature extraction mapping songs to 24 Camelot Wheel key codes (`1A`–`12B`) with harmonic transition recommendations.
- **Phrase-Aligned DJ Automix**: Downbeat-aligned energy crossfading over 4, 8, or 16 bars with dynamic tempo matching.
- **Vinyl Scratch Mode**: Touch-interactive turntable platter overlay enabling real-time scratching, backspins, and motor brake recovery.

### 🎸 BitChord & Yuma Lyrics
- **Interactive Chord Diagram Sheets**: Tapping any chord token in lyrics displays 6-String Guitar Fretboards and 12-Key Piano Keyboards.
- **Japanese Furigana & Ruby Annotations**: Renders Kanji readings in smaller 10sp text directly above Kanji characters with toggle support.

### 📱 Material 3 & System Integration
- **Metro Panoramic Hub**: Alternative Windows Phone Metro-inspired horizontal scrolling interface.
- **Android 16/17 Expressive Media Notifications**: Material You dynamic palette matching with interactive seekbar scrub progress.
- **Quick Settings Tiles**: Notification shade tiles for Equalizer, Playback Speed, and Favorite Track toggles.

---

## 📐 Architecture & Project Structure

```
AnantaDhvani/
├── app/src/main/cpp/                # Native Audio Engine (C++20 & Oboe)
│   ├── AudioEngine.cpp              # Dual-instance Oboe & Media3 DSP router
│   ├── DspProcessor.cpp             # 31-band EQ, BS2B, Biquad & Convolution
│   └── NativeBridge.cpp             # High-performance JNI bindings
├── app/src/main/java/com/lastwave/app/
│   ├── data/
│   │   ├── eq/                      # AutoEQ database & Harman profiles
│   │   ├── local/                   # Room DB & DataStore preferences
│   │   └── music/                   # InnerTube Music API client
│   ├── playback/
│   │   ├── analysis/                # HarmonicKeyAnalyzer (Camelot Wheel)
│   │   ├── automix/                 # AutomixEngine
│   │   └── MusicPlayer.kt           # Central playback controller
│   └── ui/
│       ├── charts/                  # Top 10 Ranked Charts
│       ├── metro/                   # Metro Panoramic Hub Interface
│       ├── player/                  # Full Player, Lyrics & Chords
│       │   ├── chords/              # ChordDiagramSheet (Guitar/Piano)
│       │   ├── spatial/             # SpatialPanningPuckView
│       │   └── vinyl/               # TurntablePlatterView
│       ├── settings/                # SettingsScreen & Audio Engine
│       └── visualizer/              # 3D Orbital Particle Sphere
```

---

## 🛠️ Building & Verification

### Prerequisites
- Android Studio Ladybug / 2024.2+
- Android SDK 35 & NDK 26+
- JDK 17 / Kotlin 2.1.10

### Verification Commands
```bash
# 1. Verify Kotlin Compilation
./gradlew app:compileDebugKotlin

# 2. Run Unit Test Suite (24/24)
./gradlew app:testDebugUnitTest

# 3. Assemble Release APK
./gradlew app:assembleRelease
```

---

## 👤 Credits

Crafted with passion by **Ishan**.
