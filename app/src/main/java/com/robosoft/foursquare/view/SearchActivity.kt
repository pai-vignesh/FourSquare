package com.robosoft.foursquare.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.WindowCompat
import com.robosoft.foursquare.databinding.ActivitySearchBinding
import com.robosoft.foursquare.R
import androidx.fragment.app.FragmentTransaction
import com.robosoft.foursquare.fragment.FilterOptionsFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        binding.backButton.setOnClickListener {
            onBackPressed()
        }


        binding.filterMenu.setOnClickListener {
            val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
            val filterOption = FilterOptionsFragment()
            transaction.replace(R.id.fcvSearch, filterOption)
            transaction.addToBackStack(null)
            transaction.commit()
        }

    }

}