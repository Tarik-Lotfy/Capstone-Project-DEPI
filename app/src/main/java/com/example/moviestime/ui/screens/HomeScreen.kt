package com.example.moviestime.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.launch
import com.example.moviestime.ui.components.*
import com.example.moviestime.ui.navigation.SeeAllCategory
import com.example.moviestime.viewmodel.HomeViewModel
import com.example.moviestime.viewmodel.MainViewModel

@androidx.annotation.RequiresPermission(android.Manifest.permission.POST_NOTIFICATIONS)
@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = viewModel(),
    mainViewModel: MainViewModel = viewModel(),
    onMovieClick: (Int) -> Unit,
    onSeeAllClick: (SeeAllCategory) -> Unit
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
                Text("Now Playing")
                Spacer(Modifier.height(8.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    items(5) {
                        ShimmerLargeMovieCard()
                    }
                }
            }

            item {
                SectionTitleWithSeeAll("Popular")
                Spacer(Modifier.height(8.dp))
            }
            items((0..7).chunked(2)) { row ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    row.forEach { _ ->
                        Box(modifier = Modifier.weight(1f)) {
                            ShimmerMovieCard()
                        }
                    }
                    if (row.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }

            item {
                SectionTitleWithSeeAll(" Top Rated")
                Spacer(Modifier.height(8.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(5) {
                        ShimmerMovieCard()
                    }
                }
            }

            item {
                SectionTitleWithSeeAll(" Upcoming")
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
                    Text(
                        text = "Now Playing",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(Modifier.height(16.dp))
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp)
                    ) {
                        items(nowPlaying.take(5)) { movie ->
                                    FeaturedLargeCard(
                                        movie = movie,
                                        onMovieClick = { selectedMovie ->
                                            onMovieClick(selectedMovie.id)
                                        }
                                    )
                        }
                    }
                }

                item {
                    Column {
                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "Popular",
                                style = MaterialTheme.typography.headlineMedium,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            TextButton(onClick = { onSeeAllClick(SeeAllCategory.POPULAR) }) {
                                Text(
                                    "See All",
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                        Spacer(Modifier.height(16.dp))
                    }
                }

                itemsIndexed(popular.take(8).chunked(2)) { index, rowMovies ->
                    if (index > 0) {
                        Spacer(Modifier.height(12.dp))
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        rowMovies.forEach { movie ->
                            Box(modifier = Modifier.weight(1f)) {
                                FeaturedCard(
                                    movie = movie,
                                    onMovieClick = { selectedMovie ->
                                        onMovieClick(selectedMovie.id)
                                    }
                                )
                            }
                        }
                        // Add empty space if odd number of items
                        if (rowMovies.size == 1) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }

                item {
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Top Rated",
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        TextButton(onClick = { onSeeAllClick(SeeAllCategory.TOP_RATED) }) {
                            Text(
                                "See All",
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                    Spacer(Modifier.height(16.dp))
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(topRated.take(8)) { movie ->
                            MovieRowCard(
                                movie = movie,
                                onMovieClick = { selectedMovie ->
                                    onMovieClick(selectedMovie.id)
                                }
                            )
                        }
                    }
                }

                item {
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Upcoming",
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        TextButton(onClick = { onSeeAllClick(SeeAllCategory.UPCOMING) }) {
                            Text(
                                "See All",
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                    Spacer(Modifier.height(16.dp))
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(upcoming.take(8)) { movie ->
                            MovieRowCard(
                                movie = movie,
                                onMovieClick = { selectedMovie ->
                                    onMovieClick(selectedMovie.id)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SectionTitleWithSeeAll(title: String, onClick: () -> Unit = {}) {
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
        TextButton(onClick = onClick) {
            Text(
                "See All",
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}