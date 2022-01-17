package com.robosoft.foursquare.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.robosoft.foursquare.adapter.PlaceAdapter
import com.robosoft.foursquare.databinding.FragmentCoffeePlaceBinding
import com.robosoft.foursquare.model.PlaceData
import com.robosoft.foursquare.util.CellClickListener
import com.robosoft.foursquare.util.LocationPermission
import com.robosoft.foursquare.util.Status
import com.robosoft.foursquare.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CoffeePlaceFragment : Fragment(), CellClickListener{
    private lateinit var binding : FragmentCoffeePlaceBinding
    private lateinit var placeAdapter: PlaceAdapter
    private val homeViewModel: HomeViewModel by viewModels()
    private var places = ArrayList<PlaceData>()
    @Inject
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentCoffeePlaceBinding.inflate(inflater)
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
            homeViewModel.getQueryPlaces("coffee",location).observe(this, { data ->
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