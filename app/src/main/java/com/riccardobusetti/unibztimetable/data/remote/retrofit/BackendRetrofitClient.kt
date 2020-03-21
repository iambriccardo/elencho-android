package com.riccardobusetti.unibztimetable.data.remote.retrofit

import com.google.gson.GsonBuilder
import com.riccardobusetti.unibztimetable.utils.DateUtils
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object BackendRetrofitClient {
    private const val BASE_URL = "https://elencho-scraper.herokuapp.com/"

    val webservice: BackendService = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(
            GsonBuilder().create()
        ))
        .client(
            OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build()
        )
        .build().create(BackendService::class.java)
}