# Contributing to LastWaveX

Thank you for your interest in contributing to **LastWaveX**. This document outlines the development workflow, coding standards, and submission guidelines.

---

## Code of Conduct

All contributors and community members are expected to adhere to our [Code of Conduct](CODE_OF_CONDUCT.md). Please report unacceptable behavior to maintainers.

---

## Getting Started

### Prerequisites
* Android Studio Ladybug or newer (2024.2.1+)
* JDK 17 (Temurin / Zulu recommended)
* Android SDK (API 35) & NDK 26+
* Git

### Cloning the Repository
```bash
git clone https://github.com/specttre404/LastWaveX.git
cd LastWaveX
```

---

## Architecture Overview

LastWaveX is built using clean architecture patterns and modern Android libraries:
* **UI Layer:** Jetpack Compose, Material 3 Expressive, iOS LiquidGlass, Navigation Compose.
* **Audio Engine:** AndroidX Media3 ExoPlayer, C++ Native Float32 DSP processing, `AudioEffectsEngine` (`LoudnessEnhancer`, Equalizer).
* **Dependency Injection:** Dagger Hilt.
* **Storage & Caching:** Room Database, DataStore Preferences.
* **Networking:** Retrofit 2, OkHttp 4, Kotlinx Serialization.

---

## Development Guidelines

### Branching Strategy
* `main` contains the latest stable development code.
* Create feature or bugfix branches from `main` using descriptive names:
  * `feat/your-feature-name`
  * `fix/issue-description`

### Code Style & Quality
* Follow official Kotlin coding conventions and Android Architecture recommendations.
* Keep composables focused and stateless where possible.
* Ensure all native C++ code respects 16KB memory page alignment constraints for Android 15+.
* Avoid embedding hardcoded API secrets or URLs directly in source files.

### Testing
Run local unit tests before opening a pull request:
```bash
./gradlew app:testDebugUnitTest
```

---

## Submitting a Pull Request

1. Push your branch to your fork.
2. Open a Pull Request against the `main` branch.
3. Provide a concise summary of changes, problem analysis, and testing steps in the PR description.
4. Ensure all 28 unit tests pass (`./gradlew app:testDebugUnitTest`).
5. Address reviewer feedback promptly.
