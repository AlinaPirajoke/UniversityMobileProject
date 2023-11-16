package com.example.university.ViewModel.States

import androidx.compose.material.Colors
import com.example.university.theme.ColorScheme

data class MainUiState(
    val todayTest: Int = 0, // количество слов для теста на сегодня
    val todayLearn: Int = 0, // оставшееся количество слов для изучения на сегодня
    val statLearned: Int = 0,  // количество изученных слов
    val statLearning: Int = 0, // количество изучающихся слов
    val statAverage: Int = 0, // Среднее количество изучаемых слов в день
    val colorScheme: Colors = ColorScheme.PH.colors // Цветовая тема
)