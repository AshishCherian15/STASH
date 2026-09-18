package com.ashish.stash.ui.feature.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ashish.stash.core.database.repository.DocumentRepository
import com.ashish.stash.core.security.SecuritySessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val documentRepository: DocumentRepository,
    private val securitySessionManager: SecuritySessionManager
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    val uiState: StateFlow<SearchUiState> = combine(
        _searchQuery.debounce(300L),
        securitySessionManager.isLocked
    ) { query, isLocked ->
        query to !isLocked
    }.flatMapLatest { (query, showLocked) ->
        if (query.isBlank()) {
            flowOf(SearchUiState())
        } else {
            documentRepository.searchDocuments(query, showLocked)
                .map { results ->
                    SearchUiState(
                        searchQuery = query,
                        searchResults = results,
                        isLoading = false
                    )
                }
                .onStart { emit(SearchUiState(searchQuery = query, isLoading = true)) }
        }
    }
    .stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SearchUiState()
    )

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun clearSearch() {
        _searchQuery.value = ""
    }
}
