package com.kolaemiola.nearbyrestaurant.model

data class RestaurantViewState(
  val venues : List<Venue>? = null,
  val loading : Boolean? = false,
  val error : String? = ""
)