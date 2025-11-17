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

class SearchViewModel : ViewModel() {
    private val repository = SearchRepository()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _searchResults = MutableStateFlow<List<Movie>>(emptyList())
    val searchResults: StateFlow<List<Movie>> = _searchResults.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private var searchJob: Job? = null

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query

        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(500)
            if (query.length >= 2) {
                performSearch(query)
            } else {
                _searchResults.value = emptyList()
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
}