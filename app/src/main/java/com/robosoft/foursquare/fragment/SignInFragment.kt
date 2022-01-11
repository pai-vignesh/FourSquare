package com.robosoft.foursquare.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.robosoft.foursquare.databinding.FragmentSigninBinding
import com.robosoft.foursquare.view.LoginActivity

class SignInFragment : Fragment(){
    private lateinit var binding: FragmentSigninBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding= FragmentSigninBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvSign.setOnClickListener{
            if(requireActivity() is LoginActivity){
                findNavController().navigate(com.robosoft.foursquare.R.id.action_signInFragment_to_otpFragment)
            }
        }





    }
}