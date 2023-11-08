package com.example.university.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun KotobaCustomTheme(
    schemeId: Int,
    content: @Composable () -> Unit,
){
    val colorScheme = when(schemeId){
        0 -> PHColors
        else -> PHColors
    }
    MaterialTheme(
        colors = colorScheme,
        typography = KotobaTypography,
        shapes = KotobaShapes,
        content = content
    )
}