package com.pathlearn.pathlearn.data.repository

import com.pathlearn.pathlearn.data.api.ApiClient
import com.pathlearn.pathlearn.data.local.PreferencesManager
import com.pathlearn.pathlearn.data.model.AuthResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

class AuthRepository(private val prefsManager: PreferencesManager) {

    private val authApi = ApiClient.authApiService

    suspend fun login(email: String, password: String): Result<AuthResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val credentials = mapOf(
                    "email" to email,
                    "password" to password
                )
                val response = authApi.login(credentials)

                if (response.isSuccessful && response.body() != null) {
                    val authResponse = response.body()!!

                    prefsManager.saveToken(authResponse.token)
                    prefsManager.saveUserId(authResponse.userId)
                    prefsManager.saveUserRole(authResponse.role)

                    Result.success(authResponse)
                } else {
                    Result.failure(Exception("Email ou mot de passe incorrect"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun register(
        email: String,
        password: String,
        nom: String,
        prenom: String
    ): Result<AuthResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val userData = mapOf(
                    "email" to email,
                    "password" to password,
                    "nom" to nom,
                    "prenom" to prenom,
                    "role" to "APPRENTI"
                )
                val response = authApi.register(userData)

                if (response.isSuccessful && response.body() != null) {
                    val authResponse = response.body()!!

                    prefsManager.saveToken(authResponse.token)
                    prefsManager.saveUserId(authResponse.userId)
                    prefsManager.saveUserRole(authResponse.role)

                    Result.success(authResponse)
                } else {
                    Result.failure(Exception("Erreur lors de l'inscription"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun logout() {
        try {
            val token = "Bearer ${prefsManager.token.first()}"
            authApi.logout(token)
        } catch (e: Exception) {
        }
        prefsManager.clear()
    }

    suspend fun getToken(): String? {
        return prefsManager.token.first()
    }

    suspend fun isLoggedIn(): Boolean {
        return getToken() != null
    }
}