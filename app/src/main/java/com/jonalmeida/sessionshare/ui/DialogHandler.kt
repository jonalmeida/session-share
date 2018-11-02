package com.jonalmeida.sessionshare.ui

import android.content.Context
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import com.jonalmeida.sessionshare.R

class DialogHandler {
    fun new(context: Context) {
        AlertDialog.Builder(context)
            .setView(LayoutInflater.from(context).inflate(R.layout.dialog_device_name, null))
            .setPositiveButton(R.string.enter) { _, _ ->
                //TODO: save to prefs
            }.setNegativeButton(android.R.string.cancel) { dialog, _ ->
                dialog.cancel()
            }
    }
}

