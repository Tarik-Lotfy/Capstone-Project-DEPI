package com.example.moviestime

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.moviestime.ui.screens.LoginScreen
import com.example.moviestime.ui.screens.MoviesApp
import com.example.moviestime.ui.theme.MovieMiniTheme
import com.example.moviestime.viewmodel.AuthViewModel
import com.example.moviestime.viewmodel.ThemeViewModel
import com.example.moviestime.viewmodel.LanguageViewModel
import com.example.moviestime.viewmodel.MainViewModel
import com.example.moviestime.viewmodel.NotificationViewModel
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

        setContent {
            val themeViewModel: ThemeViewModel = viewModel { ThemeViewModel(application) }
            val isDarkTheme by themeViewModel.isDarkThemeEnabled.collectAsState()

            val languageViewModel: LanguageViewModel = viewModel { LanguageViewModel(application) }
            val appLanguage by languageViewModel.appLanguage.collectAsState()

            LaunchedEffect(appLanguage) {
                val config = resources.configuration
                val locale = when (appLanguage) {
                    "ar" -> Locale("ar")
                    else -> Locale("en")
                }
                config.setLocale(locale)
                resources.updateConfiguration(config, resources.displayMetrics)
            }

            MovieMiniTheme(
                darkTheme = isDarkTheme,
                dynamicColor = true
            ) {
                val authViewModel: AuthViewModel = viewModel()
                val isLoggedIn by authViewModel.isLoggedIn.collectAsState()

                if (isLoggedIn) {
                    val mainViewModel: MainViewModel = viewModel { MainViewModel(application) }
                    val notificationViewModel: NotificationViewModel = viewModel { NotificationViewModel(application) }
                    MoviesApp(
                        mainViewModel = mainViewModel,
                        themeViewModel = themeViewModel,
                        notificationViewModel = notificationViewModel,
                        languageViewModel = languageViewModel
                    )
                } else {
                    LoginScreen()
                }
            }
        }
    }
}