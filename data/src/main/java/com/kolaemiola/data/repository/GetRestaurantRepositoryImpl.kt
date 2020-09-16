package com.kolaemiola.data.repository

import com.kolaemiola.data.BuildConfig.FOURSQUARE_API_KEY
import com.kolaemiola.data.BuildConfig.FOURSQUARE_SECRET
import com.kolaemiola.data.api.FourSquareAPI
import com.kolaemiola.data.api.FourSquareAPI.Companion.RESTAURANTCATEGORY
import com.kolaemiola.data.mapper.VenuesMapper
import com.kolaemiola.domain.repository.GetRestaurantRepository
import com.kolaemiola.domain.model.VenueModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetRestaurantRepositoryImpl @Inject constructor(
  private val fourSquareAPI: FourSquareAPI,
  private val venuesMapper: VenuesMapper
) : GetRestaurantRepository {
  override fun getRestaurants(
    latLong: String,
    near: String,
    radius: Int,
    limit: Int
  ): Flow<List<VenueModel>> = flow {
    val fourSquareResponse = fourSquareAPI.getVenues(FOURSQUARE_API_KEY,FOURSQUARE_SECRET,RESTAURANTCATEGORY,
      latLong,near, radius, limit)
    val venuesResult = fourSquareResponse.response.venues
    val venuesModel = venuesMapper.mapModelList(venuesResult)
    emit(venuesModel)
  }

}
