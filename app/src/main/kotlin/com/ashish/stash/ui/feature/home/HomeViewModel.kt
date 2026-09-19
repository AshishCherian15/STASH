package com.ashish.stash.ui.feature.home

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ashish.stash.core.database.repository.DocumentRepository
import com.ashish.stash.core.saf.SafUriManager
import com.ashish.stash.core.security.LockState
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

    private val _filterCategory = MutableStateFlow<Long?>(null)
    private val _filterFolder = MutableStateFlow<Long?>(null)
    private val _filterLabel = MutableStateFlow<Long?>(null)

    private val _importState = MutableStateFlow<ImportState>(ImportState.Idle)
    val importState = _importState.asStateFlow()

    sealed interface ImportState {
        data object Idle : ImportState
        data class Processing(val progress: String) : ImportState
        data class Success(val message: String) : ImportState
        data class Error(val error: String) : ImportState
    }

    val uiState: StateFlow<HomeUiState> = combine(
        securitySessionManager.lockState,
        _searchQuery,
        _viewMode,
        combine(_filterCategory, _filterFolder, _filterLabel) { c, f, l -> Triple(c, f, l) },
        _importState
    ) { lockState, query, mode, filters, impState ->
        Triple(lockState, query, mode) to Triple(filters, impState, Unit)
    }.flatMapLatest { (main, extras) ->
        val (lockState, query, mode) = main
        val (filters, impState, _) = extras
        
        val isLocked = lockState == LockState.Locked
        val showLocked = !isLocked
        
        val documentsFlow = if (query.isNotEmpty()) {
            repository.searchDocuments(query, showLocked)
        } else {
            repository.observeAllDocuments(showLocked)
        }
        
        combine(
            documentsFlow,
            repository.observeUnlockedDocumentsCount(),
            repository.observeLockedDocumentsCount(),
            repository.observeTotalSizeBytes(showLocked)
        ) { documents, unlocked, locked, totalSize ->
            val filteredDocs = documents.filter { doc ->
                val categoryMatch = filters.first == null || doc.category?.categoryId == filters.first
                val folderMatch = filters.second == null || doc.folder?.folderId == filters.second
                val labelMatch = filters.third == null || doc.labels.any { it.labelId == filters.third }
                categoryMatch && folderMatch && labelMatch
            }.map { metadata ->
                DocumentUiModel(
                    data = metadata,
                    isAccessible = safUriManager.isUriAccessible(Uri.parse(metadata.document.uri))
                )
            }
            
            HomeUiState(
                isLoading = lockState == LockState.Loading,
                documents = filteredDocs,
                searchQuery = query,
                viewMode = mode,
                isImporting = impState is ImportState.Processing,
                importSuccess = impState is ImportState.Success,
                importError = (impState as? ImportState.Error)?.error,
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

    fun onSearchQueryChanged(query: String) { _searchQuery.value = query }
    fun setViewMode(mode: ViewMode) { _viewMode.value = mode }
    fun setCategoryFilter(id: Long?) { _filterCategory.value = id; _filterFolder.value = null; _filterLabel.value = null }
    fun setFolderFilter(id: Long?) { _filterFolder.value = id; _filterCategory.value = null; _filterLabel.value = null }
    fun setLabelFilter(id: Long?) { _filterLabel.value = id; _filterCategory.value = null; _filterFolder.value = null }

    fun importDocuments(uris: List<Uri>) {
        viewModelScope.launch {
            _importState.value = ImportState.Processing("0/${uris.size}")
            var successCount = 0
            var duplicateCount = 0
            var errorCount = 0
            
            uris.forEachIndexed { index, uri ->
                _importState.value = ImportState.Processing("${index + 1}/${uris.size}")
                try {
                    val result = importDocumentUseCase(uri)
                    when (result) {
                        -2L -> duplicateCount++
                        -1L -> errorCount++
                        else -> successCount++
                    }
                } catch (e: Exception) {
                    errorCount++
                }
            }
            
            val summary = buildString {
                append("Import complete. ")
                if (successCount > 0) append("$successCount added. ")
                if (duplicateCount > 0) append("$duplicateCount skipped (duplicates). ")
                if (errorCount > 0) append("$errorCount failed.")
            }
            
            _importState.value = if (errorCount > 0 && successCount == 0) {
                ImportState.Error("Failed to import documents")
            } else {
                ImportState.Success(summary.trim())
            }
        }
    }

    fun clearImportSuccess() { _importState.value = ImportState.Idle }
    fun clearImportError() { _importState.value = ImportState.Idle }

    fun deleteDocument(id: Long) {
        viewModelScope.launch {
            repository.deleteDocument(id)
        }
    }
}
