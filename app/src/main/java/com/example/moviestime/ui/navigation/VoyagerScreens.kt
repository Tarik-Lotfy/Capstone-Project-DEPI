package com.example.moviestime.ui.navigation

import android.Manifest
import androidx.annotation.RequiresPermission
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.moviestime.data.model.Movie
import com.example.moviestime.ui.screens.DiscoverScreen
import com.example.moviestime.ui.screens.HomeScreen
import com.example.moviestime.ui.screens.MovieDetailsScreen
import com.example.moviestime.ui.screens.ProfileScreen
import com.example.moviestime.ui.screens.SearchScreen
import com.example.moviestime.viewmodel.HomeViewModel
import com.example.moviestime.viewmodel.LanguageViewModel
import com.example.moviestime.viewmodel.MainViewModel
import com.example.moviestime.viewmodel.SearchViewModel
import com.example.moviestime.viewmodel.ThemeViewModel

/**
 * Voyager Screen wrappers around existing composables.
 * UI/logic stays inside the original composables; navigation is handled here.
 */

object HomeScreenRoute : Screen {
    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val homeViewModel: HomeViewModel = viewModel()
        val mainViewModel: MainViewModel = viewModel()

        HomeScreen(
            homeViewModel = homeViewModel,
            mainViewModel = mainViewModel,
            onMovieClick = { movieId ->
                navigator.push(MovieDetailsScreenRoute(movieId))
            }
        )
    }
}

object DiscoverScreenRoute : Screen {
    private fun readResolve(): Any = DiscoverScreenRoute

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val searchViewModel: SearchViewModel = viewModel()

        DiscoverScreen(
            searchViewModel = searchViewModel,
            onMovieClick = { movieId ->
                navigator.push(MovieDetailsScreenRoute(movieId))
            }
        )
    }
}

object SearchScreenRoute : Screen {
    private fun readResolve(): Any = SearchScreenRoute

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val searchViewModel: SearchViewModel = viewModel()

        SearchScreen(
            searchViewModel = searchViewModel,
            onMovieClick = { movie ->
                navigator.push(MovieDetailsScreenRoute(movie.id))
            }
        )
    }
}

object ProfileScreenRoute : Screen {
    private fun readResolve(): Any = ProfileScreenRoute

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val mainViewModel: MainViewModel = viewModel()
        val themeViewModel: ThemeViewModel = viewModel()
        val languageViewModel: LanguageViewModel = viewModel()

        ProfileScreen(
            mainViewModel = mainViewModel,
            themeViewModel = themeViewModel,
            languageViewModel = languageViewModel,
            onMovieClick = { movieId ->
                navigator.push(MovieDetailsScreenRoute(movieId))
            }
        )
    }
}

data class MovieDetailsScreenRoute(
    val movieId: Int
) : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val mainViewModel: MainViewModel = viewModel()

        MovieDetailsScreen(
            movieId = movieId,
            mainViewModel = mainViewModel,
            onBack = { navigator.pop() },
            onPlayClick = { _: Movie -> },
            onFavoriteClick = { movie ->
                mainViewModel.toggleFavorite(movie)
            },
            onShareClick = { _ -> }
        )
    }
}


