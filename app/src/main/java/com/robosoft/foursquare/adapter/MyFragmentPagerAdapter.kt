package com.robosoft.foursquare.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.robosoft.foursquare.fragment.NearYouFragment
import com.robosoft.foursquare.fragment.OtpFragment
import com.robosoft.foursquare.fragment.SignInFragment
import com.robosoft.foursquare.fragment.TopPickFragment

class MyFragmentPagerAdapter(fm: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fm, lifecycle) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        val nearYouFragment = NearYouFragment()
        val topPickFragment = TopPickFragment()
        val favouriteFragment = SignInFragment()
        return when (position) {
            0 -> nearYouFragment
            1 -> topPickFragment
            2 -> favouriteFragment
            else -> favouriteFragment
        }
    }

}