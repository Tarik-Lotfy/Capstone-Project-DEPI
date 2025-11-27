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

    private val _watchlist = MutableStateFlow<List<Movie>>(emptyList())
    val watchlist: StateFlow<List<Movie>> = _watchlist.asStateFlow()

    private val _watched = MutableStateFlow<List<Movie>>(emptyList())
    val watched: StateFlow<List<Movie>> = _watched.asStateFlow()

    init {
        loadFavorites()
        loadWatchlist()
        loadWatched()
    }

    fun selectTab(index: Int) {
        _uiState.value = _uiState.value.copy(selectedTab = index)
    }

    @androidx.annotation.RequiresPermission(android.Manifest.permission.POST_NOTIFICATIONS)
    fun toggleFavorite(movie: Movie) {
        viewModelScope.launch {
            val notificationManager = AppNotificationManager(getApplication())
            val currentlyFavorite = localRepository.isFavorite(movie.id)

            if (currentlyFavorite) {
                localRepository.removeFavorite(movie)
            } else {
                localRepository.addFavorite(movie)
                notificationManager.sendMovieAddedNotification(movie)
            }
            loadFavorites()
        }
    }

    fun toggleWatchlist(movie: Movie) {
        viewModelScope.launch {
            val currentlyInWatchlist = localRepository.isInWatchlist(movie.id)

            if (currentlyInWatchlist) {
                localRepository.removeFromWatchlist(movie)
            } else {
                localRepository.addToWatchlist(movie)
            }
            loadWatchlist()
        }
    }

    fun toggleWatched(movie: Movie) {
        viewModelScope.launch {
            val currentlyWatched = localRepository.isWatched(movie.id)

            if (currentlyWatched) {
                localRepository.removeFromWatched(movie)
            } else {
                localRepository.addToWatched(movie)
            }
            loadWatched()
        }
    }

    fun isFavorite(movieId: Int): Boolean {
        return _favorites.value.any { it.id == movieId }
    }

    fun isInWatchlist(movieId: Int): Boolean {
        return _watchlist.value.any { it.id == movieId }
    }

    fun isWatched(movieId: Int): Boolean {
        return _watched.value.any { it.id == movieId }
    }

    private fun loadFavorites() {
        viewModelScope.launch {
            _favorites.value = localRepository.getAllFavorites()
        }
    }

    private fun loadWatchlist() {
        viewModelScope.launch {
            _watchlist.value = localRepository.getAllWatchlist()
        }
    }

    private fun loadWatched() {
        viewModelScope.launch {
            _watched.value = localRepository.getAllWatched()
        }
    }
}

data class MainUiState(
    val selectedTab: Int = 0
)