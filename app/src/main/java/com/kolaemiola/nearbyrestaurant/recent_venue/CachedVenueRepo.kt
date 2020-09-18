package com.kolaemiola.nearbyrestaurant.recent_venue

import com.kolaemiola.nearbyrestaurant.model.Venue
import com.kolaemiola.nearbyrestaurant.util.Result

interface CachedVenueRepo {
  fun updateCacheVenue(venues: List<Venue>, onComplete: (Boolean) -> Unit)
  fun clearCacheVenue( onComplete: (Boolean) -> Unit)
  fun getCachedVenue(): Result<List<Venue>>
}