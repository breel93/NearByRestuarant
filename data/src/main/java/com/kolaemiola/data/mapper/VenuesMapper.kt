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

import com.kolaemiola.data.model.VenueResult
import com.kolaemiola.domain.model.LocationModel
import com.kolaemiola.domain.model.VenueModel
import javax.inject.Inject

class VenuesMapper @Inject constructor() : Mapper<VenueResult, VenueModel> {
  override fun from(data: VenueResult): VenueModel =
    VenueModel(
      id = data.id,
      name = data.name,
      locationModel = LocationModel(
        address = data.location.address,
        crossStreet = data.location.crossStreet,
        lat = data.location.lat,
        long = data.location.lng,
        city = data.location.city,
        country = data.location.country
      )
    )
}
