package com.example.university.ViewModel.States

import androidx.compose.material.Colors
import com.example.university.theme.PHColors

data class PickQuantityUiState(
    val wordsQuantity: Int = 1,
    val pickedQuantity: Float = 1f,
    val colorScheme: Colors = PHColors,
    val isRememberPresent: Boolean = false,
    val pickedWords: String = ""
)