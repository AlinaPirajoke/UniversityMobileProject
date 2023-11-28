package com.example.university.ViewModel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.university.ViewModel.States.LoginUiState
import com.example.university.Model.AppDB.AppDbManager
import com.example.university.Model.MySharedPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel(val db: AppDbManager, val msp: MySharedPreferences) : ViewModel() {
    val TAG = "LoginViewModel"

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    var enteredPass by mutableStateOf("")
        private set

    init {
        Log.d(TAG, "Создано")
        msp.session = false
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
        setHaveErrorMessage(true)

    }

    fun setHaveErrorMessage(condition: Boolean) {
        _uiState.update { state ->
            state.copy(haveErrorMessage = condition)
        }
    }

    fun setIsPassWrong(condition: Boolean) {
        _uiState.update { state ->
            state.copy(isFieldWrong = condition)
        }
    }

    suspend fun checkPassword() {
        Log.i(TAG, "Проверка пароля $enteredPass")

        val enteredPass = enteredPass
        if (msp.isPasswordNeeded)
            if (enteredPass.isEmpty()) {
                setErrorMessage("Введите пароль")
                setIsPassWrong(true)
                return
            }

        val passwords = db.getPasswords()
        if (enteredPass !in passwords.keys) {
            setErrorMessage("Пароль не верен")
            setIsPassWrong(true)
            return
        }

        passwords.get(enteredPass)?.let {
            msp.user = it
            msp.session = true
            sendToHomePage()
        }

    }
}