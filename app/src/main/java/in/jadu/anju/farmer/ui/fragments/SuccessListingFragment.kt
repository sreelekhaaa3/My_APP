package `in`.jadu.anju.farmer.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import `in`.jadu.anju.R
import `in`.jadu.anju.databinding.FragmentSuccessListingBinding


class SuccessListingFragment : Fragment() {
    private lateinit var binding: FragmentSuccessListingBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSuccessListingBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        binding.btnSuccess.setOnClickListener {
            findNavController().navigate(R.id.action_successListingFragment_to_farmerDashBoardFragment2)
        }
        return binding.root
    }


}