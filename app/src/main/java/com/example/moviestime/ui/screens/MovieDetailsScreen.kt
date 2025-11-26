package com.example.moviestime.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.moviestime.R
import com.example.moviestime.data.model.Movie
import com.example.moviestime.ui.theme.Inter
import com.example.moviestime.ui.theme.PlayFair
import com.example.moviestime.viewmodel.MainViewModel
import com.example.moviestime.viewmodel.MovieDetailsViewModel

@Composable
fun MovieDetailsScreen(
    movieId: Int,
    onBack: () -> Unit = {},
    mainViewModel: MainViewModel,
    onPlayClick: (Movie) -> Unit = {},
    onFavoriteClick: (Movie) -> Unit = {},
    onShareClick: (Movie) -> Unit = {}
) {
    val viewModel: MovieDetailsViewModel = viewModel()
    val movieState by viewModel.movieDetails.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val favorites by mainViewModel.favorites.collectAsState()

    LaunchedEffect(movieId) {
        viewModel.loadMovieDetails(movieId)
    }

    val backgroundColor = colorResource(R.color.background)
    val primaryColor = colorResource(R.color.primary)
    val textColor = colorResource(R.color.foreground)
    val cardColor = colorResource(R.color.card)
    val goldColor = colorResource(R.color.secondary)

    if (isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = primaryColor)
        }
    } else {
        movieState?.let { movie ->
            val isFav = favorites.any { it.id == movie.id }

            MovieDetailsContent(
                movie = movie,
                isFavorite = isFav,
                onBack = onBack,
                onPlayClick = { onPlayClick(movie) },
                onFavoriteClick = @androidx.annotation.RequiresPermission(android.Manifest.permission.POST_NOTIFICATIONS) { mainViewModel.toggleFavorite(movie) },
                backgroundColor = backgroundColor,
                primaryColor = primaryColor,
                textColor = textColor,
                cardColor = cardColor,
                goldColor = goldColor
            )
        } ?: run {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(backgroundColor),
                contentAlignment = Alignment.Center
            ) {
                Text("فشل تحميل تفاصيل الفيلم", color = textColor)
            }
        }
    }
}

@Composable
fun MovieDetailsContent(
    movie: Movie,
    isFavorite: Boolean,
    onBack: () -> Unit,
    onPlayClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    backgroundColor: Color,
    primaryColor: Color,
    textColor: Color,
    cardColor: Color,
    goldColor: Color
) {
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(420.dp)
            ) {
                AsyncImage(
                    model = movie.backdropPath ?: movie.posterPath,
                    contentDescription = "Backdrop",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .alpha(0.65f),
                    error = painterResource(R.drawable.ic_launcher_background)
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    backgroundColor.copy(alpha = 0.3f),
                                    backgroundColor.copy(alpha = 0.9f),
                                    backgroundColor
                                ),
                                startY = 100f
                            )
                        )
                )

                AsyncImage(
                    model = movie.posterPath,
                    contentDescription = "Poster",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(start = 20.dp, bottom = 0.dp)
                        .width(130.dp)
                        .height(190.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .border(1.dp, Color.White.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
                )

                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(start = 166.dp, bottom = 10.dp, end = 16.dp)
                ) {
                    Text(
                        text = movie.title,
                        fontFamily = PlayFair, // Corrected
                        fontWeight = FontWeight.Bold,
                        fontSize = 26.sp,
                        color = textColor,
                        lineHeight = 32.sp
                    )

                    Spacer(Modifier.height(8.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Star,
                            contentDescription = null,
                            tint = goldColor,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text = String.format("%.1f", movie.rating),
                            fontFamily = Inter,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = textColor
                        )

                        Spacer(Modifier.width(16.dp))

                        Text(
                            text = "${movie.duration} min",
                            fontFamily = Inter,
                            fontSize = 14.sp,
                            color = textColor.copy(alpha = 0.7f)
                        )
                    }

                    Spacer(Modifier.height(8.dp))

                    Surface(
                        color = cardColor,
                        shape = RoundedCornerShape(6.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, color = Color.White.copy(0.1f)),
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        Text(
                            text = movie.genre.split(",").firstOrNull() ?: "Movie",
                            fontFamily = Inter,
                            fontSize = 12.sp,
                            color = textColor.copy(alpha = 0.9f),
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        )
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 24.dp)
            ) {
                Button(
                    onClick = onFavoriteClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isFavorite) Color.DarkGray else primaryColor,
                        contentColor = Color.White
                    ),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
                ) {
                    Icon(
                        if (isFavorite) Icons.Default.Check else Icons.Default.Add,
                        contentDescription = null,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = if (isFavorite) "Listed" else "Add to Watchlist",
                        fontFamily = Inter,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp
                    )
                }

                Spacer(Modifier.height(32.dp))

                Text(
                    text = "Overview", // Corrected Typo
                    fontFamily = PlayFair,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    color = textColor
                )

                Spacer(Modifier.height(12.dp))

                Text(
                    text = movie.overview.ifEmpty { "No overview available." },
                    fontFamily = Inter,
                    fontSize = 15.sp,
                    color = textColor.copy(alpha = 0.8f),
                    lineHeight = 26.sp,
                    textAlign = TextAlign.Justify
                )

                Spacer(Modifier.height(32.dp))

                Text(
                    text = "Details",
                    fontFamily = PlayFair,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    color = textColor
                )

                Spacer(Modifier.height(16.dp))

                DetailItem(label = "Director", value = movie.director, textColor = textColor)
                Spacer(Modifier.height(16.dp))
                DetailItem(label = "Cast", value = movie.cast, textColor = textColor)

                Spacer(Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    DetailItem(label = "Release date", value = movie.year, textColor = textColor)
                    DetailItem(label = "Language", value = "English", textColor = textColor)
                }
            }

            Spacer(Modifier.height(50.dp))
        }

        IconButton(
            onClick = onBack,
            modifier = Modifier
                .padding(top = 45.dp, start = 20.dp)
                .size(42.dp)
                .background(Color.Black.copy(alpha = 0.6f), RoundedCornerShape(12.dp))
                .border(1.dp, Color.White.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
fun DetailItem(label: String, value: String, textColor: Color) {
    Column {
        Text(
            text = label,
            fontFamily = Inter,
            fontWeight = FontWeight.Medium,
            fontSize = 13.sp,
            color = textColor.copy(alpha = 0.5f)
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = value,
            fontFamily = Inter,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            color = textColor
        )
    }
}