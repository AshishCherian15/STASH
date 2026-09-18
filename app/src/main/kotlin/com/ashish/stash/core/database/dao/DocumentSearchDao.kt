package com.ashish.stash.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.ashish.stash.core.database.entity.DocumentWithMetadata
import kotlinx.coroutines.flow.Flow

@Dao
interface DocumentSearchDao {
    @Transaction
    @Query("""
        SELECT d.* FROM documents d
        JOIN documents_fts fts ON d.document_id = fts.rowid
        LEFT JOIN categories c ON d.category_id = c.category_id
        LEFT JOIN folders f ON d.folder_id = f.folder_id
        WHERE documents_fts MATCH :searchQuery AND (:showLocked = 1 OR d.is_locked = 0)
    """)
    fun searchDocuments(searchQuery: String, showLocked: Int = 0): Flow<List<DocumentWithMetadata>>
}
