# KEEP Resources — Design System

Two tracks. **Track A** is the app itself (Jetpack Compose). **Track B** is the portfolio case-study page (React). Resources are scoped per track — don't cross-apply web component libraries to Compose screens.

See UI-UX.md for flows/states and FRONTEND.md for implementation structure. This file is tokens + visual direction only.

---

## 1. Design Direction

**Subject:** a private, offline document vault. The vernacular is a library catalog / archive drawer, not a "cloud SaaS dashboard" — files are indexed in place, not uploaded, so the design should feel like a filing cabinet, not a server.

Deliberately distinct from the ReleaseLedger identity (pine/brass, warm bone paper) so the two portfolio projects don't read as reskins of each other — same disciplined paper-and-ink sensibility, different palette and type.

### Color — "Vault & Ledger"
| Token | Hex | Use |
|---|---|---|
| `ink-navy` | `#1C2733` | Primary text, dark surfaces, app bar |
| `limestone` | `#F1EEE6` | Background paper (cooler/greyer than bone paper) |
| `vault-brass` | `#B8873B` | Primary accent — lock icon, active states, FAB |
| `ledger-slate` | `#5C6B73` | Secondary text, metadata, dividers |
| `signal-rust` | `#A6432C` | Critical/high-importance flag |
| `verified-sage` | `#6B8068` | Synced/verified/success state |

No terracotta-on-cream, no neon-on-black, no identical rounded SaaS cards. Document cards get a small colored tab notch (top-left, like a manila folder tab) keyed to category — the one structural device that repeats, and it repeats because the content genuinely has categories.

### Type
- **Headings:** Fraunces (serif, card-catalog character) — deliberately not Space Grotesk, so it doesn't echo ReleaseLedger
- **Body/UI:** Inter — dense metadata screens need a workhorse sans
- **Metadata/mono:** JetBrains Mono — filenames, hashes, timestamps, OCR confidence scores. Reuse across projects; it's functional, not a brand signature.

### Layout principle
Left-aligned, dense information hierarchy — utility tool, not a marketing page. Cards are index-card proportioned (taller than SaaS cards, room for the tab notch + 2 lines of metadata). Avoid uniform border-radius everywhere — only the tab notch and lock badge get a distinct shape language.

---

## 2. Track A — The App (Figma → Jetpack Compose)

| Resource | What it's for |
|---|---|
| Figma | Wireframe + high-fidelity mockups before Compose. Start from Google's official Material 3 Design Kit, reskin with tokens above |
| Google Fonts | Fraunces + Inter, bundled as Compose custom fonts |
| Material Symbols | Primary icon set — outlined for nav/actions, filled for active/selected |
| Flaticon / Vecteezy (icons only) | Only for icons Material Symbols doesn't cover (e.g. vault-door empty-state glyph). License check: free tier requires in-app attribution (Settings > About) or a paid license |

Skip for the app: 21st.dev, uiverse, Material UI (React/CSS, not Compose), Pixabay/Pexels/Unsplash (photography doesn't fit a metadata-dense utility UI).

### Screen frames (for Figma)

**Home dashboard**
```
┌─────────────────────────────┐
│ KEEP          🔍  ⚙          │  app bar, ink-navy
├─────────────────────────────┤
│ 128 docs · 4 locked · 2.1GB │  stat line, ledger-slate
├─────────────────────────────┤
│ ┌─┐Physics.pdf         🔒   │  ← tab notch = category color
│ │▮│ Category: Physics       │
│ └─┘ Opened 2d ago           │
├─────────────────────────────┤
│ ┌─┐Whiteboard_04.jpg        │
│ │▮│ OCR: "revenue target..."│
│ └─┘ Opened 5h ago           │
└─────────────────────────────┘
              ⊕ FAB (vault-brass)
```
Search results: same card, matched term highlighted in `vault-brass`, filter chip row above (category/importance/date/lock status), left-aligned.

Document viewer (PDF): full-bleed content, ink-navy translucent top bar that recedes on scroll, page counter bottom-right in JetBrains Mono.

Lock screen: centered brass lock glyph, limestone background, biometric prompt below. No card chrome — the one screen that should feel different, since it's a security gate, not a content list.

---

## 3. Track B — Portfolio Case-Study Page (React)

This is where 21st.dev / uiverse / Material UI actually apply — Tailwind/CSS patterns for the web page *describing* the project, not the app.

| Resource | What it's for |
|---|---|
| 21st.dev | Case-study layout blocks — bento-grid feature section, stat counters, before/after comparison block for the KEEP-vs-alternatives table |
| uiverse.io | Small interactive elements — animated lock icon on hover, toggle demo of biometric-lock UI inline |
| Material UI | Only if reproducing the compliance matrix or testing-coverage table as an interactive React table instead of static markdown |
| Figma (web frames) | Mock the case-study layout before building, consistent with existing ReleaseLedger site frames |
| Unsplash / Pexels / Pixabay | Hero background texture only — one subtle paper/archive-drawer photo, desaturated to `limestone`. Sparingly, one image max |
| Freepik / Pngtree | Isometric/vector "vault" illustration for the hero, if preferred over a device mockup. License check: attribution required on free tier |
| Google Fonts + Material Symbols | Same tokens as Track A — keeps the page typographically consistent with the app |
| Boxicons | Fallback small UI icons if Material Symbols feels too "Android" for a web context |

### Page structure
1. Hero — headline, one-line problem statement, device mockup (Figma-exported PNG)
2. Problem — fragmentation pain point, plain language
3. Architecture — 2-column trade-off comparison (from ARCHITECTURE.md §4)
4. Feature grid — OCR search, biometric lock, offline-first (21st.dev bento block)
5. Tech stack strip — icon row (check each project's brand-asset license before use)
6. Metrics — search latency, APK size, crash rate targets (Material UI stat cards)
7. CTA — GitHub link, Play Store link (when live)

---

## 4. Licensing Note
Anything from Flaticon, Pngtree, Vecteezy, or Freepik that ships **inside the shipped app or the public portfolio site** counts as commercial use. Free tiers on all four require either an attribution line or a paid plan to go attribution-free — check each asset's individual license before shipping, not just the site's general terms. Material Symbols, Google Fonts, and 21st.dev/uiverse components are open-license and carry no such risk.
