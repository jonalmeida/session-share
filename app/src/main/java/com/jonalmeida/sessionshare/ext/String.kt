package com.jonalmeida.sessionshare.ext

import android.util.Base64

fun String.unpaddedUrlsafeB64Encoded(): String {
    val input = this + "=".times(4 - length % 4)
    return Base64.encodeToString(input.toByteArray(), Base64.DEFAULT)
}

fun String.times(num: Int): String {
    var equal = this
    for (i in 1..num) {
        equal += "="
    }
    return equal
}
