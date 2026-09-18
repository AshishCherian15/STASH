package com.ashish.stash.ui.feature.document

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.ashish.stash.core.database.entity.DocumentWithMetadata
import com.ashish.stash.core.database.entity.ResourceLinkEntity
import com.ashish.stash.core.database.repository.DocumentRepository
import com.ashish.stash.ui.navigation.Destination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DocumentDetailUiState(
    val documentWithMetadata: DocumentWithMetadata? = null,
    val isLoading: Boolean = false,
    val isDeleted: Boolean = false
)

@HiltViewModel
class DocumentDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val documentRepository: DocumentRepository
) : ViewModel() {

    private val documentId = savedStateHandle.toRoute<Destination.DocumentDetail>().documentId

    private val _uiState = MutableStateFlow(DocumentDetailUiState(isLoading = true))
    val uiState = _uiState.asStateFlow()

    init {
        loadDocument()
    }

    private fun loadDocument() {
        viewModelScope.launch {
            val doc = documentRepository.getDocumentWithMetadataById(documentId, showLocked = true)
            _uiState.update { it.copy(documentWithMetadata = doc, isLoading = false) }
        }
    }

    fun renameDocument(newTitle: String) {
        viewModelScope.launch {
            val success = documentRepository.renameDocumentPhysical(documentId, newTitle)
            if (success) {
                loadDocument()
            }
        }
    }

    fun setPriority(priority: String) {
        viewModelScope.launch {
            val current = _uiState.value.documentWithMetadata?.document ?: return@launch
            documentRepository.updateDocument(current.copy(importance = priority))
            loadDocument()
        }
    }

    fun updateNotes(notes: String) {
        viewModelScope.launch {
            val current = _uiState.value.documentWithMetadata?.document ?: return@launch
            documentRepository.updateDocument(current.copy(notes = notes))
            loadDocument()
        }
    }

    fun addResourceLink(url: String) {
        viewModelScope.launch {
            documentRepository.insertResourceLink(ResourceLinkEntity(documentId = documentId, urlOrNote = url))
            loadDocument()
        }
    }

    fun deleteResourceLink(link: ResourceLinkEntity) {
        viewModelScope.launch {
            documentRepository.deleteResourceLink(link)
            loadDocument()
        }
    }

    fun toggleLock() {
        viewModelScope.launch {
            val current = _uiState.value.documentWithMetadata?.document ?: return@launch
            documentRepository.updateDocumentLockStatus(documentId, if (current.isLocked == 1) 0 else 1)
            loadDocument()
        }
    }

    fun deleteDocument() {
        viewModelScope.launch {
            val current = _uiState.value.documentWithMetadata?.document ?: return@launch
            documentRepository.deleteDocument(current.documentId)
            _uiState.update { it.copy(isDeleted = true) }
        }
    }
}
