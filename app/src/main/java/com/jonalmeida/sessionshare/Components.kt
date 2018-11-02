package com.jonalmeida.sessionshare

import android.content.Context
import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo
import android.preference.PreferenceManager
import com.jonalmeida.sessionshare.fxsend.AuthInterceptor
import com.jonalmeida.sessionshare.server.WebSocketServer
import okhttp3.OkHttpClient
import java.net.ServerSocket
import java.util.UUID

open class Components(private val applicationContext: Context) {

    private val serverSocket by lazy {
        ServerSocket(0)
    }

    private val localPort = serverSocket.localPort

    val serverWebSocket by lazy {
        serverSocket.close()
        WebSocketServer(localPort).apply {
            start()
        }
    }

    var nsdServiceName = "Session_" + UUID.randomUUID()

    val nsdServiceInfo by lazy {
        NsdServiceInfo().apply {
            // The name is subject to change based on conflicts
            // with other services advertised on the same network.
            serviceName = nsdServiceName
            serviceType = "_mozShare._tcp"
            port = localPort
            setAttribute("dName", displayName)
        }
    }

    val nsdManager by lazy {
        (applicationContext.getSystemService(Context.NSD_SERVICE) as NsdManager)
    }

    val httpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor())
            .build()
    }

    val displayName by lazy {
        PreferenceManager.getDefaultSharedPreferences(applicationContext).getString("display_name", "Unknown Fenix Device")
    }
}