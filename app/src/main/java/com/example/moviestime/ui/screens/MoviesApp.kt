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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.Navigator
import com.example.moviestime.R
import com.example.moviestime.viewmodel.AuthViewModel
import com.example.moviestime.viewmodel.LanguageViewModel
import com.example.moviestime.viewmodel.MainViewModel
import com.example.moviestime.viewmodel.NotificationViewModel
import com.example.moviestime.viewmodel.ThemeViewModel

// تعريف CompositionLocals لتمرير الـ ViewModels للشاشات
val LocalMainViewModel =
    staticCompositionLocalOf<MainViewModel> { error("No MainViewModel provided") }
val LocalThemeViewModel =
    staticCompositionLocalOf<ThemeViewModel> { error("No ThemeViewModel provided") }
val LocalNotificationViewModel =
    staticCompositionLocalOf<NotificationViewModel> { error("No NotificationViewModel provided") }
val LocalLanguageViewModel =
    staticCompositionLocalOf<LanguageViewModel> { error("No LanguageViewModel provided") }
val LocalAuthViewModel =
    staticCompositionLocalOf<AuthViewModel> { error("No AuthViewModel provided") }

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
    // توفير الـ ViewModels لجميع الشاشات داخل التطبيق
    CompositionLocalProvider(
        LocalMainViewModel provides mainViewModel,
        LocalThemeViewModel provides themeViewModel,
        LocalNotificationViewModel provides notificationViewModel,
        LocalLanguageViewModel provides languageViewModel,
        LocalAuthViewModel provides authViewModel
    ) {
        Navigator(HomeScreen()) { navigator ->
            val selectedTab by mainViewModel.uiState.collectAsState()

            // مزامنة التبويب المختار مع الشاشة الحالية
            LaunchedEffect(navigator.lastItem) {
                when (navigator.lastItem) {
                    is HomeScreen -> mainViewModel.selectTab(0)
                    is DiscoverScreen -> mainViewModel.selectTab(1)
                    is ProfileScreen -> mainViewModel.selectTab(2)
                }
            }

            val tabs = listOf(
                stringResource(R.string.home),
                stringResource(R.string.discover),
                stringResource(R.string.profile)
            )
            val icons = listOf(Icons.Default.Home, Icons.Default.Explore, Icons.Default.Person)

            Scaffold(
                topBar = {
                    // إخفاء الشريط العلوي في شاشة مشغل الفيديو أو حسب الحاجة
                    if (navigator.lastItem !is VideoPlayerScreen) {
                        CenterAlignedTopAppBar(
                            title = {
                                Text(
                                    stringResource(R.string.app_name),
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            },
                            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                                containerColor = Color.Transparent
                            ),
                        )
                    }
                },
                bottomBar = {
                    if (navigator.lastItem is HomeScreen || navigator.lastItem is DiscoverScreen || navigator.lastItem is ProfileScreen) {
                        NavigationBar(
                            containerColor = Color.Black.copy(alpha = 0.7f)
                        ) {
                            tabs.forEachIndexed { index, title ->
                                NavigationBarItem(
                                    icon = {
                                        Icon(
                                            icons[index],
                                            contentDescription = title,
                                            tint = if (selectedTab.selectedTab == index) MaterialTheme.colorScheme.primary else Color.White.copy(
                                                alpha = 0.7f
                                            )
                                        )
                                    },
                                    label = {
                                        Text(
                                            title,
                                            color = if (selectedTab.selectedTab == index) MaterialTheme.colorScheme.primary else Color.White.copy(
                                                alpha = 0.7f
                                            )
                                        )
                                    },
                                    selected = selectedTab.selectedTab == index,
                                    onClick = {
                                        mainViewModel.selectTab(index)
                                        when (index) {
                                            0 -> navigator.replaceAll(HomeScreen())
                                            1 -> navigator.replaceAll(DiscoverScreen())
                                            2 -> navigator.replaceAll(ProfileScreen())
                                        }
                                    },
                                    colors = NavigationBarItemDefaults.colors(
                                        indicatorColor = Color.White.copy(alpha = 0.2f)
                                    )
                                )
                            }
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
                     CurrentScreen()
                }
            }
        }
    }
}