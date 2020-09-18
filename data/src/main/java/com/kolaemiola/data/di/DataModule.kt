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
