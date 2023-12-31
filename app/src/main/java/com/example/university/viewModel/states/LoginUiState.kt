package com.example.university.viewModel.states

data class LoginUiState(
    val isGoingToMain: Boolean = false, // Отправляет на Главную
    val isGoingToRegister: Boolean = false, // Отправляет на Регистрацию
    val isFieldWrong: Boolean = false, // Устанавливает ошибку ввода
    val errorMessage: String = "", // Сообщение об ошибке, выводимое тостом (Куда это вообще запихать?)
    val haveErrorMessage: Boolean = false, // Флаг наличия сообщения об ошибке
)