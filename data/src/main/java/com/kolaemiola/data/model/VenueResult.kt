package com.kolaemiola.data.model


import com.squareup.moshi.Json

data class VenueResult(
  @field:Json(name = "id") val id: String,
  @field:Json(name = "name") val name: String,
  @field:Json(name = "location") val location: LocationResult
)