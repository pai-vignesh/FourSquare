package com.robosoft.foursquare.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.robosoft.foursquare.databinding.FragmentConfirmPasswordBinding
import com.robosoft.foursquare.view.LoginActivity


class ConfirmPasswordFragment : Fragment() {

    private lateinit var binding: FragmentConfirmPasswordBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding= FragmentConfirmPasswordBinding.inflate(inflater)
        binding.submit.setOnClickListener {
            if(requireActivity() is LoginActivity){
                findNavController().navigate(com.robosoft.foursquare.R.id.action_confirmPasswordFragment_to_signInFragment)
            }
        }
        return binding.root
    }
}