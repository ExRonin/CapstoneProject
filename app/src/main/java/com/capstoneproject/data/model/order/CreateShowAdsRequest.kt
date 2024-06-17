package com.capstoneproject.data.model.order

import com.google.gson.annotations.SerializedName

data class CreateShowAdsRequest(

	@field:SerializedName("advertisingContentId")
	val advertisingContentId: String? = null,

	@field:SerializedName("orderId")
	val orderId: String? = null
)
