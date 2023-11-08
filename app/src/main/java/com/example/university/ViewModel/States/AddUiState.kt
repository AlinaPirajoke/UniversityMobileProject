package com.example.university.ViewModel.States

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList

data class AddUiState (
    val isGoingToMain: Boolean = false, // Отправляет на Главную
    val isWordFieldWrong: Boolean = false, // Устанавливает ошибку в поле для иностранного слова
    val isTranslFieldWrong: Boolean = false, // Устанавливает ошибку в поле для перевода
    val isLvlFieldWrong: Boolean = false, // Устанавливает ошибку в поле для уровня
    val errorMessage: String = "", // Сообщение об ошибке
    val wordValue: String = "",
    val transcrValue: String = "",
    val lvlValue: String = "",
    val translValues: SnapshotStateList<String> =  mutableStateListOf(("")),
    //val translCount: Int = 1
)
