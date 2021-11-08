package com.santi.rickymortyapi.di

import android.content.Context
import com.santi.rickymortyapi.data.MainRepository
import com.santi.rickymortyapi.data.local.AppDatabase
import com.santi.rickymortyapi.data.local.CharacterDao
import com.santi.rickymortyapi.network.RetrofitService
import com.santi.rickymortyapi.utils.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)

@Module
object AppModule{

    @Provides
    fun providesBaseUrl() : String{
        return BASE_URL
    }

    @Provides
    fun providesHttpLoggingInterceptor() : HttpLoggingInterceptor{
        return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    @Provides
    fun providesOkhttpClient(interceptor : HttpLoggingInterceptor) : OkHttpClient{
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .callTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
        return okHttpClient.build()
    }

    @Provides
    fun providesConverterFactory() : Converter.Factory{
        return GsonConverterFactory.create()
    }

    @Provides
    fun providesRetrofit(baseUrl: String, converterFactory: Converter.Factory, client: OkHttpClient) : Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(converterFactory)
            .client(client)
            .build()
    }
    @Provides
    fun providesRetrofitService(retrofit: Retrofit) : RetrofitService{
        return retrofit.create(RetrofitService::class.java)
    }

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext appContext: Context) = AppDatabase.getDatabase(appContext)

    @Singleton
    @Provides
    fun provideCharacterDao(db: AppDatabase) = db.characterDao()

    @Singleton
    @Provides
    fun providesMainRepository(retrofitService: RetrofitService, characterDao: CharacterDao): MainRepository =
        MainRepository(retrofitService, characterDao)
}
