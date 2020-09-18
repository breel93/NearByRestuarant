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
package com.kolaemiola.data.mapper

import com.google.common.truth.Truth.assertThat
import com.kolaemiola.data.utils.MockVenueData
import org.junit.Test

class VenuesMapperTest {
  private val venuesMapper = VenuesMapper()

  @Test
  fun `should map VenueResult to VenueModel`() {
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
