# Stash Threat Model

## Assets

| Asset | Why It Matters |
|---|---|
| Document metadata | Filenames, MIME types, sizes, and dates may reveal private information. |
| SAF URIs | Persisted URI grants control access to user-selected files. |
| OCR text | Extracted text can contain sensitive document contents. |
| FTS index | Search indexes can leak locked/deleted content if not maintained correctly. |
| Preferences | Security settings and root SAF URI affect access behavior. |
| Notifications/widgets | These surfaces can expose information outside the app lock. |
| Backups | System backups can move sensitive app data off device if not excluded. |

## Attack Surface

| Surface | Threat | Current Mitigation | Residual Risk |
|---|---|---|---|
| App launch/UI | Content shown before authentication | App-wide `LockGate` when vault lock is enabled | Vault lock is optional and user-controlled. |
| Background/foreground | Returning to visible private content later | Configurable auto-lock through `SecuritySessionManager` | Android lifecycle edge cases need device testing. |
| Recents/screen capture | Sensitive previews/screenshots | `FLAG_SECURE` setting, enabled by default | User can disable it. |
| SAF import | Invalid/malicious URI, unsupported type, huge files | URI scheme, MIME, readability, and size validation | Provider-specific behavior can still vary. |
| Quick Add | Malformed external intents | Explicit URI extraction and central import validation | Deprecated intent APIs need modern API-specific cleanup. |
| Search/FTS | Locked content discoverable through search | Search query joins documents and filters unlocked documents | Deletion/lock synchronization needs tests. |
| Notifications | Filename exposure on lock screen | Generic notification text and private visibility | Notification channels are user-controllable in system settings. |
| Widget | Launcher-visible sensitive names | Aggregate-only widget layout | Future widget features must preserve this boundary. |
| Android backup | Database/preferences copied off device | Backup/data extraction rules exclude sensitive domains | Explicit encrypted export is not implemented. |
| Local database | Offline extraction from device backup/root | App sandbox, backup exclusion | No database encryption yet. |

## Assumptions

- The Android OS app sandbox is functioning correctly.
- Device credential/biometric authentication is managed by Android platform APIs.
- The app is not defending against a fully compromised/rooted device.
- The current build does not claim encrypted local storage.

## Priorities

1. Add instrumentation tests for lock/search/FTS side channels.
2. Add tests for Quick Add invalid URI and unsupported MIME cases.
3. Verify backup exclusion behavior on Android 12+ and older devices.
4. Design database encryption only if the product threat model requires offline-at-rest protection beyond Android sandboxing.
