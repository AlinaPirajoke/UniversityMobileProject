package com.example.university.View.Main

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.university.View.Main.Screens.AddScreen
import com.example.university.View.Main.Screens.MainScreen
import com.example.university.View.Main.Screens.PickQuantityInit
import com.example.university.View.Main.Screens.PickQuantityScreen
import com.example.university.View.Main.Screens.SettingsScreen

@Composable
fun MainNavGraph(navController: NavHostController, context: MainActivity) {
    NavHost(
        navController = navController,
        startDestination = MainScreens.Main.route
    ) {
        composable(route = MainScreens.Main.route) {
            MainScreen(context = context, navController = navController)
        }
        composable(route = MainScreens.AddNew.route) {
            AddScreen(context = context, navController = navController)
        }
        composable(route = MainScreens.Settings.route) {
            SettingsScreen(context = context, navController = navController)
        }
        composable(route = MainScreens.PickQuantity.route + "/{date}") {backStackEntry ->
            PickQuantityInit(context = context, navController = navController, backStackEntry.arguments?.getString("date")!!)
        }
    }
}