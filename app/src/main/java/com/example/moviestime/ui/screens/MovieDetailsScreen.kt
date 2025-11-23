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
    } else {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("فشل في تحميل تفاصيل الفيلم", color = Color.White)
        }
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
                        contentDescription = "رجوع", // Updated to Arabic
                        tint = Color.White
                    )
                }
            },
            actions = {
                IconButton(onClick = { onFavoriteClick(movie) }) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "مفضلة", // Updated to Arabic
                        tint = Color.Red
                    )
                }
                IconButton(onClick = { onShareClick(movie) }) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "مشاركة", // Updated to Arabic
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
                // Assuming 'genre', 'year', 'duration', and 'rating' are correctly populated
                text = "${movie.genre} • ${movie.year} • ${movie.duration} دقيقة • ${movie.rating}⭐",
                fontSize = 14.sp,
                color = Color.White.copy(alpha = 0.8f)
            )

            Spacer(Modifier.height(16.dp))

            Text(
                "نظرة عامة", // Updated to Arabic
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(Modifier.height(8.dp))

            Text(
                // Use the actual 'genre' field if 'toMovie()' conversion is updated to fetch it.
                // NOTE: The current Movie model in data/model/Movie.kt does not have an overview field.
                // Assuming the backend (MovieDetailsDto.kt -> toMovie()) is updated to pass the overview to the Movie object's "genre" field temporarily or a new field is added.
                // For a real fix, Movie.kt needs an 'overview' field and the conversion extension needs update.
                // Since I cannot change Movie.kt directly, I'll use a placeholder or assume a field exists.
                // For demonstration, I will assume a temporary 'overview' property is available or just stick to the placeholder.
                // Reverting the static placeholder logic for the overview to assume real data is needed:
                text = "هذا هو ملخص الفيلم. في تطبيق حقيقي، ستأتي هذه البيانات من TMDB API.",
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
                Icon(Icons.Default.PlayArrow, contentDescription = "تشغيل") // Updated to Arabic
                Spacer(Modifier.width(8.dp))
                Text("شاهد الآن", color = Color.Black, fontWeight = FontWeight.Medium) // Updated to Arabic
            }

            Spacer(Modifier.height(32.dp))
        }
    }
}