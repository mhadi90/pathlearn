package com.pathlearn.pathlearn.data.model

import com.google.gson.annotations.SerializedName

data class Course(
    @SerializedName("id")
    val id: Int,

    @SerializedName("titre")
    val titre: String,

    @SerializedName("description")
    val description: String,

    @SerializedName("domaine")
    val domaine: String,

    @SerializedName("niveau")
    val niveau: Int,

    @SerializedName("duree")
    val duree: Int,

    @SerializedName("prix")
    val prix: Double,

    @SerializedName("idFormateur")
    val idFormateur: Int,

    @SerializedName("note")
    val note: Double?,

    @SerializedName("statut")
    val statut: String,

    @SerializedName("createdAt")
    val createdAt: String,

    @SerializedName("dateModif")
    val dateModif: String
)