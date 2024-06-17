package com.capstoneproject.data.model.order

import com.google.gson.annotations.SerializedName

data class CreateOrderRequest(

	@field:SerializedName("imageProduct")
	val imageProduct: String? = null,

	@field:SerializedName("endBooked")
	val endBooked: String? = null,

	@field:SerializedName("productId")
	val productId: String? = null,

	@field:SerializedName("userId")
	val userId: String? = null,

	@field:SerializedName("productName")
	val productName: String? = null,

	@field:SerializedName("locationProduct")
	val locationProduct: String? = null,

	@field:SerializedName("startBooked")
	val startBooked: String? = null,

	@field:SerializedName("categoryProduct")
	val categoryProduct: String? = null,

	@field:SerializedName("totalPayment")
	val totalPayment: Double? = null,

	@field:SerializedName("phone")
	val phone: String? = null,

	@field:SerializedName("paymentMethod")
	val paymentMethod: String? = null,

	@field:SerializedName("fullname")
	val fullname: String? = null,

	@field:SerializedName("email")
	val email: String? = null
)
