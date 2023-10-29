package com.example.university.View.Auth

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun AuthNavGraph(navController: NavHostController, context: AuthActivity) {
    NavHost(
        navController = navController,
        startDestination = AuthScreens.Login.route)
    {
        composable(route = AuthScreens.Login.route){
            loginInit(context = context, navController = navController)
        }
        composable(route = AuthScreens.Registration.route){
            registrationInit(context = context, navController = navController)
        }

    }
}