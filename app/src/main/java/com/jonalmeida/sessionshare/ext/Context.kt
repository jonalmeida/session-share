package com.jonalmeida.sessionshare.ext

import android.content.Context
import com.jonalmeida.sessionshare.Components
import com.jonalmeida.sessionshare.SessionShareApp

/**
 * Get the SampleApplication object from a context.
 */
val Context.application: SessionShareApp
    get() = applicationContext as SessionShareApp

/**
 * Get the components of this application.
 */
val Context.components: Components
    get() = application.components