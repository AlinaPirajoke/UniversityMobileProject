package com.example.university.ViewModel

import android.content.SharedPreferences
import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.university.database.DBManager
import com.example.university.theme.mainColor

class LoginViewModel(val db: DBManager, val sharedPreferences: SharedPreferences): ViewModel() {
    val TAG = "LoginViewModel"

    var fieldColor = MutableLiveData<Color>()
    var isGoingToMain = MutableLiveData<Boolean>()
    var errorMessage = MutableLiveData<String>()
    var pass = MutableLiveData<String>()

    init {
        Log.d(TAG, "Создано")
        sharedPreferences.edit().putBoolean("session", false).apply()
    }

    fun setPassValue(pass: String){
        this.pass.value = pass
        setNormalFieldColor()
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
        errorMessage.value = text
    }

    fun checkPassword() {
        val pass = this.pass.value
        if (sharedPreferences.getBoolean("needPassword", true))
            if (pass?.isEmpty() == true) {
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