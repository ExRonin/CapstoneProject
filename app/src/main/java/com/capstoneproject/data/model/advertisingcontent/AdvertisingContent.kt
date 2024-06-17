package com.capstoneproject.data.model.advertisingcontent

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
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
): Parcelable
