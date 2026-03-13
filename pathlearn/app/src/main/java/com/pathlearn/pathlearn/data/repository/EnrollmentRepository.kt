package com.pathlearn.pathlearn.data.repository

import com.pathlearn.pathlearn.data.api.ApiClient
import com.pathlearn.pathlearn.data.local.PreferencesManager
import com.pathlearn.pathlearn.data.model.Enrollment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

class EnrollmentRepository(private val prefsManager: PreferencesManager) {

    private val enrollmentApi = ApiClient.enrollmentApiService

    private suspend fun getAuthToken(): String {
        val token = prefsManager.token.first()
        return "Bearer $token"
    }

    suspend fun getMyEnrollments(): Result<List<Enrollment>> {
        return withContext(Dispatchers.IO) {
            try {
                val token = getAuthToken()
                val userId = prefsManager.userId.first() ?: return@withContext Result.failure(
                    Exception("User ID not found")
                )

                val response = enrollmentApi.getUserEnrollments(token, userId)

                if (response.isSuccessful && response.body() != null) {
                    Result.success(response.body()!!)
                } else {
                    Result.failure(Exception("Erreur ${response.code()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun enrollInCourse(courseId: Int): Result<Enrollment> {
        return withContext(Dispatchers.IO) {
            try {
                val token = getAuthToken()
                val userId = prefsManager.userId.first() ?: return@withContext Result.failure(
                    Exception("User ID not found")
                )

                val data = mapOf(
                    "idUtilisateur" to userId,
                    "idFormation" to courseId
                )
                val response = enrollmentApi.createEnrollment(token, data)

                if (response.isSuccessful && response.body() != null) {
                    Result.success(response.body()!!)
                } else {
                    Result.failure(Exception("Erreur lors de l'inscription"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun cancelEnrollment(enrollmentId: Int): Result<Enrollment> {
        return withContext(Dispatchers.IO) {
            try {
                val token = getAuthToken()
                val response = enrollmentApi.cancelEnrollment(token, enrollmentId)

                if (response.isSuccessful && response.body() != null) {
                    Result.success(response.body()!!)
                } else {
                    Result.failure(Exception("Erreur lors de l'annulation"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}