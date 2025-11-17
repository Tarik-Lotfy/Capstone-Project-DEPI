package com.example.moviestime.data.repository

import android.content.Context
import com.example.moviestime.data.local.MovieDatabase
import com.example.moviestime.data.local.MovieEntity
import com.example.moviestime.data.local.toEntity
import com.example.moviestime.data.local.toMovie
import com.example.moviestime.data.model.Movie

class LocalMovieRepository(context: Context) {
    private val movieDao = MovieDatabase.getDatabase(context).movieDao()

    suspend fun getAllFavorites(): List<Movie> {
        return movieDao.getAllFavorites().map { it.toMovie() }
    }

    suspend fun isFavorite(movieId: Int): Boolean {
        return movieDao.getFavoriteById(movieId) != null
    }

    suspend fun addFavorite(movie: Movie) {
        movieDao.insertFavorite(movie.toEntity())
    }

    suspend fun removeFavorite(movie: Movie) {
        movieDao.deleteFavorite(movie.toEntity())
    }

    suspend fun clearAll() {
        movieDao.clearAllFavorites()
    }
}