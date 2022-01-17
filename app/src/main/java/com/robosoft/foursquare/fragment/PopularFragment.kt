package com.robosoft.foursquare.fragment

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.robosoft.foursquare.R
import com.robosoft.foursquare.adapter.PlaceAdapter
import com.robosoft.foursquare.databinding.FragmentPopularPlaceBinding
import com.robosoft.foursquare.model.PlaceData
import com.robosoft.foursquare.util.CellClickListener
import com.robosoft.foursquare.util.LocationPermission
import com.robosoft.foursquare.util.Status
import com.robosoft.foursquare.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PopularFragment : Fragment(), CellClickListener {
    private lateinit var binding : FragmentPopularPlaceBinding
    private lateinit var placeAdapter: PlaceAdapter
    private val homeViewModel: HomeViewModel by viewModels()
    private var places = ArrayList<PlaceData>()
    @Inject lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentPopularPlaceBinding.inflate(inflater)
        if(LocationPermission.checkPermission(requireActivity())){
            fusedLocationProviderClient.lastLocation.addOnSuccessListener { location->
                setupRv("${location.latitude},${location.longitude}")
            }
        }
        return binding.root
    }

    //recyclerview setup
    private fun setupRv(p0: String?) {
        p0?.let { location ->
            placeAdapter = PlaceAdapter(this)
            homeViewModel.getQueryPlaces("popular",location).observe(this, { data ->
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

    override fun onCellClickListener(data: PlaceData) {

    }
}