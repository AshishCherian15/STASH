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
        db.execSQL("ALTER TABLE documents RENAME COLUMN notes TO description")
    }
}

val MIGRATION_3_4 = object : Migration(3, 4) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE labels ADD COLUMN color TEXT NOT NULL DEFAULT '#748393'")
        db.execSQL("ALTER TABLE folders ADD COLUMN color TEXT NOT NULL DEFAULT '#246EE9'")
    }
}
