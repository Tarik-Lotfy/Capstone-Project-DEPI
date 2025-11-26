package com.example.moviestime.ui.navigation

import android.Manifest
import androidx.annotation.RequiresPermission
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.moviestime.data.model.Movie
import com.example.moviestime.ui.navigation.SeeAllCategory.POPULAR
import com.example.moviestime.ui.navigation.SeeAllCategory.TOP_RATED
import com.example.moviestime.ui.navigation.SeeAllCategory.UPCOMING
import com.example.moviestime.ui.screens.AppTopBarConfig
import com.example.moviestime.ui.screens.DiscoverScreen
import com.example.moviestime.ui.screens.HomeScreen
import com.example.moviestime.ui.screens.LocalAppTopBarState
import com.example.moviestime.ui.screens.MovieDetailsScreen
import com.example.moviestime.ui.screens.ProfileScreen
import com.example.moviestime.ui.screens.SearchScreen
import com.example.moviestime.ui.screens.SeeAllMoviesScreen
import com.example.moviestime.viewmodel.HomeViewModel
import com.example.moviestime.viewmodel.LanguageViewModel
import com.example.moviestime.viewmodel.MainViewModel
import com.example.moviestime.viewmodel.SearchViewModel
import com.example.moviestime.viewmodel.ThemeViewModel

enum class SeeAllCategory {
    POPULAR,
    TOP_RATED,
    UPCOMING
}

/**
 * Voyager Screen wrappers around existing composables.
 * UI/logic stays inside the original composables; navigation is handled here.
 */

object HomeScreenRoute : Screen {
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
                trailingContent = null
            )
        }

        HomeScreen(
            homeViewModel = homeViewModel,
            mainViewModel = mainViewModel,
            onMovieClick = { movieId ->
                navigator.push(MovieDetailsScreenRoute(movieId))
            },
            onSeeAllClick = { category ->
                navigator.push(SeeAllScreenRoute(category))
            }
        )
    }
}

object DiscoverScreenRoute : Screen {
    private fun readResolve(): Any = DiscoverScreenRoute

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val topBarState = LocalAppTopBarState.current
        val searchViewModel: SearchViewModel = viewModel()

        LaunchedEffect(Unit) {
            topBarState.value = AppTopBarConfig(
                title = "Discover",
                showBack = false,
                onBack = null,
                trailingContent = null
            )
        }

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
        val topBarState = LocalAppTopBarState.current
        val searchViewModel: SearchViewModel = viewModel()

        LaunchedEffect(Unit) {
            topBarState.value = AppTopBarConfig(
                title = "Discover",
                showBack = false,
                onBack = null,
                trailingContent = null
            )
        }

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
        val topBarState = LocalAppTopBarState.current
        val mainViewModel: MainViewModel = viewModel()
        val themeViewModel: ThemeViewModel = viewModel()
        val languageViewModel: LanguageViewModel = viewModel()

        LaunchedEffect(Unit) {
            topBarState.value = AppTopBarConfig(
                title = "Profile",
                showBack = false,
                onBack = null,
                trailingContent = null
            )
        }

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
        val topBarState = LocalAppTopBarState.current
        val mainViewModel: MainViewModel = viewModel()

        LaunchedEffect(Unit) {
            topBarState.value = AppTopBarConfig(
                title = "Movie Details",
                showBack = false,
                onBack = null,
                trailingContent = null
            )
        }

        MovieDetailsScreen(
            movieId = movieId,
            mainViewModel = mainViewModel,
            onBack = { navigator.pop() },
            onPlayClick = { _: Movie -> },
//            onFavoriteClick = { movie ->
//                mainViewModel.toggleFavorite(movie)
//            },
            onShareClick = { _ -> }
        )
    }
}

data class SeeAllScreenRoute(
    val category: SeeAllCategory
) : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val topBarState = LocalAppTopBarState.current
        val homeViewModel: HomeViewModel = viewModel()

        LaunchedEffect(category) {
            val title = when (category) {
                POPULAR -> "Popular"
                TOP_RATED -> "Top Rated"
                UPCOMING -> "Upcoming"
            }
            topBarState.value = AppTopBarConfig(
                title = title,
                showBack = true,
                onBack = { navigator.pop() },
                trailingContent = null
            )
        }

        SeeAllMoviesScreen(
            category = category,
            homeViewModel = homeViewModel,
            onMovieClick = { movieId ->
                navigator.push(MovieDetailsScreenRoute(movieId))
            }
        )
    }
}


