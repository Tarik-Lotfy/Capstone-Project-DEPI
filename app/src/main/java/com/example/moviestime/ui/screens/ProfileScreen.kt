package com.example.moviestime.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
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
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import coil.compose.AsyncImage
import com.example.moviestime.R
import com.example.moviestime.data.model.Movie
import com.example.moviestime.ui.navigation.MovieDetailsScreenRoute
import com.example.moviestime.ui.navigation.SettingsScreenRoute
import com.example.moviestime.ui.theme.Inter
import com.example.moviestime.ui.theme.PlayFair
import com.example.moviestime.viewmodel.AuthViewModel
import com.example.moviestime.viewmodel.MainViewModel

object ProfileScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val topBarState = LocalAppTopBarState.current
        val mainViewModel = LocalMainViewModel.current
        val authViewModel: AuthViewModel = viewModel()

        LaunchedEffect(Unit) {
            topBarState.value = AppTopBarConfig(
                title = "Profile",
                showBack = false,
                onBack = null,
                trailingContent = {
                    IconButton(onClick = { navigator.push(SettingsScreenRoute) }) {
                        Icon(
                            imageVector = Icons.Outlined.Settings,
                            contentDescription = "Settings",
                            tint = Color.White
                        )
                    }
                }
            )
            // Reload profile when screen appears
            authViewModel.loadUserProfile()
        }

        ProfileScreenContent(
            authViewModel = authViewModel,
            mainViewModel = mainViewModel,
            onMovieClick = { navigator.push(MovieDetailsScreenRoute(it)) },
            onEditProfile = { navigator.push(EditProfileScreen()) }
        )
    }
}

@Composable
fun ProfileScreenContent(
    authViewModel: AuthViewModel,
    mainViewModel: MainViewModel,
    onMovieClick: (Int) -> Unit,
    onEditProfile: () -> Unit
) {
    val userProfile by authViewModel.userProfile.collectAsState()
    val favorites by mainViewModel.favorites.collectAsState()
    val watchlist by mainViewModel.watchlist.collectAsState()
    val watched by mainViewModel.watched.collectAsState()

    // Reload profile when screen becomes visible
    LaunchedEffect(Unit) {
        authViewModel.loadUserProfile()
    }

    val backgroundColor = colorResource(R.color.background)
    val primaryColor = colorResource(R.color.primary)
    val textColor = colorResource(R.color.foreground)
    val mutedColor = colorResource(R.color.muted_foreground)
    val cardColor = colorResource(R.color.card)
    val goldColor = colorResource(R.color.secondary)

    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf("Favorites", "Watched", "Watchlist")
    val pagerState = rememberPagerState(initialPage = selectedTabIndex, pageCount = { tabs.size })

    LaunchedEffect(selectedTabIndex) {
        pagerState.animateScrollToPage(selectedTabIndex)
    }
    LaunchedEffect(pagerState.currentPage) {
        selectedTabIndex = pagerState.currentPage
    }

    val displayName = userProfile.name.ifEmpty { "Movie Lover" }
    val initials = if (displayName.isNotEmpty()) displayName.take(1).uppercase() else "U"
    val username = if (userProfile.email.isNotEmpty()) userProfile.email.substringBefore("@") else displayName.lowercase()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(horizontal = 8.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .border(2.dp, goldColor, CircleShape)
                .padding(4.dp)
                .clip(CircleShape)
                .background(primaryColor),
            contentAlignment = Alignment.Center
        ) {
            if (userProfile.photoUrl != null && userProfile.photoUrl!!.isNotEmpty()) {
                AsyncImage(
                    model = userProfile.photoUrl,
                    contentDescription = "Profile",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                )
            } else {
                Text(
                    text = initials,
                    fontFamily = Inter,
                    fontWeight = FontWeight.Bold,
                    fontSize = 40.sp,
                    color = textColor
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        Text(
            text = displayName,
            fontFamily = PlayFair,
            fontWeight = FontWeight.Bold,
            fontSize = 26.sp,
            color = textColor,
            textAlign = TextAlign.Center
        )

        if (userProfile.bio.isNotEmpty()) {
            Spacer(Modifier.height(8.dp))
            Text(
                text = userProfile.bio,
                fontFamily = Inter,
                fontSize = 14.sp,
                color = mutedColor,
                textAlign = TextAlign.Center,
                maxLines = 2
            )
        }

        if (userProfile.email.isNotEmpty()) {
            Spacer(Modifier.height(4.dp))
            Text(
                text = userProfile.email,
                fontFamily = Inter,
                fontSize = 12.sp,
                color = mutedColor.copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            )
        }

        Spacer(Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            ProfileStat(number = "${favorites.size}", label = "Favorites", textColor, mutedColor)
            ProfileStat(number = "${watchlist.size}", label = "Watchlist", textColor, mutedColor)
            ProfileStat(number = "${watched.size}", label = "Watched", textColor, mutedColor)
        }

        Spacer(Modifier.height(24.dp))

        OutlinedButton(
            onClick = onEditProfile,
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

        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) { page ->
            val list = when (page) {
                0 -> favorites
                1 -> watched
                2 -> watchlist
                else -> emptyList()
            }
            val message = when (page) {
                0 -> "No favorites yet"
                1 -> "No watched movies yet"
                2 -> "Your watchlist is empty"
                else -> "No items yet"
            }

            if (list.isEmpty()) {
                EmptyTabState(
                    message = message,
                    actionText = "Explore movies",
                    mutedColor = mutedColor,
                    goldColor = goldColor,
                    onExploreClick = { mainViewModel.selectTab(1) }
                )
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(bottom = 100.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(list) { movie ->
                        ProfileMovieItem(
                            movie = movie,
                            onClick = { onMovieClick(movie.id) }
                        )
                    }
                }
            }
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
fun EmptyTabState(message: String, actionText: String, mutedColor: Color, goldColor: Color, onExploreClick: () -> Unit) {
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
            modifier = Modifier.clickable { onExploreClick() }
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
