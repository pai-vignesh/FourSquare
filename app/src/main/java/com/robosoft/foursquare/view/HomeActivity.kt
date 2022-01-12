package com.robosoft.foursquare.view

import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.WindowCompat
import androidx.viewpager2.widget.ViewPager2
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.material.tabs.TabLayout
import com.robosoft.foursquare.R
import com.robosoft.foursquare.adapter.MyFragmentPagerAdapter
import com.robosoft.foursquare.databinding.ActivityHomeBinding
import com.yarolegovich.slidingrootnav.SlidingRootNav
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var myFragmentPagerAdapter: MyFragmentPagerAdapter
    private lateinit var viewPager: ViewPager2
    private lateinit var slidingRootNav: SlidingRootNav
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        WindowCompat.setDecorFitsSystemWindows(window, false)
        viewPager = binding.pager
        viewPager.isUserInputEnabled = false;
        slidingRootNav = SlidingRootNavBuilder(this).withToolbarMenuToggle(binding.topAppBar)
            .withMenuOpened(false)
            .withContentClickableWhenMenuOpened(false)
            .withSavedState(savedInstanceState)
            .withMenuLayout(R.layout.menu_left_drawer)
            .inject();
        


        setPagerAdapter()
    }


    private fun setPagerAdapter() {
        myFragmentPagerAdapter = MyFragmentPagerAdapter(supportFragmentManager, lifecycle)
        viewPager.adapter = myFragmentPagerAdapter


        //switch between tabs
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                binding.pager.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
        binding.pager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                binding.tabLayout.selectTab(binding.tabLayout.getTabAt(position))
            }
        })
    }
}