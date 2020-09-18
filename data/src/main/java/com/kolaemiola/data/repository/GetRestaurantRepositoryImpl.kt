/**
 *  Designed and developed by Kola Emiola
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
*/
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
    val fourSquareResponse = fourSquareAPI.getVenues(FOURSQUARE_API_KEY, FOURSQUARE_SECRET, RESTAURANTCATEGORY,
      latLong, near, radius, limit)
    val venuesResult = fourSquareResponse.response.venues
    val venuesModel = venuesMapper.mapModelList(venuesResult)
    emit(venuesModel)
  }
}
