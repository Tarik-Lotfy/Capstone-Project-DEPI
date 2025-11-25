package com.example.moviestime.ui.components.login

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun CinematicCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme
    val cardShape = RoundedCornerShape(32.dp)
    val borderBrush = Brush.linearGradient(
        listOf(
            Color(0xFFB98046).copy(alpha = 0.45f),
            Color(0xFF4C3326).copy(alpha = 0.35f)
        )
    )
    Card(
        modifier = modifier
            .wrapContentHeight()
            .shadow(
                elevation = 24.dp,
                shape = cardShape,
                clip = false
            ),
        shape = cardShape,
        border = BorderStroke(1.dp, borderBrush),
        colors = CardDefaults.cardColors(
            containerColor = colorScheme.surface.copy(alpha = 0.93f)
        )
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            content = content
        )
    }
}

