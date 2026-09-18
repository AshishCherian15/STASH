# PRD — KEEP Resources

**Status:** Approved for build · **Version:** 2.0 · **Author:** Ashish

## 1. Problem
Students, professionals, lawyers, researchers, and teachers accumulate hundreds of PDFs, slides, images, and office files across WhatsApp, Telegram, Drive, and email. They can't find them when they need them, because search is filename-only and files are scattered across apps with no shared index.

## 2. Target Users
| Persona | Primary need |
|---|---|
| Student | Fast search across notes/slides before exams; Priority Mode for cramming |
| Professional | Quick retrieval of contracts/reports; biometric lock for client files |
| Lawyer/CA | Confidentiality (biometric lock at document level), audit trail (no cloud upload) |
| Researcher | OCR search across scanned/whiteboard content, rich labeling |
| Teacher | Organize lesson material by category/folder |

## 3. Core Value Proposition
Index documents **in place** (zero extra storage, via Storage Access Framework) with full-text + OCR search, rich metadata, and biometric-gated privacy — entirely offline. No cloud upload, ever.

## 4. Must-Have Features (v2.0 scope)
1. Add documents via SAF picker, share sheet, background scan (Downloads/Documents), and folder monitoring
2. Rich metadata: category, folder, labels, color tag, importance flag, notes, resource links
3. Full-text search (filename, notes, category, labels) + OCR search on images — < 50ms at 5,000 docs
4. Thumbnail previews (PDF page 0, image downsample, text snippet)
5. Duplicate detection via MD5 hash before insert
6. Biometric lock at app / folder / document level
7. In-app viewer: PDF (pinch-zoom), image, text/markdown
8. External handoff to Office apps for DOCX/PPTX/XLSX
9. Bidirectional rename (virtual title + physical file rename)
10. Priority/Exam Mode — filtered view of HIGH/CRITICAL docs only
11. Dark mode with true OLED black
12. JSON backup export/import

## 5. Explicitly Out of Scope (v2.0)
- Any cloud sync/backup (Drive/Dropbox export is v3.0)
- AI auto-categorization (v3.0 R&D)
- In-app annotation/highlighting (v3.0)
- Collaborative/shared vaults (v3.0)
- iOS/cross-platform (v3.0, Kotlin Multiplatform candidate)

## 6. Success Metrics
| Metric | Target |
|---|---|
| Crash rate (beta) | < 1% |
| Beta satisfaction | > 4.0/5.0 |
| Search latency (5K docs) | < 50ms, confirmed in beta |
| APK size | < 15MB |
| Data collected | 0 bytes by default (opt-in crash reporting only) |

## 7. Competitive Differentiation
See Appendix C in blueprint: vs. default file manager (filename-only search, no OCR), Google Drive (cloud-dependent, storage cost), Notion (notes only, cloud), Adobe Acrobat (PDF-only OCR, cloud). KEEP is the only offline, full-text + OCR, zero-storage-overhead option in this comparison set.

## 8. Release Criteria (Milestone 4)
Production-ready on Google Play: all Phase 1–4 deliverables complete, beta tested with 20 users across personas, < 1% crash rate, privacy policy + ToS live, Data Safety form matches privacy policy exactly.

## 9. Open Questions (resolve before Phase 3 starts)
- Confirm DB encryption (SQLCipher) stays optional vs. always-on — currently optional per Appendix B trade-off; revisit if a lawyer/CA persona in beta flags it as a blocker.
- Confirm Firebase Crashlytics opt-in copy is reviewed by someone outside the dev team for clarity (this is a trust-sensitive UI moment).
