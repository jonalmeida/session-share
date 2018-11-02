package com.jonalmeida.sessionshare.ext

import android.net.nsd.NsdServiceInfo
import com.jonalmeida.sessionshare.ui.DiscoveryListAdapter

fun NsdServiceInfo.toDiscoveryItem(): DiscoveryListAdapter.DiscoveryItem {
    val displayName = attributes["dName"]?.let { bytes ->
        String(bytes)
    }
    return DiscoveryListAdapter.DiscoveryItem(
        displayName ?: serviceName.split("_").first(),
        serviceName.split("_").last(),
        info = DiscoveryListAdapter.InetInfo(
            host?.hostAddress ?: "",
            port
        )
    )
}
