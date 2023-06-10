package com.example.berasai

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.berasai.data.model.DataItem
import com.example.berasai.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailKontenViewModel(): ViewModel() {
    private val _detailArticle = MutableLiveData<DataItem>()
    val detailArticle: LiveData<DataItem> = _detailArticle

    /*init {
        getDetailArticles()
    }*/

    fun getDetail(title: String) {
        val client = ApiConfig.getApiService().getDetail(title)
        client.enqueue(object : Callback<DataItem> {
            override fun onResponse(call: Call<DataItem>, response: Response<DataItem>) {
                if (response.isSuccessful) {
                    _detailArticle.value = response.body()
                }
            }

            override fun onFailure(call: Call<DataItem>, t: Throwable) {
                Log.e("DetailKontenViewModel", "Failed to get detail articles: ${t.message}")
            }
        })
    }

}
