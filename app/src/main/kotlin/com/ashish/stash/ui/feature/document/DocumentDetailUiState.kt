package com.ashish.stash.ui.feature.document

import com.ashish.stash.core.database.entity.DocumentWithMetadata
import com.ashish.stash.core.database.entity.LabelEntity

data class DocumentDetailUiState(
    val documentWithMetadata: DocumentWithMetadata? = null,
    val allLabels: List<LabelEntity> = emptyList(),
    val isLoading: Boolean = false,
    val isDeleted: Boolean = false,
    val isAccessDenied: Boolean = false
)
