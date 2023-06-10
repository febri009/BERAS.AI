package com.example.berasai.data.model

import com.google.gson.annotations.SerializedName

data class TengkulaksResponse(

	@field:SerializedName("data")
	val data: List<DataTengkulaks>,

	@field:SerializedName("success")
	val success: Boolean,

	@field:SerializedName("message")
	val message: String
)

data class DataTengkulaks(

	@field:SerializedName("createdAt")
	val createdAt: CreatedAtTengkulaks,

	@field:SerializedName("address")
	val address: String,

	@field:SerializedName("phone")
	val phone: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("updatedAt")
	val updatedAt: UpdatedAtTengkulaks
)

data class CreatedAtTengkulaks(

	@field:SerializedName("_nanoseconds")
	val nanoseconds: Int,

	@field:SerializedName("_seconds")
	val seconds: Int
)

data class UpdatedAtTengkulaks(

	@field:SerializedName("_nanoseconds")
	val nanoseconds: Int,

	@field:SerializedName("_seconds")
	val seconds: Int
)
