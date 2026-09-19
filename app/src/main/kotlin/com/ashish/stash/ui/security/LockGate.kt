package com.ashish.stash.ui.security

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ashish.stash.core.preferences.PreferencesManager
import com.ashish.stash.core.security.BiometricLockManager
import com.ashish.stash.core.security.LockState
import com.ashish.stash.core.security.SecuritySessionManager
import com.ashish.stash.ui.theme.StashBlue
import kotlinx.coroutines.flow.first

@Composable
fun LockGate(
    preferencesManager: PreferencesManager,
    biometricLockManager: BiometricLockManager,
    securitySessionManager: SecuritySessionManager,
    content: @Composable () -> Unit
) {
    val lockState by securitySessionManager.lockState.collectAsStateWithLifecycle()
    val context = LocalContext.current as FragmentActivity

    LaunchedEffect(Unit) {
        val userData = preferencesManager.userData.first()
        val isConfigured = userData.isPinSet || (userData.biometricEnabled && biometricLockManager.canAuthenticate())
        securitySessionManager.markLoadingComplete(isConfigured)
        
        // Auto-trigger biometric if enabled
        if (userData.biometricEnabled && lockState == LockState.Locked) {
            biometricLockManager.authenticate(
                activity = context,
                onSuccess = { securitySessionManager.unlock() },
                onError = { _, _ -> }
            )
        }
    }

    when (lockState) {
        LockState.Loading -> {
            Box(modifier = Modifier.fillMaxSize().background(Color.White), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = StashBlue)
            }
        }
        LockState.Locked -> {
            PinLockScreen(
                onCorrectPin = { securitySessionManager.unlock() },
                onBiometricRequest = {
                    biometricLockManager.authenticate(
                        activity = context,
                        onSuccess = { securitySessionManager.unlock() },
                        onError = { _, _ -> }
                    )
                }
            )
        }
        LockState.Unlocked -> {
            content()
        }
    }
}
