package `in`.jadu.anju.farmer.models.remote

import `in`.jadu.anju.farmer.models.dtos.FarmerAuth
import `in`.jadu.anju.farmer.models.dtos.ListItemTypes
import `in`.jadu.anju.farmer.models.dtos.ListItemTypesRemote
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface FarmerApiService {
    companion object {
        const val BASE_URL = "http://192.168.1.4:5000/"
    }

    @GET("/api/seller/{phoneNo}")
    suspend fun getPhone(@Path("phoneNo") phoneNo: String): FarmerAuth


    @POST("api/seller/auth")
    suspend fun setPhone(@Body FarmerAuth: FarmerAuth)
    @Multipart
    @POST("api/product/{sellerPhoneNo}/createProduct")
    suspend fun createProduct(
        @Part("productType") productType: String,
        @Part("productName") productName: String,
        @Part productImageUrl : MultipartBody.Part,
        @Part("description") description: String,
        @Part("productPacked") productPacked: String,
        @Part("productExpire") productExpire: String,
        @Part("productPrice") productPrice: String,
        @Path("sellerPhoneNo") sellerPhoneNo: String,
        @Part("sellerPhoneNo") sellerPhoneNoPart: RequestBody
    )


}