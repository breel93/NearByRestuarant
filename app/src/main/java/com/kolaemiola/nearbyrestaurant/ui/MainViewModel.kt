package com.kolaemiola.nearbyrestaurant.ui

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kolaemiola.domain.model.VenuesQueryParams
import com.kolaemiola.domain.usecase.GetRestaurantUseCase
import com.kolaemiola.nearbyrestaurant.model.RestaurantViewState
import com.kolaemiola.nearbyrestaurant.mapper.toAppModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import timber.log.Timber

class MainViewModel @ViewModelInject constructor(
  private val getRestaurantUseCase: GetRestaurantUseCase
) : ViewModel(){

  private val _searchRestaurantState = MutableLiveData<RestaurantViewState>()
  val searchRestaurantState : LiveData<RestaurantViewState> = _searchRestaurantState

  init {
    _searchRestaurantState.value = RestaurantViewState()
  }


  fun getRestaurants(latLong:String, near:String, radius:Int, limit:Int){
    viewModelScope.launch {
      val venuesQueryParams = VenuesQueryParams(latLong,near,radius,limit)
      getRestaurantUseCase(venuesQueryParams).onStart {
        Log.d("find this", "Started")
        _searchRestaurantState.value = _searchRestaurantState.value?.copy(
          loading = true
        )
      }.catch {
        Log.d("find this", it.message!!)
        Timber.i("characters fetched successfully: error")
        _searchRestaurantState.value = _searchRestaurantState.value?.copy(
          loading = false,
          error = "some error"
        )
      }.collect { results ->
        Log.d("find this", "The message  $results")
        Timber.i("characters fetched successfully: $results ")
        _searchRestaurantState.value = _searchRestaurantState.value?.copy(
          loading = false,
          venues = results.map { it.toAppModel(it.locationModel.toAppModel())
          }.also {
            Timber.i("characters fetched successfully: $it ")
          }
        )
      }
    }
  }

}