# Changelog

All notable changes to Ananta Dhvani will be documented in this file.

## [1.6.0] - 2026-10-11

### Pure Audiophile Edition Release

#### Added
- **31-Band Parametric EQ**: Precision biquad filters with adjustable Q-factor (0.1–10.0) and ±12 dB gain per band.
- **Bauer BS2B Headphone Crossfeed Presets**: Calibrated acoustic room simulation profiles (*Bauer Default*, *Chu Moy*, *Jan Meier*).
- **Calibrated AutoEQ Profiles**: Added Harman Target headphone curves for **boAt Airdopes 131 Pro** and **Roseselsa - Aurora**.
- **Studio Sound FX Suite**: Restored 3D Spatial Audio, Dolby Virtualizer, Lo-Fi Tape Machine (bitcrusher), and Slowed + Reverb.
- **BeatWave Harmonic Key Engine**: Real-time Chroma feature extraction mapping tracks to 24 Camelot Wheel key codes (`1A`–`12B`) with harmonic transition recommendations.
- **Phrase-Aligned DJ Automix**: Downbeat-aligned energy crossfading over 4, 8, or 16 bars with dynamic tempo matching.
- **Interactive Vinyl Scratch Mode**: Touch-interactive turntable platter overlay enabling real-time scratching, backspins, and motor brake spin-up recovery.
- **BitChord Interactive Chord Sheets**: Displays 6-String Guitar Fretboards and 12-Key Piano Keyboards on chord token taps.
- **Japanese Furigana & Ruby Lyrics Annotations**: Renders Kanji readings in 10sp text directly above Kanji characters with toggle support.
- **Metro Panoramic Hub**: Alternative Windows Phone Metro-inspired horizontal scrolling interface.
- **Android 16/17 Expressive Notifications**: Material You dynamic palette matching with interactive seekbar scrub progress in notification shade.
- **Safe Room Statistics Fallback**: Zero-plays onboarding state "Your Listening Journey Begins Here" preventing null crashes.

#### Changed / Fixed
- **Player Layout Overhaul**: Prominent 300dp artwork stage with shadow elevation, marquee track titles, centered transport controls, and streamlined utility chips.
- **Charts Section Fix**: Capped chart feeds strictly to Top 10 tracks ("Top 10 - Global" & "Top 10 - India") and removed segmented selector pills.
- **Search Bar Polish**: Removed duplicate audio recognition mic buttons, retaining single Voice Search Mic and Acoustic Song Identification.
- **Settings Layout Spacing**: Moved `StreamCodecCard` into "Audio & Engine" tab, removing empty void panels.
- **Audiophile Theme Palettes**: Replaced horizontal scroll clipping box with auto-wrapping `FlowRow`.
- **App Launcher Icons**: Updated adaptive icons to clean dark background (`#0A0C10`) with cyan/gold infinity soundwave foreground across dark and light mipmaps.

#### Removed (Bloat Pruning)
- Scrapped Archive.org Live Concerts section.
- Scrapped Local Web Remote (:8080) server.
- Scrapped Discord Rich Presence (RPC).
- Scrapped WebDAV Cloud Sync.
- Scrapped DoH (DNS-over-HTTPS) provider selector.
- Scrapped Incognito Mode.
- Scrapped InnerTube Client Spoofing.
- Scrapped Provider Modules (`.lwp`).
- Scrapped Dynamic Island notch capsule.

---

## [1.2.1] - 2026-09-16

### Added
- **SongPlayStatsEntity + SongPlayStatsDao** — local per-track listening stats.
- **SongPlayStatsRepository** — records listened time and skips.
- **LocalTasteSuggestionEngine** — local recommendation engine.

### Changed
- **MusicPlayer.kt** — hooks `onMediaItemTransition` to record listened duration and detect skips.
- **FeedScreen.kt** — redesigned `QuickTilesGrid`/`QuickTileCard`.

### Fixed
- **Player state and cached track lost after leaving the app in the background.**
