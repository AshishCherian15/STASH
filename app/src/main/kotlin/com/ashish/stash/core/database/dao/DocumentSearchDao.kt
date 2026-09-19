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
        WHERE documents_fts MATCH :searchQuery 
        AND (:showLocked = 1 OR (d.is_locked = 0 AND (d.folder_id IS NULL OR (SELECT is_locked FROM folders WHERE folder_id = d.folder_id) = 0)))
    """)
    fun searchDocuments(searchQuery: String, showLocked: Int = 0): Flow<List<DocumentWithMetadata>>
}

fun String.toFtsQuery(): String? {
    val tokens = trim().split(Regex("\\s+"))
        .map { it.replace(Regex("[^\\p{L}\\p{N}]"), "") }
        .filter { it.isNotEmpty() }
    return if (tokens.isEmpty()) null else tokens.joinToString(" ") { "$it*" }
}
