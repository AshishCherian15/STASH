package com.ashish.stash.core.security

import com.ashish.stash.core.preferences.PreferencesManager
import com.ashish.stash.core.preferences.UserData
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SecuritySessionManagerTest {
    private lateinit var sessionManager: SecuritySessionManager
    private val preferencesManager = mockk<PreferencesManager>(relaxed = true)
    private val pinManager = mockk<PinManager>()
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        sessionManager = SecuritySessionManager(preferencesManager, pinManager)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `verifyAndUnlock returns true for correct PIN and resets failed attempts`() = runTest {
        val pin = "1234"
        val hash = "hashed"
        val salt = "salt"
        val userData = createTestUserData(pinHash = hash, pinSalt = salt)
        
        every { preferencesManager.userData } returns flowOf(userData)
        every { pinManager.verifyPin(pin, hash, salt) } returns true

        val result = sessionManager.verifyAndUnlock(pin)

        assertTrue(result)
        assertEquals(LockState.Unlocked, sessionManager.lockState.value)
        assertEquals(0, sessionManager.failedAttempts.value)
    }

    @Test
    fun `verifyAndUnlock returns false for incorrect PIN and increments failed attempts`() = runTest {
        val pin = "wrong"
        val hash = "hashed"
        val salt = "salt"
        val userData = createTestUserData(pinHash = hash, pinSalt = salt)
        
        every { preferencesManager.userData } returns flowOf(userData)
        every { pinManager.verifyPin(pin, hash, salt) } returns false

        val result = sessionManager.verifyAndUnlock(pin)

        assertFalse(result)
        assertEquals(1, sessionManager.failedAttempts.value)
    }

    @Test
    fun `lockout triggered after 5 failed attempts`() = runTest {
        val pin = "wrong"
        val userData = createTestUserData(pinHash = "h", pinSalt = "s")
        
        every { preferencesManager.userData } returns flowOf(userData)
        every { pinManager.verifyPin(any(), any(), any()) } returns false

        repeat(5) { sessionManager.verifyAndUnlock(pin) }

        assertTrue(sessionManager.lockoutSeconds.value > 0)
    }

    private fun createTestUserData(pinHash: String? = null, pinSalt: String? = null) = UserData(
        onboardingCompleted = true,
        darkTheme = false,
        dynamicColor = true,
        preventScreenshots = true,
        autoLockTimeoutMillis = 30000L,
        isPinSet = pinHash != null,
        pinHash = pinHash,
        pinSalt = pinSalt,
        biometricEnabled = false,
        themeColor = "BLUE",
        fontFamily = "SANS_SERIF",
        fontSizeScale = 1.0f,
        defaultsSeeded = true,
        vaultRootUri = null
    )
}
