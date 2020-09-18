package com.kolaemiola.nearbyrestaurant.ui.maps

import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.observe
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearSnapHelper
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
import com.kolaemiola.nearbyrestaurant.model.Venue
import com.kolaemiola.nearbyrestaurant.ui.MainViewModel
import com.kolaemiola.nearbyrestaurant.ui.adapter.RestaurantRecyclerAdapter
import com.kolaemiola.nearbyrestaurant.ui.adapter.VenueClickListener
import com.kolaemiola.nearbyrestaurant.ui.bitmapDescriptorFromVector
import com.kolaemiola.nearbyrestaurant.snap.SnapOnScrollListener
import com.kolaemiola.nearbyrestaurant.util.Constant.Companion.RESTAURANT
import com.kolaemiola.nearbyrestaurant.util.NetworkUtils
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.lang.Exception
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class MapsFragment : Fragment() {
  private lateinit var viewMap: GoogleMap
  private lateinit var mapView: MapView
  private val MAP_BUNDLE_KEY = "MAP_BUNDLE_KEY_2"
  private lateinit var mLocationRequest: LocationRequest
  private lateinit var binding: FragmentMapsBinding
  private lateinit var fusedLocationClient: FusedLocationProviderClient
  private val markerList = ArrayList<Marker>()
  private var previousMarker: Marker? = null
  private var currentMarker: Marker? = null
  private val viewModel: MainViewModel by viewModels()
  private lateinit var navController: NavController
  private lateinit var restaurantMarker: BitmapDescriptor
  private lateinit var selectedMarker: BitmapDescriptor
  private val snapHelper = LinearSnapHelper()

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
    viewModel.getRecentChecked()
    restaurantMarker = bitmapDescriptorFromVector(requireContext(), R.drawable.ic_food_tray)!!
    selectedMarker = bitmapDescriptorFromVector(requireContext(), R.drawable.ic_food_tray_selected)!!
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    navController = Navigation.findNavController(view)
  }
  private fun updateUI(){
    viewModel.searchRestaurantState.observe(viewLifecycleOwner) { state ->
      state.venues?.let { venues ->
          viewModel.clearCache()
          getRestaurantState(venues)
          viewModel.updateRecentCheck(venues.take(10))

      }
      state.venuesInitial?.let { venues ->
        getRestaurantState(venues)
      }
      loadingState(state.loading!!)
//      errorState(state.error!!)
    }

  }

  private fun resetMap() {
    viewMap.clear()
    markerList.clear()
  }


  private val callback = OnMapReadyCallback { googleMap ->
    viewMap = googleMap
    viewMap.setOnMarkerClickListener {clickedMarker ->
      viewModel.selectPlaceOnRestaurantList(markerList.indexOf(clickedMarker))
      true
    }
    locationRequest()
    if(foregroundPermissionApproved()){
      try{
        googleMap.isMyLocationEnabled = true
      }catch (unlikely: SecurityException){
        Timber.e( "Lost location permissions. Couldn't remove updates. $unlikely")
      }

    }
  }


  private fun getRestaurantState(venues : List<Venue>){
    val venueClickListener = object : VenueClickListener {
      override fun showVenueDetail(venue: Venue) {
        val bundle = bundleOf(RESTAURANT to venue)
        navController.navigate(R.id.restaurantDetailFragment, bundle)
      }
    }
    val restaurantRecyclerAdapter = RestaurantRecyclerAdapter(venueClickListener)
      if(venues.isNotEmpty()){
        try {
          for (venue in venues) {
            markerList.add(viewMap.addMarker(
              MarkerOptions().icon(restaurantMarker)
                .position(LatLng(venue.location.lat, venue.location.long))
                .title(venue.name)))
          }
          binding.restaurantList.show()
          binding.restaurantList.adapter = restaurantRecyclerAdapter

          restaurantRecyclerAdapter.apply { submitList(venues)}

          currentMarker = markerList[0]
          moveToMarker(0)

          setRecyclerSnap()
        } catch (e: Exception) {
          e.printStackTrace()
        }
      }else{
        resetMap()
      }

    viewModel.currentMarkerPosition.observe(viewLifecycleOwner){
      moveToMarker(it)
    }
  }

  private fun handleNetworkChange(){
    onNetworkChange{ isConnceted ->

    }
  }

  private fun loadmore(){

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
    viewModel.usersLocation.observe(viewLifecycleOwner){ location ->
      val latLng = LatLng(location.latitude, location.longitude)
      viewMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
      viewMap.animateCamera(CameraUpdateFactory.zoomTo(18f))
      getRestaurant(location)
    }
  }

  private fun getRestaurant(location: Location) {
    val lat = location.latitude.toString()
    val long = location.longitude.toString()
    val latLng = "$lat,$long"
    viewModel.getRestaurants(latLng,250,20)
  }

  private fun moveToMarker(position: Int) {
    previousMarker = currentMarker
    currentMarker = markerList[position]
    previousMarker?.setIcon(restaurantMarker)
    currentMarker?.setIcon(selectedMarker)
    currentMarker?.showInfoWindow()
    viewMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentMarker?.position, 18f))
  }

  private fun subscribeToLocationUpdates(){
    try {
      mLocationRequest = LocationRequest().apply {
        interval = TimeUnit.SECONDS.toMillis(60)
        fastestInterval = TimeUnit.SECONDS.toMillis(30)
        maxWaitTime = TimeUnit.MINUTES.toMillis(2)
        smallestDisplacement = 500f
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
      }
     viewModel.locationRequest(mLocationRequest)
    }catch (unlikely: SecurityException){
      Timber.e( "Lost location permissions. Couldn't remove updates. $unlikely")
    }
  }

  private fun setRecyclerSnap(){
    snapHelper.attachToRecyclerView(binding.restaurantList)
    val snapOnScrollListener = SnapOnScrollListener(
      snapHelper,
      SnapOnScrollListener.NotifyBehavior.NOTIFY_ON_STATE_IDLE
    ) { snapPosition ->
      snapPosition?.let { viewModel.setCurrentMarkerPosition(snapPosition)
      }
    }
    binding.restaurantList.addOnScrollListener(snapOnScrollListener)
    viewModel.currentPlaceIndex.observe(viewLifecycleOwner){ index ->
      binding.restaurantList.smoothScrollToPosition(index)
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
  fun onNetworkChange(block: (Boolean) -> Unit) {
    NetworkUtils.getNetworkStatus(requireContext())
      .observe(this, Observer { isConnected ->
        block(isConnected)
      })
  }
}