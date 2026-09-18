package com.ashish.stash.domain.usecase.folder

import com.ashish.stash.core.database.repository.DocumentRepository
import javax.inject.Inject

class SetFolderLockedUseCase @Inject constructor(
    private val repository: DocumentRepository
) {
    suspend operator fun invoke(folderId: Long, isLocked: Boolean) {
        repository.updateFolderLockStatus(folderId, if (isLocked) 1 else 0)
    }
}
