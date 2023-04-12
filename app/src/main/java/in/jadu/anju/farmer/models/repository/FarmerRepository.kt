package `in`.jadu.anju.farmer.models.repository

import `in`.jadu.anju.commonuis.viewmodels.PhoneVerificationViewModel
import `in`.jadu.anju.consumer.models.dtos.ConsumerAuth
import `in`.jadu.anju.farmer.models.dtos.FarmerAuth
import `in`.jadu.anju.farmer.models.remote.FarmerApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.util.concurrent.Flow
import javax.inject.Inject

class FarmerRepository @Inject constructor(private val farmerApiService: FarmerApiService) {

    fun getPhone():kotlinx.coroutines.flow.Flow<List<FarmerAuth>> = flow {
        emit(farmerApiService.getPhone())
    }.flowOn(Dispatchers.IO)

    fun setPhone(phoneNo:String):kotlinx.coroutines.flow.Flow<FarmerAuth> = flow {
        emit(farmerApiService.setPhone(phoneNo))
    }.flowOn(Dispatchers.IO)
}
