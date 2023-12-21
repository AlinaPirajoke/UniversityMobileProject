package com.example.university.viewModel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.university.model.appDB.AppDbManager
import com.example.university.model.MySharedPreferences
import com.example.university.viewModel.states.RegistrationUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegistrationViewModel(val db: AppDbManager, val msp: MySharedPreferences) :
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
        setHaveErrorMessage(true)
    }

    fun setHaveErrorMessage(condition: Boolean) {
        _uiState.update { state ->
            state.copy(haveErrorMessage = condition)
        }
    }

    fun addUser() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val pass1 = enteredPass1
                val pass2 = enteredPass2
                if (pass1 != pass2) {
                    setErrorMessage("Пароли не совпадают")
                    setIsField2Wrong(true)
                    return@withContext
                }
                if (!isValidSymbols(pass1)) {
                    setErrorMessage("Использованы недопустимые символы")
                    setIsField1Wrong(true)
                    setIsField2Wrong(true)
                    return@withContext
                }
                if (!isValidLength(pass1)) {
                    setErrorMessage("Пароль слишком короткий")
                    setIsField1Wrong(true)
                    setIsField2Wrong(true)
                    return@withContext
                }
                if ((pass1 in db.getPasswords().keys) != false) {
                    setErrorMessage("Пароль не подходит, придумайте другой")
                    setIsField1Wrong(true)
                    setIsField2Wrong(true)
                    return@withContext
                }
                viewModelScope.launch {
                    db.insertPassword(pass1.toString())
                    val newId = db.getPasswords().get(pass1)
                    msp.session = true
                    newId?.let {
                        msp.user = it
                    }
                    sendToHomePage()
                }
            }
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