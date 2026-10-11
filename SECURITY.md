# Security Policy

## Supported Versions

Only the latest release of Ananta Dhvani receives security and engine patches.

| Version | Supported |
| :--- | :---: |
| 1.6.x | :white_check_mark: |
| < 1.6.0 | :x: |

---

## 🛡️ Privacy & Offline Security Model

- **No Telemetry:** Ananta Dhvani does not collect, transmit, or store any personal telemetry, analytics, hardware identifiers, or location data.
- **Encrypted Storage:** User preferences and cached credentials are stored using Android Jetpack DataStore and Room SQLite on private application sandboxes (`/data/data/com.lastwave.app/`).
- **Minimal Permissions:** Ananta Dhvani requests only the permissions strictly necessary to index audio files and handle media playback.

---

## 🔍 VirusTotal Clean Verification

Official release binaries (`app-release.apk`) are validated against VirusTotal prior to distribution:
- **Detection Score:** `0 / 72 (100% Clean)`
- **Static Analysis:** Zero adware, zero surveillance trackers, zero hidden payload execution vectors.

---

## Reporting a Vulnerability

If you discover a security vulnerability in Ananta Dhvani, please report it privately:
1. Open a private security advisory through the GitHub repository's **Security** tab.
2. Provide a detailed summary and proof-of-concept steps to reproduce the issue.
3. Please do not disclose the issue publicly until a patch has been released.
