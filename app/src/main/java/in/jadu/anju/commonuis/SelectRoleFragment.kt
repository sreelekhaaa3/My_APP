package `in`.jadu.anju.commonuis

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import `in`.jadu.anju.R
import `in`.jadu.anju.databinding.FragmentSelectRoleBinding

class SelectRoleFragment : Fragment() {
    private lateinit var binding: FragmentSelectRoleBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSelectRoleBinding.inflate(inflater,container,false)

        return binding.root
    }


}