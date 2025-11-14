package com.example.moviestime.data.remote

import com.example.moviestime.data.model.Movie

data class MovieDetailsDto(
    val id: Int,
    val title: String,
    val release_date: String,
    val overview: String,
    val vote_average: Double,
    val runtime: Int?,
    val poster_path: String?,
    val backdrop_path: String?,
    val genres: List<GenreDto> = emptyList()
)

data class GenreDto(
    val id: Int,
    val name: String
)

fun MovieDetailsDto.toMovie(): Movie {
    return Movie(
        id = id,
        title = title,
        year = release_date.takeIf { it.isNotEmpty() }?.split("-")?.getOrNull(0) ?: "N/A",
        genre = genres.joinToString(", ") { it.name },
        rating = vote_average.toFloat(),
        duration = runtime ?: 0,
        posterPath = poster_path?.let { "https://image.tmdb.org/t/p/w500$it" }
    )
}