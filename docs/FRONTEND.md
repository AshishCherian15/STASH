# Frontend — KEEP Resources (Jetpack Compose)

See DESIGN.md for tokens/visual system and UI-UX.md for screen flows. This file covers implementation structure only.

## 1. Navigation
Single-Activity, Compose Navigation graph:
```
NavHost
 ├─ home            (start destination)
 ├─ search
 ├─ document/{id}   (detail)
 ├─ viewer/{id}     (PDF/image/text, full-screen, no bottom nav)
 ├─ priority_mode
 ├─ settings
 │   ├─ about
 │   ├─ licenses
 │   ├─ help_faq
 │   └─ privacy
 └─ lock_screen     (overlay destination, can intercept any route)
```
`lock_screen` is not a normal back-stack destination — it's shown as a full-screen overlay gate whenever an app/folder/document-level lock is active and unauthenticated, regardless of what route the user was navigating to. Implement as a top-level conditional wrapper around `NavHost`, not as a pushed route, or back-button can bypass it.

## 2. State Management
- One `ViewModel` per screen, exposing a single `UiState` data class via `StateFlow`
- No business logic in Composables — Composables render state and forward events (`onSearchQueryChanged`, `onDocumentTapped`) up to the ViewModel
- Shared state (current lock status, theme) lives in a `SessionState`/`ThemeState` singleton injected via Hilt, not passed through every screen's constructor

## 3. Component Inventory
| Component | Used in | Notes |
|---|---|---|
| `DocumentCard` | Home, Search, Priority Mode | Single source of truth — don't fork variants per screen; use content params (snippet text, badge) to vary |
| `FilterChipRow` | Search | Category/importance/color/date/lock-status chips |
| `EmptyState` | Home, Search, Priority Mode, Locked folder | Parameterized (icon, title, body, optional CTA) — see UI-UX.md §Empty States for copy per screen |
| `StatChip` | Home | Doc count, locked count, storage used |
| `AccessLostBanner` | Document detail, Home card overlay | Surfaces the URI Health monitor from TRD §4 — this is the UI gap flagged in the compliance review, must ship with Phase 3 |
| `LockGate` | App-level wrapper | Full-screen biometric prompt overlay |

## 4. Theming
- `MaterialTheme` wraps the app with custom `ColorScheme` built from DESIGN.md tokens (light + dark, true OLED black for dark)
- Typography: Fraunces (headings) + Inter (body) + JetBrains Mono (metadata) loaded as Compose custom `FontFamily`, bundled — no network font loading
- Icons: Material Symbols as the default `ImageVector` source; only fall back to a bundled custom vector asset when Material Symbols has no equivalent (see DESIGN.md asset table)

## 5. Performance Rules (Compose-specific)
- `LazyColumn`/`LazyVerticalGrid` for all document lists — never `Column` + `forEach` for card lists
- Stable/immutable data classes for `UiState` to avoid unnecessary recomposition
- Thumbnails loaded via Coil `AsyncImage`, never `Bitmap` decoded synchronously on the composition thread
- PDF viewer: only compose the visible page ± 1, dispose off-screen pages (per TRD §2 non-obvious constraints)

## 6. Accessibility (non-negotiable, ships with Phase 4 per blueprint but verify continuously)
- All icon-only buttons need `contentDescription`
- Touch targets ≥ 48dp
- Dynamic type support — no fixed `sp` values that ignore user font scale
- TalkBack pass required before any milestone is marked complete, not just at the end
