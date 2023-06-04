package com.example.berasai.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.berasai.data.model.ArticlesResponse
import com.example.berasai.data.model.DataItem
import com.example.berasai.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel() {
    private val _listArticles = MutableLiveData<List<DataItem>>()
    val listArticles: LiveData<List<DataItem>> = _listArticles

    init {
        getListArticles()
    }

    fun getListArticles(){
        val client = ApiConfig.getApiService().getArticles()
        client.enqueue(object : Callback<ArticlesResponse> {
            override fun onResponse(
                call: Call<ArticlesResponse>,
                response: Response<ArticlesResponse>
            ) {
                if (response.isSuccessful){
                    _listArticles.value = response.body()?.data
                }
            }

            override fun onFailure(call: Call<ArticlesResponse>, t: Throwable) {
                Log.e("HomeViewModel", "Failed to get Articles: ${t.message}")
            }

        })
    }

}