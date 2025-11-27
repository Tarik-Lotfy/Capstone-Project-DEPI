package com.example.moviestime.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_movies")
data class MovieEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val year: String,
    val genre: String,
    val rating: Float,
    val duration: Int,
    val posterPath: String?
)

@Entity(tableName = "watchlist_movies")
data class WatchlistEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val year: String,
    val genre: String,
    val rating: Float,
    val duration: Int,
    val posterPath: String?
)

@Entity(tableName = "watched_movies")
data class WatchedEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val year: String,
    val genre: String,
    val rating: Float,
    val duration: Int,
    val posterPath: String?
)

// Extension function to convert Entity to Domain Model
fun MovieEntity.toMovie(): com.example.moviestime.data.model.Movie {
    return com.example.moviestime.data.model.Movie(
        id = id,
        title = title,
        year = year,
        genre = genre,
        rating = rating,
        duration = duration,
        posterPath = posterPath
    )
}

fun WatchlistEntity.toMovie(): com.example.moviestime.data.model.Movie {
    return com.example.moviestime.data.model.Movie(
        id = id,
        title = title,
        year = year,
        genre = genre,
        rating = rating,
        duration = duration,
        posterPath = posterPath
    )
}

fun WatchedEntity.toMovie(): com.example.moviestime.data.model.Movie {
    return com.example.moviestime.data.model.Movie(
        id = id,
        title = title,
        year = year,
        genre = genre,
        rating = rating,
        duration = duration,
        posterPath = posterPath
    )
}

// Extension function to convert Domain Model to Entity
fun com.example.moviestime.data.model.Movie.toEntity(): MovieEntity {
    return MovieEntity(
        id = id,
        title = title,
        year = year,
        genre = genre,
        rating = rating,
        duration = duration,
        posterPath = posterPath
    )
}

fun com.example.moviestime.data.model.Movie.toWatchlistEntity(): WatchlistEntity {
    return WatchlistEntity(
        id = id,
        title = title,
        year = year,
        genre = genre,
        rating = rating,
        duration = duration,
        posterPath = posterPath
    )
}

fun com.example.moviestime.data.model.Movie.toWatchedEntity(): WatchedEntity {
    return WatchedEntity(
        id = id,
        title = title,
        year = year,
        genre = genre,
        rating = rating,
        duration = duration,
        posterPath = posterPath
    )
}