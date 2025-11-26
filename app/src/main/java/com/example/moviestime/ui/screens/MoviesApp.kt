package com.example.moviestime.ui.screens

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.annotation.RequiresPermission
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.moviestime.viewmodel.AuthViewModel
import com.example.moviestime.viewmodel.LanguageViewModel
import com.example.moviestime.viewmodel.MainViewModel
import com.example.moviestime.viewmodel.NotificationViewModel
import com.example.moviestime.viewmodel.ThemeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
@Composable
fun MoviesApp(
    mainViewModel: MainViewModel,
    themeViewModel: ThemeViewModel,
    notificationViewModel: NotificationViewModel,
    languageViewModel: LanguageViewModel,
    authViewModel: AuthViewModel = viewModel()
) {
    val navController = rememberNavController()

    val selectedTab by mainViewModel.uiState.collectAsState()

    LaunchedEffect(selectedTab.selectedTab) {
        when (selectedTab.selectedTab) {
            0 -> navController.navigate("home") {
                launchSingleTop = true
                restoreState = true
            }
            1 -> navController.navigate("discover") {
                launchSingleTop = true
                restoreState = true
            }
            2 -> navController.navigate("profile") {
                launchSingleTop = true
                restoreState = true
            }
        }
    }

    val tabs = listOf("Home", "Discover", "Profile")
    val icons = listOf(Icons.Default.Home, Icons.Default.Explore, Icons.Default.Person)
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "CineVault",
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Transparent
                ),
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = Color.Black.copy(alpha = 0.7f)
            ) {
                tabs.forEachIndexed { index, title ->
                    NavigationBarItem(
                        icon = {
                            Icon(
                                icons[index],
                                contentDescription = title,
                                tint = if (selectedTab.selectedTab == index) MaterialTheme.colorScheme.primary else Color.White.copy(alpha = 0.7f)
                            )
                        },
                        label = {
                            Text(
                                title,
                                color = if (selectedTab.selectedTab == index) MaterialTheme.colorScheme.primary else Color.White.copy(alpha = 0.7f)
                            )
                        },
                        selected = selectedTab.selectedTab == index,
                        onClick = { mainViewModel.selectTab(index) },
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = Color.White.copy(alpha = 0.2f)
                        )
                    )
                }
            }
        },
        containerColor = Color.Transparent
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(padding)
        ) {
            NavHost(navController = navController, startDestination = "home") {
                composable("home") {
                    HomeScreen(
                        navController = navController,
                        homeViewModel = viewModel(),
                        mainViewModel = mainViewModel
                    )
                }
                composable("discover") {
                    DiscoverScreen(
                        navController = navController,
                        searchViewModel = viewModel()
                    )
                }
                composable("profile") {
                    ProfileScreen(
                        mainViewModel = mainViewModel,
                        themeViewModel = themeViewModel,
                        languageViewModel = languageViewModel,
                        onMovieClick = { movieId ->
                            navController.navigate("movie/$movieId")
                        },
                        onSettingsClick = {
                            navController.navigate("settings")
                        },
                        onEditProfileClick = {
                            navController.navigate("edit_profile")
                        }
                    )
                }
                composable("edit_profile") {
                    EditProfileScreen(
                        authViewModel = authViewModel,
                        onBackClick = {
                            navController.popBackStack()
                        }
                    )
                }
                composable("settings") {
                    SettingsScreen(
                        onBack = { navController.popBackStack() },
                        onSignOut = {
                            authViewModel.logout()
                        }
                    )
                }

                composable("search") {
                    SearchScreen(
                        navController = navController,
                        searchViewModel = viewModel()
                    )
                }
                composable("movie/{movieId}") { backStackEntry ->
                    val movieId = backStackEntry.arguments?.getString("movieId")?.toInt() ?: 1
                    MovieDetailsScreen(
                        movieId = movieId,
                        mainViewModel = mainViewModel,
                        onBack = { navController.popBackStack() },
                        onPlayClick = { movie ->
                            movie.trailerKey?.let { key ->
                                val encodedKey = java.net.URLEncoder.encode(key, "UTF-8")
                                navController.navigate("video_player_route/$encodedKey")
                            }
                        },
                        onShareClick = { movie -> },
                        onMovieClick = { id ->
                            navController.navigate("movie/$id")
                        }
                    )
                }

                composable("video_player_route/{trailerKey}") { backStackEntry ->
                    val trailerKey = backStackEntry.arguments?.getString("trailerKey") ?: "ERROR"

                    VideoPlayerScreen(
                        trailerKey = trailerKey,
                        onBack = { navController.popBackStack() }
                    )
                }
            }
        }
    }
}