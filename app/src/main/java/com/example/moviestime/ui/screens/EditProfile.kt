package com.example.moviestime.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moviestime.ui.theme.Inter
import com.example.moviestime.ui.theme.MovieMiniTheme
import com.example.moviestime.ui.theme.PlayFair

// -----------------------------------------------------
// 🎨 تعريفات الألوان الدقيقة (من ملف colors.xml)
// -----------------------------------------------------

val BackgroundColor = Color(0xFF171311)
val PrimaryColor = Color(0xFF9E1938)
val TextColor = Color(0xFFF5F1E8)
val GoldColor = Color(0xFFE8C547)
val CardColor = Color(0xFF231F1C)
val MutedColor = Color(0xFFB5AA9C)
val BorderColor = Color(0xFF403935)

// -----------------------------------------------------
// 🛠️ شاشة تعديل الملف الشخصي (EditProfileScreen)
// -----------------------------------------------------

@Composable
fun EditProfileScreen(
    onBackClick: () -> Unit = {},
) {
    var username by remember { mutableStateOf("yousef") }
    var fullName by remember { mutableStateOf("John Doe") }
    var bio by remember { mutableStateOf("Tell us about your love for cinema...") }
    val maxBioLength = 300

    Scaffold(
        topBar = {
            ProfileScreenTopBar(title = "Edit Profile", onBackClick = onBackClick)
        },
        containerColor = BackgroundColor
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(16.dp))

            AvatarSection(
                initials = username.firstOrNull()?.uppercase() ?: "Y",
                goldColor = GoldColor,
                primaryColor = PrimaryColor
            )

            Spacer(Modifier.height(32.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
                    .imePadding(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                EditProfileTextField(
                    value = username, onValueChange = { username = it }, label = "Username", placeholder = "yousef", supportingText = "Your unique username visible to others",
                    cardColor = CardColor, textColor = TextColor, mutedColor = MutedColor, borderColor = BorderColor
                )

                EditProfileTextField(
                    value = fullName, onValueChange = { fullName = it }, label = "Full Name", placeholder = "John Doe", supportingText = "Your display name (optional)",
                    cardColor = CardColor, textColor = TextColor, mutedColor = MutedColor, borderColor = BorderColor
                )

                EditProfileBioField(
                    value = bio, onValueChange = { if (it.length <= maxBioLength) bio = it }, label = "Bio", placeholder = "Tell us about your love for cinema...", maxBioLength = maxBioLength,
                    cardColor = CardColor, textColor = TextColor, mutedColor = MutedColor, borderColor = BorderColor
                )
            }

            Button(
                onClick = { /* Handle Save Changes */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryColor,
                    contentColor = TextColor
                )
            ) {
                Text(
                    text = "Save Changes",
                    fontFamily = Inter,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        }
    }
}

// -----------------------------------------------------
// الدوال المساعدة (Composable Functions)
// -----------------------------------------------------

@Composable
fun ProfileScreenTopBar(title: String, onBackClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 40.dp)
    ) {
        IconButton(
            onClick = onBackClick,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .size(44.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = TextColor,
                modifier = Modifier.size(24.dp)
            )
        }

        Text(
            text = title,
            fontFamily = PlayFair,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            color = TextColor,
            textAlign = TextAlign.Center,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
fun AvatarSection(initials: String, goldColor: Color, primaryColor: Color) {
    Box(modifier = Modifier.size(100.dp)) {
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
                text = initials,
                fontFamily = Inter,
                fontWeight = FontWeight.Bold,
                fontSize = 40.sp,
                color = TextColor
            )
        }
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(x = 4.dp, y = 4.dp)
                .size(32.dp)
                .clip(CircleShape)
                .background(goldColor)
                .clickable { /* Handle image upload */ },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.CameraAlt,
                contentDescription = "Change Avatar",
                tint = BackgroundColor,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Composable
fun EditProfileTextField(
    value: String, onValueChange: (String) -> Unit, label: String, placeholder: String, supportingText: String?,
    cardColor: Color, textColor: Color, mutedColor: Color, borderColor: Color
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label, fontFamily = Inter, fontWeight = FontWeight.SemiBold, fontSize = 16.sp, color = textColor,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        OutlinedTextField(
            value = value, onValueChange = onValueChange, singleLine = true,
            placeholder = { Text(text = placeholder, fontFamily = Inter, color = mutedColor) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = cardColor, unfocusedContainerColor = cardColor,
                focusedBorderColor = borderColor, unfocusedBorderColor = borderColor.copy(alpha = 0.5f),
                cursorColor = textColor, focusedTextColor = textColor, unfocusedTextColor = textColor
            )
        )
        if (supportingText != null) {
            Text(
                text = supportingText, fontFamily = Inter, fontSize = 12.sp, color = mutedColor,
                modifier = Modifier.padding(top = 4.dp, start = 16.dp)
            )
        }
    }
}

@Composable
fun EditProfileBioField(
    value: String, onValueChange: (String) -> Unit, label: String, placeholder: String, maxBioLength: Int,
    cardColor: Color, textColor: Color, mutedColor: Color, borderColor: Color
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label, fontFamily = Inter, fontWeight = FontWeight.SemiBold, fontSize = 16.sp, color = textColor,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        OutlinedTextField(
            value = value, onValueChange = onValueChange, minLines = 3, maxLines = 5,
            placeholder = { Text(text = placeholder, fontFamily = Inter, color = mutedColor) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = cardColor, unfocusedContainerColor = cardColor,
                focusedBorderColor = borderColor, unfocusedBorderColor = borderColor.copy(alpha = 0.5f),
                cursorColor = textColor, focusedTextColor = textColor, unfocusedTextColor = textColor
            ),
            supportingText = {
                Text(
                    text = "${value.length}/$maxBioLength characters", fontFamily = Inter, fontSize = 12.sp, color = mutedColor,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun EditProfilePreview() {
    MovieMiniTheme(darkTheme = true) {
        EditProfileScreen(onBackClick = {})
    }
}