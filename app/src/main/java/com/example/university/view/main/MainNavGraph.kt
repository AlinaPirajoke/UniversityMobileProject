package com.example.university.view.main

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.university.view.main.screens.AddScreen
import com.example.university.view.main.screens.EditInit
import com.example.university.view.main.screens.FutureTestsScreen
import com.example.university.view.main.screens.MainScreen
import com.example.university.view.main.screens.PickQuantityInit
import com.example.university.view.main.screens.PickWordScreen
import com.example.university.view.main.screens.RememberInit
import com.example.university.view.main.screens.SettingsScreen
import com.example.university.view.main.screens.TestInit
import com.example.university.view.main.screens.UserWordsScreen

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
        composable(route = MainScreens.PickWord.route) {
            PickWordScreen(
                navController = navController
            )
        }
        composable(route = MainScreens.FutureTests.route) {
            FutureTestsScreen(
                navController = navController
            )
        }
        composable(route = MainScreens.UserWords.route) {
            UserWordsScreen(
                navController = navController
            )
        }
        composable(route = MainScreens.Remember.route + "/{listId}") { backStackEntry ->
            Log.i(
                TAG,
                "Передаётся аргумент listId = ${backStackEntry.arguments?.getString("listId")}"
            )
            RememberInit(
                navController = navController,
                listId = backStackEntry.arguments?.getString("listId")!!.toInt()
            )
        }
        composable(route = MainScreens.Edit.route + "/{wordId}") { backStackEntry ->
            Log.i(
                TAG,
                "Передаётся аргумент wordId = ${backStackEntry.arguments?.getString("wordId")}"
            )
            EditInit(
                navController = navController,
                wordId = backStackEntry.arguments?.getString("wordId")!!.toInt()
            )
        }
    }
}