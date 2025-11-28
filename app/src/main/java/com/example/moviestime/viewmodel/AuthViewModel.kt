package com.example.moviestime.viewmodel

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviestime.data.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import com.google.firebase.auth.FirebaseAuth.AuthStateListener

data class UserProfile(
    val name: String = "",
    val email: String = "",
    val bio: String = "",
    val photoUrl: String? = null
)

 data class AuthUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val isUpdateSuccess: Boolean = false
)

class AuthViewModel : ViewModel() {
    private val repository = AuthRepository()
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    private val _isLoggedIn = MutableStateFlow(auth.currentUser != null)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    private val _userProfile = MutableStateFlow(UserProfile())
    val userProfile: StateFlow<UserProfile> = _userProfile.asStateFlow()

    private val authStateListener = AuthStateListener { firebaseAuth ->
        val user = firebaseAuth.currentUser
        _isLoggedIn.value = user != null
        if (user != null) {
            loadUserProfile()
        } else {
            _userProfile.value = UserProfile()
        }
    }

    init {
        auth.addAuthStateListener(authStateListener)
        if (auth.currentUser != null) {
            loadUserProfile()
        }
    }

    override fun onCleared() {
        super.onCleared()
        auth.removeAuthStateListener(authStateListener)
    }

    fun loadUserProfile() {
        val user = auth.currentUser ?: return
        viewModelScope.launch {
            try {
                val document = db.collection("users").document(user.uid).get().await()
                val bio = document.getString("bio") ?: "Tell us about your love for cinema..."

                _userProfile.value = UserProfile(
                    name = user.displayName ?: "Movie Lover",
                    email = user.email ?: "",
                    bio = bio,
                    photoUrl = user.photoUrl?.toString()
                )
            } catch (e: Exception) {
                Log.e("AuthViewModel", "Error loading profile", e)
            }
        }
    }

    fun updateUserProfile(name: String, bio: String) {
        val user = auth.currentUser ?: return

        _userProfile.value = _userProfile.value.copy(
            name = name,
            bio = bio,
            email = user.email ?: _userProfile.value.email,
            photoUrl = user.photoUrl?.toString() ?: _userProfile.value.photoUrl
        )
        _uiState.value = AuthUiState(isUpdateSuccess = true)

        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    val profileUpdates = UserProfileChangeRequest.Builder()
                        .setDisplayName(name)
                        .build()
                    user.updateProfile(profileUpdates).await()

                    val userData = hashMapOf(
                        "bio" to bio,
                        "name" to name,
                        "email" to user.email
                    )
                    db.collection("users").document(user.uid)
                        .set(userData, SetOptions.merge())
                        .await()
                }
            } catch (e: Exception) {
                Log.e("AuthViewModel", "Error updating profile", e)
            }
        }
    }

     fun resetState() {
        _uiState.value = AuthUiState()
    }


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
                    loadUserProfile()


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
                    loadUserProfile()
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
                    loadUserProfile()
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
                loadUserProfile()
            } else {
                _uiState.value = AuthUiState(error = "Facebook Login Failed: ${result.exceptionOrNull()?.message}")
            }
        }
    }

    fun onExternalSignInSuccess() {
        _isLoggedIn.value = true
        _uiState.value = AuthUiState()
        loadUserProfile()
    }

    fun onExternalSignInFailure(error: String) {
        _uiState.value = AuthUiState(error = error)
    }

    fun logout() {
        repository.logout()
        _isLoggedIn.value = false
        _userProfile.value = UserProfile()
        _uiState.value = AuthUiState()
    }

    private fun validateEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}
