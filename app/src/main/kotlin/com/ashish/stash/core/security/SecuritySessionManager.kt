package com.ashish.stash.core.security

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import com.ashish.stash.core.preferences.PreferencesManager
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

sealed interface LockState {
    data object Loading : LockState
    data object Locked : LockState
    data object Unlocked : LockState
}

@Singleton
class SecuritySessionManager @Inject constructor(
    private val preferencesManager: PreferencesManager,
    private val pinManager: PinManager
) : DefaultLifecycleObserver {

    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    
    private val _lockState = MutableStateFlow<LockState>(LockState.Loading)
    val lockState: StateFlow<LockState> = _lockState.asStateFlow()

    private val _itemsUnlocked = MutableStateFlow(false)
    val itemsUnlocked: StateFlow<Boolean> = _itemsUnlocked.asStateFlow()

    private val _failedAttempts = MutableStateFlow(0)
    val failedAttempts: StateFlow<Int> = _failedAttempts.asStateFlow()

    private val _lockoutSeconds = MutableStateFlow(0)
    val lockoutSeconds: StateFlow<Int> = _lockoutSeconds.asStateFlow()

    private var lastStopTimestamp: Long = 0L

    fun startObserving() {
        scope.launch {
            ProcessLifecycleOwner.get().lifecycle.addObserver(this@SecuritySessionManager)
        }
    }

    suspend fun verifyAndUnlock(pin: String): Boolean {
        if (_lockoutSeconds.value > 0) return false
        
        val userData = preferencesManager.userData.first()
        val hash = userData.pinHash ?: return true
        val salt = userData.pinSalt ?: return true

        return if (pinManager.verifyPin(pin, hash, salt)) {
            _failedAttempts.value = 0
            unlock()
            true
        } else {
            handleFailure()
            false
        }
    }

    private fun handleFailure() {
        _failedAttempts.value += 1
        if (_failedAttempts.value >= 5) {
            startLockout()
        }
    }

    private fun startLockout() {
        scope.launch {
            for (i in 30 downTo 0) {
                _lockoutSeconds.value = i
                delay(1000)
            }
            _failedAttempts.value = 0
            _lockoutSeconds.value = 0
        }
    }

    override fun onStart(owner: LifecycleOwner) {
        scope.launch {
            val userData = preferencesManager.userData.first()
            val now = System.currentTimeMillis()
            val timeout = userData.autoLockTimeoutMillis
            
            if (lastStopTimestamp > 0 && now - lastStopTimestamp >= timeout) {
                lock()
                _itemsUnlocked.value = false
            }
        }
    }

    override fun onStop(owner: LifecycleOwner) {
        lastStopTimestamp = System.currentTimeMillis()
    }

    fun markLoadingComplete(isSecurityConfigured: Boolean) {
        if (_lockState.value == LockState.Loading) {
            _lockState.value = if (isSecurityConfigured) LockState.Locked else LockState.Unlocked
        }
    }

    fun unlock() { _lockState.value = LockState.Unlocked }
    fun lock() { _lockState.value = LockState.Locked }
    fun unlockItems() { _itemsUnlocked.value = true }
    fun lockItems() { _itemsUnlocked.value = false }
}
