package com.jonalmeida.sessionshare.server

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import org.java_websocket.WebSocket.DEFAULT_PORT
import java.net.ServerSocket

class ServerSocketManager : LifecycleObserver, ServerObserver {
    private var serverSocket: ServerSocket
    private var backingSocketServer: WebSocketServer
    var localPort: Int = DEFAULT_PORT

    init {
        serverSocket = ServerSocket(0).also { socket ->
            localPort = socket.localPort
            socket.close()
        }
        backingSocketServer = WebSocketServer(localPort).apply {
            register(this@ServerSocketManager)
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun shutdown() {
        backingSocketServer.shutdown()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun teardown() {
        backingSocketServer.teardown()
    }

    fun getOrCreateServer(): WebSocketServer {
        if (serverSocket.isClosed) {
            return backingSocketServer
        }

        serverSocket.close()

        synchronized(backingSocketServer) {
            backingSocketServer = WebSocketServer(localPort).apply {
                register(this@ServerSocketManager)
            }
        }

        return backingSocketServer
    }

    override fun onServerStopped() {
        // Hold on to the port
        serverSocket = ServerSocket(localPort)
    }
}