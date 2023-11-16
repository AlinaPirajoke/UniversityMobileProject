package com.example.university.ViewModel

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModel
import com.example.university.Model.DBManager
import com.example.university.Model.MySharedPreferences
import com.example.university.ViewModel.States.AddUiState
import com.example.university.ViewModel.States.LoginUiState
import com.example.university.usefull_stuff.showToast
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AddViewModel(val db: DBManager, val msp: MySharedPreferences) : ViewModel() {
    val TAG = "LoginViewModel"

    private val _uiState = MutableStateFlow(AddUiState(colorScheme = msp.getColorScheme()))
    val uiState: StateFlow<AddUiState> = _uiState.asStateFlow()

    init {
        Log.d(TAG, "Создано")
    }

    // Флаги ошибок
    fun setWordFieldWrong(condition: Boolean = true) {
        _uiState.update { state ->
            state.copy(isWordFieldWrong = condition)
        }
    }

    fun setLvlFieldWrong(condition: Boolean = true) {
        _uiState.update { state ->
            state.copy(isLvlFieldWrong = condition)
        }
    }

    fun setTranslFieldWrong(condition: Boolean = true) {
        _uiState.update { state ->
            state.copy(isTranslFieldWrong = condition)
        }
    }

    fun setErrorMessage(text: String) {
        _uiState.update { state ->
            state.copy(errorMessage = text)
        }
    }

    fun updateColorScheme() {
        _uiState.update { state ->
            state.copy(colorScheme = msp.getColorScheme())
        }
    }

    // Поля ввода
    fun editWordValue(text: String) {
        setWordFieldWrong(false)
        _uiState.update { state ->
            state.copy(wordValue = text)
        }
    }

    fun editTranscrValue(text: String) {
        setTranslFieldWrong(false)
        _uiState.update { state ->
            state.copy(transcrValue = text)
        }
    }

    fun editLvlValue(value: String) {
        if (value.isDigitsOnly()) {
            setLvlFieldWrong(false)
            _uiState.update { state ->
                state.copy(lvlValue = value)
            }
        } else {
            setErrorMessage("Значение должно быть целочисленным")
            setLvlFieldWrong()
        }
    }

    fun editTranslationValue(text: String, number: Int) {
        Log.d(TAG, "Обновление значение перевода $number на $text")
        _uiState.update { state ->
            val newTranslValues = state.translValues
            newTranslValues[number] = text
            state.copy(translValues = newTranslValues)
        }
    }

    fun addTranslation() {
        _uiState.update { state ->
            val newTranslValues = state.translValues
            newTranslValues.add("")
            state.copy(translValues = newTranslValues)
        }
    }

    // Добавление нового слова
     suspend fun addWord() {

        if (uiState.value.wordValue.isBlank()) {
            setErrorMessage("Слово не должно быть пустым")
            setWordFieldWrong()
            return
        }

        if (uiState.value.translValues.all { it.isBlank() }) {
            setErrorMessage("Перевод не должен быть пустым")
            setTranslFieldWrong()
            return
        }

        if (uiState.value.lvlValue.toInt() < 0) {
            setErrorMessage("Период появления должен быть положительным")
            setLvlFieldWrong()
            return
        }
        /*
        try{
            val period: Int
            if(days.isBlank())
                period = 0
            else
                period = days.toInt()
            db.addNewWord(enWord, transc, ruWord, period, user)
        }
        catch (ex: NumberFormatException) {
            showToast("Период должен быть целочисленным", context)
            return
        }
        catch (ex: Exception) {
            showToast("Ошибка добавления", context)
            return
        }

        toExit()*/
    }


}