package com.kolaemiola.nearbyrestaurant.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Location(
  val address: String?="",
  val crossStreet: String?="",
  val lat: Double,
  val long: Double,
  val city: String?="",
  val country: String?=""
):Parcelable