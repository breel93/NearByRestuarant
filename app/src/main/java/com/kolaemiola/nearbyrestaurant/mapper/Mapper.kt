package com.kolaemiola.nearbyrestaurant.mapper

import com.kolaemiola.domain.model.LocationModel
import com.kolaemiola.domain.model.VenueModel
import com.kolaemiola.nearbyrestaurant.model.Location
import com.kolaemiola.nearbyrestaurant.model.Venue

internal fun VenueModel.toAppModel(location: Location): Venue = Venue(
  id, name, location
)

internal fun LocationModel.toAppModel(): Location = Location(
  address, crossStreet, lat, long, city, country
)