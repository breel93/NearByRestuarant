package com.kolaemiola.domain.usecase

import com.kolaemiola.domain.di.IoDispatcher
import com.kolaemiola.domain.model.VenueModel
import com.kolaemiola.domain.model.VenuesQueryParams
import com.kolaemiola.domain.repository.GetRestaurantRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRestaurantUseCase @Inject constructor(
  private val getRestaurantRepository: GetRestaurantRepository,
  @IoDispatcher private val ioDispatcher: CoroutineDispatcher
):FlowUseCase<VenuesQueryParams, List<VenueModel>>(){
  override val dispatcher: CoroutineDispatcher
    get() = ioDispatcher


  override fun execute(params: VenuesQueryParams?): Flow<List<VenueModel>> {

    return getRestaurantRepository.getRestaurants(latLong = params!!.latLong, near = params.near,
      radius = params.radius, limit = params.limit )
  }

}