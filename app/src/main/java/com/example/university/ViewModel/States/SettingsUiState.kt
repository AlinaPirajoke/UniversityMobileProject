package com.example.university.ViewModel.States

data class SettingsUiState(
    val isPasswordNeeded: Boolean = true,
    val currentColorScheme: Int = 0,
    val errorMessage: String = "",
)
