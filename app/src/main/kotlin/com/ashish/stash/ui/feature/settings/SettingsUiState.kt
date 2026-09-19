package com.ashish.stash.ui.feature.settings

import com.ashish.stash.core.database.entity.CategoryEntity
import com.ashish.stash.core.database.entity.FolderEntity
import com.ashish.stash.core.database.entity.LabelEntity

data class SettingsUiState(
    val categories: List<CategoryEntity> = emptyList(),
    val folders: List<FolderEntity> = emptyList(),
    val labels: List<LabelEntity> = emptyList(),
    val isLoading: Boolean = false,
    val preventScreenshots: Boolean = false,
    val autoLockTimeoutMillis: Long = 30000,
    val darkTheme: Boolean = false,
    val dynamicColor: Boolean = true,
    val vaultPin: String? = null,
    val biometricEnabled: Boolean = true,
    val themeColor: String = "BLUE",
    val fontFamily: String = "SANS_SERIF",
    val fontSizeScale: Float = 1.0f
)
