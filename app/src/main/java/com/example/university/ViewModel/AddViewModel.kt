package com.example.university.ViewModel

import android.util.Log
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModel
import com.example.university.Model.AppDB.AppDbManager
import com.example.university.Model.MySharedPreferences
import com.example.university.ViewModel.States.AddUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

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
        val values = uiState.value

        if (values.wordValue.isBlank()) {
            setErrorMessage("Слово не должно быть пустым")
            setWordFieldWrong()
            return
        }
        if (values.translValues.all { it.isBlank() }) {
            setErrorMessage("Перевод не должен быть пустым")
            setTranslFieldWrong()
            return
        }
        if (values.lvlValue.isNotBlank() && values.lvlValue.toInt() < 0) {
            setErrorMessage("Период появления должен быть положительным")
            setLvlFieldWrong()
            return
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
            return
        } catch (ex: Exception) {
            setErrorMessage("Ошибка добавления")
            return
        }
        sendToMain()
    }


}