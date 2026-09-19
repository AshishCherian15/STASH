package com.ashish.stash.ui.security

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ashish.stash.core.preferences.PreferencesManager
import com.ashish.stash.core.security.PinFlow
import com.ashish.stash.core.security.PinManager
import com.ashish.stash.core.security.SecuritySessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SecurityUiState(
    val flow: PinFlow = PinFlow.UNLOCK,
    val enteredPin: String = "",
    val firstPin: String = "",
    val isConfirming: Boolean = false,
    val isError: Boolean = false,
    val errorMessage: String? = null,
    val lockoutSeconds: Int = 0,
    val isPinSet: Boolean = false
)

@HiltViewModel
class SecurityViewModel @Inject constructor(
    private val preferencesManager: PreferencesManager,
    private val securitySessionManager: SecuritySessionManager,
    private val pinManager: PinManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(SecurityUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                preferencesManager.userData,
                securitySessionManager.lockoutSeconds
            ) { userData, lockout ->
                _uiState.update { it.copy(
                    isPinSet = userData.isPinSet,
                    lockoutSeconds = lockout
                ) }
            }.collect()
        }
    }

    fun startFlow(flow: PinFlow) {
        _uiState.update { it.copy(flow = flow, enteredPin = "", firstPin = "", isConfirming = false, isError = false) }
    }

    fun onDigit(digit: Int) {
        if (_uiState.value.lockoutSeconds > 0) return
        if (_uiState.value.enteredPin.length >= 4) return

        _uiState.update { it.copy(enteredPin = it.enteredPin + digit, isError = false, errorMessage = null) }
        
        if (_uiState.value.enteredPin.length == 4) {
            when (_uiState.value.flow) {
                PinFlow.UNLOCK -> verifyPin()
                PinFlow.SETUP -> handleSetup()
                PinFlow.CHANGE -> handleSetup()
            }
        }
    }

    fun onDelete() {
        if (_uiState.value.enteredPin.isNotEmpty()) {
            _uiState.update { it.copy(enteredPin = it.enteredPin.dropLast(1), isError = false) }
        }
    }

    private fun handleSetup() {
        val state = _uiState.value
        if (!state.isConfirming) {
            _uiState.update { it.copy(firstPin = it.enteredPin, enteredPin = "", isConfirming = true) }
        } else {
            if (state.enteredPin == state.firstPin) {
                savePin(state.enteredPin)
            } else {
                _uiState.update { it.copy(enteredPin = "", isError = true, errorMessage = "PINs do not match") }
            }
        }
    }

    private fun savePin(pin: String) {
        viewModelScope.launch {
            val result = pinManager.hashPin(pin)
            preferencesManager.setPin(result.hash, result.salt)
            securitySessionManager.unlock()
        }
    }

    private fun verifyPin() {
        viewModelScope.launch {
            val success = securitySessionManager.verifyAndUnlock(_uiState.value.enteredPin)
            if (!success) {
                _uiState.update { it.copy(enteredPin = "", isError = true, errorMessage = "Incorrect PIN") }
            }
        }
    }
}
