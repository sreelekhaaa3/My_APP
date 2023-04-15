package `in`.jadu.anju.farmer.ui.fragments

import android.content.ClipData
import android.content.Context
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import `in`.jadu.anju.R
import `in`.jadu.anju.databinding.FragmentWalletConnectBinding
import `in`.jadu.anju.farmer.viewmodels.WalletConnectViewModel
import `in`.jadu.anju.kvstorage.KvStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.web3j.crypto.Credentials
import org.web3j.protocol.Web3j
import org.web3j.protocol.http.HttpService
import java.io.File
import java.security.Provider
import java.security.Security
import javax.inject.Inject

@AndroidEntryPoint
class WalletConnectFragment : Fragment() {
    private lateinit var binding: FragmentWalletConnectBinding
    private val walletConnectViewModel: WalletConnectViewModel by viewModels()
    private lateinit var KvStorage: KvStorage
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWalletConnectBinding.inflate(inflater, container, false)
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        setUpWallet()
        handleEvent()
        KvStorage = KvStorage(requireContext())
        return binding.root
    }

    private fun handleEvent(){
        lifecycleScope.launch{
            walletConnectViewModel.walletConnectEvent.collect { event ->
                when(event){
                    is WalletConnectViewModel.WalletConnectEvent.ConnectToWalletMessage -> {
                        Toast.makeText(requireContext(), event.message, Toast.LENGTH_SHORT).show()
                        hideUIinStart()
                    }
                    is WalletConnectViewModel.WalletConnectEvent.ConnectToWalletError -> {
                        Toast.makeText(requireContext(), event.error, Toast.LENGTH_SHORT).show()
                        hideUIinStart()
                    }
                    is WalletConnectViewModel.WalletConnectEvent.WalletCreatedSuccessfully -> {
                        Toast.makeText(requireContext(), "Wallet Created Successfully", Toast.LENGTH_SHORT).show()
                        updateSuccessUI(event.message)
                    }
                    is WalletConnectViewModel.WalletConnectEvent.WalletCreatedError -> {
                        Toast.makeText(requireContext(), event.error, Toast.LENGTH_SHORT).show()
                        Log.d("WalletConnectFragment", "handleEvent: ${event.error}")
                        hideUIinStart()
                    }

                    else -> {
                        Toast.makeText(requireContext(), "someThing is wrong", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun setUpWallet(){
        binding.btnCreateWallet.setOnClickListener {
            binding.progressWalletConnect.visibility = View.VISIBLE
            binding.btnCreateWallet.visibility = View.INVISIBLE
            walletConnectViewModel.createWallet(requireContext(),binding.etEnterWalletName.text.toString(),binding.etEnterWalletPassword.text.toString())
        }
    }

    private fun hideUIinStart(){
        binding.progressWalletConnect.visibility = View.INVISIBLE
        binding.btnCreateWallet.visibility = View.VISIBLE
        binding.llWalletCreated.visibility = View.GONE
    }

    private fun updateSuccessUI(message:String){
        binding.progressWalletConnect.visibility = View.GONE
        binding.btnCreateWallet.visibility = View.VISIBLE
        binding.llWalletCreated.visibility = View.VISIBLE
        binding.tvWalletNameValue.text = binding.etEnterWalletName.text.toString()
        binding.tvWalletPasswordValue.text = binding.etEnterWalletPassword.text.toString()
        binding.tvWalletAddressTextValue.text = message
        moveToWalletFragment(message)
    }

    private fun copyAddress(address:String){
        val clipboardManager = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
        val clipData = ClipData.newPlainText("text", address)
        clipboardManager.setPrimaryClip(clipData)
        Toast.makeText(requireContext(), "Copied to clipboard", Toast.LENGTH_SHORT).show()
    }

    private fun moveToWalletFragment(message: String){

        KvStorage.storageSetString("walletName", binding.etEnterWalletName.text.toString())
        KvStorage.storageSetString("walletPassword", binding.etEnterWalletPassword.text.toString())
        KvStorage.storageSetString("walletAddress", message)
        KvStorage.storageSetBoolean("isWalletCreated", true)

        binding.btnCopyAddress.setOnClickListener {
            binding.processingToWallet.visibility = View.VISIBLE
            binding.btnCopyAddress.visibility = View.INVISIBLE
            copyAddress(message)
            findNavController().navigate(R.id.action_walletConnectFragment_to_walletFragment)
        }
    }

}