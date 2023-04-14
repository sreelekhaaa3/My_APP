package `in`.jadu.anju.farmer.models.repository

import android.app.LauncherActivity.ListItem
import `in`.jadu.anju.farmer.models.dtos.FarmerAuth
import `in`.jadu.anju.farmer.models.dtos.ListItemTypes
import `in`.jadu.anju.farmer.models.dtos.ListItemTypesRemote
import `in`.jadu.anju.farmer.models.remote.FarmerApiService
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class FarmerRepository @Inject constructor(private val farmerApiService: FarmerApiService) {

    suspend fun getPhone(phoneNo: String) = farmerApiService.getPhone(phoneNo)
    suspend fun setPhone(phoneNo: String) = farmerApiService.setPhone(FarmerAuth(phoneNo))

    suspend fun createProduct(
        productType: String,
        productName: String,
        productImageUri: MultipartBody.Part,
        description: String,
        productPacked: String,
        productExpire: String,
        productPrice: String,
        phone: String
    ) = farmerApiService.createProduct(
            productType,
            productName,
            productImageUri,
            description,
            productPacked,
            productExpire,
            productPrice,
            phone,
            RequestBody.create(MediaType.parse("text/plain"), phone)
    )
}
