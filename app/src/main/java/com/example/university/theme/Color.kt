package com.example.university.theme

import androidx.compose.material.Colors
import androidx.compose.material.lightColors
import androidx.compose.ui.graphics.Color

val PH = Color(0xFFFF9A00)
val PHLight = Color(0xFFFFAF36)
val PHDark = Color(0xFFCC7C00)

val pink = Color(0xFFFF7BCE)
val pinkLight = Color(0xFFFFADE1)
val pinkDark = Color(0xFFC93B94)

val error = Color(0xFFFF715E)
val gray = Color(0xFF494949)
val darkGray = Color(0xFF303030)

val PHColors = lightColors(
    primary = PH,
    primaryVariant = PHLight,
    onPrimary = Color.White,
    secondary = darkGray,
    secondaryVariant = gray,
    onSecondary = Color.White,
    error = error
)
val pinkColors = lightColors(
    primary = pink,
    primaryVariant = pinkLight,
    onPrimary = Color.White,
    secondary = darkGray,
    secondaryVariant = gray,
    onSecondary = Color.White,
    error = error
)


enum class ColorScheme(val colors: Colors) {
    PH(PHColors), pink(pinkColors),
}