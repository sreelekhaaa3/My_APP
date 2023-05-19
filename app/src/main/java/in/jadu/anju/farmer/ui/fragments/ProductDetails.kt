package `in`.jadu.anju.farmer.ui.fragments

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import dagger.hilt.android.AndroidEntryPoint
import `in`.jadu.anju.R
import `in`.jadu.anju.databinding.FragmentFarmerProductListBinding
import `in`.jadu.anju.databinding.FragmentProductDetailsBinding
import `in`.jadu.anju.farmer.models.dtos.Product
import `in`.jadu.anju.farmer.viewmodels.ProductDetailViewModel
import javax.inject.Inject


class ProductDetails : Fragment() {
    private lateinit var binding : FragmentProductDetailsBinding
    private lateinit var productName:String
    private lateinit var productPrice:String
    private lateinit var productImageUrl:String
    private lateinit var productDescription:String
    private lateinit var productCategory:String
    private lateinit var productExpireDate:String
    private lateinit var productId:String
    private lateinit var contractAddress:String
    private lateinit var seedingDate:String
    private val productDetailViewModel: ProductDetailViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding  = FragmentProductDetailsBinding.inflate(inflater,container,false)
        setBundleValues()
        val product: Product? = arguments?.getParcelable("productData")
        Log.d("productData",product.toString())
//        productDetailViewModel.getImageBitmap(requireContext(),productDetailViewModel.getImageLink(productImageUrl)){bitmap ->
//            if(bitmap!=null){
//                Palette.from(bitmap).generate { palette ->
//                    val dominantColor = palette?.getDominantColor(ContextCompat.getColor(requireContext(),R.color.blueColor))
//                    val lightVibrantColor = palette?.getLightVibrantColor(ContextCompat.getColor(requireContext(),R.color.blueColor))
//                    binding.collapsingToolbar.setBackgroundColor(lightVibrantColor!!)
//                }
//            }
//        }
        return binding.root
    }

    private fun setBundleValues(){
        productName = arguments?.getString("productName").toString()
        productPrice = arguments?.getString("productPrice").toString()
        productImageUrl = arguments?.getString("productImgUrl").toString()
        productDescription = arguments?.getString("productDescription").toString()
        productCategory = arguments?.getString("productType").toString()
        productExpireDate = arguments?.getString("productExpire").toString()
        productId = arguments?.getString("productId").toString()
        contractAddress = arguments?.getString("contractAddress").toString()
        seedingDate = arguments?.getString("seedingDate").toString()
    }





}