package `in`.jadu.anju.farmer.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.jadu.anju.farmer.models.repository.FarmerRepository
import `in`.jadu.anju.farmer.models.util.GetApiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FarmerListItemViewModel @Inject constructor(private val farmerRepository: FarmerRepository):ViewModel() {

    private val _apiStateFlow:MutableStateFlow<GetApiState> = MutableStateFlow(GetApiState.Empty)
    val apiStateFlow:StateFlow<GetApiState> = _apiStateFlow

    fun getPhone() = viewModelScope.launch {
        farmerRepository.getPhone().onStart {
            _apiStateFlow.value = GetApiState.Loading
        }.catch {
            _apiStateFlow.value = GetApiState.Error(it)
        }.collect {
            _apiStateFlow.value = GetApiState.Success(it)
        }
    }

    fun setPhone(phoneNo:String) = farmerRepository.setPhone(phoneNo)

}