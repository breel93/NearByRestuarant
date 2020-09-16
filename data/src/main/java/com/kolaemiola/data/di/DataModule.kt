package com.kolaemiola.data.di

import com.kolaemiola.data.repository.GetRestaurantRepositoryImpl
import com.kolaemiola.data.api.FourSquareAPI
import com.kolaemiola.domain.repository.GetRestaurantRepository
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {
  private const val BASE_URL: String = "https://api.foursquare.com"
  @Provides
  fun provideOkHTTPClient(): OkHttpClient {
    val logging = HttpLoggingInterceptor()
    logging.level = HttpLoggingInterceptor.Level.BASIC
    return OkHttpClient.Builder()
      .addInterceptor(logging)
      .build()
  }

  @Singleton
  @Provides
  fun provideFourSquareRetrofit(okHttpClient: OkHttpClient): FourSquareAPI {
    val moshi = Moshi.Builder()
      .add(KotlinJsonAdapterFactory()).build()
    return Retrofit.Builder()
      .baseUrl(BASE_URL)
      .addConverterFactory(MoshiConverterFactory.create(moshi))
      .client(okHttpClient)
      .build()
      .create(FourSquareAPI::class.java)
  }
  @Provides
  fun providesVenueRepository(getRestaurantRepositoryImpl: GetRestaurantRepositoryImpl):
      GetRestaurantRepository {
    return getRestaurantRepositoryImpl
  }
}