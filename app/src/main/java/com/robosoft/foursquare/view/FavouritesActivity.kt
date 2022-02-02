package com.robosoft.foursquare.view

import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.core.view.WindowCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.robosoft.foursquare.adapter.FavouritesAdapter
import com.robosoft.foursquare.databinding.ActivityFavouritesBinding
import com.robosoft.foursquare.room.FavouriteModel
import com.robosoft.foursquare.util.CellClickListener
import com.robosoft.foursquare.util.LocationPermission
import com.robosoft.foursquare.util.Status
import com.robosoft.foursquare.viewmodel.FavouritesViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

@AndroidEntryPoint
class FavouritesActivity : AppCompatActivity(), CellClickListener {
    private lateinit var binding: ActivityFavouritesBinding
    private lateinit var currentLocation: Location
    private lateinit var favouritesAdapter: FavouritesAdapter
    private val favouritesViewModel: FavouritesViewModel by viewModels()

    @Inject
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var favourites = ArrayList<FavouriteModel>()
    private var favouritesFilter = ArrayList<FavouriteModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavouritesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        binding.topAppBar.setNavigationOnClickListener {
            onBackPressed()
        }
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(this)
        if (LocationPermission.checkPermission(this)) {
            fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
                currentLocation = location
                setupRv(location)
            }
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val searchText = newText!!.lowercase(Locale.getDefault())
                performFilter(searchText)
                return true
            }
        })
    }

    private fun performFilter(searchText:String){
        favouritesFilter.clear()
        if(searchText.isNotEmpty()){
            favourites.forEach { model ->
                if(model.placeName.lowercase(Locale.getDefault()).contains(searchText)){
                    favouritesFilter.add(model)
                }
            }
            favouritesAdapter.submitList(favouritesFilter)
        }else{
            favouritesFilter.clear()
            favouritesFilter.addAll(favourites)
            favouritesAdapter.submitList(favouritesFilter)
        }
    }


    //recyclerview setup
    private fun setupRv(currentLocation: Location) {
        favouritesAdapter = FavouritesAdapter(this, currentLocation)
        favouritesViewModel.favourites.observe(this) { data ->
            data?.let { favouritesData ->
                favourites = favouritesData as ArrayList<FavouriteModel>
                favouritesFilter.addAll(favourites)
                binding.favouriteRecyclerView.apply {
                    layoutManager = LinearLayoutManager(
                        this@FavouritesActivity,
                        LinearLayoutManager.VERTICAL, false
                    )
                    favouritesAdapter.submitList(favourites)
                    adapter = favouritesAdapter
                    setHasFixedSize(true)
                }
            }
        }
    }

    override fun onCellClickListener(data: FavouriteModel, isRemove: Boolean) {
        if (isRemove) {
            favouritesViewModel.deleteFavourites(data).observe(this) { dataDeleted ->
                dataDeleted?.let { resource ->
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