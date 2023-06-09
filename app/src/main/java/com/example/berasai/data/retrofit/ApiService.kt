package com.example.berasai.data.retrofit

import com.example.berasai.data.model.ArticlesResponse
import com.example.berasai.data.model.DataItem
import com.example.berasai.data.model.PricesResponse
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("articles")
    fun getArticles(): Call<ArticlesResponse>

    @GET("articles")
    fun getDetail(): Call<DataItem>

    @GET("prices")
    fun getPrices(): Call<PricesResponse>
}