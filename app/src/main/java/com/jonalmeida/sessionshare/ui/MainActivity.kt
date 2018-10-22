package com.jonalmeida.sessionshare.ui

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.LinearLayoutManager.VERTICAL
import android.view.Menu
import android.view.MenuItem
import com.jonalmeida.sessionshare.nsd.ShareNsdManager
import com.jonalmeida.sessionshare.R
import com.jonalmeida.sessionshare.ext.Log
import com.jonalmeida.sessionshare.ext.components
import com.jonalmeida.sessionshare.utils.SafeIntent

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        handleIntent(intent)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
        val adapter = DiscoveryListAdapter()
        lifecycle.addObserver(ShareNsdManager(components, adapter))
        discovery_recyclerview.adapter = adapter
        discovery_recyclerview.layoutManager = LinearLayoutManager(this, VERTICAL, false)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
//
//    override fun onNewIntent(unsafeIntent: Intent) {
//        handleIntent(unsafeIntent)
//    }

    private fun handleIntent(unsafeIntent: Intent) {
//        val intent = SafeIntent(unsafeIntent)
        when (intent.action) {
            Intent.ACTION_SEND -> {
                unsafeIntent.clipData?.let { clip ->
                    Log.d("Received url: ${clip.getItemAt(0).text}")
                }
            }
        }
    }
}
