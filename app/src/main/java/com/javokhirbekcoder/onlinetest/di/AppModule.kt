package com.javokhirbekcoder.onlinetest.di

import android.content.Context
import com.javokhirbekcoder.onlinetest.api.ApiService
import com.javokhirbekcoder.onlinetest.utils.NetworkStateListener
import com.javokhirbekcoder.onlinetest.utils.SharedPrefs
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/*
Created by Javokhirbek on 28/11/2023 at 10:00
*/


@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    private val BASE_URL = "https://quiz.onlinegroup.uz"

    @Singleton
    @Provides
    fun provideApiService(): ApiService {

        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC)
        val client: OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(ApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideSharedPrefs(@ApplicationContext context: Context):SharedPrefs{
        return SharedPrefs(context)
    }

    @Singleton
    @Provides
    fun provideNetworkState(@ApplicationContext context: Context): NetworkStateListener {
        return NetworkStateListener(context)
    }
}