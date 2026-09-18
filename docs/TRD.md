# TRD — KEEP Resources

**Depends on:** PRD.md (approved) · **Status:** Approved for build

## 1. Stack
| Layer | Choice | Why (see ARCHITECTURE.md §Trade-offs for alternatives considered) |
|---|---|---|
| UI | Jetpack Compose + Material 3 | Declarative, dynamic theming, native to Android |
| Architecture | MVVM + Clean Architecture | Testable, standard for this scale |
| DI | Hilt | Android-recommended |
| Local DB | Room + SQLite FTS4 (`@Fts4`) | Sub-50ms full text search, no backend |
| Async | Coroutines + Flow | Standard Compose-native async |
| Image loading | Coil | Lightweight, Compose-first |
| OCR | Google ML Kit Text Recognition (bundled) | Fully offline, no network dependency |
| Background work | WorkManager + ContentObserver | Battery-friendly, Play-policy compliant (no foreground service) |
| Biometric | AndroidX Biometric + Android Keystore (AES-256-GCM) | Hardware-backed, standard API |
| File access | Storage Access Framework only | No `MANAGE_EXTERNAL_STORAGE` — required for privacy stance and Play approval |
| PDF rendering | Native `PdfRenderer` | Avoids third-party PDF library licensing/bloat |
| Office file handling | External handoff via `FileProvider` + `ACTION_VIEW` | Avoids Apache POI (20MB+) |
| Crash reporting | Firebase Crashlytics (opt-in, disabled by default) | Only non-local dependency, gated by explicit consent |

## 2. Functional Modules
Full module specs (OCR pipeline, Search Engine, Thumbnail Service, Background Scanner, Share Integration, Rename Service, Biometric Lock, In-App Viewer, External Handoff) are defined in the original blueprint §3.2.1–3.2.10 — treat that as the frozen spec unless a change is logged here with rationale.

Key non-obvious constraints to preserve during implementation:
- OCR runs in batches of 5 per work cycle, skipped if battery < 15%
- Search ranking is composite: FTS4 `rank` × recency weight × importance boost — don't replace with plain FTS4 rank, the weighting is a deliberate UX choice (recently-opened and flagged docs surface first)
- Duplicate detection is MD5 hash + file size, checked *before* insert, not as a post-hoc cleanup pass
- Locked documents must be excluded from search results until unlocked, not just hidden in the UI (data-layer filter, not view-layer)

## 3. Non-Functional Requirements
| Requirement | Target | Verification |
|---|---|---|
| Search latency | < 50ms @ 5,000 docs | Benchmark library, FTS4 MATCH query |
| OCR processing | < 2s per image (Snapdragon 6-series+) | Systrace |
| Cold start | < 2s | Android Profiler launch trace |
| Memory (idle) | < 80MB | Heap dump |
| Memory (PDF reader, 3 pages loaded) | < 120MB | Heap dump |
| DB size (5K docs + OCR) | < 15MB | Direct file measurement |
| APK size | < 15MB | `bundletool`, release build |
| Scrolling | 60 FPS, 1000-card list | GPU Profiler |
| Battery (background scan) | < 2% per 6-hour cycle | Battery Historian |

## 4. Data & Privacy Constraints (hard constraints, not preferences)
- Zero data transmitted off-device by default — this is a product identity commitment, not just a policy checkbox. Any future feature proposal that requires network access needs explicit re-approval against PRD §1 value prop.
- No `MANAGE_EXTERNAL_STORAGE` permission ever.
- Biometric data never touches app storage — only success/failure callback from `BiometricPrompt`.
- Crash reports (when opted in): no file names, URIs, notes, OCR text, categories, labels, or biometric data — device model/OS/app version/RAM only.

## 5. Testing Requirements
Full matrix in blueprint §4.2 (Unit/Integration/UI/Performance/Security). Minimum gate before any phase milestone is marked complete:
- Unit test coverage targets in §4.2.1 met for that phase's modules
- No `Dispatchers.IO` violations (background work never touches main thread — verify in code review, not just test)
- Security tests in §4.2.5 pass before Phase 3 milestone (biometric lock ships)

## 6. Explicit Non-Goals (technical)
- No custom backend, ever, in this version
- No third-party analytics beyond opt-in Crashlytics
- No PDF annotation/editing (viewer is read-only by design)
