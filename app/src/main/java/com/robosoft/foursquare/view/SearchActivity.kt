package com.robosoft.foursquare.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SearchView
import androidx.core.view.WindowCompat
import com.robosoft.foursquare.databinding.ActivitySearchBinding
import com.robosoft.foursquare.R
import androidx.fragment.app.FragmentTransaction
import com.robosoft.foursquare.fragment.FilterOptionsFragment
import com.robosoft.foursquare.fragment.ListViewFragment
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
        var query = ""
        var near = ""

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
                val listView = ListViewFragment()
                val args = Bundle()
                p0?.let {
                    query=it
                }
                args.putString("query", query)
                args.putString("near", near)
                listView.arguments = args
                transaction.replace(R.id.fcvSearch, listView)
                transaction.addToBackStack(null)
                transaction.commit()
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                return false
            }
        })

        binding.searchView2.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
                val listView = ListViewFragment()
                val args = Bundle()
                p0?.let {
                    near = it
                }
                args.putString("query", query)
                args.putString("near", near)
                listView.arguments = args
                transaction.replace(R.id.fcvSearch, listView)
                transaction.addToBackStack(null)
                transaction.commit()
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                return false
            }

        })

        binding.filterMenu.setOnClickListener {
            val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
            val filterOption = FilterOptionsFragment()
            transaction.replace(R.id.fcvSearch, filterOption)
            transaction.addToBackStack(null)
            transaction.commit()
        }
    }
}