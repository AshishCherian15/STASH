package com.ashish.stash.core.security

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import com.ashish.stash.core.preferences.PreferencesManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

sealed interface LockState {
    data object Loading : LockState
    data object Locked : LockState
    data object Unlocked : LockState
}

@Singleton
class SecuritySessionManager @Inject constructor(
    private val preferencesManager: PreferencesManager
) : DefaultLifecycleObserver {

    private val _lockState = MutableStateFlow<LockState>(LockState.Loading)
    val lockState: StateFlow<LockState> = _lockState.asStateFlow()

    // Second level: Secure Items (Locked Documents/Folders)
    private val _itemsUnlocked = MutableStateFlow(false)
    val itemsUnlocked: StateFlow<Boolean> = _itemsUnlocked.asStateFlow()

    private var lastStopTimestamp: Long = 0L

    init {
        CoroutineScope(Dispatchers.Main).launch {
            ProcessLifecycleOwner.get().lifecycle.addObserver(this@SecuritySessionManager)
        }
    }

    override fun onStart(owner: LifecycleOwner) {
        CoroutineScope(Dispatchers.IO).launch {
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
        CoroutineScope(Dispatchers.IO).launch {
            val userData = preferencesManager.userData.first()
            if (userData.autoLockTimeoutMillis == 0L) {
                lock()
                _itemsUnlocked.value = false
            }
        }
    }

    fun markLoadingComplete(isSecurityConfigured: Boolean) {
        if (_lockState.value == LockState.Loading) {
            _lockState.value = if (isSecurityConfigured) LockState.Locked else LockState.Unlocked
        }
    }

    fun unlock() {
        _lockState.value = LockState.Unlocked
    }

    fun lock() {
        _lockState.value = LockState.Locked
    }

    fun unlockItems() {
        _itemsUnlocked.value = true
    }

    fun lockItems() {
        _itemsUnlocked.value = false
    }
}
