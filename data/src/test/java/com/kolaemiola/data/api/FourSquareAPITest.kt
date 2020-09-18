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
package com.kolaemiola.data.api

import com.kolaemiola.data.api.FourSquareAPI.Companion.RESTAURANTCATEGORY
import com.kolaemiola.data.utils.BaseTest
import kotlinx.coroutines.runBlocking
import org.junit.Test
import com.google.common.truth.Truth.assertThat
import com.kolaemiola.data.BuildConfig.FOURSQUARE_API_KEY
import com.kolaemiola.data.BuildConfig.FOURSQUARE_SECRET
import com.kolaemiola.data.utils.MockVenueData
import com.kolaemiola.data.utils.RequestDispatcher

internal class FourSquareAPITest : BaseTest() {


  @Test
  fun `should return correct venues on success`() = runBlocking {
    val response = fourSquareAPI.getVenues(FOURSQUARE_API_KEY, FOURSQUARE_SECRET, RESTAURANTCATEGORY,
      "40.7484,-73.9857",
      "new+york", 250, 10)
    // then
    assertThat(MockVenueData.mockVenueResponse.venues.size).isEqualTo(response.response.venues.size)
    assertThat(MockVenueData.mockVenueList.first().name).isEqualTo(response.response.venues.first().name)
    assertThat(MockVenueData.mockVenueList.first().location.address).isEqualTo(response.response.venues.first().location.address)
  }
}
