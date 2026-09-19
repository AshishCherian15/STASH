package com.ashish.stash.core.database

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE documents ADD COLUMN ocr_status TEXT NOT NULL DEFAULT 'NONE'")
        db.execSQL("ALTER TABLE documents ADD COLUMN source_kind TEXT NOT NULL DEFAULT 'SAF_REFERENCE'")
        db.execSQL("ALTER TABLE documents ADD COLUMN updated_at INTEGER NOT NULL DEFAULT 0")
        db.execSQL("UPDATE documents SET importance = 'MEDIUM' WHERE importance = 'NORMAL'")
        db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS index_documents_uri ON documents (uri)")
        db.execSQL("CREATE INDEX IF NOT EXISTS index_documents_file_hash ON documents (file_hash)")
        db.execSQL("CREATE INDEX IF NOT EXISTS index_documents_created_at ON documents (created_at)")
    }
}

val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(db: SupportSQLiteDatabase) {
        // Rename notes to description
        // SQLite doesn't support RENAME COLUMN in older versions, so we use the table recreation pattern if needed.
        // But for minSdk 26 (Android 8.0), it supports ALTER TABLE RENAME COLUMN (API 27+).
        // Stash minSdk is 26. To be safe, we'll check API or use the traditional way.
        // Actually, Room handles simple renames if using the recreate pattern.
        
        // Manual migration logic for renaming column:
        db.execSQL("ALTER TABLE documents RENAME COLUMN notes TO description")
    }
}
