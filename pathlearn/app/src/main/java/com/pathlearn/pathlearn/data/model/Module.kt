package com.pathlearn.pathlearn.data.model

import com.google.gson.annotations.SerializedName

data class Module(
    @SerializedName("id")
    val id: Int,

    @SerializedName("titre")
    val titre: String,

    @SerializedName("description")
    val description: String?,

    @SerializedName("numeroOrdre")
    val numeroOrdre: Int,

    @SerializedName("dureeMinutes")
    val dureeMinutes: Int?,

    @SerializedName("createdAt")
    val createdAt: String
)