package com.jonalmeida.sessionshare.ui

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.LinearLayoutManager.VERTICAL
import android.view.Menu
import android.view.MenuItem
import com.jonalmeida.sessionshare.nsd.ShareNsdManager
import com.jonalmeida.sessionshare.R
import com.jonalmeida.sessionshare.client.ClientObserver
import com.jonalmeida.sessionshare.client.WebSocketClient
import com.jonalmeida.sessionshare.ext.Log
import com.jonalmeida.sessionshare.ext.components
import com.jonalmeida.sessionshare.server.ServerObserver

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import android.net.Uri
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentIntegrator.QR_CODE_TYPES
import kotlinx.android.synthetic.main.upload_item.*

class MainActivity : AppCompatActivity(), ServerObserver, ClientObserver {

    // For demo-ing
    private var urlToSend: String = "https://www.youtube.com/watch?v=i8ju_10NkGY"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        handleIntent(intent)
        val listAdapter = DiscoveryListAdapter { selectedItem ->
            Log.d("selectedItem: ${selectedItem.name}")
            selectedItem.info?.let {
                WebSocketClient(it.toUri()).apply {
                    connect()
                }.also { client -> client.register(this@MainActivity, this@MainActivity) }
            }

        }

        // Register WebSocket server observer
        components.serverSocketManager.getOrCreateServer().register(this)

        // Register lifecycle observers
        lifecycle.apply {
            addObserver(components.serverSocketManager)
            addObserver(ShareNsdManager(components, listAdapter))
        }

        recyclerview.apply {
            setEmptyView(uploadItemLayout)
            adapter = listAdapter
            layoutManager = LinearLayoutManager(this@MainActivity, VERTICAL, false)
            setHasFixedSize(true)
        }

        uploadButton.setOnClickListener {
            IntentIntegrator(this).let { intent ->
                Log.d("zxing click")
                intent.initiateScan(QR_CODE_TYPES)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNewIntent(unsafeIntent: Intent) {
        handleIntent(unsafeIntent)
    }

    private fun handleIntent(unsafeIntent: Intent) {
        //val intent = SafeIntent(unsafeIntent)
        when (unsafeIntent.action) {
            Intent.ACTION_SEND -> {
                // TODO: Fix this, it's wrong but it works.
                unsafeIntent.clipData?.let { clip ->
                    Log.d("Received url: ${clip.getItemAt(0).text}")
                    urlToSend = clip.getItemAt(0).text.toString()
                }
            }
        }
    }

    override fun onClientConnected() {
        Log.d("Server: CLIENT CONNECTED!")
    }

    override fun onMessageReceived(message: String) {
        Log.d("Server: WE GOT A MESSAGE!!! $message")

        Intent(Intent.ACTION_VIEW, Uri.parse(message)).also {
            startActivity(it)
        }
    }

    override fun onServerConnected(): String? {
        Log.d("Client: CONNECTED TO A SERVER! Sending data: $urlToSend")
        return urlToSend
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val scanResult = IntentIntegrator.parseActivityResult(IntentIntegrator.REQUEST_CODE, resultCode, data)
        scanResult?.contents?.let {
            Log.d("scanned code: $it")
            Intent(Intent.ACTION_VIEW, Uri.parse(it)).also { intent ->
                startActivity(intent)
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}
