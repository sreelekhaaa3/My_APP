package `in`.jadu.anju.farmer.models.repository

import `in`.jadu.anju.farmer.models.dtos.FarmerAuth
import `in`.jadu.anju.farmer.models.remote.FarmerApiService
import javax.inject.Inject

class FarmerRepository @Inject constructor(private val farmerApiService: FarmerApiService) {

    suspend fun getPhone(phoneNo: String) = farmerApiService.getPhone(phoneNo)
    suspend fun setPhone(phoneNo:String) = farmerApiService.setPhone(FarmerAuth(phoneNo))
}
