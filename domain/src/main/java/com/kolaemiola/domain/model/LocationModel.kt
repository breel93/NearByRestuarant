package com.kolaemiola.domain.model

data class LocationModel(
  val address: String?="",
  val crossStreet: String?="",
  val lat: Double,
  val long: Double,
  val city: String?="",
  val country: String?=""
)