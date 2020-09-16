package com.kolaemiola.data.mapper

import com.google.common.truth.Truth.assertThat
import com.kolaemiola.data.utils.MockVenueData
import org.junit.Test

class VenuesMapperTest{
  private val venuesMapper = VenuesMapper()

  @Test
  fun `should map VenueResult to VenueModel`(){
    val venuesResult = MockVenueData.venueResult
    val venueModel = venuesMapper.from(venuesResult)
    assertThat(venuesResult.id).isEqualTo(venueModel.id)
    assertThat(venuesResult.location.address).isEqualTo(venueModel.locationModel.address)
    assertThat(venuesResult.location.crossStreet).isEqualTo(venueModel.locationModel.crossStreet)
    assertThat(venuesResult.location.lat).isEqualTo(venueModel.locationModel.lat)
    assertThat(venuesResult.location.lng).isEqualTo(venueModel.locationModel.long)
    assertThat(venuesResult.name).isEqualTo(venueModel.name)
  }
}