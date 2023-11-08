package com.example.university.ViewModel.States

import androidx.lifecycle.MutableLiveData

data class MainUiState (
    val isGoingToLogin: Boolean = false, // Отправляет на Главную
    val todayTest: Int = 0, // количество слов для теста на сегодня
    val todayLearn: Int = 0, // оставшееся количество слов для изучения на сегодня
    val statLearned: Int = 0,  // количество изученных слов
    val statLearning: Int = 0, // количество изучающихся слов
    val statAverage: Int = 0, // Среднее количество изучаемых слов в день
)