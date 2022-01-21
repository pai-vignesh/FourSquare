package com.robosoft.foursquare.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.robosoft.foursquare.databinding.FragmentSigninBinding
import com.robosoft.foursquare.util.Status
import com.robosoft.foursquare.view.HomeActivity
import com.robosoft.foursquare.view.LoginActivity
import com.robosoft.foursquare.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInFragment : Fragment(){
    private lateinit var binding: FragmentSigninBinding
    private val loginViewModel: LoginViewModel by viewModels()
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

        binding.forgotPassword.setOnClickListener{
            if(requireActivity() is LoginActivity){
                findNavController().navigate(com.robosoft.foursquare.R.id.action_signInFragment_to_otpFragment)
            }
        }

        binding.login.setOnClickListener {

            loginViewModel.signInUser(binding.personName.text.toString(),binding.passwordEntry.text.toString()).observe(viewLifecycleOwner, { dataFav ->
                dataFav?.let { resource ->
                    when (resource.status) {
                        Status.LOADING -> {

                        }
                        Status.SUCCESS -> {
                            resource.data?.let {
                                Log.d("TAG", "onViewCreated: $it ")
                                val i = Intent(requireActivity(), HomeActivity::class.java)
                                startActivity(i)
                            }
                        }
                        Status.ERROR -> {

                        }
                    }
                }
            })
        }

        binding.createAccount.setOnClickListener {
            if(requireActivity() is LoginActivity){
                findNavController().navigate(com.robosoft.foursquare.R.id.action_signInFragment_to_signupFragment)
            }
        }

    }
}