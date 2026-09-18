package com.ashish.stash.domain.usecase

import android.net.Uri
import com.ashish.stash.core.database.entity.DocumentEntity
import com.ashish.stash.core.database.repository.DocumentRepository
import com.ashish.stash.core.saf.SafUriManager
import javax.inject.Inject

class ImportDocumentUseCase @Inject constructor(
    private val repository: DocumentRepository,
    private val safUriManager: SafUriManager
) {
    suspend operator fun invoke(uri: Uri): Long {
        val metadata = safUriManager.queryMetadata(uri) ?: return -1
        safUriManager.takePersistablePermission(uri)
        
        val now = System.currentTimeMillis()
        val document = DocumentEntity(
            uri = uri.toString(),
            displayTitle = metadata.filename,
            originalFilename = metadata.filename,
            mimeType = metadata.mimeType,
            fileHash = "", // In a full implementation, we'd calculate this
            fileSize = metadata.size,
            importance = "NORMAL",
            isLocked = 0,
            createdAt = now,
            lastOpenedAt = now,
            importSource = "PICKER"
        )
        return repository.insertDocument(document)
    }

    suspend operator fun invoke(document: DocumentEntity): Long {
        return repository.insertDocument(document)
    }
}
