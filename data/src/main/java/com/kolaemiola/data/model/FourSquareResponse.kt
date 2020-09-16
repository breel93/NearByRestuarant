package com.kolaemiola.data.model

import com.squareup.moshi.Json

data class FourSquareResponse (
  @field:Json(name = "response")
  val response: VenueResponse
)