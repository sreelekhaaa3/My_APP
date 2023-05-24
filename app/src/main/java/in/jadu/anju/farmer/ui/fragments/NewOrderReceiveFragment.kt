package `in`.jadu.anju.farmer.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import `in` .jadu.anju.R
import `in`.jadu.anju.databinding.FragmentNewOrderReceiveBinding
import `in`.jadu.anju.farmer.adapters.ReceivedOrderAdapter
import `in`.jadu.anju.farmer.viewmodels.FarmerListItemViewModel

@AndroidEntryPoint
class NewOrderReceiveFragment : Fragment(),ReceivedOrderAdapter.OnItemClickListener {
    private lateinit var bundle: Bundle
    private lateinit var binding: FragmentNewOrderReceiveBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var newOrderReceiveAdapter: ReceivedOrderAdapter
    private  val farmerListItemViewModel: FarmerListItemViewModel by viewModels()
    private lateinit var auth:FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewOrderReceiveBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()
        bundle = Bundle()
        recyclerView = binding.rvPurchasedProduct
        recyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(requireContext())
        newOrderReceiveAdapter = ReceivedOrderAdapter(this)
        val phoneNumber = auth.currentUser?.phoneNumber?.substring(3)
        farmerListItemViewModel.getRequestedProductListData.observe(viewLifecycleOwner){
            if(it.product.isEmpty()) {
                binding.productLoadingProgress.visibility = View.GONE
                Toast.makeText(requireContext(), "No Products", Toast.LENGTH_SHORT).show()
            }else{
                binding.productLoadingProgress.visibility = View.GONE
                binding.rvPurchasedProduct.visibility = View.VISIBLE
                newOrderReceiveAdapter.receivedProduct = it
                recyclerView.adapter = newOrderReceiveAdapter
                newOrderReceiveAdapter.notifyDataSetChanged()
            }
        }


        return binding.root
    }

    override fun onItemClicked(position: Int) {
        bundle = bundleOf("position" to position)
        findNavController().navigate(`in`.jadu.anju.R.id.action_newOrderReceiveFragment_to_shipmentApprovalFragment,bundle)
    }

}