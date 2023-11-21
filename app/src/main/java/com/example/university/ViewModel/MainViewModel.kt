package com.example.university.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.university.Model.AppDB.AppDbManager
import com.example.university.Model.MySharedPreferences
import com.example.university.ViewModel.States.MainUiState
import com.example.university.UsefullStuff.getTodayDate
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel(val db: AppDbManager, val msp: MySharedPreferences) : ViewModel() {
    val TAG = "MainViewModel"
    val user = msp.user

    private val _uiState = MutableStateFlow(MainUiState(colorScheme = msp.getColorScheme()))
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            getStatistic()
            checkTodayWords()
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

    suspend fun checkTodayWords() {
        viewModelScope.launch {
            if (msp.lastOpenedAppDate <= getTodayDate())
                db.dailyDateUpdate()

            setTest(db.getQuantityFromDate(getTodayDate(), user)!!)
            var learnCount = db.getTodayLearnedCount(getTodayDate(), user)!!
            learnCount = msp.studyQuantityPerDay - learnCount
            if (learnCount < 0)
                learnCount = 0
            setLearn(learnCount)
        }
    }

    suspend fun getStatistic() {
        viewModelScope.launch {
            setStatLearned(db.getLearnedCount())
            setStatLearning(db.getLearningCount())
            setStatAverage(db.getAverage())
        }
    }

    fun updateColorScheme() {
        _uiState.update { state ->
            state.copy(colorScheme = msp.getColorScheme())
        }
    }
}