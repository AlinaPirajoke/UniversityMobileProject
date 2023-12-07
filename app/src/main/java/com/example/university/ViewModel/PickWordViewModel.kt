package com.example.university.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.university.Model.AppDB.AppDbManager
import com.example.university.Model.MySharedPreferences
import com.example.university.Model.WordsDB.WordsDbManager
import com.example.university.ViewModel.States.PickWordUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PickWordViewModel(
    val adb: AppDbManager,
    val wdb: WordsDbManager,
    val msp: MySharedPreferences
) : ViewModel() {
    private val TAG = "PickQuantityViewModel"
    private var trueRemain: Int =
        msp.studyQuantityPerDay - msp.todayStudiedQuantity
    private val _uiState = MutableStateFlow(PickWordUiState())
    val uiState: StateFlow<PickWordUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            _uiState.update { state ->
                state.copy(words = wdb.getUnlearnedWords())
            }
            setRemain()
        }
    }

    private fun setRemain() {
        var quantity = trueRemain
        if (quantity < 0) quantity = 0
        _uiState.update { state ->
            state.copy(remain = quantity)
        }
    }

    fun sendToMain(condition: Boolean = true) {
        _uiState.update { state ->
            state.copy(isGoingToMain = condition)
        }
    }

    fun updateTopText() {
        val remain = uiState.value.remain
        if (remain == 0) {
            _uiState.update { state ->
                state.copy(topText = "Выбрано достаточно")
            }
            return
        }

        var text = ""
        val rightForm = when (remain % 10) {
            1 -> "слово"
            in 2..4 -> "слова"
            else -> "слов"
        }
        if (uiState.value.pickedWords.size > 0)
            text = "Выберите ещё $remain $rightForm"
        else
            text = "Выберите слова для изучения"
        _uiState.update { state ->
            state.copy(topText = text)
        }
    }

    fun pickWord(no: Int) {
        _uiState.update { state ->
            val newlist = state.pickedWords
            newlist.add(no)
            state.copy(
                pickedWords = newlist,
                pickedQuantity = newlist.size
            )
        }
        trueRemain--
        setRemain()
        updateTopText()
    }

    fun unpickWord(no: Int) {
        _uiState.update { state ->
            val newlist = state.pickedWords
            newlist.remove(no)
            state.copy(
                pickedWords = newlist,
                pickedQuantity = newlist.size
            )
        }
        trueRemain++
        setRemain()
        updateTopText()
    }

    fun confirm() {
        if (uiState.value.pickedWords.size > 0) {
            viewModelScope.launch {
                val picked = uiState.value.words.filterIndexed { id, word ->
                    id in uiState.value.pickedWords
                }
                // wdb.removeWords(picked) TODO(разкоментить при релизе)
                adb.addNewWords(picked, msp.user)
                sendToMain()
            }
        }
    }
}