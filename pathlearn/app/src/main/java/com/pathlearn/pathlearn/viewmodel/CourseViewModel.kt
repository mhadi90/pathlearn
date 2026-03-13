package com.pathlearn.pathlearn.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.pathlearn.pathlearn.data.local.PreferencesManager
import com.pathlearn.pathlearn.data.model.Course
import com.pathlearn.pathlearn.data.model.Module
import com.pathlearn.pathlearn.data.repository.CourseRepository
import com.pathlearn.pathlearn.utils.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CourseViewModel(application: Application) : AndroidViewModel(application) {

    private val courseRepository: CourseRepository

    private val _coursesState = MutableStateFlow<UiState<List<Course>>>(UiState.Idle)
    val coursesState: StateFlow<UiState<List<Course>>> = _coursesState.asStateFlow()

    private val _courseDetailState = MutableStateFlow<UiState<Course>>(UiState.Idle)
    val courseDetailState: StateFlow<UiState<Course>> = _courseDetailState.asStateFlow()

    private val _modulesState = MutableStateFlow<UiState<List<Module>>>(UiState.Idle)
    val modulesState: StateFlow<UiState<List<Module>>> = _modulesState.asStateFlow()

    init {
        val prefsManager = PreferencesManager(application)
        courseRepository = CourseRepository(prefsManager)
    }

    fun loadCourses(
        domaine: String? = null,
        niveau: Int? = null,
        prixMax: Double? = null,
        search: String? = null
    ) {
        viewModelScope.launch {
            _coursesState.value = UiState.Loading

            val result = courseRepository.getCourses(
                domaine = domaine,
                niveau = niveau,
                prixMax = prixMax,
                search = search
            )

            _coursesState.value = if (result.isSuccess) {
                UiState.Success(result.getOrNull() ?: emptyList())
            } else {
                UiState.Error(result.exceptionOrNull()?.message ?: "Erreur lors du chargement des formations")
            }
        }
    }

    fun loadCourseDetail(courseId: Int) {
        viewModelScope.launch {
            _courseDetailState.value = UiState.Loading

            val result = courseRepository.getCourseById(courseId)

            _courseDetailState.value = if (result.isSuccess) {
                UiState.Success(result.getOrNull()!!)
            } else {
                UiState.Error(result.exceptionOrNull()?.message ?: "Erreur lors du chargement de la formation")
            }
        }
    }

    fun loadModules(courseId: Int) {
        viewModelScope.launch {
            _modulesState.value = UiState.Loading

            val result = courseRepository.getModules(courseId)

            _modulesState.value = if (result.isSuccess) {
                UiState.Success(result.getOrNull() ?: emptyList())
            } else {
                UiState.Error(result.exceptionOrNull()?.message ?: "Erreur lors du chargement des modules")
            }
        }
    }

    fun searchCourses(query: String) {
        loadCourses(search = query)
    }

    fun filterByDomaine(domaine: String?) {
        loadCourses(domaine = domaine)
    }

    fun filterByNiveau(niveau: Int?) {
        loadCourses(niveau = niveau)
    }
}