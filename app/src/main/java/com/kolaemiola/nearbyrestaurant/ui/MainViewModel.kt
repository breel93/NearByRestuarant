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
import android.location.Address
import android.location.Geocoder
import android.location.Location
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.LocationRequest
import com.kolaemiola.domain.model.VenuesQueryParams
import com.kolaemiola.domain.usecase.GetRestaurantUseCase
import com.kolaemiola.nearbyrestaurant.mapper.toAppModel
import com.kolaemiola.nearbyrestaurant.model.state.RestaurantViewState
import com.kolaemiola.nearbyrestaurant.model.Venue
import com.kolaemiola.nearbyrestaurant.recent_venue.CachedVenueRepo
import com.kolaemiola.nearbyrestaurant.ui.view_extensions.fusedLocationFlow
import com.kolaemiola.nearbyrestaurant.util.Success
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.Locale
import kotlin.collections.ArrayList
import kotlin.collections.List
import kotlin.collections.isNotEmpty
import kotlin.collections.map

class MainViewModel @ViewModelInject constructor(
  private val getRestaurantUseCase: GetRestaurantUseCase,
  private val cachedVenueRepo: CachedVenueRepo,
  application: Application
) : AndroidViewModel(application) {

  private val _searchRestaurantState = MutableLiveData<RestaurantViewState>()
  val searchRestaurantState: LiveData<RestaurantViewState> = _searchRestaurantState

  private val _usersLastLocation = MutableLiveData<Location>()
  val usersLocation: LiveData<Location> = _usersLastLocation

  private val recentCachedList = ArrayList<Venue>()
  init {
    _searchRestaurantState.value =
      RestaurantViewState()
  }

  fun getRestaurants(latLong: String, radius: Int, limit: Int) {
    viewModelScope.launch {
      val near = getCityName(application = getApplication())
      val venuesQueryParams = VenuesQueryParams(latLong, near, radius, limit)
      getRestaurantUseCase(venuesQueryParams).onStart {
        _searchRestaurantState.value = _searchRestaurantState.value?.copy(
            loading = true
          )
      }.catch {
        _searchRestaurantState.value = _searchRestaurantState.value?.copy(
          loading = false,
          error = it.message
        )

      }.collect { results ->
        _searchRestaurantState.value = _searchRestaurantState.value?.copy(
          loading = false,
          venues = results.map {
            it.toAppModel(it.locationModel.toAppModel())
          }.also {
            Timber.i("venues fetched successfully: $it ")
          }
        )
      }
    }
  }

  fun locationRequest(locationRequest: LocationRequest) {
    viewModelScope.launch {
      fusedLocationFlow(
        locationRequest,
        getApplication()
      ).collect { location ->
        _usersLastLocation.value = location
      }
    }
  }

  // For scrolling to marker position based on place of interest results
  private val _currentMarkerPosition = MutableLiveData<Int>()
  val currentMarkerPosition: LiveData<Int> get() = _currentMarkerPosition

  fun setCurrentMarkerPosition(placeIndex: Int) {
    _currentMarkerPosition.value = placeIndex
  }

  // For scrolling to restaurant index based on marker clicked
  private val _currentPlaceIndex = MutableLiveData<Int>()
  val currentPlaceIndex: LiveData<Int> get() = _currentPlaceIndex
  fun selectPlaceOnRestaurantList(placeIndex: Int) {
    _currentPlaceIndex.value = placeIndex
  }

  // cache region
  fun getRecentChecked() {
    when (val cachedVenues = cachedVenueRepo.getCachedVenue()) {
      is Success -> {
        recentCachedList.clear()
        recentCachedList.addAll(cachedVenues.data)
        if (recentCachedList.isNotEmpty()) {
          _searchRestaurantState.value =
            _searchRestaurantState.value?.copy(venuesInitial = cachedVenues.data, loading = false)
        }
      }
    }
  }

  private var _addRecentCheckData = MutableLiveData<Boolean>()
  val addRecentCheckData: LiveData<Boolean>
    get() = _addRecentCheckData

  fun updateRecentCheck(venues: List<Venue>) {
    cachedVenueRepo.updateCacheVenue(venues = venues) { updateCompleted ->
      _addRecentCheckData.value = updateCompleted
    }
  }

  fun clearCache() {
    cachedVenueRepo.clearCacheVenue {}
  }

  /***
   * temp, TODO clean up getting city name
   *
   */
  private fun getCityName(application: Application): String {
    val location = _usersLastLocation.value
    val geocoder = Geocoder(application.applicationContext, Locale.getDefault())
    val addresses: List<Address> =
      geocoder.getFromLocation(location!!.latitude, location.longitude, 1)
    return addresses[0].locality
  }
}
