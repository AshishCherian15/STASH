package com.ashish.stash.ui.security

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ashish.stash.core.preferences.PreferencesManager
import com.ashish.stash.core.security.BiometricLockManager
import com.ashish.stash.core.security.SecuritySessionManager
import com.ashish.stash.ui.theme.StashBlue

@Composable
fun LockGate(
    preferencesManager: PreferencesManager,
    biometricLockManager: BiometricLockManager,
    securitySessionManager: SecuritySessionManager,
    content: @Composable () -> Unit
) {
    val isLocked by securitySessionManager.isLocked.collectAsStateWithLifecycle()
    val userData by preferencesManager.userData.collectAsStateWithLifecycle(initialValue = null)
    
    val context = LocalContext.current as FragmentActivity
    
    // Check if security should be applied
    val hasSecurity = userData?.let { it.onboardingCompleted && (it.vaultPin != null) } ?: false

    if (!hasSecurity) {
        SideEffect {
            securitySessionManager.unlock()
        }
        content()
    } else if (!isLocked) {
        content()
    } else {
        if (userData?.vaultPin != null) {
            PinLockScreen(
                onCorrectPin = { securitySessionManager.unlock() },
                savedPin = userData?.vaultPin ?: ""
            )
        } else {
            // Default to immediate unlock if no PIN configured but security triggered
            SideEffect {
                securitySessionManager.unlock()
            }
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = StashBlue)
            }
        }
    }
}
