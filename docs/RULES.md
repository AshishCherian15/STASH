# Rules — KEEP Resources

For Claude Code / any AI-assisted session working in this repo. Read this before writing code, not after.

## 1. Planning Gate
- No implementation before PRD.md and TRD.md exist and are approved. If either is missing or a task contradicts them, stop and flag it — don't proceed on assumption.
- Any deviation from ARCHITECTURE.md §4 (frozen trade-offs) requires a written rationale in that file, not a silent substitution.

## 2. Code Conventions
- Kotlin official style guide (4-space indent, trailing commas in multi-line calls)
- ViewModels: one per screen, `UiState` as a single immutable data class, exposed via `StateFlow` — never expose mutable state or raw `LiveData`
- UseCases: single responsibility, one verb-noun name (`ImportDocumentUseCase`, not `DocumentManager`)
- No business logic inside Composables — if a Composable has an `if` that isn't purely about layout, it belongs in the ViewModel
- All DB/file/network-adjacent work on `Dispatchers.IO`, verified in code review — this is a correctness requirement, not a style preference (see TRD.md §5)

## 3. Naming
- Tables/columns: `snake_case` (matches SCHEMA.md exactly — don't rename columns ad hoc during implementation without updating SCHEMA.md in the same commit)
- Compose files: `PascalCase.kt` matching the top-level composable name
- Test files: `<ClassUnderTest>Test.kt`

## 4. Commit Discipline
- One logical change per commit — a schema change and a UI change for the same feature are two commits
- Commit message references the PLAN.md phase/week it belongs to when applicable

## 5. Privacy Invariants (never violate, no exceptions for "just testing")
- No network call may be added without updating TRD.md §4 and re-confirming against PRD.md's zero-data-collection claim
- Crash reporting stays opt-in and disabled by default — never flip this default "temporarily" for debugging
- Locked document content (including OCR text) never returned by a query unless the caller has passed an authenticated-session flag — see SCHEMA.md's non-obvious constraint

## 6. When Unsure
Stop and ask rather than guessing on: schema changes, new dependencies, anything touching the security boundary in ARCHITECTURE.md §6, or any UI copy in UI-UX.md's rationale/error tables (that copy was written deliberately, not placeholder text).
