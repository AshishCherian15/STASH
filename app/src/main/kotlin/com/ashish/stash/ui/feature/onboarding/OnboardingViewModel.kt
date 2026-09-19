package com.ashish.stash.ui.feature.onboarding

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ashish.stash.core.preferences.PreferencesManager
import com.ashish.stash.domain.usecase.UpdateVaultFolderUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val preferencesManager: PreferencesManager,
    private val updateVaultFolderUseCase: UpdateVaultFolderUseCase
) : ViewModel() {

    private val _navigationEvent = MutableSharedFlow<Unit>()
    val navigationEvent: SharedFlow<Unit> = _navigationEvent.asSharedFlow()

    fun completeOnboarding(vaultUri: Uri?) {
        viewModelScope.launch {
            vaultUri?.let { updateVaultFolderUseCase(it) }
            preferencesManager.setOnboardingCompleted(true)
            _navigationEvent.emit(Unit)
        }
    }
}
