package com.jonalmeida.sessionshare.server

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import com.jonalmeida.sessionshare.ext.Log
import mozilla.components.support.base.observer.Observable
import mozilla.components.support.base.observer.ObserverRegistry
import org.java_websocket.WebSocket
import org.java_websocket.handshake.ClientHandshake
import org.java_websocket.server.WebSocketServer
import java.lang.Exception
import java.net.InetSocketAddress

class WebSocketServer(
    port: Int = WebSocket.DEFAULT_PORT,
    delegate: Observable<ServerObserver> = ObserverRegistry()
) : WebSocketServer(InetSocketAddress(port)), LifecycleObserver, Observable<ServerObserver> by delegate {

    private var connection: WebSocket? = null

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun shutdown() {
        Log.d("stopping WebSocketServer")
        connection?.let {
            if (!it.isClosed) {
                it.close(SERVER_CLOSE_SUCCESS)
            }
        }
        stop()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun teardown() {
        connection?.let {
            if (!it.isClosed) {
                it.closeConnection(SERVER_CONTEXT_DESTROYED, "Sugar, we're going down")
            }
        }
    }

    override fun onOpen(conn: WebSocket?, handshake: ClientHandshake?) {
        notifyObservers {
            onClientConnected()
//                ?.let { data ->
//                conn?.send(data)
//            }
        }
    }

    override fun onClose(conn: WebSocket?, code: Int, reason: String?, remote: Boolean) {
        Log.e("Closing server connection for reason (code): $code")
    }

    override fun onMessage(conn: WebSocket?, message: String?) {
        message?.let {
            notifyObservers {
                onMessageReceived(it)
            }
        }
    }

    override fun onStart() {
        // TODO: figure out what to do when we open, if anything?
        notifyObservers {
            onServerStarted()
        }
        Log.d("WebSocketServer started on port $port..")
    }

    override fun onError(conn: WebSocket?, ex: Exception?) {
        Log.e("We (server) goofed!", ex)
    }

    companion object {
        const val SERVER_CLOSE_SUCCESS = 0x1
        const val SERVER_CONTEXT_DESTROYED = 0x2
    }
}