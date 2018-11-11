package com.jonalmeida.sessionshare.server

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import com.jonalmeida.sessionshare.ext.Log
import org.java_websocket.WebSocket.DEFAULT_PORT
import java.net.ServerSocket

class ServerSocketManager : LifecycleObserver, ServerObserver {
    private var serverSocket: ServerSocket
    private var backingSocketServer: WebSocketServer
    var localPort: Int = DEFAULT_PORT

    init {
        Log.d("Starting ServerSocketManager")
        serverSocket = ServerSocket(0).also { socket ->
            localPort = socket.localPort
            socket.close()
        }
        backingSocketServer = WebSocketServer(localPort).apply {
            register(this@ServerSocketManager)
            start()
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun teardown() {
        backingSocketServer.teardown()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun shutdown() {
        backingSocketServer.shutdown()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun getOrCreateServer(): WebSocketServer {
        if (serverSocket.isClosed) {
            Log.d("Server: socket is closed, so returning WebServerSocket")
            return backingSocketServer
        }

        serverSocket.close()

        synchronized(backingSocketServer) {
            Log.d("Server: starting new ServerSocket")
            backingSocketServer = WebSocketServer(localPort).apply {
                register(this@ServerSocketManager)
                start()
            }
        }

        return backingSocketServer
    }

    override fun onServerStopped() {
        Log.d("Server: We got a server stopped notification. Holding on to the socket.")
        // We're broadcasting our server on this port so it's easiest to hold on to the port so we don't lose it and
        // have to make a re-broadcast of the service.
        serverSocket = ServerSocket(localPort)
    }
}