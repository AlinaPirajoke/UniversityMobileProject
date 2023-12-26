package com.example.university.viewModel

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.university.model.MySharedPreferences
import com.example.university.model.appDB.AppDbManager
import com.example.university.viewModel.states.EditUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EditViewModel(val db: AppDbManager, val msp: MySharedPreferences) : ViewModel() {
    val TAG = "LoginViewModel"
    var modifiedWordId: Int = 0
        set(value) {
            field = value
            val word = db.getWordFromId(field)
            val transl = mutableStateListOf<String>()
            word.translations.forEach {
                transl.add(it)
            }
            _uiState.update { state ->
                state.copy(
                    wordValue = word.word,
                    transcrValue = word.transcription,
                    translValues = transl
                )
            }
        }

    private val _uiState = MutableStateFlow(EditUiState())
    val uiState: StateFlow<EditUiState> = _uiState.asStateFlow()

    init {
        Log.d(TAG, "Создано")
    }

    fun sendToExit(condition: Boolean = true) {
        _uiState.update { state ->
            state.copy(isGoingToExit = condition)
        }
    }

    // Флаги ошибок
    fun setWordFieldWrong(condition: Boolean = true) {
        _uiState.update { state ->
            state.copy(isWordFieldWrong = condition)
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
    fun editWord() {
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

                try {
                    db.editWord(
                        modifiedWordId,
                        values.wordValue,
                        values.transcrValue,
                        values.translValues,
                    )
                } catch (ex: NumberFormatException) {
                    setErrorMessage("Период должен быть целочисленным")
                    return@withContext
                } catch (ex: Exception) {
                    setErrorMessage("Ошибка добавления")
                    return@withContext
                }
                sendToExit()
            }
        }
    }
}