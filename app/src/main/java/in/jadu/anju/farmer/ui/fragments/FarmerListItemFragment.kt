package `in`.jadu.anju.farmer.ui.fragments

import android.Manifest
import android.content.Intent
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.card.MaterialCardView
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import `in`.jadu.anju.R
import `in`.jadu.anju.databinding.FragmentFarmerListItemBinding
import `in`.jadu.anju.farmer.models.dtos.ListItemTypes
import `in`.jadu.anju.farmer.viewmodels.ContractOperationViewModel
import `in`.jadu.anju.farmer.viewmodels.FarmerListItemViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class FarmerListItemFragment : Fragment() {
    private lateinit var binding: FragmentFarmerListItemBinding
    private lateinit var itemList: List<CardView>
    private lateinit var auth: FirebaseAuth
    private var dateInMillis:Long = 0L
    private val contractOperationViewModel: ContractOperationViewModel by viewModels()
    private val farmerListItemViewModel: FarmerListItemViewModel by viewModels()
    private var bundle = Bundle()
    private var _isImageImported = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        contractOperationViewModel
        binding = FragmentFarmerListItemBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()
        selectProductType()

        binding.harvestedDate.setOnClickListener {
            getSeedingDate(binding.harvestedDate)
        }
        binding.expiryDate.setOnClickListener {
            getExpiryDate(binding.expiryDate)
        }
        binding.PreviewAddedProduct.setOnClickListener {
            if (isFieldsEmpty()) {
                Toast.makeText(
                    requireContext(), getString(R.string.Fields_not_empty), Toast.LENGTH_SHORT
                ).show()
//                isDescriptionEmpty()
//                isProductNameEmpty()
//                isHarvestedDateEmpty()
//                isExpiryDateEmpty()
//                isProductPriceEmpty()
//                isFarmLocationEmpty()
//                isImageNotSelected()
                binding.productName.error = "Enter Product Name"
                binding.productDescription.error = "Enter Product Description"
                binding.harvestedDate.error = "Enter Harvested Date"
                binding.expiryDate.error = "Enter Expiry Date"
                binding.productPrice.error = "Enter Product Price"
                binding.farmLocation.error = "Enter Farm Location"
                binding.warningImgNotSelected.visibility = View.VISIBLE
            } else {
                productInfo()
                findNavController().navigate(
                    R.id.action_farmerListItemFragment2_to_farmerPreviewItemListFragment2, bundle
                )
            }
        }
        val pickMedia = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                Log.d("PhotoPicker", "Selected URI: $uri")
                farmerListItemViewModel.setUri(uri)
                binding.ivCustomimageselect.setImageURI(uri)
                binding.tvClicktoupload.text = getString(R.string.image_uploaded)
            } else {
                Log.d("PhotoPicker", "No media selected")
            }
        }
        binding.cvCustomimage.setOnClickListener {
            if(!farmerListItemViewModel.isManageStoragePermissionGranted(requireContext())){
                requestPermission()
            }else{
                _isImageImported = true
                pickMedia.launch("image/*")
            }
        }
        binding.detectLocation.setOnClickListener {
            if(!farmerListItemViewModel.checkLocationPermission()){
              requestPermissionLauncherForLocation.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }else{
                farmerListItemViewModel.getLastLocation()
                binding.farmLocation.setText(farmerListItemViewModel.locality)
            }
        }

        return binding.root
    }


    private fun selectProductType() {
        binding.fragmentProductType.apply {
            itemList = listOf(
                listItem01, listItem02, listItem03, listItem04, listItem05, listItem06, listItem07
            )
        }
        var selectedCardViewIndex: Int = -1
        itemList.forEachIndexed { index, itemView ->
            itemView.setOnClickListener {
                val cardview = itemView as MaterialCardView
                if (selectedCardViewIndex == index) {
                    // if the card view is already selected, reset its background
                    itemView.background = ContextCompat.getDrawable(requireContext(), R.drawable.resetcardviewborder)
                    selectedCardViewIndex = -1
                    cardview.isChecked = false
                } else {
                    // deselect the previously selected card view
                    selectedCardViewIndex.takeIf { it != -1 }?.let { prevIndex ->
                        val prevView = itemList[prevIndex]
                        prevView.background = ContextCompat.getDrawable(
                            requireContext(), R.drawable.resetcardviewborder
                        )
                        (prevView as? MaterialCardView)?.isChecked = false
                    }
                    // select the current card view and set its background to the border drawable
                    itemView.background = ContextCompat.getDrawable(requireContext(), R.drawable.cardviewborder)
                    selectedCardViewIndex = index
                    farmerListItemViewModel.setIndex(index)
                    cardview.isChecked = true
                }
            }
        }
        Log.d("TAG", "selectProductType: $selectedCardViewIndex")
    }

    private fun updateItemSelected(itemViewPosition: Int): Int {
        return itemViewPosition
    }

    private fun isFieldsEmpty(): Boolean {
        return binding.productName.text.toString()
            .isEmpty() || binding.productDescription.text.toString()
            .isEmpty() || binding.harvestedDate.text.toString()
            .isEmpty() || binding.expiryDate.text.toString()
            .isEmpty() || binding.productPrice.text.toString()
            .isEmpty() || binding.farmLocation.text.toString().isEmpty() || !_isImageImported
    }

    private fun isDescriptionEmpty(){
        if(binding.productDescription.text.toString().isEmpty()){
            binding.productNameInputLayout.error = "Enter Product Description"
        }
    }

    private fun isProductNameEmpty(){
        if(binding.productName.text.toString().isEmpty()){
            binding.productNameInputLayout.error = "Enter Product Name"
        }
    }

    private fun isHarvestedDateEmpty(){
        if(binding.harvestedDate.text.toString().isEmpty()){
            binding.harvestedDateInputLayout.error = "Enter Harvested Date"
        }
    }

    private fun isExpiryDateEmpty(){
        if(binding.expiryDate.text.toString().isEmpty()){
            binding.expiryDateInputLayout.error = "Enter Expiry Date"
        }
    }

    private fun isProductPriceEmpty(){
        if(binding.productPrice.text.toString().isEmpty()){
            binding.productPriceInputLayout.error = "Enter Product Price"
        }
    }

    private fun isFarmLocationEmpty(){
        if(binding.farmLocation.text.toString().isEmpty()){
            binding.farmLocationInputLayout.error = "Enter Farm Location"
        }
    }

    private fun isImageNotSelected(){
        if(!_isImageImported){
            binding.warningImgNotSelected.visibility = View.VISIBLE
        }
    }


    private fun productInfo() {
        val productName = binding.productName.text.toString()
        val productDescription = binding.productDescription.text.toString()
        val harvestedDate = binding.harvestedDate.text.toString()
        val expiryDate = binding.expiryDate.text.toString()
        val productPrice = binding.productPrice.text.toString()
        var productType = ""
        val farmLocation = binding.farmLocation.text.toString()
        //get user phone number

        when (farmerListItemViewModel.getIndex()) {
            0 -> {
                productType = "Vegetables"
            }

            1 -> {
                productType = "Fruits"
            }

            2 -> {
                productType = "Handloom"
            }

            3 -> {
                productType = "Manures"
            }

            4 -> {
                productType = "Dairy"
            }

            5 -> {
                productType = "Poultry"
            }

            6 -> {
                productType = "Others"
            }
        }

        bundle = bundleOf(
            "productName" to productName,
            "productDescription" to productDescription,
            "harvestedDate" to harvestedDate,
            "expiryDate" to expiryDate,
            "productPrice" to productPrice,
            "productType" to productType,
            "farmLocation" to farmLocation,
            "dateInMillis" to dateInMillis,
            "ImageUri" to farmerListItemViewModel.getUri().toString(),
        )


//        insertData to local
        lifecycleScope.launch(Dispatchers.IO) {
            farmerListItemViewModel.insertListItemTypes(
                ListItemTypes(
                    productType,
                    productName,
                    farmerListItemViewModel.getUri().toString(),
                    productDescription,
                    harvestedDate,
                    expiryDate,
                    productPrice,
                    auth.currentUser?.phoneNumber.toString()
                )
            )
        }
    }

    private fun getSeedingDate(view: TextInputEditText) {
        val today = MaterialDatePicker.todayInUtcMilliseconds()
        val constraintsBuilder = CalendarConstraints.Builder()
        constraintsBuilder.setValidator(DateValidatorPointBackward.before(today))

        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setCalendarConstraints(constraintsBuilder.build())
            .setTitleText(R.string.select_planting_date)
            .build()

        datePicker.show(parentFragmentManager, "DatePicker")
        datePicker.addOnPositiveButtonClickListener { selection ->
            if (selection != null) {
                dateInMillis = selection
                view.setText(datePicker.headerText)
            }
        }
    }

    private fun getExpiryDate(view: TextInputEditText) {
        val today = MaterialDatePicker.todayInUtcMilliseconds()

        val constraintsBuilder = CalendarConstraints.Builder()
        constraintsBuilder.setValidator(DateValidatorPointForward.from(today))

        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setCalendarConstraints(constraintsBuilder.build())
            .setTitleText(R.string.select_expiry_date)
            .build()

        datePicker.show(parentFragmentManager, "DatePicker")
        datePicker.addOnPositiveButtonClickListener { selection ->
            if (selection != null) {
                view.setText(datePicker.headerText)
            }
        }
    }

    private fun requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            try {
                val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                intent.addCategory("android.intent.category.DEFAULT")
                intent.data =
                    Uri.parse(String.format("package:%s", requireActivity().packageName))
                startActivity(intent, Bundle.EMPTY)
            } catch (e: Exception) {
                val intent = Intent()
                intent.action = Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION
                startActivity(intent)
            }
        } else {
            requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            Toast.makeText(requireContext(), "Permission Granted", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), getString(R.string.deny_perm_text), Toast.LENGTH_SHORT)
                .show()
        }
    }

    private val requestPermissionLauncherForLocation = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            farmerListItemViewModel.getLastLocation()
            binding.farmLocation.setText(farmerListItemViewModel.locality)
            Toast.makeText(requireContext(), "Location Detected", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), getString(R.string.deny_perm_text), Toast.LENGTH_SHORT)
                .show()
        }
    }
}