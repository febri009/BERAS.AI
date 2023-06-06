package com.example.berasai.data.model

import com.google.gson.annotations.SerializedName

data class PricesResponse(

	@field:SerializedName("data")
	val data: List<DataPrices>,

	@field:SerializedName("success")
	val success: Boolean,

	@field:SerializedName("message")
	val message: String
)

data class DataPrices(

	@field:SerializedName("provinsi")
	val provinsi: String,

	@field:SerializedName("createdAt")
	val createdAt: String,

	@field:SerializedName("harga")
	val harga: Int,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("updatedAt")
	val updatedAt: String
)
