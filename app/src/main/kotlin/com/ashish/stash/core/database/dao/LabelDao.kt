package com.ashish.stash.core.database.dao

import androidx.room.*
import com.ashish.stash.core.database.entity.DocumentLabelEntity
import com.ashish.stash.core.database.entity.LabelEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LabelDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(label: LabelEntity): Long

    @Update
    suspend fun update(label: LabelEntity)

    @Delete
    suspend fun delete(label: LabelEntity)

    @Query("SELECT * FROM labels WHERE label_id = :id")
    suspend fun getById(id: Long): LabelEntity?

    @Query("SELECT * FROM labels WHERE name = :name COLLATE NOCASE")
    suspend fun getByName(name: String): LabelEntity?

    @Query("SELECT * FROM labels ORDER BY name ASC")
    fun observeAll(): Flow<List<LabelEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertDocumentLabel(documentLabel: DocumentLabelEntity)

    @Query("DELETE FROM document_labels WHERE document_id = :documentId AND label_id = :labelId")
    suspend fun deleteDocumentLabel(documentId: Long, labelId: Long)

    @Query("DELETE FROM document_labels WHERE document_id = :documentId")
    suspend fun deleteLabelsForDocument(documentId: Long)

    @Query("""
        SELECT l.* FROM labels l
        JOIN document_labels dl ON l.label_id = dl.label_id
        JOIN documents d ON dl.document_id = d.document_id
        WHERE d.document_id = :documentId AND (:showLocked = 1 OR d.is_locked = 0)
        ORDER BY l.name ASC
    """)
    fun observeLabelsForDocument(documentId: Long, showLocked: Int = 0): Flow<List<LabelEntity>>
}
