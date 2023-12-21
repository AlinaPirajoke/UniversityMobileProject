package com.example.university.viewModel.states

import com.example.university.usefullStuff.Word

data class PickWordUiState (
    val pickedQuantity: Int = 0, // Количество выбранных слов
    val remain: Int = 10, // Количество оставшихся слов
    val words: ArrayList<Word> = arrayListOf(), // Список всех слов
    val pickedWords: MutableList<Int> = mutableListOf(), // Номера выбранных слов
    val topText: String = "Выберите слова для изучения", // Текст вверху (верхний (он на верху (выше остального) ) )
    val isGoingToMain: Boolean = false // Отправление на главный
)