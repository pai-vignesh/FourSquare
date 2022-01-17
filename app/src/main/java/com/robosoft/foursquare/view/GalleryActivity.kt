package com.robosoft.foursquare.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.core.view.WindowCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.robosoft.foursquare.adapter.PhotosAdapter
import com.robosoft.foursquare.adapter.PlaceAdapter
import com.robosoft.foursquare.databinding.ActivityGalleryBinding
import com.robosoft.foursquare.model.Photo
import com.robosoft.foursquare.model.PlaceData
import com.robosoft.foursquare.util.CellClickListener
import com.robosoft.foursquare.util.Status
import com.robosoft.foursquare.viewmodel.GalleryViewModel
import com.robosoft.foursquare.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GalleryActivity : AppCompatActivity() , CellClickListener{
    private lateinit var binding: ActivityGalleryBinding
    private lateinit var photosAdapter: PhotosAdapter
    private val galleryViewModel: GalleryViewModel by viewModels()
    private var photos = ArrayList<Photo>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGalleryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        val fsqId = intent.getStringExtra("fsqId")
        val placeName = intent.getStringExtra("placeName")
        binding.topAppBar.title = placeName
        setupRv(fsqId)
    }

    private fun setupRv(p0: String?) {
        p0?.let {
            photosAdapter = PhotosAdapter(this)
            galleryViewModel.getPhotos(it).observe(this, { data ->
                data?.let { resource ->
                    when (resource.status) {
                        Status.LOADING -> {

                        }
                        Status.SUCCESS -> {
                            Log.d("TAG", "setupRv: ${resource.data?.size} ")
                            resource.data?.let { photoData ->
                                photos = photoData as ArrayList<Photo>
                                binding.nearRecyclerView.apply {
                                    layoutManager = GridLayoutManager(this@GalleryActivity,3,LinearLayoutManager.VERTICAL,false)
                                    photosAdapter.photos = photos
                                    adapter = photosAdapter
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