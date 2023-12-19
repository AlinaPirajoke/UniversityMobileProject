package com.example.university.View.Auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.university.Model.MySharedPreferences
import com.example.university.UsefullStuff.showToast
import com.example.university.View.Main.MainActivity
import com.example.university.theme.KotobaCustomTheme

class AuthActivity : AppCompatActivity() {
    val TAG = "AuthActivity"

    @OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterialApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        val msp = MySharedPreferences(this)
        setContent {
            KotobaCustomTheme(colorScheme = msp.getColorScheme()) {
                Surface(
                    Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val navController = rememberNavController()
                    AuthNavGraph(
                        navController = navController,
                        showErrorMessage = ::showErrorMassage,
                        onGoingToMain = ::toMain
                    )
                }
            }
        }
    }

    fun showErrorMassage(message: String) {
        showToast(message, this)
        Log.w(TAG, "Получена ошибка: $message")
    }

    fun toMain() {
        val intent = Intent(this@AuthActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}