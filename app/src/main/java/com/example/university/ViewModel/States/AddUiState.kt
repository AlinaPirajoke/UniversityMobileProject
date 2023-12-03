package com.example.university.ViewModel.States

import androidx.compose.material.Colors
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.example.university.theme.ColorScheme

data class AddUiState(
    val isGoingToMain: Boolean = false, // Отправляет на Главную
    val isWordFieldWrong: Boolean = false, // Устанавливает ошибку в поле для иностранного слова
    val isTranslFieldWrong: Boolean = false, // Устанавливает ошибку в поле для перевода
    val isLvlFieldWrong: Boolean = false, // Устанавливает ошибку в поле для уровня
    val errorMessage: String = "", // Сообщение об ошибке
    val wordValue: String = "", // Слово для изучения
    val transcrValue: String = "", // Транскрипция
    val lvlValue: String = "", // начальный уровень слова (период повторения в днях)
    val translValues: SnapshotStateList<String> = mutableStateListOf(("")), // Список переводов
    val colorScheme: Colors = ColorScheme.PH.colors, // Цветовая тема
    val isTranslating: Boolean = false, // Находится ли слово в процессе перевода
    val isTranslationError: Boolean = false, // Произошла ли ошибка в процессе перевода
)
