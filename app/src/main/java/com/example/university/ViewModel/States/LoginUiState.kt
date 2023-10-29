package com.example.university.ViewModel.States

data class LoginUiState (
    val isGoingToMain: Boolean = false, // Отправляет на Главную
    val isGoingToRegister: Boolean = false, // Отправляет на Регистрацию
    val isFieldWrong: Boolean = false, // Устанавливает ошибку ввода
    val errorMessage: String = "" // Сообщение об ошибке, выводимое тостом (Куда это вообще запихать?)
)