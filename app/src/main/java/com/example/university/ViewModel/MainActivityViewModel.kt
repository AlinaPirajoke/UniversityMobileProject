package com.example.university.ViewModel

import androidx.lifecycle.ViewModel
import com.example.university.Model.DBManager
import com.example.university.Model.MySharedPreferences
import com.example.university.ViewModel.States.MainActivityUiState
import com.example.university.ViewModel.States.MainUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MainActivityViewModel(val db: DBManager, val msp: MySharedPreferences) : ViewModel() {
    val TAG = "MainActivityViewModel"
    val user = msp.user

    private val _uiState = MutableStateFlow(MainActivityUiState(colorScheme = msp.getColorScheme()))
    val uiState: StateFlow<MainActivityUiState> = _uiState.asStateFlow()

    init {
        var session = msp.session
        val needPass = msp.isPasswordNeeded

        if (!needPass)
            session = true
        if (!session)
            sendToLogin()
    }

    fun sendToLogin(condition: Boolean = true) {
        _uiState.update { state ->
            state.copy(isGoingToLogin = condition)
        }
    }

    fun updateColorScheme() {
        _uiState.update { state ->
            state.copy(colorScheme = msp.getColorScheme())
        }
    }
}