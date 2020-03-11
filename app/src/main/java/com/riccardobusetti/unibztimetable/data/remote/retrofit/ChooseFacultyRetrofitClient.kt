package com.riccardobusetti.unibztimetable.data.remote.retrofit

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ChooseFacultyRetrofitClient {
    private const val BASE_URL = "https://elencho-scraper.herokuapp.com/"

    val webservice: ChooseFacultyService = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
        .build().create(ChooseFacultyService::class.java)
}