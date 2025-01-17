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
package com.kolaemiola.nearbyrestaurant.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

fun <T> LiveData<T>.getOrAwaitValue(
  time: Long = 2,
  timeUnit: TimeUnit = TimeUnit.SECONDS
): T {
  var data: T? = null
  val latch = CountDownLatch(1)
  val observer = object : Observer<T> {
    override fun onChanged(o: T?) {
      data = o
      latch.countDown()
      this@getOrAwaitValue.removeObserver(this)
    }
  }

  this.observeForever(observer)

  // Don't wait indefinitely if the LiveData is not set.
  if (!latch.await(time, timeUnit)) {
    throw TimeoutException("LiveData value was never set.")
  }

  @Suppress("UNCHECKED_CAST")
  return data as T
}

fun <T> LiveData<T>.observeOnce(onChangeHandler: (T) -> Unit) {
  val observer = OneTimeObserver(onChangeHandler)
  // Lifecycle owner and observer
  observe(observer, observer)
}

internal class OneTimeObserver<T>(private val handler: (T) -> Unit) : Observer<T>, LifecycleOwner {

  private val lifecycle = LifecycleRegistry(this)

  init {
    lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
  }

  override fun getLifecycle(): Lifecycle = lifecycle

  override fun onChanged(t: T) {
    handler(t)
    lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
  }
}
