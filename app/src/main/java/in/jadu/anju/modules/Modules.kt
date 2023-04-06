package `in`.jadu.anju.modules

import android.app.Application
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import `in`.jadu.anju.kvstorage.KvStorage
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Modules {

    @Provides
    fun kvStorage(application:Application):KvStorage{
        return KvStorage(application.applicationContext)
    }

}