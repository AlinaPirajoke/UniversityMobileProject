package com.example.university.Theme

import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun KotobaCustomTheme(
    colorScheme: Colors,
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colors = colorScheme,
        typography = KotobaTypography,
        shapes = KotobaShapes,
        content = content
    )
}