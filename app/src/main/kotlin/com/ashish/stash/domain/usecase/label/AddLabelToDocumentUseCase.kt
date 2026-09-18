package com.ashish.stash.domain.usecase.label

import com.ashish.stash.core.database.entity.DocumentLabelEntity
import com.ashish.stash.core.database.repository.DocumentRepository
import javax.inject.Inject

class AddLabelToDocumentUseCase @Inject constructor(
    private val repository: DocumentRepository
) {
    suspend operator fun invoke(documentId: Long, labelId: Long) {
        repository.insertDocumentLabel(DocumentLabelEntity(documentId = documentId, labelId = labelId))
    }
}
