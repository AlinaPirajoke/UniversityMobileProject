package com.example.university.ViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.university.Model.DBManager
import com.example.university.Model.MySharedPreferences
import com.example.university.ViewModel.States.SettingsUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SettingsViewModel(val db: DBManager, val msp: MySharedPreferences) : ViewModel() {
    val TAG = "SettingsViewModel"

    private val _uiState = MutableStateFlow(SettingsUiState(colorScheme = msp.getColorScheme()))
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        Log.d(TAG, "Создано")
        _uiState.update { state ->
            state.copy(
                isPasswordNeeded = msp.isPasswordNeeded,
                currentColorScheme = msp.currentColorSchemeId
                /*isPasswordNeeded = sp.getBoolean("isPasswordNeeded", false),
                currentColorScheme = sp.getInt("currentColorScheme", 0),*/
            )
        }
    }

    fun setErrorMessage(text: String) {
        _uiState.update { state ->
            state.copy(errorMessage = text)
        }
    }

    fun clearErrorMessage() {
        setErrorMessage("")
    }

    fun setIsPasswordNeeded(condition: Boolean) {
        if (db.getPasswords().size > 1) {
            setErrorMessage("Для изменения этого параметра, пользователь должен быть только один!")
            Log.w(TAG, "Отклонено")
            return
        }
        _uiState.update { state ->
            state.copy(isPasswordNeeded = condition)
        }
        msp.isPasswordNeeded = condition
        /*sp.edit().putBoolean("isPasswordNeeded", condition).apply()*/
    }

    fun setCurrentColorScheme(id: Int) {
        msp.currentColorSchemeId = id
        _uiState.update { state ->
            state.copy(
                currentColorScheme = id,
                colorScheme = msp.getColorScheme()
            )
        }

        /*sp.edit().putInt("currentColorScheme", number).apply()*/
    }
}