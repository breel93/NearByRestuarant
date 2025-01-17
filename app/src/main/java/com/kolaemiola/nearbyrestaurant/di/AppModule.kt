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
package com.kolaemiola.nearbyrestaurant.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.kolaemiola.nearbyrestaurant.recent_venue.CachedVenueRepo
import com.kolaemiola.nearbyrestaurant.recent_venue.CachedVenueRepository
import com.kolaemiola.nearbyrestaurant.util.Constant.Companion.SHARED_PREF_KEY
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {
  @Provides
  @Singleton
  fun providesSharedPreferences(application: Application): SharedPreferences {
    return application.getSharedPreferences(SHARED_PREF_KEY, Context.MODE_PRIVATE)
  }
  @Provides
  @Singleton
  fun providesCachedVenueRepository(cachedVenueRepo: CachedVenueRepository): CachedVenueRepo {
    return cachedVenueRepo
  }
  @Provides
  @Singleton
  fun provideGson(): Gson {
    return Gson()
  }
}
