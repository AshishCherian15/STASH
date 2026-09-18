package com.ashish.stash.domain.usecase.document

import com.ashish.stash.core.database.repository.DocumentRepository
import javax.inject.Inject

class SetDocumentColorTagUseCase @Inject constructor(
    private val repository: DocumentRepository
) {
    suspend operator fun invoke(documentId: Long, colorTag: String?) {
        val document = repository.getDocumentById(documentId)
        if (document != null) {
            repository.updateDocument(document.copy(colorTag = colorTag))
        }
    }
}
