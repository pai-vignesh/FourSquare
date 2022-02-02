package com.robosoft.foursquare.fragment

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.robosoft.foursquare.databinding.FragmentSignupBinding
import com.robosoft.foursquare.room.UserModel
import com.robosoft.foursquare.util.Status
import com.robosoft.foursquare.view.LoginActivity
import com.robosoft.foursquare.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import com.robosoft.foursquare.R
import com.robosoft.foursquare.view.HomeActivity
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class SignupFragment : Fragment() {
    private lateinit var binding: FragmentSignupBinding
    private val loginViewModel: LoginViewModel by viewModels()
    private lateinit var auth: FirebaseAuth
    private lateinit var storedVerificationId: String
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignupBinding.inflate(inflater)
        auth = FirebaseAuth.getInstance()

        // Callback function for Phone Auth
        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                startActivity(Intent(requireContext(), HomeActivity::class.java))
                activity?.finish()
            }

            override fun onVerificationFailed(e: FirebaseException) {
                Toast.makeText(requireContext(), "Failed", Toast.LENGTH_LONG).show()
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                storedVerificationId = verificationId
                resendToken = token
                val isForgot = false
                val phone = ""
                val bundle = bundleOf(
                    "storedVerificationId" to storedVerificationId,
                    "isForgot" to isForgot,
                    "phone" to phone
                )
                if (requireActivity() is LoginActivity) {
                    findNavController().navigate(
                        R.id.action_signupFragment_to_otpFragment,
                        bundle
                    )
                }
            }
        }
        binding.login2.setOnClickListener {
            if (validateFormField()) {
                val userModel = UserModel(
                    0,
                    binding.mobileNum.text.toString(),
                    binding.EmailAddress.text.toString(),
                    binding.passwordEntry2.text.toString()
                )
                loginViewModel.registerUser(userModel).observe(viewLifecycleOwner) { dataFav ->
                    dataFav?.let { resource ->
                        when (resource.status) {
                            Status.LOADING -> {}
                            Status.SUCCESS -> {
                                resource.data?.let {
                                    Toast.makeText(
                                        requireActivity(),
                                        "User Registered successfully. Please verify otp",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    val number = "+91${binding.mobileNum.text}"
                                    sendVerificationCode(number)
                                }
                            }
                            Status.ERROR -> {}
                        }
                    }
                }
            }
        }
        return binding.root
    }

    private fun sendVerificationCode(number: String) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(number) // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(requireActivity()) // Activity (for callback binding)
            .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private fun validateFormField(): Boolean {
        var isValid = true
        if (TextUtils.isEmpty(binding.EmailAddress.text.toString()) ||
            TextUtils.isEmpty(binding.passwordEntry2.text.toString()) ||
            TextUtils.isEmpty(binding.confirmPasswordEntry.text.toString()) ||
            TextUtils.isEmpty(binding.mobileNum.text.toString())
        ) {
            Toast.makeText(requireActivity(), "Please enter all the fields", Toast.LENGTH_LONG)
                .show()
            isValid = false
        }
        if (isValid && binding.passwordEntry2.text.toString() != binding.confirmPasswordEntry.text.toString()) {
            Toast.makeText(requireActivity(), "Password not matching", Toast.LENGTH_LONG).show()
            isValid = false
        }
        return isValid
    }
}