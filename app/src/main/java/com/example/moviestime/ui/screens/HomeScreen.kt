package com.example.moviestime.ui.screens

import android.Manifest
import androidx.annotation.RequiresPermission
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.moviestime.R
import com.example.moviestime.ui.components.FeaturedCard
import com.example.moviestime.ui.components.FeaturedLargeCard
import com.example.moviestime.ui.components.MovieRowCard
import com.example.moviestime.ui.components.ShimmerLargeMovieCard
import com.example.moviestime.ui.components.ShimmerMovieCard
import com.example.moviestime.ui.navigation.SeeAllCategory
import com.example.moviestime.viewmodel.HomeViewModel
import com.example.moviestime.viewmodel.MainViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.launch

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
    val nowPlayingLabel = stringResource(R.string.now_playing)
    val popularLabel = stringResource(R.string.popular)
    val topRatedLabel = stringResource(R.string.top_rated)
    val upcomingLabel = stringResource(R.string.upcoming)

    val isLoading = popular.isEmpty() && topRated.isEmpty() && nowPlaying.isEmpty() && upcoming.isEmpty()

    if (isLoading) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {
            item {
                Text(
                    text = nowPlayingLabel,
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
                SectionTitleWithSeeAll(popularLabel)
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
                SectionTitleWithSeeAll(topRatedLabel)
                Spacer(Modifier.height(8.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(5) {
                        ShimmerMovieCard()
                    }
                }
            }

            item {
                SectionTitleWithSeeAll(upcomingLabel)
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
                    .padding(horizontal = 8.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                contentPadding = PaddingValues(bottom = 100.dp)
            ) {
                item {
                    Text(
                        text = nowPlayingLabel,
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(Modifier.height(16.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp)
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
                                popularLabel,
                                style = MaterialTheme.typography.headlineMedium,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            TextButton(onClick = { onSeeAllClick(SeeAllCategory.POPULAR) }) {
                                Text(
                                    stringResource(R.string.see_all),
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
                            text = topRatedLabel,
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        TextButton(onClick = { onSeeAllClick(SeeAllCategory.TOP_RATED) }) {
                            Text(
                                stringResource(R.string.see_all),
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
                            text = upcomingLabel,
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        TextButton(onClick = { onSeeAllClick(SeeAllCategory.UPCOMING) }) {
                            Text(
                                stringResource(R.string.see_all),
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
    val seeAllLabel = stringResource(R.string.see_all)
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
                seeAllLabel,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}
