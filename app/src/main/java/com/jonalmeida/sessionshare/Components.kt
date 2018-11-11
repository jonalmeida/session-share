package com.jonalmeida.sessionshare

import android.content.Context
import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo
import android.preference.PreferenceManager
import com.jonalmeida.sessionshare.fxsend.AuthInterceptor
import com.jonalmeida.sessionshare.server.ServerSocketManager
import okhttp3.OkHttpClient
import java.util.UUID

open class Components(private val applicationContext: Context) {

    val serviceUuid = UUID.randomUUID()!!

    var nsdServiceName = "Session_$serviceUuid"

    private val displayName by lazy {
        PreferenceManager.getDefaultSharedPreferences(applicationContext)
            .getString("display_name", "Unknown Fenix Device")
    }

    val serverSocketManager by lazy {
        ServerSocketManager()
    }

    val nsdServiceInfo by lazy {
        NsdServiceInfo().apply {
            // The name is subject to change based on conflicts
            // with other services advertised on the same network.
            serviceName = nsdServiceName
            serviceType = "_mozShare._tcp"
            port = serverSocketManager.localPort
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
}
