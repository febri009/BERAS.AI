package com.example.berasai.ui.price

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.berasai.data.model.DataPrices
import com.example.berasai.data.model.PricesResponse
import com.example.berasai.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PriceViewModel : ViewModel() {
    private val _listDataPrices = MutableLiveData<List<DataPrices>>()
    val listDataPrices: LiveData<List<DataPrices>> = _listDataPrices

    private val _loadPrice = MutableLiveData<Boolean>()
    val loadPrice: LiveData<Boolean> = _loadPrice

    private val _priceDate = MutableLiveData<DataPrices>()

    init {
        getListPrices()
        getDatePrice()
    }

    private fun getListPrices(){
        _loadPrice.value = true
        val client = ApiConfig.getApiService().getPrices()
        client.enqueue(object : Callback<PricesResponse>{
            override fun onResponse(
                call: Call<PricesResponse>,
                response: Response<PricesResponse>
            ) {
                _loadPrice.value = false
                if (response.isSuccessful){
                    _listDataPrices.value = response.body()?.data
                }
            }

            override fun onFailure(call: Call<PricesResponse>, t: Throwable) {
                Log.e("PriceViewModel", "Failed to get Prices: ${t.message}")
            }

        })
    }

    private fun getDatePrice(){
        val client = ApiConfig.getApiService().getDate()
        client.enqueue(object : Callback<DataPrices>{
            override fun onResponse(call: Call<DataPrices>, response: Response<DataPrices>) {
                if (response.isSuccessful){
                    _priceDate.value = response.body()
                }
            }

            override fun onFailure(call: Call<DataPrices>, t: Throwable) {
                Log.e("PriceViewModel", "Failed to get date: ${t.message}")
            }

        })
    }

}