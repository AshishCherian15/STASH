package com.ashish.stash.ui.security

import android.content.Context
import android.content.ContextWrapper
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
    
    val context = LocalContext.current
    
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
            // Priority 2: Biometric (if PIN is not set but security is active)
            // Implementation note: If PIN is null, we might want to trigger biometric here.
            // But currently hasSecurity requires vaultPin != null.
            SideEffect {
                securitySessionManager.unlock()
            }
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = StashBlue)
            }
        }
    }
}

fun Context.findActivity(): FragmentActivity? {
    var context = this
    while (context is ContextWrapper) {
        if (context is FragmentActivity) return context
        context = context.baseContext
    }
    return null
}
