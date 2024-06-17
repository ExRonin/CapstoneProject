package com.capstoneproject.data.model.advertisingcontent

import com.google.gson.annotations.SerializedName

data class CreateAdvertisingContentResponse(

	@field:SerializedName("data")
	val data: AdvertisingContent? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
)
