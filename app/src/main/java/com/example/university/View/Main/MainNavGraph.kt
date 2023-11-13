package com.example.university.View.Main

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.university.ViewModel.MainViewModel
import org.koin.androidx.compose.viewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

@Composable
fun MainNavGraph(navController: NavHostController, context: MainActivity) {
    NavHost(
    navController = navController,
    startDestination = MainScreens.Main.route
    )
    {
        composable(route = MainScreens.Main.route) {
            context.mainInit(context = context, navController = navController)
        }
        composable(route = MainScreens.AddNew.route) {
            context.addInit(context = context, navController = navController)
        }
        composable(route = MainScreens.Settings.route) {
            context.settingsInit(context = context, navController = navController)
        }
    }
}