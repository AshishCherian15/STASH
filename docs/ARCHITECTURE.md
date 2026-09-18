# Architecture — KEEP Resources

## 1. Pattern
MVVM + Clean Architecture, 3 layers:

```
UI (Compose)  →  ViewModel  →  UseCase  →  Repository  →  Room DAO / SAF / ML Kit
   (state)        (Hilt)       (business    (single       (data sources)
                                 rules)       source
                                              of truth)
```

- **UI layer:** Compose screens, stateless where possible, hoist state to ViewModel
- **Domain layer:** UseCases encapsulate one action each (`ImportDocumentUseCase`, `SearchDocumentsUseCase`, `LockDocumentUseCase`) — no Android framework imports here, keeps it unit-testable without instrumentation
- **Data layer:** Repository is the single source of truth; DAOs never called directly from ViewModel

## 2. Module Boundaries
| Module | Owns |
|---|---|
| `core:database` | Room DB, DAOs, FTS4 (SCHEMA.md) |
| `core:saf` | URI persistence, hash service, rename service |
| `feature:home` | Dashboard, document cards |
| `feature:search` | Search bar, filters, results |
| `feature:viewer` | PDF/image/text in-app viewer |
| `feature:lock` | Biometric flows, lock screen, Keystore |
| `worker:ocr` | ML Kit OCR pipeline, `OcrProcessingWorker` |
| `worker:scanner` | Background scanner, `ContentObserver`, folder monitor |

Rule: feature modules depend on `core:*`, never on each other directly — if two features need to share logic, promote it to a `core` module.

## 3. Data Flow (import → searchable)
```
SAF picker / Share intent / Auto-scan
        ↓
   Hash service (MD5 + size) → dedup check
        ↓ (not duplicate)
   Insert into `documents` (see SCHEMA.md)
        ↓
   If image: enqueue OcrProcessingWorker (WorkManager)
        ↓
   OCR result → update `documents.ocr_text` → FTS4 sync
        ↓
   Document now searchable (title/notes immediately, OCR text after worker completes)
```

## 4. Key Trade-offs (frozen decisions — reopen only with explicit rationale)
| Decision | Chosen | Rejected | Why |
|---|---|---|---|
| File storage | URI linking, no copy | Physical copy to sandbox | Zero storage overhead, respects user's existing organization |
| Search | SQLite FTS4 | Elasticsearch/Lucene/Algolia | No backend, sub-50ms locally, zero extra dependencies |
| OCR | ML Kit, bundled | Tesseract / Cloud Vision | Offline, no network, no privacy risk |
| Background work | WorkManager + ContentObserver | Foreground Service | Battery-friendly, no persistent notification, Play-policy compliant |
| Permissions | SAF only | `MANAGE_EXTERNAL_STORAGE` | Privacy-first, guaranteed Play approval |
| Office files | External handoff | Apache POI in-app | Avoids 20MB+ library |
| DB encryption | Optional (SQLCipher) | Always-on | Performance trade-off; biometric lock covers most threat models |

## 5. Concurrency Rules
- All DB writes and file I/O on `Dispatchers.IO` — never main thread (enforced in code review, see RULES.md)
- OCR batches of 5 per WorkManager cycle, `setRequiresBatteryNotLow(true)`
- Thumbnail generation: LRU memory cache (50 entries/20MB) + disk fallback, generated off main thread

## 6. Security Boundary
Everything below the dotted line never leaves the device:
```
┌─────────────────────────────┐
│  Device (all processing)    │
│  Room DB · ML Kit OCR ·     │
│  Biometric auth · Files     │
└──────────────┄┄┄┄┄┄┄┄┄┄─────┘   ← boundary
  Crashlytics (opt-in only, anonymized stack traces — no user content)
```
Any architecture change that would move processing above this line requires a PRD-level re-approval, not just a TRD update — it changes the product's privacy claim.
