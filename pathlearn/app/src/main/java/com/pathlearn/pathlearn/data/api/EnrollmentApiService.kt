package com.pathlearn.pathlearn.data.api

import com.pathlearn.pathlearn.data.model.Enrollment
import retrofit2.Response
import retrofit2.http.*

interface EnrollmentApiService {

    @POST("inscriptions")
    suspend fun createEnrollment(
        @Header("Authorization") token: String,
        @Body data: Map<String, Int>
    ): Response<Enrollment>

    @GET("inscriptions/{id}")
    suspend fun getEnrollment(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<Enrollment>

    @GET("inscriptions/utilisateur/{idUtilisateur}")
    suspend fun getUserEnrollments(
        @Header("Authorization") token: String,
        @Path("idUtilisateur") idUtilisateur: Int
    ): Response<List<Enrollment>>

    @PUT("inscriptions/{id}/progression")
    suspend fun updateProgress(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Body data: Map<String, Int>
    ): Response<Enrollment>

    @PUT("inscriptions/{id}/annuler")
    suspend fun cancelEnrollment(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<Enrollment>
}