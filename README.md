# STASH — Premium Private Document Vault & Intelligence Engine

[![Android](https://img.shields.io/badge/Platform-Android-green.svg)]()
[![Kotlin](https://img.shields.io/badge/Language-Kotlin-blue.svg)]()
[![Compose](https://img.shields.io/badge/UI-Jetpack%20Compose-orange.svg)]()
[![Privacy](https://img.shields.io/badge/Privacy-100%25%20Offline-black.svg)]()
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

**Stash** is a production-grade, privacy-first document discovery and management application for Android. Built for legal professionals, researchers, and security-conscious users, Stash allows you to index, organize, and search through thousands of local documents using on-device AI intelligence.

---

## 🚀 Premium Features

### 🔍 Intelligence & Search
- **Full-Text Search (FTS):** Instant sub-50ms search across filenames, custom descriptions, and document content using SQLite FTS4.
- **On-Device OCR:** Automatically extracts text from images and scanned PDFs using Google ML Kit. 100% local processing—no cloud data leaks.
- **Smart Ranking:** Search results are ranked using a professional scoring algorithm based on relevance, priority flags, and recent activity.

### 🔒 Vault-Grade Security
- **Multi-Layer Locking:** Secure your vault behind a **4-digit PIN** or native **Biometrics** (Fingerprint/Face).
- **Auto-Lock Engine:** Configurable session timeouts (15s to 5m) that automatically lock the vault when the app is backgrounded.
- **Privacy Invariants:** Locked documents are completely invisible to the system—hidden from search and the main dashboard until you authenticate.
- **Screenshot Protection:** Prevents content from appearing in the recent apps switcher and blocks unauthorized screen recordings.

### 📂 "Poco-Style" Browser & Organization
- **Multi-Select Engine:** Long-press to enter a high-performance selection mode for bulk sharing, deleting, or re-categorizing.
- **Social & External Share:** Seamlessly share multiple documents at once via the system share sheet (WhatsApp, Gmail, etc.) using a secure `FileProvider`.
- **Professional Palette:** Industry-standard categories (Legal, Financial, Medical) with custom hex-coded identity notches.
- **Dynamic View Modes:** Switch between **Large Icons**, **Grid**, **List**, **Details**, and **Tiles** to suit your professional workflow.

### 🖼️ Seamless In-App Previews
- **Native Viewers:** High-quality built-in viewing for PDFs, Images, and Text/Markdown—no need to leave the secure vault environment.
- **Deep Metadata:** Attach private markdown descriptions and external resource links to any case file.

---

## 🏗️ Modern Android Architecture

Stash follows the **Clean Architecture** principles and the **MVVM** pattern for maximum maintainability:

- **UI:** 100% Jetpack Compose with Material 3 ("Vault & Ledger" design system).
- **Logic:** Single-responsibility Use Cases and Hilt Dependency Injection.
- **Storage:** Room Database (Metadata) + SQLite FTS4 (Content Index).
- **Background:** WorkManager for non-blocking OCR and recursive storage synchronization.
- **State:** Kotlin Coroutines & Flow for reactive, thread-safe UI updates.

---

## 🛠️ Build & Development

### Prerequisites
- **Android Studio Ladybug** (2024.2.1) or newer.
- **Gradle 9.7.1** (included in wrapper).
- **Android SDK 35** (Build target 36).
- Minimum Android version: Android 8.0 (API 26).

### Project Structure
```text
app/src/main/kotlin/com/ashish/stash/
├── core/             # Services (OCR, SAF, Security, Database, Backup)
├── domain/           # Business logic (Use Cases)
├── ui/               # Presentation (Compose UI, Navigation, Theme)
│   ├── feature/      # Modules: Home, Search, Detail, Settings
│   └── security/     # PIN & Biometric Gates
└── worker/           # Background Synchronization Engine
```

---

## 🛡️ License & Credits

Distributed under the **MIT License**. Created with professional Android engineering standards.

Developed with ❤️ by **[Ashish Cherian](https://github.com/AshishCherian15/)**
- **GitHub Repo:** [https://github.com/AshishCherian15/STASH](https://github.com/AshishCherian15/STASH)
- **Email:** [ashishcherian15@gmail.com](mailto:ashishcherian15@gmail.com)
