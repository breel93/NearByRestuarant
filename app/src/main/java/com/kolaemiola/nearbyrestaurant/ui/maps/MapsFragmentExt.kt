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

import android.Manifest
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import com.google.android.material.snackbar.Snackbar
import com.kolaemiola.nearbyrestaurant.R
import timber.log.Timber

private const val REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE = 34

fun MapsFragment.requestForegroundPermissions() {
  val provideRationale = foregroundPermissionApproved()
  if (provideRationale) {
    Snackbar.make(this.requireView(), R.string.permission_rationale, Snackbar.LENGTH_LONG)
      .setAction(R.string.ok) {
        ActivityCompat.requestPermissions(
          requireActivity(),
          arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
          REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE
        )
      }
      .show()
  } else {
    Timber.d("Request foreground only permission")
    ActivityCompat.requestPermissions(
      requireActivity(),
      arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
      REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE
    )
  }
}

fun MapsFragment.foregroundPermissionApproved():
    Boolean = PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(requireContext(),
  Manifest.permission.ACCESS_FINE_LOCATION
)
