package com.example.university.View.Auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.compose.rememberNavController
import com.example.university.Model.MySharedPreferences
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
                val navController = rememberNavController()
                AuthNavGraph(navController = navController, this)
            }
        }
    }

    fun toMain() {
        val intent = Intent(this@AuthActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}