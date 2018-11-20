package com.jonalmeida.sessionshare.ui

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.jonalmeida.sessionshare.R
import com.jonalmeida.sessionshare.discovery.DiscoveryItem
import com.jonalmeida.sessionshare.discovery.DiscoveryServiceReceiver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

typealias DiscoveryListener = (DiscoveryItem) -> Unit

class DiscoveryListAdapter(
    private val listener: DiscoveryListener
) : RecyclerView.Adapter<DiscoveryListAdapter.ItemViewHolder>(), DiscoveryServiceReceiver {

    private var listStore = mutableListOf<DiscoveryItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.discovery_item, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ItemViewHolder, position: Int) =
        viewHolder.bind(listStore[position], listener)

    override fun getItemCount() = listStore.size

    override fun serviceFound(item: DiscoveryItem) {
        if (!listStore.contains(item)) {
            val newList = listStore.toMutableList()
            newList.add(item)
            updateListStore(newList)
        }
    }

    override fun serviceLost(item: DiscoveryItem) {
        if (listStore.contains(item)) {
            val newList = listStore.toMutableList()
            newList.remove(item)
            updateListStore(newList)
        }
    }

    private fun updateListStore(newList: MutableList<DiscoveryItem>) {
        val diff = DiffUtil.calculateDiff(DiscoveryDiffCallback(listStore, newList))
        CoroutineScope(Dispatchers.Main).launch {
            diff.dispatchUpdatesTo(this@DiscoveryListAdapter)
            listStore = newList
        }
    }

    fun addAll(newList: MutableList<DiscoveryItem>) = updateListStore(newList)

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var name: TextView = itemView.findViewById(R.id.discovery_name)
        private var address: TextView = itemView.findViewById(R.id.discovery_address)

        fun bind(item: DiscoveryItem, listener: DiscoveryListener) {
            address.text = item.uuid
            name.text = item.name
            itemView.setOnClickListener {
                listener(item)
            }
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
