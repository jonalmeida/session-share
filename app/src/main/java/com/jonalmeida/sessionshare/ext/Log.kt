package com.jonalmeida.sessionshare.ext

import android.util.Log

object Log {
    fun d(s: String, e: Throwable? = null) {
        Log.d(LOGTAG, s, e)
    }

    fun e(s: String, e: Throwable? = null) {
        Log.e(LOGTAG, s, e)
    }

    fun w(s: String, e: Throwable? = null) {
        Log.w(LOGTAG, s, e)
    }

    private const val LOGTAG = "SessionShare"
}