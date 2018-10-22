package com.jonalmeida.sessionshare.ext

import android.util.Log
import java.lang.Exception

object Log {
    fun d(s: String, e: Exception? = null) {
        Log.d(LOGTAG, s, e)
    }

    fun e(s: String, e: Exception? = null) {
        Log.e(LOGTAG, s, e)
    }

    fun w(s: String, e: Exception? = null) {
        Log.w(LOGTAG, s, e)
    }

    private const val LOGTAG = "SessionShare"
}