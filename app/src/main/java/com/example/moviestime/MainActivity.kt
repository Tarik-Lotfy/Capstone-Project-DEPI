package com.example.moviestime

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.moviestime.ui.screens.LoginScreenContent
import com.example.moviestime.ui.screens.MoviesApp
import com.example.moviestime.ui.screens.OnboardingScreen
import com.example.moviestime.ui.screens.SplashScreen
import com.example.moviestime.ui.theme.MovieMiniTheme
import com.example.moviestime.viewmodel.AuthViewModel
import com.example.moviestime.viewmodel.LanguageViewModel
import com.example.moviestime.viewmodel.MainViewModel
import com.example.moviestime.viewmodel.ThemeViewModel
import com.google.firebase.FirebaseApp
import kotlinx.coroutines.delay
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (FirebaseApp.getApps(this).isEmpty()) {
            FirebaseApp.initializeApp(this)
        }
        val prefs = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

        setContent {
            val themeViewModel: ThemeViewModel = viewModel { ThemeViewModel(application) }
            val isDarkTheme by themeViewModel.isDarkThemeEnabled.collectAsState()

            val languageViewModel: LanguageViewModel = viewModel { LanguageViewModel(application) }
            val appLanguage by languageViewModel.currentLanguage.collectAsState()

            val shouldRecreate by languageViewModel.shouldRecreate.collectAsState()

            var hasSeenOnboarding by remember {
                mutableStateOf(prefs.getBoolean("has_seen_onboarding", false))
            }

            LaunchedEffect(appLanguage) {
                val config = resources.configuration
                val locale = when (appLanguage) {
                    "ar" -> Locale("ar")
                    else -> Locale("en")
                }
                config.setLocale(locale)
                resources.updateConfiguration(config, resources.displayMetrics)
            }

            LaunchedEffect(shouldRecreate) {
                if (shouldRecreate) {
                    languageViewModel.onRecreated()
                    recreate()
                }
            }

            MovieMiniTheme(
                darkTheme = isDarkTheme
            ) {
                val authViewModel: AuthViewModel = viewModel()
                val isLoggedIn by authViewModel.isLoggedIn.collectAsState()
                
                var showSplash by remember { mutableStateOf(true) }
                var forceLogin by remember { mutableStateOf(false) }

                // Show medieval splash screen for 3 seconds
                LaunchedEffect(Unit) {
                    delay(3000)
                    showSplash = false
                }

                if (showSplash) {
                    SplashScreen()
                } else {
                    when {
                        !hasSeenOnboarding -> {
                            OnboardingScreen(
                                onFinish = {
                                    prefs.edit().putBoolean("has_seen_onboarding", true).apply()
                                    hasSeenOnboarding = true
                                    forceLogin = true
                                }
                            )
                        }
                        forceLogin -> {
                            LoginScreenContent(authViewModel = authViewModel)
                        }
                        !isLoggedIn -> {
                            LoginScreenContent(authViewModel = authViewModel)
                        }
                        else -> {
                            val mainViewModel: MainViewModel = viewModel { MainViewModel(application) }
                            LaunchedEffect(Unit) {
                                when (intent?.getStringExtra("navigate_to")) {
                                    "profile" -> mainViewModel.selectTab(2)
                                }
                            }
                            MoviesApp(
                                mainViewModel = mainViewModel,
                                themeViewModel = themeViewModel,
                                languageViewModel = languageViewModel,
                            )
                        }
                    }
                }
            }
        }
    }
}
