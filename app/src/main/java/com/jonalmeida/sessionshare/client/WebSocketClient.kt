package com.jonalmeida.sessionshare.client

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import com.jonalmeida.sessionshare.ext.Log
import mozilla.components.support.base.observer.Observable
import mozilla.components.support.base.observer.ObserverRegistry
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import java.lang.Exception
import java.net.URI

class WebSocketClient(
    url: URI,
    registry: Observable<ClientObserver> = ObserverRegistry()
) : WebSocketClient(url), LifecycleObserver, Observable<ClientObserver> by registry {
    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun shutdown() {
        close(CLIENT_SLEEPING)
    }

    override fun onOpen(handshakedata: ServerHandshake?) {
        notifyObservers {
            val data = onServerConnected()
            handshakedata?.let { handshake ->
                Log.d("Checking handshake status: ${handshake.httpStatus}")
//                if (handshake.httpStatus in 200..299 && bytes != null) {
                    send(data)
//                }
            }.also {
                close(CLIENT_CLOSED_SUCCESS)
            }
        }
    }

    override fun onClose(code: Int, reason: String?, remote: Boolean) {
        notifyObservers {
            close(code)
        }
//        when (code) {
//            WebSocketServer.SERVER_CONTEXT_DESTROYED ->
//                Log.d("Server closed unexpectedly.")
//            WebSocketServer.SERVER_CLOSE_SUCCESS ->
//                Log.d("Server closed connection; data received.")
//        }
        Log.e("Closing server connection for reason (code): $code")
    }

    override fun onMessage(message: String?) {
        //TODO: TBD?
    }

    override fun onError(ex: Exception?) {
        Log.e("We (server) goofed!", ex)
    }

    companion object {
        const val CLIENT_CLOSED_SUCCESS = 0x1 // Data sent successfully
        const val CLIENT_SLEEPING = 0x2
        const val CLIENT_NOTHING_TO_SEND = 0x3
    }
}