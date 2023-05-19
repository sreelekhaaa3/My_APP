package `in`.jadu.anju.farmer.models.repository

import `in`.jadu.anju.farmer.models.dtos.FarmerAuth
import `in`.jadu.anju.farmer.models.remote.FarmerApiService
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class FarmerRepository @Inject constructor(private val farmerApiService: FarmerApiService) {

    suspend fun farmerProductList(phoneNo: String) = farmerApiService.getFarmerProductList(phoneNo)
    suspend fun setPhone(phoneNo: String) = farmerApiService.setPhone(FarmerAuth(phoneNo))

    suspend fun createProduct(
        productType: String,
        productName: String,
        productImageUri: MultipartBody.Part,
        description: String,
        productPacked: String,
        productExpire: String,
        web3Id: String,
        contractAddress: String,
        productPrice: String,
        phone: String
    ) = farmerApiService.createProduct(
            productType,
            productName,
            productImageUri,
            description,
            productPacked,
            productExpire,
            web3Id,
            contractAddress,
            productPrice,
            phone,
            RequestBody.create("text/plain".toMediaTypeOrNull(), phone)
    )
}
