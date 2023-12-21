package com.example.university.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.university.model.appDB.AppDbManager
import com.example.university.model.MySharedPreferences
import com.example.university.model.wordsDB.WordsDbManager
import com.example.university.usefullStuff.getTodayDate
import com.example.university.viewModel.states.MainActivityUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivityViewModel(
    val adb: AppDbManager,
    val wdb: WordsDbManager,
    val msp: MySharedPreferences
) : ViewModel() {
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
        //onFirstAccess()
        Log.e(TAG, "${msp.lastOpenedAppDate}, ${getTodayDate()}")
        if (msp.lastOpenedAppDate < getTodayDate())
            dailyUpdates()
    }

    private fun dailyUpdates() {
        Log.i(TAG, "Проводится ежедневное обновление")
        viewModelScope.launch{
            withContext(Dispatchers.IO) {

                msp.todayStudiedQuantity = 0
                adb.dailyDateUpdate()
                msp.lastOpenedAppDate = getTodayDate()
            }
        }
    }

    fun onFirstAccess() {
        Log.i(TAG, "Первый заход в приложение")
        viewModelScope.launch{
            withContext(Dispatchers.IO) {
                msp.firstAppAccessDate = getTodayDate()
                msp.lastOpenedAppDate = getTodayDate()
                adb.fillWordsLibrary(wdb.getAllWords())
            }
        }

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