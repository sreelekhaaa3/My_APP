package `in`.jadu.anju.modules

import android.app.Application
import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import `in`.jadu.anju.commonuis.viewmodels.PhoneVerificationViewModel
import `in`.jadu.anju.farmer.models.local.ItemListDatabase
import `in`.jadu.anju.farmer.models.local.ListItemTypesDao
import `in`.jadu.anju.farmer.models.local.LocalDataInterface
import `in`.jadu.anju.farmer.models.remote.FarmerApiService
import `in`.jadu.anju.farmer.models.repository.LocalDataRepository
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

//    @Provides
//    @Singleton
//    fun provideConsumerApiService():ConsumerApiService = Retrofit.Builder().baseUrl(ConsumerApiService.BASE_URL)
//        .addConverterFactory(GsonConverterFactory.create())
//        .build()
//        .create(ConsumerApiService::class.java)

    @Provides
    @Singleton
    fun provideFarmerApiService():FarmerApiService = Retrofit.Builder().baseUrl(FarmerApiService.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(FarmerApiService::class.java)

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): ItemListDatabase {
        return Room.databaseBuilder(
            context,
            ItemListDatabase::class.java,
            "ItemListDatabase"
        ).build()
    }
    @Provides
    fun provideListItemTypesDao(database: ItemListDatabase): ListItemTypesDao {
        return database.listItemDao()
    }

    @Provides
    fun provideLocalDataInterface(listItemTypesDao: ListItemTypesDao): LocalDataInterface {
        return LocalDataRepository(listItemTypesDao)
    }


    //provide LocalDataInterface



}