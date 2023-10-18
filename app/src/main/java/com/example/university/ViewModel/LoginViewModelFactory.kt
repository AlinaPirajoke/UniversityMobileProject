package com.example.university.ViewModel

import android.content.Context
import android.preference.PreferenceManager
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.university.database.DBManager

class LoginViewModelFactory(context: Context) : ViewModelProvider.Factory {
    val TAG = "LoginViewModel"
    val db = DBManager(context)
    val sharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(context /* Activity context */)

    override fun <T: ViewModel> create(modelClass: Class<T>): T {
        Log.d(TAG, "Создание LoginViewModel")
        return LoginViewModel(db, sharedPreferences) as T
    }
}