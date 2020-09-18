package com.kolaemiola.nearbyrestaurant.recent_venue

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kolaemiola.nearbyrestaurant.model.Venue
import com.kolaemiola.nearbyrestaurant.util.Constant.Companion.LAST_VIEWED_STRING_LIST_KEY
import com.kolaemiola.nearbyrestaurant.util.Failure
import com.kolaemiola.nearbyrestaurant.util.Result
import com.kolaemiola.nearbyrestaurant.util.Success
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CachedVenueRepository @Inject constructor(
  private val gson: Gson,
  private val sharedPreferences: SharedPreferences
) : CachedVenueRepo{

  override fun updateCacheVenue(venues: List<Venue>, onComplete: (Boolean) -> Unit) {
    val venuesEditor = sharedPreferences.edit()
    val linkedList = LinkedList<Venue>(venues)
    venuesEditor.putString(LAST_VIEWED_STRING_LIST_KEY, transformListToString(linkedList))
    return onComplete(venuesEditor.commit())
  }

  override fun getCachedVenue(): Result<List<Venue>> {
    getRestaurantFromSource()?.let { venueList ->
      return Success(transformJsonToList(venueList))
    }
    return Failure(Throwable("Failed to get venues"))
  }

  private fun getRestaurantFromSource(): String? {
    return sharedPreferences.getString(LAST_VIEWED_STRING_LIST_KEY, null)
  }

  private fun transformJsonToList(characterData: String): LinkedList<Venue> {
    return gson.fromJson<LinkedList<Venue>>(
      characterData, object : TypeToken<LinkedList<Venue>>() {}.type
    )
  }

  private fun transformListToString(venueList: LinkedList<Venue>): String {
    return gson.toJson(venueList, object : TypeToken<LinkedList<Venue>>() {}.type)
  }

  override fun clearCacheVenue(onComplete: (Boolean) -> Unit) {
    val venuesEditor = sharedPreferences.edit()
    venuesEditor.putString(LAST_VIEWED_STRING_LIST_KEY, null)
    return onComplete(venuesEditor.commit())
  }

}