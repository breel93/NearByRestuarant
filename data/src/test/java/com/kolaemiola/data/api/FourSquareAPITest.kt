package com.kolaemiola.data.api

import com.kolaemiola.data.api.FourSquareAPI.Companion.RESTAURANTCATEGORY
import com.kolaemiola.data.utils.BaseTest
import kotlinx.coroutines.runBlocking
import org.junit.Test
import com.google.common.truth.Truth.assertThat
import com.kolaemiola.data.BuildConfig.FOURSQUARE_API_KEY
import com.kolaemiola.data.BuildConfig.FOURSQUARE_SECRET
import com.kolaemiola.data.utils.MockVenueData

internal class FourSquareAPITest : BaseTest() {


  @Test
  fun `should return correct venues on success`() = runBlocking {
    val response = fourSquareAPI.getVenues(FOURSQUARE_API_KEY,FOURSQUARE_SECRET, RESTAURANTCATEGORY,
      "40.7484,-73.9857",
      "new+york",250,10)
    //then
    assertThat(MockVenueData.mockVenueResponse.venues.size).isEqualTo(response.response.venues.size)
    assertThat(MockVenueData.mockVenueList.first().name).isEqualTo(response.response.venues.first().name)
    assertThat(MockVenueData.mockVenueList.first().location.address).isEqualTo(response.response.venues.first().location.address)
  }
}