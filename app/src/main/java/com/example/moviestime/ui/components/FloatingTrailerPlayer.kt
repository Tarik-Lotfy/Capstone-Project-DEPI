package com.example.moviestime.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.zIndex
import kotlin.math.roundToInt

@Composable
fun FloatingTrailerPlayer(
    videoId: String,
    isVisible: Boolean,
    onDismiss: () -> Unit,
    onEmbedError: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }
    
    // Reset offset when visibility changes
    LaunchedEffect(isVisible) {
        if (!isVisible) {
            offsetX = 0f
            offsetY = 0f
        }
    }
    
    // Debug logging
    LaunchedEffect(isVisible, videoId) {
        if (isVisible) {
            android.util.Log.d("FloatingTrailerPlayer", "Floating player visible with videoId: $videoId")
        }
    }
    
    if (isVisible) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp)
                .height(200.dp)
                .offset {
                    IntOffset(offsetX.roundToInt(), offsetY.roundToInt())
                }
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDrag = { change, dragAmount ->
                            change.consume()
                            offsetX += dragAmount.x
                            offsetY += dragAmount.y
                        }
                    )
                }
                .shadow(16.dp, RoundedCornerShape(12.dp))
                .clip(RoundedCornerShape(12.dp))
                .background(Color.Black)
        ) {
            // Use the same TrailerPlayer - do NOT use key() as it violates the single instance rule
            // The TrailerPlayer handles videoId changes internally via LaunchedEffect
            android.util.Log.d("FloatingTrailerPlayer", "Rendering TrailerPlayer in floating container")
            TrailerPlayer(
                videoId = videoId,
                modifier = Modifier.fillMaxSize(),
                onEmbedError = {
                    android.util.Log.d("FloatingTrailerPlayer", "Embed error in floating player")
                    onEmbedError?.invoke()
                    // Dismiss floating player on embed error
                    onDismiss()
                }
            )
            
            // Close button
            IconButton(
                onClick = onDismiss,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(32.dp)
                    .zIndex(1f)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

