package com.example.university.View.Main

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.university.View.Main.Screens.AddScreen
import com.example.university.View.Main.Screens.MainScreen
import com.example.university.View.Main.Screens.PickQuantityInit
import com.example.university.View.Main.Screens.PickQuantityScreen
import com.example.university.View.Main.Screens.SettingsScreen
import com.example.university.View.Main.Screens.TestInit

private const val TAG = "MainNavGraph"

@Composable
fun MainNavGraph(
    navController: NavHostController,
    onGoingToLogin: () -> Unit,
    onChangeColorScheme: () -> Unit,
    showErrorMessage: (String) -> Unit,
) {
    NavHost(
        navController = navController,
        startDestination = MainScreens.Main.route
    ) {
        composable(route = MainScreens.Main.route) {
            MainScreen(navController = navController, onGoingToLogin = onGoingToLogin)
        }
        composable(route = MainScreens.AddNew.route) {
            AddScreen(navController = navController)
        }
        composable(route = MainScreens.Settings.route) {
            SettingsScreen(
                showErrorMessage = showErrorMessage,
                navController = navController,
                onChangeColorScheme = onChangeColorScheme
            )
        }
        composable(route = MainScreens.PickQuantity.route + "/{date}") { backStackEntry ->
            PickQuantityInit(
                navController = navController,
                date = backStackEntry.arguments?.getString("date")!!
            )
        }
        composable(route = MainScreens.Test.route + "/{listId}") { backStackEntry ->
            Log.i(
                TAG,
                "Передаётся аргумент listId = ${backStackEntry.arguments?.getString("listId")}"
            )
            TestInit(
                navController = navController,
                listId = backStackEntry.arguments?.getString("listId")!!.toInt()
            )
        }
    }
}