package com.capstoneproject.data.model.user

data class UserData(
    val id: String,
    val username: String,
    val email: String,
    val role: String,
    val fullname: String,
    val phone: String,
    val isFillSurvey: Boolean
)