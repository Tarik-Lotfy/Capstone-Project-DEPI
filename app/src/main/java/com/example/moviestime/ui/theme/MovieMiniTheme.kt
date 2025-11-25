package com.example.moviestime.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import com.example.moviestime.R

// Typography with PlayFair for headings and Inter for body text
private val AppTypography = Typography(
    // Display styles - PlayFair for headings
    displayLarge = TextStyle(
        fontFamily = PlayFair,
        fontWeight = FontWeight.Bold,
        fontSize = 57.sp,
        lineHeight = 64.sp,
        letterSpacing = (-0.25).sp
    ),
    displayMedium = TextStyle(
        fontFamily = PlayFair,
        fontWeight = FontWeight.Bold,
        fontSize = 45.sp,
        lineHeight = 52.sp,
        letterSpacing = 0.sp
    ),
    displaySmall = TextStyle(
        fontFamily = PlayFair,
        fontWeight = FontWeight.Bold,
        fontSize = 36.sp,
        lineHeight = 44.sp,
        letterSpacing = 0.sp
    ),
    // Headline styles - PlayFair for headings
    headlineLarge = TextStyle(
        fontFamily = PlayFair,
        fontWeight = FontWeight.SemiBold,
        fontSize = 32.sp,
        lineHeight = 40.sp,
        letterSpacing = 0.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = PlayFair,
        fontWeight = FontWeight.Normal,
        fontSize = 28.sp,
        lineHeight = 36.sp,
        letterSpacing = 0.sp
    ),
    headlineSmall = TextStyle(
        fontFamily = PlayFair,
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.sp
    ),
    // Title styles - PlayFair for headings
    titleLarge = TextStyle(
        fontFamily = PlayFair,
        fontWeight = FontWeight.Medium,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    titleMedium = TextStyle(
        fontFamily = PlayFair,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.15.sp
    ),
    titleSmall = TextStyle(
        fontFamily = PlayFair,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    ),
    // Body styles - Inter for body text
    bodyLarge = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp
    ),
    bodySmall = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.4.sp
    ),
    // Label styles - Inter for body text
    labelLarge = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    ),
    labelMedium = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    ),
    labelSmall = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
)

@Composable
fun MovieMiniTheme(
    darkTheme: Boolean,
    content: @Composable () -> Unit
) {
    // Colors from colors.xml
    val cinemaDark = colorResource(R.color.cinema_dark)
    val foreground = colorResource(R.color.foreground)
    val cinemaCard = colorResource(R.color.cinema_card)
    val cinemaBurgundy = colorResource(R.color.cinema_burgundy)
    val cinemaGold = colorResource(R.color.cinema_gold)
    val secondaryForeground = colorResource(R.color.secondary_foreground)
    val accent = colorResource(R.color.accent)
    val accentForeground = colorResource(R.color.accent_foreground)
    val muted = colorResource(R.color.muted)
    val mutedForeground = colorResource(R.color.muted_foreground)
    val border = colorResource(R.color.border)
    val ring = colorResource(R.color.ring)
    val destructive = colorResource(R.color.destructive)
    val destructiveForeground = colorResource(R.color.destructive_foreground)

    val colorScheme = if (darkTheme) {
        darkColorScheme(
            primary = cinemaBurgundy,
            onPrimary = foreground,
            secondary = cinemaGold,
            onSecondary = secondaryForeground,
            tertiary = accent,
            onTertiary = accentForeground,
            background = cinemaDark,
            onBackground = foreground,
            surface = cinemaCard,
            onSurface = foreground,
            surfaceVariant = muted,
            onSurfaceVariant = mutedForeground,
            error = destructive,
            onError = destructiveForeground,
            errorContainer = destructive.copy(alpha = 0.2f),
            onErrorContainer = destructiveForeground,
            outline = border,
            outlineVariant = ring, // Use ring color for focus states
            scrim = Color.Black,
            inverseSurface = foreground,
            inverseOnSurface = cinemaDark,
            inversePrimary = ring // Use ring color for focus indicators
        )
    } else {
        lightColorScheme(
            primary = cinemaBurgundy,
            onPrimary = foreground,
            secondary = cinemaGold,
            onSecondary = secondaryForeground,
            tertiary = accent,
            onTertiary = accentForeground,
            background = cinemaDark,
            onBackground = foreground,
            surface = cinemaCard,
            onSurface = foreground,
            surfaceVariant = muted,
            onSurfaceVariant = mutedForeground,
            error = destructive,
            onError = destructiveForeground,
            errorContainer = destructive.copy(alpha = 0.2f),
            onErrorContainer = destructiveForeground,
            outline = border,
            outlineVariant = ring, // Use ring color for focus states
            scrim = Color.Black,
            inverseSurface = foreground,
            inverseOnSurface = cinemaDark,
            inversePrimary = ring // Use ring color for focus indicators
        )
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = android.graphics.Color.TRANSPARENT
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        content = content
    )
}