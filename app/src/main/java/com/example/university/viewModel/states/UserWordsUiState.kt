package com.example.university.viewModel.states

import com.example.university.usefullStuff.Word

data class UserWordsUiState(
    val words: ArrayList<Word> = arrayListOf(), // Список всех слов
    val pickedWordNo: Int = -1, // Номер выбранного слоа (-1 - слово не выбрано)
    val isAlertShowing: Boolean = false // Показывает предупреждающее сообщение
)