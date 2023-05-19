package `in`.jadu.anju.farmer.ui.fragments

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import `in`.jadu.anju.R
import `in`.jadu.anju.databinding.FragmentFarmerPreviewItemListBinding
import `in`.jadu.anju.farmer.viewmodels.ContractOperationViewModel
import `in`.jadu.anju.farmer.viewmodels.FarmerListItemViewModel
import `in`.jadu.anju.farmer.viewmodels.WalletConnectViewModel
import `in`.jadu.anju.kvstorage.KvStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.web3j.protocol.core.methods.response.TransactionReceipt
import org.web3j.protocol.exceptions.TransactionException
import java.math.BigInteger
import java.util.Random
import javax.inject.Inject

@AndroidEntryPoint
class FarmerPreviewItemListFragment : Fragment() {
    private val farmerListItemViewModel: FarmerListItemViewModel by viewModels()
    private val contractOperationViewModel: ContractOperationViewModel by viewModels()
    private val walletConnectViewModel: WalletConnectViewModel by viewModels()
    private lateinit var binding: FragmentFarmerPreviewItemListBinding
    private var productName: String? = null
    private var productDescription: String? = null
    private var seedingDate: String? = null
    private var expiryDate: String? = null
    private var productPrice: String? = null
    private var productType: String? = null
    private var farmLocation:String?=null
    private var dateInMillis:Long = 0L
    private var getUri: String? = null
    private var isWalletCreated: Boolean = false
    private var snackBar: Snackbar? = null
    private var currentTimeInMillis:Long? = null
    private var productId:Long? = 0L
    private var bundle:Bundle? = null

    @Inject
    lateinit var KvStorage: KvStorage
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentFarmerPreviewItemListBinding.inflate(inflater, container, false)
        bundle = Bundle()
        setPreviewData()
        isWalletCreated = KvStorage.storageGetBoolean("isWalletCreated")
        binding.btnPreviewBtn.setOnClickListener {
            binding.lottieProgress.visibility = View.GONE
            binding.btnPreviewBtn.visibility = View.VISIBLE
            binding.btnPreviewBtn.text = getString(`in`.jadu.anju.R.string.processing)
            binding.btnPreviewBtn.isEnabled = false
            binding.farmerPreviewList.alpha = 0.5f
            binding.loadingTransactionLottie.visibility = View.VISIBLE
            currentTimeInMillis = System.currentTimeMillis()
            addProductBlock()
//            uploadDataToServer()
        }
        binding.btnEditFields.setOnClickListener {
            findNavController().navigate(R.id.action_farmerPreviewItemListFragment2_to_farmerListItemFragment2)
        }
        lifecycleScope.launch {
            farmerListItemViewModel.mainEvent.collect() { event ->
                when (event) {
                    is FarmerListItemViewModel.MainEvent.Error -> {
                        Toast.makeText(requireContext(), event.error, Toast.LENGTH_SHORT).show()
                        binding.lottieProgress.visibility = View.GONE
                        binding.btnPreviewBtn.visibility = View.VISIBLE
                        binding.btnPreviewBtn.isEnabled = true
                    }
                    is FarmerListItemViewModel.MainEvent.Success -> {
                        Toast.makeText(requireContext(), event.message, Toast.LENGTH_SHORT).show()
                        if(event.message == "Product Created Successfully"){
                            updateUiOnSuccessfulUpload()
                            findNavController().navigate(R.id.action_farmerPreviewItemListFragment2_to_successListingFragment,bundle)
                        }
                    }
                }
            }
        }
        return binding.root
    }
//    private fun setupContractOperation(privateKey: String) {
//        contractOperationViewModel.deployContract(privateKey,requireContext())
//    }


    private fun uploadDataToServer() {
        //upload Data to server
        val imagePart = farmerListItemViewModel.getImagePart(getUriFromPath(getUri!!), requireContext())
        val contractAddress = KvStorage.storageGetString("contractAddress")
        Log.d("imagepart", "uploadDataToServer: ${imagePart.body}")
        farmerListItemViewModel.createProductRemote(
            productType!!,
            productName!!,
            imagePart,
            productDescription!!,
            seedingDate!!,
            expiryDate!!,
            productId.toString(),
            contractAddress!!,
            productPrice!!
        )

    }
    private fun addProductBlock(){
            farmLocation?.let {
                productId = System.currentTimeMillis()
                addProductToBlockchain(BigInteger.valueOf(productId!!), it,BigInteger.valueOf(dateInMillis/1000))
                bundle = bundleOf("productId" to productId.toString())
                Log.d("randomId","$productId")
                Log.d("transactionSuccess","Trasactiondone")
            }
    }

    private  fun addProductToBlockchain(productId: BigInteger, farmLocation: String, plantingDate: BigInteger) {
        Log.d("transactionSuccessaddproduct","Trasaction")

        contractOperationViewModel.contractInstance.observe(requireActivity()){
            val contractInstance = it
            Log.d("transactionSuccesscontractx",contractInstance.toString())

                lifecycleScope.launch(Dispatchers.IO){
                    try {
                        val transactionReceipt: TransactionReceipt? =
                            contractInstance?.addProduct(productId, farmLocation, plantingDate)?.send()
                        if (transactionReceipt != null) {
                            val transactionHash = transactionReceipt.transactionHash
//                            updateUiOnTransactionSuccess()
                            uploadDataToServer()
                            Log.d("transactionSuccess", transactionHash)
                        } else {
                            // Transaction failed or was not mined yet
                            withContext(Dispatchers.Main){
                                updateUiOnTransactionError()
                                displayErrorSnackBar("Transaction Failed")
                            }
                            Log.d("transactionFailed","Trasaction")
                        }
                    }catch (e:TransactionException){
                        displayErrorSnackBar("Transaction Failed")
                        Log.d("transactionFailed",e.toString())
                        withContext(Dispatchers.Main){
                            updateUiOnTransactionError()
                        }
                    }
                }
        }
    }

    private fun updateUiOnTransactionError(){
        binding.btnPreviewBtn.visibility = View.VISIBLE
        binding.lottieProgress.visibility = View.GONE
        binding.btnPreviewBtn.isEnabled = true
        binding.btnPreviewBtn.text = "Retry"
        binding.farmerPreviewList.alpha = 1f
        binding.loadingTransactionLottie.visibility = View.GONE
    }

    private fun updateUiOnTransactionSuccess(){
        uploadDataToServer()
    }

    private fun updateUiOnSuccessfulUpload(){
        binding.btnPreviewBtn.visibility = View.VISIBLE
        binding.lottieProgress.visibility = View.GONE
        binding.btnPreviewBtn.isEnabled = true
        binding.btnPreviewBtn.text = "Success"
        binding.farmerPreviewList.alpha = 1f
        binding.loadingTransactionLottie.visibility = View.GONE
    }
    private fun getUriFromPath(path: String): Uri {
        return Uri.parse(path)
    }

    private fun displayErrorSnackBar(msg: String) {
        snackBar = Snackbar.make(
            requireActivity().findViewById(android.R.id.content),
            msg,
            Snackbar.LENGTH_SHORT
        )

        // Get the TextView from the Snackbar
//        val snackbarTextView: TextView? = snackBar?.view?.findViewById(com.google.android.material.R.id.snackbar_text)
//
//        // Apply the custom style to the TextView
//        snackbarTextView?.setTextAppearance(R.style.SnackbarText)

        snackBar?.setTextColor(ContextCompat.getColor(requireContext(), R.color.primary_text_color))
        snackBar?.setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.ErrorColor))
        snackBar?.show()
    }







    private fun setPreviewData() {
        productName = arguments?.getString("productName")
        productDescription = arguments?.getString("productDescription")
        seedingDate = arguments?.getString("harvestedDate")
        expiryDate = arguments?.getString("expiryDate")
        productPrice = arguments?.getString("productPrice")
        productType = arguments?.getString("productType")
        getUri = arguments?.getString("ImageUri")
        farmLocation = arguments?.getString("farmLocation")
        dateInMillis = arguments?.getLong("dateInMillis")!!

        Log.d("unixtime","$dateInMillis")
        binding.apply {
            tvProductType.text = productType
            tvProductName.setText(productName)
            tvProductDescription.setText(productDescription)
            tvHarvestedDate.setText(seedingDate)
            tvExpiryDate.setText(expiryDate)
            tvProductPrice.setText(productPrice)
            ivCustomimageselect.setImageURI(getUriFromPath(getUri!!))
            ivVegetables.setImageURI(getUriFromPath(getUri!!))
            tvFarmLocation.setText(farmLocation)

            tvProductName.keyListener = null
            tvProductDescription.keyListener = null
            tvHarvestedDate.keyListener = null
            tvExpiryDate.keyListener = null
            tvProductPrice.keyListener = null
            tvFarmLocation.keyListener = null

        }
    }
    private fun generateRandomId(): Long {
        val random = Random()
        while (true) {
            val randomId = random.nextInt(900000) + 100000
            val id = "101$randomId"

            if (id.length == 7) {
                return id.toLong()
            }
        }
    }

}