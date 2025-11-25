package com.example.moviestime.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.moviestime.R
import com.example.moviestime.data.model.Movie
import com.example.moviestime.ui.theme.Inter
import com.example.moviestime.ui.theme.PlayFair
import com.example.moviestime.viewmodel.LanguageViewModel
import com.example.moviestime.viewmodel.MainViewModel
import com.example.moviestime.viewmodel.ThemeViewModel

@Composable
fun ProfileScreen(
    mainViewModel: MainViewModel = viewModel(),
    themeViewModel: ThemeViewModel = viewModel(),
    languageViewModel: LanguageViewModel = viewModel(),
    onMovieClick: (Int) -> Unit
) {
    val watchlist by mainViewModel.favorites.collectAsState()

    val backgroundColor = colorResource(R.color.background)
    val primaryColor = colorResource(R.color.primary)
    val textColor = colorResource(R.color.foreground)
    val mutedColor = colorResource(R.color.muted_foreground)
    val cardColor = colorResource(R.color.card)
    val goldColor = colorResource(R.color.secondary)

    var selectedTabIndex by remember { mutableIntStateOf(1) }
    val tabs = listOf("Reviews", "Watchlist", "Favorites")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp, bottom = 24.dp)
        ) {
            Text(
                text = "Profile",
                fontFamily = PlayFair,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                color = textColor,
                modifier = Modifier.align(Alignment.Center)
            )

            Box(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .size(44.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .border(2.dp, primaryColor, RoundedCornerShape(12.dp))
                    .clickable { /* Settings */ },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.Settings,
                    contentDescription = "Settings",
                    tint = textColor,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        Box(
            modifier = Modifier
                .size(100.dp)
                .border(2.dp, goldColor, CircleShape)
                .padding(4.dp)
                .clip(CircleShape)
                .background(primaryColor),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Y",
                fontFamily = Inter,
                fontWeight = FontWeight.Bold,
                fontSize = 40.sp,
                color = textColor
            )
        }

        Spacer(Modifier.height(16.dp))

        Text(
            text = "youssohuyuiyuiyh",
            fontFamily = PlayFair,
            fontWeight = FontWeight.Bold,
            fontSize = 26.sp,
            color = textColor,
            textAlign = TextAlign.Center
        )

        Text(
            text = "@youssohuyuiyuiyh",
            fontFamily = Inter,
            fontSize = 14.sp,
            color = mutedColor,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            ProfileStat(number = "0", label = "Followers", textColor, mutedColor)
            ProfileStat(number = "0", label = "Following", textColor, mutedColor)
            ProfileStat(number = "${watchlist.size}", label = "Watchlist", textColor, mutedColor)
        }

        Spacer(Modifier.height(24.dp))

        OutlinedButton(
            onClick = {   },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.dp, mutedColor.copy(alpha = 0.3f)),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = Color.Transparent
            )
        ) {
            Text(
                text = "Edit Profile",
                fontFamily = Inter,
                fontWeight = FontWeight.SemiBold,
                color = textColor
            )
        }

        Spacer(Modifier.height(24.dp))

        Container(color = cardColor, shape = RoundedCornerShape(12.dp)) {
            Row(modifier = Modifier.fillMaxWidth()) {
                tabs.forEachIndexed { index, title ->
                    val isSelected = selectedTabIndex == index
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(40.dp)
                            .background(
                                if (isSelected) Color.Black.copy(alpha = 0.3f) else Color.Transparent,
                                RoundedCornerShape(12.dp)
                            )
                            .clickable { selectedTabIndex = index },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = title,
                            fontFamily = Inter,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) textColor else mutedColor,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(20.dp))

        if (selectedTabIndex == 1) {
            if (watchlist.isEmpty()) {
                EmptyTabState(
                    message = "Your watchlist is empty",
                    actionText = "Add movies now",
                    mutedColor = mutedColor,
                    goldColor = goldColor
                )
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(watchlist) { movie ->
                        ProfileMovieItem(
                            movie = movie,
                            onClick = { onMovieClick(movie.id) }
                        )
                    }
                }
            }
        } else {
            EmptyTabState(
                message = "No items yet",
                actionText = "Explore movies",
                mutedColor = mutedColor,
                goldColor = goldColor
            )
        }
    }
}

@Composable
fun ProfileMovieItem(movie: Movie, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .aspectRatio(0.7f)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        AsyncImage(
            model = movie.posterPath,
            contentDescription = movie.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
            placeholder = painterResource(R.drawable.ic_launcher_background),
            error = painterResource(R.drawable.ic_launcher_background)
        )
    }
}

@Composable
fun EmptyTabState(message: String, actionText: String, mutedColor: Color, goldColor: Color) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 40.dp)
    ) {
        Text(text = message, fontFamily = Inter, fontSize = 16.sp, color = mutedColor)
        Spacer(Modifier.height(8.dp))
        Text(
            text = actionText,
            fontFamily = Inter,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = goldColor,
            modifier = Modifier.clickable { }
        )
    }
}

@Composable
fun ProfileStat(number: String, label: String, numberColor: Color, labelColor: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = number,
            fontFamily = Inter,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            color = numberColor
        )
        Text(
            text = label,
            fontFamily = Inter,
            fontSize = 12.sp,
            color = labelColor
        )
    }
}

@Composable
fun Container(
    color: Color,
    shape: androidx.compose.ui.graphics.Shape,
    content: @Composable () -> Unit
) {
    Surface(color = color, shape = shape, modifier = Modifier.fillMaxWidth()) {
        content()
    }
}