package com.example.moviestime.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.moviestime.data.model.Movie

@Composable
fun FeaturedLargeCard(
    movie: Movie,
    onPlayClick: (Movie) -> Unit = {}
) {
    Box(
        modifier = Modifier
            .width(280.dp)
            .height(320.dp)
            .clip(RoundedCornerShape(20.dp))
            .clickable { onPlayClick(movie) }
    ) {
        AsyncImage(
            model = movie.posterPath ?: "https://via.placeholder.com/300x450",
            contentDescription = movie.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(20.dp))
        )

        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp)
                .background(Color.Black.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                .padding(12.dp)
        ) {
            Text(
                text = movie.title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = "${movie.genre} • ${movie.year} • ${movie.duration} min",
                color = Color.White.copy(alpha = 0.85f),
                fontSize = 13.sp
            )

            Spacer(Modifier.height(8.dp))

            Button(
                onClick = { onPlayClick(movie) },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFC107))
            ) {
                Icon(Icons.Default.PlayArrow, contentDescription = "Play")
                Spacer(Modifier.width(6.dp))
                Text("Watch Now", color = Color.Black)
            }
        }
    }
}