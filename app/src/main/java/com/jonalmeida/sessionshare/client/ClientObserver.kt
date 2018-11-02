package com.jonalmeida.sessionshare.client

interface ClientObserver {
    fun onServerConnected(): String? = null

    fun close(code: Int) = Unit
}