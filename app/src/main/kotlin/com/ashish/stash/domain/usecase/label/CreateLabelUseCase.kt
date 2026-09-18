package com.ashish.stash.domain.usecase.label

import com.ashish.stash.core.database.entity.LabelEntity
import com.ashish.stash.core.database.repository.DocumentRepository
import javax.inject.Inject

class CreateLabelUseCase @Inject constructor(
    private val repository: DocumentRepository
) {
    suspend operator fun invoke(name: String): Long {
        val label = LabelEntity(name = name)
        return repository.insertLabel(label)
    }
}
