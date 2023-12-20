package com.example.university.ViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.university.Model.AppDB.AppDbManager
import com.example.university.Model.MySharedPreferences
import com.example.university.ViewModel.States.SettingsUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SettingsViewModel(val db: AppDbManager, val msp: MySharedPreferences) : ViewModel() {
    val TAG = "SettingsViewModel"

    private val _uiState = MutableStateFlow(SettingsUiState(colorScheme = msp.getColorScheme()))
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        Log.d(TAG, "Создано")
        _uiState.update { state ->
            state.copy(
                isPasswordNeeded = msp.isPasswordNeeded,
                currentColorScheme = msp.currentColorSchemeId,
                isRememberPresent = msp.isRememberPresent,
                studyQuantityPerDay = msp.studyQuantityPerDay
            )
        }
    }

    fun setErrorMessage(text: String) {
        _uiState.update { state ->
            state.copy(errorMessage = text)
        }
        setHaveErrorMessage(true)
    }

    fun setHaveErrorMessage(condition: Boolean) {
        _uiState.update { state ->
            state.copy(haveErrorMessage = condition)
        }
    }

    fun setIsPasswordNeeded(condition: Boolean) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                if (db.getPasswords().size > 1) {
                    setErrorMessage("Для изменения этого параметра, пользователь должен быть только один!")
                    Log.w(TAG, "Отклонено")
                    return@withContext
                }
                _uiState.update { state ->
                    state.copy(isPasswordNeeded = condition)
                }
                msp.isPasswordNeeded = condition
            }
        }
    }

    fun setCurrentColorScheme(id: Int) {
        msp.currentColorSchemeId = id
        _uiState.update { state ->
            state.copy(
                currentColorScheme = id,
                colorScheme = msp.getColorScheme()
            )
        }
    }

    fun setIsRememberPresent(condition: Boolean){
        _uiState.update { state ->
            state.copy(isRememberPresent = condition)
        }
        msp.isRememberPresent = condition
    }

    fun setIsAnimationsLong(condition: Boolean){
        _uiState.update { state ->
            state.copy(isAnimationsLong = condition)
        }
        msp.isAnimationsLong = condition
    }

    fun setStudyQuantityPerDay(quantity: Int){
        if(quantity in 0..100) {
            _uiState.update { state ->
                if(state.studyQuantityPerDay == 0)
                    state.copy(studyQuantityPerDay = quantity/10)
                else
                    state.copy(studyQuantityPerDay = quantity)
            }
            msp.studyQuantityPerDay = quantity
        }
    }
}