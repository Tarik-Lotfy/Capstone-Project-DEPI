package com.example.moviestime.data.notification

import android.Manifest
import android.app.Notification
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import coil.Coil
import coil.request.ImageRequest
import com.example.moviestime.R
import com.example.moviestime.data.model.Movie
import com.example.moviestime.utils.NotificationUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import android.os.Build
import androidx.annotation.RequiresPermission
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.scale

class AppNotificationManager(private val context: Context) {

    init {
        NotificationUtils.createNotificationChannel(context)
    }

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    suspend fun sendMovieAddedNotification(movie: Movie) {
        val notificationId = movie.id

        val largeIcon = loadBitmapFromUrl(movie.posterPath)

        val notification = NotificationCompat.Builder(context, NotificationUtils.getNotificationChannelId())
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setLargeIcon(largeIcon)
            .setContentTitle("🎬 Added to Watchlist!")
            .setContentText("“${movie.title}” has been added to your favorites.")
            .setStyle(NotificationCompat.BigTextStyle().bigText("“${movie.title}” has been added to your watchlist. Enjoy!"))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        showNotification(notificationId, notification)
    }

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    suspend fun sendNewMovieReleasedNotification(movie: Movie) {
        val notificationId = movie.id + 1000

        val largeIcon = loadBitmapFromUrl(movie.posterPath)

        val notification = NotificationCompat.Builder(context, NotificationUtils.getNotificationChannelId())
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setLargeIcon(largeIcon)
            .setContentTitle("🎉 New Movie Released!")
            .setContentText("“${movie.title}” is now available to watch!")
            .setStyle(NotificationCompat.BigTextStyle().bigText("“${movie.title}” is now available to watch! Don't miss it."))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        showNotification(notificationId, notification)
    }

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    suspend fun sendWeeklyRecommendationNotification(movies: List<Movie>) {
        if (movies.isEmpty()) return

        val notificationId = System.currentTimeMillis().toInt()

        val firstMovie = movies.first()
        val largeIcon = loadBitmapFromUrl(firstMovie.posterPath)

        val movieTitles = movies.joinToString(", ") { "“${it.title}”" }

        val notification = NotificationCompat.Builder(context, NotificationUtils.getNotificationChannelId())
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setLargeIcon(largeIcon)
            .setContentTitle("🍿 Weekly Recommendations")
            .setContentText("Check out: ${movies.first().title} and more!")
            .setStyle(NotificationCompat.BigTextStyle().bigText("We recommend: $movieTitles"))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        showNotification(notificationId, notification)
    }

    private suspend fun loadBitmapFromUrl(url: String?): Bitmap? {
        if (url == null) {
            return getDefaultBitmap()
        }

        return withContext(Dispatchers.IO) {
            try {
                val imageLoader = Coil.imageLoader(context)

                val request = ImageRequest.Builder(context)
                    .data(url)
                    .size(128, 128)
                    .build()

                val result = imageLoader.execute(request)

                if (result.drawable != null) {
                    val bitmap = result.drawable!!.toBitmap()
                    return@withContext bitmap.scale(128, 128, false)
                } else {
                    getDefaultBitmap()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                getDefaultBitmap()
            }
        }
    }

    private fun getDefaultBitmap(): Bitmap? {
        return try {
            BitmapFactory.decodeResource(context.resources, R.drawable.ic_launcher_foreground)
        } catch (e: Exception) {
            null
        }
    }

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    private fun showNotification(id: Int, notification: Notification) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val notificationManager = NotificationManagerCompat.from(context)
            if (notificationManager.areNotificationsEnabled()) {
                notificationManager.notify(id, notification)
            }
        } else {
            NotificationManagerCompat.from(context).notify(id, notification)
        }
    }
}