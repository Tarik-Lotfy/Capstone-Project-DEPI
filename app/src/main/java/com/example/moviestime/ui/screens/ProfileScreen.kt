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
import androidx.compose.ui.res.stringResource // إضافة Import
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
import com.example.moviestime.viewmodel.AuthViewModel

@Composable
fun ProfileScreen(
    mainViewModel: MainViewModel = viewModel(),
    themeViewModel: ThemeViewModel = viewModel(),
    languageViewModel: LanguageViewModel = viewModel(),
    authViewModel: AuthViewModel = viewModel(),
    onMovieClick: (Int) -> Unit,
    onSettingsClick: () -> Unit,
    onEditProfileClick: () -> Unit
) {
    val watchlist by mainViewModel.favorites.collectAsState()
    val userProfile by authViewModel.userProfile.collectAsState()

    val userName = if (userProfile.name.isNotEmpty()) userProfile.name else "Movie Lover"
    val userEmail = if (userProfile.email.isNotEmpty()) userProfile.email else "user@example.com"
    val userBio = if (userProfile.bio.isNotEmpty()) userProfile.bio else "Tell us about your love for cinema..."

    val backgroundColor = colorResource(R.color.background)
    val primaryColor = colorResource(R.color.primary)
    val textColor = colorResource(R.color.foreground)
    val mutedColor = colorResource(R.color.muted_foreground)
    val cardColor = colorResource(R.color.card)
    val goldColor = colorResource(R.color.secondary)

    var selectedTabIndex by remember { mutableIntStateOf(1) }
    // ترجمة التابات
    val tabs = listOf(
        "Reviews", // غير موجودة في strings.xml
        stringResource(R.string.watchlist),
        "Favorites" // غير موجودة في strings.xml
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // --- Header ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp, bottom = 24.dp)
        ) {
            Text(
                text = stringResource(R.string.profile), // ترجمة
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
                    .clickable { onSettingsClick() },
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

        // ... (Profile Image & Stats remain the same code)
        Box(
            modifier = Modifier
                .size(100.dp)
                .border(2.dp, goldColor, CircleShape)
                .padding(4.dp)
                .clip(CircleShape)
                .background(primaryColor),
            contentAlignment = Alignment.Center
        ) {
            if (userProfile.photoUrl != null) {
                AsyncImage(
                    model = userProfile.photoUrl,
                    contentDescription = "Profile Picture",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Text(
                    text = userName.take(1).uppercase(),
                    fontFamily = Inter,
                    fontWeight = FontWeight.Bold,
                    fontSize = 40.sp,
                    color = textColor
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        Text(
            text = userName,
            fontFamily = PlayFair,
            fontWeight = FontWeight.Bold,
            fontSize = 26.sp,
            color = textColor,
            textAlign = TextAlign.Center
        )

        Text(
            text = userEmail,
            fontFamily = Inter,
            fontSize = 14.sp,
            color = mutedColor,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(12.dp))
        Text(
            text = userBio,
            fontFamily = Inter,
            fontSize = 13.sp,
            color = textColor.copy(alpha = 0.8f),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 24.dp)
        )

        Spacer(Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            ProfileStat(number = "0", label = "Followers", textColor, mutedColor)
            ProfileStat(number = "0", label = "Following", textColor, mutedColor)
            ProfileStat(number = "${watchlist.size}", label = stringResource(R.string.watchlist), textColor, mutedColor)
        }

        Spacer(Modifier.height(24.dp))

        // Edit Button (Translated)
        OutlinedButton(
            onClick = onEditProfileClick,
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
                text = stringResource(R.string.edit_profile), // ترجمة
                fontFamily = Inter,
                fontWeight = FontWeight.SemiBold,
                color = textColor
            )
        }

        Spacer(Modifier.height(24.dp))

        // --- Tabs ---
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

        // ... (Rest of content logic remains same)
        Spacer(Modifier.height(20.dp))

        // --- Content ---
        if (selectedTabIndex == 1) { // Watchlist Tab
            if (watchlist.isEmpty()) {
                EmptyTabState(
                    message = stringResource(R.string.no_results), // استخدام نص "لا نتائج" مؤقتاً
                    actionText = stringResource(R.string.discover),
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
                message = stringResource(R.string.no_results),
                actionText = stringResource(R.string.discover),
                mutedColor = mutedColor,
                goldColor = goldColor
            )
        }
    }
}

// ... (Helper Composables remain the same)
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