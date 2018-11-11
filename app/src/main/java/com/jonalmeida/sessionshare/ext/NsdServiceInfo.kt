package com.jonalmeida.sessionshare.ext

import android.net.nsd.NsdServiceInfo
import com.jonalmeida.sessionshare.discovery.DiscoveryItem
import com.jonalmeida.sessionshare.discovery.InetInfo

fun NsdServiceInfo.toDiscoveryItem(): DiscoveryItem {
    val displayName = attributes["dName"]?.let { bytes ->
        String(bytes)
    }
    return DiscoveryItem(
        displayName ?: serviceName.split("_").first(),
        serviceName.split("_").last(),
        info = InetInfo(
            host?.hostAddress ?: "",
            port
        )
    )
}
