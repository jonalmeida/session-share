package com.jonalmeida.sessionshare.ui

import android.content.Context
import android.support.annotation.DrawableRes
import android.support.v7.widget.AppCompatImageView
import android.util.AttributeSet
import android.widget.LinearLayout
import com.jonalmeida.sessionshare.R

class BearerLayout @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    init {
        orientation = LinearLayout.VERTICAL
    }

    fun setBearers(bearers: List<Bearer> ) {
        removeAllViews()
        bearers.forEach { bearer ->
            AppCompatImageView(context).apply {
                setImageResource(bearer.res)
            }.also {
                addView(it)
            }
        }
    }

    enum class Bearer(@DrawableRes val res: Int) {
        WIFI(R.drawable.ic_wifi),
        BLUETOOTH(R.drawable.ic_bluetooth)
    }
}