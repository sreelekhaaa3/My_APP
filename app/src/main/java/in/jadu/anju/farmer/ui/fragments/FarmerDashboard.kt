package `in`.jadu.anju.farmer.ui.fragments

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import `in`.jadu.anju.R
import `in`.jadu.anju.commonuis.MainActivity
import `in`.jadu.anju.databinding.FragmentFarmerDashBoardBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class FarmerDashboard : Fragment() {
    private lateinit var binding : FragmentFarmerDashBoardBinding
    private var backPressedTime: Long = 0 // initialize the variable
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding  = FragmentFarmerDashBoardBinding.inflate(inflater,container,false)
        exitOnBackPress()
        setupDashBoard()
        return binding.root
    }

    private fun setupDashBoard(){
        binding.productLsts.setOnClickListener {
            findNavController().navigate(R.id.action_farmerDashboard_to_farmerProductList)
        }
        binding.lstNewProduct.setOnClickListener {
            findNavController().navigate(R.id.action_farmerDashboard_to_farmerListItemFragment)
        }
        binding.checkWallet.setOnClickListener {
            binding.loadwalletprogress.visibility = View.VISIBLE
            findNavController().navigate(R.id.action_farmerDashboard_to_walletFragment)
        }
        binding.allOrdersBtn.setOnClickListener {
            findNavController().navigate(R.id.action_farmerDashboard_to_allOrdersFragment)
        }
        binding.newOrders.setOnClickListener {
            findNavController().navigate(R.id.action_farmerDashboard_to_newOrderReceiveFragment)
        }
        binding.editProfile.setOnClickListener {
            findNavController().navigate(R.id.action_farmerDashboard_to_farmerProfileFragment)
        }
        binding.exitToApp.setOnClickListener {
            findNavController().navigate(R.id.action_farmerDashboard_to_selectLanguage)
            FirebaseAuth.getInstance().signOut()
        }
        binding.exitApp.setOnClickListener {
            findNavController().navigate(R.id.action_farmerDashboard_to_selectLanguage)
            FirebaseAuth.getInstance().signOut()
        }

    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
        (requireActivity() as AppCompatActivity).supportActionBar?.title=""
        (requireActivity() as AppCompatActivity).supportActionBar?.elevation=0f
    }

    override fun onDestroy() {
        super.onDestroy()
        (requireActivity() as AppCompatActivity).supportActionBar?.elevation=24f
    }

    override fun onPause() {
        super.onPause()
        (requireActivity() as MainActivity).showActionBar()
    }
    private fun exitOnBackPress(){
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner) {
            if (System.currentTimeMillis() - backPressedTime < 2000) { // if back pressed twice within 2 seconds
                activity?.finish()
            } else {
                Toast.makeText(context, "Press back again to exit", Toast.LENGTH_SHORT).show()
                backPressedTime = System.currentTimeMillis() // update the last back pressed time
            }
        }
    }

 fun statusBar(){
     val window = requireActivity().window
     window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
     window.statusBarColor = Color.TRANSPARENT
 }



}