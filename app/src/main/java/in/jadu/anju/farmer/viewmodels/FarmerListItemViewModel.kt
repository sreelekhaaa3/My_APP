package `in`.jadu.anju.farmer.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.jadu.anju.farmer.models.dtos.FarmerAuth
import `in`.jadu.anju.farmer.models.repository.FarmerRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FarmerListItemViewModel @Inject constructor(private val farmerRepository: FarmerRepository):ViewModel() {
//
//    private val _apiStateFlow:MutableStateFlow<GetApiState> = MutableStateFlow(GetApiState.Empty)
//    val apiStateFlow:StateFlow<GetApiState> = _apiStateFlow

    private val _getVerificationData = MutableLiveData<FarmerAuth>()
    val phoneVerification: LiveData<FarmerAuth>
        get() = _getVerificationData

//    fun getPhone() = viewModelScope.launch {
//        farmerRepository.getPhone().onStart {
//            _apiStateFlow.value = GetApiState.Loading
//        }.catch {
//            _apiStateFlow.value = GetApiState.Error(it)
//        }.collect {
//            _apiStateFlow.value = GetApiState.Success(it)
//        }
//    }

    fun setPhone(phoneNo:String) = viewModelScope.launch(Dispatchers.IO) {
        farmerRepository.setPhone(phoneNo)
    }

    fun getPhone(phoneNo: String) = viewModelScope.launch(Dispatchers.IO) {
        _getVerificationData.postValue(farmerRepository.getPhone(phoneNo))
    }


}