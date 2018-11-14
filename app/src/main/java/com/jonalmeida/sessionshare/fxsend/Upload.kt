package com.jonalmeida.sessionshare.fxsend

import com.jonalmeida.sessionshare.Components

class Upload(private val components: Components) {
    fun upload(
        endpoint: String = SERVICE_UPLOAD,
        encodedData: String,
        encodedMetadata: String,
        keys: SecretKeys
    ) {
        //components.httpClient.newBuilder()
    }

    companion object {
        const val SERVICE_UPLOAD = "https://send.firefox.com/api/upload"
    }
}