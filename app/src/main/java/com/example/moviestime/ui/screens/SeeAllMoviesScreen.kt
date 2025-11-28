package com.example.moviestime.ui.screens

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.moviestime.ui.components.MovieRowCard
import com.example.moviestime.ui.navigation.SeeAllCategory
import com.example.moviestime.viewmodel.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SeeAllMoviesScreenContent(
    category: SeeAllCategory,
    homeViewModel: HomeViewModel = viewModel(),
    onMovieClick: (Int) -> Unit
) {
    val popular by homeViewModel.popular.collectAsState()
    val topRated by homeViewModel.topRated.collectAsState()
    val upcoming by homeViewModel.upcoming.collectAsState()

    val movies = when (category) {
        SeeAllCategory.POPULAR -> popular
        SeeAllCategory.TOP_RATED -> topRated
        SeeAllCategory.UPCOMING -> upcoming
    }

     Scaffold { padding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 8.dp),
            contentPadding = PaddingValues(top = 8.dp, bottom = 96.dp),
            horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp),
            verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp)
        ) {
            items(movies) { movie ->
                MovieRowCard(
                    movie = movie,
                    onMovieClick = { onMovieClick(movie.id) }
                )
            }
        }
    }
}


