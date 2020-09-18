package com.kolaemiola.nearbyrestaurant.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Venue(
  val id: String,
  val name: String,
  val location: Location
):Parcelable