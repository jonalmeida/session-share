package com.jonalmeida.sessionshare.ui

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.View

/**
 * Simple RecyclerView subclass that supports providing an empty view (which
 * is displayed when the adapter has no data and hidden otherwise).
 */
class DiscoveryRecyclerView : RecyclerView {
    private var emptyView: View? = null

    private val dataObserver = object : RecyclerView.AdapterDataObserver() {
        override fun onChanged() {
            updateEmptyView()
        }

        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            updateEmptyView()
        }

        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
            updateEmptyView()
        }
    }

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)

    override fun setAdapter(newAdapter: RecyclerView.Adapter<*>?) {
        adapter?.unregisterAdapterDataObserver(dataObserver)
        super.setAdapter(newAdapter)
        newAdapter?.registerAdapterDataObserver(dataObserver)
        updateEmptyView()
    }

    fun setEmptyView(view: View) {
        emptyView = view
        updateEmptyView()
    }

    private fun updateEmptyView() {
        if (emptyView != null && adapter != null) {
            val emptyViewVisible = adapter!!.itemCount == 0
            emptyView!!.visibility = if (emptyViewVisible) View.VISIBLE else View.GONE
            visibility = if (emptyViewVisible) View.GONE else View.VISIBLE
        }
    }
}
