package com.example.moviestime.ui.screens

import android.Manifest
import androidx.annotation.RequiresPermission
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.moviestime.ui.components.*
import com.example.moviestime.ui.navigation.MovieDetailsScreenRoute
import com.example.moviestime.ui.navigation.SeeAllCategory
import com.example.moviestime.ui.navigation.SeeAllScreenRoute
import com.example.moviestime.ui.navigation.SettingsScreenRoute
import com.example.moviestime.viewmodel.HomeViewModel
import com.example.moviestime.viewmodel.MainViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.launch

object HomeScreen : Screen {
    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val topBarState = LocalAppTopBarState.current
        val homeViewModel: HomeViewModel = viewModel()
        val mainViewModel: MainViewModel = viewModel()

        LaunchedEffect(Unit) {
            topBarState.value = AppTopBarConfig(
                title = "CineVault",
                showBack = false,
                onBack = null,
                trailingContent = {
                    IconButton(onClick = { navigator.push(SettingsScreenRoute) }) {
                        Icon(
                            imageVector = Icons.Outlined.Settings,
                            contentDescription = "Settings",
                            tint = Color.White
                        )
                    }
                }
            )
        }

        HomeScreenContent(
            homeViewModel = homeViewModel,
            mainViewModel = mainViewModel,
            onMovieClick = { navigator.push(MovieDetailsScreenRoute(it)) },
            onSeeAllClick = { navigator.push(SeeAllScreenRoute(it)) }
        )
    }
}

@Composable
fun HomeScreenContent(
    homeViewModel: HomeViewModel = viewModel(),
    mainViewModel: MainViewModel = viewModel(),
    onMovieClick: (Int) -> Unit,
    onSeeAllClick: (SeeAllCategory) -> Unit
) {
    val popular by homeViewModel.popular.collectAsState()
    val topRated by homeViewModel.topRated.collectAsState()
    val nowPlaying by homeViewModel.nowPlaying.collectAsState()
    val upcoming by homeViewModel.upcoming.collectAsState()

    val isLoading = popular.isEmpty() && topRated.isEmpty() && nowPlaying.isEmpty() && upcoming.isEmpty()

    if (isLoading) {
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

                itemsIndexed(popular.take(6).chunked(2)) { index, rowMovies ->
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
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        TextButton(onClick = onClick) {
            Text(
                "See All",
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}