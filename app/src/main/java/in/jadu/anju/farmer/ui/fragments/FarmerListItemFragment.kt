package `in`.jadu.anju.farmer.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import `in`.jadu.anju.R
import `in`.jadu.anju.databinding.FragmentFarmerListItemBinding

class FarmerListItemFragment : Fragment() {
    private lateinit var binding : FragmentFarmerListItemBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentFarmerListItemBinding.inflate(inflater,container,false)
        binding.btnPreviewBtn.setOnClickListener {
            findNavController().navigate(R.id.action_farmerListItemFragment_to_farmerPreviewItemListFragment)
        }

        return binding.root
    }

}