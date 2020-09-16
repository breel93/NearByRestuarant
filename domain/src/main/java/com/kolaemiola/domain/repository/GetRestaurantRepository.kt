package com.kolaemiola.domain.repository

import com.kolaemiola.domain.model.VenueModel
import kotlinx.coroutines.flow.Flow

interface GetRestaurantRepository {
  fun getRestaurants(latLong: String, near: String, radius: Int, limit: Int ) : Flow<List<VenueModel>>
}