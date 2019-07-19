package com.example.mvvmkotlin.projectListView.service

import com.example.mvvmkotlin.projectListView.model.Project
import com.example.mvvmkotlin.utils.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import java.util.concurrent.TimeUnit

interface ProjectListService {
    @GET("users/{user}/repos")
    fun getProjectList(@Path("user") user: String): Call<ArrayList<Project>>

    companion object {

        val HTTPS_API_GITHUB_URL = "https://api.github.com/"

        fun createCoreService(): ProjectListService {
            val httpLoggingInterceptor : HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
                this.level = HttpLoggingInterceptor.Level.BODY
            }
            val okHttpClient = OkHttpClient.Builder()
                .connectTimeout(1, TimeUnit.MINUTES)
                .addInterceptor(httpLoggingInterceptor)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .build()

            return Retrofit.Builder()
                .baseUrl(HTTPS_API_GITHUB_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .build().create(ProjectListService::class.java)
        }
    }
}