package com.jonalmeida.sessionshare

import android.content.Context
import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo
import com.jonalmeida.sessionshare.send.AuthInterceptor
import okhttp3.OkHttpClient
import java.net.ServerSocket
import java.util.UUID

open class Components(private val applicationContext: Context) {

    val serverSocket by lazy {
        ServerSocket(0)
    }

    private val localPort = serverSocket.localPort

    var nsdServiceName = "Session_" + UUID.randomUUID()

    val nsdServiceInfo by lazy {
        NsdServiceInfo().apply {
            // The name is subject to change based on conflicts
            // with other services advertised on the same network.
            serviceName = nsdServiceName
            serviceType = "_mozShare._tcp"
            port = localPort
            //TODO: Add a setting UI that let's you enter your display name.
//            setAttribute("dName", "Fenix Device on Nexus 5X")
            setAttribute("dName", "Fenix Device on Pixel 2")
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
}