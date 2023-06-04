package com.example.berasai.data.retrofit

import com.example.berasai.data.model.ArticlesResponse
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("articles")
    fun getArticles(): Call<ArticlesResponse>
}