# Local Data Schema — KEEP Resources

No backend — this is the on-device Room/SQLite schema. 7 tables, matching blueprint §3.2.3 indexed fields and the modules that read/write them.

> Field names below are inferred from the functional spec (indexed fields, module I/O) to give implementation a concrete starting point — confirm/adjust column types during Week 1 build (blueprint Phase 1) rather than treating this as frozen, unlike PRD/TRD.

## 1. `documents` (core table)
| Column | Type | Notes |
|---|---|---|
| `document_id` | INTEGER PK | Autoincrement |
| `uri` | TEXT | Persisted SAF URI |
| `display_title` | TEXT | User-editable virtual title |
| `original_filename` | TEXT | From SAF, synced after physical rename |
| `mime_type` | TEXT | |
| `file_hash` | TEXT | MD5, for dedup |
| `file_size` | INTEGER | Bytes, paired with hash for dedup check |
| `category_id` | INTEGER FK | → `categories.category_id` |
| `folder_id` | INTEGER FK | → `folders.folder_id`, nullable |
| `notes` | TEXT | Nullable |
| `ocr_text` | TEXT | Nullable, populated by OCR worker |
| `color_tag` | TEXT | Enum-like string |
| `importance` | TEXT | LOW / MEDIUM / HIGH / CRITICAL |
| `is_locked` | INTEGER | Boolean (document-level lock) |
| `created_at` | INTEGER | Epoch ms |
| `last_opened_at` | INTEGER | Epoch ms, drives search recency weight |
| `import_source` | TEXT | SHARE / PICKER / AUTO_SCAN / FOLDER_MONITOR |

## 2. `documents_fts` (FTS4 virtual table)
Mirrors `display_title`, `original_filename`, `notes`, `ocr_text`, `category_name`, `label_names` per blueprint §3.2.3. Kept in sync via Room's `@Fts4` content-table linkage to `documents` — do not write to this table directly outside the sync path.

## 3. `categories`
| Column | Type | Notes |
|---|---|---|
| `category_id` | INTEGER PK | |
| `name` | TEXT | Unique |
| `color` | TEXT | Drives category badge + folder-tab notch color per design.md |

## 4. `folders`
| Column | Type | Notes |
|---|---|---|
| `folder_id` | INTEGER PK | |
| `name` | TEXT | |
| `parent_folder_id` | INTEGER FK | Nullable, self-referencing for nesting |
| `is_locked` | INTEGER | Boolean (folder-level lock) |

## 5. `labels`
| Column | Type | Notes |
|---|---|---|
| `label_id` | INTEGER PK | |
| `name` | TEXT | Unique, user-created |

## 6. `document_labels` (junction table)
| Column | Type | Notes |
|---|---|---|
| `document_id` | INTEGER FK | → `documents.document_id` |
| `label_id` | INTEGER FK | → `labels.label_id` |
| | | Composite PK (`document_id`, `label_id`), many-to-many |

## 7. `resource_links`
| Column | Type | Notes |
|---|---|---|
| `link_id` | INTEGER PK | |
| `document_id` | INTEGER FK | → `documents.document_id` |
| `url_or_note` | TEXT | User-added related link/reference |

## Indices (required, not optional)
- `documents(category_id)`, `documents(folder_id)` — filter combo performance
- `documents(is_locked)` — must be filterable fast since locked docs are excluded from every search pre-filter
- `documents(last_opened_at)` — recency weight in ranking formula
- FTS4 index on `documents_fts` per above

## Non-obvious constraint
`is_locked` filtering happens at the **query layer** (DAO `WHERE is_locked = 0` on every search/list query unless explicitly unlocked in session), not filtered client-side after fetch — locked doc content (including OCR text) must never leave the DAO layer unfiltered, or search could leak snippets of locked content in results.
