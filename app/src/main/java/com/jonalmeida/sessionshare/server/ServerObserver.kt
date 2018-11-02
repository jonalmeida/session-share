package com.jonalmeida.sessionshare.server

interface ServerObserver {
    fun onServerStarted() = Unit

//    fun onClientConnected(): ByteArray? = null
    fun onClientConnected() = Unit

    fun onMessageReceived(message: String) = Unit
}