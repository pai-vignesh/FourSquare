package com.robosoft.foursquare.fragment

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.robosoft.foursquare.R
import com.robosoft.foursquare.databinding.FragmentNearYouBinding
import com.google.android.gms.maps.CameraUpdateFactory

import com.google.android.gms.maps.model.LatLng
import com.robosoft.foursquare.adapter.PlaceAdapter
import com.robosoft.foursquare.model.PlaceData
import com.robosoft.foursquare.util.CellClickListener
import com.robosoft.foursquare.util.Status
import com.robosoft.foursquare.view.PlaceDetailsActivity
import com.robosoft.foursquare.view.ReviewActivity
import com.robosoft.foursquare.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

@AndroidEntryPoint
class NearYouFragment : Fragment() ,CellClickListener {
    private lateinit var binding: FragmentNearYouBinding
    private lateinit var mapFragment: SupportMapFragment
    private lateinit var googleMap: GoogleMap
    private lateinit var currentLocation: Location
    private lateinit var placeAdapter: PlaceAdapter
    private val homeViewModel: HomeViewModel by viewModels()
    private var places = ArrayList<PlaceData>()
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNearYouBinding.inflate(inflater)
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())
        fetchLocation()



        return binding.root
    }

    //recyclerview setup
    private fun setupRv(p0: String?) {
        p0?.let {
            placeAdapter = PlaceAdapter(this)
            homeViewModel.getNearbyPlaces(p0).observe(this, { data ->
                data?.let { resource ->
                    when (resource.status) {
                        Status.LOADING -> {

                        }
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
                        Status.ERROR -> {

                        }
                    }
                }

            })
        }

    }


    private fun fetchLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION
            ) !=
            PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION
            ) !=
            PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 101
            )
            return
        }
        val task = fusedLocationProviderClient.lastLocation
        task.addOnSuccessListener { location->
            currentLocation = location
            Toast.makeText(requireContext(), currentLocation.latitude.toString() + "" +
                    currentLocation.longitude, Toast.LENGTH_SHORT).show()

            mapFragment = childFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
            mapFragment.getMapAsync {
                googleMap = it
                Log.d("TAG", "fetchLocation: ${currentLocation.latitude} ${currentLocation.longitude} ")
                val myLocation = LatLng(
                    location.latitude,
                    location.longitude
                )
                setupRv("${location.latitude},${location.longitude}")
                googleMap.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        myLocation,
                        15.5f
                    )
                )
            }
        }
    }

    override fun onCellClickListener(data: PlaceData) {

    }
}