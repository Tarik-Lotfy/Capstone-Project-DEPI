package com.example.moviestime.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import com.example.moviestime.data.model.Movie
import com.example.moviestime.data.notification.AppNotificationManager
import com.example.moviestime.data.repository.MovieRepository

class NotificationViewModel(application: Application) : AndroidViewModel(application) {
    private val movieRepository = MovieRepository()
    private val appContext = application.applicationContext

    @androidx.annotation.RequiresPermission(android.Manifest.permission.POST_NOTIFICATIONS)
    fun sendWeeklyRecommendations() {
        viewModelScope.launch @androidx.annotation.RequiresPermission(android.Manifest.permission.POST_NOTIFICATIONS) {
            try {
                val popularMovies = movieRepository.getPopularMovies().take(3)
                val notificationManager = AppNotificationManager(appContext)
                notificationManager.sendWeeklyRecommendationNotification(popularMovies)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    @androidx.annotation.RequiresPermission(android.Manifest.permission.POST_NOTIFICATIONS)
    fun sendNewMovieReleased(movie: Movie) {
        viewModelScope.launch {
            try {
                val notificationManager = AppNotificationManager(appContext)
                notificationManager.sendNewMovieReleasedNotification(movie)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}