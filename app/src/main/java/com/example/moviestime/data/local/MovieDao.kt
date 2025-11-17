package com.example.moviestime.data.local

import androidx.room.*

@Dao
interface MovieDao {

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
}