package com.example.university.View.Auth

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.university.View.Auth.Screens.LoginScreen
import com.example.university.View.Auth.Screens.RegistrationScreen

@Composable
fun AuthNavGraph(
    navController: NavHostController,
    showErrorMessage: (String) -> Unit,
    onGoingToMain: () -> Unit,
) {
    NavHost(
        navController = navController,
        startDestination = AuthScreens.Login.route
    )
    {
        composable(route = AuthScreens.Login.route) {
            LoginScreen(
                showErrorMessage = showErrorMessage,
                onGoingToMain = onGoingToMain,
                navController = navController
            )
        }
        composable(route = AuthScreens.Registration.route) {
            RegistrationScreen(
                navController = navController,
                showErrorMessage = showErrorMessage,
                onGoingToMain = onGoingToMain,
            )
        }
    }
}