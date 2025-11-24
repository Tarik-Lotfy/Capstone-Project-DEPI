package com.example.moviestime.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.moviestime.R
import com.example.moviestime.ui.theme.Inter
import com.example.moviestime.ui.theme.PlayFair
import com.example.moviestime.viewmodel.LanguageViewModel
import com.example.moviestime.viewmodel.MainViewModel
import com.example.moviestime.viewmodel.ThemeViewModel

@Composable
fun ProfileScreen(
    mainViewModel: MainViewModel = viewModel(),
    themeViewModel: ThemeViewModel = viewModel(),
    languageViewModel: LanguageViewModel = viewModel()
) {
    val backgroundColor = colorResource(R.color.background)
    val primaryColor = colorResource(R.color.primary)
    val textColor = colorResource(R.color.foreground)
    val mutedColor = colorResource(R.color.muted_foreground)
    val cardColor = colorResource(R.color.card)
    val goldColor = colorResource(R.color.secondary)

    var selectedTabIndex by remember { mutableIntStateOf(0) }
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

            IconButton(
                onClick = { /* TODO: Open Settings BottomSheet or Screen */ },
                modifier = Modifier
                    .align(Alignment.CenterEnd)

                    .border(2.dp, primaryColor, RoundedCornerShape(12.dp))
                    .size(44.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Settings,
                    contentDescription = "Settings",
                    tint = textColor,
                    modifier = Modifier.size(22.dp)
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
            ProfileStat(number = "0", label = "Reviews", textColor, mutedColor)
        }

        Spacer(Modifier.height(24.dp))

        OutlinedButton(
            onClick = { /* TODO */ },
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

        Spacer(Modifier.height(40.dp))


        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = when (selectedTabIndex) {
                    0 -> "No reviews yet"
                    1 -> "Watchlist is empty"
                    else -> "No favorites yet"
                },
                fontFamily = Inter,
                fontSize = 16.sp,
                color = mutedColor
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = when (selectedTabIndex) {
                    0 -> "Write your first review"
                    1 -> "Add movies to watchlist"
                    else -> "Mark movies as favorite"
                },
                fontFamily = Inter,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = goldColor,
                modifier = Modifier.clickable { /* TODO */ }
            )
        }
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
    Surface(
        color = color,
        shape = shape,
        modifier = Modifier.fillMaxWidth()
    ) {
        content()
    }
}