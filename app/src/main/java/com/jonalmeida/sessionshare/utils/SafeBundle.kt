package com.jonalmeida.sessionshare.utils

import android.os.Bundle
import android.os.Parcelable
import com.jonalmeida.sessionshare.ext.Log

/**
 * See SafeIntent for more background: applications can put garbage values into Bundles. This is primarily
 * experienced when there's garbage in the Intent's Bundle. However that Bundle can contain further bundles,
 * and we need to handle those defensively too.
 *
 * See bug 1090385 for more.
 */
class SafeBundle(private val bundle: Bundle) {

    fun getString(name: String): String? {
        return try {
            bundle.getString(name)
        } catch (e: OutOfMemoryError) {
            Log.w("Couldn't get bundle items: OOM. Malformed?")
            null
        } catch (e: RuntimeException) {
            Log.w("Couldn't get bundle items.", e)
            null
        }
    }

    fun <T : Parcelable> getParcelable(name: String): T? {
        return try {
            bundle.getParcelable(name)
        } catch (e: OutOfMemoryError) {
            Log.w("Couldn't get bundle items: OOM. Malformed?")
            null
        } catch (e: RuntimeException) {
            Log.w("Couldn't get bundle items.", e)
            null
        }
    }
}