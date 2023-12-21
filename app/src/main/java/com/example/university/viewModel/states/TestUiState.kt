package com.example.university.viewModel.states

data class TestUiState(
    val wordLabel: String = "хихихихи",
    val transcrLabel: String = "",
    val translLabel: String = "",
    val currentStage: Int = 1,
    val isExitAlertDialogShowing: Boolean = false,
    val isFinishAlertDialogShowing: Boolean = false,
)