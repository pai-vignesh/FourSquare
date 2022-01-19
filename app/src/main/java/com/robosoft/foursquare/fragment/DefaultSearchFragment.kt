package com.robosoft.foursquare.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.robosoft.foursquare.databinding.FragmentSearchDefaultBinding
import com.robosoft.foursquare.R

class DefaultSearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchDefaultBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentSearchDefaultBinding.inflate(inflater)

        binding.useLocation.setOnClickListener{
            findNavController().navigate(R.id.action_defaultSearchFragment_to_listViewFragment)
        }

        binding.useMap.setOnClickListener{
            findNavController().navigate(R.id.action_defaultSearchFragment_to_mapViewFragment)
        }



        return binding.root
    }
}