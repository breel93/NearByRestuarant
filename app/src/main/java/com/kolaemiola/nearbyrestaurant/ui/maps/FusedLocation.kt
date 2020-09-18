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
package com.kolaemiola.nearbyrestaurant.ui.maps

import android.content.Context
import android.location.Location
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.tasks.await

@ExperimentalCoroutinesApi
@Throws(SecurityException::class)
inline fun fusedLocationFlow(
  context: Context,
  configLocationRequest: LocationRequest.() -> Unit
): Flow<Location> = fusedLocationFlow(
  locationRequest = LocationRequest.create().apply(configLocationRequest),
  context = context
)

@Throws(SecurityException::class)
@ExperimentalCoroutinesApi
fun fusedLocationFlow(
  locationRequest: LocationRequest,
  context: Context
): Flow<Location> = channelFlow {
  val locationClient = LocationServices.getFusedLocationProviderClient(context)
  val locationCallback = object : LocationCallback() {
    override fun onLocationResult(result: LocationResult) {
      result.locations.forEachByIndex { offerCatching(it) }
    }
  }
  locationClient.lastLocation.await<Location?>()?.let { send(it) }
  locationClient.requestLocationUpdates(locationRequest, locationCallback, null).await()
  awaitClose {
    locationClient.removeLocationUpdates(locationCallback)
  }
}

/**
 * [SendChannel.offer] that returns `false` when this [SendChannel.isClosedForSend], instead of
 * throwing.
 *
 * [SendChannel.offer] throws when the channel is closed. In race conditions, especially when using
 * multithreaded dispatchers, that can lead to uncaught exceptions as offer is often called from
 * non suspending functions that don't catch the default [CancellationException] or any other
 * exception that might be the cause of the closing of the channel.
 */
// Copy pasted from splitties.coroutines
fun <E> SendChannel<E>.offerCatching(element: E): Boolean {
  return runCatching { offer(element) }.getOrDefault(false)
}

/**
 * Iterates the receiver [List] using an index instead of an [Iterator] like [forEach] would do.
 * Using this function saves an [Iterator] allocation, which is good for immutable lists or usages
 * confined to a single thread like UI thread only use.
 * However, this method will not detect concurrent modification, except if the size of the list
 * changes on an iteration as a result, which may lead to unpredictable behavior.
 *
 * @param action the action to invoke on each list element.
 */
// Copy pasted from splitties.collections
inline fun <T> List<T>.forEachByIndex(action: (T) -> Unit) {
  val initialSize = size
  for (i in 0..lastIndex) {
    if (size != initialSize) throw ConcurrentModificationException()
    action(get(i))
  }
}
