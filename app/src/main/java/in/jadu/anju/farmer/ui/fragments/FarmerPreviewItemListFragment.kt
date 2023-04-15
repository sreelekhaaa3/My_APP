package `in`.jadu.anju.farmer.ui.fragments

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import `in`.jadu.anju.R
import `in`.jadu.anju.databinding.FragmentFarmerPreviewItemListBinding
import `in`.jadu.anju.farmer.viewmodels.FarmerListItemViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

@AndroidEntryPoint
class FarmerPreviewItemListFragment : Fragment() {
    private val farmerListItemViewModel: FarmerListItemViewModel by viewModels()
    private lateinit var binding: FragmentFarmerPreviewItemListBinding
    private var productName: String? = null
    private var productDescription: String? = null
    private var harvestedDate: String? = null
    private var expiryDate: String? = null
    private var productPrice: String? = null
    private var productType: String? = null
    private var getUri: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFarmerPreviewItemListBinding.inflate(inflater, container, false)
        setPreviewData()
        binding.btnPreviewBtn.setOnClickListener {
            binding.lottieProgress.visibility = View.VISIBLE
            binding.btnPreviewBtn.visibility = View.GONE
            uploadDataToServer()
        }
        binding.btnEditFields.setOnClickListener {
            findNavController().navigate(R.id.action_farmerPreviewItemListFragment2_to_farmerListItemFragment2)
        }
        lifecycleScope.launch {
            farmerListItemViewModel.mainEvent.collect() { event ->
                when (event) {
                    is FarmerListItemViewModel.MainEvent.Error -> {
                        Toast.makeText(requireContext(), event.error, Toast.LENGTH_SHORT).show()
                    }
                    is FarmerListItemViewModel.MainEvent.Success -> {
                        Toast.makeText(requireContext(), event.message, Toast.LENGTH_SHORT).show()
                        if(event.message == "Product Created Successfully"){
                            findNavController().navigate(R.id.action_farmerPreviewItemListFragment2_to_successListingFragment)
                        }
                    }
                }
            }
        }
        return binding.root
    }

    private fun uploadDataToServer() {
        //upload Data to server
        val imagePart = farmerListItemViewModel.getImagePart(getUriFromPath(getUri!!), requireContext())
        farmerListItemViewModel.createProductRemote(
            productType!!,
            productName!!,
            imagePart,
            productDescription!!,
            harvestedDate!!,
            expiryDate!!,
            productPrice!!
        )
    }

    private fun getUriFromPath(path: String): Uri {
        return Uri.parse(path)
    }

    private fun setPreviewData() {
        productName = arguments?.getString("productName")
        productDescription = arguments?.getString("productDescription")
        harvestedDate = arguments?.getString("harvestedDate")
        expiryDate = arguments?.getString("expiryDate")
        productPrice = arguments?.getString("productPrice")
        productType = arguments?.getString("productType")
        getUri = arguments?.getString("ImageUri")
        binding.apply {
            tvProductType.text = productType
            tvProductName.text = productName
            tvProductDescription.text = productDescription
            tvHarvestedDate.text = harvestedDate
            tvExpiryDate.text = expiryDate
            tvProductPrice.text = productPrice
            ivCustomimage.setImageURI(getUriFromPath(getUri!!))
            ivVegetables.setImageURI(getUriFromPath(getUri!!))
        }
    }

}