package com.example.berasai.data.retrofit

import com.example.berasai.data.model.ArticlesResponse
import com.example.berasai.data.model.PricesResponse
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("articles")
    fun getArticles(): Call<ArticlesResponse>

    @GET("prices")
    fun getPrices(): Call<PricesResponse>
}