package com.example.moviestime.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.example.moviestime.data.model.Movie
import com.example.moviestime.data.repository.SearchRepository
import com.example.moviestime.data.remote.Genre

class SearchViewModel : ViewModel() {
    private val repository = SearchRepository()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _searchResults = MutableStateFlow<List<Movie>>(emptyList())
    val searchResults: StateFlow<List<Movie>> = _searchResults.asStateFlow()

    private val _genres = MutableStateFlow<List<Genre>>(emptyList())
    val genres: StateFlow<List<Genre>> = _genres.asStateFlow()

    private val _selectedGenreId = MutableStateFlow<Int?>(null)
    val selectedGenreId: StateFlow<Int?> = _selectedGenreId.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private var searchJob: Job? = null
    private var discoverJob: Job? = null

    init {
        fetchMovieGenres()
        fetchRecommendedMovies()
    }

    private fun fetchMovieGenres() {
        viewModelScope.launch {
            try {
                val genreList = repository.getMovieGenres()
                _genres.value = genreList
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun fetchRecommendedMovies() {
        discoverJob?.cancel()
        discoverJob = viewModelScope.launch {
            _isLoading.value = true
            _selectedGenreId.value = null
            _searchQuery.value = ""
            try {
                val results = repository.getPopularMovies()
                _searchResults.value = results
            } catch (e: Exception) {
                e.printStackTrace()
                _searchResults.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }


    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query

        _selectedGenreId.value = null
        if (query.length < 2) {
            _searchResults.value = emptyList()
        }

        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(500)
            if (query.length >= 2) {
                performSearch(query)
            }
        }
    }

    private suspend fun performSearch(query: String) {
        _isLoading.value = true
        try {
            val results = repository.searchMovies(query)
            _searchResults.value = results
        } catch (e: Exception) {
            e.printStackTrace()
            _searchResults.value = emptyList()
        } finally {
            _isLoading.value = false
        }
    }

    fun onGenreSelected(genreId: Int) {
        _searchQuery.value = ""
        _searchResults.value = emptyList()

        if (genreId == 0) {
            if (_selectedGenreId.value == null) return

            _selectedGenreId.value = null
            fetchRecommendedMovies()
            return
        }

        if (_selectedGenreId.value == genreId) {
            _selectedGenreId.value = null
            fetchRecommendedMovies()
            return
        }

        _selectedGenreId.value = genreId
        discoverJob?.cancel()
        discoverJob = viewModelScope.launch {
            performGenreDiscovery(genreId)
        }
    }

    private suspend fun performGenreDiscovery(genreId: Int) {
        _isLoading.value = true
        try {
            val results = repository.getMoviesByGenre(genreId)
            _searchResults.value = results
        } catch (e: Exception) {
            e.printStackTrace()
            _searchResults.value = emptyList()
        } finally {
            _isLoading.value = false
        }
    }
}