// ui/screens/MoviesApp.kt
package com.example.moviestime.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.moviestime.R
import com.example.moviestime.viewmodel.MainViewModel
import com.example.moviestime.viewmodel.ThemeViewModel
import com.example.moviestime.viewmodel.NotificationViewModel
import com.example.moviestime.viewmodel.LanguageViewModel

@OptIn(ExperimentalMaterial3Api::class)
@androidx.annotation.RequiresPermission(android.Manifest.permission.POST_NOTIFICATIONS)
@Composable
fun MoviesApp(
    mainViewModel: MainViewModel,
    themeViewModel: ThemeViewModel,
    notificationViewModel: NotificationViewModel,
    languageViewModel: LanguageViewModel
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

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.backkkkk),
            contentDescription = "Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.8f)
                        ),
                        startY = 0f,
                        endY = Float.POSITIVE_INFINITY
                    )
                )
        )

        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            "Movies Time",
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = Color.Transparent
                    ),
                    actions = {
                        IconButton(onClick = { navController.navigate("search") }) {
                            Icon(
                                Icons.Default.Search,
                                contentDescription = "Search",
                                tint = Color.White
                            )
                        }
                    }
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
            Box(Modifier.padding(padding)) {
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
                            languageViewModel = languageViewModel
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
                                navController.navigate("video/${movie.id}")
                            },
                            onShareClick = { movie ->
                            }
                        )
                    }
                }
            }
        }
    }
}