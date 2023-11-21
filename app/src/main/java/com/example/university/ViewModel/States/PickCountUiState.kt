package com.example.university.ViewModel.States

import androidx.compose.material.Colors
import com.example.university.Theme.PHColors

data class PickQuantityUiState (
    val wordsQuantity: Int = 1,
    val pickedQuantity: Int = 1,
    val colorScheme: Colors = PHColors,
    val isRememberPresent: Boolean = false,
    val pickedWords: String = "",
)