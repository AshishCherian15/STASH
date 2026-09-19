package com.ashish.stash.core.database.entity

import androidx.room.*

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
        Index(value = ["uri"], unique = true),
        Index(value = ["file_hash"]),
        Index(value = ["category_id"]),
        Index(value = ["folder_id"]),
        Index(value = ["is_locked"]),
        Index(value = ["created_at"]),
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
    
    @ColumnInfo(name = "ocr_status")
    val ocrStatus: OcrStatus = OcrStatus.NONE,
    
    @ColumnInfo(name = "source_kind")
    val sourceKind: SourceKind = SourceKind.SAF_REFERENCE,
    
    @ColumnInfo(name = "color_tag")
    val colorTag: String? = null,
    
    @ColumnInfo(name = "importance")
    val importance: Importance = Importance.MEDIUM,
    
    @ColumnInfo(name = "is_locked")
    val isLocked: Boolean = false,
    
    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis(),
    
    @ColumnInfo(name = "last_opened_at")
    val lastOpenedAt: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "updated_at")
    val updatedAt: Long = System.currentTimeMillis(),
    
    @ColumnInfo(name = "import_source")
    val importSource: String,

    @ColumnInfo(name = "category_name")
    val categoryName: String? = null,
    
    @ColumnInfo(name = "label_names")
    val labelNames: String? = null
)
