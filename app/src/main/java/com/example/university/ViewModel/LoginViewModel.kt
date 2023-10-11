package com.example.university.ViewModel

import android.content.SharedPreferences
import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.university.R
import com.example.university.database.DBManager
import com.example.university.theme.mainColor

class LoginViewModel(val db: DBManager): ViewModel() {
    val TAG = "LoginViewModel"

    var fieldColor = MutableLiveData<Color>()
    var isGoingToMain = MutableLiveData<Boolean>()
    var errorMesage = MutableLiveData<String>()

    init {
        Log.d(TAG, "Создано")
    }

    fun setErrorFieldColor(){
        fieldColor.value = Color.Red
    }

    fun setNormalFieldColor(){
        fieldColor.value = mainColor
    }

    fun sendToHomePage(){
        isGoingToMain.value = true
    }

    fun setErrorMessage(text: String){
        errorMesage.value = text
    }

    fun checkPassword(
        pass: String,
        sharedPreferences: SharedPreferences
    ) {
        if (sharedPreferences.getBoolean("needPassword", true))
            if (pass.isEmpty()) {
                setErrorMessage("Введите пароль")
                setErrorFieldColor()
            }
        val passwords = db.getPasswords()
        if (pass in passwords.keys) {
            passwords.get(pass)?.let {
                sharedPreferences.edit().putInt("user", it).apply()
            }
            sharedPreferences.edit().putBoolean("session", true).apply()
            sendToHomePage()

        } else {
            setErrorMessage("Пароль не верен")
            setErrorFieldColor()
        }

    }

}