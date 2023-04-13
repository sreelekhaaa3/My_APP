package `in`.jadu.anju.farmer.models.remote

import `in`.jadu.anju.farmer.models.dtos.FarmerAuth
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface FarmerApiService {
    companion object{
        const val BASE_URL = "http://192.168.1.4:5000/"
    }

    @GET("/api/seller/login")
    suspend fun getPhone(phoneNo: String):FarmerAuth


    @POST("api/seller/signup")
    suspend fun setPhone(@Body FarmerAuth: FarmerAuth)



}