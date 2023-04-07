package `in`.jadu.anju.commonuis

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import `in`.jadu.anju.R
import `in`.jadu.anju.databinding.FragmentSelectRoleBinding
import `in`.jadu.anju.kvstorage.KvStorage
import `in`.jadu.anju.utils.Constants
import javax.inject.Inject

class SelectRoleFragment : Fragment() {
    private lateinit var binding: FragmentSelectRoleBinding
    @Inject lateinit var kvStorage: KvStorage
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSelectRoleBinding.inflate(inflater,container,false)
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        binding.btnBuyers.setOnClickListener {
            auth.signOut()
        }
        btnBuyers()
        btnFarmers()
        return binding.root
    }
    private fun btnBuyers(){
        binding.btnBuyers.setOnClickListener {
            kvStorage.storageSetString(Constants.userSelectedRole,"buyer")
            //navigate to buyers fragment
        }
    }

    private fun btnFarmers(){
        binding.btnFarmers.setOnClickListener {
            kvStorage.storageSetString(Constants.userSelectedRole,"farmer")
            //navigate to sellers fragment
        }
    }


}