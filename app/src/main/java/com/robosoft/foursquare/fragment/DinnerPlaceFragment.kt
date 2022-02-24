package com.robosoft.foursquare.fragment

import android.content.res.Configuration
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.robosoft.foursquare.adapter.PlaceAdapter
import com.robosoft.foursquare.databinding.FragmentDinnerPlaceBinding
import com.robosoft.foursquare.model.PlaceData
import com.robosoft.foursquare.room.FavouriteModel
import com.robosoft.foursquare.util.CellClickListener
import com.robosoft.foursquare.util.LocationPermission
import com.robosoft.foursquare.util.Status
import com.robosoft.foursquare.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DinnerPlaceFragment : Fragment(), CellClickListener {
    private lateinit var binding: FragmentDinnerPlaceBinding
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
        binding = FragmentDinnerPlaceBinding.inflate(inflater)
        if (LocationPermission.checkPermission(requireActivity())) {
            fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
                setupRv("${location.latitude},${location.longitude}", location)
            }
        }
        return binding.root
    }

    private fun setupRv(p0: String?, currentLocation: Location) {
        p0?.let { location ->
            placeAdapter = PlaceAdapter(this, currentLocation)
            homeViewModel.getQueryPlaces("restaurants", location).observe(viewLifecycleOwner) { data ->
                data?.let { resource ->
                    when (resource.status) {
                        Status.LOADING -> {}
                        Status.SUCCESS -> {
                            resource.data?.let { placeData ->
                                places = placeData.results as ArrayList<PlaceData>
                                binding.nearRecyclerView.apply {
                                    layoutManager =
                                        if (requireActivity().resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                                            LinearLayoutManager(
                                                activity,
                                                LinearLayoutManager.VERTICAL, false
                                            )
                                        } else {
                                            GridLayoutManager(
                                                activity,
                                                2,
                                                LinearLayoutManager.VERTICAL,
                                                false
                                            )
                                        }
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