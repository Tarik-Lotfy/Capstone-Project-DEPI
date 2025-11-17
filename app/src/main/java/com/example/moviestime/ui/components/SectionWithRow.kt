package com.example.moviestime.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SectionWithRow(
    title: String,
    movies: List<com.example.moviestime.data.model.Movie>,
    favorites: List<com.example.moviestime.data.model.Movie> = emptyList(),
    onMovieClick: (com.example.moviestime.data.model.Movie) -> Unit = {},
    onFavoriteClick: (com.example.moviestime.data.model.Movie) -> Unit = {}
) {
    Column {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = androidx.compose.ui.graphics.Color.White
            )
            TextButton(onClick = {}) {
                Text(
                    "See All",
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        Spacer(Modifier.height(8.dp))

        androidx.compose.foundation.lazy.LazyRow(horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(12.dp)) {
            items(movies) { movie ->
                MovieCardSmall(
                    movie = movie,
                    isFavorite = favorites.any { it.id == movie.id },
                    onMovieClick = onMovieClick,
                    onFavoriteClick = onFavoriteClick
                )
            }
        }
    }
}