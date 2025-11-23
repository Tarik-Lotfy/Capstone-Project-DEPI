package com.example.moviestime.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.moviestime.R
import com.example.moviestime.data.model.Movie

import com.example.moviestime.viewmodel.MainViewModel
import com.example.moviestime.viewmodel.MovieDetailsViewModel

@SuppressLint("MissingPermission") // لتجاهل خطأ التحقق من الإذن هنا لأننا نعالجه في MainActivity
@Composable
fun MovieDetailsScreen(
    movieId: Int,
    onBack: () -> Unit = {},
    mainViewModel: MainViewModel,
    onPlayClick: (Movie) -> Unit = {},
    onFavoriteClick: (Movie) -> Unit = {},
    onShareClick: (Movie) -> Unit = {}
) {
    val viewModel: MovieDetailsViewModel = viewModel()
    val movieState by viewModel.movieDetails.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val favorites by mainViewModel.favorites.collectAsState()

    LaunchedEffect(movieId) {
        viewModel.loadMovieDetails(movieId)
    }

    // ألوان الثيم السينمائي
    val backgroundColor = colorResource(R.color.background)
    val primaryColor = colorResource(R.color.primary)
    val textColor = colorResource(R.color.foreground)
    val cardColor = colorResource(R.color.card)
    val goldColor = colorResource(R.color.secondary)

    if (isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = primaryColor)
        }
    } else {
         movieState?.let { movie ->
            val isFav = favorites.any { it.id == movie.id }

            MovieDetailsContent(
                movie = movie,
                isFavorite = isFav,
                onBack = onBack,
                onPlayClick = { onPlayClick(movie) },
                 onFavoriteClick = {
                    mainViewModel.toggleFavorite(movie)
                },
                backgroundColor = backgroundColor,
                primaryColor = primaryColor,
                textColor = textColor,
                cardColor = cardColor,
                goldColor = goldColor
            )
        } ?: run {
             Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(backgroundColor),
                contentAlignment = Alignment.Center
            ) {
                Text("فشل تحميل تفاصيل الفيلم", color = textColor)
            }
        }
    }
}

// تأكد أن هذه الدالة تأخذ جميع الباراميترات كما هو موضح هنا
@Composable
fun MovieDetailsContent(
    movie: Movie,
    isFavorite: Boolean,
    onBack: () -> Unit,
    onPlayClick: () -> Unit,       // تم تعديل التوقيع ليكون () -> Unit
    onFavoriteClick: () -> Unit,   // تم تعديل التوقيع ليكون () -> Unit
    backgroundColor: Color,
    primaryColor: Color,
    textColor: Color,
    cardColor: Color,
    goldColor: Color
) {
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            // --- قسم الهيدر (صورة الخلفية + البوستر) ---
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(420.dp)
            ) {
                // 1. صورة الخلفية العريضة
                AsyncImage(
                    model = movie.backdropPath ?: movie.posterPath,
                    contentDescription = "Backdrop",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .alpha(0.65f),
                    error = painterResource(R.drawable.ic_launcher_background)
                )

                // 2. تدرج لوني
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    backgroundColor.copy(alpha = 0.3f),
                                    backgroundColor.copy(alpha = 0.9f),
                                    backgroundColor
                                ),
                                startY = 100f
                            )
                        )
                )

                // 3. صورة البوستر
                AsyncImage(
                    model = movie.posterPath,
                    contentDescription = "Poster",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(start = 20.dp, bottom = 0.dp)
                        .width(130.dp)
                        .height(190.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .border(1.dp, Color.White.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
                )

                // 4. العنوان والتقييم
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(start = 166.dp, bottom = 10.dp, end = 16.dp)
                ) {
                    Text(
                        text = movie.title,
                         fontWeight = FontWeight.Bold,
                        fontSize = 26.sp,
                        color = textColor,
                        lineHeight = 32.sp
                    )

                    Spacer(Modifier.height(8.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Star,
                            contentDescription = null,
                            tint = goldColor,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text = String.format("%.1f", movie.rating),
                             fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = textColor
                        )

                        Spacer(Modifier.width(16.dp))

                        Text(
                            text = "${movie.duration} min",
                             fontSize = 14.sp,
                            color = textColor.copy(alpha = 0.7f)
                        )
                    }

                    Spacer(Modifier.height(8.dp))

                    Surface(
                        color = cardColor,
                        shape = RoundedCornerShape(6.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, color = Color.White.copy(0.1f)),
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        Text(
                            text = movie.genre.split(",").firstOrNull() ?: "Movie",
                             fontSize = 12.sp,
                            color = textColor.copy(alpha = 0.9f),
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        )
                    }
                }
            }

            // --- قسم الأزرار والقصة ---
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 24.dp)
            ) {
                // زر الإضافة للمفضلة
                Button(
                    onClick = onFavoriteClick, // الآن هذا المتغير معرف في الباراميترات بالأعلى
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        // هذه المتغيرات (isFavorite, primaryColor) معرفة في الباراميترات أيضاً
                        containerColor = if (isFavorite) Color.DarkGray else primaryColor,
                        contentColor = Color.White
                    ),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
                ) {
                    Icon(
                        if (isFavorite) Icons.Default.Check else Icons.Default.Add,
                        contentDescription = null,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = if (isFavorite) "Listed" else "Add to Watchlist",
                         fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp
                    )
                }

                Spacer(Modifier.height(32.dp))

                Text(
                    text = "Overview",
                     fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    color = textColor
                )

                Spacer(Modifier.height(12.dp))

                Text(
                    text = movie.overview.ifEmpty { "لا يوجد وصف متاح لهذا الفيلم حالياً." },
                     fontSize = 15.sp,
                    color = textColor.copy(alpha = 0.8f),
                    lineHeight = 26.sp,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Justify
                )

                Spacer(Modifier.height(32.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    DetailItem(label = "Release date", value = movie.year, textColor = textColor)
                    DetailItem(label = "Language", value = "English", textColor = textColor)
                }
            }

            Spacer(Modifier.height(50.dp))
        }

         IconButton(
            onClick = onBack,
            modifier = Modifier
                .padding(top = 45.dp, start = 20.dp)
                .size(42.dp)
                .background(Color.Black.copy(alpha = 0.6f), RoundedCornerShape(12.dp))
                .border(1.dp, Color.White.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Go back",
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
fun DetailItem(label: String, value: String, textColor: Color) {
    Column {
        Text(
            text = label,
             fontWeight = FontWeight.Medium,
            fontSize = 13.sp,
            color = textColor.copy(alpha = 0.5f)
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = value,
             fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            color = textColor
        )
    }
}