package com.robosoft.foursquare.fragment

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.robosoft.foursquare.databinding.FragmentSignupBinding
import com.robosoft.foursquare.room.UserModel
import com.robosoft.foursquare.util.Status
import com.robosoft.foursquare.view.LoginActivity
import com.robosoft.foursquare.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import com.robosoft.foursquare.R

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
            if(validateFormField()){
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
                                    Toast.makeText(requireActivity(),"User Registered successfully. Please login", Toast.LENGTH_LONG).show()
                                    if(requireActivity() is LoginActivity){
                                        findNavController().navigate(R.id.action_signupFragment_to_signInFragment)
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

    private fun validateFormField(): Boolean {
        var isValid = true

        if (TextUtils.isEmpty(binding.EmailAddress.text.toString()) ||
            TextUtils.isEmpty(binding.passwordEntry2.text.toString()) ||
            TextUtils.isEmpty(binding.confirmPasswordEntry.text.toString()) ||
            TextUtils.isEmpty(binding.mobileNum.text.toString())
        ) {
            Toast.makeText(requireActivity(),"Please enter all the fields", Toast.LENGTH_LONG).show()
            isValid = false
        }

        if(isValid && binding.passwordEntry2.text.toString() != binding.confirmPasswordEntry.text.toString()){
            Toast.makeText(requireActivity(),"Password not matching", Toast.LENGTH_LONG).show()
            isValid = false
        }





        return isValid
    }
}