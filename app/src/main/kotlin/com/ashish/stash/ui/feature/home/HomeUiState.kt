package com.ashish.stash.ui.feature.home

import com.ashish.stash.core.database.entity.DocumentWithMetadata

data class HomeUiState(
    val isLoading: Boolean = true,
    val documents: List<DocumentUiModel> = emptyList(),
    val searchQuery: String = "",
    val stats: HomeStats = HomeStats(),
    val isImporting: Boolean = false,
    val importSuccess: Boolean = false,
    val importError: String? = null
)

data class HomeStats(
    val totalDocuments: Int = 0,
    val lockedDocuments: Int = 0,
    val unlockedDocuments: Int = 0,
    val totalSizeBytes: Long = 0L
)

data class DocumentUiModel(
    val data: DocumentWithMetadata,
    val isAccessible: Boolean = true
)
