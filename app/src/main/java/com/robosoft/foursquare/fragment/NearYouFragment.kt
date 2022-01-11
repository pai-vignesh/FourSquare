package com.robosoft.foursquare.fragment

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.robosoft.foursquare.R
import com.robosoft.foursquare.databinding.FragmentNearYouBinding
import com.google.android.gms.maps.CameraUpdateFactory

import com.google.android.gms.maps.model.LatLng
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

class NearYouFragment : Fragment() {
    private lateinit var binding: FragmentNearYouBinding
    private lateinit var mapFragment: SupportMapFragment
    private lateinit var googleMap: GoogleMap
    private lateinit var currentLocation: Location
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

//    private fun makeApiCall(location:Location){
//        val request = Request.Builder().url("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=${location.latitude},${location.longitude}&radius=1500&type=restaurant&key=AIzaSyB2gUXijgN5MDWN_Wf6Dby55XYPSkgjLmQ")
//            .build()
//
//        val response = OkHttpClient().newCall(request).execute().body?.string()
//        val jsonObject = JSONObject(response!!) // This will make the json below as an object for you
//
//        Log.d("TAG", "makeApiCall: $jsonObject ")
//        // You can access all the attributes , nested ones using JSONArray and JSONObject here
//    }

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
                googleMap.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        myLocation,
                        15.5f
                    )
                )
            }
        }
    }
}