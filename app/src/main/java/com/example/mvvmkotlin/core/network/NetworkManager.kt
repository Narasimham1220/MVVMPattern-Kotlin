package com.example.mvvmkotlin.core.network

import com.example.mvvmkotlin.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class NetworkManager{

    companion object {
        private lateinit var baseUrl: String
        private  var serviceClass : Any? =null


        private val loggingInterceptor = HttpLoggingInterceptor().apply {
            this.level = HttpLoggingInterceptor.Level.BODY
        }

        fun baseUrl(baseUrl: String) = apply { this.baseUrl = baseUrl }

        fun <T> serviceClass(serviceClass : Class<T>?) = apply { this.serviceClass = serviceClass }


        // init Retrofit base url instance
        fun <T> build() : T {
            val client = OkHttpClient.Builder().apply {
                if(BuildConfig.DEBUG)
                    addInterceptor(loggingInterceptor)
                connectTimeout(10, TimeUnit.MINUTES)
                readTimeout(10, TimeUnit.MINUTES)
                writeTimeout(10, TimeUnit.MINUTES)
            }.build()

            return Retrofit.Builder()
                .client(client)
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(serviceClass as Class<T>)
        }
    }
}