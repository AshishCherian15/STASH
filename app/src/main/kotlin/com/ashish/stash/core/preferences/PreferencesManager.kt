package com.ashish.stash.core.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

data class UserData(
    val onboardingCompleted: Boolean,
    val darkTheme: Boolean,
    val dynamicColor: Boolean,
    val preventScreenshots: Boolean,
    val autoLockTimeoutMillis: Long,
    val vaultPin: String?
)

@Singleton
class PreferencesManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val dataStore = context.dataStore

    private object Keys {
        val ONBOARDING_COMPLETED = booleanPreferencesKey("onboarding_completed")
        val DARK_THEME = booleanPreferencesKey("dark_theme")
        val DYNAMIC_COLOR = booleanPreferencesKey("dynamic_color")
        val PREVENT_SCREENSHOTS = booleanPreferencesKey("prevent_screenshots")
        val AUTO_LOCK_TIMEOUT = longPreferencesKey("auto_lock_timeout")
        val VAULT_PIN = stringPreferencesKey("vault_pin")
    }

    val userData: Flow<UserData> = dataStore.data.map { prefs ->
        UserData(
            onboardingCompleted = prefs[Keys.ONBOARDING_COMPLETED] ?: false,
            darkTheme = prefs[Keys.DARK_THEME] ?: false,
            dynamicColor = prefs[Keys.DYNAMIC_COLOR] ?: true,
            preventScreenshots = prefs[Keys.PREVENT_SCREENSHOTS] ?: false,
            autoLockTimeoutMillis = prefs[Keys.AUTO_LOCK_TIMEOUT] ?: 30000L,
            vaultPin = prefs[Keys.VAULT_PIN]
        )
    }

    val onboardingCompleted: Flow<Boolean> = dataStore.data.map { it[Keys.ONBOARDING_COMPLETED] ?: false }

    suspend fun setOnboardingCompleted(completed: Boolean) {
        dataStore.edit { it[Keys.ONBOARDING_COMPLETED] = completed }
    }

    suspend fun setDarkTheme(enabled: Boolean) {
        dataStore.edit { it[Keys.DARK_THEME] = enabled }
    }

    suspend fun setDynamicColor(enabled: Boolean) {
        dataStore.edit { it[Keys.DYNAMIC_COLOR] = enabled }
    }

    suspend fun setPreventScreenshots(enabled: Boolean) {
        dataStore.edit { it[Keys.PREVENT_SCREENSHOTS] = enabled }
    }

    suspend fun setAutoLockTimeoutMillis(timeout: Long) {
        dataStore.edit { it[Keys.AUTO_LOCK_TIMEOUT] = timeout }
    }

    suspend fun setVaultPin(pin: String?) {
        dataStore.edit { prefs ->
            if (pin == null) prefs.remove(Keys.VAULT_PIN)
            else prefs[Keys.VAULT_PIN] = pin
        }
    }
}
