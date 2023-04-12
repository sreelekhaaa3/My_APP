package `in`.jadu.anju.modules

import android.app.Application
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import `in`.jadu.anju.commonuis.viewmodels.PhoneVerificationViewModel
import `in`.jadu.anju.consumer.models.remote.ConsumerApiService
import `in`.jadu.anju.farmer.models.dtos.FarmerAuth
import `in`.jadu.anju.farmer.models.remote.FarmerApiService
import `in`.jadu.anju.kvstorage.KvStorage
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Modules {

    @Provides
    fun kvStorage(application:Application):KvStorage{
        return KvStorage(application.applicationContext)
    }

    @Provides
    @Singleton
    fun providePhoneVerificationViewModelFactory(): PhoneVerificationViewModel {
        return PhoneVerificationViewModel()
    }

    @Provides
    @Singleton
    fun provideConsumerApiService():ConsumerApiService = Retrofit.Builder().baseUrl(ConsumerApiService.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ConsumerApiService::class.java)

    @Provides
    @Singleton
    fun provideFarmerApiService():FarmerApiService = Retrofit.Builder().baseUrl(FarmerApiService.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(FarmerApiService::class.java)
}