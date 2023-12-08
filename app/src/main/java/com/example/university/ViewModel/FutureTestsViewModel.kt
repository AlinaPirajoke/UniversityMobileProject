package com.example.university.ViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.university.Model.AppDB.AppDbManager
import com.example.university.Model.MySharedPreferences
import com.example.university.ViewModel.States.FutureTestsUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FutureTestsViewModel(val db: AppDbManager, val msp: MySharedPreferences) : ViewModel() {
    val TAG = "FutureTestsViewModel"
    val BLOCK_QUANTITY = 17

    private val _uiState =
        MutableStateFlow(FutureTestsUiState())
    val uiState: StateFlow<FutureTestsUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            _uiState.update { state ->
                state.copy(dateQuantity = getDateQuantityList())
            }
        }
    }

    suspend fun getDateQuantityList(): List<Pair<String, Int>> {
        val quality = db.getListsSizeAndDays(length = BLOCK_QUANTITY, user = msp.user)
        Log.i(TAG, "Даты и кол-во слов для повторения: $quality")
        return quality
    }
}