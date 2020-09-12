package com.kolaemiola.nearbyrestaurant

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.kolaemiola.nearbyrestaurant.databinding.FragmentMapsBinding


class MapsFragment : Fragment(), OnMapReadyCallback {
  private lateinit var viewMap: GoogleMap
  private lateinit var mapView: MapView
  private val MAP_BUNDLE_KEY = "MAP_BUNDLE_KEY_2"
  private lateinit var mLastLocation: Location
  private lateinit var mLocationRequest: LocationRequest
  private lateinit var binding: FragmentMapsBinding
  private lateinit var fusedLocationClient: FusedLocationProviderClient

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    binding = FragmentMapsBinding.inflate(inflater, container, false)
    mapView = binding.cityMap
    var mapBundle: Bundle? = null
    if (savedInstanceState != null) {
      mapBundle = savedInstanceState.getBundle(MAP_BUNDLE_KEY)
    }
    fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
    mapView.onCreate(mapBundle)
    mapView.getMapAsync(this)
    return binding.root
  }

  private fun resetMap() {
    viewMap.clear()
  }

  override fun onMapReady(googleMap: GoogleMap) {
    viewMap = googleMap
    mLocationRequest = LocationRequest()
    mLocationRequest.interval = 10000
    mLocationRequest.fastestInterval = 5000
    mLocationRequest.smallestDisplacement = 500f
    mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    checkLocationPermission()
    fusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback(googleMap),
      Looper.myLooper())
    if(googleMap != null){
      googleMap.isMyLocationEnabled = true
    }

  }
  private fun  mLocationCallback(viewMap: GoogleMap): LocationCallback = object : LocationCallback() {
    override fun onLocationResult(locationResult: LocationResult) {
      for (location in locationResult.locations) {
          mLastLocation = location
          val latLng = LatLng(location.latitude, location.longitude)
          val zoomLevel = 16.0f
          viewMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
          viewMap.animateCamera(CameraUpdateFactory.zoomTo(17f))
      }
    }
  }
  override fun onLowMemory() {
    super.onLowMemory()
    mapView.onLowMemory()
  }

  override fun onDestroy() {
    super.onDestroy()
    mapView.onDestroy()
  }

  override fun onPause() {
    super.onPause()
    mapView.onPause()
  }

  override fun onStop() {
    super.onStop()
    mapView.onStop()
  }

  override fun onStart() {
    super.onStart()
    mapView.onStart()
  }

  override fun onResume() {
    super.onResume()
    mapView.onResume()
  }
  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    var mapViewBundle = outState.getBundle(MAP_BUNDLE_KEY)
    if (mapViewBundle == null) {
      mapViewBundle = Bundle()
      outState.putBundle(MAP_BUNDLE_KEY, mapViewBundle)
    }

    mapView.onSaveInstanceState(mapViewBundle)
  }

  private fun checkLocationPermission() {
    if (ContextCompat.checkSelfPermission(requireContext(),ACCESS_FINE_LOCATION)  == PackageManager.PERMISSION_GRANTED
      && ContextCompat.checkSelfPermission(requireContext(),ACCESS_COARSE_LOCATION)  == PackageManager.PERMISSION_GRANTED) {
      if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), ACCESS_FINE_LOCATION)
        && ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), ACCESS_COARSE_LOCATION) ) {
        AlertDialog.Builder(requireContext())
          .setTitle("give permission")
          .setMessage("allow nearby to use location")
          .setPositiveButton(
            "OK"
          ) { _, _ ->
            ActivityCompat.requestPermissions(
              requireActivity(),
              arrayOf(ACCESS_FINE_LOCATION,ACCESS_COARSE_LOCATION),
              1
            )
          }
          .create()
          .show()
      } else {
        ActivityCompat.requestPermissions(
          requireActivity(),
          arrayOf(ACCESS_FINE_LOCATION,ACCESS_COARSE_LOCATION),
          1
        )
      }
    }
  }
}