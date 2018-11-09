package com.jonalmeida.sessionshare.discovery

import com.jonalmeida.sessionshare.ui.DiscoveryListAdapter

interface DiscoveryServiceProvider {
    fun serviceFound(item: DiscoveryListAdapter.DiscoveryItem)
    fun serviceLost(item: DiscoveryListAdapter.DiscoveryItem)
}