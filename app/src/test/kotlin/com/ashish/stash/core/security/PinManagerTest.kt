package com.ashish.stash.core.security

import android.util.Base64
import io.mockk.every
import io.mockk.mockkStatic
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class PinManagerTest {
    private lateinit var pinManager: PinManager

    @Before
    fun setup() {
        mockkStatic(Base64::class)
        // Simple mock for Base64 to avoid "not mocked" errors
        every { Base64.encodeToString(any(), any()) } answers { 
            java.util.Base64.getEncoder().encodeToString(it.invocation.args[0] as ByteArray)
        }
        every { Base64.decode(any<String>(), any()) } answers {
            java.util.Base64.getDecoder().decode(it.invocation.args[0] as String)
        }
        
        pinManager = PinManager()
    }

    @Test
    fun `hashPin generates different hashes and salts for same PIN`() {
        val pin = "1234"
        val result1 = pinManager.hashPin(pin)
        val result2 = pinManager.hashPin(pin)
        
        assertNotEquals(result1.salt, result2.salt)
        assertNotEquals(result1.hash, result2.hash)
    }

    @Test
    fun `verifyPin returns true for correct PIN`() {
        val pin = "1234"
        val hashResult = pinManager.hashPin(pin)
        
        assertTrue(pinManager.verifyPin(pin, hashResult.hash, hashResult.salt))
    }

    @Test
    fun `verifyPin returns false for incorrect PIN`() {
        val pin = "1234"
        val hashResult = pinManager.hashPin(pin)
        
        assertFalse(pinManager.verifyPin("5678", hashResult.hash, hashResult.salt))
    }
}
