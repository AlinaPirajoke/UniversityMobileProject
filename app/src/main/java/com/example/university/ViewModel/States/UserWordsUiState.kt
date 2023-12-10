package com.example.university.ViewModel.States

import com.example.university.UsefullStuff.Word

data class UserWordsUiState(
    val words: ArrayList<Word> = arrayListOf(), // Список всех слов
    val pickedWordNo: Int = -1, // Номер выбранного слоа (-1 - слово не выбрано)
    val isAlertShowing: Boolean = false // Показывает предупреждающее сообщение
)