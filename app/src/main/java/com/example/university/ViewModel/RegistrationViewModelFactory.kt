package com.example.university.ViewModel

import android.content.Context
import android.preference.PreferenceManager
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.university.database.DBManager

class RegistrationViewModelFactory(context: Context) : ViewModelProvider.Factory {

    val TAG = "RegistrationViewModel"
    val db = DBManager(context)
    val sharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(context /* Activity context */)

    override fun <T: ViewModel> create(modelClass: Class<T>): T {
        Log.d(TAG, "Создание RegistrationViewModel")
        return RegistrationViewModel(db, sharedPreferences) as T
    }
}