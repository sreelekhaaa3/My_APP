package `in`.jadu.anju.farmer.ui.fragments

import android.graphics.Bitmap
import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.palette.graphics.Palette
import dagger.hilt.android.AndroidEntryPoint
import `in` .jadu.anju.R
import `in`.jadu.anju.commonuis.MainActivity
import `in`.jadu.anju.databinding.FragmentShipmentApprovalBinding
import `in`.jadu.anju.farmer.viewmodels.FarmerListItemViewModel

@AndroidEntryPoint
class ShipmentApprovalFragment : Fragment() {
    private lateinit var binding:FragmentShipmentApprovalBinding
    private var position:Int? = null
    private  val farmerListItemViewModel: FarmerListItemViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentShipmentApprovalBinding.inflate(layoutInflater,container,false)
        position = arguments?.getInt("position")
        loadProduct()
        binding.confirmOrder.setOnClickListener {
            binding.update.visibility = View.VISIBLE
            binding.update.text = getString(`in`.jadu.anju.R.string.order_confirmed)
        }
        binding.canelOrder.setOnClickListener {
            binding.update.visibility = View.VISIBLE
            binding.update.text = getString(`in`.jadu.anju.R.string.order_cancelled)
        }
        return binding.root
    }

    private fun loadProduct(){
        Log.d("productPosition","$position")
        farmerListItemViewModel.getRequestedProductListData.observe(viewLifecycleOwner){productList->
            if(productList.product.isEmpty()){
                binding.productLoadingProgress.visibility = View.GONE
            }else{
                binding.productLoadingProgress.visibility = View.GONE
                binding.appbarLayout.visibility = View.VISIBLE
                binding.nestedScrollView.visibility = View.VISIBLE
                position?.let {
                    binding.productIdValue.text = productList.product[it].product.web3Id.removeSurrounding("\"")
                    binding.tvProductQuantityValue.text = productList.product[it].quantity.removeSurrounding("\"")
                    binding.tvOrderIdValue.text = productList.product[it].sellerId.removeSurrounding("\"")
                    binding.tvOrderedByValue.text = productList.userName.removeSurrounding("\"")
                    binding.tvOrderedDateValue.text = productList.date.removeSurrounding("\"")
                    binding.tvDeliveryLocationValue.text = productList.userAddress.removeSurrounding("\"")
                    binding.collapsingToolbar.title = productList.product[it].product.productName.removeSurrounding("\"")
                    binding.tvPriceValue.text = productList.product[it].product.productPrice.removeSurrounding("\"")
                    farmerListItemViewModel.getImageBitmap(requireContext(),farmerListItemViewModel.getImageLink(productList.product[it].product.productImageUrl)){bitmap ->
                        if(bitmap!=null){
                            Palette.from(bitmap).generate { palette ->
                                val dominantColor = palette?.getDominantColor(
                                    ContextCompat.getColor(requireContext(),
                                        R.color.gradient))
                                val lightVibrantColor = palette?.getLightVibrantColor(
                                    ContextCompat.getColor(requireContext(),
                                        R.color.gradient))
                                changeStatusBarColor(requireActivity().window, dominantColor!!)
                                (activity as MainActivity).setupActionBarColor(dominantColor)
                                binding.collapsingToolbar.setBackgroundColor(dominantColor!!)
                                binding.productDetailsLayout.setBackgroundColor(dominantColor)
                                binding.ivProductImage.setImageBitmap(bitmap)
                            }
                        }
                    }
                    binding.confirmOrder.setOnClickListener {view->
                        farmerListItemViewModel.updateOrderStatus(productList._id.removeSurrounding("\""),productList.product[it].product._id.removeSurrounding("\""),"Approved")
                        Toast.makeText(requireContext(), "Approved", Toast.LENGTH_SHORT).show()
                        Log.d("productListId",productList._id.removeSurrounding("\""))
                    }
                }

            }
            }

    }

    override fun onDestroy() {
        super.onDestroy()
        (activity as MainActivity).setupActionBarColor(ContextCompat.getColor(requireContext(),R.color.gradient))
        changeStatusBarColor(requireActivity().window,ContextCompat.getColor(requireContext(),R.color.gradient))
    }
    private fun changeStatusBarColor(window: Window, color: Int) {
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = color
    }

}