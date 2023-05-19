package `in`.jadu.anju.farmer.models.remote

import `in`.jadu.anju.farmer.models.dtos.FarmerAuth
import `in`.jadu.anju.farmer.models.dtos.ListItemTypes
import `in`.jadu.anju.farmer.models.dtos.Product
import `in`.jadu.anju.farmer.models.dtos.RemoteListTypeBackend
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.Response
import retrofit2.http.*

interface FarmerApiService {
    companion object {
        const val BASE_URL = "http://34.131.170.232"
    }

    @GET("/api/product/{phoneNo}")
    suspend fun getFarmerProductList(@Path("phoneNo") phoneNo: String): retrofit2.Response<RemoteListTypeBackend>


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
        @Part("web3Id") web3Id: String,
        @Part("contractAddress") contractAddress: String,
        @Part("productPrice") productPrice: String,
        @Path("sellerPhoneNo") sellerPhoneNo: String,
        @Part("sellerPhoneNo") sellerPhoneNoPart: RequestBody
    )


}