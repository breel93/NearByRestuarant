package com.kolaemiola.nearbyrestaurant.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.kolaemiola.nearbyrestaurant.R
import com.kolaemiola.nearbyrestaurant.util.NetworkUtils
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import timber.log.Timber.DebugTree


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {


  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    Timber.plant(DebugTree())
  }

  protected fun onNetworkChange(block: (Boolean) -> Unit) {
    NetworkUtils.getNetworkStatus(this)
      .observe(this, Observer { isConnected ->
        block(isConnected)
      })
  }


  override fun onStop() {
    super.onStop()
  }

}