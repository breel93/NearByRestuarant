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
package com.kolaemiola.domain.util

import com.kolaemiola.domain.model.LocationModel
import com.kolaemiola.domain.model.VenueModel

object MockVenueData {

  val mockVenueList: List<VenueModel>
    get() {
      return listOf(
        venueResult
      )
    }

  val locationResult = LocationModel(
    "200 5th Ave",
    "at W 23rd St",
    40.74198696405861,
    -73.98991319280194,
    "New York",
    "United States"
  )
  val venueResult = VenueModel(
    "4c5ef77bfff99c74eda954d3",
    "Eataly Flatiron",
    locationResult
  )
}
