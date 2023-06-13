package com.example.berasai.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.berasai.data.model.DataTengkulaks
import com.example.berasai.data.model.TengkulaksResponse
import com.example.berasai.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel() {
    private val _listTengkulaks = MutableLiveData<List<DataTengkulaks>>()
    val listTengkulaks: LiveData<List<DataTengkulaks>> = _listTengkulaks

    private val _loadHome = MutableLiveData<Boolean>()
    val loadHome: LiveData<Boolean> = _loadHome

    init {
        getListTengkulaks()
    }

    private fun getListTengkulaks(){
        _loadHome.value = true
        val client = ApiConfig.getApiService().getTengkulaks()
        client.enqueue(object : Callback<TengkulaksResponse> {
            override fun onResponse(
                call: Call<TengkulaksResponse>,
                response: Response<TengkulaksResponse>
            ) {
                _loadHome.value = false
                if (response.isSuccessful){
                    _listTengkulaks.value = response.body()?.data
                }
            }

            override fun onFailure(call: Call<TengkulaksResponse>, t: Throwable) {
                Log.e("HomeViewModel", "Failed to get Articles: ${t.message}")
            }

        })
    }

}