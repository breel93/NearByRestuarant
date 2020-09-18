package com.kolaemiola.data.model

import com.squareup.moshi.Json

data class LocationResult(
  @field:Json(name = "address") val address: String? ="",
  @field:Json(name = "crossStreet") val crossStreet: String? ="",
  @field:Json(name = "lat") val lat: Double,
  @field:Json(name = "lng") val lng: Double,
  @field:Json(name = "city") val city: String? ="",
  @field:Json(name = "country") val country: String? =""
)