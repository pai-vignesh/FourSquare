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
import com.robosoft.foursquare.databinding.FragmentSigninBinding
import com.robosoft.foursquare.preferences.Preferences
import com.robosoft.foursquare.util.Status
import com.robosoft.foursquare.view.HomeActivity
import com.robosoft.foursquare.view.LoginActivity
import com.robosoft.foursquare.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class SignInFragment : Fragment() {
    private lateinit var binding: FragmentSigninBinding
    private val loginViewModel: LoginViewModel by viewModels()
    private lateinit var auth: FirebaseAuth
    private lateinit var storedVerificationId: String
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    private lateinit var phoneNum: String
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSigninBinding.inflate(inflater)
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
                val bundle = bundleOf(
                    "storedVerificationId" to storedVerificationId,
                    "isForgot" to true,
                    "phone" to phoneNum
                )
                if (requireActivity() is LoginActivity) {
                    findNavController().navigate(
                        com.robosoft.foursquare.R.id.action_signInFragment_to_otpFragment,
                        bundle
                    )
                }
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.forgotPassword.setOnClickListener {
            if (!(TextUtils.isEmpty(binding.personName.text.toString()))) {
                updateUserPassword()
            } else {
                Toast.makeText(
                    requireActivity(),
                    "Please enter email to reset password",
                    Toast.LENGTH_LONG
                )
                    .show()
            }
        }
        binding.login.setOnClickListener {
            if (!(TextUtils.isEmpty(binding.personName.text.toString()) || TextUtils.isEmpty(binding.passwordEntry.text.toString()))) {
                checkUserAndLogin()
            } else {
                Toast.makeText(requireActivity(), "Please enter all the fields", Toast.LENGTH_LONG)
                    .show()
            }
        }
        binding.createAccount.setOnClickListener {
            if (requireActivity() is LoginActivity) {
                findNavController().navigate(com.robosoft.foursquare.R.id.action_signInFragment_to_signupFragment)
            }
        }
    }

    private fun updateUserPassword() {
        loginViewModel.getUserData(binding.personName.text.toString())
            .observe(viewLifecycleOwner, { dataFav ->
                dataFav?.let { resource ->
                    when (resource.status) {
                        Status.LOADING -> {}
                        Status.SUCCESS -> {
                            resource.data?.also {
                                val number = "+91${it.phone}"
                                phoneNum = it.phone
                                sendVerificationCode(number)
                            } ?: run {
                                Toast.makeText(
                                    requireActivity(),
                                    "No user found.Please register",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                        Status.ERROR -> {}
                    }
                }
            })
    }

    private fun checkUserAndLogin() {
        loginViewModel.signInUser(
            binding.personName.text.toString(),
            binding.passwordEntry.text.toString()
        ).observe(viewLifecycleOwner, { dataFav ->
            dataFav?.let { resource ->
                when (resource.status) {
                    Status.LOADING -> {}
                    Status.SUCCESS -> {
                        resource.data?.also {
                            Preferences.setPrefs("isLogged","true",requireActivity())
                            val i = Intent(requireActivity(), HomeActivity::class.java)
                            startActivity(i)
                            activity?.finish()
                        } ?: run {
                            Toast.makeText(
                                requireActivity(),
                                "No user found.Please register",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                    Status.ERROR -> {}
                }
            }
        })
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
}