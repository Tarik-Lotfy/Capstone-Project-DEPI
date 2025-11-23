package com.example.moviestime.viewmodel

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.example.moviestime.data.repository.AuthRepository
import com.google.firebase.auth.FirebaseUser

class AuthViewModel : ViewModel() {
    private val repository = AuthRepository()

    // التصحيح: إزالة "value =" وتمرير القيمة مباشرة
    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    // التصحيح: إزالة "value =" وتمرير القيمة مباشرة
    private val _isLoggedIn = MutableStateFlow(repository.currentUser != null)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    fun login(email: String, password: String) {
        if (!validateEmail(email)) {
            _uiState.value = AuthUiState(error = "Invalid email")
            return
        }
        if (password.length < 6) {
            _uiState.value = AuthUiState(error = "Password must be at least 6 characters")
            return
        }

        _uiState.value = AuthUiState(isLoading = true)
        viewModelScope.launch {
            try {
                val result = repository.login(email, password)
                if (result.isSuccess) {
                    _isLoggedIn.value = true
                    _uiState.value = AuthUiState()
                } else {
                    _uiState.value = AuthUiState(error = "Login failed: ${result.exceptionOrNull()?.message}")
                }
            } catch (e: Exception) {
                Log.e("AuthViewModel", "Unexpected error", e)
                _uiState.value = AuthUiState(error = "Unexpected error: ${e.message}")
            }
        }
    }

    fun register(email: String, password: String, name: String) {
        if (name.isBlank()) {
            _uiState.value = AuthUiState(error = "Name is required")
            return
        }
        if (!validateEmail(email)) {
            _uiState.value = AuthUiState(error = "Invalid email")
            return
        }
        if (password.length < 6) {
            _uiState.value = AuthUiState(error = "Password must be at least 6 characters")
            return
        }

        _uiState.value = AuthUiState(isLoading = true)
        viewModelScope.launch {
            try {
                val result = repository.register(email, password, name)
                if (result.isSuccess) {
                    _isLoggedIn.value = true
                    _uiState.value = AuthUiState()
                } else {
                    _uiState.value = AuthUiState(error = "Registration failed: ${result.exceptionOrNull()?.message}")
                }
            } catch (e: Exception) {
                Log.e("AuthViewModel", "Unexpected error", e)
                _uiState.value = AuthUiState(error = "Unexpected error: ${e.message}")
            }
        }
    }

    fun signInWithGoogleToken(idToken: String) {
        _uiState.value = AuthUiState(isLoading = true)
        viewModelScope.launch {
            try {
                val result = repository.signInWithGoogleToken(idToken)
                if (result.isSuccess) {
                    _isLoggedIn.value = true
                    _uiState.value = AuthUiState()
                } else {
                    _uiState.value = AuthUiState(error = "Google Sign-In failed: ${result.exceptionOrNull()?.message}")
                }
            } catch (e: Exception) {
                Log.e("AuthViewModel", "Unexpected error", e)
                _uiState.value = AuthUiState(error = "Unexpected error: ${e.message}")
            }
        }
    }

    fun signInWithFacebookToken(token: String) {
        _uiState.value = AuthUiState(isLoading = true)
        viewModelScope.launch {
            val result = repository.signInWithFacebookToken(token)
            if (result.isSuccess) {
                _isLoggedIn.value = true
                _uiState.value = AuthUiState()
            } else {
                _uiState.value = AuthUiState(error = "Facebook Login Failed: ${result.exceptionOrNull()?.message}")
            }
        }
    }

    fun onExternalSignInSuccess() {
        _isLoggedIn.value = true
        _uiState.value = AuthUiState()
    }

    fun onExternalSignInFailure(error: String) {
        _uiState.value = AuthUiState(error = error)
    }

    fun logout() {
        repository.logout()
        _isLoggedIn.value = false
        _uiState.value = AuthUiState()
    }

    private fun validateEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}

data class AuthUiState(
    val isLoading: Boolean = false,
    val error: String? = null
)