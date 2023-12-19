package com.example.university.theme

import androidx.compose.material.Colors
import androidx.compose.material.lightColors
import androidx.compose.ui.graphics.Color

val PH = Color(0xFFFF9A00)
val PHLight = Color(0xFFFFBB53)
val PHDark = Color(0xFFCC7C00)

val pink = Color(0xFFFF7BCE)
val pinkLight = Color(0xFFFD9FDB)
val pinkDark = Color(0xFFC93B94)

val mint = Color(0xFF29C2B5)
val mintLight = Color(0xFF8CECE4)
val mintDark = Color(0xFF4DB6AC)

val error = Color(0xFFFF715E)
val gray = Color(0xFF494949)
val lightGray = Color(0xFF707070)
val mediumGray = Color(0xFF1B1B1B)
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
    onSurface = mediumGray,
    onBackground = mediumGray,
    onError = lightGray // onError всё равно нигде не используется, я им пользуюсь как вспомогательным полем
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
    onSurface = mediumGray,
    onBackground = mediumGray,
    onError = lightGray
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
    onSurface = mediumGray,
    onBackground = mediumGray,
    onError = lightGray
)


enum class ColorScheme(val colors: Colors) {
    PH(PHColors), pink(pinkColors), mint(greenColors)
}