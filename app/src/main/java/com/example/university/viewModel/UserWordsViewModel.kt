package com.example.university.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.university.model.MySharedPreferences
import com.example.university.model.appDB.AppDbManager
import com.example.university.usefullStuff.Word
import com.example.university.viewModel.states.UserWordsUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserWordsViewModel(val db: AppDbManager, val msp: MySharedPreferences) : ViewModel() {
    val TAG = "UserWordsViewModel"
    private lateinit var words: ArrayList<Word>

    private val _uiState = MutableStateFlow(UserWordsUiState())
    val uiState: StateFlow<UserWordsUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                words = getAllwords()
            }
            updateWords()
        }
    }

    fun updateWords() {
        val filtered = words.filter {
            it.word.contains(uiState.value.filter) || it.translations.filter {
                it.contains(
                    uiState.value.filter
                )
            }.isNotEmpty()
        }
        _uiState.update { state ->
            state.copy(
                words = ArrayList(filtered),
                pickedWordNo = -1
                )
        }
    }

    suspend fun getAllwords() = db.getAllWords(user = msp.user)

    fun openFinder() {
        _uiState.update { state ->
            state.copy(isFinderActive = true)
        }
    }

    fun setFilter(text: String) {
        _uiState.update { state ->
            state.copy(filter = text)
        }
        updateWords()
    }

    fun openDeleteAlert() {
        _uiState.update { state ->
            state.copy(isAlertShowing = true)
        }
    }

    fun closeDeleteAlert() {
        _uiState.update { state ->
            state.copy(isAlertShowing = false)
        }
    }

    fun pickElement(no: Int) {
        _uiState.update { state ->
            state.copy(pickedWordNo = no)
        }
    }

    fun deleteWord() {
        openDeleteAlert()
    }

    fun DeleteWordReject() {
        closeDeleteAlert()
    }

    fun DeleteWordConfirm() {
        viewModelScope.launch {
            db.deleteWord(uiState.value.words[uiState.value.pickedWordNo])
            uiState.value.words.removeAt(uiState.value.pickedWordNo)
            closeDeleteAlert()
        }
    }
}