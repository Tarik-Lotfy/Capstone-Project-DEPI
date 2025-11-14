package com.example.moviestime.viewmodel

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

    init {
        loadMovies()
    }

    fun loadMovies() {
        viewModelScope.launch {
            try {
                _popular.value = repository.getPopularMovies()
                _topRated.value = repository.getTopRatedMovies()
                _nowPlaying.value = repository.getNowPlayingMovies()
                _upcoming.value = repository.getUpcomingMovies()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}