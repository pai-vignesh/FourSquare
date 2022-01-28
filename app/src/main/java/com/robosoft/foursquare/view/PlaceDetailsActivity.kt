package com.robosoft.foursquare.view

import android.content.Intent
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
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
import com.robosoft.foursquare.model.PlaceData
import com.robosoft.foursquare.preferences.Preferences
import com.robosoft.foursquare.room.FavouriteModel
import com.robosoft.foursquare.util.CustomDialogClass
import com.robosoft.foursquare.util.LocationPermission
import com.robosoft.foursquare.util.Status
import com.robosoft.foursquare.viewmodel.HomeViewModel
import com.robosoft.foursquare.viewmodel.PlaceDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.DecimalFormat
import javax.inject.Inject

@AndroidEntryPoint
class PlaceDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlaceDetailsBinding
    private lateinit var mapFragment: SupportMapFragment
    private lateinit var googleMap: GoogleMap
    private lateinit var currentLocation: Location
    private val placeDetailsViewModel: PlaceDetailsViewModel by viewModels()
    private val homeViewModel : HomeViewModel by viewModels()

    @Inject
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var fsqId : String? = null
    lateinit var favouriteModel : FavouriteModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlaceDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.topAppBar)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        fsqId = intent.getStringExtra("fsqId")
        binding.apply {
            topAppBar.setNavigationOnClickListener {
                onBackPressed()
            }
            addReview.setOnClickListener {
                val i = Intent(this@PlaceDetailsActivity, AddReview::class.java)
                startActivity(i)
            }
            reviews.setOnClickListener {
                val i = Intent(this@PlaceDetailsActivity, ReviewActivity::class.java)
                i.putExtra("fsqId", fsqId)
                i.putExtra("placeName",topAppBar.title)
                startActivity(i)
            }
            photos.setOnClickListener {
                val i = Intent(this@PlaceDetailsActivity, GalleryActivity::class.java)
                i.putExtra("fsqId", fsqId)
                i.putExtra("placeName", topAppBar.title)
                startActivity(i)
            }
            ratingTv?.setOnClickListener {
                val cdd = CustomDialogClass(this@PlaceDetailsActivity)
                cdd.show()
            }
        }
        fsqId?.let {
            placeDetailsViewModel.getPlaceDetails(it).observe(this, { data ->
                data?.let { resource ->
                    when (resource.status) {
                        Status.LOADING -> {}
                        Status.SUCCESS -> {
                            resource.data?.let { placeData ->
                                updateView(placeData)
                            }
                        }
                        Status.ERROR -> {}
                    }
                }
            })
        }
    }


    private fun updateView(placeData: PlaceData){
        fetchLocation(
            placeData.geocodes.main.latitude.toDouble(),
            placeData.geocodes.main.longitude.toDouble()
        )
        var imageUrl = ""
        if (!placeData.photos.isNullOrEmpty()) {
            val requestOptions = RequestOptions().diskCacheStrategy(
                DiskCacheStrategy.ALL
            )
            val imgSize = "400x400"
            imageUrl =
                placeData.photos[0].prefix + imgSize + placeData.photos[0].suffix
            Glide.with(this).load(imageUrl).apply(requestOptions)
                .into(binding.placeImg)
        }
        binding.apply {
            topAppBar.title = placeData.name
            tvAddr.text = placeData.location.address
            tvLocality.text = placeData.location.locality
            placeData.rating?.let { stars ->
                ratingBar.rating = ((stars * 5) / 10).toFloat()
            }
            placeData.tel?.let { tel ->
                tvphone.text = tel
            }
            if (placeData.categories.isEmpty()) {
                placeType.text = "unknown type"
            } else {
                placeType.text = placeData.categories[0].name
            }
            var priceVal = "• ₹"
            placeData.price?.let { price ->
                priceVal = when (price) {
                    1 -> "• ₹"
                    2 -> "• ₹₹"
                    3 -> "• ₹₹₹"
                    4 -> "• ₹₹₹₹"
                    5 -> "• ₹₹₹₹₹"
                    else -> ""
                }
            }
            favouriteModel = FavouriteModel(
                placeData.fsqId,
                placeData.name,
                placeType.text.toString(),
                priceVal,
                placeData.geocodes.main.latitude,
                placeData.geocodes.main.longitude,
                ratingBar.rating.toString(),
                imageUrl,
                placeData.location.address
            )
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.place_details_bar, menu)
        val fsqList = Preferences.getArrayPrefs("PlaceList", this)
        if (fsqList.contains(fsqId)) {
            menu.getItem(1).icon = ContextCompat.getDrawable(this, R.drawable.fav_selected)
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.share ->{
                val i = Intent(Intent.ACTION_SEND)
                i.type = "text/plain"
                i.putExtra(Intent.EXTRA_SUBJECT, "Sharing URL")
                i.putExtra(Intent.EXTRA_TEXT, "Thank you for using app")
                startActivity(Intent.createChooser(i, "Share URL"))
            }
            R.id.favorite ->{
                val fsqList = Preferences.getArrayPrefs("PlaceList", this)
                if (fsqList.contains(fsqId)) {
                    item.icon = ContextCompat.getDrawable(this, R.drawable.favourite)
                    fsqList.remove(fsqId)
                    Preferences.setArrayPrefs("PlaceList",fsqList,this)
                    homeViewModel.deleteFavourites(favouriteModel).observe(this, { dataInserted ->
                        dataInserted?.let { resource ->
                            when (resource.status) {
                                Status.LOADING -> {}
                                Status.SUCCESS -> {}
                                Status.ERROR -> {}
                            }
                        }
                    })
                }else{
                    fsqList.add(fsqId)
                    Preferences.setArrayPrefs("PlaceList",fsqList,this)
                    item.icon = ContextCompat.getDrawable(this, R.drawable.fav_selected)
                    homeViewModel.insertFavourites(favouriteModel).observe(this, { dataInserted ->
                        dataInserted?.let { resource ->
                            when (resource.status) {
                                Status.LOADING -> {}
                                Status.SUCCESS -> {}
                                Status.ERROR -> {}
                            }
                        }
                    })
                }
            }
        }
        return super.onOptionsItemSelected(item)
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