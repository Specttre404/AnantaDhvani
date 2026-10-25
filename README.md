# Ananta Dhvani (अनन्त-ध्वनि)

<p align="center">
  <img src="https://img.shields.io/badge/Platform-Android%2017%20Canary-00E5FF?style=for-the-badge&logo=android&logoColor=0B0F19" alt="Android 17" />
  <img src="https://img.shields.io/badge/UI-M3%20Expressive-FF2E93?style=for-the-badge&logo=materialdesign&logoColor=white" alt="Material 3 Expressive" />
  <img src="https://img.shields.io/badge/Visual-iOS%20Liquid%20Glass-7F52FF?style=for-the-badge&logo=apple&logoColor=white" alt="iOS Liquid Glass" />
  <img src="https://img.shields.io/badge/Audio-Bit--Perfect%20DAC-06D6A0?style=for-the-badge&logo=soundcharts&logoColor=0B0F19" alt="Bit-Perfect Audio" />
  <img src="https://img.shields.io/badge/DSP-libsoxr%20Resampling-9D4EDD?style=for-the-badge&logo=audacity&logoColor=white" alt="libsoxr" />
  <img src="https://img.shields.io/badge/Bridge-Squidify%20Audio%20Hub-FF0055?style=for-the-badge&logo=octopusdeploy&logoColor=white" alt="Squidify Hub" />
  <img src="https://img.shields.io/badge/Engine-Media3%20ExoPlayer-FFB703?style=for-the-badge&logo=googleplay&logoColor=0B0F19" alt="Media3" />
</p>

---

## ⚡ Technical Architecture & System Specifications

* **Minimum Android SDK:** API 29 (Android 10.0+ for 16 KB page-aligned native ELF binary execution and hardware backdrop shaders; backward-compatible to API 24).
* **Target Android SDK:** Android 17 (Canary / Preview).
* **Compiler & Codegen:**
  * Kotlin Symbol Processing (KSP) for dependency injection and Room SQLite persistence.
  * R8 Compiler / ProGuard (`com.android.tools:r8:9.4.14`) configured for high-level dead-code elimination, member inlining, and aggressive rule optimization.
  * Android NDK r27+ and CMake 3.22.1+ compiling C/C++ audio DSP modules into 16 KB page-aligned ELF shared libraries (`.so`).
* **UI Toolkit:** Jetpack Compose with Jetpack Navigation Compose and `PredictiveBackScreen` gesture orchestration.
* **Design Systems (Dual-Engine Visual Pipeline):**
  * **Material 3 Expressive (Android 17 Canary):** Asymmetric pill geometries (`ExpressivePillShape`), solid multi-tier tonal elevations (`surfaceContainerHigh`, `surfaceContainerHighest`), fluid spring physics (`ExpressiveMotion.spatialSpring`), and dynamic Monet palette extraction.
  * **Apple iOS Liquid Glass:** Real-time refractive translucency via `@kyant/backdrop` (`LayerBackdrop`, `rememberLayerBackdrop()`), specular chrome contours (`liquidGlassChrome`), and fluid expandable capsule navigation.
* **Media Framework:** AndroidX Media3:
  * `androidx.media3:media3-exoplayer` (Core playback, buffer allocations, and audio sink control).
  * `androidx.media3:media3-session` (`MediaSessionService` handling background media persistence, Android Auto, and lock-screen interfaces).
  * `androidx.media3:media3-ui`.
* **Native Audio Pipeline:**
  * High-performance C/C++ PCM audio processing engine.
  * Bit-perfect USB Audio Class 2 (UAC2) direct DAC routing, bypassing the Android OS audio flinger and native resampler.
  * Dual-crystal master clock family management (44.1 kHz vs. 48 kHz base) with **libsoxr** high-precision fallback resampling.
  * Hardware-accelerated OpenGL / Canvas audio oscilloscope and real-time FFT visualizer routines.
* **Codecs & Containers:**
  * Bit-exact Hi-Res FLAC (up to 24-bit / 192 kHz).
  * Opus (160 kbps high-efficiency audio).
  * Advanced Audio Coding (AAC-LC / AAC-ELD up to 256 kbps).
* **Integrations & Embedded Modules:**
  * **Squidify Audio Hub:** Accelerated WebView bridge with two-way JavaScript bindings (`SquidifyBridge`) and network interception for lossless web audio extraction.
  * **LRCLIB API:** Millisecond-accurate synchronized lyrics rendering engine.
  * **Scrobbling Engine:** Native Last.fm REST API and system-level ListenBrainz broadcast receiver.
  * **YouTube Music / InnerTube:** Streaming backend provider and endless recommendation radio feeder.
* **Client:** Native Android Client (`com.lastwave.app`), supporting Phone, Foldable, Tablet, and Automotive forms.
