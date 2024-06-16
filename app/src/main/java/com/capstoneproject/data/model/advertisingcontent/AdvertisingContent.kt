package com.capstoneproject.data.model.advertisingcontent

import com.google.gson.annotations.SerializedName

data class AdvertisingContent(

    @field:SerializedName("imageUrl")
    val imageUrl: String? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("category")
    val category: String? = null,

    @field:SerializedName("userId")
    val userId: String? = null,

    @field:SerializedName("status")
    val status: String? = null
)
