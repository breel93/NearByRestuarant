package com.kolaemiola.data.repository

import com.kolaemiola.data.mapper.VenuesMapper
import com.kolaemiola.data.utils.BaseTest
import com.kolaemiola.domain.repository.GetRestaurantRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import com.google.common.truth.Truth.assertThat
import com.kolaemiola.data.utils.MockVenueData
import org.junit.Before
import org.junit.Test


internal class GetRestaurantRepositoryImplTest: BaseTest(){
  private lateinit var getRestaurantRepository: GetRestaurantRepository
  private val venuesMapper = VenuesMapper()

  @Before
  override fun setup() {
    super.setup()
    getRestaurantRepository = GetRestaurantRepositoryImpl(fourSquareAPI,venuesMapper)
  }

  @Test
  fun `should return venues data`() = runBlocking {
    val venuesResponse = getRestaurantRepository.getRestaurants("40.7484,-73.9857",
      "new+york",250,10).first()
    assertThat(venuesResponse).isNotEmpty()
  }

  @Test
  fun `should return correct venue data`() = runBlocking {
    val venuesResponse = getRestaurantRepository.getRestaurants("40.7484,-73.9857",
      "new+york",250,10).first().first()
      assertThat(venuesResponse.name).isEqualTo(MockVenueData.venueResult.name)
      assertThat(venuesResponse.id).isEqualTo(MockVenueData.venueResult.id)
      assertThat(venuesResponse.locationModel.address).isEqualTo(MockVenueData.venueResult.location.address)
      assertThat(venuesResponse.locationModel.crossStreet).isEqualTo(MockVenueData.venueResult.location.crossStreet)
      assertThat(venuesResponse.locationModel.long).isEqualTo(MockVenueData.venueResult.location.lng)
      assertThat(venuesResponse.locationModel.lat).isEqualTo(MockVenueData.venueResult.location.lat)

  }

}