package com.example.university.ViewModel.States

import com.example.university.UsefullStuff.Word

data class RememberUiState(
    val currentWord: Word = Word(0,"", "", listOf(), 0),
    val isTranscrShowing: Boolean = false,
    val currentStage: Int = 1,
    val isExitAlertDialogShowing: Boolean = false,
    val isFinishAlertDialogShowing: Boolean = false,
    val isItLastWord: Boolean = false,
)