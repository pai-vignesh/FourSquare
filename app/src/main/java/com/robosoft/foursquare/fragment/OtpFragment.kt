package com.robosoft.foursquare.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.robosoft.foursquare.databinding.FragmentOtpBinding
import com.robosoft.foursquare.view.LoginActivity

class OtpFragment : Fragment(){

    private lateinit var binding: FragmentOtpBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding= FragmentOtpBinding.inflate(inflater)
        binding.getIn.setOnClickListener {
            if(requireActivity() is LoginActivity){
                findNavController().navigate(com.robosoft.foursquare.R.id.action_otpFragment_to_confirmPasswordFragment)
            }
        }
        return binding.root
    }

}