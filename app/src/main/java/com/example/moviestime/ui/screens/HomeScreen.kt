package com.example.moviestime.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.launch
import com.example.moviestime.ui.components.*
import com.example.moviestime.viewmodel.HomeViewModel
import com.example.moviestime.viewmodel.MainViewModel

@androidx.annotation.RequiresPermission(android.Manifest.permission.POST_NOTIFICATIONS)
@Composable
fun HomeScreen(
    navController: NavHostController,
    homeViewModel: HomeViewModel = viewModel(),
    mainViewModel: MainViewModel = viewModel()
) {
    val popular by homeViewModel.popular.collectAsState()
    val topRated by homeViewModel.topRated.collectAsState()
    val nowPlaying by homeViewModel.nowPlaying.collectAsState()
    val upcoming by homeViewModel.upcoming.collectAsState()
    val favorites by mainViewModel.favorites.collectAsState()

    val isLoading = popular.isEmpty() && topRated.isEmpty() && nowPlaying.isEmpty() && upcoming.isEmpty()

    if (isLoading) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item {
                Text("🎬 Now Playing", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
                Spacer(Modifier.height(8.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    items(5) {
                        ShimmerMovieCard()
                    }
                }
            }

            item {
                SectionTitleWithSeeAll("🔥 Popular")
                Spacer(Modifier.height(8.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(5) {
                        ShimmerMovieCard()
                    }
                }
            }

            item {
                SectionTitleWithSeeAll("🏆 Top Rated")
                Spacer(Modifier.height(8.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(5) {
                        ShimmerMovieCard()
                    }
                }
            }

            item {
                SectionTitleWithSeeAll("🍿 Upcoming")
                Spacer(Modifier.height(8.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(5) {
                        ShimmerMovieCard()
                    }
                }
            }
        }
    } else {
        val refreshScope = rememberCoroutineScope()
        val refreshing = remember { mutableStateOf(false) }

        val onRefresh: () -> Unit = {
            refreshing.value = true
            refreshScope.launch {
                homeViewModel.loadMovies()
                refreshing.value = false
            }
        }

        SwipeRefresh(
            state = rememberSwipeRefreshState(isRefreshing = refreshing.value),
            onRefresh = onRefresh,
            modifier = Modifier.fillMaxSize()
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                item {
                    Text("🎬 Now Playing", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Spacer(Modifier.height(8.dp))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        items(nowPlaying) { movie ->
                            FeaturedLargeCard(movie = movie) { selectedMovie ->
                                navController.navigate("movie/${selectedMovie.id}")
                            }
                        }
                    }
                }

                item {
                    SectionWithRow(
                        title = "🔥 Popular",
                        movies = popular,
                        favorites = favorites,
                        onMovieClick = { movie ->
                            navController.navigate("movie/${movie.id}")
                        },
                        onFavoriteClick = { movie -> mainViewModel.toggleFavorite(movie) }
                    )
                }

                item {
                    SectionWithRow(
                        title = "🏆 Top Rated",
                        movies = topRated,
                        favorites = favorites,
                        onMovieClick = { movie ->
                            navController.navigate("movie/${movie.id}")
                        },
                        onFavoriteClick = { movie -> mainViewModel.toggleFavorite(movie) }
                    )
                }

                item {
                    SectionWithRow(
                        title = "🍿 Upcoming",
                        movies = upcoming,
                        favorites = favorites,
                        onMovieClick = { movie ->
                            navController.navigate("movie/${movie.id}")
                        },
                        onFavoriteClick =  { movie -> mainViewModel.toggleFavorite(movie) }
                    )
                }
            }
        }
    }
}

@Composable
fun SectionTitleWithSeeAll(title: String) {
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            title,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        TextButton(onClick = {}) {
            Text(
                "See All",
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}