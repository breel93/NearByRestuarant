package com.kolaemiola.data.mapper

import com.kolaemiola.data.model.VenueResult
import com.kolaemiola.domain.model.LocationModel
import com.kolaemiola.domain.model.VenueModel
import javax.inject.Inject

class VenuesMapper @Inject constructor() : Mapper<VenueResult, VenueModel> {
  override fun from(data: VenueResult): VenueModel =
    VenueModel(
      id = data.id,
      name = data.name,
      locationModel = LocationModel(
        address = data.location.address,
        crossStreet = data.location.crossStreet,
        lat = data.location.lat,
        long = data.location.lng,
        city = data.location.city,
        country = data.location.country
      )
    )

}

