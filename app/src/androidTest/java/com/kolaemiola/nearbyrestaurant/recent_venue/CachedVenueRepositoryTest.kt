package com.kolaemiola.nearbyrestaurant.recent_venue

import android.content.Context
import android.content.SharedPreferences
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.gson.Gson
import com.kolaemiola.nearbyrestaurant.NearByRestaurant
import com.kolaemiola.nearbyrestaurant.util.Constant.Companion.LAST_VIEWED_STRING_LIST_KEY
import com.kolaemiola.nearbyrestaurant.util.Constant.Companion.SHARED_PREF_KEY
import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class CachedVenueRepositoryTest{
  private lateinit var sharedPrefs: SharedPreferences
  private lateinit var venueRepo: CachedVenueRepo

  @Before
  fun setUp(){
    // set shared preferences
    sharedPrefs = ApplicationProvider
      .getApplicationContext<NearByRestaurant>()
      .getSharedPreferences(SHARED_PREF_KEY, Context.MODE_PRIVATE)

    // start with a clean slate before each test run
    val sharedPrefEditor = sharedPrefs.edit()
    sharedPrefEditor.putString(LAST_VIEWED_STRING_LIST_KEY, null)
    sharedPrefEditor.commit()

    venueRepo = CachedVenueRepository(Gson(), sharedPrefs)
  }

  @After
  fun tearDown() {
    val sharedPrefEditor = sharedPrefs.edit()
    sharedPrefEditor.putString(LAST_VIEWED_STRING_LIST_KEY, null)
    sharedPrefEditor.commit()
  }
}