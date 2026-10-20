# Ananta Dhvani v1.6.1-HDR (Testing Phase Preview)

### 🎛️ Key Highlights & Core Overhaul
- **Dynamic Palette Play Button**: Play/Pause FAB extracts and renders dominant track artwork colors dynamically across all 8 layouts.
- **Restored All 16 Audio Studios**: Fully repopulated the `showAdvancedSubsystemsSheet` modal bottom sheet to 92% screen height with complete scrolling across all 16 engineering studios.
- **Canary & Alpha M3 Expressive Activation**: Directly configured explicit Compose `material3:1.4.0-alpha05`, `animation:1.8.0-alpha07`, and `foundation:1.8.0-alpha07` dependencies ahead of BOM constraints.
- **Media3 1.6.0-alpha02 Upgrade**: Upgraded Media3 ExoPlayer core, HLS, and DASH engines to `1.6.0-alpha02`.
- **Android 13–16 Storage Compliance**: Added `READ_MEDIA_AUDIO` permission to `AndroidManifest.xml` for seamless local music library indexing.
- **Transcoding Safeguard**: Added pre-flight stream validation checks in `AudioFormatConverter.kt`.
- **Obsidian Branding & Notch Purge**: Rebranded startup splash to Ananta Dhvani on obsidian void (`#0B0F1A`) and purged camera punch-hole circle overlay.
