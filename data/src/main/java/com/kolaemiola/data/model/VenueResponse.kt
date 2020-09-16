package com.kolaemiola.data.model

import com.squareup.moshi.Json


data class VenueResponse(
  @field:Json(name = "venues") val venues: List<VenueResult>,
  @field:Json(name = "confident") val confident: Boolean
)