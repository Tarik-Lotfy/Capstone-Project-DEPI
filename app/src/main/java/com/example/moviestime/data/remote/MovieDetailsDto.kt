package com.example.moviestime.data.remote

import com.example.moviestime.data.model.Movie

data class MovieDetailsDto(
    val id: Int,
    val title: String,
    val release_date: String?,
    val overview: String?,
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

data class MovieVideosResponse(
    val id: Int,
    val results: List<VideoDto>
)

data class VideoDto(
    val id: String,
    val key: String,
    val name: String,
    val site: String,
    val type: String
)

fun MovieDetailsDto.toMovie(trailerKey: String? = null): Movie {
    return Movie(
        id = id,
        title = title,
        year = release_date?.takeIf { it.isNotEmpty() }?.split("-")?.getOrNull(0) ?: "N/A",
        genre = genres.firstOrNull()?.name ?: "",
        rating = vote_average.toFloat(),
        duration = runtime ?: 0,
        posterPath = poster_path?.let { "https://image.tmdb.org/t/p/w500$it" },
        backdropPath = backdrop_path?.let { "https://image.tmdb.org/t/p/w1280$it" },
        overview = overview ?: "",
        director = "",
        cast = "",
        trailerKey = trailerKey
    )
}