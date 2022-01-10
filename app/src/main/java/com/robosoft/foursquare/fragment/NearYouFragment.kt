package com.robosoft.foursquare.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.robosoft.foursquare.R
import com.robosoft.foursquare.databinding.FragmentNearYouBinding


class NearYouFragment : Fragment() {
    private lateinit var binding: FragmentNearYouBinding
    private lateinit var mapFragment : SupportMapFragment
    private lateinit var googleMap: GoogleMap
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNearYouBinding.inflate(inflater)
        mapFragment = childFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync {
            googleMap = it
        }
        return binding.root
    }


}