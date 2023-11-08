package com.example.university.View.Auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import com.example.university.View.Main.MainActivity
import com.example.university.ViewModel.LoginViewModel
import com.example.university.ViewModel.LoginViewModelFactory
import com.example.university.ViewModel.RegistrationViewModel
import com.example.university.ViewModel.RegistrationViewModelFactory
import com.example.university.theme.ColorScheme
import com.example.university.theme.KotobaCustomTheme
import com.example.university.usefull_stuff.showToast

// private val Context.dataStore by preferencesDataStore("user_preferences")

class AuthActivity : AppCompatActivity() {
    val TAG = "AuthActivity"

    @OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterialApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)


        setContent {
            KotobaCustomTheme(schemeId = ColorScheme.PH.id) {
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