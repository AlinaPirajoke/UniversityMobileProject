package com.example.university.viewModel.states

import androidx.compose.material.Colors
import com.example.university.theme.ColorScheme

data class MainActivityUiState(
    val isGoingToLogin: Boolean = false, // Отправляет на авторизацию
    val colorScheme: Colors = ColorScheme.PH.colors // Цветовая тема
)