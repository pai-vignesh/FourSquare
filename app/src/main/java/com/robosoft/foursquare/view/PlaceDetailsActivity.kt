package com.robosoft.foursquare.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.view.WindowCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.robosoft.foursquare.R
import com.robosoft.foursquare.databinding.ActivityPlaceDetailsBinding
import com.robosoft.foursquare.model.Photo
import com.robosoft.foursquare.util.Status
import com.robosoft.foursquare.viewmodel.PlaceDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlaceDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlaceDetailsBinding
    private lateinit var mapFragment: SupportMapFragment
    private lateinit var googleMap: GoogleMap
    private lateinit var currentLocation: Location
    private val placeDetailsViewModel: PlaceDetailsViewModel by viewModels()
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlaceDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        val fsqId = intent.getStringExtra("fsqId")

        binding.topAppBar.setNavigationOnClickListener {
            onBackPressed()
        }

        binding.addReview.setOnClickListener {
            val i = Intent(this, AddReview::class.java)
            startActivity(i)
        }

        binding.reviews.setOnClickListener {
            val i = Intent(this, ReviewActivity::class.java)
            startActivity(i)
        }

        binding.photos.setOnClickListener {
            val i = Intent(this,GalleryActivity::class.java)
            i.putExtra("fsqId", fsqId)
            i.putExtra("placeName",binding.topAppBar.title)
            startActivity(i)
        }
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(this)

        fsqId?.let {
            placeDetailsViewModel.getPlaceDetails(it).observe(this,{ data ->
                data?.let { resource ->
                    when (resource.status) {
                        Status.LOADING -> {

                        }
                        Status.SUCCESS -> {
                            resource.data?.let { placeData ->
                                Log.d("TAG", "onCreate: ${placeData.geocodes.main} ")
                                binding.topAppBar.title = placeData.name
                                binding.placeType.text = if(placeData.categories.isEmpty())  "Unknown Type" else placeData.categories[0].name
                                fetchLocation(placeData.geocodes.main.latitude.toDouble(),placeData.geocodes.main.longitude.toDouble())
                            }
                        }
                        Status.ERROR -> {

                        }
                    }
                }

            })
        }

    }

    private fun fetchLocation(lat : Double,lng: Double) {
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) !=
            PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION
            ) !=
            PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 101
            )
            return
        }
        val task = fusedLocationProviderClient.lastLocation
        task.addOnSuccessListener { location->
            currentLocation = location
            mapFragment = supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
            mapFragment.getMapAsync {
                googleMap = it
                val myLocation = LatLng(
                    lat,
                    lng
                )
                googleMap.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        myLocation,
                        15.5f
                    )
                )
                googleMap.addMarker(
                    MarkerOptions()
                        .position(myLocation)
                        .title("Marker in Sydney")
                )
            }
        }
    }
}