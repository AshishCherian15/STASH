package com.ashish.stash.domain.usecase

import com.ashish.stash.core.database.repository.DocumentRepository
import javax.inject.Inject

/**
 * Use case to rename a document.
 * Bidirectional: Updates both display title and original filename.
 */
class RenameDocumentUseCase @Inject constructor(
    private val repository: DocumentRepository
) {
    suspend operator fun invoke(documentId: Long, newName: String) {
        val document = repository.getDocumentById(documentId)
        if (document != null) {
            repository.updateDocument(
                document.copy(
                    displayTitle = newName,
                    originalFilename = newName
                )
            )
        }
    }
}
