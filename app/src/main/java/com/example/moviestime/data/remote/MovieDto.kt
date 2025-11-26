package com.example.moviestime.data.remote

import com.example.moviestime.data.model.Movie

data class MovieResponse(
    val page: Int,
    val results: List<MovieDto>,
    val total_pages: Int,
    val total_results: Int
)
data class MovieDto(
    val id: Int,
    val title: String,
    val release_date: String,
    val overview: String,
    val vote_average: Double,
    val poster_path: String?,
    val genre_ids: List<Int> = emptyList(),
    val runtime: Int? = null
)

fun MovieDto.toMovie(): Movie {
    return Movie(
        id = id,
        title = title,
        year = release_date.takeIf { it.isNotEmpty() }?.split("-")?.getOrNull(0) ?: "N/A",
        genre = "",
        rating = vote_average.toFloat(),
        duration = runtime ?: 0,
        posterPath = poster_path?.let { "https://image.tmdb.org/t/p/w500$it" }
    )
}

data class GenreListResponse(
    val genres: List<GenreDto>
)

data class Genre(
    val id: Int,
    val name: String
)

fun GenreDto.toGenre(): Genre {
    return Genre(
        id = id,
        name = name
    )
}