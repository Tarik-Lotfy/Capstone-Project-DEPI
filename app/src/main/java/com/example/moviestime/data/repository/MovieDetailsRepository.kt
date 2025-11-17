package com.example.moviestime.data.repository

import com.example.moviestime.data.remote.RetrofitClient
import com.example.moviestime.data.remote.MovieDetailsDto

class MovieDetailsRepository {
    private val api = RetrofitClient.apiService
    private val API_KEY = "a32aa23478c53097a5c3164eab6a4098"

    suspend fun getMovieDetails(movieId: Int): MovieDetailsDto {
        return api.getMovieDetails(movieId, API_KEY)
    }
}