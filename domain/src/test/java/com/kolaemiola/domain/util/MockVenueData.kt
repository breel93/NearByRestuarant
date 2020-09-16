package com.kolaemiola.domain.util

import com.kolaemiola.domain.model.LocationModel
import com.kolaemiola.domain.model.VenueModel

object MockVenueData {


  val mockVenueList: List<VenueModel>
    get() {
      return listOf(
        venueResult
      )
    }

  val locationResult = LocationModel(
    "200 5th Ave",
    "at W 23rd St",
    40.74198696405861,
    -73.98991319280194,
    "New York",
    "United States"
  )
  val venueResult = VenueModel(
    "4c5ef77bfff99c74eda954d3",
    "Eataly Flatiron",
    locationResult
  )

}