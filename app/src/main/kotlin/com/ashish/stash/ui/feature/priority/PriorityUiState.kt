package com.ashish.stash.ui.feature.priority

import com.ashish.stash.core.database.entity.DocumentWithMetadata

data class PriorityUiState(
    val documents: List<DocumentWithMetadata> = emptyList(),
    val isLoading: Boolean = false
)
