package com.example.university.ViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.university.Model.AppDB.AppDbManager
import com.example.university.Model.MySharedPreferences
import com.example.university.UsefullStuff.Word
import com.example.university.ViewModel.States.RememberUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RememberViewModel(val db: AppDbManager, val msp: MySharedPreferences) : ViewModel() {
    private val TAG = " RememberViewModel"
    private var listId: Int = -1
    private var wordList = ArrayList<Word>()
    private lateinit var currentWord: Word
    private lateinit var iterator: ListIterator<Word>

    private val _uiState = MutableStateFlow(
        RememberUiState()
    )
    val uiState: StateFlow<RememberUiState> = _uiState.asStateFlow()

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

    fun rememberStart(pickedListId: Int) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                listId = pickedListId
                wordList = db.getWordsFromList(listId)
                iterator = wordList.listIterator()
                toFirstStage()
            }
        }
    }

    fun testFinish() {
        Log.i(TAG, "Тест завершен")
        showFinishAlertDialog()
    }

    private fun toNextWord(){
        if (!(iterator.hasNext())) {
            testFinish()
            return
        }

        showTranscrLabel(false)
        currentWord = iterator.next()
        setCurrentWord()
        Log.d(TAG, "Следующее слово: ${currentWord.word}")
        if (!(iterator.hasNext())) {
            setItLastWord()
        }
    }

    fun toFirstStage() {
        toNextWord()
        setCurrentStage(1)
    }

    fun toSecondStage() = setCurrentStage(2)

    fun showKana() = showTranscrLabel()

    private fun setCurrentStage(stage: Int) {
        _uiState.update { state ->
            state.copy(
                currentStage = stage
            )
        }
    }

    private fun setCurrentWord() {
        _uiState.update { state ->
            state.copy(
                currentWord = currentWord,
            )
        }
    }

    private fun showTranscrLabel(condition: Boolean = true) {
        _uiState.update { state ->
            state.copy(
                isTranscrShowing = condition,
            )
        }
    }

    fun setItLastWord(condition: Boolean = true){
        _uiState.update { state ->
            state.copy(
                isItLastWord = condition,
            )
        }
    }

    fun onExit() {
        showExitAlertDialog()
    }
}