package com.jonalmeida.sessionshare.ui

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.jonalmeida.sessionshare.R
import com.jonalmeida.sessionshare.discovery.DiscoveryServiceProvider
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import java.net.URI

class DiscoveryListAdapter(
    private val listener: DiscoveryListener
) : RecyclerView.Adapter<DiscoveryListAdapter.ItemViewHolder>(),
    DiscoveryServiceProvider {
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

    fun addAll(newList: MutableList<DiscoveryItem>) = updateListStore(newList)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.discovery_item, parent, false)
        return ItemViewHolder(view)
    }

    override fun getItemCount() = listStore.size

    override fun onBindViewHolder(viewHolder: ItemViewHolder, position: Int) =
        viewHolder.bind(listStore[position], listener)

    override fun serviceFound(item: DiscoveryItem) = add(item)

    override fun serviceLost(item: DiscoveryItem) = remove(item)

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var name: TextView = itemView.findViewById(R.id.discovery_name)
        private var address: TextView = itemView.findViewById(R.id.discovery_address)

        fun bind(discoveryItem: DiscoveryItem, listener: DiscoveryListener) {
            address.text = discoveryItem.uuid
            name.text = discoveryItem.name
            itemView.setOnClickListener {
                listener(discoveryItem)
            }
        }
    }

    data class InetInfo(val address: String, val port: Int)

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

    class DiscoveryDiffCallback(
        private val oldList: List<DiscoveryItem>,
        private val newList: List<DiscoveryItem>
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int = oldList.size
        override fun getNewListSize(): Int = newList.size
        override fun areItemsTheSame(oldPos: Int, newPos: Int) = true
        override fun areContentsTheSame(oldPos: Int, newPos: Int) = oldList[oldPos] == newList[newPos]
    }
}

typealias DiscoveryListener = (DiscoveryListAdapter.DiscoveryItem) -> Unit

fun DiscoveryListAdapter.InetInfo.toUri() : URI {
    return URI("ws://$address:$port")
}
