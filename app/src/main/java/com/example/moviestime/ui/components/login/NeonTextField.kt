package com.example.moviestime.ui.components.login

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation

@Composable
fun NeonTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    icon: ImageVector,
    keyboardType: KeyboardType = KeyboardType.Text,
    isPassword: Boolean = false,
    modifier: Modifier = Modifier.fillMaxWidth()
) {
    val outlineColor = LoginAccentColor
    val onSurface = LoginOnSurfaceColor
    val containerFocused = Color.Black.copy(alpha = 0.7f)
    val containerUnfocused = Color.Black.copy(alpha = 0.5f)

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, color = outlineColor.copy(alpha = 0.9f)) },
        leadingIcon = { Icon(icon, contentDescription = null, tint = outlineColor) },
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        singleLine = true,
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = containerFocused,
            unfocusedContainerColor = containerUnfocused,
            focusedTextColor = onSurface,
            unfocusedTextColor = onSurface.copy(alpha = 0.9f),
            cursorColor = outlineColor,
            focusedBorderColor = outlineColor,
            unfocusedBorderColor = onSurface.copy(alpha = 0.3f)
        )
    )
}

