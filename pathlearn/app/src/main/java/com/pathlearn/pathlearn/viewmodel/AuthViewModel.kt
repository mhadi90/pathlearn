package com.pathlearn.pathlearn.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.pathlearn.pathlearn.data.local.PreferencesManager
import com.pathlearn.pathlearn.data.model.AuthResponse
import com.pathlearn.pathlearn.data.repository.AuthRepository
import com.pathlearn.pathlearn.utils.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val authRepository: AuthRepository

    private val _loginState = MutableStateFlow<UiState<AuthResponse>>(UiState.Idle)
    val loginState: StateFlow<UiState<AuthResponse>> = _loginState.asStateFlow()

    private val _registerState = MutableStateFlow<UiState<AuthResponse>>(UiState.Idle)
    val registerState: StateFlow<UiState<AuthResponse>> = _registerState.asStateFlow()

    init {
        val prefsManager = PreferencesManager(application)
        authRepository = AuthRepository(prefsManager)
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _loginState.value = UiState.Loading

            val result = authRepository.login(email, password)

            _loginState.value = if (result.isSuccess) {
                UiState.Success(result.getOrNull()!!)
            } else {
                UiState.Error(result.exceptionOrNull()?.message ?: "Erreur inconnue")
            }
        }
    }

    fun register(email: String, password: String, nom: String, prenom: String) {
        viewModelScope.launch {
            _registerState.value = UiState.Loading

            val result = authRepository.register(email, password, nom, prenom)

            _registerState.value = if (result.isSuccess) {
                UiState.Success(result.getOrNull()!!)
            } else {
                UiState.Error(result.exceptionOrNull()?.message ?: "Erreur inconnue")
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
        }
    }

    fun resetLoginState() {
        _loginState.value = UiState.Idle
    }
}