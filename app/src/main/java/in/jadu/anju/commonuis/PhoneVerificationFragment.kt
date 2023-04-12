package `in`.jadu.anju.commonuis

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import dagger.hilt.android.AndroidEntryPoint
import `in`.jadu.anju.commonuis.viewmodels.PhoneVerificationViewModel
import `in`.jadu.anju.databinding.FragmentPhoneVerificationBinding
import `in`.jadu.anju.farmer.models.util.GetApiState
import `in`.jadu.anju.farmer.viewmodels.FarmerListItemViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class PhoneVerificationFragment  : Fragment() {
    private lateinit var binding:FragmentPhoneVerificationBinding
    private lateinit var auth:FirebaseAuth
    @Inject lateinit var phoneVerificationViewModel: PhoneVerificationViewModel
    private val farmerListItemViewModel:FarmerListItemViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        binding = FragmentPhoneVerificationBinding.inflate(inflater,container,false)
        auth = FirebaseAuth.getInstance()
        getOtp()

        lifecycleScope.launch {
            phoneVerificationViewModel.mainEvent.collect() { event->
                when(event){
                    is PhoneVerificationViewModel.MainEvent.GetUser -> {
                        updateUiAfterVerification()
                    }
                    is PhoneVerificationViewModel.MainEvent.Error -> {
                        updateUiOnError(event.error)
                    }
                }
            }
        }
        return binding.root
    }
    private fun getOtp(){
            binding.btnVerify.setOnClickListener {
                if(checkIfEditTextIsNotEmpty()){
                    val phoneNumber = binding.etEnterPhoneNumber.text?.trim().toString()
                    //add +91 to phone number
                    val newPhoneNumber = "+91$phoneNumber"
                    binding.lottieProgress.visibility = View.VISIBLE
                    binding.btnVerify.visibility = View.GONE
                    sendOtp(newPhoneNumber)
                    setPhone(newPhoneNumber)
                    getPhone(newPhoneNumber)
                }else{
                    binding.etEnterPhoneNumber.error = "Enter Phone Number"
                }
        }
    }

    private fun setPhone(newPhoneNumber: String) {
        lifecycleScope.launchWhenStarted {
            farmerListItemViewModel.setPhone(newPhoneNumber)
                .catch {
                    updateUiOnError(it.message.toString())
                }
        }

    }

    private fun getPhone(newPhoneNumber: String) {
        farmerListItemViewModel.getPhone()
        lifecycleScope.launchWhenStarted {
            farmerListItemViewModel.apiStateFlow.collect{
                when(it){
//                    is GetApiState.Success-> {
//                        val phone = it.data.
//                        if(phone == newPhoneNumber){
//                            updateUiAfterVerification()
//                        }else{
//                            updateUiOnError("Phone Number is not registered")
//                        }
//                    }

                    else -> {}
                }
            }
        }
    }



    private fun checkIfEditTextIsNotEmpty():Boolean{
        return binding.etEnterPhoneNumber.text.toString().isNotEmpty()
    }

    private fun sendOtp(phoneNumber: String) {
        val options = PhoneAuthOptions.newBuilder(FirebaseAuth.getInstance())
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L,TimeUnit.SECONDS)
            .setActivity(requireActivity())
            .setCallbacks(callbacks)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(p0: PhoneAuthCredential) {
            phoneVerificationViewModel.signInWithPhoneAuthCredential(p0)
        }

        override fun onVerificationFailed(e: FirebaseException) {

            if (e is FirebaseAuthInvalidCredentialsException) {
                // Invalid request
                 updateUiOnError(e.toString())
            } else if (e is FirebaseTooManyRequestsException) {
                // The SMS quota for the project has been exceeded
                updateUiOnError(e.toString())
            } else if (e is FirebaseAuthMissingActivityForRecaptchaException) {
                // reCAPTCHA verification attempted with null Activity
                updateUiOnError(e.toString())
            }
        }

        override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
//            storedVerificationId = verificationId
//            resendToken = token
            //send the verficaiton id to the next fragment
            val bundle = Bundle()
            bundle.putString("verificationId",verificationId)
            findNavController().navigate(`in`.jadu.anju.R.id.action_phoneVerfication_to_confirmOtpFragment,bundle)
        }
    }

    private fun updateUiOnError(e: String) {
        binding.tvError.visibility = View.VISIBLE
        binding.lottieProgress.visibility = View.GONE
        binding.btnVerify.visibility = View.VISIBLE
        binding.tvError.text = e
    }

    private fun updateUiAfterVerification(){
        binding.lottieProgress.visibility = View.GONE
        binding.btnVerify.visibility = View.VISIBLE
        findNavController().navigate(`in`.jadu.anju.R.id.action_phoneVerfication_to_confirmOtpFragment)
    }





}