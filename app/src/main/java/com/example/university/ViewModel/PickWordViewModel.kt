package com.example.university.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.university.Model.AppDB.AppDbManager
import com.example.university.Model.MySharedPreferences
import com.example.university.ViewModel.States.PickWordUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PickWordViewModel(
    val db: AppDbManager,
    val msp: MySharedPreferences
) : ViewModel() {
    private val TAG = "PickQuantityViewModel"
    private var trueRemain: Int =
        msp.studyQuantityPerDay - msp.todayStudiedQuantity
    private val _uiState = MutableStateFlow(PickWordUiState())
    val uiState: StateFlow<PickWordUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                if (msp.isAnimationsLong)
                    delay(3500)

                _uiState.update { state ->
                    state.copy(words = db.getUnlernedLibraryWords(user = msp.user))
                }
                setRemain()
            }
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
        if (uiState.value.pickedWords.size > 0) {
            var rightForm = ""
            if (remain % 10 == 1)
                rightForm = "слово"
            else if (remain % 10 <= 4)
                rightForm = "слова"
            else
                rightForm = "слов"
            text = "Выберите ещё $remain $rightForm"
        } else
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
                db.markWordsAsLearned(picked = picked, user = msp.user)
                db.addNewWords(picked, msp.user)
                sendToMain()
            }
        }
    }
}