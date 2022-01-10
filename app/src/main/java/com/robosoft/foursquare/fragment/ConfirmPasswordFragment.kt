package com.robosoft.foursquare.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.robosoft.foursquare.databinding.FragmentConfirmPasswordBinding


class ConfirmPasswordFragment : Fragment() {

    private lateinit var binding: FragmentConfirmPasswordBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding= FragmentConfirmPasswordBinding.inflate(inflater)

        return binding.root
    }
}