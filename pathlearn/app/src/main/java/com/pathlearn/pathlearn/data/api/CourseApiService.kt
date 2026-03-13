package com.pathlearn.pathlearn.data.api

import com.pathlearn.pathlearn.data.model.Course
import com.pathlearn.pathlearn.data.model.Module
import retrofit2.Response
import retrofit2.http.*

interface CourseApiService {

    @GET("courses")
    suspend fun getCourses(
        @Query("domaine") domaine: String? = null,
        @Query("niveau") niveau: Int? = null,
        @Query("prixMax") prixMax: Double? = null,
        @Query("search") search: String? = null
    ): Response<List<Course>>

    @GET("courses/{id}")
    suspend fun getCourseById(
        @Path("id") id: Int
    ): Response<Course>

    @GET("courses/{courseId}/modules")
    suspend fun getModules(
        @Path("courseId") courseId: Int
    ): Response<List<Module>>
}