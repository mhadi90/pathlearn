package com.pathlearn.pathlearn.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.pathlearn.pathlearn.data.local.PreferencesManager
import com.pathlearn.pathlearn.data.model.Enrollment
import com.pathlearn.pathlearn.data.repository.EnrollmentRepository
import com.pathlearn.pathlearn.utils.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class EnrollmentViewModel(application: Application) : AndroidViewModel(application) {

    private val enrollmentRepository: EnrollmentRepository

    private val _enrollmentsState = MutableStateFlow<UiState<List<Enrollment>>>(UiState.Idle)
    val enrollmentsState: StateFlow<UiState<List<Enrollment>>> = _enrollmentsState.asStateFlow()

    private val _enrollmentCreateState = MutableStateFlow<UiState<Enrollment>>(UiState.Idle)
    val enrollmentCreateState: StateFlow<UiState<Enrollment>> = _enrollmentCreateState.asStateFlow()

    private val _enrollmentDeleteState = MutableStateFlow<UiState<Enrollment>>(UiState.Idle)
    val enrollmentDeleteState: StateFlow<UiState<Enrollment>> = _enrollmentDeleteState.asStateFlow()

    init {
        val prefsManager = PreferencesManager(application)
        enrollmentRepository = EnrollmentRepository(prefsManager)
    }

    fun loadMyEnrollments() {
        viewModelScope.launch {
            _enrollmentsState.value = UiState.Loading

            val result = enrollmentRepository.getMyEnrollments()

            _enrollmentsState.value = if (result.isSuccess) {
                UiState.Success(result.getOrNull() ?: emptyList())
            } else {
                UiState.Error(result.exceptionOrNull()?.message ?: "Erreur lors du chargement des inscriptions")
            }
        }
    }

    fun enrollInCourse(courseId: Int) {
        viewModelScope.launch {
            _enrollmentCreateState.value = UiState.Loading

            val result = enrollmentRepository.enrollInCourse(courseId)

            _enrollmentCreateState.value = if (result.isSuccess) {
                loadMyEnrollments()
                UiState.Success(result.getOrNull()!!)
            } else {
                UiState.Error(result.exceptionOrNull()?.message ?: "Erreur lors de l'inscription")
            }
        }
    }

    fun cancelEnrollment(enrollmentId: Int) {
        viewModelScope.launch {
            _enrollmentDeleteState.value = UiState.Loading

            val result = enrollmentRepository.cancelEnrollment(enrollmentId)

            _enrollmentDeleteState.value = if (result.isSuccess) {
                loadMyEnrollments()
                UiState.Success(result.getOrNull()!!)
            } else {
                UiState.Error(result.exceptionOrNull()?.message ?: "Erreur lors de l'annulation")
            }
        }
    }

    fun resetCreateState() {
        _enrollmentCreateState.value = UiState.Idle
    }
}