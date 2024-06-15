package com.capstoneproject.data.model.order

import com.google.gson.annotations.SerializedName

data class OrderResponse(

	@field:SerializedName("data")
	val data: List<Order?>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
)


