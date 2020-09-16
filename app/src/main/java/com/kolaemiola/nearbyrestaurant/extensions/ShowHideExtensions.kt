package com.kolaemiola.nearbyrestaurant.extensions

import android.content.Context
import android.view.View
import android.widget.Toast

internal fun View.show() {
  this.visibility = View.VISIBLE
}
internal fun View.gone() {
  this.visibility = View.GONE
}
internal fun View.hide() {
  this.visibility = View.INVISIBLE
}

internal fun Toast.showToast(message:String, context: Context){
  Toast.makeText(context, message,Toast.LENGTH_LONG).show()
}


