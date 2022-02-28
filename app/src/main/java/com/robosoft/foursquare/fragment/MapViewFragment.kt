package com.robosoft.foursquare.fragment

import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
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
import com.robosoft.foursquare.databinding.FragmentMapViewBinding
import com.robosoft.foursquare.model.PlaceData
import com.robosoft.foursquare.util.LocationPermission
import com.robosoft.foursquare.util.Resource
import com.robosoft.foursquare.view.PlaceDetailsActivity
import com.robosoft.foursquare.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.lang.IllegalStateException
import java.text.DecimalFormat
import javax.inject.Inject

@AndroidEntryPoint
class MapViewFragment : Fragment() {
    private lateinit var binding: FragmentMapViewBinding
    private lateinit var mapFragment: SupportMapFragment
    private lateinit var googleMap: GoogleMap
    private lateinit var currentLocation: Location

    @Inject
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val homeViewModel: HomeViewModel by viewModels()
    private var places = ArrayList<PlaceData>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMapViewBinding.inflate(inflater)
        binding.listView.setOnClickListener {
            try {
                findNavController().navigate(R.id.action_mapViewFragment_to_listViewFragment)
            } catch (e: IllegalStateException) {
                val transaction: FragmentTransaction = parentFragmentManager.beginTransaction()
                val mapViewFragment = ListViewFragment()
                transaction.replace(R.id.fcvSearch, mapViewFragment)
                transaction.addToBackStack(null)
                transaction.commit()
            }
        }
        binding.placeCard.layout.visibility = View.GONE
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())
        if (LocationPermission.checkPermission(requireActivity())) {
            val task = fusedLocationProviderClient.lastLocation
            task.addOnSuccessListener { location ->
                currentLocation = location
                mapFragment =
                    childFragmentManager.findFragmentById(R.id.mapSearchFragment) as SupportMapFragment
                mapFragment.getMapAsync {
                    googleMap = it
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
                    googleMap.setOnMapClickListener { pointLocation ->
                        googleMap.clear()
                        googleMap.addMarker(
                            MarkerOptions()
                                .position(pointLocation)
                                .title("Marker in Sydney")
                        )
                        updateCard("${pointLocation.latitude},${pointLocation.longitude}", location)
                    }
                }
            }
        }
        return binding.root
    }

    private fun updateCard(p0: String?, currentLocation: Location) {
        p0?.let { location ->
            homeViewModel.getQueryPlaces("", location, "DISTANCE")
                .observe(viewLifecycleOwner) { data ->
                    when (data) {
                        is Resource.Loading -> {}
                        is Resource.Success -> {
                            val placeData = (data as? Resource.Success)?.data
                            binding.placeCard.layout.visibility = View.VISIBLE
                            places = placeData?.results as ArrayList<PlaceData>
                            if (!places[0].photos.isNullOrEmpty()) {
                                val requestOptions = RequestOptions().diskCacheStrategy(
                                    DiskCacheStrategy.ALL
                                )
                                val imgSize = "400x400"
                                val imageUrl =
                                    places[0].photos[0].prefix + imgSize + places[0].photos[0].suffix
                                Glide.with(this).load(imageUrl).apply(requestOptions)
                                    .into(binding.placeCard.imageView)
                            }
                            binding.placeCard.layout.setOnClickListener { v ->
                                val intent =
                                    Intent(
                                        requireActivity(),
                                        PlaceDetailsActivity::class.java
                                    )
                                intent.putExtra("fsqId", places[0].fsqId)
                                v.context.startActivity(intent)
                            }
                            binding.placeCard.placeName.text = places[0].name
                            binding.placeCard.placeAddress.text = places[0].location.address
                            if (places[0].categories.isNotEmpty()) {
                                binding.placeCard.placeType.text =
                                    places[0].categories[0].name
                            }
                            places[0].price?.let { price ->
                                when (price) {
                                    1 -> binding.placeCard.price.text =
                                        getString(R.string.expense, "₹")
                                    2 -> binding.placeCard.price.text =
                                        getString(R.string.expense, "₹₹")
                                    3 -> binding.placeCard.price.text =
                                        getString(R.string.expense, "₹₹₹")
                                    4 -> binding.placeCard.price.text =
                                        getString(R.string.expense, "₹₹₹₹")
                                    5 -> binding.placeCard.price.text =
                                        getString(R.string.expense, "₹₹₹₹₹")
                                    else -> binding.placeCard.price.text =
                                        getString(R.string.expense, "")
                                }
                            }
                            val destLocation = Location("destination")
                            destLocation.latitude =
                                places[0].geocodes.main.latitude.toDouble()
                            destLocation.longitude =
                                places[0].geocodes.main.longitude.toDouble()
                            val km = DecimalFormat("##.##").format(
                                currentLocation.distanceTo(destLocation) / 1000
                            )
                            binding.placeCard.distance.text =
                                getString(R.string.card_distance_text, km)
                        }
                        is Resource.Error -> {}
                    }
                }
        }
    }
}