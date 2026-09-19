package com.ashish.stash.ui.feature.home

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ashish.stash.core.database.entity.DocumentWithMetadata
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

enum class SortOption(val label: String) {
    DATE_DESC("Newest First"),
    DATE_ASC("Oldest First"),
    NAME_ASC("A to Z"),
    NAME_DESC("Z to A"),
    SIZE_DESC("Largest First")
}

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
    private val _sortOption = MutableStateFlow(SortOption.DATE_DESC)

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
        securitySessionManager.itemsUnlocked,
        _searchQuery,
        _viewMode,
        _sortOption,
        _filterCategory,
        _filterFolder,
        _filterLabel
    ) { args ->
        FilterState(
            lockState = args[0] as LockState,
            itemsUnlocked = args[1] as Boolean,
            query = args[2] as String,
            mode = args[3] as ViewMode,
            sortOption = args[4] as SortOption,
            categoryId = args[5] as Long?,
            folderId = args[6] as Long?,
            labelId = args[7] as Long?
        )
    }.flatMapLatest { state ->
        val showLocked = state.itemsUnlocked
        
        val documentsFlow = if (state.query.isNotEmpty()) {
            repository.searchDocuments(state.query, showLocked)
        } else {
            repository.observeAllDocuments(showLocked)
        }
        
        combine(
            documentsFlow,
            repository.observeUnlockedDocumentsCount(),
            repository.observeLockedDocumentsCount(),
            repository.observeTotalSizeBytes(showLocked),
            _importState
        ) { documents, unlocked, locked, totalSize, impState ->
            val sortedDocs = sortDocuments(documents, state.sortOption)
            val filteredDocs = sortedDocs.filter { doc ->
                val categoryMatch = state.categoryId == null || doc.category?.categoryId == state.categoryId
                val folderMatch = state.folderId == null || doc.folder?.folderId == state.folderId
                val labelMatch = state.labelId == null || doc.labels.any { it.labelId == state.labelId }
                categoryMatch && folderMatch && labelMatch
            }.map { metadata ->
                DocumentUiModel(
                    data = metadata,
                    isAccessible = safUriManager.isUriAccessible(Uri.parse(metadata.document.uri))
                )
            }
            
            HomeUiState(
                isLoading = state.lockState == LockState.Loading,
                documents = filteredDocs,
                searchQuery = state.query,
                viewMode = state.mode,
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

    private data class FilterState(
        val lockState: LockState,
        val itemsUnlocked: Boolean,
        val query: String,
        val mode: ViewMode,
        val sortOption: SortOption,
        val categoryId: Long?,
        val folderId: Long?,
        val labelId: Long?
    )

    private fun sortDocuments(docs: List<DocumentWithMetadata>, option: SortOption): List<DocumentWithMetadata> {
        return when (option) {
            SortOption.DATE_DESC -> docs.sortedByDescending { it.document.createdAt }
            SortOption.DATE_ASC -> docs.sortedBy { it.document.createdAt }
            SortOption.NAME_ASC -> docs.sortedBy { it.document.displayTitle }
            SortOption.NAME_DESC -> docs.sortedByDescending { it.document.displayTitle }
            SortOption.SIZE_DESC -> docs.sortedByDescending { it.document.fileSize }
        }
    }

    fun onSearchQueryChanged(query: String) { _searchQuery.value = query }
    fun setViewMode(mode: ViewMode) { _viewMode.value = mode }
    fun setSortOption(option: SortOption) { _sortOption.value = option }
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
