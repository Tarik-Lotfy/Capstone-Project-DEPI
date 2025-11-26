// ui/screens/MoviesApp.kt
package com.example.moviestime.ui.screens

import android.Manifest
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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

data class AppTopBarConfig(
    val title: String = "",
    val showBack: Boolean = false,
    val onBack: (() -> Unit)? = null,
    val trailingContent: (@Composable () -> Unit)? = null
)

val LocalAppTopBarState = compositionLocalOf<MutableState<AppTopBarConfig>> {
    mutableStateOf(AppTopBarConfig())
}

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

    val topBarState = remember {
        mutableStateOf(
            AppTopBarConfig(
                title = "CineVault",
                showBack = false
            )
        )
    }

    CompositionLocalProvider(LocalAppTopBarState provides topBarState) {
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
                    val config = topBarState.value
                    CenterAlignedTopAppBar(
                        title = {
                            Text(
                                config.title,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        },
                        navigationIcon = {
                            if (config.showBack && config.onBack != null) {
                                IconButton(onClick = config.onBack) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                        contentDescription = "Back",
                                        tint = Color.White
                                    )
                                }
                            }
                        },
                        actions = {
                            config.trailingContent?.invoke()
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
}