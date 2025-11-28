package com.example.moviestime.ui.screens
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.core.screen.Screen
import com.example.moviestime.R
import com.example.moviestime.ui.theme.Inter
import com.example.moviestime.ui.theme.PlayFair
import com.example.moviestime.viewmodel.AuthViewModel
import com.example.moviestime.viewmodel.LanguageViewModel
import com.example.moviestime.viewmodel.ThemeViewModel

@Composable
fun SettingsScreenContent(
    authViewModel: AuthViewModel,
    languageViewModel: LanguageViewModel,
    themeViewModel: ThemeViewModel,
    onEditProfile: () -> Unit = {},
    onDeleteAccount: () -> Unit = {},
    onSignOut: () -> Unit = {}
) {
    val currentLanguage by languageViewModel.currentLanguage.collectAsState()
    val isDarkTheme by themeViewModel.isDarkThemeEnabled.collectAsState()
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
        Text(
            text = stringResource(R.string.settings),
            fontFamily = PlayFair,
            fontWeight = FontWeight.Bold,
            fontSize = 26.sp,
            color = textColor,
            modifier = Modifier.padding(top = 12.dp, bottom = 24.dp)
        )

        // --- Account Section ---
        SectionHeader(title = stringResource(R.string.account_section), color = mutedColor)
        SettingsGroup(cardColor = cardColor) {
            SettingsItem(
                icon = Icons.Outlined.Person,
                title = stringResource(R.string.edit_profile),
                textColor = textColor,
                onClick = onEditProfile
            )
        }

        Spacer(Modifier.height(24.dp))

        // --- Preferences Section ---
        SectionHeader(title = stringResource(R.string.preferences_section), color = mutedColor)
        SettingsGroup(cardColor = cardColor) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 18.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Outlined.Palette,
                        contentDescription = null,
                        tint = textColor.copy(alpha = 0.7f),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(Modifier.width(16.dp))
                    Text(
                        text = stringResource(R.string.dark_mode),
                        fontFamily = Inter,
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp,
                        color = textColor
                    )
                }
                Switch(
                    checked = isDarkTheme,
                    onCheckedChange = { themeViewModel.setDarkThemeEnabled(it) },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = colorResource(R.color.primary),
                        checkedTrackColor = colorResource(R.color.primary).copy(alpha = 0.5f)
                    )
                )
            }
            HorizontalDivider(
                color = Color.White.copy(alpha = 0.05f),
                thickness = 1.dp,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            SettingsItem(
                icon = Icons.Outlined.Notifications,
                title = stringResource(R.string.notifications),
                textColor = textColor
            )
        }

        Spacer(Modifier.height(24.dp))

        SettingsButton(
            icon = Icons.Default.Language,
            title = if (currentLanguage == "ar") stringResource(R.string.language_english) else stringResource(R.string.language_arabic),
            color = textColor,
            borderColor = textColor.copy(alpha = 0.3f),
            onClick = { languageViewModel.toggleLanguage() }
        )

        Spacer(Modifier.height(16.dp))

        SettingsButton(
            icon = Icons.AutoMirrored.Filled.Logout,
            title = stringResource(R.string.logout),
            color = textColor,
            borderColor = textColor.copy(alpha = 0.3f),
            onClick = {
                authViewModel.logout()
                onSignOut()
            }
        )

        Spacer(Modifier.height(16.dp))

        SettingsButton(
            icon = Icons.Outlined.Delete,
            title = stringResource(R.string.delete_account),
            color = Color(0xFFE53935),
            borderColor = Color(0xFFE53935).copy(alpha = 0.5f),
            onClick = onDeleteAccount
        )

        Spacer(Modifier.height(32.dp))

        // --- Support Section ---
        SectionHeader(title = stringResource(R.string.support_section), color = mutedColor)
        SettingsGroup(cardColor = cardColor) {
            SettingsItem(
                icon = Icons.Outlined.HelpOutline,
                title = stringResource(R.string.help_support),
                textColor = textColor
            )
        }
        Spacer(Modifier.height(12.dp))
        HelpFeedbackCard(
            cardColor = cardColor,
            textColor = textColor,
            mutedColor = mutedColor
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
                contentDescription = stringResource(R.string.settings_item_arrow_cd),
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

@Composable
fun HelpFeedbackCard(
    cardColor: Color,
    textColor: Color,
    mutedColor: Color
) {
    var isExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(cardColor)
            .clickable { isExpanded = !isExpanded }
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = stringResource(R.string.help_support),
                    fontFamily = PlayFair,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = textColor
                )
                Text(
                    text = if (isExpanded) stringResource(R.string.tap_to_hide) else stringResource(R.string.tap_to_expand),
                    fontFamily = Inter,
                    fontSize = 12.sp,
                    color = mutedColor
                )
            }
            Icon(
                imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                contentDescription = null,
                tint = textColor
            )
        }

        if (isExpanded) {
            Spacer(Modifier.height(12.dp))
            Text(
                text = stringResource(R.string.help_card_about_title),
                fontFamily = PlayFair,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = textColor
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.help_card_about_desc),
                fontFamily = Inter,
                fontSize = 14.sp,
                color = mutedColor,
                lineHeight = 20.sp
            )
            Spacer(Modifier.height(12.dp))
            Text(
                text = stringResource(R.string.help_card_support_prompt),
                fontFamily = Inter,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                color = textColor
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = stringResource(R.string.help_card_support_contact),
                fontFamily = Inter,
                fontSize = 14.sp,
                color = mutedColor
            )
        }
    }
}