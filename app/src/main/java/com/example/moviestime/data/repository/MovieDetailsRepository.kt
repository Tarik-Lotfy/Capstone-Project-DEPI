package com.example.moviestime.data.repository

import com.example.moviestime.data.remote.RetrofitClient
import com.example.moviestime.data.remote.MovieDetailsDto
import com.example.moviestime.data.remote.MovieVideosResponse
import com.example.moviestime.data.remote.MovieResponse
import com.example.moviestime.data.remote.MovieCreditsResponse

class MovieDetailsRepository {
    private val api = RetrofitClient.apiService
    private val API_KEY = "a32aa23478c53097a5c3164eab6a4098"

    suspend fun getMovieDetails(movieId: Int): MovieDetailsDto {
        return api.getMovieDetails(movieId, API_KEY)
    }

    suspend fun getMovieVideos(movieId: Int): MovieVideosResponse {
        return api.getMovieVideos(movieId, API_KEY)
    }

    suspend fun getSimilarMovies(movieId: Int): MovieResponse {
        return api.getSimilarMovies(movieId, API_KEY)
    }

    suspend fun getMovieCredits(movieId: Int): MovieCreditsResponse {
        return api.getMovieCredits(movieId, API_KEY)
    }
}