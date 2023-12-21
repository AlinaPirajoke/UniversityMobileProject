package com.example.university.viewModel.states

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList

data class EditUiState(
    val isGoingToExit: Boolean = false, // Отправляет на Главную
    val isWordFieldWrong: Boolean = false, // Устанавливает ошибку в поле для иностранного слова
    val isTranslFieldWrong: Boolean = false, // Устанавливает ошибку в поле для перевода
//    val isLvlFieldWrong: Boolean = false, // Устанавливает ошибку в поле для уровня
    val errorMessage: String = "", // Сообщение об ошибке
    val wordValue: String = "", // Слово для изучения
    val transcrValue: String = "", // Транскрипция
//    val lvlValue: String = "", // начальный уровень слова (период повторения в днях)
    val translValues: SnapshotStateList<String> = mutableStateListOf(("")), // Список переводов
)