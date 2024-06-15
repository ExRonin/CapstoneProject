package com.capstoneproject.data.model.order

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Order(

    @field:SerializedName("imageProduct")
    val imageProduct: String? = null,

    @field:SerializedName("endBooked")
    val endBooked: String? = null,

    @field:SerializedName("productId")
    val productId: String? = null,

    @field:SerializedName("advertisingContentId")
    val advertisingContentId: String? = null,

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
    val totalPayment: Int? = null,

    @field:SerializedName("phone")
    val phone: String? = null,

    @field:SerializedName("paymentMethod")
    val paymentMethod: String? = null,

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("fullname")
    val fullname: String? = null,

    @field:SerializedName("email")
    val email: String? = null,

    @field:SerializedName("status")
    val status: String? = null
): Parcelable