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
    val backdrop_path: String?, // أضفنا هذا
    val genre_ids: List<Int> = emptyList(),
    val runtime: Int? = null
)

fun MovieDto.toMovie(): Movie {
    return Movie(
        id = id,
        title = title,
        year = release_date.takeIf { !it.isNullOrEmpty() }?.split("-")?.getOrNull(0) ?: "N/A",
        genre = "", // سيتم تحديث الأنواع لاحقًا أو تجاهلها في القوائم البسيطة
        rating = vote_average.toFloat(),
        duration = runtime ?: 0,
        posterPath = poster_path?.let { "https://image.tmdb.org/t/p/w500$it" },
        backdropPath = backdrop_path?.let { "https://image.tmdb.org/t/p/w780$it" },
        overview = overview,
        director = "",
        cast = ""
    )
}