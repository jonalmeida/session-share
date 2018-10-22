package com.jonalmeida.sessionshare

import android.app.Application

class SessionShareApp : Application() {
    val components by lazy { Components(this) }
}