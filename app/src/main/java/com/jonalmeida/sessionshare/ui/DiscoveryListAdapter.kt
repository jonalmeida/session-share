package com.jonalmeida.sessionshare.ui

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.jonalmeida.sessionshare.R
import com.jonalmeida.sessionshare.nsd.ServiceCallback
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import java.net.InetAddress

class DiscoveryListAdapter : RecyclerView.Adapter<DiscoveryListAdapter.ItemViewHolder>(), ServiceCallback {
    private var listStore = mutableListOf<DiscoveryItem>()

    private fun add(item: DiscoveryItem) {
        if (!listStore.contains(item)) {
            val newList = listStore.toMutableList()
            newList.add(item)
            updateListStore(newList)
        }
    }

    private fun remove(item: DiscoveryItem) {
        if (listStore.contains(item)) {
            val newList = listStore.toMutableList()
            newList.remove(item)
            updateListStore(newList)
        }
    }

    private fun updateListStore(newList: MutableList<DiscoveryItem>) {
        val diff = DiffUtil.calculateDiff(DiscoveryDiffCallback(listStore, newList))
        launch(UI) {
            diff.dispatchUpdatesTo(this@DiscoveryListAdapter)
            listStore = newList
        }
    }

    fun addAll(newList: MutableList<DiscoveryItem>) {
        val diff = DiffUtil.calculateDiff(DiscoveryDiffCallback(listStore, newList))
        launch(UI) {
            diff.dispatchUpdatesTo(this@DiscoveryListAdapter)
            listStore = newList
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.discovery_item, parent,
            false)
        return ItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listStore.size
    }

    override fun onBindViewHolder(viewHolder: ItemViewHolder, position: Int) {
        viewHolder.address.text = listStore[position].uuid
        viewHolder.name.text = listStore[position].name
    }

    override fun serviceFound(item: DiscoveryItem) {
        add(item)
    }

    override fun serviceLost(item: DiscoveryItem) {
        remove(item)
    }

    data class InetInfo(val address: InetAddress?, val port: Int)
    data class DiscoveryItem(val name: String, val uuid: String, val info: InetInfo?) {
        override fun equals(other: Any?): Boolean {
            (other as DiscoveryItem).let {
                return other.uuid == uuid
            }
        }
    }

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name: TextView = itemView.findViewById(R.id.discovery_name)
        var address: TextView = itemView.findViewById(R.id.discovery_address)
    }

    class DiscoveryDiffCallback(private val oldList: List<DiscoveryItem>,
                                private val newList: List<DiscoveryItem>) : DiffUtil.Callback() {
        override fun getOldListSize(): Int = oldList.size
        override fun getNewListSize(): Int = newList.size
        override fun areItemsTheSame(oldPos: Int, newPos: Int) = oldList[oldPos].uuid == newList[newPos].uuid
        override fun areContentsTheSame(oldPos: Int, newPos: Int) = oldList[oldPos] == newList[newPos]
    }
}