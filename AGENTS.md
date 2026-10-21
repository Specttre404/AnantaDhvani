# AGENTS.md: Autonomous Engineering Directives for Ananta Dhvani

This repository is maintained and directed by **Ishan** with **Google Gemini 4 Argon** as the primary engineering intelligence. All autonomous code agents (Gemini in Android Studio, Claude, Cursor, Copilot) MUST adhere to the following invariant rules:

### 1. Invariant Architectural Principles
- **The Anti-Podcast Manifesto**: Never introduce podcast feeds, talk shows, audiobooks, spoken-word processors, or algorithmic feed recommenders. Every subsystem is dedicated solely to music playback fidelity.
- **Silent Hardware Auto-Tuning**: Never add manual audio engineering sliders, codec forcing switches, or low-level buffer tuning sheets to user-facing settings. All hardware probing must occur silently in the background through `AutoHardwareAudioTuner.kt` and `AudioDeviceCallback`.
- **Absolute Privacy**: Never add telemetry beacons, user analytics, crashlytics tracking scripts, or ad SDKs. Cleartext HTTP traffic must remain permanently forbidden.
- **Modern Android & Liquid Glass**: Adhere strictly to Android 15/16/17 standards, Compose Material 3 Expressive, and iOS Liquid Glass frosted specular refraction shaders.

### 2. Build & Tooling Directives
- **Never Run Static Linting**: Never execute `analyze_file` or static lint checks that lock or hang Gradle daemon processes.
- **Build Exclusively with `assembleRelease`**: Always verify code syntax using `app:compileReleaseKotlin`, run unit tests via `app:test`, and verify distribution binaries with `app:assembleRelease`.
