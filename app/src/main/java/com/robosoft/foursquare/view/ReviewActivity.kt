package com.robosoft.foursquare.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.core.view.WindowCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.robosoft.foursquare.R
import com.robosoft.foursquare.adapter.ReviewAdapter
import com.robosoft.foursquare.databinding.ActivityReviewBinding
import com.robosoft.foursquare.model.Tip
import com.robosoft.foursquare.util.Status
import com.robosoft.foursquare.viewmodel.ReviewViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReviewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityReviewBinding
    private lateinit var reviewAdapter: ReviewAdapter
    private val reviewViewModel: ReviewViewModel by viewModels()
    private var tips = ArrayList<Tip>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReviewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        val fsqId = intent.getStringExtra("fsqId")
        val placeName = intent.getStringExtra("placeName")
        setupRv(fsqId)
        binding.topAppBar.title = placeName
        binding.topAppBar.setNavigationOnClickListener {
            onBackPressed()
        }
        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.share -> {
                    val i = Intent(this, AddReview::class.java)
                    startActivity(i)
                    true
                }
                else -> false
            }
        }
    }

    private fun setupRv(p0: String?) {
        p0?.let {
            reviewAdapter = ReviewAdapter()
            reviewViewModel.getPlaceReviews(it).observe(this, { data ->
                data?.let { resource ->
                    when (resource.status) {
                        Status.LOADING -> {}
                        Status.SUCCESS -> {
                            Log.d("TAG", "setupRv: ${resource.data?.size} ")
                            resource.data?.let { tipData ->
                                tips = tipData as ArrayList<Tip>
                                binding.reviewRecyclerview.apply {
                                    layoutManager = LinearLayoutManager(
                                        this@ReviewActivity,
                                        LinearLayoutManager.VERTICAL, false
                                    )
                                    reviewAdapter.tips = tips
                                    adapter = reviewAdapter
                                    setHasFixedSize(true)
                                }
                            }
                        }
                        Status.ERROR -> {}
                    }
                }
            })
        }
    }
}