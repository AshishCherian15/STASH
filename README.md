# STASH — Premium Private Document Vault & Intelligence Engine

[![Android](https://img.shields.io/badge/Platform-Android-green.svg)]()
[![Kotlin](https://img.shields.io/badge/Language-Kotlin-blue.svg)]()
[![Compose](https://img.shields.io/badge/UI-Jetpack%20Compose-orange.svg)]()
[![Privacy](https://img.shields.io/badge/Privacy-100%25%20Offline-black.svg)]()
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

**Stash** is a production-grade, privacy-first document discovery and management application for Android. Designed for professionals, researchers, and security-conscious users, Stash allows you to index, organize, and search through local documents using on-device AI intelligence.

---

## 🚀 Premium Features

### 🔍 Intelligence & Search
- **Deep Full-Text Search (FTS):** Instant sub-50ms search across filenames, descriptions, and OCR content using SQLite FTS4.
- **On-Device OCR:** Automatically extracts text from images and PDFs using Google ML Kit. 100% local—no data leaks.
- **Smart Ranking:** Search results are scored by keyword relevance, importance flags, and recent activity.

### 🔒 Vault-Grade Security
- **Multi-Layer Locking:** Secure your vault behind a **4-digit PIN** or native **Biometrics** (Fingerprint/Face).
- **Auto-Lock Engine:** Configurable session timeouts (15s to 5m) that lock the vault when the app is backgrounded.
- **Screenshot Protection:** Prevents content from appearing in the recent apps switcher and blocks unauthorized recordings.

### 📂 "Poco-Style" Browser & Organization
- **Multi-Select Engine:** Long-press to enter a high-performance selection mode for bulk sharing, deleting, or re-categorizing.
- **Device-Wide Indexing:** Optionally grant **All Files Access** to search every corner of your device without duplicating files.
- **Professional Palette:** Industry-standard categories (Legal, Financial, Medical) with professional hex-coded identity markers.
- **Dynamic View Modes:** Switch between **Large Icons**, **Grid**, **List**, **Details**, and **Tiles** to suit your workflow.

### 🖼️ Seamless In-App Previews
- **Native Viewers:** High-quality built-in viewing for PDFs, Images, and Text/Markdown.
- **Deep Metadata:** Attach private descriptions and external resource links to any case file.

---

## 🏗️ Modern Android Architecture

Stash follows the **Clean Architecture** principles and the **MVVM** pattern:

- **UI:** 100% Jetpack Compose with Material 3.
- **Logic:** Single-responsibility Use Cases and Hilt Dependency Injection.
- **Storage:** Room Database (Metadata) + SQLite FTS4 (Content Index) + DataStore (Prefs).
- **Background:** WorkManager for OCR and recursive storage synchronization.

---

## 🛠️ Build & Development

### Prerequisites
- **Android Studio Ladybug** (2024.2.1) or newer.
- **Gradle 9.7.1** (included in wrapper).
- **Android SDK 35** (Build target 36).
- Minimum Android version: Android 8.0 (API 26).

---

## 🛡️ License & Credits

Distributed under the **MIT License**. Created with professional Android engineering standards.

Developed with ❤️ by **[Ashish Cherian](https://github.com/AshishCherian15/)**
- **GitHub Repo:** [https://github.com/AshishCherian15/STASH](https://github.com/AshishCherian15/STASH)
- **Email:** [ashishcherian15@gmail.com](mailto:ashishcherian15@gmail.com)
