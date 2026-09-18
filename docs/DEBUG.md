# Debug Workflow — KEEP Resources

## 1. Reproduce First
Don't fix from a stack trace alone. Reproduce with: device/emulator API level, whether the doc set includes locked items, whether OCR queue had pending items, battery level (OCR/scan behavior changes < 15%).

## 2. Isolate by Layer
Given the architecture (ARCHITECTURE.md §1), bugs cluster predictably:
| Symptom | Check first |
|---|---|
| Search returns stale/wrong results | FTS4 sync path — is `documents_fts` actually updated after the write to `documents`? |
| Locked doc appears in search | DAO-level filter missing `WHERE is_locked = 0` — this is a correctness bug, treat as high severity even if rare |
| OCR text never appears | WorkManager constraints (battery < 15%? storage low?) blocking the worker — check `WorkInfo` state before assuming ML Kit failed |
| Thumbnail blank/wrong | LRU cache serving a stale entry — check cache key includes a content-hash or last-modified, not just document_id |
| Crash on SAF access | URI permission likely revoked — this should surface via `AccessLostBanner`, not crash; if it crashes, the URI Health monitor isn't catching this case |
| Biometric prompt doesn't appear | `BiometricManager.canAuthenticate()` result — hardware/enrollment state, not just a UI bug |

## 3. Common False Fixes (things that look like fixes but aren't)
- Wrapping a crash in try/catch without understanding the cause — masks the bug, doesn't fix it. Only acceptable if the exception is genuinely expected and handled meaningfully (e.g. `SecurityException` on revoked URI → trigger reconnect flow, not swallow silently)
- "Just clear the cache" as a fix for a data bug — if clearing cache "fixes" it, the bug is a stale-cache-key issue, find and fix the key logic
- Adding a delay/retry loop to paper over a race condition — find the actual race (usually a `Dispatchers.IO` vs main-thread read/write ordering issue)

## 4. Logging During Debug (then remove before commit)
- Never log document content, filenames, notes, or OCR text — even temporarily. Log document_id and operation name only.
- Remove all debug logging before the fix ships — see REVIEW.md privacy checklist.

## 5. Performance Regressions
If search latency or scroll FPS regresses (TRD.md §3 targets), profile before guessing:
- Search: check the FTS4 query plan hasn't silently fallen back to a table scan (e.g. after a schema change broke the index)
- Scroll: check for a Composable that isn't using stable/immutable state and is recomposing the whole list on every scroll tick
