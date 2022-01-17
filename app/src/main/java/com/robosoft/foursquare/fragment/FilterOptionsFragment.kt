package com.robosoft.foursquare.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.robosoft.foursquare.databinding.FragmentFilterOptionsBinding

class FilterOptionsFragment : Fragment(){

    private lateinit var binding: FragmentFilterOptionsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding= FragmentFilterOptionsBinding.inflate(inflater)
        return binding.root
    }
}
