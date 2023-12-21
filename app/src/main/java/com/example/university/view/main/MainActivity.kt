package com.example.university.view.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.navigation.compose.rememberNavController
import com.example.university.model.MySharedPreferences
import com.example.university.theme.KotobaCustomTheme
import com.example.university.usefullStuff.showToast
import com.example.university.view.auth.AuthActivity
import com.example.university.viewModel.MainActivityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    val TAG = "MainActivity"
    val vm by viewModel<MainActivityViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "Инициализация...")
        checkIsThisFirstAcces()
        super.onCreate(savedInstanceState)
        setContent {
            val uiState by vm.uiState.collectAsState()
            if (uiState.isGoingToLogin) this.toLogin()
            KotobaCustomTheme(colorScheme = uiState.colorScheme) {
                Surface(
                    Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    window.statusBarColor = MaterialTheme.colors.primary.toArgb()
                    val navController = rememberNavController()
                    MainNavGraph(
                        navController = navController,
                        onGoingToLogin = ::toLogin,
                        onChangeColorScheme = ::updateColorScheme,
                        showErrorMessage = ::showErrorMassage,
                    )
                }
            }
        }
    }

    fun checkIsThisFirstAcces() {
        val msp = MySharedPreferences(this)
        if (msp.firstAppAccessDate.isBlank())
            vm.onFirstAccess()
    }

    fun updateColorScheme() {
        vm.updateColorScheme()
    }

    override fun onResume() {
        super.onResume()
        updateColorScheme()
    }

    fun showErrorMassage(message: String) {
        showToast(message, this)
        Log.w(TAG, "Получена ошибка: $message")
    }

    fun toLogin() {
        val intent = Intent(this@MainActivity, AuthActivity::class.java)
        startActivity(intent)
        finish()
    }
}