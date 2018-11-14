package com.jonalmeida.sessionshare.fxsend

import org.whispersystems.libsignal.kdf.HKDFv2
import java.security.SecureRandom

class SecretKeys(
    val secretKey: ByteArray = randomSecretKey(),
    val encryptKey: ByteArray = deriveEncryptKey(secretKey),
    val encryptIV: ByteArray = randomEncryptIV(),
    val authKey: ByteArray = deriveAuthKey(secretKey),
    val metaKey: ByteArray = deriveMetaKey(secretKey),
    val metaIV: ByteArray = byteArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
) {

    companion object {
        fun randomSecretKey() = getRandomBytes(16)

        fun randomEncryptIV() = getRandomBytes(12)

        fun deriveEncryptKey(secretKey: ByteArray): ByteArray =
            HKDFv2().deriveSecrets(secretKey, byteArrayOf(), 16)

        fun deriveAuthKey(secretKey: ByteArray): ByteArray =
            HKDFv2().deriveSecrets(secretKey, byteArrayOf(), 64)

        fun deriveMetaKey(secretKey: ByteArray): ByteArray =
            HKDFv2().deriveSecrets(secretKey, byteArrayOf(), 16)

        @JvmStatic
        private fun getRandomBytes(n: Int): ByteArray {
            return ByteArray(n).apply {
                SecureRandom().nextBytes(this)
            }
        }
    }
}