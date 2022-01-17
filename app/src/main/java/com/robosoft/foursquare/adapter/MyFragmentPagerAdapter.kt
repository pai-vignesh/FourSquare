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
        val nearYouFragment = NearYouFragment()
        val topPickFragment = TopPickFragment()
        val popularFragment = PopularFragment()
        val coffeePlaceFragment = CoffeePlaceFragment()
        val dinnerPlaceFragment = DinnerPlaceFragment()
        return when (position) {
            0 -> nearYouFragment
            1 -> topPickFragment
            2 -> popularFragment
            3 -> dinnerPlaceFragment
            4 -> coffeePlaceFragment
            else -> coffeePlaceFragment
        }
    }

}