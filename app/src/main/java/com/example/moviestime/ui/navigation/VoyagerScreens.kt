package com.example.moviestime.ui.navigation

import android.Manifest
import androidx.annotation.RequiresPermission
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.moviestime.data.model.Movie
import com.example.moviestime.ui.screens.AppTopBarConfig
import com.example.moviestime.ui.screens.DiscoverScreen
import com.example.moviestime.ui.screens.HomeScreenContent
import com.example.moviestime.ui.screens.LocalAppTopBarState
import com.example.moviestime.ui.screens.LocalMainViewModel
import com.example.moviestime.ui.screens.LoginScreenContent
import com.example.moviestime.ui.screens.MovieDetailsScreen
import com.example.moviestime.ui.screens.ProfileScreenContent
import com.example.moviestime.ui.screens.EditProfileScreen
import com.example.moviestime.ui.screens.SeeAllMoviesScreenContent
import com.example.moviestime.ui.screens.SettingsScreenContent
import com.example.moviestime.ui.screens.VideoPlayerScreenContent
import com.example.moviestime.viewmodel.AuthViewModel
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


object HomeScreenRoute : Screen {
    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val topBarState = LocalAppTopBarState.current
        val homeViewModel: HomeViewModel = viewModel()
        val mainViewModel = LocalMainViewModel.current

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
        val query by searchViewModel.searchQuery.collectAsState()
        
        val isSearchTyping = query.isNotEmpty()
        val title = if (isSearchTyping) "Searching..." else "Discover"

        LaunchedEffect(query) {
            topBarState.value = AppTopBarConfig(
                title = title,
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

        DiscoverScreen(
            searchViewModel = searchViewModel,
            onMovieClick = { movieId ->
                navigator.push(MovieDetailsScreenRoute(movieId))
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
        val mainViewModel = LocalMainViewModel.current
        val authViewModel: AuthViewModel = viewModel()
        val themeViewModel: ThemeViewModel = viewModel()
        val languageViewModel: LanguageViewModel = viewModel()

        LaunchedEffect(Unit) {
            topBarState.value = AppTopBarConfig(
                title = "Profile",
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

        ProfileScreenContent(
            authViewModel = authViewModel,
            mainViewModel = mainViewModel,
            onMovieClick = { movieId ->
                navigator.push(MovieDetailsScreenRoute(movieId))
            },
            onEditProfile = {
                navigator.push(EditProfileScreen())
            }
        )
    }
}

data class MovieDetailsScreenRoute(
    val movieId: Int
) : Screen {

    @androidx.annotation.RequiresPermission(android.Manifest.permission.POST_NOTIFICATIONS)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val topBarState = LocalAppTopBarState.current
        val mainViewModel = LocalMainViewModel.current

        LaunchedEffect(Unit) {
            topBarState.value = AppTopBarConfig(
                title = "Movie Details",
                showBack = true,
                onBack = { navigator.pop() },
                trailingContent = null
            )
        }

        MovieDetailsScreen(
            movieId = movieId,
            mainViewModel = mainViewModel,
            onBack = { navigator.pop() },
            onPlayClick = { movie: Movie ->
                movie.trailerKey?.let { trailerKey ->
                    navigator.push(VideoPlayerScreenRoute(trailerKey))
                }
            },
            onShareClick = { _ -> },
            onMovieClick = { id ->
                navigator.push(MovieDetailsScreenRoute(id))
            }
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
                SeeAllCategory.POPULAR -> "Popular"
                SeeAllCategory.TOP_RATED -> "Top Rated"
                SeeAllCategory.UPCOMING -> "Upcoming"
            }
            topBarState.value = AppTopBarConfig(
                title = title,
                showBack = true,
                onBack = { navigator.pop() },
                trailingContent = null
            )
        }

        SeeAllMoviesScreenContent(
            category = category,
            homeViewModel = homeViewModel,
            onMovieClick = { movieId ->
                navigator.push(MovieDetailsScreenRoute(movieId))
            }
        )
    }
}

object SettingsScreenRoute : Screen {
    private fun readResolve(): Any = SettingsScreenRoute

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val topBarState = LocalAppTopBarState.current
        val authViewModel: AuthViewModel = viewModel()
        val themeViewModel: ThemeViewModel = viewModel()
        val languageViewModel: LanguageViewModel = viewModel()

        LaunchedEffect(Unit) {
            topBarState.value = AppTopBarConfig(
                title = "Settings",
                showBack = true,
                onBack = { navigator.pop() },
                trailingContent = null
            )
        }

        SettingsScreenContent(
            authViewModel = authViewModel,
            languageViewModel = languageViewModel,
            themeViewModel = themeViewModel,
            onEditProfile = {
                navigator.push(EditProfileScreen())
            },
            onDeleteAccount = {
                // TODO: Implement delete account functionality
            },
            onSignOut = {
                // Navigate to login screen after logout
                // The MainActivity will handle this based on isLoggedIn state
            }
        )
    }
}

object LoginScreenRoute : Screen {
    private fun readResolve(): Any = LoginScreenRoute

    @Composable
    override fun Content() {
        val authViewModel: AuthViewModel = viewModel()

        LoginScreenContent(authViewModel = authViewModel)
    }
}

data class VideoPlayerScreenRoute(
    val trailerKey: String
) : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        VideoPlayerScreenContent(
            trailerKey = trailerKey,
            onBack = { navigator.pop() }
        )
    }
}


