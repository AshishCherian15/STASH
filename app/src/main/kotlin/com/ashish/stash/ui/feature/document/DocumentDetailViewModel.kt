package com.ashish.stash.ui.feature.document

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.ashish.stash.core.database.entity.DocumentLabelEntity
import com.ashish.stash.core.database.entity.Importance
import com.ashish.stash.core.database.entity.LabelEntity
import com.ashish.stash.core.database.entity.ResourceLinkEntity
import com.ashish.stash.core.database.repository.DocumentRepository
import com.ashish.stash.core.security.SecuritySessionManager
import com.ashish.stash.domain.usecase.PhysicalLockDocumentUseCase
import com.ashish.stash.ui.navigation.Destination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class DocumentDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val documentRepository: DocumentRepository,
    private val securitySessionManager: SecuritySessionManager,
    private val physicalLockDocumentUseCase: PhysicalLockDocumentUseCase
) : ViewModel() {

    private val documentId = savedStateHandle.toRoute<Destination.DocumentDetail>().documentId

    private val _uiState = MutableStateFlow<DocumentDetailUiState>(DocumentDetailUiState(isLoading = true))
    val uiState = _uiState.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            securitySessionManager.itemsUnlocked.flatMapLatest { itemsUnlocked ->
                combine(
                    documentRepository.observeAllLabels(),
                    flow { emit(documentRepository.getDocumentWithMetadataById(documentId, showLocked = itemsUnlocked)) }
                ) { labels, doc ->
                    if (doc == null && !itemsUnlocked) {
                        val rawDoc = documentRepository.getDocumentById(documentId)
                        if (rawDoc != null && rawDoc.isLocked) {
                            return@combine DocumentDetailUiState(isAccessDenied = true, isLoading = false)
                        }
                    }
                    
                    DocumentDetailUiState(
                        documentWithMetadata = doc,
                        allLabels = labels,
                        isLoading = false,
                        isAccessDenied = doc == null
                    )
                }
            }.collect { state ->
                _uiState.value = state
            }
        }
    }

    private fun refreshDocument() {
        viewModelScope.launch {
            val itemsUnlocked = securitySessionManager.itemsUnlocked.value
            val doc = documentRepository.getDocumentWithMetadataById(documentId, showLocked = itemsUnlocked)
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

    fun updateDescription(description: String) {
        viewModelScope.launch {
            documentRepository.updateDocumentDescription(documentId, description)
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
            physicalLockDocumentUseCase(documentId, !current.isLocked)
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
