package com.kolaemiola.nearbyrestaurant.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import com.kolaemiola.nearbyrestaurant.NetworkUtils
import com.kolaemiola.nearbyrestaurant.R
import dagger.hilt.android.AndroidEntryPoint

private const val REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE = 34

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {


  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
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