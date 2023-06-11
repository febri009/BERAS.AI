package com.example.berasai.data.retrofit

import com.example.berasai.data.model.DataPrices
import com.example.berasai.data.model.PricesResponse
import com.example.berasai.data.model.TengkulaksResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("prices")
    fun getPrices(): Call<PricesResponse>

    @GET("tengkulaks")
    fun getTengkulaks():Call<TengkulaksResponse>

    @GET("prices")
    fun getDate():Call<DataPrices>
}