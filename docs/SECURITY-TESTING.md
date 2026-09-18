# Security Testing Plan

Use this checklist before describing Stash as production-ready.

## Authentication

- [ ] Correct biometric unlock opens the app.
- [ ] Device credential fallback opens the app.
- [ ] Failed biometric remains locked.
- [ ] Canceled biometric remains locked.
- [ ] Process recreation starts locked when vault lock is enabled.
- [ ] Background then foreground immediately re-locks when timeout is immediate.
- [ ] Background then foreground after timeout re-locks for 15s, 30s, 1m, and 5m settings.
- [ ] Never timeout does not re-lock an authenticated session.

## Secure UI

- [ ] Screenshot is blocked when screenshot protection is enabled.
- [ ] Screen recording is blocked when screenshot protection is enabled.
- [ ] Android recents preview hides app content when screenshot protection is enabled.
- [ ] Screenshot behavior returns to normal when user disables the setting.

## SAF And Import

- [ ] Valid PDF imports.
- [ ] Valid image imports.
- [ ] Valid text file imports.
- [ ] Unsupported MIME type is rejected.
- [ ] Invalid URI scheme is rejected.
- [ ] Unreadable URI is rejected.
- [ ] File larger than 100 MB is rejected.
- [ ] Revoked permission shows inaccessible/unavailable state instead of crashing.
- [ ] Deleted or moved file shows inaccessible/unavailable state instead of crashing.

## Quick Add

- [ ] `ACTION_SEND` with one valid file imports.
- [ ] `ACTION_SEND_MULTIPLE` with valid files imports.
- [ ] Missing `EXTRA_STREAM` shows "No readable document was shared."
- [ ] Duplicate shared files are skipped.
- [ ] Unsupported files are skipped.
- [ ] Raw URI strings are not displayed in Quick Add UI.

## Search And FTS

- [ ] Normal indexed document appears in search.
- [ ] Locked document does not appear in search.
- [ ] Deleted document does not appear in search.
- [ ] Deleted OCR text does not remain searchable.
- [ ] FTS table has no orphan rows after delete workflows.

## System Integration

- [ ] Notifications do not display filenames or OCR text.
- [ ] Lock-screen notification visibility is private.
- [ ] Widget does not display document names, URIs, or OCR text.
- [ ] Android Auto Backup excludes Room databases and preferences.
- [ ] Device-transfer backup excludes Room databases and preferences.

## Release Build

- [ ] `assembleRelease` completes.
- [ ] Release manifest is not debuggable.
- [ ] No `INTERNET` permission unless a documented network feature is added.
- [ ] No secrets in `strings.xml`, `BuildConfig`, `local.properties`, or source files.
- [ ] No document URI, filename, OCR text, or auth data in logs.
