package com.example.university.viewModel.states

import com.example.university.usefullStuff.Word

data class RememberUiState(
    val currentWord: Word = Word(0,"", "", listOf(), 0),
    val isTranscrShowing: Boolean = false,
    val currentStage: Int = 1,
    val isExitAlertDialogShowing: Boolean = false,
    val isFinishAlertDialogShowing: Boolean = false,
    val isItLastWord: Boolean = false,
    val wordIndex: Int = 1,
    val totalWords: Int = 1
)