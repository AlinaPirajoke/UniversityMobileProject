package com.example.university.ViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.university.Model.AppDB.AppDbManager
import com.example.university.Model.MySharedPreferences
import com.example.university.UsefullStuff.Word
import com.example.university.ViewModel.States.PickQuantityUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PickQuantityViewModel(val db: AppDbManager, val msp: MySharedPreferences) : ViewModel() {
    private val TAG = "PickQuantityViewModel"
    private lateinit var date: String
    private lateinit var words: ArrayList<Word>

    private val _uiState = MutableStateFlow(
        PickQuantityUiState(
            colorScheme = msp.getColorScheme(),
            isRememberPresent = msp.isRememberPresent
        )
    )
    val uiState: StateFlow<PickQuantityUiState> = _uiState.asStateFlow()

    init {
        Log.d(TAG, "Создано")
    }

    private fun setWordsQuantity(quantity: Int) {
        _uiState.update { state ->
            state.copy(wordsQuantity = quantity)
        }
    }

    fun setPickedQuantity(newQuantity: Int) {
        _uiState.update { state ->
            if (newQuantity > state.wordsQuantity) {
                state.copy(pickedQuantity = state.wordsQuantity)
                return
            }
            state.copy(pickedQuantity = newQuantity)
        }
        setPickedWords()
//        Log.i(TAG, "Установленно количество: ${uiState.value.pickedQuantity}")
    }

    fun setDate(targetDate: String) {
        date = targetDate
        viewModelScope.launch {
            words = db.getWordsFromDate(date, msp.user)
            setWordsQuantity(words.size)
            setPickedWords()
        }
    }

    private fun setPickedWords() {
        _uiState.update { state ->
            state.copy(pickedWords = words.joinToString(limit = state.pickedQuantity, postfix = "") { it.word })
        }
    }

    suspend fun toTest() {
        val listId =
            createList() // createList() (оба) возвращают номер списка слов для прохождения
        // TODO(запуск экрана теста)
    }

    private suspend fun createList(): Int {
        return db.createList(words = words, date = date, user = msp.user)
    }
}