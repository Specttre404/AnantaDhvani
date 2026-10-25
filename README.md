# Ananta Dhvani (अनन्त-ध्वनि)
> The cutting-edge, dual-engine audiophile music streaming workstation for Android.

<p align="center">
  <img src="https://img.shields.io/badge/Version-v1.7.0-00E5FF?style=for-the-badge&logo=git&logoColor=0B0F19" alt="Version 1.7.0" />
  <img src="https://img.shields.io/badge/Platform-Android%2017%20Canary-00E5FF?style=for-the-badge&logo=android&logoColor=0B0F19" alt="Android 17" />
  <img src="https://img.shields.io/badge/UI-M3%20Expressive-FF2E93?style=for-the-badge&logo=materialdesign&logoColor=white" alt="M3 Expressive" />
  <img src="https://img.shields.io/badge/Engine-iOS%20Liquid%20Glass-7F52FF?style=for-the-badge&logo=apple&logoColor=white" alt="iOS Liquid Glass" />
  <img src="https://img.shields.io/badge/Audio-Bit--Perfect%20DAC-06D6A0?style=for-the-badge&logo=soundcharts&logoColor=0B0F19" alt="Bit-Perfect Audio" />
  <img src="https://img.shields.io/badge/Bridge-Squidify%20Audio%20Hub-FF0055?style=for-the-badge&logo=octopusdeploy&logoColor=white" alt="Squidify Hub" />
  <img src="https://img.shields.io/badge/Framework-Media3%20ExoPlayer-FFB703?style=for-the-badge&logo=googleplay&logoColor=0B0F19" alt="Media3" />
</p>

---

## 📢 Important Notice & Project Status

> [!WARNING]
> ### ⏸️ Development Status: Temporarily On Hold
> Please note that due to personal reasons, active development on this app/fork is **temporarily paused**. Once everything is sorted out, **development will bounce back with a bang!**
> 
> * **Project Nature:** This is a pure passion project and my **very first software project ever**.
> * **Author:** Built and maintained with passion by **Ishan**.
> * **AI Collaboration:** Built and architected using **Google Gemini 4 Aragon**.
> * **Current Stage:** Active Alpha / Experimental Testing Phase. Features are evolving rapidly.

---

## 🌌 Vision & Philosophy

**Ananta Dhvani** (Infinite Sound) was founded on a singular obsession: **absolute fidelity and modern aesthetic perfection without compromise.**

* **Zero-Degradation Audio Pipeline:** Standard mobile operating systems resample, choke, and limit high-definition sound through generic software mixers. Ananta Dhvani circumvents these bottlenecks by routing digital audio directly to hardware DACs in its pure, unaltered form.
* **Dual-Engine Visual Harmony:** We reject cookie-cutter interfaces. The app unites the bold geometry, expressive springs, and dynamic Monet palettes of **Material 3 Expressive (Android 17)** with the specular, refractive elegance of **Apple iOS Liquid Glass**.
* **Community & Openness:** Built as an authentic, ad-free haven for music lovers, dedicated to pushing Android audio engineering to its bleeding edge.

---

## 🚀 Complete Feature Manifest (v1.7.0)

### 🔊 Audiophile Audio Engine & DSP
* **Bit-Perfect Direct USB DAC Passthrough:** Direct hardware UAC2 routing bypassing the Android OS audio flinger and native resampler.
* **Dual Master Clock Management:** Native crystal family switching (44.1 kHz vs. 48 kHz base) with **libsoxr** high-precision fallback resampling.
* **High-Res Codec Pipeline:** Lossless FLAC (up to 24-bit / 192 kHz), Opus (160 kbps high-efficiency), and AAC (up to 256 kbps).
* **Real-Time Visualizer Engine:** Low-latency hardware-accelerated OpenGL / Canvas audio oscilloscope and live FFT frequency spectrum analysis.
* **Media3 Integration:** Built on `androidx.media3:media3-exoplayer` and `media3-session` for robust background service persistence, lock screen transports, and Android Auto integration.

### 🎨 Dual-Engine UI & Shell Experience
* **Material 3 Expressive Mode:** Asymmetric pill geometries (`ExpressivePillShape`), tactile tonal elevations, and fluid physics springs (`ExpressiveMotion.spatialSpring`).
* **Apple iOS Liquid Glass Mode:** Real-time refractive shader blur powered by `@kyant/backdrop` (`LayerBackdrop`), specular frosted borders (`liquidGlassChrome`), and fluid expandable navigation capsules.
* **Persistent Floating Mini-Player:** Docked floating bar with gesture expansion, track progress indicators, spinning vinyl physics, and quick playback toggles.
* **Responsive Command Dock:** 3-tab unified navigation pill (Feed, Discover, Playlists) featuring an active animated soundwave equalizer indicator (`GraphicEq`).
* **Custom Seekbar Shader Variants:** Dynamic canvas styling across 7 styles (*Android 17 Wavy Fluid*, *ViVi Capsule*, *Analog Mixing Console*, *Metro Segmented Dash*, *Minimal Needle*, *Prism Neon Ribbon*, and *Nuvia Live Waveform*).

### 🌐 Hybrid Integrations & Playback Automation
* **Squidify Audio Hub:** Hybrid hardware-accelerated WebView bridge with bidirectional JavaScript bindings (`SquidifyBridge`) and network interception for lossless web audio extraction.
* **Endless Radio Autoplay:** Predictive queue replenishment engine that seamlessly fetches related tracks via InnerTube/YouTube Music seeds when approaching queue exhaustion.
* **Synced Typographic Lyrics:** Millisecond-accurate live lyrics powered by the **LRCLIB API** with kinetic animation tracking.
* **Unified Scrobbler:** Native Last.fm REST integration and broadcast session support for ListenBrainz.
* **Offline Library & Storage:** SQLite persistence via Jetpack Room and reactive preferences via Jetpack DataStore.

---

## 🗺️ Upcoming Features & Future Roadmap

We are committed to making Ananta Dhvani the most advanced, modern, cutting-edge audiophile music application in existence:

- [ ] **Squidify Anime Hub Pipeline:** Full native indexing and streaming integration for Anime OSTs, Openings (OP), and Endings (ED) directly via Squidify.
- [ ] **Ultra-Premium Master Streaming Tiers:** Pushing native bitrates to the absolute theoretical maximum of source availability, targeting true studio-master quality.
- [ ] **Advanced Parametric EQ & DSP:** 15+ band parametric equalizer with AutoEQ profile imports, room convolution filters, and binaural crossfeed simulation.
- [ ] **Modular Provider System:** Encrypted `.lwp` external provider module installer for community-driven backend extensions.
- [ ] **Gapless DSD / DoP Passthrough:** Native DSD over PCM support for high-end external DAC hardware.

---

## 🤝 Credits & Acknowledgements

Ananta Dhvani stands on the shoulders of brilliant open-source pioneers:

* **[LastWave](https://github.com/) & LastWave-Native:** The foundational codebase and architectural inspiration for our core player modules.
* **Google AndroidX Media3 Team:** For providing the robust `ExoPlayer` media engine infrastructure.
* **[@kyant/backdrop](https://github.com/Kyant0/backdrop):** For the revolutionary Compose backdrop shader and glassmorphic blur engine.
* **libsoxr Developers:** For the high-grade one-dimensional sample rate conversion library.
* **LRCLIB:** For the open-source community lyrics database and API.
* **InnerTube / Piped Project Contributors:** For YouTube Music stream reverse-engineering breakthroughs.
* **Squidify Project:** For web audio streaming, lossless track extraction tools, and search services.

---

## ⚖️ Disclaimer

Ananta Dhvani is an open-source, non-commercial software client developed strictly for personal use, educational research, and audiophile experimentation. All audio streams, album artworks, metadata, and lyrics are the intellectual property of their respective copyright holders, artists, and record labels. This project does not host, store, or distribute copyrighted media files.
