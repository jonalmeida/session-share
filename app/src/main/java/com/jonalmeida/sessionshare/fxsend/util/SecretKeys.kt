package com.jonalmeida.sessionshare.fxsend.util

import java.security.SecureRandom
import java.util.Arrays

data class SecretKeys(val secretKey: ByteArray = getRandomBytes(16),
                      val encryptKey: ByteArray,
                      val randomEncryptIV: ByteArray,
                      val authKey: ByteArray,
                      val metaKey: ByteArray,
                      val metaIV: ByteArray = byteArrayOf(0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0,
                          0x0, 0x0, 0x0, 0x0, 0x0),
                      val password: ByteArray?) {

    init {
    }


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SecretKeys

        if (!Arrays.equals(secretKey, other.secretKey)) return false
        if (!Arrays.equals(encryptKey, other.encryptKey)) return false
        if (!Arrays.equals(randomEncryptIV, other.randomEncryptIV)) return false
        if (!Arrays.equals(authKey, other.authKey)) return false
        if (!Arrays.equals(metaKey, other.metaKey)) return false
        if (!Arrays.equals(metaIV, other.metaIV)) return false
        if (!Arrays.equals(password, other.password)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = Arrays.hashCode(secretKey)
        result = 31 * result + Arrays.hashCode(encryptKey)
        result = 31 * result + Arrays.hashCode(randomEncryptIV)
        result = 31 * result + Arrays.hashCode(authKey)
        result = 31 * result + Arrays.hashCode(metaKey)
        result = 31 * result + Arrays.hashCode(metaIV)
        result = 31 * result + (password?.let { Arrays.hashCode(it) } ?: 0)
        return result
    }
}

fun getRandomBytes(n: Int): ByteArray {
    return ByteArray(n).apply {
        SecureRandom().nextBytes(this)
    }
}

fun SecretKeys.deriveEncryptKey() {
//    HKDF.createFor(3).deriveSecrets()
}

