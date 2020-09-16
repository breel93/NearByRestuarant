package com.kolaemiola.nearbyrestaurant.ui


import com.kolaemiola.nearbyrestaurant.model.Location
import com.kolaemiola.nearbyrestaurant.model.RestaurantViewState
import com.kolaemiola.nearbyrestaurant.model.Venue

object MockVenueStateData {
  val mockVenueStateData= RestaurantViewState(
    mockVenueList,
    false,
    ""
  )


  val mockVenueList: List<Venue>
    get() {
      return listOf(
        venue
      )
    }

  val location = Location(
    "200 5th Ave",
    "at W 23rd St",
    40.74198696405861,
    -73.98991319280194,
    "New York",
    "United States"
  )
  val venue = Venue(
    "4c5ef77bfff99c74eda954d3",
    "Eataly Flatiron",
    location
  )
}