package com.nazeeran.quillvibe.notes.ui.theme

import androidx.compose.ui.graphics.Color

// Primary Branding & Accent Tones
val BrandTealPrimary = Color(0xFF0F766E)
val BrandTealLight = Color(0xFF14B8A6)
val BrandTealDark = Color(0xFF2DD4BF)
val BrandSecondary = Color(0xFF0284C7)

// Light Theme Tokens
val LightBackground = Color(0xFFF8FAFC)
val LightSurface = Color(0xFFFFFFFF)
val LightSurfaceVariant = Color(0xFFF1F5F9)
val LightOutline = Color(0xFFE2E8F0)
val LightOnSurface = Color(0xFF0F172A)
val LightOnSurfaceVariant = Color(0xFF64748B)

// Pure Dark & OLED Dark Tokens
val DarkBackground = Color(0xFF000000)      // True OLED Pure Black
val DarkSurface = Color(0xFF111418)         // Elevated surface
val DarkSurfaceVariant = Color(0xFF1A202C)  // Card background
val DarkOutline = Color(0xFF2D3748)
val DarkOnSurface = Color(0xFFF8FAFC)
val DarkOnSurfaceVariant = Color(0xFF94A3B8)

// 6 Muted Pastel Card Tint Palettes (0: Default Surface, 1..6: Pastel Tints)
object CardTintPalette {
    val lightColors = listOf(
        Color(0xFFFFFFFF), // 0: Default Clean White
        Color(0xFFE8F5E9), // 1: Soft Mint / Sage
        Color(0xFFF3E5F5), // 2: Pale Lavender / Iris
        Color(0xFFFFF3E0), // 3: Warm Peach / Amber
        Color(0xFFE1F5FE), // 4: Serene Sky / Ocean
        Color(0xFFFCE4EC), // 5: Soft Rose / Blush
        Color(0xFFFFFDE7)  // 6: Muted Honey / Buttercup
    )

    val darkColors = listOf(
        Color(0xFF161B22), // 0: Default OLED Elevated Card
        Color(0xFF12231B), // 1: Dark Mint
        Color(0xFF20152B), // 2: Dark Lavender
        Color(0xFF2A1910), // 3: Dark Peach
        Color(0xFF11222E), // 4: Dark Sky
        Color(0xFF2B131E), // 5: Dark Rose
        Color(0xFF282512)  // 6: Dark Honey
    )

    val tintNames = listOf(
        "Default",
        "Sage",
        "Lavender",
        "Peach",
        "Sky",
        "Rose",
        "Honey"
    )

    fun getTint(index: Int, isDark: Boolean): Color {
        val safeIndex = index.coerceIn(0, 6)
        return if (isDark) darkColors[safeIndex] else lightColors[safeIndex]
    }
}
