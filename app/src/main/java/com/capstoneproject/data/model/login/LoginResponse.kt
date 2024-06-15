package com.capstoneproject.data.model.login

import com.capstoneproject.data.model.user.UserData

data class LoginResponse(
    val status: Boolean,
    val message: String,
    val data: UserData,
    val token: String,
    val refreshToken: String,
)