package com.robosoft.foursquare.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.robosoft.foursquare.databinding.FragmentConfirmPasswordBinding
import com.robosoft.foursquare.view.LoginActivity
import com.robosoft.foursquare.R
import com.robosoft.foursquare.util.Status
import com.robosoft.foursquare.view.HomeActivity
import com.robosoft.foursquare.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ConfirmPasswordFragment : Fragment() {

    private lateinit var binding: FragmentConfirmPasswordBinding
    private val loginViewModel: LoginViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentConfirmPasswordBinding.inflate(inflater)
        val phone = arguments?.getString("phone")
        Log.d("TAG", "onConfirmpassword: $phone")
        binding.submit.setOnClickListener {
            if (binding.passwordEntry3.text.toString() != binding.confirmPasswordEntry2.text.toString()) {
                Toast.makeText(requireActivity(), "Password not matching", Toast.LENGTH_LONG).show()
            } else {
                loginViewModel.updatePassword(binding.passwordEntry3.text.toString(), phone!!)
                    .observe(viewLifecycleOwner, { dataFav ->
                        dataFav?.let { resource ->
                            when (resource.status) {
                                Status.LOADING -> {

                                }
                                Status.SUCCESS -> {
                                    resource.data?.also {
                                        if (requireActivity() is LoginActivity) {
                                            findNavController().navigate(R.id.action_confirmPasswordFragment_to_signInFragment)
                                        }
                                    }
                                }
                                Status.ERROR -> {

                                }
                            }
                        }
                    })
            }

        }
        return binding.root
    }
}