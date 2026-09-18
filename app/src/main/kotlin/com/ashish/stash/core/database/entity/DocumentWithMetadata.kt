package com.ashish.stash.core.database.entity

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class DocumentWithMetadata(
    @Embedded val document: DocumentEntity,
    
    @Relation(
        parentColumn = "category_id",
        entityColumn = "category_id"
    )
    val category: CategoryEntity?,
    
    @Relation(
        parentColumn = "folder_id",
        entityColumn = "folder_id"
    )
    val folder: FolderEntity?,
    
    @Relation(
        associateBy = Junction(DocumentLabelEntity::class),
        parentColumn = "document_id",
        entityColumn = "label_id"
    )
    val labels: List<LabelEntity>,
    
    @Relation(
        parentColumn = "document_id",
        entityColumn = "document_id"
    )
    val resourceLinks: List<ResourceLinkEntity>
)
