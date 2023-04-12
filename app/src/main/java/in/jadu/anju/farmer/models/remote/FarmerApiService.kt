package `in`.jadu.anju.farmer.models.remote

import `in`.jadu.anju.consumer.models.dtos.ConsumerAuth
import `in`.jadu.anju.farmer.models.dtos.FarmerAuth
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface FarmerApiService {
    companion object{
        const val BASE_URL = "http://192.168.1.7:5000/"
    }

    @GET("/api/seller/signup")
    suspend fun getPhone():List<FarmerAuth>

    @FormUrlEncoded
    @POST("api/seller/signup")
    suspend fun setPhone(@Field("phoneNo") phoneNo: String):FarmerAuth



}