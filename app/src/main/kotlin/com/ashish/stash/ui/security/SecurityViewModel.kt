package com.ashish.stash.ui.security

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ashish.stash.core.preferences.PreferencesManager
import com.ashish.stash.core.security.PinManager
import com.ashish.stash.core.security.SecuritySessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class PinFlow { UNLOCK, SETUP, CHANGE }

data class SecurityUiState(
    val flow: PinFlow = PinFlow.UNLOCK,
    val enteredPin: String = "",
    val firstPin: String = "", // For setup confirmation
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

    private var failedAttempts = 0

    init {
        viewModelScope.launch {
            preferencesManager.userData.collect { userData ->
                _uiState.update { it.copy(isPinSet = userData.isPinSet) }
            }
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
                PinFlow.CHANGE -> handleSetup() // Simplified
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
            val userData = preferencesManager.userData.first()
            val hash = userData.pinHash
            val salt = userData.pinSalt

            if (hash != null && salt != null) {
                if (pinManager.verifyPin(_uiState.value.enteredPin, hash, salt)) {
                    failedAttempts = 0
                    securitySessionManager.unlock()
                } else {
                    handleFailure()
                }
            } else {
                securitySessionManager.unlock()
            }
        }
    }

    private fun handleFailure() {
        failedAttempts++
        _uiState.update { it.copy(enteredPin = "", isError = true, errorMessage = "Incorrect PIN") }
        
        if (failedAttempts >= 5) {
            startLockout()
        }
    }

    private fun startLockout() {
        viewModelScope.launch {
            var remaining = 30
            while (remaining > 0) {
                _uiState.update { it.copy(lockoutSeconds = remaining) }
                delay(1000)
                remaining--
            }
            _uiState.update { it.copy(lockoutSeconds = 0) }
            failedAttempts = 0
        }
    }
}
