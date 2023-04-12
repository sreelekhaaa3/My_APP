package `in`.jadu.anju.consumer.models.remote

import `in`.jadu.anju.consumer.models.dtos.ConsumerAuth
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ConsumerApiService {
    companion object{
        const val BASE_URL = "http://192.168.1.7:5000/"
    }

    @FormUrlEncoded
    @POST("api/user/signup")
    suspend fun setPhone(@Field("phoneNo") phoneNo: String):ConsumerAuth



}