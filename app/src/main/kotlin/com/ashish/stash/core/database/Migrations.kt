package com.ashish.stash.core.database

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        // 1. Add new columns to documents table
        db.execSQL("ALTER TABLE documents ADD COLUMN ocr_status TEXT NOT NULL DEFAULT 'NONE'")
        db.execSQL("ALTER TABLE documents ADD COLUMN source_kind TEXT NOT NULL DEFAULT 'SAF_REFERENCE'")
        db.execSQL("ALTER TABLE documents ADD COLUMN updated_at INTEGER NOT NULL DEFAULT 0")
        
        // 2. Fix importance data (NORMAL -> MEDIUM)
        db.execSQL("UPDATE documents SET importance = 'MEDIUM' WHERE importance = 'NORMAL'")
        
        // 3. Create unique index on uri
        // We drop existing indices first to be safe or just create the new one
        db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS index_documents_uri ON documents (uri)")
        
        // 4. Ensure other indices from schema v2 exist
        db.execSQL("CREATE INDEX IF NOT EXISTS index_documents_file_hash ON documents (file_hash)")
        db.execSQL("CREATE INDEX IF NOT EXISTS index_documents_created_at ON documents (created_at)")
    }
}
