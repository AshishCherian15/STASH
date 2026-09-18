package com.ashish.stash.ui.feature.search

import com.ashish.stash.core.database.entity.DocumentWithMetadata

data class SearchUiState(
    val searchQuery: String = "",
    val searchResults: List<DocumentWithMetadata> = emptyList(),
    val isLoading: Boolean = false
)
