package com.pathlearn.pathlearn.data.api

import com.pathlearn.pathlearn.data.model.AuthResponse
import com.pathlearn.pathlearn.data.model.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT

interface AuthApiService {

    @POST("auth/login")
    suspend fun login(
        @Body credentials: Map<String, String>
    ): Response<AuthResponse>

    @POST("auth/register")
    suspend fun register(
        @Body userData: Map<String, String>
    ): Response<AuthResponse>

    @GET("auth/me")
    suspend fun getCurrentUser(
        @Header("Authorization") token: String
    ): Response<User>

    @PUT("auth/me")
    suspend fun updateProfile(
        @Header("Authorization") token: String,
        @Body userData: Map<String, String>
    ): Response<User>

    @POST("auth/logout")
    suspend fun logout(
        @Header("Authorization") token: String
    ): Response<Map<String, String>>
}