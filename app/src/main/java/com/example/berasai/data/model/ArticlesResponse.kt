package com.example.berasai.data.model

import com.google.gson.annotations.SerializedName

data class ArticlesResponse(

	@field:SerializedName("data")
	val data: List<DataItem>,

	@field:SerializedName("success")
	val success: Boolean,

	@field:SerializedName("message")
	val message: String
)

data class DataItem(

	@field:SerializedName("createdAt")
	val createdAt: String,

	@field:SerializedName("article_images")
	val articleImages: List<ArticleImagesItem>,

	@field:SerializedName("author")
	val author: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("judul")
	val judul: String,

	@field:SerializedName("content")
	val content: String,

	@field:SerializedName("updatedAt")
	val updatedAt: String
)

data class ArticleImagesItem(

	@field:SerializedName("createdAt")
	val createdAt: String,

	@field:SerializedName("imagePath")
	val imagePath: String,

	@field:SerializedName("articleId")
	val articleId: Int,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("updatedAt")
	val updatedAt: String
)
