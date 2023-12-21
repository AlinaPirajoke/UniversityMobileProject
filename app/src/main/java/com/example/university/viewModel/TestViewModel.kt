package com.example.university.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.university.model.MySharedPreferences
import com.example.university.model.appDB.AppDbManager
import com.example.university.usefullStuff.Word
import com.example.university.viewModel.states.TestUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TestViewModel(val db: AppDbManager, val msp: MySharedPreferences) : ViewModel() {
    private val TAG = "TestViewModel"
    private var listId: Int = -1
    private var wordList = ArrayList<Word>()
    private var resultCounter = 0
    private lateinit var currentWord: Word
    private lateinit var iterator: ListIterator<Word>

    private val _uiState = MutableStateFlow(
        TestUiState()
    )
    val uiState: StateFlow<TestUiState> = _uiState.asStateFlow()

    fun showExitAlertDialog() {
        _uiState.update { state ->
            state.copy(
                isExitAlertDialogShowing = true,
            )
        }
    }

    fun hideExitAlertDialog() {
        _uiState.update { state ->
            state.copy(
                isExitAlertDialogShowing = false,
            )
        }
    }

    fun showFinishAlertDialog() {
        _uiState.update { state ->
            state.copy(
                isFinishAlertDialogShowing = true,
            )
        }
    }

    fun hideFinishAlertDialog() {
        _uiState.update { state ->
            state.copy(
                isFinishAlertDialogShowing = false,
            )
        }
    }

    fun testStart(pickedListId: Int) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                listId = pickedListId
                wordList = db.getWordsFromList(listId)
                iterator = wordList.listIterator()
                setTotalWords()
                nextWord()
            }
        }
    }

    fun setTotalWords(){
        _uiState.update { state ->
            state.copy(
                totalWords = wordList.size
            )
        }
    }

    private fun testFinish() {
        Log.i(TAG, "Тест завершен")
        showFinishAlertDialog()
    }

    private fun nextWord() {
        if (!(iterator.hasNext())) {
            testFinish()
            return
        }

        updateWordIndex()
        currentWord = iterator.next()
        Log.d(TAG, "Следующее слово: ${currentWord.word}")
        cleanAllLabels()
        toFirstStage()
    }

    fun toFirstStage() {
        setCurrentStage(1)
        showWordLabel()
    }

    // Отсылка к моему прошлому проекту (Не кривой нейминг)
    fun showKana() {
        showTranscrLabel()
    }

    fun toSecondStage() {
        setCurrentStage(2)
        showAllLabels()
    }

    // -_- в этих методах различается ровно одна цифра, но логически, это разные случаи
    // Может быть я говнокодер?
    fun goodResultProcessing() {
        resultCounter += 1
        Log.d(TAG, "Результат в баллах: $resultCounter")
        currentWord.result = 1
        viewModelScope.launch {
            saveWordResult()
        }
        nextWord()
    }

    fun badResultProcessing() {
        currentWord.result = 0
        viewModelScope.launch {
            saveWordResult()
        }
        nextWord()
    }

    private fun setCurrentStage(stage: Int) {
        _uiState.update { state ->
            state.copy(
                currentStage = stage
            )
        }
    }

    private fun cleanAllLabels() {
        _uiState.update { state ->
            state.copy(
                wordLabel = "",
                transcrLabel = "",
                translLabel = ""
            )
        }
    }

    private fun showWordLabel() {
        _uiState.update { state ->
            state.copy(
                wordLabel = currentWord.word,
            )
        }
    }

    private fun showTranscrLabel() {
        _uiState.update { state ->
            state.copy(
                transcrLabel = currentWord.transcription,
            )
        }
    }

    private fun showTranslLabel() {
        _uiState.update { state ->
            state.copy(
                translLabel = currentWord.translationsToString(),
            )
        }
    }

    private fun showAllLabels() {
        _uiState.update { state ->
            state.copy(
                wordLabel = currentWord.word,
                transcrLabel = currentWord.transcription,
                translLabel = currentWord.translationsToString(),
            )
        }
    }

    suspend fun saveWordResult(word: Word = currentWord) {
        db.saveWordResult(word = word, listId = listId)
    }

    fun getResult(): Double {
        return resultCounter.toDouble() / wordList.size * 100
    }

    fun getListId(): Int = listId

    fun updateWordIndex(){
        _uiState.update { state ->
            state.copy(
                wordIndex = iterator.nextIndex() + 1
            )
        }
    }

    fun onExit() {
        showExitAlertDialog()
    }
}