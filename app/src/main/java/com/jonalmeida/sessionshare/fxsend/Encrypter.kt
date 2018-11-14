package com.jonalmeida.sessionshare.fxsend

import javax.crypto.Cipher

class Encrypter(
    val secretKeys: SecretKeys = SecretKeys()
) {
    fun encrypt(data: String): ByteArray {
        Cipher.getInstance("AES/GCM/NoPadding").let { cipher ->
            return cipher.doFinal(data.toByteArray())
        }
    }

    fun encryptMetaData(
        keys: SecretKeys = secretKeys,
        fileName: String,
        fileType: String = "application/octet-stream"
    ) {
        // TODO: Use a json parser to fix this
    }
}