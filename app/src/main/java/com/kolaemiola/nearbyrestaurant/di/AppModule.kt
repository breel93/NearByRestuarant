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