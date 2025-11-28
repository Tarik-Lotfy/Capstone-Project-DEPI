@file:Suppress("unused")

package com.example.moviestime.ui.navigation

import android.Manifest
import androidx.annotation.RequiresPermission
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.moviestime.R
import com.example.moviestime.data.model.Movie
import com.example.moviestime.ui.screens.AppTopBarConfig
import com.example.moviestime.ui.screens.DiscoverScreen
import com.example.moviestime.ui.screens.EditProfileScreenContent
import com.example.moviestime.ui.screens.HomeScreenContent
import com.example.moviestime.ui.screens.LocalAppTopBarState
import com.example.moviestime.ui.screens.LocalAuthViewModel
import com.example.moviestime.ui.screens.LocalLanguageViewModel
import com.example.moviestime.ui.screens.LocalMainViewModel
import com.example.moviestime.ui.screens.LocalThemeViewModel
import com.example.moviestime.ui.screens.LoginScreenContent
import com.example.moviestime.ui.screens.MovieDetailsScreen
import com.example.moviestime.ui.screens.ProfileScreenContent
import com.example.moviestime.ui.screens.SeeAllMoviesScreenContent
import com.example.moviestime.ui.screens.SettingsScreenContent
import com.example.moviestime.ui.screens.VideoPlayerScreenContent
import com.example.moviestime.viewmodel.AuthViewModel
import com.example.moviestime.viewmodel.HomeViewModel
import com.example.moviestime.viewmodel.SearchViewModel

enum class SeeAllCategory {
    POPULAR,
    TOP_RATED,
    UPCOMING
}


object HomeScreenRoute : Screen {
    private fun readResolve(): Any = HomeScreenRoute

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val topBarState = LocalAppTopBarState.current
        val homeViewModel: HomeViewModel = viewModel()
        val mainViewModel = LocalMainViewModel.current
        val appTitle = stringResource(R.string.app_name)
        val settingsCd = stringResource(R.string.settings_button_cd)

        LaunchedEffect(appTitle) {
            topBarState.value = AppTopBarConfig(
                title = appTitle,
                showBack = false,
                onBack = null,
                trailingContent = null
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
        val discoverTitle = stringResource(R.string.discover)
        val searchingTitle = stringResource(R.string.searching)
        
        val isSearchTyping = query.isNotEmpty()
        val title = if (isSearchTyping) searchingTitle else discoverTitle

        LaunchedEffect(title) {
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
    @Suppress("unused")
    private fun readResolve(): Any = SearchScreenRoute

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val topBarState = LocalAppTopBarState.current
        val searchViewModel: SearchViewModel = viewModel()
        val discoverTitle = stringResource(R.string.discover)

        LaunchedEffect(discoverTitle) {
            topBarState.value = AppTopBarConfig(
                title = discoverTitle,
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
        val authViewModel: AuthViewModel = LocalAuthViewModel.current ?: viewModel()
        val profileTitle = stringResource(R.string.profile)
        val settingsCd = stringResource(R.string.settings_button_cd)

        // Reload profile when screen is entered
        // Use a key that ensures reload when navigating back
        var reloadKey by remember { mutableIntStateOf(0) }
        
        LaunchedEffect(reloadKey) {
            authViewModel.loadUserProfile(force = true)
        }
        
        // Increment reload key when screen becomes visible (using DisposableEffect)
        DisposableEffect(Unit) {
            reloadKey++
            onDispose { }
        }

        LaunchedEffect(profileTitle) {
            topBarState.value = AppTopBarConfig(
                title = profileTitle,
                showBack = false,
                onBack = null,
                trailingContent = {
                    IconButton(onClick = { navigator.push(SettingsScreenRoute) }) {
                        Icon(
                            imageVector = Icons.Outlined.Settings,
                            contentDescription = settingsCd,
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
                navigator.push(EditProfileScreenRoute)
            }
        )
    }
}

data class MovieDetailsScreenRoute(
    val movieId: Int
) : Screen {

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val topBarState = LocalAppTopBarState.current
        val mainViewModel = LocalMainViewModel.current
        val detailsTitle = stringResource(R.string.movie_details_title)

        LaunchedEffect(movieId, detailsTitle) {
            topBarState.value = AppTopBarConfig(
                title = detailsTitle,
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
        val title = when (category) {
            SeeAllCategory.POPULAR -> stringResource(R.string.popular)
            SeeAllCategory.TOP_RATED -> stringResource(R.string.top_rated)
            SeeAllCategory.UPCOMING -> stringResource(R.string.upcoming)
        }

        LaunchedEffect(title) {
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
        val authViewModel: AuthViewModel = LocalAuthViewModel.current ?: viewModel()
        val themeViewModel = LocalThemeViewModel.current
        val languageViewModel = LocalLanguageViewModel.current
        val settingsTitle = stringResource(R.string.settings)

        LaunchedEffect(settingsTitle) {
            topBarState.value = AppTopBarConfig(
                title = settingsTitle,
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
                navigator.push(EditProfileScreenRoute)
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

object EditProfileScreenRoute : Screen {
    private fun readResolve(): Any = EditProfileScreenRoute

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val topBarState = LocalAppTopBarState.current
        val authViewModel: AuthViewModel = LocalAuthViewModel.current ?: viewModel()
        val editProfileTitle = stringResource(R.string.edit_profile)

        // Load user profile when screen appears
        LaunchedEffect(Unit) {
            authViewModel.loadUserProfile(force = true)
        }

        LaunchedEffect(editProfileTitle) {
            topBarState.value = AppTopBarConfig(
                title = editProfileTitle,
                showBack = true,
                onBack = { navigator.pop() },
                trailingContent = null
            )
        }

        EditProfileScreenContent(
            authViewModel = authViewModel,
            onBackClick = { navigator.pop() },
            onNavigateToProfile = {
                navigator.pop()
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
        val topBarState = LocalAppTopBarState.current
        val trailerTitle = stringResource(R.string.trailer_title)

        LaunchedEffect(trailerKey, trailerTitle) {
            topBarState.value = AppTopBarConfig(
                title = trailerTitle,
                showBack = true,
                onBack = { navigator.pop() },
                trailingContent = null
            )
        }

        VideoPlayerScreenContent(
            trailerKey = trailerKey,
            onBack = { navigator.pop() }
        )
    }
}


