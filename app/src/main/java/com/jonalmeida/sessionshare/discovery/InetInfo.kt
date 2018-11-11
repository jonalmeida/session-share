package com.jonalmeida.sessionshare.discovery

import java.net.URI

data class InetInfo(val address: String, val port: Int)

fun InetInfo.toUri() : URI {
    return URI("ws://$address:$port")
}