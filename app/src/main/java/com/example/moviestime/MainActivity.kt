package com.example.moviestime

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.moviestime.ui.screens.LoginScreenContent
import com.example.moviestime.ui.screens.MoviesApp
import com.example.moviestime.ui.screens.OnboardingScreen
import com.example.moviestime.ui.screens.SplashScreen
import com.example.moviestime.ui.theme.MovieMiniTheme
import kotlinx.coroutines.delay
import com.example.moviestime.viewmodel.AuthViewModel
import com.example.moviestime.viewmodel.LanguageViewModel
import com.example.moviestime.viewmodel.MainViewModel
import com.example.moviestime.viewmodel.ThemeViewModel
import com.google.firebase.FirebaseApp
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (FirebaseApp.getApps(this).isEmpty()) {
            FirebaseApp.initializeApp(this)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val notificationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
            if (notificationPermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), 1001)
            }
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
                                }
                            )
                        }
                        !isLoggedIn -> {
                            LoginScreenContent(authViewModel = authViewModel)
                        }
                        else -> {
                            val mainViewModel: MainViewModel = viewModel { MainViewModel(application) }
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