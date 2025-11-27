package com.example.moviestime.data.repository

import android.content.Context
import com.example.moviestime.data.local.MovieDatabase
import com.example.moviestime.data.local.toEntity
import com.example.moviestime.data.local.toWatchlistEntity
import com.example.moviestime.data.local.toWatchedEntity
import com.example.moviestime.data.local.toMovie
import com.example.moviestime.data.model.Movie

class LocalMovieRepository(context: Context) {
    private val movieDao = MovieDatabase.getDatabase(context).movieDao()

    // Favorites
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

    // Watchlist
    suspend fun getAllWatchlist(): List<Movie> {
        return movieDao.getAllWatchlist().map { it.toMovie() }
    }

    suspend fun isInWatchlist(movieId: Int): Boolean {
        return movieDao.getWatchlistById(movieId) != null
    }

    suspend fun addToWatchlist(movie: Movie) {
        movieDao.insertWatchlist(movie.toWatchlistEntity())
    }

    suspend fun removeFromWatchlist(movie: Movie) {
        movieDao.deleteWatchlist(movie.toWatchlistEntity())
    }

    // Watched
    suspend fun getAllWatched(): List<Movie> {
        return movieDao.getAllWatched().map { it.toMovie() }
    }

    suspend fun isWatched(movieId: Int): Boolean {
        return movieDao.getWatchedById(movieId) != null
    }

    suspend fun addToWatched(movie: Movie) {
        movieDao.insertWatched(movie.toWatchedEntity())
    }

    suspend fun removeFromWatched(movie: Movie) {
        movieDao.deleteWatched(movie.toWatchedEntity())
    }
}