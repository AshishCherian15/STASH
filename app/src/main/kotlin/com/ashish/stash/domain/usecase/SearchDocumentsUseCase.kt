package com.ashish.stash.domain.usecase

import com.ashish.stash.core.database.dao.toFtsQuery
import com.ashish.stash.core.database.entity.DocumentWithMetadata
import com.ashish.stash.core.database.entity.Importance
import com.ashish.stash.core.database.repository.DocumentRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SearchDocumentsUseCase @Inject constructor(
    private val repository: DocumentRepository
) {
    operator fun invoke(query: String, showLocked: Boolean = false): Flow<List<DocumentWithMetadata>> {
        val ftsQuery = query.toFtsQuery() ?: return flowOf(emptyList())
        
        return repository.searchDocuments(ftsQuery, showLocked).map { results ->
            results.sortedByDescending { docWithMetadata ->
                calculateScore(docWithMetadata, query)
            }
        }
    }

    private fun calculateScore(docWithMetadata: DocumentWithMetadata, query: String): Double {
        val doc = docWithMetadata.document
        var score = 0.0

        // 1. Title Exact match / Starts with
        if (doc.displayTitle.equals(query, ignoreCase = true)) {
            score += 100.0
        } else if (doc.displayTitle.startsWith(query, ignoreCase = true)) {
            score += 50.0
        } else if (doc.displayTitle.contains(query, ignoreCase = true)) {
            score += 20.0
        }

        // 2. Importance Multiplier
        score += when (doc.importance) {
            Importance.CRITICAL -> 30.0
            Importance.HIGH -> 15.0
            Importance.MEDIUM -> 5.0
            Importance.LOW -> 0.0
        }

        // 3. Recency
        val now = System.currentTimeMillis()
        val ageDays = (now - doc.lastOpenedAt).toDouble() / (1000.0 * 60.0 * 60.0 * 24.0)
        score += if (ageDays < 1.0) 20.0 else if (ageDays < 7.0) 10.0 else 0.0

        return score
    }
}
