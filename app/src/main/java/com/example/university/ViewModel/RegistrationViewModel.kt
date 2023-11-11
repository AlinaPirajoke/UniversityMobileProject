package com.example.university.ViewModel

import android.content.SharedPreferences
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.university.Model.DBManager
import com.example.university.ViewModel.States.LoginUiState
import com.example.university.ViewModel.States.RegistrationUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegistrationViewModel(val db: DBManager, val sharedPreferences: SharedPreferences) :
    ViewModel() {
    val TAG = "RegistrationViewModel"

    private val _uiState = MutableStateFlow(RegistrationUiState())
    val uiState: StateFlow<RegistrationUiState> = _uiState.asStateFlow()

    var enteredPass1 by mutableStateOf("")
        private set
    var enteredPass2 by mutableStateOf("")
        private set

    init {
        Log.d(TAG, "Создано")
    }

    fun setPass1Value(pass: String) {
        enteredPass1 = pass
        // Здесь лучше запустить в корутине
        viewModelScope.launch {
            if (isValidSymbols(pass))
                setIsField1Wrong(false)
            else {
                setIsField1Wrong(true)
                setErrorMessage("Вы вводите некорректные символы!")
            }
        }
    }

    fun setPass2Value(pass: String) {
        enteredPass2 = pass
        setIsField2Wrong(false)
    }

    fun setIsField1Wrong(condition: Boolean) {
        _uiState.update { state ->
            state.copy(isField1Wrong = condition)
        }
    }

    fun setIsField2Wrong(condition: Boolean) {
        _uiState.update { state ->
            state.copy(isField2Wrong = condition)
        }
    }

    fun sendToHomePage(condition: Boolean = true) {
        _uiState.update { state ->
            state.copy(isGoingToMain = condition)
        }
    }

    fun sendToLoginPage(condition: Boolean = true) {
        _uiState.update { state ->
            state.copy(isGoingToLogin = condition)
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

    fun addUser() {
        val pass1 = enteredPass1
        val pass2 = enteredPass2
        if (pass1 != pass2) {
            setErrorMessage("Пароли не совпадают")
            setIsField2Wrong(true)
            return
        }
        if (!(isValidSymbols(pass1) == true && isValidLength(pass1))) {
            setErrorMessage("Использованы недопустимые символы")
            setIsField1Wrong(true)
            setIsField2Wrong(true)
            return
        }
        if ((pass1 in db.getPasswords().keys) != false) {
            setErrorMessage("Пароль не подходит, придумайте другой")
            setIsField1Wrong(true)
            setIsField2Wrong(true)
            return
        }
        viewModelScope.launch {
            db.insertPassword(pass1.toString())
            val newId = db.getPasswords().get(pass1)
            sharedPreferences.edit().putBoolean("session", true).apply()
            newId?.let {
                sharedPreferences.edit().putInt("user", it).apply()
            }
            sendToHomePage()
        }
    }

    private fun isValidSymbols(password: String): Boolean {
        val passwordRegex = Regex("^[a-zA-Z0-9]*$")
        Log.d(
            TAG, "Проверка пароля: " +
                    "валидность символов: ${password.matches(passwordRegex)}, " +
                    "длинна: ${password.length}"
        )
        return password.matches(passwordRegex)
    }

    private fun isValidLength(password: String): Boolean {
        return password.length >= 6
    }
}