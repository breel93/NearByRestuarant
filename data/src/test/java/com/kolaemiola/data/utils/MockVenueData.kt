package com.kolaemiola.data.utils

import com.kolaemiola.data.model.FourSquareResponse
import com.kolaemiola.data.model.LocationResult
import com.kolaemiola.data.model.VenueResponse
import com.kolaemiola.data.model.VenueResult

object MockVenueData {

  val mockVenueResponse = VenueResponse(
    mockVenueList,false
  )
  val mockFourSquareResponse = FourSquareResponse(
    mockVenueResponse
  )

  val mockVenueList: List<VenueResult>
  get() {
    return listOf(
      venueResult
    )
  }

  val locationResult = LocationResult(
    "200 5th Ave",
    "at W 23rd St",
    40.74198696405861,
    -73.98991319280194,
    "New York",
    "United States"
  )
  val venueResult = VenueResult(
    "4c5ef77bfff99c74eda954d3",
    "Eataly Flatiron",
    locationResult
  )

}
