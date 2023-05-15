package `in`.jadu.anju.farmer.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import `in`.jadu.anju.R
import `in`.jadu.anju.databinding.FragmentAllOrdersBinding


class AllOrdersFragment : Fragment() {
    private lateinit var binding : FragmentAllOrdersBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding  = FragmentAllOrdersBinding.inflate(inflater,container,false)
        return binding.root
    }
}