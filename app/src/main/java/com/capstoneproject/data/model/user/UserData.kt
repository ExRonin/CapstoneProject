package com.capstoneproject.data.model.user

import com.google.gson.annotations.SerializedName

data class UserData(
    @field:SerializedName("role")
    val role: String,

    @field:SerializedName("phone")
    val phone: String,

    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("fullname")
    val fullname: String,

    @field:SerializedName("email")
    val email: String,

    @field:SerializedName("username")
    val username: String,

    @field:SerializedName("isFillSurvey")
    val isFillSurvey: Boolean
)