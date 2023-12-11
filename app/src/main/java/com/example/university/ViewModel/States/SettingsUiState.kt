package com.example.university.ViewModel.States

import androidx.compose.material.Colors
import com.example.university.theme.ColorScheme

data class SettingsUiState(
    val isPasswordNeeded: Boolean = true, // Настройка: требуется ли пароль при входе
    val currentColorScheme: Int = 0, // Номер выбранной цветовой схемы
    val errorMessage: String = "", // Сообщение об ошибке
    val haveErrorMessage: Boolean = false, // Флаг наличия сообщения об ошибке
    val colorScheme: Colors = ColorScheme.PH.colors, // Цветовая схемы
    val isRememberPresent: Boolean = false, // Настройка: предлагается ли пользователю повторить слова
)
