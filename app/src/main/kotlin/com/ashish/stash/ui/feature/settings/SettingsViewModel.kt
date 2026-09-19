package com.ashish.stash.ui.feature.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ashish.stash.core.backup.BackupManager
import com.ashish.stash.core.database.entity.CategoryEntity
import com.ashish.stash.core.database.entity.FolderEntity
import com.ashish.stash.core.database.entity.LabelEntity
import com.ashish.stash.core.database.repository.DocumentRepository
import com.ashish.stash.core.preferences.PreferencesManager
import com.ashish.stash.core.security.PinManager
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
    private val backupManager: BackupManager,
    private val pinManager: PinManager
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
            dynamicColor = userData.dynamicColor,
            isPinSet = userData.isPinSet,
            biometricEnabled = userData.biometricEnabled,
            themeColor = userData.themeColor,
            fontFamily = userData.fontFamily,
            fontSizeScale = userData.fontSizeScale
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

    fun updateCategory(category: CategoryEntity) {
        viewModelScope.launch {
            documentRepository.updateCategory(category)
        }
    }

    fun deleteCategory(category: CategoryEntity) {
        viewModelScope.launch {
            documentRepository.deleteCategory(category)
        }
    }

    fun addFolder(name: String) {
        viewModelScope.launch {
            documentRepository.insertFolder(FolderEntity(name = name))
        }
    }

    fun updateFolder(folder: FolderEntity) {
        viewModelScope.launch {
            documentRepository.updateFolder(folder)
        }
    }

    fun deleteFolder(folder: FolderEntity) {
        viewModelScope.launch {
            documentRepository.deleteFolder(folder)
        }
    }

    fun addLabel(name: String) {
        viewModelScope.launch {
            documentRepository.insertLabel(LabelEntity(name = name))
        }
    }

    fun updateLabel(label: LabelEntity) {
        viewModelScope.launch {
            documentRepository.updateLabel(label)
        }
    }

    fun deleteLabel(label: LabelEntity) {
        viewModelScope.launch {
            documentRepository.deleteLabel(label)
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

    fun updatePin(oldPin: String?, newPin: String) {
        viewModelScope.launch {
            val userData = preferencesManager.userData.first()
            if (userData.isPinSet) {
                if (oldPin == null || !pinManager.verifyPin(oldPin, userData.pinHash!!, userData.pinSalt!!)) {
                    _message.value = "Incorrect old PIN"
                    return@launch
                }
            }
            val result = pinManager.hashPin(newPin)
            preferencesManager.setPin(result.hash, result.salt)
            _message.value = "PIN updated successfully"
        }
    }

    fun disablePin(currentPin: String) {
        viewModelScope.launch {
            val userData = preferencesManager.userData.first()
            if (pinManager.verifyPin(currentPin, userData.pinHash!!, userData.pinSalt!!)) {
                preferencesManager.setPin(null, null)
                _message.value = "PIN disabled"
            } else {
                _message.value = "Incorrect PIN"
            }
        }
    }

    fun setBiometricEnabled(enabled: Boolean) {
        viewModelScope.launch {
            preferencesManager.setBiometricEnabled(enabled)
        }
    }

    fun setDarkTheme(enabled: Boolean) {
        viewModelScope.launch {
            preferencesManager.setDarkTheme(enabled)
            themeManager.setDarkTheme(enabled)
        }
    }

    fun setThemeColor(color: String) {
        viewModelScope.launch {
            preferencesManager.setThemeColor(color)
        }
    }

    fun setFontFamily(font: String) {
        viewModelScope.launch {
            preferencesManager.setFontFamily(font)
        }
    }

    fun setFontSizeScale(scale: Float) {
        viewModelScope.launch {
            preferencesManager.setFontSizeScale(scale)
        }
    }

    fun setDynamicColor(enabled: Boolean) {
        viewModelScope.launch {
            preferencesManager.setDynamicColor(enabled)
        }
    }

    fun exportBackup() {
        viewModelScope.launch {
            _message.value = backupManager.exportBackup()
        }
    }

    fun importBackup(jsonString: String) {
        viewModelScope.launch {
            _message.value = backupManager.importBackup(jsonString)
        }
    }

    fun clearMessage() {
        _message.value = null
    }
}
