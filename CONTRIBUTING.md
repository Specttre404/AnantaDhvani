# Contributing to Ananta Dhvani

Thank you for your interest in contributing to Ananta Dhvani! We welcome contributions that align with our core focus: audiophile acoustic purism, musician-first utility, and clean Material 3 Expressive design.

---

## 📜 Guiding Principles

Before submitting a pull request, ensure your contribution respects our project rules:
- **The Anti-Podcast Manifesto:** Do NOT submit pull requests adding podcasts, audiobooks, talk shows, spoken-word feeds, or social media integration.
- **Audio Pipeline Integrity:** Any modification to `app/src/main/cpp/` or `MusicPlayer.kt` must preserve bit-perfect fidelity and must not introduce uncontrolled resampling.
- **Zero Telemetry:** We do not accept PRs introducing tracking libraries, analytical crash reporters, or external cloud analytics.

---

## 🛠️ Development Setup

1. **Clone the repository:**
   ```bash
   git clone https://github.com/Specttre404/AnantaDhvani.git
   cd AnantaDhvani
   ```
2. **Open the project** in Android Studio Ladybug (2024.2+) or newer.
3. **Ensure you have installed:**
   - Android SDK Platform 35
   - Android NDK 26.1.10909125 or newer
   - CMake 3.22.1 or newer
   - JDK 17

---

## 🧪 Testing & Verification

Before pushing changes, all code must pass compilation and unit testing:

```bash
# Verify Kotlin compilation
./gradlew app:compileDebugKotlin

# Run unit tests (must pass 24/24 tests)
./gradlew app:testDebugUnitTest

# Assemble Release build
./gradlew app:assembleRelease
```

---

## 🎨 Code Style

- **Kotlin:** Follow official Kotlin coding conventions with Jetpack Compose best practices. Avoid unneeded re-compositions by using `@Stable` and `remember`.
- **C++:** C++20 standard, formatted according to Google C++ Style Guide.

---

Handcrafted with acoustic precision by **Ishan**.
