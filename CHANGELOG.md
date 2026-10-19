# Changelog

All notable changes to Ananta Dhvani (अनन्त-ध्वनि / অনন্ত ধ্বনি) are documented here.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/), and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [1.6.0-HDR] - 2026-10-24

### Finalized Production Release — Pure High-Performance Audiophile Workstation

This release finalizes the Ananta Dhvani architecture with native C++20 signal processing, iOS Liquid Glass surfaces, Material 3 Expressive motion physics, and clean Light/Dark mode contrast.

#### Added & Upgraded
- **iOS Liquid Glass Engine (Lelonio/Square):** Dual-layer vertical background gradients, hardware `RenderEffect.createBlurEffect`, and specular reflection borders on mini-player, bottom sheets, and cards.
- **Material 3 Expressive Motion Tokens (vivi-music):** Bouncy spring physics (`dampingRatio = 0.68f`, `stiffness = 380f`) and tactile press-scale modifiers on all interactive buttons.
- **HRTF 3D Spatial Radar Puck:** Interactive 2D soundstage touch puck with live azimuth orientation, room volume, and elevation dragging.
- **Real-Time Stereo Vectorscope & Oscilloscope:** Live 2D Lissajous XY phase scope and time-domain acoustic waveform display.
- **Yuma Japanese Furigana Engine:** CJK Hiragana Ruby readings rendered above Kanji in synchronized lyrics.
- **Calibrated Headphone Catalog:** Pre-indexed Harman target curves for 25 major headphone lines including boAt, OnePlus, Sony, Sennheiser, Audio-Technica, Moondrop, Apple, Samsung, JBL, KZ, 7Hz, Beyerdynamic, and Bose.
- **Pure Monochrome 1-Bit E-Ink Mode:** Zero-blur daylight paper mode for high-contrast sunlight readability.

#### Fixed & Refined
- **Equalizer Quick Presets Sync:** Tapping `Flat`, `Bass Boost`, `Vocal Enhancer`, `Rock`, `Pop`, or `Electronic` automatically enables EQ, updates active preset highlighting in neon cyan (`#00F0FF`), and syncs the bass boost slider.
- **Live ID3 Tag Metadata Update:** Saving edited tags in `TagEditorSheet` immediately updates the live Now Playing screen, notification shade, and chrome state.
- **Phrase Looper Cooldown:** Added a $600\text{ ms}$ seek cooldown and $3500\text{ ms}$ minimum bar duration to eliminate ExoPlayer buffering micro-loops.
- **Light Mode Directory Contrast:** Updated directory codec badges to `MaterialTheme.colorScheme.primary` for deep cobalt readability in Light Mode.
- **Splash Screen Transition:** Mapped `splash_background` to void dark (`#020408`) with centered `@drawable/ic_launcher_logo` to eliminate trapped background boxes.

#### Scrapped & Pruned
- Permanently removed DJ Studio workstation, hot cue pads (`C1–C4`), and phrase loopers.
- Permanently removed AI recommendation generators, AI playlist auto-mixers, and MusicBrainz Auto-Tagger.
- Permanently removed Chromecast menu options and Block Artist / Mute Track rows.

---

## [1.6.0] - 2026-10-11

### Pure Audiophile Edition — Complete Architectural Overhaul

- **C++20 Oboe / AAudio Native DSP Engine:** Low-latency floating-point audio processing pipeline operating in direct hardware output mode.
- **31-Band Parametric Equalizer:** Individual biquad IIR filters with adjustable Q-factors ($0.10\text{--}10.00$) and $\pm 12\text{ dB}$ gain per band.
- **Bauer BS2B Crossfeed Processor:** Implemented Bauer Default, Chu Moy, Jan Meier, and Custom presets.
- **Sub-Bass Resonant Haptics:** Audio-reactive linear resonant motor transducer excitation ($20\text{--}80\text{ Hz}$).
