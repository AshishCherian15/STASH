package com.ashish.stash.ui.feature.home

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ashish.stash.core.database.repository.DocumentRepository
import com.ashish.stash.core.saf.SafUriManager
import com.ashish.stash.core.security.SecuritySessionManager
import com.ashish.stash.domain.usecase.ImportDocumentUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: DocumentRepository,
    private val importDocumentUseCase: ImportDocumentUseCase,
    private val safUriManager: SafUriManager,
    private val securitySessionManager: SecuritySessionManager
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _viewMode = MutableStateFlow(ViewMode.LIST)
    val viewMode = _viewMode.asStateFlow()

    private val _importSuccess = MutableStateFlow(false)
    val importSuccess = _importSuccess.asStateFlow()

    private val _importError = MutableStateFlow<String?>(null)
    val importError = _importError.asStateFlow()

    private val _isImporting = MutableStateFlow(false)
    val isImporting = _isImporting.asStateFlow()

    val uiState: StateFlow<HomeUiState> = combine(
        securitySessionManager.isLocked,
        _searchQuery,
        _viewMode
    ) { isLocked, query, mode ->
        Triple(isLocked, query, mode)
    }.flatMapLatest { (isLocked, query, mode) ->
        val showLocked = !isLocked
        val documentsFlow = if (query.isEmpty()) {
            repository.observeAllDocuments(showLocked)
        } else {
            repository.searchDocuments(query, showLocked)
        }
        
        combine(
            documentsFlow,
            repository.observeUnlockedDocumentsCount(),
            repository.observeLockedDocumentsCount(),
            repository.observeTotalSizeBytes()
        ) { documents, unlocked, locked, totalSize ->
            val uiDocs = documents.map { metadata ->
                DocumentUiModel(
                    data = metadata,
                    isAccessible = safUriManager.isUriAccessible(Uri.parse(metadata.document.uri))
                )
            }
            HomeUiState(
                isLoading = false,
                documents = uiDocs,
                searchQuery = query,
                viewMode = mode,
                stats = HomeStats(
                    totalDocuments = unlocked + locked,
                    unlockedDocuments = unlocked,
                    lockedDocuments = locked,
                    totalSizeBytes = totalSize ?: 0L
                )
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = HomeUiState()
    )

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun setViewMode(mode: ViewMode) {
        _viewMode.value = mode
    }

    fun importDocument(uri: Uri) {
        viewModelScope.launch {
            _isImporting.value = true
            _importError.value = null
            try {
                importDocumentUseCase(uri)
                _importSuccess.value = true
            } catch (e: Exception) {
                _importError.value = e.message ?: "Import failed"
            } finally {
                _isImporting.value = false
            }
        }
    }

    fun importDocuments(uris: List<Uri>) {
        viewModelScope.launch {
            _isImporting.value = true
            try {
                uris.forEach { importDocumentUseCase(it) }
                _importSuccess.value = true
            } catch (e: Exception) {
                _importError.value = e.message ?: "Batch import failed"
            } finally {
                _isImporting.value = false
            }
        }
    }

    fun clearImportSuccess() { _importSuccess.value = false }
    fun clearImportError() { _importError.value = null }

    fun deleteDocument(id: Long) {
        viewModelScope.launch {
            repository.deleteDocument(id)
        }
    }
}
