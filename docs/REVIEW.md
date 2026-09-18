# Review Checklist — KEEP Resources

Run through this before marking any PR/task complete, not just before a milestone.

## Correctness
- [ ] Matches the module spec in TRD.md — no silent scope changes
- [ ] All DB/file I/O confirmed off main thread (`Dispatchers.IO`)
- [ ] New/changed queries respect the `is_locked` filter (SCHEMA.md constraint) — test with a locked doc present in seed data, not just an empty DB
- [ ] Search ranking changes don't bypass the composite formula (FTS4 rank × recency × importance) without a documented reason

## Privacy/Security
- [ ] No new network call introduced without a TRD.md §4 update
- [ ] No user content (filenames, notes, OCR text, URIs) in any log statement — check `Log.d`/`println` left in from debugging
- [ ] Crash reporting still opt-in, still disabled by default
- [ ] Biometric flow: failure path tested, not just the happy path

## UI/UX Fidelity
- [ ] Uses tokens from design.md — no ad-hoc hex codes or off-palette colors
- [ ] Empty/error states match UI-UX.md copy exactly (not paraphrased) unless a copy change was deliberately approved
- [ ] Touch targets ≥ 48dp, `contentDescription` present on icon-only buttons
- [ ] Tested in both light and true-OLED-black dark mode

## AI-Slop Check (see AI-SLOP-PREVENTION.md for full detail)
- [ ] No invented API calls — every Android/Compose/library API used actually exists and is spelled correctly (verify against docs, don't trust pattern-matched confidence)
- [ ] No redundant abstraction — a single-use interface with one implementation is a smell, not a pattern
- [ ] Comments explain *why*, not restate *what* the code does line-by-line
- [ ] No leftover TODO/placeholder logic disguised as complete (e.g. a function that returns a hardcoded value pretending to be the real implementation)
- [ ] Error handling is specific to the failure mode, not a blanket generic catch-and-log

## Before Approving
- [ ] Tests for this change pass locally (not just "should pass")
- [ ] If this touches a frozen ARCHITECTURE.md decision, the rationale for the change is written down, not just implemented
