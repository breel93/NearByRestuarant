package com.kolaemiola.nearbyrestaurant.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.google.common.truth.Truth.assertThat
import com.kolaemiola.domain.usecase.GetRestaurantUseCase
import com.kolaemiola.nearbyrestaurant.model.RestaurantViewState
import com.kolaemiola.nearbyrestaurant.util.CoroutineTestRule
import com.nhaarman.mockitokotlin2.mock
import org.junit.Before
import org.junit.Rule

class MainViewModelTest{
  @get:Rule
  val rule = CoroutineTestRule()

  @get:Rule
  val instantExecutorRule = InstantTaskExecutorRule()
  private val getRestaurantUseCase = mock<GetRestaurantUseCase>()
  private val restaurantViewStateObserver = mock<Observer<RestaurantViewState>>()
  private lateinit var viewModel: MainViewModel

  @Before
  fun setUp(){
//    viewModel = MainViewModel(getRestaurantUseCase)
  }

}