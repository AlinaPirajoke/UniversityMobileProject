package com.example.university.View.Main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.toArgb
import androidx.navigation.compose.rememberNavController
import com.example.university.UsefullStuff.showToast
import com.example.university.View.Auth.AuthActivity
import com.example.university.ViewModel.MainActivityViewModel
import com.example.university.theme.KotobaCustomTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    val TAG = "MainActivity"
    val vm by viewModel<MainActivityViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "Инициализация...")
        super.onCreate(savedInstanceState)

        setContent {
            val uiState by vm.uiState.collectAsState()
            if (uiState.isGoingToLogin) this.toLogin()
            KotobaCustomTheme(colorScheme = uiState.colorScheme) {
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