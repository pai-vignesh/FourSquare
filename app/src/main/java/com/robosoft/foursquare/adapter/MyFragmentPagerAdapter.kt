package com.robosoft.foursquare.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.robosoft.foursquare.fragment.*

class MyFragmentPagerAdapter(fm: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fm, lifecycle) {
    override fun getItemCount(): Int {
        return 5
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> NearYouFragment()
            1 -> TopPickFragment()
            2 -> PopularFragment()
            3 -> DinnerPlaceFragment()
            4 -> CoffeePlaceFragment()
            else -> CoffeePlaceFragment()
        }
    }

}