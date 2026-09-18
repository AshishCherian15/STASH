package com.ashish.stash.ui.feature.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ashish.stash.core.backup.BackupManager
import com.ashish.stash.core.database.entity.CategoryEntity
import com.ashish.stash.core.database.entity.FolderEntity
import com.ashish.stash.core.database.entity.LabelEntity
import com.ashish.stash.core.database.repository.DocumentRepository
import com.ashish.stash.core.preferences.PreferencesManager
import com.ashish.stash.ui.theme.ThemeManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val documentRepository: DocumentRepository,
    private val preferencesManager: PreferencesManager,
    private val themeManager: ThemeManager,
    private val backupManager: BackupManager
) : ViewModel() {

    private val _message = MutableStateFlow<String?>(null)
    val message = _message.asStateFlow()

    val uiState: StateFlow<SettingsUiState> = combine(
        documentRepository.observeAllCategories(),
        documentRepository.observeAllFolders(showLocked = true),
        documentRepository.observeAllLabels(),
        preferencesManager.userData
    ) { categories, folders, labels, userData ->
        SettingsUiState(
            categories = categories,
            folders = folders,
            labels = labels,
            isLoading = false,
            preventScreenshots = userData.preventScreenshots,
            autoLockTimeoutMillis = userData.autoLockTimeoutMillis,
            darkTheme = userData.darkTheme,
            dynamicColor = userData.dynamicColor
        )
    }
    .onStart { emit(SettingsUiState(isLoading = true)) }
    .stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SettingsUiState()
    )

    fun addCategory(name: String, color: String) {
        viewModelScope.launch {
            documentRepository.insertCategory(CategoryEntity(name = name, color = color))
        }
    }

    fun deleteCategory(category: CategoryEntity) {
        viewModelScope.launch {
            documentRepository.deleteCategory(category)
        }
    }

    fun updateCategory(category: CategoryEntity) {
        viewModelScope.launch {
            documentRepository.updateCategory(category)
        }
    }

    fun addFolder(name: String) {
        viewModelScope.launch {
            documentRepository.insertFolder(FolderEntity(name = name))
        }
    }

    fun deleteFolder(folder: FolderEntity) {
        viewModelScope.launch {
            documentRepository.deleteFolder(folder)
        }
    }

    fun updateFolder(folder: FolderEntity) {
        viewModelScope.launch {
            documentRepository.updateFolder(folder)
        }
    }

    fun addLabel(name: String) {
        viewModelScope.launch {
            documentRepository.insertLabel(LabelEntity(name = name))
        }
    }

    fun deleteLabel(label: LabelEntity) {
        viewModelScope.launch {
            documentRepository.deleteLabel(label)
        }
    }

    fun updateLabel(label: LabelEntity) {
        viewModelScope.launch {
            documentRepository.updateLabel(label)
        }
    }

    fun setPreventScreenshots(enabled: Boolean) {
        viewModelScope.launch {
            preferencesManager.setPreventScreenshots(enabled)
        }
    }

    fun setAutoLockTimeoutMillis(timeout: Long) {
        viewModelScope.launch {
            preferencesManager.setAutoLockTimeoutMillis(timeout)
        }
    }

    fun setDarkTheme(enabled: Boolean) {
        viewModelScope.launch {
            preferencesManager.setDarkTheme(enabled)
            themeManager.setDarkTheme(enabled)
        }
    }

    fun setDynamicColor(enabled: Boolean) {
        viewModelScope.launch {
            preferencesManager.setDynamicColor(enabled)
        }
    }

    fun exportBackup() {
        viewModelScope.launch {
            val result = backupManager.exportBackup()
            _message.value = result
        }
    }

    fun importBackup(jsonString: String) {
        viewModelScope.launch {
            val result = backupManager.importBackup(jsonString)
            _message.value = result
        }
    }

    fun clearMessage() {
        _message.value = null
    }
}
