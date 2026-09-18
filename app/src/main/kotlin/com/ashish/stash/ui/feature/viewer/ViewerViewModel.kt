package com.ashish.stash.ui.feature.viewer

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.ashish.stash.core.database.entity.DocumentEntity
import com.ashish.stash.core.database.repository.DocumentRepository
import com.ashish.stash.ui.navigation.Destination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ViewerUiState(
    val document: DocumentEntity? = null,
    val isLoading: Boolean = false
)

@HiltViewModel
class ViewerViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val documentRepository: DocumentRepository
) : ViewModel() {

    private val documentId = savedStateHandle.toRoute<Destination.Viewer>().documentId

    private val _uiState = MutableStateFlow(ViewerUiState(isLoading = true))
    val uiState = _uiState.asStateFlow()

    init {
        loadDocument()
    }

    private fun loadDocument() {
        viewModelScope.launch {
            val doc = documentRepository.getDocumentById(documentId)
            _uiState.update { it.copy(document = doc, isLoading = false) }
            
            // Update last opened
            documentRepository.updateDocumentLastOpened(documentId, System.currentTimeMillis())
        }
    }
}
