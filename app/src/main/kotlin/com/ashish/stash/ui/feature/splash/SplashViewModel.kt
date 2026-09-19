package com.ashish.stash.ui.feature.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ashish.stash.core.database.repository.DocumentRepository
import com.ashish.stash.core.preferences.PreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val preferencesManager: PreferencesManager,
    private val repository: DocumentRepository
) : ViewModel() {

    private val _navigationEvent = MutableSharedFlow<SplashNavigation>()
    val navigationEvent: SharedFlow<SplashNavigation> = _navigationEvent.asSharedFlow()

    init {
        checkInitialDestination()
    }

    private fun checkInitialDestination() {
        viewModelScope.launch {
            val userData = preferencesManager.userData.first()
            
            if (!userData.defaultsSeeded) {
                repository.checkAndInjectDefaults()
                preferencesManager.setDefaultsSeeded(true)
            }
            
            // Artificial delay for splash feel - keeping minimal 500ms
            delay(500)
            
            if (userData.onboardingCompleted) {
                _navigationEvent.emit(SplashNavigation.ToHome)
            } else {
                _navigationEvent.emit(SplashNavigation.ToOnboarding)
            }
        }
    }
}

sealed interface SplashNavigation {
    data object ToHome : SplashNavigation
    data object ToOnboarding : SplashNavigation
}
