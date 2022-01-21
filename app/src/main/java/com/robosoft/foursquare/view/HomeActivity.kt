package com.robosoft.foursquare.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.core.view.WindowCompat
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.robosoft.foursquare.R
import com.robosoft.foursquare.adapter.MyFragmentPagerAdapter
import com.robosoft.foursquare.databinding.ActivityHomeBinding
import com.robosoft.foursquare.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import nl.psdcompany.duonavigationdrawer.widgets.DuoDrawerToggle

@AndroidEntryPoint
class HomeActivity : AppCompatActivity(), View.OnClickListener {
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

        val llFavourites = menuView.findViewById<LinearLayout>(R.id.list1)
        val llFeedback = menuView.findViewById<LinearLayout>(R.id.list2)
        val llAboutUs = menuView.findViewById<LinearLayout>(R.id.list3)
        llFavourites.setOnClickListener(this)
        llFeedback.setOnClickListener(this)
        llAboutUs.setOnClickListener(this)

        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.homeFilter -> {
                    val i = Intent(this, SearchActivity::class.java)
                    startActivity(i)
                    true
                }
                R.id.search -> {
                    // Handle search icon press
                    true
                }
                else -> false
            }
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

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.list1 -> {
                binding.drawer.closeDrawer()
                val i = Intent(this, FavouritesActivity::class.java)
                startActivity(i)
            }
            R.id.list2 -> {
                binding.drawer.closeDrawer()
                val i = Intent(this, FeedbackActivity::class.java)
                startActivity(i)
            }
            R.id.list3 -> {
                binding.drawer.closeDrawer()
                val i = Intent(this, AboutUsActivity::class.java)
                startActivity(i)
            }
            R.id.list4 -> {
                binding.drawer.closeDrawer()
                val i = Intent(this, LoginActivity::class.java)
                startActivity(i)
                finish()
            }
        }
    }
}