package com.example.moviestime.data.repository

import com.example.moviestime.data.remote.RetrofitClient
import com.example.moviestime.data.remote.toMovie

class SearchRepository {
    private val api = RetrofitClient.apiService
    private val API_KEY = "a32aa23478c53097a5c3164eab6a4098"

    suspend fun searchMovies(query: String): List<com.example.moviestime.data.model.Movie> {
        return api.searchMovies(API_KEY, query).results.map { it.toMovie() }
    }
}