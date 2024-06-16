package com.capstoneproject.data.model.advertisingcontent

import com.google.gson.annotations.SerializedName

data class AdvertisingContentsResponse(

	@field:SerializedName("data")
	val data: List<AdvertisingContent?>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
)


