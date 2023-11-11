package com.example.university.ViewModel

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.university.Model.DBManager
import com.example.university.ViewModel.States.MainUiState
import com.example.university.ViewModel.States.RegistrationUiState
import com.example.university.usefull_stuff.getTodayDate
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel(val db: DBManager, val sharedPreferences: SharedPreferences) : ViewModel() {
    val TAG = "MainViewModel"
    val user = sharedPreferences.getInt("user", 1)

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    init {
        var session = sharedPreferences.getBoolean("session", false)
        val needPass = sharedPreferences.getBoolean("isPasswordNeeded", false)

        if (!needPass)
            session = true
        if (!session)
            sendToLogin()
        getStatistic()
        checkTodayWords()
    }

    fun sendToLogin(condition: Boolean = true) {
        _uiState.update { state ->
            state.copy(isGoingToLogin = condition)
        }
    }

    fun setStatLearned(count: Int) {
        _uiState.update { state ->
            state.copy(statLearned = count)
        }
    }

    fun setStatLearning(count: Int) {
        _uiState.update { state ->
            state.copy(statLearning = count)
        }
    }

    fun setStatAverage(count: Int) {
        _uiState.update { state ->
            state.copy(statAverage = count)
        }
    }

    fun setTest(count: Int) {
        _uiState.update { state ->
            state.copy(todayTest = count)
        }
    }

    fun setLearn(count: Int) {
        _uiState.update { state ->
            state.copy(todayLearn = count)
        }
    }

    fun checkTodayWords() {
        viewModelScope.launch {
            setTest(db.getSizeFromDate(getTodayDate(), user)!!)
            var learnCount = db.getTodayLearnedCount(
                getTodayDate(),
                user
            )!! - sharedPreferences.getInt("studiedPerDay", 10)
            if (learnCount < 0)
                learnCount = 0
            setLearn(learnCount)
        }
    }

    fun getStatistic() {
        viewModelScope.launch {
            setStatLearned(db.getLearnedCount())
            setStatLearning(db.getLerningCount())
            setStatAverage(db.getAverage())
        }
    }
}