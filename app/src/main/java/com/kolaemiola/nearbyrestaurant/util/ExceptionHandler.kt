package com.kolaemiola.nearbyrestaurant.util

import androidx.annotation.StringRes
import com.kolaemiola.nearbyrestaurant.R
import java.net.UnknownHostException


internal object ExceptionHandler {

  @StringRes
  fun parse(t: Throwable): Int {
    return when (t) {
      is UnknownHostException -> R.string.error_check_internet_connection
      else -> R.string.error_oops_error_occured
    }
  }

}