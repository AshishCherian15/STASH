package com.ashish.stash.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Fts4

@Fts4(contentEntity = DocumentEntity::class)
@Entity(tableName = "documents_fts")
data class DocumentFtsEntity(
    @ColumnInfo(name = "display_title")
    val displayTitle: String,
    
    @ColumnInfo(name = "original_filename")
    val originalFilename: String,
    
    @ColumnInfo(name = "notes")
    val notes: String?,
    
    @ColumnInfo(name = "ocr_text")
    val ocrText: String?,
    
    @ColumnInfo(name = "category_name")
    val categoryName: String?,
    
    @ColumnInfo(name = "label_names")
    val labelNames: String?
)
