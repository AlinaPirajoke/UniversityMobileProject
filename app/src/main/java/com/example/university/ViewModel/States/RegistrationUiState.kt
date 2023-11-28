package com.example.university.ViewModel.States

data class RegistrationUiState(
    val isGoingToMain: Boolean = false, // Отправляет на Главную
    val isGoingToLogin: Boolean = false, // Отправляет на Логин
    val isField1Wrong: Boolean = false, // Устанавливает ошибку ввода поля 1
    val isField2Wrong: Boolean = false, // Устанавливает ошибку ввода поля 2
    val errorMessage: String = "", // Сообщение об ошибке, выводимое тостом (Куда это вообще запихать?)
    val haveErrorMessage: Boolean = false, // Флаг наличия сообщения об ошибке
)