package com.example.university.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.university.Model.AppDB.AppDbManager
import com.example.university.Model.MySharedPreferences
import com.example.university.ViewModel.States.UserWordsUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UserWordsViewModel(val db: AppDbManager, val msp: MySharedPreferences) : ViewModel() {
    val TAG = "UserWordsViewModel"

    private val _uiState =
        MutableStateFlow(UserWordsUiState())
    val uiState: StateFlow<UserWordsUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            _uiState.update { state ->
                state.copy(words = getAllwords())
            }
        }
    }

    suspend fun getAllwords() = db.getAllWords(user = msp.user)

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