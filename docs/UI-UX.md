# UI/UX — KEEP Resources

Companion to DESIGN.md (visual tokens) and FRONTEND.md (implementation). This file is user flows, states, and copy — not colors/type.

## 1. Core User Flows

**First launch**
```
Splash → Onboarding (3 screens: what KEEP does, privacy promise, "no cloud upload")
   → Permissions explainer ("why we need folder access")
   → SAF picker (OpenDocumentTree)
   → Home (empty state)
```

**Add a document (3 entry points)**
```
Share from another app  → Quick Add modal (transparent activity) → category/label → save → toast confirm
SAF picker (manual)     → same Quick Add modal
Background scan         → notification "New file detected" → tap → Quick Add modal
```

**Search**
```
Home → tap search → query typed → results stream in (<50ms)
   → optional filter chips (category/importance/color/date/lock)
   → tap result → Document Detail
```

**Lock a document**
```
Document Detail → "Lock" action → (first time only: Biometric onboarding, see §3)
   → BiometricPrompt → success → document shows 🔒 badge, excluded from search until unlocked
```

## 2. Empty States (copy + trigger)
| Screen | Trigger | Copy | CTA |
|---|---|---|---|
| Home | Zero documents ever added | "Nothing indexed yet. Add your first document to get started." | "Add Document" → opens SAF picker |
| Search | Query with zero matches | "No matches for '{query}'. Try a different term or check your filters." | none — filters are already visible above |
| Priority Mode | Zero HIGH/CRITICAL docs flagged | "Nothing flagged yet. Mark a document HIGH or CRITICAL from its detail screen to see it here." | "Learn more" → Help/FAQ entry |
| Locked folder (pre-setup) | User opens Lock section, never configured biometric | "Locking protects sensitive documents behind your fingerprint or face. Nothing is locked yet." | "Set Up Lock" → Biometric onboarding |

## 3. Biometric Lock Onboarding (first-run only, separate from lock screen itself)
1. Explain scope: "You can lock the whole app, a folder, or a single document."
2. "Enable Now" → `BiometricManager.canAuthenticate()` check → enrollment prompt if needed → success confirmation
3. "Maybe Later" → skip, resurface the same flow from Settings > Privacy whenever user first taps "Lock" on any item

## 4. Error / Permission-Denied States
| Scenario | Surface | Copy | Action |
|---|---|---|---|
| SAF permission revoked (URI Health monitor detects) | Banner on affected `DocumentCard` | "Access lost — tap to reconnect" | Re-triggers SAF picker scoped to that file/folder only |
| Biometric hardware unavailable mid-session | Lock screen | "Biometric unavailable. Use device PIN instead." | Falls back to `DEVICE_CREDENTIAL` |
| Storage pressure < 100MB free | Non-blocking snackbar | "Low storage — thumbnail cache may be reduced." | Dismiss, no forced action |
| DB integrity check fails on launch | Blocking dialog | "We detected a data issue. Restore from your last backup?" | Restore from JSON backup or start fresh |

## 5. Permissions Rationale Copy (shown before every OS permission dialog)
| Permission | Rationale shown |
|---|---|
| Folder access (SAF) | "KEEP needs access to a folder so it can index your files. Nothing is uploaded — everything stays on this device." |
| Photos (for OCR) | "Used to read text from photos of whiteboards and handwritten notes, entirely on this device." |
| Notifications | "So KEEP can tell you when it finds a new file worth adding." |
| Biometric | "Locks sensitive documents behind your fingerprint or face. We never see or store your biometric data." |

## 6. Help/FAQ (minimum viable set — ship with Phase 4)
1. Why can't KEEP see all my files? (SAF scoping — explain in plain terms)
2. Is my data uploaded anywhere? (No — link to Privacy Policy)
3. What happens if I revoke folder access? (Docs from that folder show "Access lost," reconnect anytime)
4. How does OCR search work? (Photos are scanned on-device; searching finds text inside them)
5. Can I recover a deleted document? (No — deleting removes it from the index only; original file is untouched wherever it lives, so recovery depends on where you deleted it from)
6. Does locking encrypt my files? (Locking hides them from the app UI and search; the DB itself is protected via Android Keystore — clarify this isn't full-disk encryption of the original file)
