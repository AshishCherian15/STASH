package com.ashish.stash.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "documents",
    foreignKeys = [
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["category_id"],
            childColumns = ["category_id"],
            onDelete = ForeignKey.SET_NULL,
        ),
        ForeignKey(
            entity = FolderEntity::class,
            parentColumns = ["folder_id"],
            childColumns = ["folder_id"],
            onDelete = ForeignKey.SET_NULL,
        )
    ],
    indices = [
        Index(value = ["category_id"]),
        Index(value = ["folder_id"]),
        Index(value = ["is_locked"]),
        Index(value = ["last_opened_at"])
    ]
)
data class DocumentEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "document_id")
    val documentId: Long = 0,
    
    @ColumnInfo(name = "uri")
    val uri: String,
    
    @ColumnInfo(name = "display_title")
    val displayTitle: String,
    
    @ColumnInfo(name = "original_filename")
    val originalFilename: String,
    
    @ColumnInfo(name = "mime_type")
    val mimeType: String,
    
    @ColumnInfo(name = "file_hash")
    val fileHash: String,
    
    @ColumnInfo(name = "file_size")
    val fileSize: Long,
    
    @ColumnInfo(name = "category_id")
    val categoryId: Long? = null,
    
    @ColumnInfo(name = "folder_id")
    val folderId: Long? = null,
    
    @ColumnInfo(name = "notes")
    val notes: String? = null,
    
    @ColumnInfo(name = "ocr_text")
    val ocrText: String? = null,
    
    @ColumnInfo(name = "color_tag")
    val colorTag: String? = null,
    
    @ColumnInfo(name = "importance")
    val importance: String,
    
    @ColumnInfo(name = "is_locked")
    val isLocked: Int = 0,
    
    @ColumnInfo(name = "created_at")
    val createdAt: Long,
    
    @ColumnInfo(name = "last_opened_at")
    val lastOpenedAt: Long,
    
    @ColumnInfo(name = "import_source")
    val importSource: String,

    @ColumnInfo(name = "category_name")
    val categoryName: String? = null,
    
    @ColumnInfo(name = "label_names")
    val labelNames: String? = null
)
