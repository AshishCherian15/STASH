package com.ashish.stash.ui.feature.home

import com.ashish.stash.core.database.entity.DocumentWithMetadata

enum class ViewMode(val label: String) {
    LARGE_GRID("Large Icons"),
    MEDIUM_GRID("Grid"),
    SMALL_GRID("Small Icons"),
    LIST("List"),
    DETAILS("Details"),
    TILES("Tiles"),
    CONTENT("Content")
}

data class HomeUiState(
    val isLoading: Boolean = false,
    val documents: List<DocumentUiModel> = emptyList(),
    val searchQuery: String = "",
    val viewMode: ViewMode = ViewMode.LIST,
    val isImporting: Boolean = false,
    val importSuccess: Boolean = false,
    val importError: String? = null,
    val stats: HomeStats = HomeStats(),
    val selectedCategoryId: Long? = null,
    val selectedFolderId: Long? = null,
    val selectedLabelId: Long? = null
)

data class DocumentUiModel(
    val data: DocumentWithMetadata,
    val isAccessible: Boolean = true
)

data class HomeStats(
    val totalDocuments: Int = 0,
    val unlockedDocuments: Int = 0,
    val lockedDocuments: Int = 0,
    val totalSizeBytes: Long = 0L
)
