package com.kolaemiola.nearbyrestaurant.model

data class Location(
  val address: String,
  val crossStreet: String?="",
  val lat: Double,
  val long: Double,
  val city: String,
  val country: String
)