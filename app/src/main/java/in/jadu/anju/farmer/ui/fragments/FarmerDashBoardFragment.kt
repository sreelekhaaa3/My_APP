package `in`.jadu.anju.farmer.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import `in`.jadu.anju.R
import `in`.jadu.anju.databinding.FragmentFarmerDashBoardBinding
import `in`.jadu.anju.farmer.adapters.FarmerListAdapter
import `in`.jadu.anju.farmer.viewmodels.FarmerListItemViewModel
import javax.inject.Inject

@AndroidEntryPoint
class FarmerDashBoardFragment : Fragment() {
    private val farmerListItemViewModel: FarmerListItemViewModel by viewModels()
    private lateinit var binding : FragmentFarmerDashBoardBinding
    private var auth:FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var itemListRecyclerView:RecyclerView
    private lateinit var itemListAdapter: FarmerListAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFarmerDashBoardBinding.inflate(inflater,container,false)
        itemListRecyclerView = binding.rvFarmerDashBoard
        itemListRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        farmerListItemViewModel.setPhone(auth.currentUser?.phoneNumber!!.substring(3))
        farmerListItemViewModel.getFarmerItemList(auth.currentUser?.phoneNumber!!.substring(3))
        farmerListItemViewModel.getFarmerListData.observe(viewLifecycleOwner){
            Log.d("FarmerList",it.toString())
            if(it.isEmpty()){
                binding.rvFarmerDashBoard.visibility = View.GONE
                val layoutParams = binding.AddItemButton.layoutParams as ConstraintLayout.LayoutParams
                layoutParams.verticalBias = 0.1f // set the vertical bias to 0.5 (centered vertically)
                binding.AddItemButton.layoutParams = layoutParams
            }else{
                binding.lottieAnimationView.visibility = View.GONE
                binding.tvTitle.visibility = View.GONE
                binding.AddItemButton.visibility = View.VISIBLE
                binding.rvFarmerDashBoard.visibility = View.VISIBLE
                itemListAdapter = FarmerListAdapter(it)
                itemListRecyclerView.adapter = itemListAdapter
                itemListAdapter.notifyDataSetChanged()
            }
        }

        binding.AddItemButton.setOnClickListener {
            findNavController().navigate(R.id.action_farmerDashBoardFragment2_to_farmerListItemFragment2)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupMenu()
    }

    private fun setupMenu() {
        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menuitems, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                if (menuItem.itemId == R.id.wallet){
                    binding.loadwalletprogress.isVisible = true
                    findNavController().navigate(R.id.action_farmerDashBoardFragment2_to_walletFragment)
                }
                if (menuItem.itemId == R.id.logout){
                    auth.signOut()
                    findNavController().navigate(R.id.action_farmerDashBoardFragment2_to_selectLanguage)
                }
                return true
            }

        },viewLifecycleOwner,Lifecycle.State.RESUMED)
    }

}