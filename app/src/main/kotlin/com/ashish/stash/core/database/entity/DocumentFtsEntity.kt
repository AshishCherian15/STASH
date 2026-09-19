package com.ashish.stash.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Fts4

@Entity(tableName = "documents_fts")
@Fts4(contentEntity = DocumentEntity::class)
data class DocumentFtsEntity(
    @ColumnInfo(name = "display_title")
    val displayTitle: String,
    
    @ColumnInfo(name = "description")
    val description: String?,
    
    @ColumnInfo(name = "ocr_text")
    val ocrText: String?,

    @ColumnInfo(name = "category_name")
    val categoryName: String?,
    
    @ColumnInfo(name = "label_names")
    val labelNames: String?
)
