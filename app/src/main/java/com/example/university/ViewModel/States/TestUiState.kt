package com.example.university.ViewModel.States

import androidx.compose.material.Colors
import com.example.university.theme.PHColors

data class TestUiState(
    val wordLabel: String = "Пизда хихихихи",
    val transcrLabel: String = "",
    val translLabel: String = "",
    val currentStage: Int = 1,
    val colorScheme: Colors = PHColors
)