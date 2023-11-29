package com.example.university.ViewModel.States

import androidx.compose.material.Colors
import com.example.university.theme.PHColors

data class TestUiState(
    val wordLabel: String = "хихихихи",
    val transcrLabel: String = "",
    val translLabel: String = "",
    val currentStage: Int = 1,
    val colorScheme: Colors = PHColors,
    val isExitAlertDialogShowing: Boolean = false,
    val isFinishAlertDialogShowing: Boolean = false,
)