package com.pathlearn.pathlearn.data.model

import com.google.gson.annotations.SerializedName

data class Enrollment(
    @SerializedName("id")
    val id: Int,

    @SerializedName("idUtilisateur")
    val idUtilisateur: Int,

    @SerializedName("idFormation")
    val idFormation: Int,

    @SerializedName("statut")
    val statut: String,

    @SerializedName("pourcentageProgression")
    val pourcentageProgression: Int,

    @SerializedName("dateInscription")
    val dateInscription: String,

    @SerializedName("dateFin")
    val dateFin: String?
)