package com.ashish.stash.ui.feature.document

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.ashish.stash.core.database.entity.DocumentLabelEntity
import com.ashish.stash.core.database.entity.DocumentWithMetadata
import com.ashish.stash.core.database.entity.Importance
import com.ashish.stash.core.database.entity.LabelEntity
import com.ashish.stash.core.database.entity.ResourceLinkEntity
import com.ashish.stash.core.database.repository.DocumentRepository
import com.ashish.stash.ui.navigation.Destination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DocumentDetailUiState(
    val documentWithMetadata: DocumentWithMetadata? = null,
    val allLabels: List<LabelEntity> = emptyList(),
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
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            combine(
                documentRepository.observeAllLabels(),
                flow { emit(documentRepository.getDocumentWithMetadataById(documentId, showLocked = true)) }
            ) { labels, doc ->
                DocumentDetailUiState(
                    documentWithMetadata = doc,
                    allLabels = labels,
                    isLoading = false
                )
            }.collect { state ->
                _uiState.value = state
            }
        }
    }

    private fun refreshDocument() {
        viewModelScope.launch {
            val doc = documentRepository.getDocumentWithMetadataById(documentId, showLocked = true)
            _uiState.update { it.copy(documentWithMetadata = doc) }
        }
    }

    fun renameDocument(newTitle: String) {
        viewModelScope.launch {
            val success = documentRepository.renameDocumentPhysical(documentId, newTitle)
            if (success) refreshDocument()
        }
    }

    fun setPriority(priority: String) {
        viewModelScope.launch {
            val importance = try { Importance.valueOf(priority) } catch (e: Exception) { Importance.MEDIUM }
            documentRepository.updateDocumentImportance(documentId, importance)
            refreshDocument()
        }
    }

    fun updateNotes(notes: String) {
        viewModelScope.launch {
            documentRepository.updateDocumentNotes(documentId, notes)
            refreshDocument()
        }
    }

    fun setCategory(categoryId: Long) {
        viewModelScope.launch {
            documentRepository.updateDocumentCategory(documentId, categoryId)
            refreshDocument()
        }
    }

    fun setFolder(folderId: Long) {
        viewModelScope.launch {
            documentRepository.updateDocumentFolder(documentId, folderId)
            refreshDocument()
        }
    }

    fun toggleLabel(labelId: Long) {
        viewModelScope.launch {
            val currentLabels = _uiState.value.documentWithMetadata?.labels ?: emptyList()
            if (currentLabels.any { it.labelId == labelId }) {
                documentRepository.deleteDocumentLabel(documentId, labelId)
            } else {
                documentRepository.insertDocumentLabel(DocumentLabelEntity(documentId, labelId))
            }
            refreshDocument()
        }
    }

    fun createAndAddLabel(name: String) {
        viewModelScope.launch {
            val id = documentRepository.insertLabel(LabelEntity(name = name))
            if (id != -1L) {
                documentRepository.insertDocumentLabel(DocumentLabelEntity(documentId, id))
                refreshDocument()
            }
        }
    }

    fun addResourceLink(url: String) {
        viewModelScope.launch {
            documentRepository.insertResourceLink(ResourceLinkEntity(documentId = documentId, urlOrNote = url))
            refreshDocument()
        }
    }

    fun deleteResourceLink(link: ResourceLinkEntity) {
        viewModelScope.launch {
            documentRepository.deleteResourceLink(link)
            refreshDocument()
        }
    }

    fun toggleLock() {
        viewModelScope.launch {
            val current = _uiState.value.documentWithMetadata?.document ?: return@launch
            documentRepository.updateDocumentLockStatus(documentId, !current.isLocked)
            refreshDocument()
        }
    }

    fun deleteDocument() {
        viewModelScope.launch {
            documentRepository.deleteDocument(documentId)
            _uiState.update { it.copy(isDeleted = true) }
        }
    }
}
