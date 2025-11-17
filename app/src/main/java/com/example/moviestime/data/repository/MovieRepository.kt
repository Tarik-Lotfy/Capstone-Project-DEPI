package com.example.moviestime.data.repository

import com.example.moviestime.data.model.Movie
import com.example.moviestime.data.remote.RetrofitClient
import com.example.moviestime.data.remote.toMovie

class MovieRepository {
    private val api = RetrofitClient.apiService
    private val API_KEY = "a32aa23478c53097a5c3164eab6a4098"
    suspend fun getPopularMovies(): List<Movie> {
        return api.getPopularMovies(API_KEY).results.map { it.toMovie() }
    }

    suspend fun getTopRatedMovies(): List<Movie> {
        return api.getTopRatedMovies(API_KEY).results.map { it.toMovie() }
    }

    suspend fun getNowPlayingMovies(): List<Movie> {
        return api.getNowPlayingMovies(API_KEY).results.map { it.toMovie() }
    }

    suspend fun getUpcomingMovies(): List<Movie> {
        return api.getUpcomingMovies(API_KEY).results.map { it.toMovie() }
    }
}