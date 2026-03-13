package com.pathlearn.pathlearn.data.repository

import com.pathlearn.pathlearn.data.api.ApiClient
import com.pathlearn.pathlearn.data.local.PreferencesManager
import com.pathlearn.pathlearn.data.model.Course
import com.pathlearn.pathlearn.data.model.Module
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CourseRepository(private val prefsManager: PreferencesManager) {

    private val courseApi = ApiClient.courseApiService

    suspend fun getCourses(
        domaine: String? = null,
        niveau: Int? = null,
        prixMax: Double? = null,
        search: String? = null
    ): Result<List<Course>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = courseApi.getCourses(
                    domaine = domaine,
                    niveau = niveau,
                    prixMax = prixMax,
                    search = search
                )

                if (response.isSuccessful && response.body() != null) {
                    Result.success(response.body()!!)
                } else {
                    Result.failure(Exception("Erreur ${response.code()}: ${response.message()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun getCourseById(courseId: Int): Result<Course> {
        return withContext(Dispatchers.IO) {
            try {
                val response = courseApi.getCourseById(courseId)

                if (response.isSuccessful && response.body() != null) {
                    Result.success(response.body()!!)
                } else {
                    Result.failure(Exception("Formation introuvable"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun getModules(courseId: Int): Result<List<Module>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = courseApi.getModules(courseId)

                if (response.isSuccessful && response.body() != null) {
                    Result.success(response.body()!!)
                } else {
                    Result.failure(Exception("Erreur lors du chargement des modules"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}