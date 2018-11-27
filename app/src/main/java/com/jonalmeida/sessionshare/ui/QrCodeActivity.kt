package com.jonalmeida.sessionshare.ui

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity;
import com.jonalmeida.sessionshare.R

import kotlinx.android.synthetic.main.activity_qr_code.*

class QrCodeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qr_code)
        setSupportActionBar(toolbar)
    }
}
