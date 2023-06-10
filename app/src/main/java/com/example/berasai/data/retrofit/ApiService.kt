package com.example.berasai.data.retrofit

import com.example.berasai.data.model.ArticlesResponse
import com.example.berasai.data.model.DataItem
import com.example.berasai.data.model.PricesResponse
import com.example.berasai.data.model.TengkulaksResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("articles")
    fun getArticles(): Call<ArticlesResponse>

    @GET("articles/{title}")
    fun getDetail(@Path("title") title: String): Call<DataItem>

    @GET("prices")
    fun getPrices(): Call<PricesResponse>

    @GET("tengkulaks")
    fun getTengkulaks():Call<TengkulaksResponse>
}