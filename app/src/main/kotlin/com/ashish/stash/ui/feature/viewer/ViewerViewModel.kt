package com.ashish.stash.ui.feature.viewer

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.ashish.stash.core.database.entity.DocumentEntity
import com.ashish.stash.core.database.repository.DocumentRepository
import com.ashish.stash.core.security.SecuritySessionManager
import com.ashish.stash.ui.navigation.Destination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ViewerUiState(
    val document: DocumentEntity? = null,
    val isLoading: Boolean = false,
    val isAccessDenied: Boolean = false
)

@HiltViewModel
class ViewerViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val documentRepository: DocumentRepository,
    private val securitySessionManager: SecuritySessionManager
) : ViewModel() {

    private val documentId = savedStateHandle.toRoute<Destination.Viewer>().documentId

    private val _uiState = MutableStateFlow(ViewerUiState(isLoading = true))
    val uiState = _uiState.asStateFlow()

    init {
        observeSecurity()
    }

    private fun observeSecurity() {
        viewModelScope.launch {
            securitySessionManager.itemsUnlocked.collectLatest { unlocked ->
                loadDocument(unlocked)
            }
        }
    }

    private suspend fun loadDocument(showLocked: Boolean) {
        val doc = documentRepository.getDocumentById(documentId)
        if (doc != null) {
            if (doc.isLocked && !showLocked) {
                _uiState.update { it.copy(document = null, isAccessDenied = true, isLoading = false) }
            } else {
                _uiState.update { it.copy(document = doc, isAccessDenied = false, isLoading = false) }
                documentRepository.updateDocumentLastOpened(documentId, System.currentTimeMillis())
            }
        } else {
            _uiState.update { it.copy(document = null, isAccessDenied = false, isLoading = false) }
        }
    }
}
