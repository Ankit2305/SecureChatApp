package com.ankitkumar.securechatapplication

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.ankitkumar.securechatapplication.Daos.UserDao
import com.ankitkumar.securechatapplication.databinding.FragmentSignInBinding
import com.ankitkumar.securechatapplication.model.User
import com.ankitkumar.securechatapplication.util.AUTH_CODE
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.firestore.FirebaseFirestore
import java.util.concurrent.TimeUnit


class SignInFragment : Fragment(R.layout.fragment_sign_in) {

    private val TAG: String = "SignInFragment"
    lateinit var binding: FragmentSignInBinding
    lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    lateinit var mAuth: FirebaseAuth
    private lateinit var verificationId : String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSignInBinding.bind(view)
        mAuth = FirebaseAuth.getInstance()

        if(mAuth.currentUser!=null){
            view.findNavController().navigate(R.id.action_signInFragment_to_chatListFragment)
        }
        setViewClickListener()

        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                Log.d(TAG, "onVerificationCompleted:$credential")
                signInWithPhoneAuthCredential(credential)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w(TAG, "onVerificationFailed", e)

                if (e is FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                } else if (e is FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                }
                // Show a message and update the UI
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d(TAG, "onCodeSent:$verificationId")
                // Save verification ID and resending token so we can use them later
                this@SignInFragment.verificationId = verificationId
            }
        }

    }


    private fun setViewClickListener() {

        binding.btnGetOtp.setOnClickListener {
            val mobilNo = binding.etPhoneNumber.text
            val name = binding.etName.text
            if(mobilNo.isNotEmpty() && mobilNo.length==10 && name.isNotEmpty()){
                val phone = "+91$mobilNo"
                sendVerificationCode(phone)
            }else {
                if(name.isEmpty())
                    Toast.makeText(context, "Please enter your name", Toast.LENGTH_SHORT).show()
                else
                    Toast.makeText(context, "Please enter valid Phone number", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnVerify.setOnClickListener {
            val otpEntered = binding.etOtp.text.toString()
            if(otpEntered.isNotEmpty())
                verifyOtp(otpEntered)
            else
                Toast.makeText(context,"Please enter OTP",Toast.LENGTH_SHORT).show()
        }
    }

    private fun verifyOtp(otpEntered: String) {
        val credential = PhoneAuthProvider.getCredential(verificationId, otpEntered)
        signInWithPhoneAuthCredential(credential)
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(context as Activity) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    Toast.makeText(context,"SignIn : success",Toast.LENGTH_SHORT).show()

                    val user = task.result?.user
                    updateUI(user)
                } else {
                    // Sign in failed, display a message and update the UI
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        Toast.makeText(context,"PLease enter valid OTP.",Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }

    private fun sendVerificationCode(phone: String) {
        val options = PhoneAuthOptions.newBuilder(mAuth)
            .setPhoneNumber(phone)       // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(context as Activity)                 // Activity (for callback binding)
            .setCallbacks(callbacks)          // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private fun updateUI(currentUser : FirebaseUser?){
        if(currentUser!=null){
            val user  = User(currentUser.uid,
                binding.etName.text.toString(),
                binding.etPhoneNumber.text.toString()
            )
            val sharedPref = activity?.getSharedPreferences(resources.getString(R.string.app_name),Context.MODE_PRIVATE) ?: return
            with (sharedPref.edit()) {
                putString(AUTH_CODE,user.uid)
                apply()
            }

            UserDao().addUser(user)
            view?.findNavController()?.navigate(R.id.action_signInFragment_to_chatListFragment)
        }
    }


}