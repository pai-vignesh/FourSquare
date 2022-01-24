package com.robosoft.foursquare.view

import android.content.Intent
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.core.view.WindowCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.robosoft.foursquare.R
import com.robosoft.foursquare.databinding.ActivityPlaceDetailsBinding
import com.robosoft.foursquare.util.LocationPermission
import com.robosoft.foursquare.util.Status
import com.robosoft.foursquare.viewmodel.PlaceDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.DecimalFormat

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
//        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
//            when (menuItem.itemId) {
//                R.id.share -> {
//                    val i = Intent(this, SearchActivity::class.java)
//                    startActivity(i)
//                    true
//                }
//                R.id.favorite -> {
//                    true
//                }
//                else -> false
//            }
//        }

        binding.reviews.setOnClickListener {
            val i = Intent(this, ReviewActivity::class.java)
            i.putExtra("fsqId", fsqId)
            i.putExtra("placeName", binding.topAppBar.title)
            startActivity(i)
        }
        binding.photos.setOnClickListener {
            val i = Intent(this, GalleryActivity::class.java)
            i.putExtra("fsqId", fsqId)
            i.putExtra("placeName", binding.topAppBar.title)
            startActivity(i)
        }
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(this)

        fsqId?.let {
            placeDetailsViewModel.getPlaceDetails(it).observe(this, { data ->
                data?.let { resource ->
                    when (resource.status) {
                        Status.LOADING -> {}
                        Status.SUCCESS -> {
                            resource.data?.let { placeData ->
                                if (!placeData.photos.isNullOrEmpty()) {
                                    Log.d("TAG", "onCreate: ${placeData.photos} ")
                                    val requestOptions = RequestOptions().diskCacheStrategy(
                                        DiskCacheStrategy.ALL
                                    )
                                    val imgSize = "400x400"
                                    val imageUrl =
                                        placeData.photos[0].prefix + imgSize + placeData.photos[0].suffix
                                    Log.d("TAG", "onCreate: $imageUrl ")
                                    Glide.with(this).load(imageUrl).apply(requestOptions)
                                        .into(binding.placeImg)
                                }
                                binding.topAppBar.title = placeData.name
                                binding.tvAddr.text = placeData.location.address
                                binding.tvLocality.text = placeData.location.locality
                                placeData.rating?.let { stars ->
                                    binding.ratingBar.rating = ((stars * 5) / 10).toFloat()
                                }
                                placeData.tel?.let { tel ->
                                    binding.tvphone.text = tel
                                }
                                if (placeData.categories.isEmpty()) {
                                    binding.placeType.text = "unknown type"
                                } else {
                                    binding.placeType.text = placeData.categories[0].name
                                }
                                fetchLocation(
                                    placeData.geocodes.main.latitude.toDouble(),
                                    placeData.geocodes.main.longitude.toDouble()
                                )
                            }
                        }
                        Status.ERROR -> {}
                    }
                }
            })
        }
    }

    private fun fetchLocation(lat: Double, lng: Double) {
        if (LocationPermission.checkPermission(this)) {
            val task = fusedLocationProviderClient.lastLocation
            task.addOnSuccessListener { location ->
                currentLocation = location
                mapFragment =
                    supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
                mapFragment.getMapAsync {
                    googleMap = it
                    val destLocation = Location("destination")
                    val myLocation = LatLng(
                        lat,
                        lng
                    )
                    destLocation.latitude = lat
                    destLocation.longitude = lng
                    val km = DecimalFormat("##.##").format(location.distanceTo(destLocation) / 1000)
                    binding.tvDistance.text = getString(R.string.distance_text, km)
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
}