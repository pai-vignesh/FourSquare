package com.robosoft.foursquare.fragment

import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.robosoft.foursquare.R
import com.robosoft.foursquare.databinding.FragmentNearYouBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.robosoft.foursquare.adapter.PlaceAdapter
import com.robosoft.foursquare.model.PlaceData
import com.robosoft.foursquare.util.CellClickListener
import com.robosoft.foursquare.util.Status
import com.robosoft.foursquare.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import com.google.android.gms.maps.model.MarkerOptions
import com.robosoft.foursquare.room.FavouriteModel
import com.robosoft.foursquare.util.LocationPermission
import javax.inject.Inject
import com.google.android.gms.maps.model.MapStyleOptions

@AndroidEntryPoint
class NearYouFragment : Fragment(), CellClickListener {
    private lateinit var binding: FragmentNearYouBinding
    private lateinit var mapFragment: SupportMapFragment
    private lateinit var googleMap: GoogleMap
    private lateinit var currentLocation: Location
    private lateinit var placeAdapter: PlaceAdapter
    private val homeViewModel: HomeViewModel by viewModels()
    private var places = ArrayList<PlaceData>()

    @Inject
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNearYouBinding.inflate(inflater)
        if (LocationPermission.checkPermission(requireActivity())) {
            fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
                currentLocation = location
                mapFragment =
                    childFragmentManager.findFragmentById(R.id.mapNearFragment) as SupportMapFragment
                mapFragment.getMapAsync {
                    googleMap = it
                    googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(requireContext(),R.raw.style_json))
                    val myLocation = LatLng(
                        location.latitude,
                        location.longitude
                    )
                    setupRv("${location.latitude},${location.longitude}", location)
                    googleMap.animateCamera(
                        CameraUpdateFactory.newLatLngZoom(
                            myLocation,
                            15.5f
                        )
                    )
                    val marker = MarkerOptions()
                        .position(myLocation)
                        .title("My Marker")
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker))
                    googleMap.addMarker(marker)
                }
            }
        }
        return binding.root
    }

    override fun onResume() {
        if (LocationPermission.checkPermission(requireActivity())) {
            fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
                setupRv("${location.latitude},${location.longitude}", location)
            }
        }
        super.onResume()
    }

    private fun setupRv(p0: String?, currentLocation: Location) {
        p0?.let { location ->
            placeAdapter = PlaceAdapter(this, currentLocation)
            homeViewModel.getQueryPlaces("", location,"DISTANCE").observe(viewLifecycleOwner) { data ->
                data?.let { resource ->
                    when (resource.status) {
                        Status.LOADING -> {}
                        Status.SUCCESS -> {
                            resource.data?.let { placeData ->
                                places = placeData.results as ArrayList<PlaceData>
                                binding.nearRecyclerView.apply {
                                    layoutManager = LinearLayoutManager(
                                        activity,
                                        LinearLayoutManager.VERTICAL, false
                                    )
                                    placeAdapter.placeData = places
                                    adapter = placeAdapter
                                    setHasFixedSize(true)
                                }
                            }
                        }
                        Status.ERROR -> {}
                    }
                }
            }
        }
    }

    override fun onCellClickListener(data: FavouriteModel, isRemove: Boolean) {
        if (isRemove) {
            homeViewModel.deleteFavourites(data).observe(this) { dataDeleted ->
                dataDeleted?.let { resource ->
                    when (resource.status) {
                        Status.LOADING -> {}
                        Status.SUCCESS -> {}
                        Status.ERROR -> {}
                    }
                }
            }
        } else {
            homeViewModel.insertFavourites(data).observe(this) { dataInserted ->
                dataInserted?.let { resource ->
                    when (resource.status) {
                        Status.LOADING -> {}
                        Status.SUCCESS -> {}
                        Status.ERROR -> {}
                    }
                }
            }
        }
    }
}