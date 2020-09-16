package com.kolaemiola.nearbyrestaurant.ui.maps

import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.kolaemiola.nearbyrestaurant.R
import com.kolaemiola.nearbyrestaurant.databinding.FragmentMapsBinding
import com.kolaemiola.nearbyrestaurant.extensions.hide
import com.kolaemiola.nearbyrestaurant.extensions.show
import com.kolaemiola.nearbyrestaurant.model.RestaurantQuery
import com.kolaemiola.nearbyrestaurant.model.Venue
import com.kolaemiola.nearbyrestaurant.ui.MainViewModel
import com.kolaemiola.nearbyrestaurant.ui.adapter.RestaurantRecyclerAdapter
import com.kolaemiola.nearbyrestaurant.ui.adapter.VenueClickListener
import com.kolaemiola.nearbyrestaurant.ui.bitmapDescriptorFromVector
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class MapsFragment : Fragment() {
  private lateinit var viewMap: GoogleMap
  private lateinit var mapView: MapView
  private val MAP_BUNDLE_KEY = "MAP_BUNDLE_KEY_2"
  private lateinit var mLocationRequest: LocationRequest
  private lateinit var locationCallback: LocationCallback
  private lateinit var binding: FragmentMapsBinding
  private lateinit var fusedLocationClient: FusedLocationProviderClient
  private val markerList = ArrayList<Marker>()
  private var currentLocation: Location? = null
  private val viewModel: MainViewModel by viewModels()
  private lateinit var navController: NavController
  private lateinit var restaurantMarker: BitmapDescriptor

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
    mapView.onCreate(mapBundle)
    mapView.getMapAsync(callback)

    fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

    if(foregroundPermissionApproved()){
      locationRequest()
      subscribeToLocationUpdates()
    }else{
      requestForegroundPermissions()
    }
    updateUI()
    restaurantMarker = bitmapDescriptorFromVector(requireContext(), R.drawable.ic_food_tray)!!
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    navController = Navigation.findNavController(view)
  }


  private fun updateUI(){
    viewModel.searchRestaurantState.observe(viewLifecycleOwner) { state ->
      state.venues?.let { venues ->
        getRestaurantState(venues)
        Toast.makeText(requireContext(), venues.size.toString(), Toast.LENGTH_LONG).show()
      }
      loadingState(state.loading!!)
//      errorState(state.error!!)
    }

  }

  private fun resetMap() {
    viewMap.clear()
  }

  private val callback = OnMapReadyCallback { googleMap ->
    viewMap = googleMap
    locationRequest()
    if(foregroundPermissionApproved()){
      try{
        googleMap.isMyLocationEnabled = true
      }catch (unlikely: SecurityException){
        Timber.e( "Lost location permissions. Couldn't remove updates. $unlikely")
      }

    }
  }

  private fun getRestaurantState(venues : List<Venue>?){
    val venueClickListener = object : VenueClickListener {
      override fun showVenueDetail(venue: Venue) {
        val bundle = bundleOf("venue" to venue)
        navController.navigate(R.id.restaurantDetailFragment, bundle)
      }
    }
    val restaurantRecyclerAdapter = RestaurantRecyclerAdapter(venueClickListener)
    venues?.let {it ->
      if(it.isNotEmpty()){
        for (venue in it) {
          markerList.add(viewMap.addMarker(
            MarkerOptions().icon(restaurantMarker)
              .position(LatLng(venue.location.lat, venue.location.long))
              .title(venue.name)))
        }
        binding.restaurantList.show()
        binding.restaurantList.adapter = restaurantRecyclerAdapter
        restaurantRecyclerAdapter.apply { submitList(venues)}
      }
    }
  }
  private fun loadingState(loading: Boolean){
    if(loading){
      binding.findRestaurantProgress.show()
      binding.restaurantList.hide()
    }else{
      binding.findRestaurantProgress.hide()
      binding.restaurantList.show()
    }
  }


  private fun locationRequest(){
    mLocationRequest = LocationRequest().apply {
      interval = TimeUnit.SECONDS.toMillis(60)
      fastestInterval = TimeUnit.SECONDS.toMillis(30)
      maxWaitTime = TimeUnit.MINUTES.toMillis(2)
      smallestDisplacement = 500f
      priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    locationCallback = object : LocationCallback(){
      override fun onLocationResult(locationResult: LocationResult?) {
        super.onLocationResult(locationResult)
        if(locationResult?.lastLocation != null){
          getRestaurant(locationResult.lastLocation)
          currentLocation = locationResult.lastLocation
          val latLng = LatLng(locationResult.lastLocation.latitude, locationResult.lastLocation.longitude)
          val zoomLevel = 16.0f
          viewMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
          viewMap.animateCamera(CameraUpdateFactory.zoomTo(17f))
        }
      }
    }

  }

  private fun getRestaurant(location: Location) {
    val lat = location.latitude.toString()
    val long = location.longitude.toString()
    val latLng = "$lat,$long"
    //getCityName(location)
    viewModel.getRestaurants(latLng, "lagos", 250, 10)
//    viewModel.getRestaurants("40.7484,-73.9857", "new york", 250, 10)
  }

  private fun subscribeToLocationUpdates(){
    try {
      fusedLocationClient.requestLocationUpdates(
        mLocationRequest, locationCallback, Looper.myLooper())
    }catch (unlikely: SecurityException){
      Timber.e( "Lost location permissions. Couldn't remove updates. $unlikely")
    }
  }
  fun unsubscribeToLocationUpdates(){
    try {
      val removeTask = fusedLocationClient.removeLocationUpdates(locationCallback)
      removeTask.addOnCompleteListener { task ->
        if (task.isSuccessful) {
          Timber.i("Location Callback removed.")
        } else {
          Timber.i("Failed to remove Location Callback.")
        }
      }
    } catch (unlikely: SecurityException) {
      Timber.i( "Lost location permissions. Couldn't remove updates. $unlikely")
    }
  }

  override fun onLowMemory() {
    super.onLowMemory()
    mapView.onLowMemory()
  }

  override fun onDestroy() {
    super.onDestroy()
    mapView.onDestroy()
    unsubscribeToLocationUpdates()
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



}