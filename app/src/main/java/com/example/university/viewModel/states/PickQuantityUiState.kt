package com.example.university.viewModel.states

import androidx.compose.material.Colors
import com.example.university.theme.PHColors

data class PickQuantityUiState(
    val wordsQuantity: Int = 1,
    val pickedQuantitySlider: Float = 1f,
    val pickedQuantityReal: Int = 1,
    val colorScheme: Colors = PHColors,
    val isRememberPresent: Boolean = false,
    val pickedWords: String = ""
)