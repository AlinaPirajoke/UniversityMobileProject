package com.example.university.theme

import androidx.compose.material.lightColors
import androidx.compose.ui.graphics.Color

val PHColor = Color(0xFFFF9A00)
//val mainColor = Color(0xFFFF9A00)
val PHLightColor = Color(0xFFFFAF36)
val PHDarkColor = Color(0xFFCC7C00)
val errorColor = Color(0xFFFF715E)
val grayColor = Color(0xFF494949)
val darkGrayColor = Color(0xFF303030)

val PHColors = lightColors(
    primary = PHColor,
    primaryVariant = PHLightColor,
    onPrimary = Color.White,
    secondary = darkGrayColor,
    secondaryVariant = grayColor,
    onSecondary = Color.White,
    error = errorColor
)

enum class ColorScheme(val id: Int){
    PH(0),
}