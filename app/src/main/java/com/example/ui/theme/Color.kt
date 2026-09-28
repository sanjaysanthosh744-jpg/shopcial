package com.example.ui.theme

import androidx.compose.ui.graphics.Color

// Shopcial Light Palette (Warm Ivory + Olive + Burnt Sienna)
val LightBackground = Color(0xFFF5F1E8) // Warm Ivory
val LightSurface = Color(0xFFFFFDF8)    // Soft Cream
val LightPrimary = Color(0xFF596044)    // Deep Olive
val LightPrimaryPressed = Color(0xFF414832) // Dark Olive
val LightAccent = Color(0xFFB8663F)     // Burnt Sienna
val LightTextMain = Color(0xFF29261F)   // Espresso
val LightTextSecondary = Color(0xFF746E64) // Warm Gray
val LightBorder = Color(0xFFDDD5C7)     // Sand
val LightDivider = Color(0xFFE9E3D8)    // Light Sand
val LightSuccess = Color(0xFF526B4A)    // Forest
val LightWarning = Color(0xFFA8873D)    // Mustard
val LightDanger = Color(0xFFA65345)     // Brick

// Shopcial Dark Palette (Deep Espresso + Dark Cocoa + Soft Olive + Terracotta)
val DarkBackground = Color(0xFF171613)  // Deep Espresso
val DarkSurface = Color(0xFF211F1A)     // Dark Cocoa
val DarkElevatedSurface = Color(0xFF29271F)
val DarkPrimary = Color(0xFFA5AE82)     // Soft Olive
val DarkPrimaryPressed = Color(0xFF858E67) // Muted Olive
val DarkAccent = Color(0xFFC87954)      // Terracotta
val DarkTextMain = Color(0xFFF2EEE5)    // Warm White
val DarkTextSecondary = Color(0xFFB5AEA1)
val DarkBorder = Color(0xFF39362E)
val DarkDivider = Color(0xFF2D2B25)
val DarkSuccess = Color(0xFF82996E)     // Sage Green
val DarkWarning = Color(0xFFC1A15A)     // Muted Gold
val DarkDanger = Color(0xFFC06B5D)      // Dusty Brick

// Brand Semantic aliases
val OlivePrimary = LightPrimary
val BurntSiennaAccent = LightAccent
val ForestGreen = LightSuccess
val MustardYellow = LightWarning
val BrickRed = LightDanger

// Compatibility aliases mapped to user's warm theme
val CoralPrimary = LightAccent
val CoralPrimaryDark = LightPrimaryPressed
val VioletSecondary = LightPrimary
val AmberAccent = LightWarning
