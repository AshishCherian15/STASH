package com.ashish.stash.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "resource_links",
    foreignKeys = [
        ForeignKey(
            entity = DocumentEntity::class,
            parentColumns = ["document_id"],
            childColumns = ["document_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["document_id"])]
)
data class ResourceLinkEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "link_id")
    val linkId: Long = 0,
    @ColumnInfo(name = "document_id")
    val documentId: Long,
    @ColumnInfo(name = "url_or_note")
    val urlOrNote: String
)
