package com.robosoft.foursquare.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.WindowCompat
import com.robosoft.foursquare.databinding.ActivityAddReviewBinding
import android.net.Uri
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import com.robosoft.foursquare.util.CustomDialogClass

class AddReview : AppCompatActivity() {
    private lateinit var binding: ActivityAddReviewBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddReviewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.topAppBar)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        binding.pickImage.setOnClickListener {
            selectImageFromGallery()
        }

        binding.topAppBar.setNavigationOnClickListener {
            onBackPressed()
        }


        val cdd = CustomDialogClass(this)

        binding.submitBtn.setOnClickListener {
            cdd.show()
        }
    }

    private val selectImageFromGalleryResult = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let { binding.pickImage.setImageURI(uri) }
    }


    private fun selectImageFromGallery() = selectImageFromGalleryResult.launch("image/*")



}