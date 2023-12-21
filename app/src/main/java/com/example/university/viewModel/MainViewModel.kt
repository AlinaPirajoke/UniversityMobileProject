package com.example.university.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.university.model.appDB.AppDbManager
import com.example.university.model.MySharedPreferences
import com.example.university.usefullStuff.getDaysBeforeToday
import com.example.university.usefullStuff.getTodayDate
import com.example.university.viewModel.states.MainUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(val db: AppDbManager, val msp: MySharedPreferences) : ViewModel() {
    val TAG = "MainViewModel"
    val user = msp.user

    private val _uiState = MutableStateFlow(MainUiState(colorScheme = msp.getColorScheme(), isPasswordNeeded = msp.isPasswordNeeded))
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                //db.logAllWords()
                getStatistic()
                checkTodayWords()
            }
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

    suspend fun setStatAverage(number: Int) {
        val period = getDaysBeforeToday(msp.firstAppAccessDate) + 1
        val quantity = String.format("%.2f", number.toDouble().div(period))
        _uiState.update { state ->
            state.copy(statAverage = quantity)
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
            withContext(Dispatchers.IO) {
                setTest(db.getQuantityFromDate(getTodayDate(), user)!!)
                var learnQuantity = msp.studyQuantityPerDay - msp.todayStudiedQuantity
                if (learnQuantity < 0)
                    learnQuantity = 0
                setLearn(learnQuantity)
            }
        }
    }

    suspend fun getStatistic() {
        viewModelScope.launch {
            setStatLearned(db.getLearnedCount())
            setStatLearning(db.getLearningCount())
            setStatAverage(db.getAllWordsQuantity())
        }
    }

    fun updateColorScheme() {
        _uiState.update { state ->
            state.copy(colorScheme = msp.getColorScheme())
        }
    }
}