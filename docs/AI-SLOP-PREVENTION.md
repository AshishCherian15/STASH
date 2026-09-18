# AI-Slop Prevention — KEEP Resources

Specific failure modes to watch for when code/copy/design is AI-generated (by Claude Code or otherwise), on top of REVIEW.md's general checklist.

## Code
- **Invented APIs.** The most common failure: a plausible-looking Android/Compose/Room/ML Kit call that doesn't actually exist, or exists with a different signature. Verify against current docs before trusting it, especially for less-common APIs (`DocumentsContract`, `PdfRenderer` edge methods, `BiometricManager` variants).
- **Over-abstraction.** A `Repository` interface with exactly one implementation and no planned second one is premature. This codebase has a small, known module set (ARCHITECTURE.md §2) — don't add abstraction layers "for flexibility" that aren't justified by an actual second use case.
- **Generic error handling.** `catch (e: Exception) { Log.e(...) }` everywhere is a tell. Each failure mode in DEBUG.md §2 has a specific, meaningful response (retry, reconnect prompt, fallback) — write to that, not a catch-all.
- **Placeholder logic disguised as done.** A function that returns `emptyList()` or a hardcoded value "for now" should be flagged as incomplete in the PR description, not silently merged as if it's the real implementation.
- **Comment noise.** `// increment counter` above `count++` adds nothing. Comments should explain *why* a non-obvious choice was made (e.g. why OCR batches are capped at 5, why locked-doc filtering happens at the DAO layer) — reference the doc that explains it (TRD.md, SCHEMA.md) instead of re-explaining inline.
- **Unnecessary dependencies.** Adding a library for something Room/Compose/AndroidX already does natively inflates APK size against the < 15MB target (TRD.md §3) and contradicts the "avoid third-party bloat" decisions already made in ARCHITECTURE.md §4.

## Copy / UX Text
- **Generic empty-state copy.** "No items found" is slop. UI-UX.md's empty-state table has specific, situation-aware copy — use it verbatim, don't regenerate a blander version.
- **Marketing tone in a utility app.** Avoid "Unlock the power of..." or "Seamlessly..." language anywhere in-app. The product's whole identity is plain, trustworthy, unglamorous (design.md §1) — copy should match: direct, factual, no hype adjectives.
- **Vague permission rationales.** "This app needs storage access to function" is the generic version Play Store flags as low-quality. Use the specific rationale copy in UI-UX.md §5, which names exactly what and why.

## Design
- **Template-default aesthetics.** Generic rounded-card-on-white-background SaaS look is explicitly rejected in design.md §1 — if a generated screen looks like it could be any dashboard app, it's off-brand. Check against the "Vault & Ledger" tokens and the tab-notch device before accepting a mockup.
- **Icon inconsistency.** Mixing Material Symbols with a differently-weighted third-party icon pack mid-screen reads as unfinished. One primary set (Material Symbols), documented fallback only (design.md §2).
- **Photography where illustration belongs, or vice versa.** Track A (app) is flat/vector only; Track B (case-study page) is where photography is allowed. Don't let a generated mockup blend the two.

## Process Check (run before accepting any AI-generated batch of work)
1. Does it match an existing doc (PRD/TRD/SCHEMA/ARCHITECTURE/design/UI-UX/FRONTEND), or does it quietly introduce a new pattern? New patterns need explicit sign-off, not silent inclusion.
2. Would a human reviewer unfamiliar with "AI slop" patterns still catch this on a normal review pass? If the only reason it looks fine is unfamiliarity with these specific tells, it needs a second look.
3. Is there anything that's technically plausible but was never actually verified (an API call, a library method, a stat)? Verify, don't trust fluency.
