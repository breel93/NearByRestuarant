package com.kolaemiola.nearbyrestaurant.ui.maps

import android.Manifest
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import androidx.core.app.ActivityCompat
import com.google.android.material.snackbar.Snackbar
import com.kolaemiola.nearbyrestaurant.R
import timber.log.Timber
import java.util.*


private const val REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE = 34

fun MapsFragment.requestForegroundPermissions(){
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
    Timber.d( "Request foreground only permission")
    ActivityCompat.requestPermissions(
      requireActivity(),
      arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
      REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE
    )
  }
}

fun MapsFragment.foregroundPermissionApproved() :
    Boolean = PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(requireContext(),
  Manifest.permission.ACCESS_FINE_LOCATION
)

fun MapsFragment.getCityName(location: Location) : String{
  val geocoder = Geocoder(requireContext(), Locale.getDefault())
  val addresses: List<Address> = geocoder.getFromLocation(location.latitude, location.longitude, 1)
  return addresses[0].getAddressLine(0)
}