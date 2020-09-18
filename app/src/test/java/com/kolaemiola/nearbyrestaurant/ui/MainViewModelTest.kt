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
package com.kolaemiola.nearbyrestaurant.ui

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.kolaemiola.domain.model.LocationModel
import com.kolaemiola.domain.model.VenueModel
import com.kolaemiola.domain.model.VenuesQueryParams
import com.kolaemiola.domain.usecase.GetRestaurantUseCase
import com.kolaemiola.nearbyrestaurant.model.RestaurantViewState
import com.kolaemiola.nearbyrestaurant.recent_venue.CachedVenueRepo
import com.kolaemiola.nearbyrestaurant.util.CoroutineTestRule
import com.kolaemiola.nearbyrestaurant.util.observeOnce
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MainViewModelTest {
  @get:Rule
  val rule = CoroutineTestRule()

  @get:Rule
  val instantExecutorRule = InstantTaskExecutorRule()
  private val getRestaurantUseCase = mock<GetRestaurantUseCase>()
  private val cachedVenueRepo = mock<CachedVenueRepo>()
  private val restaurantViewStateObserver = mock<Observer<RestaurantViewState>>()
  private lateinit var viewModel: MainViewModel
  private val application = Application()

  @Before
  fun setUp() {
    viewModel = MainViewModel(getRestaurantUseCase, cachedVenueRepo, application).apply {
      searchRestaurantState.observeForever(restaurantViewStateObserver)
    }
  }

  @Test
  fun `should return state on success`() = rule.dispatcher.runBlockingTest {
    val locationModel = LocationModel("200 5th Ave",
      "at W 23rd St",
      40.74198696405861,
      -73.98991319280194,
      "New York",
      "United States")
    val venue = VenueModel("4c5ef77bfff99c74eda954d3",
      "Eataly Flatiron",
      locationModel
    )

    val venues = listOf(venue)
    val queryParams = VenuesQueryParams("", "", 250, 10)
    // given
    val channel = Channel<List<VenueModel>>()
    val flow = channel.consumeAsFlow()

    // when
    whenever(getRestaurantUseCase(queryParams)).thenReturn(flow)
    launch {
      channel.send(venues)
    }
    viewModel.getRestaurants(queryParams.latLong, queryParams.radius, queryParams.limit)
    viewModel.searchRestaurantState.observeOnce { state ->
      verify(restaurantViewStateObserver).onChanged(state)
    }
  }
}
