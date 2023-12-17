package com.example.university.theme

import androidx.compose.material.Colors
import androidx.compose.material.lightColors
import androidx.compose.ui.graphics.Color

val PH = Color(0xFFFF9A00)
val PHLight = Color(0xFFFFAF36)
val PHDark = Color(0xFFCC7C00)

val pink = Color(0xFFFF7BCE)
val pinkLight = Color(0xFFFD9FDB)
val pinkDark = Color(0xFFC93B94)

val mint = Color(0xFF25AFA3)
val mintLight = Color(0xFF8EDFD7)
val mintDark = Color(0xFF4DB6AC)

val error = Color(0xFFFF715E)
val gray = Color(0xFF494949)
val mediumGray = Color(0xFF2B2B2B)
val darkGray = Color(0xFF303030)
val whiteGray = Color(0xFFFEFAFD)

val PHColors = lightColors(
    primary = PH,
    primaryVariant = PHLight,
    onPrimary = Color.White,
    secondary = darkGray,
    secondaryVariant = gray,
    onSecondary = Color.White,
    error = error,
    background = whiteGray,
    onBackground = mediumGray
)
val pinkColors = lightColors(
    primary = pink,
    primaryVariant = pinkLight,
    onPrimary = Color.White,
    secondary = darkGray,
    secondaryVariant = gray,
    onSecondary = Color.White,
    error = error,
    background = whiteGray,
    onBackground = mediumGray
)
val greenColors = lightColors(
    primary = mint,
    primaryVariant = mintLight,
    onPrimary = Color.White,
    secondary = darkGray,
    secondaryVariant = gray,
    onSecondary = Color.White,
    error = error,
    background = whiteGray,
    onBackground = mediumGray
)


enum class ColorScheme(val colors: Colors) {
    PH(PHColors), pink(pinkColors), green(greenColors)
}