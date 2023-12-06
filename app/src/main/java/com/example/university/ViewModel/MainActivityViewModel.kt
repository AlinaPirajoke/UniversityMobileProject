package com.example.university.ViewModel

import androidx.lifecycle.ViewModel
import com.example.university.Model.AppDB.AppDbManager
import com.example.university.Model.MySharedPreferences
import com.example.university.UsefullStuff.getTodayDate
import com.example.university.ViewModel.States.MainActivityUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MainActivityViewModel(val db: AppDbManager, val msp: MySharedPreferences) : ViewModel() {
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

        if(msp.lastOpenedAppDate > getTodayDate())
            dailyUpdates()
    }

    fun dailyUpdates(){
        msp.todayStudiedQuantity = 0
        db.dailyDateUpdate()
        msp.lastOpenedAppDate = getTodayDate()
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