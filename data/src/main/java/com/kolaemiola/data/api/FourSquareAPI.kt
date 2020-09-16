package com.kolaemiola.data.api

import com.kolaemiola.data.model.FourSquareResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface FourSquareAPI {
  @GET("/v2/venues/search")
  suspend fun getVenues(
    @Query("client_id") clientId: String,
    @Query("client_secret") clientSecret: String,
    @Query("categoryId") categoryId: String,
    @Query("ll") latLong: String,
    @Query("near") near: String,
    @Query("radius") radius: Int,
    @Query("limit") limit: Int,
    @Query("v") apiVersion: String = FOURSQUARE_API_VERSION
  ) : FourSquareResponse

  companion object {
    // FOURSQUARE API
    const val FOURSQUARE_API_VERSION = "20180323"
    const val RESTAURANTCATEGORY = "4d4b7105d754a06374d81259"
  }
}