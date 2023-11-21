package com.example.university.View.Main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.rememberNavController
import com.example.university.View.Auth.AuthActivity
import com.example.university.ViewModel.MainActivityViewModel
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
            val navController = rememberNavController()
            MainNavGraph(navController = navController, this)
        }
    }
    
    override fun onResume() {
        super.onResume()
        vm.updateColorScheme()
    }

    fun toLogin() {
        val intent = Intent(this@MainActivity, AuthActivity::class.java)
        startActivity(intent)
        finish()
    }
}