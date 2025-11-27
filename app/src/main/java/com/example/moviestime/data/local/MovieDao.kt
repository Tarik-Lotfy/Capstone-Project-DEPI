package com.example.moviestime.data.local

import androidx.room.*

@Dao
interface MovieDao {

    // Favorites
    @Query("SELECT * FROM favorite_movies")
    suspend fun getAllFavorites(): List<MovieEntity>

    @Query("SELECT * FROM favorite_movies WHERE id = :movieId")
    suspend fun getFavoriteById(movieId: Int): MovieEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(movie: MovieEntity)

    @Delete
    suspend fun deleteFavorite(movie: MovieEntity)

    @Query("DELETE FROM favorite_movies")
    suspend fun clearAllFavorites()

    // Watchlist
    @Query("SELECT * FROM watchlist_movies")
    suspend fun getAllWatchlist(): List<WatchlistEntity>

    @Query("SELECT * FROM watchlist_movies WHERE id = :movieId")
    suspend fun getWatchlistById(movieId: Int): WatchlistEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWatchlist(movie: WatchlistEntity)

    @Delete
    suspend fun deleteWatchlist(movie: WatchlistEntity)

    // Watched
    @Query("SELECT * FROM watched_movies")
    suspend fun getAllWatched(): List<WatchedEntity>

    @Query("SELECT * FROM watched_movies WHERE id = :movieId")
    suspend fun getWatchedById(movieId: Int): WatchedEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWatched(movie: WatchedEntity)

    @Delete
    suspend fun deleteWatched(movie: WatchedEntity)
}