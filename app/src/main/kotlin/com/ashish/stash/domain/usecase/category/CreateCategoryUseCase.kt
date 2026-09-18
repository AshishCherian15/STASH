package com.ashish.stash.domain.usecase.category

import com.ashish.stash.core.database.entity.CategoryEntity
import com.ashish.stash.core.database.repository.DocumentRepository
import javax.inject.Inject

class CreateCategoryUseCase @Inject constructor(
    private val repository: DocumentRepository
) {
    suspend operator fun invoke(name: String, color: String): Long {
        val category = CategoryEntity(name = name, color = color)
        return repository.insertCategory(category)
    }
}
