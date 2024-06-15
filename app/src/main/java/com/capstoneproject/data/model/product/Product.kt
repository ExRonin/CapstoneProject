package com.capstoneproject.data.model.product

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Product(
    val id: String,
    val endBooked: String,
    val latitude: Double,
    val isBooked: Boolean,
    val type: String,
    val userId: String,
    val startBooked: String,
    val userImage: String,
    val price: Int,
    val imageUrl: String,
    val userPosition: String,
    val name: String,
    val width: Int,
    val userFullname: String,
    val locationDesc: String,
    val theme: String,
    val userCompany: String,
    val category: String,
    val longitude: Double,
    val height: Int,
    val description: String,
    val rating: Double
): Parcelable