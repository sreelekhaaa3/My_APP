package `in`.jadu.anju.farmer.ui.fragments

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import `in`.jadu.anju.R
import `in`.jadu.anju.databinding.FragmentFarmerListItemBinding

class FarmerListItemFragment : Fragment() {
    private lateinit var binding: FragmentFarmerListItemBinding
    private lateinit var itemList: List<CardView>
    private val PICK_IMAGE_REQUEST = 1
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentFarmerListItemBinding.inflate(inflater, container, false)
        binding.btnPreviewBtn.setOnClickListener {
            findNavController().navigate(R.id.action_farmerListItemFragment_to_farmerPreviewItemListFragment)
        }
        selectProductType()
        binding.cvCustomimage.setOnClickListener {
            openGallery()
        }
        binding.btnPreviewBtn.setOnClickListener {
            if (isFieldsEmpty()) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.Fields_not_empty),
                    Toast.LENGTH_SHORT
                ).show()
                binding.productName.error = "Enter Product Name"
                binding.productDescription.error = "Enter Product Description"
                binding.harvestedDate.error = "Enter Harvested Date"
                binding.expiryDate.error = "Enter Expiry Date"
                binding.productPrice.error = "Enter Product Price"
            } else {

            }
        }

        return binding.root
    }

    private fun selectProductType() {
        binding.fragmentProductType.apply {
            itemList = listOf(
                listItem01,
                listItem02,
                listItem03,
                listItem04,
                listItem05,
                listItem06,
                listItem07
            )
        }
        var selectedCardViewIndex: Int = -1
        itemList.forEachIndexed { index, itemView ->
            itemView.setOnClickListener {
                if (selectedCardViewIndex == index) {
                    // if the card view is already selected, reset its background
                    itemView.background =
                        ContextCompat.getDrawable(requireContext(), R.drawable.resetcardviewborder)
                    itemView.cardElevation = 10f
                    selectedCardViewIndex = -1
                } else {
                    // deselect the previously selected card view
                    selectedCardViewIndex.takeIf { it != -1 }?.let {
                        itemList[it].background = ContextCompat.getDrawable(
                            requireContext(),
                            R.drawable.resetcardviewborder
                        )
                        itemList[it].cardElevation = 10f
                    }
                    // select the current card view and set its background to the border drawable
                    itemView.background =
                        ContextCompat.getDrawable(requireContext(), R.drawable.cardviewborder)
                    itemView.cardElevation = 20f
                    selectedCardViewIndex = index
                    getAllFieldsData(index)
                }
            }
        }
        Log.d("TAG", "selectProductType: $selectedCardViewIndex")
    }

    private fun updateItemSelected(itemViewPosition: Int): Int {
        return itemViewPosition
    }

    private fun isFieldsEmpty(): Boolean {
        return binding.productName.text.toString().isEmpty() ||
                binding.productDescription.text.toString().isEmpty() ||
                binding.harvestedDate.text.toString().isEmpty() ||
                binding.expiryDate.text.toString().isEmpty() ||
                binding.productPrice.text.toString().isEmpty()
    }

    private fun getAllFieldsData(index: Int) {
        val productName = binding.productName.text.toString()
        val productDescription = binding.productDescription.text.toString()
        val harvestedDate = binding.harvestedDate.text.toString()
        val expiryDate = binding.expiryDate.text.toString()
        val productPrice = binding.productPrice.text.toString()
        var productType = ""

        when (index) {
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
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            val selectedImageUri = data.data
            binding.ivCustomimageselect.setImageResource(R.drawable.tick_icon)
            binding.tvClicktoupload.text = "Image Uploaded"
            //cvcustomimage should be disabled
            binding.cvCustomimage.isEnabled = false
        }
    }

}