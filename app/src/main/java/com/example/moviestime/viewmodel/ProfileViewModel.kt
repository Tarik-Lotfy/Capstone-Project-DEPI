package com.example.moviestime.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviestime.data.repository.UserProfileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ProfileUiState(
    val name: String = "",
    val bio: String = "",
    val photoUrl: String? = null,
    val email: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSaved: Boolean = false
)

class ProfileViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = UserProfileRepository(application.applicationContext)

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    private var pendingPhotoUri: String? = null

    init {
        viewModelScope.launch {
            repository.observeProfile().collect { data ->
                _uiState.value = _uiState.value.copy(
                    name = data.name,
                    bio = data.bio,
                    photoUrl = data.photoUrl,
                    email = data.email,
                    isLoading = false,
                    isSaved = false,
                    error = null
                )
            }
        }
        viewModelScope.launch {
            runCatching { repository.syncFromRemote() }
                .onFailure { e -> _uiState.value = _uiState.value.copy(error = e.message) }
        }
    }

    fun updateName(name: String) {
        _uiState.value = _uiState.value.copy(name = name, isSaved = false, error = null)
    }

    fun updateBio(bio: String) {
        _uiState.value = _uiState.value.copy(bio = bio, isSaved = false, error = null)
    }

    fun updatePhotoUri(uri: String?) {
        pendingPhotoUri = uri
        _uiState.value = _uiState.value.copy(photoUrl = uri ?: _uiState.value.photoUrl, isSaved = false)
    }

    fun save() {
        val name = _uiState.value.name
        val bio = _uiState.value.bio
        val photoUri = pendingPhotoUri
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null, isSaved = false)
            runCatching { repository.saveLocalOnly(name, bio, photoUri) }
                .onSuccess {
                    _uiState.value = _uiState.value.copy(isLoading = false, isSaved = true)
                    pendingPhotoUri = null
                }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(isLoading = false, error = e.message)
                }

            viewModelScope.launch {
                runCatching { repository.saveRemote(name, bio, photoUri) }
                    .onFailure { e ->
                        _uiState.value = _uiState.value.copy(error = e.message)
                    }
            }
        }
    }
}
