package com.kolaemiola.nearbyrestaurant.model

data class RestaurantViewState(
  val venuesInitial: List<Venue>? = null,
  val venues : List<Venue>? = null,
  val loading : Boolean? = false,
  val error : String? = ""
)