package `in`.jadu.anju.farmer.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.jadu.anju.contract.javawrappers.`in`.jadu.anju.farmer.SupplyChainContract_sol_SupplyChainContract
import `in`.jadu.anju.kvstorage.KvStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.web3j.protocol.Web3j
import org.web3j.protocol.core.DefaultBlockParameterName
import org.web3j.protocol.http.HttpService
import org.web3j.tx.Contract
import org.web3j.tx.gas.DefaultGasProvider
import java.math.BigInteger
import javax.inject.Inject

@HiltViewModel
class ContractOperationViewModel @Inject constructor() : ViewModel() {
    private  var web3: Web3j = Web3j.build(HttpService("https://eth-sepolia.g.alchemy.com/v2/90tQ5woHcI8ey8xPwOe-apkcJV1PLXoy"))
    private lateinit var contractAddress: String
    private lateinit var credentials: org.web3j.crypto.Credentials
    private lateinit var kvStorage: KvStorage
    private val _contractOperationEventChannel = Channel<ContractOperationEvent>()
    private val gasLimit = BigInteger.valueOf(6721975)
    private val gasPrice = BigInteger.valueOf(20000000000)
    val contractOperationEvent = _contractOperationEventChannel.receiveAsFlow()

    fun deployContract(privateKey: String, context: Context) {
        credentials = org.web3j.crypto.Credentials.create(privateKey)
        kvStorage = KvStorage(context)
        viewModelScope.launch(Dispatchers.IO) {
            if (kvStorage.storageGetString("contractAddress")?.let { isContractDeployed(it) } == true) {
                contractAddress = kvStorage.storageGetString("contractAddress")!!
                loadContract(contractAddress)
                Log.d("retrieveContract", "Contract Retrieved Successfully")
                _contractOperationEventChannel.send( ContractOperationEvent.ContractOperationMessage("Contract Retrieved Successfully"))
            } else {
            val deployContract: SupplyChainContract_sol_SupplyChainContract =
                SupplyChainContract_sol_SupplyChainContract.deploy(
                    web3, credentials,
                    gasPrice,
                    gasLimit
                ).sendAsync().get()
            contractAddress = deployContract.contractAddress
            loadContract(contractAddress)
            _contractOperationEventChannel.send(ContractOperationEvent.ContractOperationMessage("Contract Deployed Successfully"))
            kvStorage.storageSetString("contractAddress", contractAddress)
            Log.d("contractAddress", contractAddress)
            }
        }



    }

    private fun loadContract(contractAddress: String) {
        if(contractAddress.isEmpty()) return
        else{
            val contract: SupplyChainContract_sol_SupplyChainContract =
                SupplyChainContract_sol_SupplyChainContract.load(
                    contractAddress, web3, credentials,
                    gasPrice,
                    gasLimit
                )
            viewModelScope.launch(Dispatchers.IO) {
                Log.d("isValid", contract.isValid.toString())
            }
        }

    }

    private fun isContractDeployed(contractAddress: String): Boolean {
        return if (contractAddress.isEmpty()) false
        else{
            val isDeployed: Boolean
            val ethGetCode = web3.ethGetCode(contractAddress, DefaultBlockParameterName.LATEST).send()
            isDeployed = !ethGetCode.hasError() && ethGetCode.code != "0x"
            isDeployed
        }

    }

    sealed class ContractOperationEvent {
        data class ContractOperationMessage(val message: String) : ContractOperationEvent()
        data class ContractOperationError(val message: String) : ContractOperationEvent()
    }

}