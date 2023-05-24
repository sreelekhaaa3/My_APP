package `in`.jadu.anju.farmer.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import `in` .jadu.anju.R
import `in`.jadu.anju.databinding.FragmentFarmerProfileBinding


class FarmerProfileFragment : Fragment() {
    private lateinit var binding:FragmentFarmerProfileBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFarmerProfileBinding.inflate(layoutInflater,container,false)
        // Inflate the layout for this fragment
        return binding.root
    }

}