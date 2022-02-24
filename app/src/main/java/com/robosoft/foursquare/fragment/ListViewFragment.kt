package com.robosoft.foursquare.fragment


import android.content.res.Configuration
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.robosoft.foursquare.R
import com.robosoft.foursquare.adapter.PlaceAdapter
import com.robosoft.foursquare.databinding.FragmentListViewBinding
import com.robosoft.foursquare.model.PlaceData
import com.robosoft.foursquare.room.FavouriteModel
import com.robosoft.foursquare.util.CellClickListener
import com.robosoft.foursquare.util.LocationPermission
import com.robosoft.foursquare.util.Status
import com.robosoft.foursquare.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ListViewFragment : Fragment(), CellClickListener {
    private lateinit var binding: FragmentListViewBinding
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
        binding = FragmentListViewBinding.inflate(inflater)
        val query = arguments?.getString("query")
        val near = arguments?.getString("near")
        binding.mapView.setOnClickListener {
            query?.let {
                val transaction: FragmentTransaction = parentFragmentManager.beginTransaction()
                val mapViewFragment = MapViewFragment()
                transaction.replace(R.id.fcvSearch, mapViewFragment)
                transaction.addToBackStack(null)
                transaction.commit()
            } ?: run{
                findNavController().navigate(R.id.action_listViewFragment_to_mapViewFragment)
            }
        }
        if (LocationPermission.checkPermission(requireActivity())) {
            fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
                query?.let { q->
                    near?.let { n->
                        setupSearchRv(q, location,n)
                    } ?: run{
                        setupSearchRv(q, location,"")
                    }
                } ?: run{
                    near?.let { n->
                        query?.let { q->
                            setupSearchRv(q, location,n)
                        } ?: run{
                            setupSearchRv("", location,n)
                        }
                    } ?: run{
                        setupRv("${location.latitude},${location.longitude}", location)
                    }
                }
            }
        }
        return binding.root
    }

    private fun setupSearchRv(p0: String?, currentLocation: Location,near: String) {
        p0?.let { location ->
            placeAdapter = PlaceAdapter(this, currentLocation)
            homeViewModel.getSearchPlaces(location, near).observe(viewLifecycleOwner) { data ->
                data?.let { resource ->
                    when (resource.status) {
                        Status.LOADING -> {}
                        Status.SUCCESS -> {
                            resource.data?.let { placeData ->
                                places = placeData.results as ArrayList<PlaceData>
                                binding.searchRecyclerview.apply {
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

    //recyclerview setup
    private fun setupRv(p0: String?, currentLocation: Location,query:String="") {
        p0?.let { location ->
            placeAdapter = PlaceAdapter(this, currentLocation)
            homeViewModel.getQueryPlaces(query!!, location).observe(viewLifecycleOwner) { data ->
                data?.let { resource ->
                    when (resource.status) {
                        Status.LOADING -> {}
                        Status.SUCCESS -> {
                            resource.data?.let { placeData ->
                                places = placeData.results as ArrayList<PlaceData>
                                binding.searchRecyclerview.apply {
                                    layoutManager =
                                        if (activity!!.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
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