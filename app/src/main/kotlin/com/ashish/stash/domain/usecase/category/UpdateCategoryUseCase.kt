package com.ashish.stash.domain.usecase.category

import com.ashish.stash.core.database.entity.CategoryEntity
import com.ashish.stash.core.database.repository.DocumentRepository
import javax.inject.Inject

class UpdateCategoryUseCase @Inject constructor(
    private val repository: DocumentRepository
) {
    suspend operator fun invoke(category: CategoryEntity) {
        repository.updateCategory(category)
    }
}
