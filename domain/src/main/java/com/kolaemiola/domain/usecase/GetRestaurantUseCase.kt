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
package com.kolaemiola.domain.usecase

import com.kolaemiola.domain.di.IoDispatcher
import com.kolaemiola.domain.model.VenueModel
import com.kolaemiola.domain.model.VenuesQueryParams
import com.kolaemiola.domain.repository.GetRestaurantRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRestaurantUseCase @Inject constructor(
  private val getRestaurantRepository: GetRestaurantRepository,
  @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : FlowUseCase<VenuesQueryParams, List<VenueModel>>() {
  override val dispatcher: CoroutineDispatcher
    get() = ioDispatcher

  override fun execute(params: VenuesQueryParams?): Flow<List<VenueModel>> {

    return getRestaurantRepository.getRestaurants(latLong = params!!.latLong, near = params.near,
      radius = params.radius, limit = params.limit)
  }
}
