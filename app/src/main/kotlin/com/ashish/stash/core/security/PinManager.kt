package com.ashish.stash.core.security

import android.util.Base64
import java.security.SecureRandom
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PinManager @Inject constructor() {
    private val iterations = 120000
    private val keyLength = 256
    private val saltLength = 16

    data class HashResult(val hash: String, val salt: String)

    fun hashPin(pin: String): HashResult {
        val saltBytes = ByteArray(saltLength)
        SecureRandom().nextBytes(saltBytes)
        val salt = Base64.encodeToString(saltBytes, Base64.NO_WRAP)
        val hash = pbkdf2(pin.toCharArray(), saltBytes)
        return HashResult(hash, salt)
    }

    fun verifyPin(pin: String, storedHash: String, salt: String): Boolean {
        val saltBytes = Base64.decode(salt, Base64.NO_WRAP)
        val hashAttempt = pbkdf2(pin.toCharArray(), saltBytes)
        return constantTimeEquals(storedHash, hashAttempt)
    }

    private fun pbkdf2(pin: CharArray, salt: ByteArray): String {
        val spec = PBEKeySpec(pin, salt, iterations, keyLength)
        val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
        val key = factory.generateSecret(spec).encoded
        return Base64.encodeToString(key, Base64.NO_WRAP)
    }

    private fun constantTimeEquals(a: String, b: String): Boolean {
        if (a.length != b.length) return false
        var result = 0
        for (i in a.indices) {
            result = result or (a[i].code xor b[i].code)
        }
        return result == 0
    }
}
