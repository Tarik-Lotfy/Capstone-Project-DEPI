// ui/screens/MoviesApp.kt
package com.example.moviestime.ui.screens

import android.Manifest
import androidx.annotation.RequiresPermission
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.Navigator
import com.example.moviestime.ui.navigation.DiscoverScreenRoute
import com.example.moviestime.ui.navigation.HomeScreenRoute
import com.example.moviestime.ui.navigation.ProfileScreenRoute
import com.example.moviestime.viewmodel.LanguageViewModel
import com.example.moviestime.viewmodel.MainViewModel
import com.example.moviestime.viewmodel.ThemeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoviesApp(
    mainViewModel: MainViewModel,
    themeViewModel: ThemeViewModel,
    languageViewModel: LanguageViewModel
) {
    val selectedTabState by mainViewModel.uiState.collectAsState()
    val tabs = listOf("Home", "Discover", "Profile")
    val icons = listOf(Icons.Default.Home, Icons.Default.Explore, Icons.Default.Person)

    Navigator(HomeScreenRoute) { navigator ->

        LaunchedEffect(selectedTabState.selectedTab) {
            when (selectedTabState.selectedTab) {
                0 -> navigator.replace(HomeScreenRoute)
                1 -> navigator.replace(DiscoverScreenRoute)
                2 -> navigator.replace(ProfileScreenRoute)
            }
        }

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
                                    tint = if (selectedTabState.selectedTab == index) androidx.compose.material3.MaterialTheme.colorScheme.primary else Color.White.copy(
                                        alpha = 0.7f
                                    )
                                )
                            },
                            label = {
                                Text(
                                    title,
                                    color = if (selectedTabState.selectedTab == index) androidx.compose.material3.MaterialTheme.colorScheme.primary else Color.White.copy(
                                        alpha = 0.7f
                                    )
                                )
                            },
                            selected = selectedTabState.selectedTab == index,
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
                    .background(androidx.compose.material3.MaterialTheme.colorScheme.background)
                    .padding(padding)
            ) {
                CurrentScreen()
            }
        }
    }
}