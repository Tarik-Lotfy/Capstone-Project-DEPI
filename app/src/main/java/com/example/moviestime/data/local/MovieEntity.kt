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