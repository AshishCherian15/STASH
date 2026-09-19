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
    val isPinSet: Boolean,
    val pinHash: String?,
    val pinSalt: String?,
    val biometricEnabled: Boolean,
    val themeColor: String,
    val fontFamily: String,
    val fontSizeScale: Float,
    val defaultsSeeded: Boolean,
    val vaultRootUri: String?
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
        val PIN_HASH = stringPreferencesKey("pin_hash")
        val PIN_SALT = stringPreferencesKey("pin_salt")
        val BIOMETRIC_ENABLED = booleanPreferencesKey("biometric_enabled")
        val THEME_COLOR = stringPreferencesKey("theme_color")
        val FONT_FAMILY = stringPreferencesKey("font_family")
        val FONT_SIZE_SCALE = floatPreferencesKey("font_size_scale")
        val DEFAULTS_SEEDED = booleanPreferencesKey("defaults_seeded")
        val VAULT_ROOT_URI = stringPreferencesKey("vault_root_uri")
    }

    val userData: Flow<UserData> = dataStore.data.map { prefs ->
        UserData(
            onboardingCompleted = prefs[Keys.ONBOARDING_COMPLETED] ?: false,
            darkTheme = prefs[Keys.DARK_THEME] ?: false,
            dynamicColor = prefs[Keys.DYNAMIC_COLOR] ?: true,
            preventScreenshots = prefs[Keys.PREVENT_SCREENSHOTS] ?: true,
            autoLockTimeoutMillis = prefs[Keys.AUTO_LOCK_TIMEOUT] ?: 30000L,
            isPinSet = prefs[Keys.PIN_HASH] != null,
            pinHash = prefs[Keys.PIN_HASH],
            pinSalt = prefs[Keys.PIN_SALT],
            biometricEnabled = prefs[Keys.BIOMETRIC_ENABLED] ?: false,
            themeColor = prefs[Keys.THEME_COLOR] ?: "BLUE",
            fontFamily = prefs[Keys.FONT_FAMILY] ?: "SANS_SERIF",
            fontSizeScale = prefs[Keys.FONT_SIZE_SCALE] ?: 1.0f,
            defaultsSeeded = prefs[Keys.DEFAULTS_SEEDED] ?: false,
            vaultRootUri = prefs[Keys.VAULT_ROOT_URI]
        )
    }

    val onboardingCompleted: Flow<Boolean> = dataStore.data.map { it[Keys.ONBOARDING_COMPLETED] ?: false }

    suspend fun setOnboardingCompleted(completed: Boolean) {
        dataStore.edit { it[Keys.ONBOARDING_COMPLETED] = completed }
    }

    suspend fun setDefaultsSeeded(seeded: Boolean) {
        dataStore.edit { it[Keys.DEFAULTS_SEEDED] = seeded }
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

    suspend fun setBiometricEnabled(enabled: Boolean) {
        dataStore.edit { it[Keys.BIOMETRIC_ENABLED] = enabled }
    }

    suspend fun setThemeColor(color: String) {
        dataStore.edit { it[Keys.THEME_COLOR] = color }
    }

    suspend fun setFontFamily(font: String) {
        dataStore.edit { it[Keys.FONT_FAMILY] = font }
    }

    suspend fun setFontSizeScale(scale: Float) {
        dataStore.edit { it[Keys.FONT_SIZE_SCALE] = scale }
    }

    suspend fun setVaultRootUri(uri: String?) {
        dataStore.edit { prefs ->
            if (uri == null) prefs.remove(Keys.VAULT_ROOT_URI)
            else prefs[Keys.VAULT_ROOT_URI] = uri
        }
    }

    suspend fun setPin(hash: String?, salt: String?) {
        dataStore.edit { prefs ->
            if (hash == null || salt == null) {
                prefs.remove(Keys.PIN_HASH)
                prefs.remove(Keys.PIN_SALT)
            } else {
                prefs[Keys.PIN_HASH] = hash
                prefs[Keys.PIN_SALT] = salt
            }
        }
    }
}
