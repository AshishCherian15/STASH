package com.ashish.stash.core.database.dao

import androidx.room.*
import com.ashish.stash.core.database.entity.DocumentEntity
import com.ashish.stash.core.database.entity.DocumentWithMetadata
import com.ashish.stash.core.database.entity.Importance
import com.ashish.stash.core.database.entity.OcrStatus
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

    @Query("""
        SELECT COUNT(*) FROM documents 
        WHERE (is_locked = 0 AND (folder_id IS NULL OR (SELECT is_locked FROM folders WHERE folder_id = documents.folder_id) = 0))
    """)
    fun observeUnlockedCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM documents WHERE is_locked = 1 OR (folder_id IS NOT NULL AND (SELECT is_locked FROM folders WHERE folder_id = documents.folder_id) = 1)")
    fun observeLockedCount(): Flow<Int>

    @Query("UPDATE documents SET last_opened_at = :timestamp WHERE document_id = :id")
    suspend fun updateLastOpened(id: Long, timestamp: Long): Int

    @Query("UPDATE documents SET display_title = :title, updated_at = :timestamp WHERE document_id = :id")
    suspend fun updateTitle(id: Long, title: String, timestamp: Long = System.currentTimeMillis()): Int

    @Query("UPDATE documents SET is_locked = :isLocked, updated_at = :timestamp WHERE document_id = :id")
    suspend fun updateLockStatus(id: Long, isLocked: Boolean, timestamp: Long = System.currentTimeMillis()): Int

    @Query("UPDATE documents SET color_tag = :colorTag, updated_at = :timestamp WHERE document_id = :id")
    suspend fun updateColorTag(id: Long, colorTag: String?, timestamp: Long = System.currentTimeMillis()): Int

    @Query("UPDATE documents SET notes = :notes, updated_at = :timestamp WHERE document_id = :id")
    suspend fun updateNotes(id: Long, notes: String?, timestamp: Long = System.currentTimeMillis()): Int

    @Query("UPDATE documents SET importance = :importance, updated_at = :timestamp WHERE document_id = :id")
    suspend fun updateImportance(id: Long, importance: Importance, timestamp: Long = System.currentTimeMillis()): Int

    @Query("UPDATE documents SET ocr_text = :ocrText, ocr_status = :status, updated_at = :timestamp WHERE document_id = :id")
    suspend fun updateOcrResult(id: Long, ocrText: String?, status: OcrStatus, timestamp: Long = System.currentTimeMillis()): Int

    @Query("UPDATE documents SET category_id = :categoryId, updated_at = :timestamp WHERE document_id = :id")
    suspend fun updateCategory(id: Long, categoryId: Long?, timestamp: Long = System.currentTimeMillis()): Int

    @Query("UPDATE documents SET folder_id = :folderId, updated_at = :timestamp WHERE document_id = :id")
    suspend fun updateFolder(id: Long, folderId: Long?, timestamp: Long = System.currentTimeMillis()): Int

    @Query("""
        SELECT SUM(file_size) FROM documents 
        WHERE (:showLocked = 1 OR (is_locked = 0 AND (folder_id IS NULL OR (SELECT is_locked FROM folders WHERE folder_id = documents.folder_id) = 0)))
    """)
    fun observeTotalSizeBytes(showLocked: Int = 0): Flow<Long?>

    @Query("DELETE FROM documents WHERE document_id = :id")
    suspend fun deleteById(id: Long)
}
