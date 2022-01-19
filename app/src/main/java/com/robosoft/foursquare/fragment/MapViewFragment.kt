package com.robosoft.foursquare.fragment

import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.robosoft.foursquare.R
import com.robosoft.foursquare.databinding.FragmentMapViewBinding
import com.robosoft.foursquare.util.LocationPermission
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MapViewFragment : Fragment() {

    private lateinit var binding: FragmentMapViewBinding
    private lateinit var mapFragment: SupportMapFragment
    private lateinit var googleMap: GoogleMap
    private lateinit var currentLocation: Location
    @Inject
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentMapViewBinding.inflate(inflater)

        binding.listView.setOnClickListener{
            findNavController().navigate(R.id.action_mapViewFragment_to_listViewFragment)
        }
        
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())
        if (LocationPermission.checkPermission(requireActivity())) {
            fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
                //setupRv("${location.latitude},${location.longitude}")
            }
            val task = fusedLocationProviderClient.lastLocation
            task.addOnSuccessListener { location ->
                currentLocation = location
                mapFragment =
                    childFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
                mapFragment.getMapAsync {
                    googleMap = it
                    val myLocation = LatLng(
                        location.latitude,
                        location.longitude
                    )
                    //setupRv("${location.latitude},${location.longitude}")
                    googleMap.animateCamera(
                        CameraUpdateFactory.newLatLngZoom(
                            myLocation,
                            15.5f
                        )
                    )
                    googleMap.isMyLocationEnabled = true
                }
            }
        }
        return binding.root
    }


}