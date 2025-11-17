package com.example.moviestime.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.moviestime.data.model.Movie
import com.example.moviestime.viewmodel.MainViewModel
import com.example.moviestime.viewmodel.MovieDetailsViewModel

@Composable
@androidx.annotation.RequiresPermission(android.Manifest.permission.POST_NOTIFICATIONS)
fun MovieDetailsScreen(
    movieId: Int,
    onBack: () -> Unit = {},
    mainViewModel: MainViewModel,
    onPlayClick: (Movie) -> Unit = {},
    onFavoriteClick: (Movie) -> Unit = {},
    onShareClick: (Movie) -> Unit = {}
) {
    val viewModel: MovieDetailsViewModel = viewModel()
    val movie by viewModel.movieDetails.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    LaunchedEffect(movieId) {
        viewModel.loadMovieDetails(movieId)
    }

    if (isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
        }
    } else if (movie != null) {
        MovieDetailsContent(
            movie = movie!!,
            onBack = onBack,
            onPlayClick = onPlayClick,
            onFavoriteClick = {
                mainViewModel.toggleFavorite(movie!!)
            },
            onShareClick = onShareClick
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDetailsContent(
    movie: Movie,
    onBack: () -> Unit,
    onPlayClick: (Movie) -> Unit,
    onFavoriteClick: (Movie) -> Unit,
    onShareClick: (Movie) -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .verticalScroll(scrollState)
    ) {
        TopAppBar(
            title = { },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
            },
            actions = {
                IconButton(onClick = { onFavoriteClick(movie) }) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "Favorite",
                        tint = Color.Red
                    )
                }
                IconButton(onClick = { onShareClick(movie) }) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Share",
                        tint = Color.White
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent
            )
        )

        AsyncImage(
            model = movie.posterPath?.replace("w500", "w780"),
            contentDescription = movie.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
        )

        Spacer(Modifier.height(16.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = movie.title,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = "${movie.genre} • ${movie.year} • ${movie.duration} min • ${movie.rating}⭐",
                fontSize = 14.sp,
                color = Color.White.copy(alpha = 0.8f)
            )

            Spacer(Modifier.height(16.dp))

            Text(
                text = "Overview",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = "This is the overview of the movie. In a real app, this would come from TMDB API.",
                fontSize = 14.sp,
                color = Color.White.copy(alpha = 0.9f),
                lineHeight = 20.sp
            )

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = { onPlayClick(movie) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFC107))
            ) {
                Icon(Icons.Default.PlayArrow, contentDescription = "Play")
                Spacer(Modifier.width(8.dp))
                Text("Watch Now", color = Color.Black, fontWeight = FontWeight.Medium)
            }

            Spacer(Modifier.height(32.dp))
        }
    }
}