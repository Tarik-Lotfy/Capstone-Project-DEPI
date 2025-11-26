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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.moviestime.R
import com.example.moviestime.ui.components.*
import com.example.moviestime.viewmodel.HomeViewModel
import com.example.moviestime.viewmodel.MainViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.launch

class HomeScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val homeViewModel: HomeViewModel = viewModel()
        val mainViewModel = LocalMainViewModel.current

        HomeScreenContent(
            homeViewModel = homeViewModel,
            mainViewModel = mainViewModel,
            onMovieClick = { movieId ->
                navigator.push(MovieDetailsScreen(movieId))
            }
        )
    }
}

@Composable
fun HomeScreenContent(
    homeViewModel: HomeViewModel,
    mainViewModel: MainViewModel,
    onMovieClick: (Int) -> Unit
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
                Text(stringResource(R.string.now_playing))
                Spacer(Modifier.height(8.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    items(5) { ShimmerMovieCard() }
                }
            }
            // ... (بقية الشيمر)
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
                        text = stringResource(R.string.now_playing),
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
                                onMovieClick = { onMovieClick(it.id) }
                            )
                        }
                    }
                }

                item {
                    SectionTitleWithSeeAll(stringResource(R.string.popular))
                    Spacer(Modifier.height(16.dp))
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
                                    onMovieClick = { onMovieClick(it.id) }
                                )
                            }
                        }
                        if (rowMovies.size == 1) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }

                item {
                    Text(
                        text = stringResource(R.string.top_rated),
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(Modifier.height(16.dp))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        items(topRated.take(8)) { movie ->
                            MovieRowCard(
                                movie = movie,
                                onMovieClick = { onMovieClick(it.id) }
                            )
                        }
                    }
                }

                item {
                    Text(
                        text = stringResource(R.string.upcoming),
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(Modifier.height(16.dp))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        items(upcoming.take(8)) { movie ->
                            MovieRowCard(
                                movie = movie,
                                onMovieClick = { onMovieClick(it.id) }
                            )
                        }
                    }
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
                stringResource(R.string.see_all),
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}