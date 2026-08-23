package com.example.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// TurfGo Premium Pink Theme Palette
val PinkPrimary = Color(0xFFE91E63)
val PinkDark = Color(0xFFAD1457)
val PinkLight = Color(0xFFFCE4EC)
val PinkSoft = Color(0xFFFFF5F8)
val PinkSecondary = Color(0xFFFF4081)
val PinkAccent = Color(0xFFFF80AB)
val PinkDeep = Color(0xFF880E4F)

// Neutrals & Surface Colors
val White = Color(0xFFFFFFFF)
val DarkText = Color(0xFF212121)
val TextMuted = Color(0xFF616161)
val TextSecondary = Color(0xFF757575)
val TextLight = Color(0xFF9E9E9E)

val SurfaceLight = Color(0xFFFFFFFF)
val SurfaceSoftPink = Color(0xFFFFF8FA)
val SurfaceCard = Color(0xFFFFFFFF)
val BorderLight = Color(0xFFF8BBD0)
val BorderSubtle = Color(0xFFEEEEEE)

// Status Colors
val StatusAvailable = Color(0xFFE91E63)
val StatusAvailableBg = Color(0xFFFCE4EC)
val StatusBooked = Color(0xFF9E9E9E)
val StatusBookedBg = Color(0xFFF5F5F5)
val StatusSelected = Color(0xFFE91E63)
val StatusSelectedBg = Color(0xFFAD1457)
val StatusCancelled = Color(0xFFD32F2F)
val StatusCancelledBg = Color(0xFFFFEBEE)
val StatusConfirmed = Color(0xFF2E7D32)
val StatusConfirmedBg = Color(0xFFE8F5E9)

// Sports & Pitch Accents
val TurfGreen = Color(0xFF1B5E20)
val TurfGreenLight = Color(0xFF4CAF50)
val GoldenRating = Color(0xFFFFB300)
val DarkPitch = Color(0xFF1A1A24)

// Gradient Brushes
val PinkHeroGradient = Brush.horizontalGradient(
    colors = listOf(Color(0xFFE91E63), Color(0xFFFF4081))
)

val PinkDarkGradient = Brush.verticalGradient(
    colors = listOf(Color(0xFFAD1457), Color(0xFFE91E63))
)

val PinkCardGradient = Brush.linearGradient(
    colors = listOf(Color(0xFFFFF0F5), Color(0xFFFFFFFF))
)

val PinkRadialGradient = Brush.radialGradient(
    colors = listOf(Color(0xFFFF4081), Color(0xFFAD1457))
)
