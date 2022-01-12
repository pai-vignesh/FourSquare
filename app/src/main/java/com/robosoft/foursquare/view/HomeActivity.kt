package com.robosoft.foursquare.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import androidx.core.view.WindowCompat
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.robosoft.foursquare.R
import com.robosoft.foursquare.adapter.MyFragmentPagerAdapter
import com.robosoft.foursquare.databinding.ActivityHomeBinding
import nl.psdcompany.duonavigationdrawer.widgets.DuoDrawerToggle

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var myFragmentPagerAdapter: MyFragmentPagerAdapter
    private lateinit var viewPager: ViewPager2
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        viewPager = binding.pager
        viewPager.isUserInputEnabled = false;

        val drawerToggle = DuoDrawerToggle(
            this, binding.drawer, binding.topAppBar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )

        binding.drawer.setDrawerListener(drawerToggle)
        drawerToggle.syncState()

        val menuView = binding.drawer.menuView

        val ll1 = menuView.findViewById<LinearLayout>(R.id.list1)

        ll1.setOnClickListener {
            binding.drawer.closeDrawer()
            val i = Intent(this, ReviewActivity::class.java)
            startActivity(i)
        }

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