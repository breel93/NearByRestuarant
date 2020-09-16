package com.kolaemiola.nearbyrestaurant.ui.adapter

import com.kolaemiola.nearbyrestaurant.model.Venue

interface VenueClickListener {
  fun showVenueDetail(venue: Venue)
}