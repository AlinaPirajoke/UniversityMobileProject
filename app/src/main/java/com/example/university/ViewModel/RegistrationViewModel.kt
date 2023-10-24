package com.example.university.ViewModel

import android.content.SharedPreferences
import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.university.database.DBManager
import com.example.university.theme.mainColor
import kotlinx.coroutines.launch

class RegistrationViewModel (val db: DBManager, val sharedPreferences: SharedPreferences): ViewModel() {
    val TAG = "RegistrationViewModel"

    var field1Color = MutableLiveData<Color>()
    var field2Color = MutableLiveData<Color>()
    var pass1 = MutableLiveData<String>()
    var pass2 = MutableLiveData<String>()
    var isGoingToMain = MutableLiveData<Boolean>()
    var errorMessage = MutableLiveData<String>()

    init {
        Log.d(TAG, "Создано")
    }

    fun setPass1Value(pass: String){
        pass1.value = pass
        // Здесь лучше запустить в корутине
        viewModelScope.launch {
            if(isValidSymbols(pass))
                setNormalField1Color()
            else {
                setErrorField1Color()
                setErrorMessage("Вы вводите некорректные символы!")
            }
        }
    }

    fun setPass2Value(pass: String){
        pass2.value = pass
        setNormalField2Color()

    }

    fun setErrorField1Color(){
        field1Color.value = Color.Red
    }

    fun setNormalField1Color(){
        field1Color.value = mainColor
    }

    fun setErrorField2Color(){
        field2Color.value = Color.Red
    }

    fun setNormalField2Color(){
        field2Color.value = mainColor
    }

    fun sendToHomePage(){
        isGoingToMain.value = true
    }

    fun setErrorMessage(text: String){
        errorMessage.value = text
    }

    fun addUser() {
        val pass1 = this.pass1.value
        val pass2 = this.pass2.value
        if (pass1 != pass2) {
            setErrorMessage("Пароли не совпадают")
            setErrorField2Color()
            return
        }
        if (!(pass1?.let { isValidSymbols(it)} == true && pass1.let { isValidLength(it) })) {
            setErrorMessage("Использованы недопустимые символы")
            setErrorField1Color()
            setErrorField2Color()
            return
        }
        if ((pass1 in db.getPasswords().keys) != false) {
            setErrorMessage("Пароль не подходит, придумайте другой")
            setErrorField1Color()
            setErrorField2Color()
            return
        }
        viewModelScope.launch {
            db.insertPassword(pass1.toString())
            val newId = db.getPasswords().get(pass1)
            sharedPreferences.edit().putBoolean("session", true).apply()
            newId?.let {
                sharedPreferences.edit().putInt("user", it).apply()
            }
            sendToHomePage()
        }
    }

    private fun isValidSymbols(password: String): Boolean {
        val passwordRegex = Regex("^[a-zA-Z0-9]*$")
        Log.d(
            TAG, "Проверка пароля: " +
                    "валидность символов: ${password.matches(passwordRegex)}, " +
                    "длинна: ${password.length}"
        )
        return password.matches(passwordRegex)
    }

    private fun isValidLength(password: String): Boolean{
        return password.length >= 6
    }
}