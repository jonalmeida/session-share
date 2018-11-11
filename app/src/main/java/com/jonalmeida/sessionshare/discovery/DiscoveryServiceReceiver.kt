package com.jonalmeida.sessionshare.discovery

interface DiscoveryServiceReceiver {
    fun serviceFound(item: DiscoveryItem)
    fun serviceLost(item: DiscoveryItem)
}