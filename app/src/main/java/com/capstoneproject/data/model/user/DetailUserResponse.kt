package com.capstoneproject.data.model.user

import com.google.gson.annotations.SerializedName

data class DetailUserResponse(

	@field:SerializedName("data")
	val data: UserData,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: Boolean
)
