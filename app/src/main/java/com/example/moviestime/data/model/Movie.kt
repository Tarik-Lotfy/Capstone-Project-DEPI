package com.example.moviestime.data.model

data class Movie(
    val title: String,
    val year: String,
    val genre: String,
    val rating: Float,
    val duration: Int,
    val posterPath: String? = null,
    val id: Int = 0
)