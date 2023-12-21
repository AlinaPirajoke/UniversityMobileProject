package com.example.university.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.university.model.appDB.AppDbManager
import com.example.university.model.MySharedPreferences
import com.example.university.viewModel.states.FutureTestsUiState
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
        val quantity = db.getListsSizeAndDays(length = BLOCK_QUANTITY, user = msp.user)
        Log.i(TAG, "Даты и кол-во слов для повторения: $quantity")
        return quantity
    }
}