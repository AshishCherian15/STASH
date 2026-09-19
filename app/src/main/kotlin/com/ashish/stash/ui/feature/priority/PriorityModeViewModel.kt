package com.ashish.stash.ui.feature.priority

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ashish.stash.core.database.repository.DocumentRepository
import com.ashish.stash.core.security.LockState
import com.ashish.stash.core.security.SecuritySessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class PriorityModeViewModel @Inject constructor(
    private val documentRepository: DocumentRepository,
    private val securitySessionManager: SecuritySessionManager
) : ViewModel() {

    val uiState: StateFlow<PriorityUiState> = securitySessionManager.lockState
        .flatMapLatest { lockState ->
            val showLocked = lockState == LockState.Unlocked
            documentRepository.observePriorityDocumentsWithMetadata(showLocked)
                .map { docs -> PriorityUiState(documents = docs, isLoading = false) }
        }
        .onStart { emit(PriorityUiState(isLoading = true)) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = PriorityUiState()
        )
}
