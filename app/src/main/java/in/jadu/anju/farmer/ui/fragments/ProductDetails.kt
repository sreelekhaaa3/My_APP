package `in`.jadu.anju.farmer.ui.fragments

import android.content.ClipData
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.icu.text.SimpleDateFormat
import android.os.Build
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
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import dagger.hilt.android.AndroidEntryPoint
import `in`.jadu.anju.R
import `in`.jadu.anju.commonuis.MainActivity
import `in`.jadu.anju.databinding.FragmentFarmerProductListBinding
import `in`.jadu.anju.databinding.FragmentProductDetailsBinding
import `in`.jadu.anju.farmer.models.dtos.Product
import `in`.jadu.anju.farmer.viewmodels.ProductDetailViewModel
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import kotlin.math.abs


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
        updatingUi()
        val product: Product? = arguments?.getParcelable("productData")
        Log.d("productData",product.toString())
        productDetailViewModel.getImageBitmap(requireContext(),productDetailViewModel.getImageLink(productImageUrl)){bitmap ->
            if(bitmap!=null){
                Palette.from(bitmap).generate { palette ->
                    val dominantColor = palette?.getDominantColor(ContextCompat.getColor(requireContext(),R.color.blueColor))
                    val lightVibrantColor = palette?.getLightVibrantColor(ContextCompat.getColor(requireContext(),R.color.blueColor))
                    changeStatusBarColor(requireActivity().window, dominantColor!!)
                    (activity as MainActivity).setupActionBarColor(dominantColor)
                    binding.collapsingToolbar.setBackgroundColor(dominantColor!!)
                    binding.productDetailsLayout.setBackgroundColor(dominantColor)
                    binding.ivProductImage.setImageBitmap(bitmap)
                    binding.appbarLayout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { _, verticalOffset ->
                        if (abs(verticalOffset) == binding.appbarLayout.totalScrollRange) {
                            binding.toolbar.setBackgroundColor(dominantColor)
                            binding.toolbar.visibility = View.VISIBLE
                            binding.collapsingToolbar.title = ""
                        } else {
                            binding.toolbar.visibility = View.GONE
                            binding.collapsingToolbar.title = productName.removeSurrounding("\"")
                        }
                    })


                }
            }
        }
        binding.copyProductId.setOnClickListener {
            copyAddress(productId.removeSurrounding("\""))
        }
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
        Log.d("productidx","$productId")
        contractAddress = arguments?.getString("contractAddress").toString()
        seedingDate = arguments?.getString("seedingDate").toString()
    }

    private fun updatingUi(){
        binding.collapsingToolbar.title = productName.removeSurrounding("\"")
        binding.tvExpiryDate.text = productExpireDate.removeSurrounding("\"")
        binding.tvDescription.text = productDescription.removeSurrounding("\"")
        binding.tvSeedingDate.text = seedingDate.removeSurrounding("\"")
        binding.tvPriceValue.text = "₹ "+productPrice.removeSurrounding("\"")
        val time = productId.removeSurrounding("\"").toLong()
        binding.tvProductAddedDate.text = convertUnixTimeToDate(time)
        binding.tvProductAddedTime.text = convertUnixTimeToTime(time)
        binding.productIdValue.text = productId.removeSurrounding("\"")
    }

    private fun convertUnixTimeToDate(unixTime: Long): String {
        val date = Date(unixTime) // Convert Unix timestamp to milliseconds
        val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        return dateFormat.format(date)
    }
    private fun convertUnixTimeToTime(unixTime: Long): String {
        val date = Date(unixTime * 1000) // Convert Unix timestamp to milliseconds
        val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        return timeFormat.format(date)
    }

    private fun changeStatusBarColor(window: Window, color: Int) {
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = color
    }


    override fun onDestroy() {
        super.onDestroy()
        (activity as MainActivity).setupActionBarColor(ContextCompat.getColor(requireContext(),R.color.gradient))
        changeStatusBarColor(requireActivity().window,ContextCompat.getColor(requireContext(),R.color.gradient))
    }

    //copy the contents to clipboard
    private fun copyAddress(address:String){
        val clipboardManager = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
        val clipData = ClipData.newPlainText("text", address)
        clipboardManager.setPrimaryClip(clipData)
        Toast.makeText(requireContext(), "ID Copied to clipboard", Toast.LENGTH_SHORT).show()
    }




}