package com.example.moviestime.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
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
import com.example.moviestime.data.model.CastMember
import com.example.moviestime.data.model.Director
import com.example.moviestime.ui.components.SectionWithRow
import com.example.moviestime.ui.theme.Inter
import com.example.moviestime.ui.theme.PlayFair
import com.example.moviestime.viewmodel.MainViewModel
import com.example.moviestime.viewmodel.MovieDetailsViewModel

import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.List
import com.example.moviestime.ui.components.MovieRowCard

@SuppressLint("MissingPermission")
@Composable
fun MovieDetailsScreen(
    movieId: Int,
    onBack: () -> Unit = {},
    mainViewModel: MainViewModel,
    onPlayClick: (Movie) -> Unit = {},
    onFavoriteClick: (Movie) -> Unit = {},
    onShareClick: (Movie) -> Unit = {},
    onMovieClick: (Int) -> Unit
) {
    val viewModel: MovieDetailsViewModel = viewModel()
    val movieState by viewModel.movieDetails.collectAsState()
    val similarMovies by viewModel.similarMovies.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val favorites by mainViewModel.favorites.collectAsState()

    val watchedList by mainViewModel.watched.collectAsState()
    val watchlist by mainViewModel.watchlist.collectAsState()

    LaunchedEffect(movieId) {
        viewModel.loadMovieDetails(movieId)
    }

    val backgroundColor = colorResource(R.color.background)
    val primaryColor = colorResource(R.color.primary)
    val textColor = colorResource(R.color.foreground)
    val cardColor = colorResource(R.color.card)
    val goldColor = colorResource(R.color.secondary)
    val accentColor = colorResource(R.color.accent)

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
            val isWatched = watchedList.any { it.id == movie.id }
            val isInWatchlist = watchlist.any { it.id == movie.id }

            MovieDetailsContent(
                movie = movie,
                similarMovies = similarMovies,
                isFavorite = isFav,
                isWatched = isWatched,
                isInWatchlist = isInWatchlist,
                onBack = onBack,
                onPlayClick = { onPlayClick(movie) },
                onFavoriteClick = { mainViewModel.toggleFavorite(movie) },
                onWatchedClick = { mainViewModel.toggleWatched(movie) },
                onWatchlistClick = { mainViewModel.toggleWatchlist(movie) },
                onMovieClick = onMovieClick,
                onFavoriteMovieClick = { movieToFav -> mainViewModel.toggleFavorite(movieToFav) },
                backgroundColor = backgroundColor,
                primaryColor = primaryColor,
                textColor = textColor,
                cardColor = cardColor,
                goldColor = goldColor,
                accentColor = accentColor
            )
        } ?: run {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(backgroundColor),
                contentAlignment = Alignment.Center
            ) {
                Text("Failed to load movie details", color = textColor)
            }
        }
    }
}

@Composable
fun MovieDetailsContent(
    movie: Movie,
    similarMovies: List<Movie>,
    isFavorite: Boolean,
    isWatched: Boolean,
    isInWatchlist: Boolean,
    onBack: () -> Unit,
    onPlayClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    onWatchedClick: () -> Unit,
    onWatchlistClick: () -> Unit,
    onMovieClick: (Int) -> Unit,
    onFavoriteMovieClick: (Movie) -> Unit,
    backgroundColor: Color,
    primaryColor: Color,
    textColor: Color,
    cardColor: Color,
    goldColor: Color,
    accentColor: Color
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
                        .padding(start = 20.dp)
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
                        fontFamily = PlayFair,
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
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                        )
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 24.dp)
            ) {
                val trailerKey = movie.trailerKey

                if (trailerKey != null) {
                    Button(
                        onClick = onPlayClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = primaryColor,
                            contentColor = Color.White
                        ),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_move),
                            contentDescription = "Trailer",
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(Modifier.width(10.dp))
                        Text(
                            text = "Play Trailer",
                            fontFamily = Inter,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    }

                    Spacer(Modifier.height(20.dp))
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(20.dp,
                        alignment = Alignment.CenterHorizontally),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CircularToggleButton(
                        icon = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Favorite",
                        isActive = isFavorite,
                        onClick = onFavoriteClick,
                        activeColor = goldColor,
                        cardColor = cardColor,
                        textColor = textColor
                    )

                    CircularToggleButton(
                        icon = if (isWatched) Icons.Default.Check else Icons.Default.Visibility,
                        contentDescription = "Watched",
                        isActive = isWatched,
                        onClick = onWatchedClick,
                        activeColor = primaryColor,
                        cardColor = cardColor,
                        textColor = textColor
                    )

                    CircularToggleButton(
                        icon = Icons.Default.List,
                        contentDescription = "Watchlist",
                        isActive = isInWatchlist,
                        onClick = onWatchlistClick,
                        activeColor = accentColor,
                        cardColor = cardColor,
                        textColor = textColor
                    )
                }


                Spacer(Modifier.height(24.dp))

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = cardColor
                    ),
                    shape = RoundedCornerShape(14.dp),
                    border = BorderStroke(1.dp, Color.White.copy(0.08f))
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp)
                    ) {
                        Text(
                            text = "Overview",
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
                    }
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = cardColor
                    ),
                    shape = RoundedCornerShape(14.dp),
                    border = BorderStroke(1.dp, Color.White.copy(0.08f))
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp)
                    ) {
                        Text(
                            text = "Details",
                            fontFamily = PlayFair,
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp,
                            color = textColor
                        )

                        Spacer(Modifier.height(16.dp))

                        // Director with image
                        if (movie.directorInfo != null) {
                            DirectorItem(
                                director = movie.directorInfo,
                                textColor = textColor
                            )
                        } else {
                            DetailItem(label = "Director", value = movie.director, textColor = textColor)
                        }
                        
                        Spacer(Modifier.height(16.dp))
                        
                        // Cast with images
                        if (movie.castMembers.isNotEmpty()) {
                            CastSection(
                                castMembers = movie.castMembers,
                                textColor = textColor
                            )
                        } else {
                            DetailItem(label = "Cast", value = movie.cast, textColor = textColor)
                        }

                        Spacer(Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            DetailItem(label = "Release date", value = movie.year, textColor = textColor)
                            DetailItem(label = "Language", value = "English", textColor = textColor)
                        }
                    }
                }

                if (similarMovies.isNotEmpty()) {
                    Spacer(Modifier.height(24.dp))

                    Text(
                        text = "More Like This",
                        color = textColor,
                        fontFamily = PlayFair,
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp,
                        modifier = Modifier.padding(horizontal = 10.dp)
                    )

                    Spacer(Modifier.height(12.dp))

                    LazyRow(
                        contentPadding = PaddingValues(start = 0.dp, end = 20.dp)
                    ) {
                        items(similarMovies.size) { index ->
                            val movie = similarMovies[index]
                            MovieRowCard(
                                movie = movie,
                                onMovieClick = {
                                    onMovieClick(movie.id)
                                }
                            )
                            Spacer(Modifier.width(12.dp))
                        }
                    }
                }
            }

            Spacer(Modifier.height(100.dp))
        }

        
    }
}

@Composable
fun CircularToggleButton(
    icon: ImageVector,
    contentDescription: String,
    isActive: Boolean,
    onClick: () -> Unit,
    activeColor: Color,
    cardColor: Color,
    textColor: Color
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Card(
            onClick = onClick,
            modifier = Modifier.size(56.dp),
            shape = CircleShape,
            colors = CardDefaults.cardColors(
                containerColor = if (isActive) activeColor.copy(alpha = 0.3f) else cardColor,
                contentColor = if (isActive) activeColor else textColor
            ),
            border = BorderStroke(1.dp, if (isActive) activeColor else textColor.copy(alpha = 0.3f))
        ) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Icon(
                    icon,
                    contentDescription = contentDescription,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
        Spacer(Modifier.height(4.dp))
        Text(
            text = contentDescription.split(" ").firstOrNull() ?: "",
            fontFamily = Inter,
            fontSize = 11.sp,
            color = textColor.copy(alpha = 0.7f)
        )
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
            text = value.ifEmpty { "N/A" },
            fontFamily = Inter,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            color = textColor
        )
    }
}

@Composable
fun DirectorItem(
    director: Director,
    textColor: Color
) {
    Column {
        Text(
            text = "Director",
            fontFamily = Inter,
            fontWeight = FontWeight.Medium,
            fontSize = 13.sp,
            color = textColor.copy(alpha = 0.5f)
        )
        Spacer(Modifier.height(8.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AsyncImage(
                model = director.profilePath,
                contentDescription = director.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .border(2.dp, textColor.copy(alpha = 0.2f), CircleShape),
                placeholder = painterResource(R.drawable.ic_launcher_background),
                error = painterResource(R.drawable.ic_launcher_background)
            )
            Column {
                Text(
                    text = director.name,
                    fontFamily = Inter,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    color = textColor
                )
            }
        }
    }
}

@Composable
fun CastSection(
    castMembers: List<CastMember>,
    textColor: Color
) {
    Column {
        Text(
            text = "Cast",
            fontFamily = Inter,
            fontWeight = FontWeight.Medium,
            fontSize = 13.sp,
            color = textColor.copy(alpha = 0.5f)
        )
        Spacer(Modifier.height(12.dp))
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(castMembers.size) { index ->
                val castMember = castMembers[index]
                CastMemberItem(
                    castMember = castMember,
                    textColor = textColor
                )
            }
        }
    }
}

@Composable
fun CastMemberItem(
    castMember: CastMember,
    textColor: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(80.dp)
    ) {
        AsyncImage(
            model = castMember.profilePath,
            contentDescription = castMember.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .border(2.dp, textColor.copy(alpha = 0.2f), CircleShape),
            placeholder = painterResource(R.drawable.ic_launcher_background),
            error = painterResource(R.drawable.ic_launcher_background)
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = castMember.name,
            fontFamily = Inter,
            fontWeight = FontWeight.SemiBold,
            fontSize = 12.sp,
            color = textColor,
            textAlign = TextAlign.Center,
            maxLines = 2
        )
        if (castMember.character.isNotEmpty()) {
            Text(
                text = castMember.character,
                fontFamily = Inter,
                fontSize = 10.sp,
                color = textColor.copy(alpha = 0.7f),
                textAlign = TextAlign.Center,
                maxLines = 1
            )
        }
    }
}
