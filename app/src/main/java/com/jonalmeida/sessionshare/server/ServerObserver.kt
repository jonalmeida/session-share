package com.jonalmeida.sessionshare.server

interface ServerObserver {
    fun onServerStarted() = Unit

    fun onClientConnected() = Unit

    fun onMessageReceived(message: String) = Unit

    fun onServerStopped() = Unit
}