package com.robosoft.foursquare.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.view.WindowCompat
import com.bumptech.glide.Glide
import com.robosoft.foursquare.R
import com.robosoft.foursquare.databinding.ActivityImageDetailsBinding
import java.text.SimpleDateFormat
import java.util.*

class ImageDetails : AppCompatActivity() {
    private lateinit var binding: ActivityImageDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImageDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        binding.topAppBar.setNavigationOnClickListener {
            onBackPressed()
        }
        val imageUrl = intent.getStringExtra("imageUrl")
        Glide.with(this).load(imageUrl).into(binding.img)
        val placeName = intent.getStringExtra("placeName")
        binding.topAppBar.title = placeName
        val dt = intent.getStringExtra("date")
        dt?.let {
            convertDate(it)
        }

        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.share -> {
                    val i = Intent(Intent.ACTION_SEND)
                    i.type = "text/plain"
                    i.putExtra(Intent.EXTRA_SUBJECT, "Sharing URL")
                    i.putExtra(Intent.EXTRA_TEXT, imageUrl)
                    startActivity(Intent.createChooser(i, "Share URL"))
                    true
                }
                else -> false
            }
        }
    }

    private fun convertDate(dateString: String){
        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH)
        val sdf = SimpleDateFormat("MMM dd,yyyy", Locale.US)
        val date: Date? = format.parse(dateString)
        date?.let {
            val res = sdf.format(it)
            Log.d("TAG", "convertDate: $res")
            binding.review.text = "Added "+ res
        }
    }
}