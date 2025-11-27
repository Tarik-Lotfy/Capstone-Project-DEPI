package com.example.moviestime.data.remote

import com.example.moviestime.data.model.Movie
import com.example.moviestime.data.model.CastMember
import com.example.moviestime.data.model.Director

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

data class MovieCreditsResponse(
    val id: Int,
    val cast: List<CastDto>,
    val crew: List<CrewDto>
)

data class CastDto(
    val id: Int,
    val name: String,
    val character: String,
    val order: Int,
    val profile_path: String? = null
)

data class CrewDto(
    val id: Int,
    val name: String,
    val job: String,
    val profile_path: String? = null
)

fun MovieDetailsDto.toMovie(
    trailerKey: String? = null,
    director: String? = null,
    cast: String? = null,
    directorInfo: Director? = null,
    castMembers: List<CastMember> = emptyList()
): Movie {
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
        director = director ?: "",
        cast = cast ?: "",
        trailerKey = trailerKey,
        directorInfo = directorInfo,
        castMembers = castMembers
    )
}