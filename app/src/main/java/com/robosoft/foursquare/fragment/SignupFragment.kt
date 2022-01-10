package com.robosoft.foursquare.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.robosoft.foursquare.databinding.FragmentSignupBinding

class SignupFragment : Fragment(){
    private lateinit var binding: FragmentSignupBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding= FragmentSignupBinding.inflate(inflater)

        return binding.root
    }
}