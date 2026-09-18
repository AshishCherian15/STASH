# Stash Security Model

Stash is a privacy-first Android document indexer. It should not be described as encrypted unless database encryption is implemented and verified. The current security model focuses on user-controlled file access, local processing, authenticated UI access, conservative system integration, and transparent limitations.

## Implemented Controls

1. User-controlled file access
   - Documents are selected through Android Storage Access Framework.
   - Stash stores SAF URI references and metadata rather than copying original files as a core workflow.
   - Import validation checks URI scheme, supported MIME type, readability, and a 100 MB per-file size limit.

2. Authentication boundary
   - Vault locking is controlled by a centralized `SecuritySessionManager`.
   - When vault lock is enabled, app navigation content is behind `LockGate`.
   - Authentication uses Android `BiometricPrompt` with device credential fallback.

3. Lifecycle protection
   - Auto-lock is configurable: immediately, 15 seconds, 30 seconds, 1 minute, 5 minutes, or never.
   - The session manager re-locks on foreground return after the configured timeout.

4. Screenshot and recents protection
   - Screenshot protection is enabled by default.
   - `FLAG_SECURE` is applied from the main activity when the setting is enabled.

5. System integration privacy
   - Notifications use generic text and private visibility.
   - The home-screen widget is aggregate-only and does not display document names.
   - Automatic Android backup/device transfer excludes databases, files, preferences, and external app data.

6. Data access filtering
   - Locked documents are filtered out from unlocked list/search queries at the DAO/repository layer.
   - FTS search joins back to documents and filters `is_locked = 0`.

7. Network minimization
   - The app manifest does not request Internet permission.

## Not Yet Implemented

- SQLCipher or equivalent encrypted Room database.
- Keystore-backed database encryption key lifecycle.
- Encrypted user export/import backups.
- Full OCR/thumbnail deletion lifecycle tests.
- A separate app-specific PIN/password system.

## Security Claims

Use this claim:

> Stash is a privacy-first, offline document discovery and management application for Android that indexes user-selected files in place.

Do not use this claim yet:

> Stash is a locally encrypted vault.

That claim requires implemented and verified encrypted database/storage behavior.
