package com.pathlearn.pathlearn.data.model

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("id")
    val id: Int,

    @SerializedName("email")
    val email: String,

    @SerializedName("nom")
    val nom: String,

    @SerializedName("prenom")
    val prenom: String,

    @SerializedName("role")
    val role: String,

    @SerializedName("dateCreation")
    val dateCreation: String
)