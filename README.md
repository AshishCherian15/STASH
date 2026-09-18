# Stash — Private Document Vault & Intelligence Engine

[![Android](https://img.shields.io/badge/Platform-Android-green.svg)]()
[![Kotlin](https://img.shields.io/badge/Language-Kotlin-blue.svg)]()
[![Compose](https://img.shields.io/badge/UI-Jetpack%20Compose-orange.svg)]()
[![Privacy](https://img.shields.io/badge/Privacy-100%25%20Offline-black.svg)]()

**Stash** is a premium, privacy-first document discovery and management application for Android. Designed for students, researchers, and professionals, Stash allows you to index, organize, and search through thousands of documents entirely offline using local intelligence.

---

## 🚀 Key Features

### 🔍 Deep Intelligence & Search
- **Full-Text Search (FTS):** Instant search results across filenames, your custom notes, and even content inside documents using SQLite FTS4.
- **On-Device OCR:** Powered by Google ML Kit, Stash automatically reads text from images and scanned PDFs. All processing is 100% local—no data ever leaves your device.
- **Smart Ranking:** Search results are ranked using a composite score of keyword relevance, document importance, and recent activity.

### 🔒 Vault-Grade Security
- **Biometric Gating:** Secure your entire vault, specific folders, or individual files behind fingerprint or facial recognition.
- **Privacy Invariants:** Locked documents are completely hidden from search and the main dashboard until you authenticate.
- **Screenshot Protection:** Optional security mode that hides app content from the recent apps switcher and prevents screen recordings.

### 📂 High-Speed Organization
- **Index-in-Place:** Uses Android's Storage Access Framework (SAF) to index files without duplicating them, saving storage space.
- **Rich Metadata:** Organize files with color-coded categories, hierarchical folders, and multi-tag searchable labels.
- **Priority Mode:** A dedicated dashboard for your most critical documents (marked as High or Critical).
- **Notes & Links:** Attach private markdown notes and external web references to any document.

### 🖼️ Seamless Viewing
- **Built-in Previews:** High-quality in-app viewing for PDFs, Images, and Text/Markdown files.
- **Quick Add:** A system-wide Share Sheet target that lets you "Stash" files directly from WhatsApp, Gmail, or any file manager.

---

## 🏗️ Technical Architecture

Stash is built using the latest Android development standards:

- **UI Layer:** Jetpack Compose with Material 3 following the "Vault & Ledger" design system.
- **Architecture:** MVVM + Clean Architecture with single-responsibility Use Cases.
- **Dependency Injection:** Dagger Hilt for robust scoping and testability.
- **Local Storage:** Room Database for metadata and SQLite FTS4 for high-performance indexing.
- **Background Engine:** WorkManager for non-blocking OCR processing and folder synchronization.
- **Data Persistence:** DataStore for secure, thread-safe user preferences.

---

## 🛠️ Build & Development

### Prerequisites
- Android Studio Ladybug (2024.2.1) or newer.
- Android SDK 35 (Build target 36).
- Minimum Android version: Android 8.0 (Oreo / API 26).

### Repository Structure
```text
app/src/main/kotlin/com/ashish/stash/
├── core/             # Core Services (OCR, Hash, SAF, Security, Database)
├── domain/           # Business Logic (Use Cases)
├── ui/               # Presentation (Compose UI, Navigation, Theme)
│   ├── feature/      # Modules: Home, Search, Detail, Settings, etc.
│   └── theme/        # "Vault & Ledger" Palette & Typography
└── worker/           # Background Synchronization & Scanning
```

---

## 📜 Full Documentation

Explore the detailed specifications in the `docs/` folder:
- [PRD](docs/PRD.md) • [TRD](docs/TRD.md) • [Architecture](docs/ARCHITECTURE.md) • [Schema](docs/SCHEMA.md) • [UI-UX](docs/UI-UX.md) • [Security Model](docs/SECURITY.md)

---

## 🛡️ License & Credits

Distributed under the MIT License. See `LICENSE` for more information.

Developed with by **[Ashish Cherian](https://github.com/AshishCherian15/)**
- **GitHub:** [https://github.com/AshishCherian15/STASH](https://github.com/AshishCherian15/STASH)
- **Email:** [ashishcherian15@gmail.com](mailto:ashishcherian15@gmail.com)
