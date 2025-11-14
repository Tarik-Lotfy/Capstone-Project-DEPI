package com.example.moviestime.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.example.moviestime.data.model.Movie
import com.example.moviestime.data.notification.AppNotificationManager
import com.example.moviestime.data.repository.LocalMovieRepository

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val localRepository = LocalMovieRepository(application.applicationContext)

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    private val _favorites = MutableStateFlow<List<Movie>>(emptyList())
    val favorites: StateFlow<List<Movie>> = _favorites.asStateFlow()

    init {
        loadFavorites()
    }

    fun selectTab(index: Int) {
        _uiState.value = _uiState.value.copy(selectedTab = index)
    }


    @androidx.annotation.RequiresPermission(android.Manifest.permission.POST_NOTIFICATIONS)
    fun toggleFavorite(movie: Movie) {
        viewModelScope.launch {
            val notificationManager = AppNotificationManager(getApplication())

            if (isFavorite(movie.id)) {
                localRepository.removeFavorite(movie)
            } else {
                localRepository.addFavorite(movie)
                notificationManager.sendMovieAddedNotification(movie)
            }
            loadFavorites()
        }
    }



    fun isFavorite(movieId: Int): Boolean {
        return _favorites.value.any { it.id == movieId }
    }

    private fun loadFavorites() {
        viewModelScope.launch {
            _favorites.value = localRepository.getAllFavorites()
        }
    }
}

data class MainUiState(
    val selectedTab: Int = 0
)