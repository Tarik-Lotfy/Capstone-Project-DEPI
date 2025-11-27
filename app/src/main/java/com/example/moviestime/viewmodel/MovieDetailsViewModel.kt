package com.example.moviestime.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.example.moviestime.data.model.Movie
import com.example.moviestime.data.remote.toMovie
import com.example.moviestime.data.repository.MovieDetailsRepository

class MovieDetailsViewModel : ViewModel() {
    private val repository = MovieDetailsRepository()
    private val _movieDetails = MutableStateFlow<Movie?>(null)
    val movieDetails: StateFlow<Movie?> = _movieDetails.asStateFlow()
    private val _similarMovies = MutableStateFlow<List<Movie>>(emptyList())
    val similarMovies: StateFlow<List<Movie>> = _similarMovies.asStateFlow()
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun loadMovieDetails(movieId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val details = repository.getMovieDetails(movieId)

                val videosResponse = repository.getMovieVideos(movieId)
                val similarMoviesResponse = repository.getSimilarMovies(movieId)
                val trailerKey = videosResponse.results.firstOrNull {
                    it.site == "YouTube" && it.type == "Trailer"
                }?.key

                _movieDetails.value = details.toMovie(trailerKey = trailerKey)
                _similarMovies.value = similarMoviesResponse.results.map { it.toMovie() }

            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }
}