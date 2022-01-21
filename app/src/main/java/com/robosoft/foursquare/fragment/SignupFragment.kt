package com.robosoft.foursquare.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.robosoft.foursquare.databinding.FragmentSignupBinding
import com.robosoft.foursquare.room.UserModel
import com.robosoft.foursquare.util.Status
import com.robosoft.foursquare.view.HomeActivity
import com.robosoft.foursquare.view.LoginActivity
import com.robosoft.foursquare.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignupFragment : Fragment(){
    private lateinit var binding: FragmentSignupBinding
    private val loginViewModel: LoginViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding= FragmentSignupBinding.inflate(inflater)

        binding.login2.setOnClickListener {
            val userModel = UserModel(
                0,
                binding.mobileNum.text.toString(),
                binding.EmailAddress.text.toString(),
                binding.passwordEntry2.text.toString()
            )
            loginViewModel.registerUser(userModel).observe(viewLifecycleOwner, { dataFav ->
                dataFav?.let { resource ->
                    when (resource.status) {
                        Status.LOADING -> {

                        }
                        Status.SUCCESS -> {
                            resource.data?.let {
                                if(requireActivity() is LoginActivity){
                                    findNavController().navigate(com.robosoft.foursquare.R.id.action_signupFragment_to_signInFragment)
                                }
                            }
                        }
                        Status.ERROR -> {

                        }
                    }
                }
            })
        }


        return binding.root
    }
}