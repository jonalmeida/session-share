package com.jonalmeida.sessionshare.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity;
import com.jonalmeida.sessionshare.R

import kotlinx.android.synthetic.main.activity_qr_code.*
import kotlinx.android.synthetic.main.content_qr_code.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class QrCodeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qr_code)
        setSupportActionBar(toolbar)
    }

    override fun onStart() {
        simulateLoading()
        super.onStart()
    }

    private var job: Job? = null
    private var loading: Boolean = false

    private fun simulateLoading() {
        job?.cancel()

        loading = true

        job = CoroutineScope(Dispatchers.Main).launch {
            try {
                loop@ for (progress in 0..100 step 20) {
                    if (!isActive) {
                        break@loop
                    }

                    viewFlipper.displayedChild = VIEW_TYPE_LOADING

                    delay(progress * 5L)
                }
            } catch (t: Throwable) {
                viewFlipper.displayedChild = VIEW_TYPE_SHOW_CODE

                throw t
            } finally {
                loading = false

                viewFlipper.displayedChild = VIEW_TYPE_SHOW_CODE
            }
        }

        viewFlipper.displayedChild = VIEW_TYPE_SHOW_CODE
    }

    companion object {
        private const val VIEW_TYPE_LOADING = 0
        private const val VIEW_TYPE_SHOW_CODE = 1
    }
}
