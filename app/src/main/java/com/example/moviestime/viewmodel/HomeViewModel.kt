package com.example.moviestime.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.example.moviestime.data.model.Movie
import com.example.moviestime.data.repository.MovieRepository

class HomeViewModel : ViewModel() {
    private val repository = MovieRepository()

    private val _popular = MutableStateFlow<List<Movie>>(emptyList())
    val popular: StateFlow<List<Movie>> = _popular.asStateFlow()

    private val _topRated = MutableStateFlow<List<Movie>>(emptyList())
    val topRated: StateFlow<List<Movie>> = _topRated.asStateFlow()

    private val _nowPlaying = MutableStateFlow<List<Movie>>(emptyList())
    val nowPlaying: StateFlow<List<Movie>> = _nowPlaying.asStateFlow()

    private val _upcoming = MutableStateFlow<List<Movie>>(emptyList())
    val upcoming: StateFlow<List<Movie>> = _upcoming.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        loadMovies()
    }

    fun loadMovies() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                Log.d("HomeViewModel", "Loading movies...")
                _popular.value = repository.getPopularMovies()
                Log.d("HomeViewModel", "Popular loaded: ${_popular.value.size}")
                _topRated.value = repository.getTopRatedMovies()
                _nowPlaying.value = repository.getNowPlayingMovies()
                _upcoming.value = repository.getUpcomingMovies()
                Log.d("HomeViewModel", "All movies loaded successfully")
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Error loading movies", e)
                _error.value = e.message ?: "Failed to load movies"
            } finally {
                _isLoading.value = false
            }
        }
    }
}