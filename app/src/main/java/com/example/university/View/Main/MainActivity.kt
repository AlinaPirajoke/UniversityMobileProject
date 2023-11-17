package com.example.university.View.Main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.university.View.Auth.AuthActivity
import com.example.university.View.Main.Screens.addScreen
import com.example.university.View.Main.Screens.mainScreen
import com.example.university.View.Main.Screens.settingsScreen
import com.example.university.ViewModel.AddViewModel
import com.example.university.ViewModel.MainActivityViewModel
import com.example.university.ViewModel.MainViewModel
import com.example.university.ViewModel.SettingsViewModel
import com.example.university.usefull_stuff.StringInt
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


    /* Следующие функции запускаются Навхостом и запускают соответствующие экраны
     Я понимаю, что это в классе лучше не оставлять, но я не смог получить вьюмодели из
     Навхоста или самих фрагментов (другого решения я не нашел(я реально искал)). */
    @Composable
    fun mainInit(context: MainActivity, navController: NavHostController) {
        val mvm by viewModel<MainViewModel>()
        mvm.updateColorScheme()
        mainScreen(context = context, navController = navController, vm = mvm)
    }

    @Composable
    fun addInit(context: MainActivity, navController: NavHostController) {
        val avm by viewModel<AddViewModel>()
        avm.updateColorScheme()
        addScreen(context = context, navController = navController, vm = avm)

    }

    @Composable
    fun settingsInit(context: MainActivity, navController: NavHostController) {
        val svm by viewModel<SettingsViewModel>()
        settingsScreen(context = this, navController = navController, vm = svm)
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

    fun toTest(dateCount: StringInt) {
        val intent = Intent(this@MainActivity, PickActivity::class.java)
        intent.putExtra("date", dateCount.string)
        //intent.putExtra("count", dateCount.string)
        startActivity(intent)
    }
}