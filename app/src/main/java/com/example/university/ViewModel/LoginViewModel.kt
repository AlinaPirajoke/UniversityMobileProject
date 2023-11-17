package com.example.university.ViewModel

import android.content.SharedPreferences
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.university.ViewModel.States.LoginUiState
import com.example.university.Model.DBManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(val db: DBManager, val sharedPreferences: SharedPreferences) : ViewModel() {
    val TAG = "LoginViewModel"

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    var enteredPass by mutableStateOf("")
        private set

    init {
        Log.d(TAG, "Создано")
        sharedPreferences.edit().putBoolean("session", false).apply()
    }

    fun editUserEnter(enteredPass: String) {
        this.enteredPass = enteredPass
        setIsPassWrong(false)

    }

    fun sendToHomePage(condition: Boolean = true) {
        _uiState.update { state ->
            state.copy(isGoingToMain = condition)
        }
    }

    fun sendToRegisterPage(condition: Boolean = true) {
        _uiState.update { state ->
            state.copy(isGoingToRegister = condition)
        }
    }

    fun setErrorMessage(text: String) {
        _uiState.update { state ->
            state.copy(errorMessage = text)
        }
        if (!text.isEmpty())
            Log.w(TAG, "Ошибка ввода: ${uiState.value.errorMessage}")
    }

    fun clearErrorMessage() {
        setErrorMessage("")
    }

    fun setIsPassWrong(condition: Boolean) {
        _uiState.update { state ->
            state.copy(isFieldWrong = condition)
        }
    }

    fun checkPassword() {
        Log.d(TAG, "Проверка пароля $enteredPass")
        viewModelScope.launch {
            val enteredPass = enteredPass
            if (!sharedPreferences.getBoolean("isPasswordNeeded", true))
                if (enteredPass?.isEmpty() == true) {
                    setErrorMessage("Введите пароль")
                    setIsPassWrong(true)
                    return@launch
                }
            val passwords = db.getPasswords()
            if (!(enteredPass in passwords.keys)) {
                setErrorMessage("Пароль не верен")
                setIsPassWrong(true)
                return@launch
            }

            passwords.get(enteredPass)?.let {
                sharedPreferences.edit().putInt("user", it).apply()
                sharedPreferences.edit().putBoolean("session", true).apply()
                sendToHomePage()
            }
        }
    }
}