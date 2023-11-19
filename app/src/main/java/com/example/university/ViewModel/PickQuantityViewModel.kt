package com.example.university.ViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.university.Model.AppDB.AppDbManager
import com.example.university.Model.MySharedPreferences
import com.example.university.ViewModel.States.PickQuantityUiState
import com.example.university.ViewModel.States.RegistrationUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class PickQuantityViewModel(val db: AppDbManager, val msp: MySharedPreferences) : ViewModel() {
    private val TAG = "PickQuantityViewModel"

    private val _uiState = MutableStateFlow(PickQuantityUiState(
        colorScheme = msp.getColorScheme(),
        isRememberPresent = msp.isRememberPresent))
    val uiState: StateFlow<PickQuantityUiState> = _uiState.asStateFlow()

    init {
        Log.d(TAG, "Создано")
    }

    fun setQuantity(newQuantity: (Int)) {
        _uiState.update { state ->
            if (newQuantity <= state.wordsQuantity)
                state.copy(pickedQuantity = newQuantity)
            else
                state.copy(pickedQuantity = state.wordsQuantity)
            return
        }
    }
}