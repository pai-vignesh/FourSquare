package com.robosoft.foursquare.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.WindowCompat
import com.robosoft.foursquare.databinding.ActivityAddReviewBinding
import androidx.core.app.ActivityCompat.startActivityForResult

import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import com.robosoft.foursquare.BuildConfig
import java.io.File


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

    }

    private val selectImageFromGalleryResult = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let { binding.pickImage.setImageURI(uri) }
    }


    private fun selectImageFromGallery() = selectImageFromGalleryResult.launch("image/*")



}