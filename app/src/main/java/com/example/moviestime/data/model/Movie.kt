package com.example.moviestime.data.model

data class Movie(
    val title: String,
    val year: String,
    val genre: String,
    val rating: Float,
    val duration: Int,
    val posterPath: String? = null,
    val id: Int = 0,
    val backdropPath: String? = null,
    val overview: String = "",
    val director: String = "",
    val cast: String = "",
    val trailerKey: String? = null,
    val directorInfo: Director? = null,
    val castMembers: List<CastMember> = emptyList()
)