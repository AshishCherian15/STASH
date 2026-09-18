package com.ashish.stash.domain.usecase

import com.ashish.stash.core.database.entity.DocumentWithMetadata
import com.ashish.stash.core.database.entity.Importance
import com.ashish.stash.core.database.repository.DocumentRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Use case to search documents with composite scoring.
 * Scores are based on title matching, importance level, and recency.
 */
class SearchDocumentsUseCase @Inject constructor(
    private val repository: DocumentRepository
) {
    operator fun invoke(query: String, showLocked: Boolean = false): Flow<List<DocumentWithMetadata>> {
        return repository.searchDocuments(query, showLocked).map { results ->
            results.sortedByDescending { docWithMetadata ->
                calculateScore(docWithMetadata, query)
            }
        }
    }

    private fun calculateScore(docWithMetadata: DocumentWithMetadata, query: String): Double {
        val doc = docWithMetadata.document
        var score = 0.0

        // Title match score
        if (doc.displayTitle.contains(query, ignoreCase = true)) {
            score += 10.0
            if (doc.displayTitle.startsWith(query, ignoreCase = true)) {
                score += 5.0
            }
        }

        // Importance score
        score += try {
            when (Importance.valueOf(doc.importance)) {
                Importance.CRITICAL -> 20.0
                Importance.HIGH -> 15.0
                Importance.MEDIUM -> 10.0
                Importance.LOW -> 5.0
            }
        } catch (e: IllegalArgumentException) {
            0.0
        }

        // Recency score (normalized timestamp)
        val now = System.currentTimeMillis()
        val ageDays = (now - doc.lastOpenedAt).toDouble() / (1000.0 * 60.0 * 60.0 * 24.0)
        score += if (ageDays < 1.0) 10.0 else if (ageDays < 7.0) 5.0 else 0.0

        return score
    }
}
