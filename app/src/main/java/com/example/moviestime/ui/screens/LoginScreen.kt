package com.example.moviestime.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.moviestime.ui.components.login.CinematicCard
import com.example.moviestime.ui.components.login.LoginAccentColor
import com.example.moviestime.ui.components.login.LoginOnAccentColor
import com.example.moviestime.ui.components.login.LoginOnSurfaceColor
import com.example.moviestime.ui.components.login.NeonTextField
import com.example.moviestime.viewmodel.AuthViewModel

@Composable

fun LoginScreen(
    authViewModel: AuthViewModel = viewModel()
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var isRegisterMode by remember { mutableStateOf(false) }

    val uiState by authViewModel.uiState.collectAsState()
    val heroTitle = if (isRegisterMode) "Create Account" else "Welcome Back"
    val heroSubtitle = if (isRegisterMode) {
        "Step into the theater of stories."
    } else {
        "Log in to continue your cinematic journey."
    }

    val colorScheme = MaterialTheme.colorScheme
    val heroStyle = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold, fontSize = 24.sp)
    val subtitleStyle = MaterialTheme.typography.bodySmall
    val backgroundBrush = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF090505),
            Color(0xFF140A08),
            Color(0xFF1C0F0A)
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundBrush),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LoginBranding()
            Spacer(Modifier.height(28.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                CinematicCard(
                    modifier = Modifier
                        .fillMaxWidth(0.87f)
                ) {
                    Text(
                        text = heroTitle,
                        style = heroStyle,
                        color = LoginOnSurfaceColor,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Text(
                        text = heroSubtitle,
                        style = subtitleStyle,
                        color = LoginOnSurfaceColor.copy(alpha = 0.7f),
                        modifier = Modifier.padding(bottom = 24.dp)
                    )

                    if (isRegisterMode) {
                        NeonTextField(
                            value = name,
                            onValueChange = { name = it },
                            label = "Full Name",
                            icon = Icons.Default.Person,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(Modifier.height(16.dp))
                    }

                    NeonTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = "Email",
                        icon = Icons.Default.Email,
                        keyboardType = KeyboardType.Email,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(16.dp))

                    NeonTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = "Password",
                        icon = Icons.Default.Lock,
                        keyboardType = KeyboardType.Password,
                        isPassword = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    if (uiState.error != null) {
                        Spacer(Modifier.height(10.dp))
                        Text(
                            text = uiState.error!!,
                            color = colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    Spacer(Modifier.height(26.dp))

                    if (uiState.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(40.dp),
                            color = LoginAccentColor
                        )
                    } else {
                        Button(
                            onClick = {
                                if (isRegisterMode) authViewModel.register(
                                    email,
                                    password,
                                    name
                                )
                                else authViewModel.login(email, password)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            shape = MaterialTheme.shapes.large,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = LoginAccentColor,
                                contentColor = LoginOnAccentColor
                            ),
                            elevation = ButtonDefaults.buttonElevation(10.dp)
                        ) {
                            Text(
                                text = if (isRegisterMode) "Sign Up" else "Login",
                                color = LoginOnAccentColor,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                    }

                    Spacer(Modifier.height(16.dp))

                    TextButton(onClick = { isRegisterMode = !isRegisterMode }) {
                        Text(
                            text = if (isRegisterMode) "Already have an account? Login" else "No account? Register",
                            color = LoginAccentColor,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    Spacer(Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
private fun LoginBranding() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(84.dp)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0xFFFFE08C),
                            Color(0xFFFFB347)
                        )
                    ),
                    shape = RoundedCornerShape(26.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Movie,
                contentDescription = null,
                tint = Color(0xFF2B1200),
                modifier = Modifier.size(38.dp)
            )
        }

        Spacer(Modifier.height(14.dp))

        val titleGradient = Brush.linearGradient(
            listOf(Color(0xFFFFF8E1), Color(0xFFFFB347))
        )
        Text(
            text = buildAnnotatedString {
                withStyle(
                    SpanStyle(
                        brush = titleGradient,
                        fontWeight = FontWeight.Bold
                    )
                ) {
                    append("CineVault")
                }
            },
            style = MaterialTheme.typography.headlineLarge
        )

        Spacer(Modifier.height(4.dp))

        Text(
            text = "Your personal cinema companion",
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFFE7CFA3)
        )
    }
}