package `in`.jadu.anju.farmer.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import `in`.jadu.anju.R
import `in`.jadu.anju.databinding.FragmentFarmerDashBoardBinding


class FarmerDashBoardFragment : Fragment() {

    private lateinit var binding : FragmentFarmerDashBoardBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFarmerDashBoardBinding.inflate(inflater,container,false)
        binding.btnAddItem.setOnClickListener {
            findNavController().navigate(R.id.action_farmerDashBoardFragment_to_farmerListItemFragment)
        }
        return binding.root
    }

}