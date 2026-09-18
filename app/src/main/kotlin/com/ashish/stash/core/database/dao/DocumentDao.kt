package com.ashish.stash.core.database.dao

import androidx.room.*
import com.ashish.stash.core.database.entity.DocumentEntity
import com.ashish.stash.core.database.entity.DocumentWithMetadata
import kotlinx.coroutines.flow.Flow

@Dao
interface DocumentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(document: DocumentEntity): Long

    @Update
    suspend fun update(document: DocumentEntity)

    @Delete
    suspend fun delete(document: DocumentEntity)

    @Query("SELECT * FROM documents WHERE document_id = :id")
    suspend fun getById(id: Long): DocumentEntity?

    @Transaction
    @Query("""
        SELECT * FROM documents 
        WHERE document_id = :id 
        AND (:showLocked = 1 OR (is_locked = 0 AND (folder_id IS NULL OR (SELECT is_locked FROM folders WHERE folder_id = documents.folder_id) = 0)))
    """)
    suspend fun getWithMetadataById(id: Long, showLocked: Int = 0): DocumentWithMetadata?

    /**
     * Observes all documents, respecting both Document-level and Folder-level lock states.
     * Fixes the "Folder Lock Gap" identified in audit.
     */
    @Transaction
    @Query("""
        SELECT * FROM documents 
        WHERE (:showLocked = 1 OR (is_locked = 0 AND (folder_id IS NULL OR (SELECT is_locked FROM folders WHERE folder_id = documents.folder_id) = 0)))
        ORDER BY created_at DESC
    """)
    fun observeAll(showLocked: Int = 0): Flow<List<DocumentWithMetadata>>

    @Query("SELECT COUNT(*) FROM documents WHERE file_hash = :hash AND file_size = :size")
    suspend fun countByHashAndSize(hash: String, size: Long): Int

    @Transaction
    @Query("""
        SELECT * FROM documents 
        WHERE (:showLocked = 1 OR (is_locked = 0 AND (folder_id IS NULL OR (SELECT is_locked FROM folders WHERE folder_id = documents.folder_id) = 0)))
        AND (importance = 'HIGH' OR importance = 'CRITICAL') 
        ORDER BY created_at DESC
    """)
    fun observePriorityWithMetadata(showLocked: Int = 0): Flow<List<DocumentWithMetadata>>

    @Query("SELECT COUNT(*) FROM documents WHERE is_locked = 0")
    fun observeUnlockedCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM documents WHERE is_locked = 1")
    fun observeLockedCount(): Flow<Int>

    @Query("UPDATE documents SET last_opened_at = :timestamp WHERE document_id = :id")
    suspend fun updateLastOpened(id: Long, timestamp: Long): Int

    @Query("UPDATE documents SET display_title = :title WHERE document_id = :id")
    suspend fun updateTitle(id: Long, title: String): Int

    @Query("UPDATE documents SET is_locked = :isLocked WHERE document_id = :id")
    suspend fun updateLockStatus(id: Long, isLocked: Int): Int

    @Query("UPDATE documents SET color_tag = :colorTag WHERE document_id = :id")
    suspend fun updateColorTag(id: Long, colorTag: String?): Int

    @Query("SELECT SUM(file_size) FROM documents")
    fun observeTotalSizeBytes(): Flow<Long?>

    @Query("DELETE FROM documents WHERE document_id = :id")
    suspend fun deleteById(id: Long)
}
