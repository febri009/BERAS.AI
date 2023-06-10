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

data class UpdatedAtPrices(

	@field:SerializedName("_nanoseconds")
	val nanoseconds: Int,

	@field:SerializedName("_seconds")
	val seconds: Int
)

data class CreatedAtPrices(

	@field:SerializedName("_nanoseconds")
	val nanoseconds: Int,

	@field:SerializedName("_seconds")
	val seconds: Int
)

data class DataPrices(

	@field:SerializedName("createdAt")
	val createdAt: CreatedAtPrices,

	@field:SerializedName("province")
	val province: String,

	@field:SerializedName("price")
	val price: Int,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("updatedAt")
	val updatedAt: UpdatedAtPrices
)
