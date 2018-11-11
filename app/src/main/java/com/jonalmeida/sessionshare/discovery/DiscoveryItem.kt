package com.jonalmeida.sessionshare.discovery

data class DiscoveryItem(val name: String, val uuid: String, val info: InetInfo?) {
    override fun equals(other: Any?): Boolean {
        (other as DiscoveryItem).let {
            return other.uuid == uuid
        }
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }
}