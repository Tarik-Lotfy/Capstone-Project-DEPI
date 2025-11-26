package com.example.moviestime.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moviestime.R
import com.example.moviestime.ui.theme.Inter
import com.example.moviestime.ui.theme.PlayFair

@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    onSignOut: () -> Unit,
    onEditProfile: () -> Unit = {},
    onDeleteAccount: () -> Unit = {},
    onLanguageChange: () -> Unit = {},
    currentLanguage: String = "en"
) {
    val backgroundColor = colorResource(R.color.background)
    val cardColor = colorResource(R.color.card)
    val textColor = colorResource(R.color.foreground)
    val mutedColor = colorResource(R.color.muted_foreground)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // --- Header ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp, bottom = 24.dp)
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = textColor
                )
            }

            Text(
                text = stringResource(R.string.settings), // ترجمة العنوان
                fontFamily = PlayFair,
                fontWeight = FontWeight.Bold,
                fontSize = 26.sp,
                color = textColor,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        // --- Account Section ---
        SectionHeader(title = "ACCOUNT", color = mutedColor) // يمكن إضافتها لملف strings لاحقاً
        SettingsGroup(cardColor = cardColor) {
            SettingsItem(
                icon = Icons.Outlined.Person,
                title = stringResource(R.string.edit_profile), // ترجمة
                textColor = textColor,
                onClick = onEditProfile
            )
        }

        Spacer(Modifier.height(24.dp))

        // --- Preferences Section ---
        SectionHeader(title = "PREFERENCES", color = mutedColor)
        SettingsGroup(cardColor = cardColor) {
            SettingsItem(
                icon = Icons.Outlined.Notifications,
                title = stringResource(R.string.notifications),
                textColor = textColor,
                showDivider = true
            )

        }

        Spacer(Modifier.height(24.dp))

        // --- Support Section ---
        SectionHeader(title = "SUPPORT", color = mutedColor)
        SettingsGroup(cardColor = cardColor) {
            SettingsItem(
                icon = Icons.Outlined.HelpOutline,
                title = stringResource(R.string.help_support), // ترجمة
                textColor = textColor
            )
        }

        Spacer(Modifier.height(40.dp))

         SettingsButton(
            icon = Icons.Default.Language,
             title = if (currentLanguage == "ar") stringResource(R.string.language_english) else stringResource(R.string.language_arabic),
            color = textColor,
            borderColor = textColor.copy(alpha = 0.3f),
            onClick = onLanguageChange
        )

        Spacer(Modifier.height(16.dp))

         SettingsButton(
            icon = Icons.AutoMirrored.Filled.Logout,
            title = stringResource(R.string.logout),
            color = textColor,
            borderColor = textColor.copy(alpha = 0.3f),
            onClick = onSignOut
        )

        Spacer(Modifier.height(16.dp))

        SettingsButton(
            icon = Icons.Outlined.Delete,
            title = "Delete Account",
            color = Color(0xFFE53935),
            borderColor = Color(0xFFE53935).copy(alpha = 0.5f),
            onClick = onDeleteAccount
        )
    }
}

 @Composable
fun SectionHeader(title: String, color: Color) {
    Text(
        text = title,
        fontFamily = PlayFair,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        color = color,
        modifier = Modifier.padding(bottom = 8.dp, start = 4.dp)
    )
}

@Composable
fun SettingsGroup(
    cardColor: Color,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(cardColor)
            .border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(16.dp))
    ) {
        content()
    }
}

@Composable
fun SettingsItem(
    icon: ImageVector,
    title: String,
    textColor: Color,
    showDivider: Boolean = false,
    onClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 18.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = textColor.copy(alpha = 0.7f),
                    modifier = Modifier.size(24.dp)
                )
                Spacer(Modifier.width(16.dp))
                Text(
                    text = title,
                    fontFamily = Inter,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
                    color = textColor
                )
            }
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "Arrow",
                tint = textColor.copy(alpha = 0.3f)
            )
        }
        if (showDivider) {
            HorizontalDivider(
                color = Color.White.copy(alpha = 0.05f),
                thickness = 1.dp,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    }
}

@Composable
fun SettingsButton(
    icon: ImageVector,
    title: String,
    color: Color,
    borderColor: Color,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .border(1.dp, borderColor, RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(24.dp)
        )
        Spacer(Modifier.width(16.dp))
        Text(
            text = title,
            fontFamily = Inter,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            color = color
        )
    }
}