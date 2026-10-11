# Changelog

All notable changes to Ananta Dhvani (अनन्त-ध्वनि / অনন্ত ধ্বনি) are documented here.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/), and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [1.6.0] - 2026-10-11

### Pure Audiophile Edition — Complete Architectural Overhaul

This release completes the transformation of the codebase from a lightweight scrobbler client into a high-performance, native audiophile music workstation.

#### Added
- **C++20 Oboe / AAudio Native DSP Engine:** Low-latency floating-point audio processing pipeline operating in direct hardware output mode.
- **31-Band Parametric Equalizer:** Individual biquad IIR filters with adjustable Q-factors ($0.10\text{--}10.00$) and $\pm 12\text{ dB}$ gain per band.
- **Bauer BS2B Crossfeed Processor:** Implemented Bauer Default, Chu Moy, Jan Meier, and Custom presets to eliminate headphone acoustic fatigue.
- **AutoEQ Headphone Database:** Pre-indexed over 4,000 calibrated Harman Target profiles, with custom-tuned profiles for boAt Airdopes 131 Pro and Roseselsa - Aurora.
- **Partitioned FIR Convolution:** Real-time convolution of custom studio and speaker cabinet impulse responses (.wav).
- **Dual-Crystal Clock Locking:** Hardware clock locking to eliminate jitter across $44.1\text{ kHz}$ and $48\text{ kHz}$ sample-rate families.
- **Restored Sound FX Suite:** Dedicated interactive chips in the player dock for:
  - 3D Spatial Audio (Haas delay with 2D touch puck steering)
  - Dolby Surround Virtualizer (Acoustic expansion simulation)
  - Lo-Fi Tape Machine (10-bit bitcrusher and $4\times$ decimation filter)
  - Slowed + Reverb ($0.85\times$ speed modulation with room reverberation)
- **BitChord Musician Tools:** Interactive 6-string guitar fretboard and 12-key piano keyboard diagrams on lyrics chord taps.
- **BeatWave Harmonic Key Engine:** Real-time Camelot Wheel key detection ($1\text{A}\text{--}12\text{B}$) with harmonic mix recommendations.
- **Phrase-Aligned DJ Automix:** Beat-synchronized downbeat transitions across 4, 8, or 16-bar phrases.
- **Touch Vinyl Scratch Platter:** Turntable platter view with interactive scratching, backspins, and motor brake simulation.
- **Yuma Japanese Furigana Engine:** Ruby text rendering of Hiragana readings directly above Kanji characters.
- **Sub-Bass Resonant Haptics:** Audio-reactive linear resonant motor transducer excitation ($20\text{--}80\text{ Hz}$).
- **Windows Phone Metro Panoramic Hub:** High-contrast typographic home screen with dynamic live tiles.
- **Android 16/17 Expressive Notifications:** Standard `NotificationCompat.MediaStyle` with dynamic Material You artwork palette extraction and real-time seekbar scrubbing.
- **Quick Settings Custom Tiles:** System shade tiles for Equalizer, Playback Speed, and Favorite Track toggles.
- **Screen-Off Battery Guard:** Automatically halts GPU visualizers and throttles background FFTs when the display sleeps.

#### Changed & Refined
- **Player UI / UX Redesign:** Replaced the small avatar and floating card with a centered, $310\text{ dp}$ album art stage featuring ambient radial glow, marquee titles, and a unified 5-button transport dock.
- **Settings Tab Alignment:** Resolved tab-page misalignment; moved `StreamCodecCard` under *Audio & Engine* with `.wrapContentHeight()`, eliminating the empty gray void.
- **Charts Screen Simplification:** Removed the segmented Top 100 / Top 10 toggle pill row; fixed lists to the Top 10 Global and Top 10 India charts.
- **Search Bar Accessories:** Removed duplicate microphone icons, leaving one Voice Search Mic and one Acoustic Identification button.
- **App Launcher Icons:** Adaptive icons mapped to `#0A0C10` background and cyan/gold soundwave vector foreground across all densities.
- **Audiophile Palettes:** Replaced the horizontally clipping box with an auto-wrapping `FlowRow`.

#### Removed (Bloat Pruning)
- **Anti-Podcast Policy Enforced:** Completely filtered "Titan", "Episodes for Later", and podcast feeds across all playlist providers.
- Scrapped Archive.org live concert browser.
- Scrapped Local HTTP streaming web server (`:8080`).
- Scrapped Discord Rich Presence background services and token listeners.
- Scrapped Encrypted WebDAV cloud sync and DoH provider selectors.
- Scrapped Incognito mode and InnerTube spoofing settings.
- Scrapped dynamic Provider Modules (`.lwp`).
- Scrapped Punch-Hole & Notch Island capsule overlays.
- Scrapped the standalone Stats tab from the main navigation bar.

---

## [1.3.0] - 2026-09-18

### Added
- Room SQLite database play stats tracking (`SongPlayStatsDao`).
- Initial Local recommendation engine prototype.
- Initial Last.fm background scrobbling pipeline.
