package com.example.university.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.university.model.appDB.AppDbManager
import com.example.university.model.MySharedPreferences
import com.example.university.usefullStuff.Word
import com.example.university.viewModel.states.PickQuantityUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Math.round

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

    fun setPickedQuantity(sliderValue: Float) {
        val roundedQuantity = round(sliderValue)

        _uiState.update { state ->
            if (sliderValue >= state.wordsQuantity.toFloat())
                state.copy(
                    pickedQuantitySlider = state.wordsQuantity.toFloat(),
                    pickedQuantityReal = state.wordsQuantity
                )
            else
                state.copy(
                    pickedQuantityReal = roundedQuantity,
                    pickedQuantitySlider = sliderValue,
                )
        }

        setPickedWords()
//        Log.i(TAG, "Установленно количество: ${uiState.value.pickedQuantity}")
    }

    fun setDate(targetDate: String) {
        date = targetDate
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                words = db.getWordsFromDate(date, msp.user)
                setWordsQuantity(words.size)
                setPickedWords()
            }
        }
    }

    private fun setPickedWords() {
        _uiState.update { state ->
            state.copy(
                pickedWords = words.joinToString(
                    limit = state.pickedQuantityReal,
                    postfix = ""
                ) { it.word }.removeSuffix(", ..."))
        }
    }

    // createList() (оба) возвращают номер списка слов для прохождения
    fun createList(): Int {
        return db.createList(words = words.subList(0, uiState.value.pickedQuantityReal), date = date, user = msp.user)
    }
}