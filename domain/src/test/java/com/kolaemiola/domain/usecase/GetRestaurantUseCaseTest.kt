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

import com.kolaemiola.domain.model.VenueModel
import com.kolaemiola.domain.model.VenuesQueryParams
import com.kolaemiola.domain.repository.GetRestaurantRepository
import com.kolaemiola.domain.util.MockVenueData
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test

class GetRestaurantUseCaseTest {
  private val testDispatcher = TestCoroutineDispatcher()
  private lateinit var venueUseCase: GetRestaurantUseCase

  private val getRestaurantRepository = mock<GetRestaurantRepository>()

  @Before
  fun setUp() {
    venueUseCase = GetRestaurantUseCase(getRestaurantRepository, testDispatcher)
  }

  @Test
  fun `should return list of restaurants`() = runBlockingTest {
    // given
    val channel = Channel<List<VenueModel>>()
    val flow = channel.consumeAsFlow()
    // when
    doReturn(flow).whenever(getRestaurantRepository).getRestaurants("40.7484,-73.9857", "new york", 250, 10)
    launch {
      channel.send(MockVenueData.mockVenueList)
    }
    val params = VenuesQueryParams("40.7484,-73.9857", "new york", 250, 10)
    val venues = venueUseCase(params).first()
    val venue = venues.first()

    // then
    assertThat(venue.id).isEqualTo(MockVenueData.venueResult.id)
    assertThat(venue.name).isEqualTo(MockVenueData.venueResult.name)
    assertThat(venue.locationModel.address).isEqualTo(MockVenueData.venueResult.locationModel.address)
    assertThat(venue.locationModel.crossStreet).isEqualTo(MockVenueData.venueResult.locationModel.crossStreet)
    assertThat(venue.locationModel.lat).isEqualTo(MockVenueData.venueResult.locationModel.lat)
    assertThat(venue.locationModel.long).isEqualTo(MockVenueData.venueResult.locationModel.long)
  }
}
