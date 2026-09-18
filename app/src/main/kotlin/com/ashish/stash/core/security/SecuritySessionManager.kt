package com.ashish.stash.core.security

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SecuritySessionManager @Inject constructor() {

    private val _isLocked = MutableStateFlow(true)
    val isLocked: StateFlow<Boolean> = _isLocked.asStateFlow()

    private var lastInteractionTime: Long = System.currentTimeMillis()
    private var lockTimeout: Long = 30000 // Default 30 seconds

    fun onUserInteraction() {
        lastInteractionTime = System.currentTimeMillis()
    }

    fun unlock() {
        _isLocked.value = false
        onUserInteraction()
    }

    fun lock() {
        _isLocked.value = true
    }

    fun checkAutoLock() {
        if (!_isLocked.value && System.currentTimeMillis() - lastInteractionTime > lockTimeout) {
            lock()
        }
    }

    fun setLockTimeout(timeoutMillis: Long) {
        lockTimeout = timeoutMillis
    }
}
