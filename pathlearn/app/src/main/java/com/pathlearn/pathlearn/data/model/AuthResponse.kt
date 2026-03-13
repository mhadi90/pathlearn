package com.pathlearn.pathlearn.data.model

import com.google.gson.annotations.SerializedName

data class AuthResponse(
    @SerializedName("token")
    val token: String,

    @SerializedName("role")
    val role: String,

    @SerializedName("email")
    val email: String,

    @SerializedName("userId")
    val userId: Int
)