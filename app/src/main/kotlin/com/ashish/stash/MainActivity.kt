package com.ashish.stash

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.fragment.app.FragmentActivity
import com.ashish.stash.core.preferences.PreferencesManager
import com.ashish.stash.core.security.BiometricLockManager
import com.ashish.stash.core.security.SecuritySessionManager
import com.ashish.stash.ui.StashApp
import com.ashish.stash.ui.theme.StashTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : FragmentActivity() {

    @Inject lateinit var preferencesManager: PreferencesManager
    @Inject lateinit var biometricLockManager: BiometricLockManager
    @Inject lateinit var securitySessionManager: SecuritySessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val userData by preferencesManager.userData.collectAsState(null)
            
            StashTheme(userData = userData) {
                StashApp(
                    preferencesManager = preferencesManager,
                    biometricLockManager = biometricLockManager,
                    securitySessionManager = securitySessionManager
                )
            }
        }
    }
}
