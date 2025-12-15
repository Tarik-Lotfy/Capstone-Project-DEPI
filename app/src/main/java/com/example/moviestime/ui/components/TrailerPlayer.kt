@file:Suppress("COMPOSE_APPLIER_CALL_MISMATCH")

package com.example.moviestime.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView


private fun extractVideoId(input: String): String? {
    if (input.isBlank()) return null
    
    // TMDB provides direct video IDs, so if it doesn't look like a URL, return as-is
    if (!input.contains("/") && !input.contains("?") && !input.contains("&") && !input.contains("http")) {
        return input.trim()
    }
    
    // Only extract from URL if it's actually a URL (for backward compatibility with test URLs)
    val patterns = listOf(
        "(?:youtube\\.com\\/watch\\?v=|youtu\\.be\\/|youtube\\.com\\/embed\\/)([^&\\n?#]+)",
        "youtube\\.com\\/v\\/([^&\\n?#]+)",
        "youtube\\.com\\/.*[?&]v=([^&\\n?#]+)"
    )
    
    for (pattern in patterns) {
        val regex = Regex(pattern)
        val match = regex.find(input)
        if (match != null && match.groupValues.size > 1) {
            return match.groupValues[1]
        }
    }
    
    // If no pattern matches but it's not a URL, return as-is (assume it's a direct ID)
    return input.trim()
}

@Composable
fun TrailerPlayer(
    videoId: String,
    modifier: Modifier = Modifier,
    onPlayerReady: ((YouTubePlayer) -> Unit)? = null,
    onEmbedError: (() -> Unit)? = null
) {
    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current
    val context = LocalContext.current

    val cleanVideoId = remember(videoId) { extractVideoId(videoId) }
    if (cleanVideoId.isNullOrBlank()) {
        android.util.Log.e("TrailerPlayer", "Invalid videoId: $videoId")
        return
    }

    // ONE YouTubePlayerView - created once, never recreated
    val youTubePlayerView = remember {
        android.util.Log.d("TrailerPlayer", "Creating YouTubePlayerView (once)")
        YouTubePlayerView(context).apply {
            enableAutomaticInitialization = false
        }
    }

    // Track the player instance and error state
    var youTubePlayer by remember { mutableStateOf<YouTubePlayer?>(null) }
    var hasEmbedError by remember { mutableStateOf(false) }

    // Lifecycle management - attach/detach observer
    DisposableEffect(Unit) {
        android.util.Log.d("TrailerPlayer", "Attaching lifecycle observer")
        lifecycleOwner.lifecycle.addObserver(youTubePlayerView)
        onDispose {
            android.util.Log.d("TrailerPlayer", "Removing lifecycle observer and releasing player")
            lifecycleOwner.lifecycle.removeObserver(youTubePlayerView)
            youTubePlayerView.release()
        }
    }

    // Initialize ONCE - LaunchedEffect(Unit) guarantees single execution
    LaunchedEffect(Unit) {
        android.util.Log.d("TrailerPlayer", "Initializing player (once)")
        
        val options = IFramePlayerOptions.Builder()
            .controls(0) // Hide YouTube controls
            .fullscreen(0) // Disable fullscreen button
            .rel(0) // Disable related videos
            .ivLoadPolicy(3) // Hide annotations
            .build()

        youTubePlayerView.initialize(
            object : AbstractYouTubePlayerListener() {
                override fun onReady(player: YouTubePlayer) {
                    android.util.Log.d("TrailerPlayer", "Player ready")
                    // Store player instance in outer state
                    youTubePlayer = player
                    // Load initial video
                    if (cleanVideoId.isNotBlank()) {
                        try {
                            player.loadVideo(cleanVideoId, 0f)
                            android.util.Log.d("TrailerPlayer", "Initial video loaded: $cleanVideoId")
                            onPlayerReady?.invoke(player)
                        } catch (e: Exception) {
                            android.util.Log.e("TrailerPlayer", "Error loading initial video: ${e.message}", e)
                            try {
                                player.cueVideo(cleanVideoId, 0f)
                            } catch (e2: Exception) {
                                android.util.Log.e("TrailerPlayer", "Error cueing initial video: ${e2.message}", e2)
                            }
                        }
                    }
                }

                override fun onError(youTubePlayer: YouTubePlayer, error: com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants.PlayerError) {
                    android.util.Log.e("TrailerPlayer", "YouTube Player Error: $error")
                    // Error 152 means video is not embeddable
                    // This error occurs when a video cannot be played in an embedded player
                    if (error == com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants.PlayerError.VIDEO_NOT_PLAYABLE_IN_EMBEDDED_PLAYER) {
                        hasEmbedError = true
                        onEmbedError?.invoke()
                    }
                    super.onError(youTubePlayer, error)
                }

                override fun onCurrentSecond(youTubePlayer: YouTubePlayer, second: Float) {
                    // Log first frame to confirm video is playing
                    if (second > 0f && second < 0.1f) {
                        android.util.Log.d("TrailerPlayer", "Video started playing! Current second: $second")
                    }
                }
            },
            options
        )
    }

    // Update video when videoId changes (player is already initialized)
    LaunchedEffect(cleanVideoId) {
        youTubePlayer?.let { player ->
            android.util.Log.d("TrailerPlayer", "Video ID changed, updating to: $cleanVideoId")
            try {
                player.loadVideo(cleanVideoId, 0f)
            } catch (e: Exception) {
                android.util.Log.w("TrailerPlayer", "loadVideo failed, trying cueVideo: ${e.message}")
                try {
                    player.cueVideo(cleanVideoId, 0f)
                } catch (e2: Exception) {
                    android.util.Log.e("TrailerPlayer", "Error updating video: ${e2.message}", e2)
                }
            }
        }
    }

    // AndroidView just displays the ONE view - it doesn't recreate it
    Box(modifier = modifier) {
        if (hasEmbedError) {
            // Show fallback message when video cannot be embedded
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "This video is not available for embedding.\nPlease watch it on YouTube.",
                    color = Color.White,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            AndroidView(
                factory = { youTubePlayerView },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}
