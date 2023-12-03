package com.example.university.ViewModel

import android.util.Log
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.university.Model.API.ApiManager
import com.example.university.Model.AppDB.AppDbManager
import com.example.university.Model.MySharedPreferences
import com.example.university.ViewModel.States.AddUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddViewModel(val db: AppDbManager, val msp: MySharedPreferences) : ViewModel() {
    val TAG = "LoginViewModel"

    private val _uiState = MutableStateFlow(AddUiState(colorScheme = msp.getColorScheme()))
    val uiState: StateFlow<AddUiState> = _uiState.asStateFlow()

    init {
        Log.d(TAG, "Создано")
    }

    fun sendToMain(condition: Boolean = true) {
        _uiState.update { state ->
            state.copy(isGoingToMain = condition)
        }
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

    fun setTranslating(condition: Boolean){
        _uiState.update { state ->
            state.copy(isTranslating = condition)
        }
    }

    fun setTranslationError(condition: Boolean){
        _uiState.update { state ->
            state.copy(isTranslationError = condition)
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

    fun autoTranslate(){
        if (uiState.value.wordValue.isBlank()) {
            setErrorMessage("Укажите слово для перевода")
            setWordFieldWrong()
            return
        }
        viewModelScope.launch {
            setTranslationError(false)
            try {
                setTranslating(true)
                val api = ApiManager()
                val translation = api.translateText(uiState.value.wordValue)
                editTranslationValue(translation.text, 0)
            }
            catch (e: Exception){
                setTranslationError(true)
            }
            setTranslating(false)
        }
    }

    // Добавление нового слова
    fun addWord() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val values = uiState.value

                if (values.wordValue.isBlank()) {
                    setErrorMessage("Слово не должно быть пустым")
                    setWordFieldWrong()
                    return@withContext
                }
                if (values.translValues.all { it.isBlank() }) {
                    setErrorMessage("Перевод не должен быть пустым")
                    setTranslFieldWrong()
                    return@withContext
                }
                if (values.lvlValue.isNotBlank() && values.lvlValue.toInt() < 0) {
                    setErrorMessage("Период появления должен быть положительным")
                    setLvlFieldWrong()
                    return@withContext
                }

                try {
                    var period = 0
                    if (values.lvlValue.isNotBlank())
                        period = values.lvlValue.toInt()

                    db.addNewWord(
                        values.wordValue,
                        values.transcrValue,
                        values.translValues,
                        period,
                        msp.user
                    )
                } catch (ex: NumberFormatException) {
                    setErrorMessage("Период должен быть целочисленным")
                    return@withContext
                } catch (ex: Exception) {
                    setErrorMessage("Ошибка добавления")
                    return@withContext
                }
                sendToMain()
            }
        }
    }
}