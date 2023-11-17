package com.example.university.View.Auth

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.university.View.Auth.Screens.LoginScreen
import com.example.university.View.Auth.Screens.RegistrationScreen

@Composable
fun AuthNavGraph(navController: NavHostController, context: AuthActivity) {
    NavHost(
        navController = navController,
        startDestination = AuthScreens.Login.route
    )
    {
        composable(route = AuthScreens.Login.route) {
            LoginScreen(context = context, navController = navController)
        }
        composable(route = AuthScreens.Registration.route) {
            RegistrationScreen(context = context, navController = navController)
        }

    }
}