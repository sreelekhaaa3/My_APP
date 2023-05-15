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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import `in`.jadu.anju.R
import `in`.jadu.anju.databinding.FragmentFarmerDashBoardBinding
import `in`.jadu.anju.databinding.FragmentFarmerProductListBinding
import `in`.jadu.anju.farmer.adapters.FarmerListAdapter
import `in`.jadu.anju.farmer.viewmodels.FarmerListItemViewModel

@AndroidEntryPoint
class FarmerProductList : Fragment() {
    private val farmerListItemViewModel: FarmerListItemViewModel by viewModels()
    private lateinit var binding : FragmentFarmerProductListBinding
    private var auth:FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var itemListRecyclerView:RecyclerView
    private lateinit var itemListAdapter: FarmerListAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFarmerProductListBinding.inflate(inflater,container,false)
        itemListRecyclerView = binding.rvFarmerDashBoard
        itemListRecyclerView.layoutManager = GridLayoutManager(requireContext(),2,LinearLayoutManager.VERTICAL,false)
        farmerListItemViewModel.setPhone(auth.currentUser?.phoneNumber!!.substring(3))
        farmerListItemViewModel.getFarmerItemList(auth.currentUser?.phoneNumber!!.substring(3))
        farmerListItemViewModel.getFarmerListData.observe(viewLifecycleOwner){
            Log.d("FarmerList",it.toString())
            if(it.isEmpty()){
                binding.rvFarmerDashBoard.visibility = View.GONE
            }else{
                binding.lottieAnimationView.visibility = View.GONE
                binding.tvTitle.visibility = View.GONE
                binding.rvFarmerDashBoard.visibility = View.VISIBLE
                itemListAdapter = FarmerListAdapter(it)
                itemListRecyclerView.adapter = itemListAdapter
                itemListAdapter.notifyDataSetChanged()
            }
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