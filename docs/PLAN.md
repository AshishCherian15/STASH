# Plan — KEEP Resources

Gate: PRD.md + TRD.md approved before any implementation (per vibecoding-system rule). This file is the execution sequence.

## Phase 1 — Foundation (Weeks 1–5): Core Vault
| Week | Task | Exit criteria |
|---|---|---|
| 1 | Hilt DI setup, Room DB (SCHEMA.md, all 7 tables), Material 3 theme (design.md tokens) | Project compiles, DB migrations run clean |
| 2 | SAF picker + URI persistence + hash service | User can add a file manually, dedup works |
| 3 | `DocumentCard` + Compose nav graph (FRONTEND.md §1) + Home dashboard | Home renders real cards from DB |
| 4 | FTS4 search + filter chips + pagination | Search returns results < 50ms on seed data |
| 5 | Categories + Folders + Labels + Color tags | Full metadata CRUD, category tab-notch colors render |

**Milestone 1 gate:** add, organize, search all functional before starting Phase 2.

## Phase 2 — Intelligence (Weeks 6–10): OCR + Automation
Share sheet → ML Kit OCR + worker → thumbnails → dedup alerts → background scanner (WorkManager + ContentObserver + folder monitor).
**Milestone 2 gate:** OCR search demonstrably finds text in a photo (test with a real whiteboard photo, not synthetic data).

## Phase 3 — Security & Viewing (Weeks 11–14): Lock + Reader
Biometric lock (app/folder/document) → in-app PDF reader → image/text viewer + external handoff → Priority Mode + bidirectional rename.
**Milestone 3 gate:** run full §4.2.5 security test suite before this phase is marked done — biometric bypass attempt must fail.

## Phase 4 — Polish & Release (Weeks 15–18): Production
Batch import + notifications → dark mode/OLED/accessibility → JSON backup + privacy policy + Play listing → beta test (20 users) + fixes.
**Milestone 4 gate:** < 1% crash rate, privacy policy live at a public URL, Data Safety form matches privacy policy exactly (see COMPLIANCE-CHECKLIST.md).

## Phase 5 — Post-Launch (Weeks 19–24)
Feedback/hotfixes → home screen widget → on-device auto-categorization R&D (v3.0 prototype only, not shipped in v2.x).

## Cross-Phase, Not Deferrable
These aren't a separate phase — they must land inside the phase where the underlying feature ships, not bolted on later:
- Onboarding/permissions explainer → ships with Phase 1 Week 2 (before SAF picker goes live to real users)
- Biometric lock onboarding flow → ships with Phase 3 Week 11 (same week as lock screen itself)
- `AccessLostBanner` (URI Health UI surface) → ships with Phase 3, since it depends on lock/folder state being real
- Empty states per screen → ship with the screen, not retrofitted in Phase 4 polish

## Weekly Discipline
- End of each week: run the unit test suite for that week's modules (TRD.md §5) before moving on
- Don't start a new week's tasks with a failing test suite carried over
- Log any deviation from ARCHITECTURE.md §4 trade-offs in a dated note — don't silently drift from a frozen decision
