package com.nazeeran.quillvibe.notes.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.nazeeran.quillvibe.notes.ui.theme.CardTintPalette

@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    colorIndex: Int = 0,
    shape: Shape = RoundedCornerShape(20.dp),
    borderWidth: Dp = 1.dp,
    onClick: (() -> Unit)? = null,
    content: @Composable BoxScope.() -> Unit
) {
    val isDark = isSystemInDarkTheme()
    val baseTint = CardTintPalette.getTint(colorIndex, isDark)
    
    val borderColor = if (colorIndex == 0) {
        MaterialTheme.colorScheme.outlineVariant.copy(alpha = if (isDark) 0.35f else 0.6f)
    } else {
        if (isDark) {
            Color.White.copy(alpha = 0.12f)
        } else {
            Color.Black.copy(alpha = 0.06f)
        }
    }

    if (onClick != null) {
        Card(
            onClick = onClick,
            modifier = modifier.clip(shape),
            shape = shape,
            colors = CardDefaults.cardColors(
                containerColor = baseTint
            ),
            border = BorderStroke(borderWidth, borderColor)
        ) {
            Box { content() }
        }
    } else {
        Card(
            modifier = modifier.clip(shape),
            shape = shape,
            colors = CardDefaults.cardColors(
                containerColor = baseTint
            ),
            border = BorderStroke(borderWidth, borderColor)
        ) {
            Box { content() }
        }
    }
}
