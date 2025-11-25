package com.example.moviestime.data.repository

import com.example.moviestime.data.remote.RetrofitClient
import com.example.moviestime.data.remote.toMovie
import com.example.moviestime.data.remote.toGenre
import com.example.moviestime.data.model.Movie
import com.example.moviestime.data.remote.Genre

class SearchRepository {
    private val api = RetrofitClient.apiService
    private val API_KEY = "a32aa23478c53097a5c3164eab6a4098"

    suspend fun searchMovies(query: String): List<Movie> {
        return api.searchMovies(API_KEY, query).results.map { it.toMovie() }
    }

    suspend fun getMovieGenres(): List<Genre> {
        return api.getMovieGenres(API_KEY).genres.map { it.toGenre() }
    }

    suspend fun getMoviesByGenre(genreId: Int): List<Movie> {
        return api.getMoviesByGenre(API_KEY, genreId).results.map { it.toMovie() }
    }

    suspend fun getPopularMovies(): List<com.example.moviestime.data.model.Movie> {
        return api.getPopularMovies(API_KEY).results.map { it.toMovie() }
    }
}