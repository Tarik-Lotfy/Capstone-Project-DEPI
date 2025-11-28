package com.example.moviestime.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.random.Random
import com.example.moviestime.R
import com.example.moviestime.ui.theme.PlayFair

@Preview
@Composable
fun SplashScreen() {
    // Load your logo from drawable
    val logo = painterResource(id = R.drawable.logo)
    
    // Get colors from resources
    val cinemaDark = colorResource(R.color.cinema_dark)
    val cinemaBurgundy = colorResource(R.color.cinema_burgundy)
    val cinemaGold = colorResource(R.color.cinema_gold)
    
    // Create medieval gradient background - darker, more dramatic
    val gradientBrush = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF0A0504), // Very dark brown-black
            cinemaDark,
            cinemaBurgundy,
            Color(0xFF6B0F1A), // Darker burgundy
            cinemaDark,
            Color(0xFF0A0504)
        )
    )
    
    // Seed for consistent noise pattern
    val noiseSeed = remember { Random.nextInt() }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradientBrush)
    ) {
        // Noisy texture overlay using Canvas
        Canvas(
            modifier = Modifier.fillMaxSize()
        ) {
            val random = Random(noiseSeed)
            val noiseColor = cinemaGold.copy(alpha = 0.03f)
            
            // Draw random noise points
            repeat(2000) {
                val x = random.nextFloat() * size.width
                val y = random.nextFloat() * size.height
                val radius = random.nextFloat() * 2f + 0.5f
                drawCircle(
                    color = noiseColor,
                    radius = radius,
                    center = Offset(x, y)
                )
            }
        }

        // Medieval Decorative Border - Top
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .align(Alignment.TopCenter)
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            Color.Transparent,
                            cinemaGold.copy(alpha = 0.4f),
                            cinemaGold.copy(alpha = 0.6f),
                            cinemaGold.copy(alpha = 0.4f),
                            Color.Transparent
                        )
                    )
                )
        )

        // Medieval Decorative Border - Bottom
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .align(Alignment.BottomCenter)
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            Color.Transparent,
                            cinemaGold.copy(alpha = 0.4f),
                            cinemaGold.copy(alpha = 0.6f),
                            cinemaGold.copy(alpha = 0.4f),
                            Color.Transparent
                        )
                    )
                )
        )

        // Logo + Text - Medieval Styled
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Medieval decorative frame around logo
            Box(
                modifier = Modifier
                    .size(180.dp)
            ) {
                Image(
                    painter = logo,
                    contentDescription = "Cinevault Logo",
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Medieval-styled text with decorative elements
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Decorative line above text
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(2.dp)
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    cinemaGold.copy(alpha = 0.5f),
                                    cinemaGold.copy(alpha = 0.7f),
                                    cinemaGold.copy(alpha = 0.5f),
                                    Color.Transparent
                                )
                            )
                        )
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Main text with medieval styling - Use PlayFair font explicitly
                Text(
                    text = "CineVault",
                    style = TextStyle(
                        fontFamily = PlayFair,
                        fontWeight = FontWeight.Bold,
                        fontSize = 48.sp,
                        color = cinemaGold,
                        shadow = Shadow(
                            color = Color.Black.copy(alpha = 0.8f),
                            offset = Offset(3f, 3f),
                            blurRadius = 6f
                        ),
                        letterSpacing = 4.sp,
                        textAlign = TextAlign.Center
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Decorative line below text
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(2.dp)
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    cinemaGold.copy(alpha = 0.5f),
                                    cinemaGold.copy(alpha = 0.7f),
                                    cinemaGold.copy(alpha = 0.5f),
                                    Color.Transparent
                                )
                            )
                        )
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Medieval subtitle text
            Text(
                text = "Guardian of Cinematic Treasures",
                style = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = cinemaGold.copy(alpha = 0.7f),
                    shadow = Shadow(
                        color = Color.Black.copy(alpha = 0.8f),
                        offset = Offset(1f, 1f),
                        blurRadius = 3f
                    ),
                    letterSpacing = 2.sp,
                    textAlign = TextAlign.Center
                )
            )
        }
    }
}

