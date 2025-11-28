// ui/screens/MoviesApp.kt
package com.example.moviestime.ui.screens

import android.Manifest
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Brush
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

val LocalMainViewModel = compositionLocalOf<MainViewModel> {
    error("MainViewModel not provided")
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

    CompositionLocalProvider(
        LocalAppTopBarState provides topBarState,
        LocalMainViewModel provides mainViewModel
    ) {
        Navigator(HomeScreenRoute) { navigator ->

            LaunchedEffect(selectedTabState.selectedTab) {
                when (selectedTabState.selectedTab) {
                    0 -> navigator.replaceAll(HomeScreenRoute)
                    1 -> navigator.replaceAll(DiscoverScreenRoute)
                    2 -> navigator.replaceAll(ProfileScreenRoute)
                }
            }

            Scaffold(
                topBar = {
                    val config = topBarState.value
                    val canPop = navigator.canPop
                    CenterAlignedTopAppBar(
                        title = {
                            Text(
                                config.title,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        },
                        navigationIcon = {
                            if (canPop && config.onBack != null) {
                                IconButton(onClick = config.onBack) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                        contentDescription = "Back",
                                        tint = Color.White
                                    )
                                }
                            } else if (canPop) {
                                IconButton(onClick = { navigator.pop() }) {
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
                containerColor = Color.Transparent
            ) { padding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(androidx.compose.material3.MaterialTheme.colorScheme.background)
                        .padding(padding)
                ) {
                    CurrentScreen()
                    CinematicBottomBar(
                        tabs = tabs,
                        icons = icons,
                        selectedIndex = selectedTabState.selectedTab,
                        onSelect = { index ->
                            if (selectedTabState.selectedTab == index) {
                                when (index) {
                                    0 -> navigator.replaceAll(HomeScreenRoute)
                                    1 -> navigator.replaceAll(DiscoverScreenRoute)
                                    2 -> navigator.replaceAll(ProfileScreenRoute)
                                }
                            } else {
                                mainViewModel.selectTab(index)
                            }
                        },
                        modifier = Modifier.align(Alignment.BottomCenter)
                    )
                }
            }
        }
    }
}

@Composable
fun CinematicBottomBar(
    tabs: List<String>,
    icons: List<androidx.compose.ui.graphics.vector.ImageVector>,
    selectedIndex: Int,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val scheme = androidx.compose.material3.MaterialTheme.colorScheme
    val containerShape = RoundedCornerShape(26.dp)
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, bottom = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(containerShape)
                .shadow(24.dp, containerShape, clip = false)
                .background(scheme.surface)
                .border(1.dp, Color.White.copy(alpha = 0.08f), containerShape)
                .padding(horizontal = 8.dp, vertical = 6.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                tabs.forEachIndexed { index, title ->
                    val selected = index == selectedIndex
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { onSelect(index) }
                            .padding(vertical = 6.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(46.dp)
                                .clip(CircleShape)
                                .background(
                                    if (selected) scheme.secondary.copy(alpha = 0.18f) else Color.Transparent
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = icons[index],
                                contentDescription = title,
                                tint = if (selected) scheme.secondary else Color.White.copy(alpha = 0.7f)
                            )
                        }
                        Text(
                            text = title,
                            color = if (selected) scheme.secondary else Color.White.copy(alpha = 0.7f),
                            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }
}
