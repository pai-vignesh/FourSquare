package com.robosoft.foursquare.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.robosoft.foursquare.databinding.FragmentTopPickBinding

class TopPickFragment : Fragment() {
    private lateinit var binding: FragmentTopPickBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentTopPickBinding.inflate(inflater)
        return binding.root
    }
}