package com.ashish.stash.domain.usecase.folder

import com.ashish.stash.core.database.entity.FolderEntity
import com.ashish.stash.core.database.repository.DocumentRepository
import javax.inject.Inject

class CreateFolderUseCase @Inject constructor(
    private val repository: DocumentRepository
) {
    suspend operator fun invoke(name: String, parentFolderId: Long? = null, isLocked: Boolean = false): Long {
        val folder = FolderEntity(
            name = name,
            parentFolderId = parentFolderId,
            isLocked = if (isLocked) 1 else 0
        )
        return repository.insertFolder(folder)
    }
}
