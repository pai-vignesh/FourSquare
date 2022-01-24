package com.robosoft.foursquare.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.*
import com.robosoft.foursquare.databinding.FragmentOtpBinding
import com.robosoft.foursquare.view.HomeActivity
import com.robosoft.foursquare.view.LoginActivity

class OtpFragment : Fragment() {
    private lateinit var binding: FragmentOtpBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOtpBinding.inflate(inflater)
        auth = FirebaseAuth.getInstance()
        val storedVerificationId = arguments?.getString("storedVerificationId")
        val isForgot = arguments?.getBoolean("isForgot")
        val phone = arguments?.getString("phone")
        binding.getIn.setOnClickListener {
            val otp = binding.OTPEntry.text.toString().trim()
            if (otp.isNotEmpty()) {
                val credential: PhoneAuthCredential = PhoneAuthProvider.getCredential(
                    storedVerificationId.toString(), otp
                )
                isForgot?.let { forgot ->
                    phone?.let { num ->
                        signInWithPhoneAuthCredential(credential, forgot, num)
                    }
                }
            } else {
                Toast.makeText(requireActivity(), "Enter OTP", Toast.LENGTH_SHORT).show()
            }
        }
        return binding.root
    }

    private fun signInWithPhoneAuthCredential(
        credential: PhoneAuthCredential,
        isForgot: Boolean,
        phone: String = ""
    ) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    if (isForgot) {
                        if (requireActivity() is LoginActivity) {
                            val bundle = bundleOf("phone" to phone)
                            findNavController().navigate(
                                com.robosoft.foursquare.R.id.action_otpFragment_to_confirmPasswordFragment,
                                bundle
                            )
                        }
                    } else {
                        startActivity(Intent(requireActivity(), HomeActivity::class.java))
                        activity?.finish()
                    }
                } else {
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        Toast.makeText(requireActivity(), "Invalid OTP", Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }
}